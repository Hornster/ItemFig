package serialization;

import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SerializationManager {
    /**Stores all objects that shall be de- and serialized.
     * The key is preferably the ID used in mod for given item, but can be something else as
     * long as it is unique, obviously..*/
    private static Map<String, ISerializedObj> _registeredObjects = new HashMap<>();
    private static String _jsonPath = "";
    private static String _jsonFileName;
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


    /**Sets the name of the result json file. Extension is not needed.*/
    public void setConfigFileName(String name){
        _jsonFileName = name;
    }

    public static void saveConfig () throws IOException {
        var gson = new Gson();
        var jsonData = gson.toJson(_registeredObjects);

        var fileWriter = new FileWriter(_jsonFileName);
        fileWriter.write(jsonData);
        fileWriter.close();
    }

    public static void readConfig(){

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
}
