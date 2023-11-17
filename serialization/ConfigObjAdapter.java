package serialization;

import com.google.gson.*;
import objects.ObjC;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

public abstract class ConfigObjAdapter<T extends IConfigObj> implements JsonSerializer<T>, JsonDeserializer<T> {
    @Override
    public JsonElement serialize(T src, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        //jsonObject.addProperty("type", src.getClass().getName());
        var fields = getFields();
        //jsonObject.add("data", jsonSerializationContext.serialize(src));
        for(var field : fields){
            var fieldType = field.getType();
            try{
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
            catch(IllegalAccessException ex){
                System.out.println("Could not access the field " + field.getName() + " during serialization! " + ex.getMessage());
                //TODO add mod logger!
            }
            catch (Exception ex){
                System.out.println(ex.getMessage());
                //TODO add mod logger!
            }
        }
        return jsonObject;
    }

    protected abstract Field[] getFields();

    @Override
    public T deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        var fields = getFields();

        String className = jsonObject.get("type").getAsString();
        JsonElement jsonElement = jsonObject.get("data");
        try {
            Class<?> classType = Class.forName(className);
            return jsonDeserializationContext.deserialize(jsonElement, classType);
        } catch (ClassNotFoundException e) {
            throw new JsonParseException("Unknown element type: " + className, e);
        }
    }
}
