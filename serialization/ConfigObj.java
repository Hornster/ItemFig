package serialization;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.lang.reflect.Type;

public abstract class ConfigObj implements IConfigObj {
    protected Type _myType;
    protected String _myID;
    /**
     * @param myType needed for auto deserialization.
     * @param myID will be used to discern this config object from others during deserialization.
     * It MUST be unique.*/
    public ConfigObj(Type myType, String myID){
        _myType = myType;
        _myID = myID;
    }

    /**Returns the type of the config object class provided during creation. It will be used
     * by gson to determine what fields need to be read and assigned.*/
    protected Type getConfigObjType() {
        return _myType;
    }
    @Override
    public IConfigObj DeserializeConfigObj(Gson gson, JsonElement element) {
        var result = gson.fromJson(element, getConfigObjType());

        return (IConfigObj)result;
    }

    @Override
    public String SerializeConfigObj(Gson gson) {
        return gson.toJson(this);
    }

    @Override
    public String getConfigObjId() {
        return _myID;
    }
}
