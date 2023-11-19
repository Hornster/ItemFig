package serialization.adapters;

import objects.ObjC;

import java.lang.reflect.Field;
import java.util.List;

public class ConfigObjCAdapter extends ConfigObjAdapter<ObjC>{

    @Override
    protected List<Field> getFields() {
        return getFields(ObjC.class);
    }
}
