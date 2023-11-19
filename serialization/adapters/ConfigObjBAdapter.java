package serialization.adapters;

import objects.ObjB;

import java.lang.reflect.Field;

public class ConfigObjBAdapter extends ConfigObjAdapter<ObjB> {
    @Override
    protected Field[] getFields() {
        return ObjB.class.getFields();
    }
}
