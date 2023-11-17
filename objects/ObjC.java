package objects;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import serialization.IConfigObj;

import java.io.IOException;
import java.lang.reflect.Type;

public class ObjC extends ObjCC implements IConfigObj {
    public String id;
    private final Type thisType = ObjC.class;
    public Integer paramC;

    public ObjC(String id, int paramC, float paramCC1, String paramCC2){
        this.paramC = paramC;
        this.paramCC1 = paramCC1;
        this.paramCC2 = paramCC2;
        this.id = id;
    }

    public ObjC(String id) {
        this.id = id;
    }

    @Override
    public void ChkDefaultValues() {
        if(paramC == null){
            paramC = DefaultVals.OBJC_PARAMC1;
        }
        if(paramCC1 == null){
            paramCC1 = DefaultVals.OBJCC_PARAMC1;
        }
        if(paramCC2 == null){
            paramCC2 = DefaultVals.OBJCC_PARAMC2;
        }
    }

    @Override
    public Type getConfigObjType() {
        return thisType;
    }

        @Override
    public IConfigObj DeserializeConfigObj(Gson gson, JsonElement element) {
        var deserializedObj = (ObjC)gson.fromJson(element,thisType);
        return deserializedObj;
    }

    @Override
    public String SerializeConfigObj(Gson gson) {
        return gson.toJson(this);
    }

    @Override
    public String getConfigObjId() {
        return id;
    }
    @Override
    public boolean equals(Object obj) {
        if(obj == this){
            return true;
        }
        if(obj == null){
            return false;
        }
        if(getClass() != obj.getClass()){
            return false;
        }

        ObjC other = (ObjC) obj;

        return Float.floatToIntBits(paramCC1) == Float.floatToIntBits(other.paramCC1)
                && paramC.equals(other.paramC)
                && paramCC2.equals(other.paramCC2)
                && id.equals(other.id);
    }

}
