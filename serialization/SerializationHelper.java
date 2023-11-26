package serialization;

import com.google.gson.JsonElement;
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

    public static void readProperty(Field field, JsonObject jsonObject, Object dest) throws Exception {
        var fieldType = field.getType();
        var jsonFieldElement = jsonObject.get(field.getName());

        if(jsonFieldElement == null){
            //TODO Add LOGGER WARN/INFO
            //We ignore simply non-existent fields in config. They will get their default values.
            return;
        }
        if(!jsonFieldElement.isJsonPrimitive()){
            throw new Exception("Objects as config fields are not supported at the moment!");
        }
        var jsonFieldVal = jsonFieldElement.getAsJsonPrimitive();
        var wasAccessible = field.canAccess(dest);

        field.setAccessible(true);

        if (fieldType.equals(Character.class)) {
            var val = jsonFieldVal.getAsString();
            if(val != null){
                field.set(dest, val.charAt(0));
            }
        }
        else if (fieldType.equals(Integer.class)) {
            field.set(dest, jsonFieldVal.getAsInt());
        }
        else if (fieldType.equals(Short.class)) {
            field.set(dest, jsonFieldVal.getAsShort());
        }
        else if (fieldType.equals(Float.class)) {
            var val = jsonFieldVal.getAsFloat();
            field.set(dest, val);
        }
        else if (fieldType.equals(Double.class)) {
            field.set(dest, jsonFieldVal.getAsDouble());
        }
        else if (fieldType.equals(String.class)) {
            field.set(dest, jsonFieldVal.getAsString());
        }
        else if (fieldType.equals(Long.class)) {
            field.set(dest, jsonFieldVal.getAsLong());
        }
        else if (fieldType.equals(Boolean.class)) {
            field.set(dest, jsonFieldVal.getAsBoolean());
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
