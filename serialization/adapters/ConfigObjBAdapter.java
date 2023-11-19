package serialization.adapters;

import objects.ObjB;
import objects.ObjC;

import java.lang.reflect.Field;
import java.util.List;

public class ConfigObjBAdapter extends ConfigObjAdapter<ObjB> {
    @Override
    protected List<Field> getFields() {
        return getFields(ObjB.class);
    }
}
