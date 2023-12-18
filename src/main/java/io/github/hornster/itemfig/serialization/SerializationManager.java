package io.github.hornster.itemfig.serialization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.hornster.itemfig.ItemFig;
import io.github.hornster.itemfig.serialization.common.constants.Constants;
import io.github.hornster.itemfig.api.serialization.config.ConfigObj;
import io.github.hornster.itemfig.serialization.config.ConfigObjAdapter;
import org.apache.commons.lang3.tuple.Pair;

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

public class SerializationManager {
    private final String _registeredObjectsFieldName = "registeredObjects";
    /**
     * Stores all objects that shall be de- and serialized.
     * The key is preferably the ID used in mod for given item, but can be something else as
     * long as it is unique, obviously..
     */
    private final Map<String, ConfigObj> _registeredObjects = new LinkedHashMap<>();
    private final Map<Type, ConfigObjAdapter<?>> _registeredObjectsAdapters = new HashMap<>();
    private final String _jsonPath = System.getProperty("user.dir") + File.separator + "config" + File.separator;
    private String _jsonFileName = "itemfig-mod-config";
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
     * DANGER OF LOSING DATA IF SET TO TRUE!
     * By default, false.
     */
    public boolean _forceConfigSave = false;
    /**
     * Update the config with default values and new items if any were added.
     * Will not work if deserialization errors occurred.
     * By default, true.
     */
    public boolean _updateConfigSave = true;
    /**
     * Wipe current config clean and recreate new one from default values.
     * By default, false.
     */
    public boolean _recreateConfig = false;

    /**
     * Registers a config object for reading and saving. The param type of the adapter
     * has to be the same as the one of registered object.
     * @param object Object to register.
     * @param adapter Adapter for the registered object that will be used to serialize and deserialize (write and read from and to config file) object data.
     */
    public void registerObject(ConfigObj object, ConfigObjAdapter<?> adapter) {
        _registeredObjects.put(object.getConfigObjId(), object);
        _registeredObjectsAdapters.put(object.getConfigObjType(), adapter);
    }

    /**
     * Registers multiple objects utilizing a list of pairs. The param type of the adapters
     * have to be the same as the ones of registered objects'
     * @param objects A list of config objects paired with their adapters to register. {@link #registerObject(ConfigObj, ConfigObjAdapter)} for details.
     */
    public void registerObjects(List<Pair<ConfigObj, ConfigObjAdapter<?>>> objects) {
        for (var obj : objects) {
            var configObj = obj.getKey();
            _registeredObjects.put(configObj.getConfigObjId(), configObj);
            _registeredObjectsAdapters.put(configObj.getConfigObjType(), obj.getValue());
        }
    }
    /**
     * If logging of a serialization (saving) error is not enough, you can
     * attach a handler here. Errors mean something went wrong and
     * something probably failed to be saved.
     * @param handler The string argument will contain the error message.*/
    public void registerSaveErrorHandler(Consumer<String> handler) {
        _configSaveErrHandler = handler;
    }
    /**
     * If logging of a serialization (saving) warning is not enough, you can
     * attach a handler here. Warnings are reported when something probably unwanted
     * happens, but does not cause errors or exceptions. An example would be a warning
     * about an empty file saving (no registered objects).
     * @param handler The string argument will contain the error message.*/
    public void registerSaveWarningHandler(Consumer<String> handler) {
        _configSaveWarnHandler = handler;
    }
    /**
     * If logging of a deserialization (loading) error is not enough, you can
     * attach a handler here. Errors mean something went wrong and
     * something probably failed to be loaded.
     * @param handler The string argument will contain the error message.*/
    public void registerReadErrorHandler(Consumer<String> handler) {
        _configReadErrHandler = handler;
    }
    /**
     * If logging of a deserialization (load) warning is not enough, you can
     * attach a handler here. Warnings are reported when something probably unwanted
     * happens, but does not cause errors or exceptions. An example would be a warning
     * about an empty file being read (no saved objects).
     * @param handler The string argument will contain the error message.*/
    public void registerReadWarningHandler(Consumer<String> handler) {
        _configReadWarnHandler = handler;
    }

    /**
     * Sets the name of the result json file. Extension is not needed.
     */
    public void setConfigFileName(String name) {
        _jsonFileName = name;
    }
    /**
     * Returns the name of the config file, with .json extension attached even if it was
     * not provided earlier upon changing the config name.*/
    public String getConfigFileName(){
        chkIfExtensionProvided();
        return _jsonFileName;
    }
    /**
     * Returns the absolute path to the folder where the config will be saved, without
     * the name of the config itself.*/
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
        ItemFig.LOGGER.error(msg);
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
        ItemFig.LOGGER.warn(msg);
    }

    private void reportSerializationWarning(String msg){
        if(_configSaveWarnHandler != null){
            _configSaveWarnHandler.accept(msg);
        }
        ItemFig.LOGGER.warn(msg);
    }
    private void reportSerializationError(String msg){
        if(_configSaveErrHandler != null){
            _configSaveErrHandler.accept(msg);
        }
        ItemFig.LOGGER.error(msg);
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
            reportDeserializationWarning(Constants.EMPTY_CONFIG_READ_WARN);
            return; //Nothing to do here, there is no config in the file. We will read default data and recreate
            //the config file with it.
        }

        var jsonRegisteredObjectsSet = jsonRegisteredObjects.asMap();

        if(jsonRegisteredObjectsSet.size() == 0){
            reportDeserializationWarning(Constants.EMPTY_CONFIG_READ_WARN);
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
     * Read config from the config file. If config is not found, it will be recreated using default values
     * of registered objects. If read config lacks data, it will be added automatically.
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
            reportSerializationWarning(Constants.EMPTY_CONFIG_SAVE_WARN);
        }

        var configPath = getConfigPath();
        new File(configPath).mkdirs();
        var fileWriter = new FileWriter(configPath + getConfigFileName());
        fileWriter.write(jsonData);
        fileWriter.close();
    }
}
