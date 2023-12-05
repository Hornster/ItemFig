package objects;


import net.crazedaerialcable.itemfig.serialization.config.ConfigObj;

import java.lang.reflect.Type;

public class ObjCC extends ConfigObj {
    protected Float paramCC1;
    protected String paramCC2;

    /**
     * @param myType needed for auto deserialization.
     * @param myID   will be used to discern this config object from others during deserialization.
     *               It MUST be unique.
     */
    public ObjCC(Type myType, String myID) {
        super(myType, myID);
    }

    @Override
    public void chkDefaultValues() {
        if(paramCC1 == null){
            paramCC1 = DefaultVals.OBJCC_PARAMC1_DEFAULT;
        }
        if(paramCC2 == null){
            paramCC2 = DefaultVals.OBJCC_PARAMC2_DEFAULT;
        }
    }
}
