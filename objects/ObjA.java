package objects;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import serialization.IConfigObj;

public class ObjA implements IConfigObj {
    public Float paramA1;
    public Float paramA2;

    public ObjA(float paramA1, float paramA2){
        this.paramA1 = paramA1;
        this.paramA2 = paramA2;
    }

    @Override
    public void ChkDefaultValues() {
        if(paramA1 == null){
            paramA1 = DefaultVals.OBJA_PARAMA1;
        }
        if(paramA2 == null){
            paramA2 = DefaultVals.OBJA_PARAMA2;
        }
    }

    @Override
    public IConfigObj DeserializeConfigObj(Gson gson, JsonElement element) {
        return null;
    }

    @Override
    public String SerializeConfigObj(Gson gson) {
        return null;
    }

    @Override
    public String getConfigObjId() {
        return null;
    }
}
