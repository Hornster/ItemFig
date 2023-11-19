package serialization;

import com.google.gson.JsonObject;

import java.lang.reflect.Field;

public class SerializationHelper {
    public static void addProperty(Field field, JsonObject jsonObject, Object src) throws IllegalAccessException, Exception{
        var fieldType = field.getType();
        if (fieldType.equals(Character.class)) {
            field.setAccessible(true);
            jsonObject.addProperty(field.getName(), (Character) field.get(src));
            field.setAccessible(false);
        }
        else if (fieldType.equals(Integer.class)) {
            field.setAccessible(true);
            jsonObject.addProperty(field.getName(), (Integer)field.get(src));
            field.setAccessible(false);
        }
        else if (fieldType.equals(Short.class)) {
            field.setAccessible(true);
            jsonObject.addProperty(field.getName(), (Short)field.get(src));
            field.setAccessible(false);
        }
        else if (fieldType.equals(Float.class)) {
            field.setAccessible(true);
            jsonObject.addProperty(field.getName(), (Float)field.get(src));
            field.setAccessible(false);
        }
        else if (fieldType.equals(Double.class)) {
            field.setAccessible(true);
            jsonObject.addProperty(field.getName(), (Double)field.get(src));
            field.setAccessible(false);
        }
        else if (fieldType.equals(String.class)) {
            field.setAccessible(true);
            jsonObject.addProperty(field.getName(), (String)field.get(src));
            field.setAccessible(false);
        }
        else if (fieldType.equals(Long.class)) {
            field.setAccessible(true);
            jsonObject.addProperty(field.getName(), (Long)field.get(src));
            field.setAccessible(false);
        }
        else if (fieldType.equals(Boolean.class)) {
            field.setAccessible(true);
            jsonObject.addProperty(field.getName(), (Boolean)field.get(src));
            field.setAccessible(false);
        }
        else{
            throw new Exception("Unknown field type during serialization: " + fieldType.getTypeName());
        }
    }
}
