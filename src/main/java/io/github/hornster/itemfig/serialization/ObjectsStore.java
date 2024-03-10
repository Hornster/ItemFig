package io.github.hornster.itemfig.serialization;

import io.github.hornster.itemfig.api.serialization.config.ConfigObj;
import io.github.hornster.itemfig.api.serialization.config.ConfigObjAdapterConfig;
import io.github.hornster.itemfig.serialization.config.ConfigObjAdapter;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ObjectsStore {
    /**
     * Stores all objects that shall be de- and serialized.
     * The outer collections key is the filename that should contain the objects.
     * The inner collections key is preferably the ID used in mod for given item, but can be something else as
     * long as it is unique, obviously.
     */
    private final Map<String, FileObject> _registeredFiles = new LinkedHashMap<>();
    private final Map<Type, ConfigObjAdapter<?>> _registeredFilesAdapters = new HashMap<>();

    /**
     * Registers a config object for reading and saving. The param type of the adapter
     * has to be the same as the one of registered object.
     * @param fileName The name of the file the config object shall be saved to.
     * @param object Object to register.
     * @param adapter Adapter for the registered object that will be used to serialize and deserialize (write and read from and to config file) object data.
     */
    public void registerObject(String fileName, ConfigObj object, ConfigObjAdapterConfig<?> adapter) {
        if(!_registeredFiles.containsKey(fileName)){
            _registeredFiles.put(fileName, new FileObject(fileName));
        }

        var registeredObjectsForFile = _registeredFiles.get(fileName);
        registeredObjectsForFile.registerObject(object);

        var objAdapter = new ConfigObjAdapter<>(adapter);
        _registeredFilesAdapters.put(object.getConfigObjType(), objAdapter);
    }

    /**
     * Registers multiple objects utilizing a list of pairs. The param type of the adapters
     * have to be the same as the ones of registered objects'
     * @param objects A list of config objects paired with their adapters to register. {@link #registerObject(String, ConfigObj, ConfigObjAdapterConfig)} for details.
     */
    public void registerObjects(String fileName, List<Pair<ConfigObj, ConfigObjAdapterConfig<?>>> objects) {
        for (var obj : objects) {
            var configObj = obj.getKey();
            var configAdapter = obj.getValue();
            registerObject(fileName, configObj, configAdapter);
        }
    }
    public Map<String, FileObject> GetConfigFileObjects(){
        return _registeredFiles;
    }
    public FileObject GetConfigFileObject(String fileName){
        return _registeredFiles.get(fileName);
    }
    public Map<Type, ConfigObjAdapter<?>> GetAdapters(){
        return _registeredFilesAdapters;
    }
}
