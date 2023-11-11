package serialization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class SerializationManager {
    /**Stores all objects that shall be de- and serialized.
     * The key is preferably the ID used in mod for given item, but can be something else as
     * long as it is unique, obviously..*/
    private static Map<String, ISerializedObj> _registeredObjects = new HashMap<>();
    private static final String _jsonPath = "config" + File.pathSeparator;
    private static String _jsonFileName;
    private static final String _jsonExtension = ".json";

    private static Consumer<String> _configSaveErrHandler;
    private static Consumer<String> _configReadErrHandler;

    /**Set to TRUE if there was a deserialization error that made it impossible to read
     * the config whole.*/
    private static boolean _jsonDeserializationError = false;
    /**Force saving of the config no matter if any deserialization errors occurred.*/
    private static boolean _forceConfigSave = false;
    private static boolean _recreateConfig = false;

    /**Registers a data object for serialization.*/
    public static void registerObject(ISerializedObj object){
        _registeredObjects.put(object.getId(), object);
    }
    /**Registers multiple data objects for serialization.*/
    public static void registerObjects(List<ISerializedObj> objects){
        for(var obj : objects){
            _registeredObjects.put(obj.getId(), obj);
        }
    }
    public static void registerSaveErrorHandler(Consumer<String> handler){
        _configSaveErrHandler = handler;
    }
    public static void registerReadErrorHandler(Consumer<String> handler){
        _configReadErrHandler = handler;
    }

    /**Sets the name of the result json file. Extension is not needed.*/
    public static void setConfigFileName(String name){
        _jsonFileName = name;
    }
    private static void reportError(Consumer<String> handler, String msg){
        if(handler != null){
            handler.accept(msg);
        }
    }
    /**Reports a critical error during deserialization.*/
    private static void reportDeserializationError(String msg){
        _jsonDeserializationError = true;
        _configReadErrHandler.accept(msg);
    }
    /**Reports an error during deserialization that does not cause the entire process to fall from bicycle
     * and crash violently on the pavement below. Example would be inability to read a value or lack of value
     * despite key being present.*/
    private static void reportDeserializationWarning(String msg){
        _configReadErrHandler.accept(msg);
    }
    private static void chkIfExtensionProvided(){
        if(!_jsonFileName.contains(_jsonExtension)){
            _jsonFileName = _jsonFileName + _jsonExtension;
        }
    }
    private static void deserializeFile(Gson gson, String jsonString){
        var jsonConfigElement = gson.fromJson(jsonString, JsonElement.class);
        if(!jsonConfigElement.isJsonObject()){
            reportDeserializationError("Base json element is not a json object!");
        }
        var jsonConfigObj = jsonConfigElement.getAsJsonObject();
        var jsonRegisteredObjects = jsonConfigObj.getAsJsonObject("_registeredObjects");

        if(jsonRegisteredObjects == null){
            return; //Nothing to do here, there is no config in the file. We will read default data and recreate
                    //the config file with it.
        }

        var jsonRegisteredObjectsSet = jsonRegisteredObjects.asMap();
        var keys = jsonRegisteredObjectsSet.keySet();
        for(var key : keys){
            var jsonElement = jsonRegisteredObjectsSet.get(key);
            if(!jsonElement.isJsonObject())
            {
                reportDeserializationWarning("Element with key " + key + " is not a valid json object! Skipping.");
                continue;
            }

            if(_registeredObjects.containsKey(key)){
                reportDeserializationWarning("Element with key " + key + " does not have a registered data object! Skipping.");
            }
            var registeredObject = _registeredObjects.get(key);
            registeredObject.Deserialize(gson, jsonElement);
        }
    }
    private static void chkDefaultValues(){
        var registeredObjectsCollection = _registeredObjects.values();
        for(var registeredObj : registeredObjectsCollection){
            registeredObj.ChkDefaultValues();;
        }
    }
    private static void recreateConfig(Gson gson){
        chkDefaultValues();
        var path = Paths.get(_jsonPath, _jsonFileName);
        try{
            Files.delete(path);
        }
        catch(NoSuchFileException ex){
            reportDeserializationWarning("Config file not found. No need to delete.");
        }
        catch (IOException ex){
            reportDeserializationWarning(ex.getMessage());
        }

        saveConfig();
    }
    public static void readConfig(){
        chkIfExtensionProvided();
        _jsonDeserializationError = false;
        var gsonObj = new GsonBuilder().setPrettyPrinting().create();

        if(_recreateConfig){
            recreateConfig(gsonObj);
            return;
        }

        String configStr = "";

        try{
            configStr = readFile(_jsonFileName);
            deserializeFile(gsonObj, configStr);
        }
        catch(IOException ex){
            if(_configReadErrHandler != null){
                _configReadErrHandler.accept(ex.getMessage());
            }
        }

        chkDefaultValues();


        if(_forceConfigSave){
            saveConfig();
        }
    }
    /**Get the config for provided ID. Should be called after saveConfig method.
     * Returned object can be cast to whatever type it was upon being registered.*/
    public static ISerializedObj getItemConfig(String itemId){
        var object = _registeredObjects.get(itemId);
        return object;
    }

    /**Get the config for provided ID. Should be called after saveConfig method.
     * Automatically casts returned config object to provided type.*/
    public static <T> T getItemConfigAutoCast(String itemId){
        var object = _registeredObjects.get(itemId);
        return (T)object;
    }

    private static String readFile(String fileName) throws IOException {
        var result = "";
        var path = Paths.get(fileName);
        var readLines = Files.readAllLines(path);

        result = new StringBuilder().append(readLines).toString();

        return result;
    }
    private static void saveConfig(){
        try{
            saveConfigPerform();
        }
        catch(IOException ex){
            if(_configSaveErrHandler != null){
                _configSaveErrHandler.accept(ex.getMessage());
            }
        }
    }
    private static void saveConfigPerform() throws IOException {
        var gson = new Gson();
        var jsonData = gson.toJson(_registeredObjects);

        var fileWriter = new FileWriter(_jsonFileName);
        fileWriter.write(jsonData);
        fileWriter.close();
    }
}
