package io.github.hornster.itemfig.serialization.config;

import com.google.gson.*;
import io.github.hornster.itemfig.ItemFig;
import io.github.hornster.itemfig.serialization.SerializationHelper;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static io.github.hornster.itemfig.serialization.common.constants.Constants.ID_FIELD_NAME;


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
                var errMsg = "Could not access the field " + field.getName() + " during serialization! " + ex.getMessage();
                System.out.println(errMsg);
                ItemFig.LOGGER.error(errMsg);
            }
            catch (Exception ex){
                System.out.println(ex.getMessage());
                ItemFig.LOGGER.error(ex.getMessage());
            }
        }
        return jsonObject;
    }
    protected abstract List<Field> getFields();
    /**
     * Used to retrieve the constructor of config object which the adapter takes care of.
     * The constructor needs to accept a single String type for the ID, as in, item ID.
     * Example: return ConfigObjClassHere.class.getConstructor(String.class);*/
    protected abstract Constructor<T> getConstructorForDeserialization() throws NoSuchMethodException;
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

        Constructor<T> cons = null;
        var myId = jsonObject.getAsJsonPrimitive(ID_FIELD_NAME).getAsString();
        try{
            cons = getConstructorForDeserialization();//obj ID as param. The class MUST have constructor with one string  (for the id) arg!
        }
        catch(NoSuchMethodException ex){
            var errMsg = "Constructor for class" + type.getTypeName()
                    + "not found! Config type has to have a public constructor that accepts one string type!"
                    + " Additional info: " + ex.getMessage();
            System.out.println(errMsg);

            ItemFig.LOGGER.error(errMsg);
            return null;
        }

        T configObject = null;

        try{
            configObject = cons.newInstance(myId);
        }
        catch(Exception ex){
            var errMsg = "Could not instantiate object of type " + type.getTypeName() + "! The constructor that has" +
                    "single String type must be public! Additional info: " + ex.getMessage();
            System.out.println(errMsg);
            ItemFig.LOGGER.error(errMsg);
            return null;
        }

        configObject.chkDefaultValues();

        for(var field : fields){
            try{
                //We do not want a type field here, nor the ID one. ID was already assigned a moment ago.
                if(!field.getType().equals(Type.class)){//_myID field is serialized already above
                    SerializationHelper.readProperty(field, jsonObject, configObject);
                }
            }
            catch(IllegalAccessException ex){
                var errMsg = "Could not access the field " + field.getName() + " during serialization! " + ex.getMessage();
                System.out.println(errMsg);
                ItemFig.LOGGER.error(errMsg);
            }
            catch (Exception ex){
                System.out.println(ex.getMessage());
                ItemFig.LOGGER.error(ex.getMessage());
            }
        }

        return configObject;
    }
}
