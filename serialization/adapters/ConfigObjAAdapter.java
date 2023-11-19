package serialization.adapters;

import objects.ObjA;
import objects.ObjB;

import java.lang.reflect.Field;
import java.util.List;

public class ConfigObjAAdapter extends ConfigObjAdapter<ObjA>{
    @Override
    protected List<Field> getFields() {
        return getFields(ObjA.class);
    }
}
