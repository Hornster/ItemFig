package net.crazedaerialcable.itemfig.api.serialization.config;

import net.crazedaerialcable.itemfig.api.serialization.ItemFigApi;
import net.crazedaerialcable.itemfig.api.serialization.config.ConfigObj;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;

/**
 * A serialization/deserialization adapter for your configurable object. Needs to be provided along {@link ConfigObj}
 * to SerializationManager through {@link ItemFigApi}.
 * <br><br>
 * You need to provide implementations for {@link #getFields()} and {@link #getConstructorForDeserialization()}
 * which are, pretty much, necessary boilerplate code that needs a bit of adjusting.
 *
 * @param <T> The type of the config object this adapter will be working with.
 * */
public abstract class ConfigObjAdapter<T extends ConfigObj> extends net.crazedaerialcable.itemfig.serialization.config.ConfigObjAdapter<T> {
    /**
     * Used to retrieve the config objects class fields.
     * <br><br>
     * Example: return getFields(ConfigObjClassHere.class);
     * <br><br>
     * */
    protected abstract List<Field> getFields();

    /**
     * Used to retrieve the constructor of config object which the adapter takes care of.
     * The constructor needs to accept a single String type for the ID, as in, item ID.
     * <br><br>
     * Example: return ConfigObjClassHere.class.getConstructor(String.class);*/
    protected abstract Constructor<T> getConstructorForDeserialization() throws NoSuchMethodException;
}
