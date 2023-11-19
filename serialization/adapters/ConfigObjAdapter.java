package serialization.adapters;

import com.google.gson.*;
import objects.ObjC;
import serialization.ConfigObj;
import serialization.SerializationHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class ConfigObjAdapter<T extends ConfigObj> implements JsonSerializer<T>, JsonDeserializer<T> {
    @Override
    public JsonElement serialize(T src, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        //jsonObject.addProperty("type", src.getClass().getName());
        var fields = getFields();
        //jsonObject.add("data", jsonSerializationContext.serialize(src));
        for(var field : fields){
            try{
                if(!field.getType().equals(Type.class)){
                    SerializationHelper.addProperty(field, jsonObject, src);
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
    protected abstract List<Field> getFields();
    protected List<Field> getFields(Class checkedClass){
        if(checkedClass == null){
            return Collections.emptyList();
        }

        var result = new ArrayList<>(getFields(checkedClass.getSuperclass()));
        var checkedClassFields = Arrays.stream(checkedClass.getDeclaredFields()).toList();
        result.addAll(checkedClassFields);

        return result;
    }

    @Override
    public T deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
//        JsonObject jsonObject = json.getAsJsonObject();
//        var fields = getFields(type.getClass());
//
//        String className = jsonObject.get("type").getAsString();
//        JsonElement jsonElement = jsonObject.get("data");
//        try {
//            Class<?> classType = Class.forName(className);
//            return jsonDeserializationContext.deserialize(jsonElement, classType);
//        } catch (ClassNotFoundException e) {
//            throw new JsonParseException("Unknown element type: " + className, e);
//        }
        return null;
    }
}
