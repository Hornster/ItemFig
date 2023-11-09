package objects;

import serialization.ISerializedObj;

public class ObjC extends ObjCC implements ISerializedObj {
    public Integer paramC;

    public ObjC(int paramC, float paramCC1, String paramCC2){
        this.paramC = paramC;
        this.paramCC1 = paramCC1;
        this.paramCC2 = paramCC2;
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
}
