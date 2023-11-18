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
            try{
                SerializationHelper.addProperty(field, jsonObject, src);
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
