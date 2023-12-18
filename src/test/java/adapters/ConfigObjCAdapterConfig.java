package adapters;

import io.github.hornster.itemfig.api.serialization.config.ConfigObjAdapterConfig;
import objects.ObjC;

import java.lang.reflect.Constructor;

public class ConfigObjCAdapterConfig extends ConfigObjAdapterConfig<ObjC> {

    @Override
    public Class getConfigObjClass() {
        return ObjC.class;
    }

    @Override
    public Constructor<ObjC> getConstructorForDeserialization() throws NoSuchMethodException {
        return ObjC.class.getConstructor(String.class);
    }
}
