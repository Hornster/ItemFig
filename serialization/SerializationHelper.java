package serialization;

import com.google.gson.JsonObject;

import java.lang.reflect.Field;

public class SerializationHelper {
    public static void addProperty(Field field, JsonObject jsonObject, Object src) throws IllegalAccessException, Exception{
        var fieldType = field.getType();
        var wasAccessible = field.canAccess(src);
        field.setAccessible(true);
        if (fieldType.equals(Character.class)) {
            jsonObject.addProperty(field.getName(), (Character) field.get(src));
        }
        else if (fieldType.equals(Integer.class)) {
            jsonObject.addProperty(field.getName(), (Integer)field.get(src));
        }
        else if (fieldType.equals(Short.class)) {
            jsonObject.addProperty(field.getName(), (Short)field.get(src));
        }
        else if (fieldType.equals(Float.class)) {
            jsonObject.addProperty(field.getName(), (Float)field.get(src));
        }
        else if (fieldType.equals(Double.class)) {
            jsonObject.addProperty(field.getName(), (Double)field.get(src));
        }
        else if (fieldType.equals(String.class)) {
            jsonObject.addProperty(field.getName(), (String)field.get(src));
        }
        else if (fieldType.equals(Long.class)) {
            jsonObject.addProperty(field.getName(), (Long)field.get(src));
        }
        else if (fieldType.equals(Boolean.class)) {
            jsonObject.addProperty(field.getName(), (Boolean)field.get(src));
        }
        else{
            if(!wasAccessible){
                field.setAccessible(false);
            }
            throw new Exception("Unknown field type during serialization: " + fieldType.getTypeName());
        }
        if(!wasAccessible){
            field.setAccessible(false);
        }
    }
}
