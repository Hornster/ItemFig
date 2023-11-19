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
    public void chkDefaultValues() {
        if(paramA1 == null){
            paramA1 = DefaultVals.OBJA_PARAMA1;
        }
        if(paramA2 == null){
            paramA2 = DefaultVals.OBJA_PARAMA2;
        }
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

        ObjA other = (ObjA) obj;

        return Float.floatToIntBits(paramA1) == Float.floatToIntBits(other.paramA1)
                    && Float.floatToIntBits(paramA2) == Float.floatToIntBits(other.paramA2)
                && _myID.equals(other._myID);
    }
}
