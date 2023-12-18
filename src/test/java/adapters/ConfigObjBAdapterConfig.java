package adapters;

import io.github.hornster.itemfig.api.serialization.config.ConfigObjAdapterConfig;
import io.github.hornster.itemfig.serialization.config.ConfigObjAdapter;
import objects.ObjB;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;

public class ConfigObjBAdapterConfig extends ConfigObjAdapterConfig<ObjB> {
    @Override
    public Class getConfigObjClass() {
        return ObjB.class;
    }

    @Override
    public Constructor<ObjB> getConstructorForDeserialization() throws NoSuchMethodException {
        return ObjB.class.getConstructor(String.class);
    }
}
