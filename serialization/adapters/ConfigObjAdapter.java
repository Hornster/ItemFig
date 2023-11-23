package serialization.adapters;

import com.google.gson.*;
import objects.ObjC;
import serialization.ConfigObj;
import serialization.Constants;
import serialization.SerializationHelper;

import java.io.ObjectInputFilter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static serialization.Constants.ID_FIELD_NAME;

public abstract class ConfigObjAdapter<T extends ConfigObj> implements JsonSerializer<T>, JsonDeserializer<T> {
    @Override
    public JsonElement serialize(T src, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        //jsonObject.addProperty("type", src.getClass().getName());
        var fields = getFields();
        //TODO change save and tests: the keys for objects should be item ids.
        //TODO so instead "ObjC": there should be "ItemId":
        //TODO will probably require changing tests too
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
        JsonObject jsonObject = json.getAsJsonObject();
        var fields = getFields();

        if(!type.equals(ConfigObj.class)){
            System.out.println("Provided for deserialization type " + type.getTypeName() + "is not of " + ConfigObj.class.getTypeName() + " type!");
            //TODO Add LOGGER
            return null;
        }

        var c = (Class<?>)type.getClass();
        Constructor<ConfigObj> cons = null;
        var myId = jsonObject.getAsJsonPrimitive(ID_FIELD_NAME).getAsString();
        try{
            cons = c.getConstructor(String.class);//obj ID as param. The class MUST have constructor with one string  (for the id) arg!
        }
        catch(NoSuchMethodException ex){
            System.out.println("Constructor for class" + c.getTypeName()
                    + "not found! Config type has to have a public constructor that accepts one string type!"
                    + " Additional info: " + ex.getMessage()
            );

            //TODO Add LOGGER
            return null;
        }

        Object configObject = null;

        try{
            configObject = cons.newInstance(myId);
        }
        catch(Exception ex){
            System.out.println("Could not instantiate object of type " + c.getTypeName() + "! The constructor that has" +
                    "single String type must be public! Additional info: " + ex.getMessage());
            return null;
        }


        for(var field : fields){
            try{
                if(!field.getType().equals(Type.class) && !field.getName().equals(ID_FIELD_NAME)){//_myID field is serialized already above
                    SerializationHelper.readProperty(field, jsonObject, configObject);
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
