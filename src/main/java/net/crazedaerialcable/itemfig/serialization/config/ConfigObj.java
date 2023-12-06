package net.crazedaerialcable.itemfig.serialization.config;

import com.google.gson.*;

import java.lang.reflect.Type;

public abstract class ConfigObj {
    protected Type _myType;
    protected String _myID;
    /**
     * @param myType needed for auto deserialization.
     * @param myID will be used to discern this config object from others during deserialization.
     * It MUST be unique. Just like in MC itself.*/
    public ConfigObj(Type myType, String myID){
        _myType = myType;
        _myID = myID;
    }

    /**Checks if any fields were not assigned to during deserialization and
     * assigns default values to them.*/
    public abstract void chkDefaultValues();
    //@Override
    public Type getConfigObjType() {

        return _myType;
    }
    //@Override
    public ConfigObj DeserializeConfigObj(Gson gson, JsonElement element) {
        var result = gson.fromJson(element, getConfigObjType());

        return (ConfigObj)result;
    }

    //@Override
    public String SerializeConfigObj(Gson gson) {
        return gson.toJson(this);
    }

    //@Override
    public String getConfigObjId() {
        return _myID;
    }


}
