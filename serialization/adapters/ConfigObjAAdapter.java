package serialization.adapters;

import objects.ObjA;

import java.lang.reflect.Field;

public class ConfigObjAAdapter extends ConfigObjAdapter<ObjA>{
    @Override
    protected Field[] getFields() {
        return ObjA.class.getFields();
    }
}
