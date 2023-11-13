package objects;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import serialization.ConfigObj;
import serialization.IConfigObj;

public class ObjA extends ConfigObj {
    public Float paramA1;
    public Float paramA2;
    public ObjA(String id){
        super(ObjA.class, id);

    }
    public ObjA(String id, float paramA1, float paramA2){
        super(ObjA.class, id);

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

}
