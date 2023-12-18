package adapters;

import io.github.hornster.itemfig.api.serialization.config.ConfigObjAdapterConfig;
import objects.ObjA;

import java.lang.reflect.Constructor;

public class ConfigObjAAdapterConfig extends ConfigObjAdapterConfig<ObjA> {

    @Override
    public Class getConfigObjClass() {
        return ObjA.class;
    }

    @Override
    public Constructor<ObjA> getConstructorForDeserialization() throws NoSuchMethodException {
        return ObjA.class.getConstructor(String.class);
    }
}
