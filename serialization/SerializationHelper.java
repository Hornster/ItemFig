package serialization;

import com.google.gson.JsonObject;

import java.lang.reflect.Field;

public class SerializationHelper {
    public static void addProperty(Field field, JsonObject jsonObject, Object src) throws IllegalAccessException, Exception{
        var fieldType = field.getType();
        if (fieldType.equals(char.class)) {
            field.setAccessible(true);
            jsonObject.addProperty(field.getName(), field.getChar(src));
            field.setAccessible(false);
        }
        else if (fieldType.equals(int.class)) {
            field.setAccessible(true);
            jsonObject.addProperty(field.getName(), field.getInt(src));
            field.setAccessible(false);
        }
        else if (fieldType.equals(short.class)) {
            field.setAccessible(true);
            jsonObject.addProperty(field.getName(), field.getShort(src));
            field.setAccessible(false);
        }
        else if (fieldType.equals(float.class)) {
            field.setAccessible(true);
            jsonObject.addProperty(field.getName(), field.getFloat(src));
            field.setAccessible(false);
        }
        else if (fieldType.equals(Double.class)) {
            field.setAccessible(true);
            jsonObject.addProperty(field.getName(), field.getDouble(src));
            field.setAccessible(false);
        }
        else if (fieldType.equals(String.class)) {
            field.setAccessible(true);
            jsonObject.addProperty(field.getName(), (String)field.get(src));
            field.setAccessible(false);
        }
        else if (fieldType.equals(long.class)) {
            field.setAccessible(true);
            jsonObject.addProperty(field.getName(), field.getLong(src));
            field.setAccessible(false);
        }
        else if (fieldType.equals(boolean.class)) {
            field.setAccessible(true);
            jsonObject.addProperty(field.getName(), field.getBoolean(src));
            field.setAccessible(false);
        }
        else{
            throw new Exception("Unknown field type during serialization: " + fieldType.getTypeName());
        }
    }
}
