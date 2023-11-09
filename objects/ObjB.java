package objects;

import serialization.ISerializedObj;

public class ObjB implements ISerializedObj {
    public Float paramB1;
    public String paramB2;

    public ObjB(float paramB, String paramB2){
        this.paramB1 = paramB;
        this.paramB2 = paramB2;
    }
    @Override
    public void ChkDefaultValues() {

        if(paramB1 == null){
            paramB1 = DefaultVals.OBJB_PARAMB1;
        }
        if(paramB2 == null){
            paramB2 = DefaultVals.OBJB_PARAMB2;
        }
    }
}
