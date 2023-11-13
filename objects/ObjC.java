package objects;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import serialization.IConfigObj;

import java.lang.reflect.Type;

public class ObjC extends ObjCC implements IConfigObj {
    public String id;
    private Type thisType = ObjC.class;
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
    public IConfigObj DeserializeConfigObj(Gson gson, JsonElement element) {
        var deserializedObj = (IConfigObj)gson.fromJson(element,thisType);
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
}
