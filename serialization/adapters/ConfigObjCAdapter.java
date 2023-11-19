package serialization.adapters;

import objects.ObjC;

import java.lang.reflect.Field;

public class ConfigObjCAdapter extends ConfigObjAdapter<ObjC>{
    @Override
    protected Field[] getFields() {
        return ObjC.class.getFields();
    }
}
