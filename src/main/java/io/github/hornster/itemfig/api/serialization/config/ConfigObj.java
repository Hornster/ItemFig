package io.github.hornster.itemfig.api.serialization.config;

import java.lang.reflect.Type;
/**
 * Represents a single object class that you wish to save and read config for. Extend this class and override
 * the chkDefaultValues() method.
 * <br>
 * <br>
 * chkDefaultValues() should, as name implies, check if current values stored in an instance of extending
 * class are considered proper values. If not, it should assign or call for assignment of default values
 * of them. It is called after deserialization (config read) and before serialization (config save).
 * <br>
 * <br>
 * Use together with ConfigObjAdapter.
 * */
public abstract class ConfigObj extends io.github.hornster.itemfig.serialization.config.ConfigObj {

    /**
     * @param myType needed for auto deserialization.
     * @param myID   will be used to discern this config object from others during deserialization.
     *               It MUST be unique. Just like in MC itself.
     */
    public ConfigObj(Type myType, String myID) {
        super(myType, myID);
    }
    /**Checks if any fields were not assigned to during deserialization and
     * assigns default values to them.*/
    public abstract void chkDefaultValues();
}
