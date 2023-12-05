package adapters;

import net.crazedaerialcable.itemfig.serialization.config.ConfigObjAdapter;
import objects.ObjC;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;

public class ConfigObjCAdapter extends ConfigObjAdapter<ObjC> {

    @Override
    protected List<Field> getFields() {
        return getFields(ObjC.class);
    }

    @Override
    protected Constructor<ObjC> getConstructorForDeserialization() throws NoSuchMethodException {
        return ObjC.class.getConstructor(String.class);
    }
}
