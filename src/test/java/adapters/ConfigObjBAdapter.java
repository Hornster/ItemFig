package adapters;

import io.github.hornster.itemfig.serialization.config.ConfigObjAdapter;
import objects.ObjB;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;

public class ConfigObjBAdapter extends ConfigObjAdapter<ObjB> {
    @Override
    protected List<Field> getFields() {
        return getFields(ObjB.class);
    }

    @Override
    protected Constructor<ObjB> getConstructorForDeserialization() throws NoSuchMethodException {
        return ObjB.class.getConstructor(String.class);
    }
}
