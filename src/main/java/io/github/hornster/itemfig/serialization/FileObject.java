package io.github.hornster.itemfig.serialization;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import io.github.hornster.itemfig.api.serialization.config.ConfigObj;
import io.github.hornster.itemfig.serialization.common.constants.Constants;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

import static io.github.hornster.itemfig.serialization.common.constants.Constants.JSON_EXTENSION;

public class FileObject {
    public String FileName;
    protected final String _registeredObjectsFieldName = "registeredObjects";
    /**
     * Stores all objects that shall be de- and serialized.
     * The outer collections key is the filename that should contain the objects.
     * The inner collections key is preferably the ID used in mod for given item, but can be something else as
     * long as it is unique, obviously.
     */
    protected final Map<String, ConfigObj> _registeredObjects = new LinkedHashMap<>();

    public FileObject(String fileName){
        FileName = fileName;
    }
    /**
     * Registers a config object for reading and saving. The param type of the adapter
     * has to be the same as the one of registered object.
     * @param object Object to register.
     */
    public void registerObject(ConfigObj object) {
        _registeredObjects.put(object.getConfigObjId(), object);
    }
    public void deserializeFile(Gson gson, String jsonString, Consumer<String> reportDeserializationError, Consumer<String> reportDeserializationWarning) {
        var jsonConfigElement = gson.fromJson(jsonString, JsonElement.class);
        if (!jsonConfigElement.isJsonObject()) {
            reportDeserializationError.accept("Base json element is not a json object!");
        }
        var jsonConfigObj = jsonConfigElement.getAsJsonObject();
        var jsonRegisteredObjects = jsonConfigObj.getAsJsonObject(_registeredObjectsFieldName);

        if (jsonRegisteredObjects == null) {
            reportDeserializationWarning.accept(Constants.EMPTY_CONFIG_READ_WARN);
            return; //Nothing to do here, there is no config in the file. We will read default data and recreate
            //the config file with it.
        }

        var jsonRegisteredObjectsSet = SerializationHelper.getAsMap(jsonRegisteredObjects);//jsonRegisteredObjects.asMap();

        if(jsonRegisteredObjectsSet.size() == 0){
            reportDeserializationWarning.accept(Constants.EMPTY_CONFIG_READ_WARN);
            return; //Nothing to do here, there is no config in the file. We will read default data and recreate
            //the config file with it.
        }

        var keys = jsonRegisteredObjectsSet.keySet();

        for (var key : keys) {
            var jsonElement = jsonRegisteredObjectsSet.get(key);
            if (!jsonElement.isJsonObject()) {
                reportDeserializationWarning.accept("Element with key " + key + " is not a valid json object! Skipping.");
                continue;
            }

            if (!_registeredObjects.containsKey(key)) {
                reportDeserializationWarning.accept("Element with key " + key + " does not have a registered data object! Skipping.");
                continue;
            }
            var registeredObject = _registeredObjects.get(key);
            var deserializedConfigObj = registeredObject.DeserializeConfigObj(gson, jsonElement);
            _registeredObjects.replace(key, deserializedConfigObj);
        }
    }

    public void chkDefaultValues() {
        var registeredObjectsCollection = _registeredObjects.values();
        for (var registeredObj : registeredObjectsCollection) {
            registeredObj.chkDefaultValues();
        }
    }

    public Map<String, JsonElement> serializeObjData(Gson gson){
        var configMapElement = gson.toJsonTree(new HashMap<String, ConfigObj>());
        var configMapJsonObj = configMapElement.getAsJsonObject();
        var configMapObj = SerializationHelper.getAsMap(configMapJsonObj);//configMapElement.getAsJsonObject().asMap();

        _registeredObjects.forEach((key, configObj) -> {
            var serializedObj = gson.toJsonTree(configObj);
            configMapObj.put(key, serializedObj);
        });

        return configMapObj;
    }

    public ConfigObj GetConfigObj(String itemId){
        return _registeredObjects.get(itemId);
    }
}
