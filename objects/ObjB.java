package objects;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import serialization.ConfigObj;
import serialization.IConfigObj;

public class ObjB extends ConfigObj {
    public Float paramB1;
    public String paramB2;

    public ObjB(String id, float paramB, String paramB2){
        super(ObjB.class, id);

        this.paramB1 = paramB;
        this.paramB2 = paramB2;
    }

    public ObjB(String id) {
        super(ObjB.class, id);
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
