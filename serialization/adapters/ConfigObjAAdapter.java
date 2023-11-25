package serialization.adapters;

import objects.ObjA;
import objects.ObjB;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;

public class ConfigObjAAdapter extends ConfigObjAdapter<ObjA>{
    @Override
    protected List<Field> getFields() {
        return getFields(ObjA.class);
    }

    @Override
    protected Constructor<ObjA> getConstructorForDeserialization() throws NoSuchMethodException {
        return ObjA.class.getConstructor(String.class);
    }
}
