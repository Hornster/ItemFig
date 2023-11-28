package serialization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import objects.ObjC;
import org.testng.internal.collections.Pair;
import serialization.adapters.ConfigObjAdapter;
import serialization.adapters.ConfigObjCAdapter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static serialization.Constants.EMPTY_CONFIG_READ_WARN;
import static serialization.Constants.EMPTY_CONFIG_SAVE_WARN;

public class SerializationManager {
    private final String _registeredObjectsFieldName = "registeredObjects";
    /**
     * Stores all objects that shall be de- and serialized.
     * The key is preferably the ID used in mod for given item, but can be something else as
     * long as it is unique, obviously..
     */
    private Map<String, ConfigObj> _registeredObjects = new LinkedHashMap<>();
    private Map<Type, ConfigObjAdapter<?>> _registeredObjectsAdapters = new HashMap<>();
    private final String _jsonPath = System.getProperty("user.dir") + File.separator + "config" + File.separator;
    private String _jsonFileName = "mod-config";
    private final String _jsonExtension = ".json";

    private Consumer<String> _configSaveErrHandler;
    private Consumer<String> _configSaveWarnHandler;
    private Consumer<String> _configReadErrHandler;
    private Consumer<String> _configReadWarnHandler;

    /**
     * Set to TRUE if there was a deserialization error that made it impossible to read
     * the config whole.
     */
    private boolean _jsonDeserializationError = false;
    /**
     * Force saving of the config no matter if any deserialization errors occurred.
     */
    public boolean _forceConfigSave = false;
    /**
     * Update the config with default values and new items if any were added.
     * Will not work if deserialization errors occurred.
     */
    public boolean _updateConfigSave = true;
    /**
     * Wipe current config clean and recreate new one from default values.
     */
    public boolean _recreateConfig = false;

    /**
     * Registers a data object for serialization.
     */
    public void registerObject(ConfigObj object, ConfigObjAdapter<?> adapter) {
        _registeredObjects.put(object.getConfigObjId(), object);
        _registeredObjectsAdapters.put(object.getConfigObjType(), adapter);
    }

    /**
     * Registers multiple data objects for serialization.
     */
    public void registerObjects(List<Pair<ConfigObj, ConfigObjAdapter<?>>> objects) {
        for (var obj : objects) {
            var configObj = obj.first();
            _registeredObjects.put(configObj.getConfigObjId(), configObj);
            _registeredObjectsAdapters.put(configObj.getConfigObjType(), obj.second());
        }
    }

    public void registerSaveErrorHandler(Consumer<String> handler) {
        _configSaveErrHandler = handler;
    }
    public void registerSaveWarningHandler(Consumer<String> handler) {
        _configSaveWarnHandler = handler;
    }

    public void registerReadErrorHandler(Consumer<String> handler) {
        _configReadErrHandler = handler;
    }
    public void registerReadWarningHandler(Consumer<String> handler) {
        _configReadWarnHandler = handler;
    }

    /**
     * Sets the name of the result json file. Extension is not needed.
     */
    public void setConfigFileName(String name) {
        _jsonFileName = name;
    }
    public String getConfigFileName(){
        chkIfExtensionProvided();
        return _jsonFileName;
    }
    public String getConfigPath(){
        chkIfExtensionProvided();
        return _jsonPath;
    }

    private void reportError(Consumer<String> handler, String msg) {
        if (handler != null) {
            handler.accept(msg);
        }
    }

    /**
     * Reports a critical error during deserialization.
     */
    private void reportDeserializationError(String msg) {
        _jsonDeserializationError = true;
        if(_configReadErrHandler != null){
            _configReadErrHandler.accept(msg);
        }
        //TODO report to LOGGER here
    }

    /**
     * Reports an error during deserialization that does not cause the entire process to fall from bicycle
     * and crash violently on the pavement below. Example would be inability to read a value or lack of value
     * despite key being present.
     */
    private void reportDeserializationWarning(String msg) {
        if(_configReadWarnHandler != null){
            _configReadWarnHandler.accept(msg);
        }
        //TODO report to LOGGER here
    }

    private void reportSerializationWarning(String msg){
        if(_configSaveWarnHandler != null){
            _configSaveWarnHandler.accept(msg);
        }
        //TODO report to LOGGER here
    }
    private void reportSerializationError(String msg){
        if(_configSaveErrHandler != null){
            _configSaveErrHandler.accept(msg);
        }
        //TODO report to LOGGER here
    }

    private void chkIfExtensionProvided() {
        if (!_jsonFileName.contains(_jsonExtension)) {
            _jsonFileName = _jsonFileName + _jsonExtension;
        }
    }

    private void deserializeFile(Gson gson, String jsonString) {
        var jsonConfigElement = gson.fromJson(jsonString, JsonElement.class);
        if (!jsonConfigElement.isJsonObject()) {
            reportDeserializationError("Base json element is not a json object!");
        }
        var jsonConfigObj = jsonConfigElement.getAsJsonObject();
        var jsonRegisteredObjects = jsonConfigObj.getAsJsonObject(_registeredObjectsFieldName);

        if (jsonRegisteredObjects == null) {
            reportDeserializationWarning(EMPTY_CONFIG_READ_WARN);
            return; //Nothing to do here, there is no config in the file. We will read default data and recreate
            //the config file with it.
        }

        var jsonRegisteredObjectsSet = jsonRegisteredObjects.asMap();

        if(jsonRegisteredObjectsSet.size() == 0){
            reportDeserializationWarning(EMPTY_CONFIG_READ_WARN);
            return; //Nothing to do here, there is no config in the file. We will read default data and recreate
            //the config file with it.
        }

        var keys = jsonRegisteredObjectsSet.keySet();

        for (var key : keys) {
            var jsonElement = jsonRegisteredObjectsSet.get(key);
            if (!jsonElement.isJsonObject()) {
                reportDeserializationWarning("Element with key " + key + " is not a valid json object! Skipping.");
                continue;
            }

            if (!_registeredObjects.containsKey(key)) {
                reportDeserializationWarning("Element with key " + key + " does not have a registered data object! Skipping.");
                continue;
            }
            var registeredObject = _registeredObjects.get(key);
            var deserializedConfigObj = registeredObject.DeserializeConfigObj(gson, jsonElement);
            _registeredObjects.replace(key, deserializedConfigObj);
        }
    }

    private void chkDefaultValues() {
        var registeredObjectsCollection = _registeredObjects.values();
        for (var registeredObj : registeredObjectsCollection) {
            registeredObj.chkDefaultValues();
        }
    }

    /**
     * Recreates the config file using provided defaults.
     */
    private void recreateConfig(Gson gson) {
        chkDefaultValues();
        var path = Paths.get(getConfigPath()+getConfigFileName());
        try {
            Files.delete(path);
        } catch (NoSuchFileException ex) {
            //reportSerializationWarning("Config file not found. No need to delete.");
        } catch (IOException ex) {
            reportSerializationError(ex.getMessage());
        }

        saveConfig();
    }

    /**
     * Read config from the config file. If recreateConfig flag has been set, the current config, if present,
     * will be deleted and new one will be created in its place.
     */
    public void readConfig() {
        chkIfExtensionProvided();
        _jsonDeserializationError = false;
        var gsonObj = getGson();

        String configStr = "";

        chkDefaultValues();

        if (_recreateConfig) {
            recreateConfig(gsonObj);
            return;
        }

        try {
            var filePath = getConfigPath() + getConfigFileName();
            configStr = readFile(filePath);
            deserializeFile(gsonObj, configStr);
        } catch (IOException ex) {
            if (_configReadErrHandler != null) {
                _configReadErrHandler.accept(ex.getMessage());
            }
        }

        if (_forceConfigSave
                || (_updateConfigSave && !_jsonDeserializationError)) {
            saveConfig();
        }
    }

    /**
     * Get the config for provided ID. Should be called after saveConfig method.
     * Returned object can be cast to whatever type it was upon being registered.
     *
     * @param itemId The ID unique of the config object to retrieve.*
     * @return Object described by provided itemId or null if none present.
     */

    public ConfigObj getItemConfig(String itemId) {
        var object = _registeredObjects.get(itemId);
        return object;
    }

    /**
     * Gets the config for provided ID. Should be called after saveConfig method.
     *
     * @param itemId ID of the registered item used to find the item.
     * @return Automatically casts returned config object to provided type.
     */
    public <T extends ConfigObj> T  getItemConfigAutoCast(String itemId) {
        var object = _registeredObjects.get(itemId);
        return (T) object;
    }

    private String readFile(String fileName) throws IOException {
        var result = "";
        var path = Paths.get(fileName);
        var readLines = Files.readAllLines(path);
        var sb = new StringBuilder();

        for(var readLine : readLines){
            sb.append(readLine);
        }
        result = sb.toString();

        return result;
    }

    private void saveConfig() {
        try {
            saveConfigPerform();
        } catch (IOException ex) {
            if (_configSaveErrHandler != null) {
                _configSaveErrHandler.accept(ex.getMessage());
            }
        }
    }

    private Gson getGson() {
        var gsonBuilder = new GsonBuilder()
                .setPrettyPrinting();
        for(var adapterType : _registeredObjectsAdapters.keySet()){
            gsonBuilder.registerTypeAdapter(adapterType, _registeredObjectsAdapters.get(adapterType));
        }

         return gsonBuilder.create();
    }

    private void saveConfigPerform() throws IOException {
        var gson = getGson();

        var rootJsonObj = new JsonObject();
        var configMapElement = gson.toJsonTree(new HashMap<String, ConfigObj>());
        var configMapObj = configMapElement.getAsJsonObject().asMap();

        _registeredObjects.forEach((key, configObj) -> {
            var serializedObj = gson.toJsonTree(configObj);
            configMapObj.put(key, serializedObj);
        });

        var modifiedConfigMapJsonElement = gson.toJsonTree(configMapObj);
        rootJsonObj.add(_registeredObjectsFieldName, modifiedConfigMapJsonElement);

        var jsonData = gson.toJson(rootJsonObj);

        if(_registeredObjects.size() == 0){
            reportSerializationWarning(EMPTY_CONFIG_SAVE_WARN);
        }

        var configPath = getConfigPath();
        new File(configPath).mkdirs();
        var fileWriter = new FileWriter(configPath + getConfigFileName());
        fileWriter.write(jsonData);
        fileWriter.close();
    }
}
