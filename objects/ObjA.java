package objects;

import serialization.ISerializedObj;

public class ObjA implements ISerializedObj {
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
}
