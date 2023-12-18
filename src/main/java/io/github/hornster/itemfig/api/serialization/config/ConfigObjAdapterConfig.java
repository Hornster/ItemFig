package io.github.hornster.itemfig.api.serialization.config;

import io.github.hornster.itemfig.api.serialization.ItemFigApi;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;

/**
 * A serialization/deserialization adapter for your configurable object. Needs to be provided along {@link ConfigObj}
 * to SerializationManager through {@link ItemFigApi}.
 * <br><br>
 * You need to provide implementations for {@link #getConfigObjClass()} and {@link #getConstructorForDeserialization()}
 * which are, pretty much, necessary boilerplate code that needs a bit of adjusting.
 *
 * @param <T> The type of the config object this adapter will be working with.
 * */
public abstract class ConfigObjAdapterConfig<T extends ConfigObj> {
    /**
     * Used to retrieve the objects true class type.
     * <br><br>
     * Example: return getFields(ConfigObjClassHere.class);
     * <br><br>
     * */
    public abstract Class getConfigObjClass();

    /**
     * Used to retrieve the constructor of config object which the adapter takes care of.
     * The constructor needs to accept a single String type for the ID, as in, item ID.
     * <br><br>
     * Example: return ConfigObjClassHere.class.getConstructor(String.class);*/
    public abstract Constructor<T> getConstructorForDeserialization() throws NoSuchMethodException;
}
