package tests;

import objects.ObjA;
import objects.ObjB;
import objects.ObjC;

import static objects.DefaultVals.*;

public class TestCases {
    public final static String OBJA_CUSTOM_ID = "ObjAAleInny";
    public final static String OBJB_CUSTOM_ID = "ObjBBBBB";
    public final static String OBJC_CUSTOM_ID = "ObjCAAAA";
    public final static String OBJC_CUSTOM_ID2 = "ObjCAAAAAAAA";
    public final static float PARAMA1_CUSTOM_VAL = 10.0f;
    public final static float PARAMA2_CUSTOM_VAL = 11.5f;
    public final static float PARAMB_CUSTOM_VAL = 1000.0f;
    public final static String PARAMB2_CUSTOM_VAL = "Where is my bowl!";
    public final static int PARAMC_CUSTOM_VAL = 125;
    public final static int PARAMC_CUSTOM_VAL2 = 250;
    public final static float PARAMC1_CUSTOM_VAL = 121.0f;
    public final static float PARAMC1_CUSTOM_VAL2 = 242.0f;
    public final static String PARAMC2_CUSTOM_VAL = "AAAA";
    public final static String PARAMC2_CUSTOM_VAL2 = "AAAAAAAA";

    public final static ObjA OBJA_TEST_FULL_CUSTOM_DATA = new ObjA(OBJA_CUSTOM_ID,PARAMA1_CUSTOM_VAL,PARAMA2_CUSTOM_VAL);
    public final static ObjB OBJB_TEST_FULL_CUSTOM_DATA = new ObjB(OBJB_CUSTOM_ID,PARAMB_CUSTOM_VAL,  PARAMB2_CUSTOM_VAL);
    public final static ObjC OBJC_TEST_FULL_CUSTOM_DATA = new ObjC(OBJC_CUSTOM_ID,PARAMC_CUSTOM_VAL,PARAMC1_CUSTOM_VAL, PARAMC2_CUSTOM_VAL);
    public final static ObjC OBJC_TEST_FULL_CUSTOM_DATA2 = new ObjC(OBJC_CUSTOM_ID2,PARAMC_CUSTOM_VAL2,PARAMC1_CUSTOM_VAL2, PARAMC2_CUSTOM_VAL2);

    public final static ObjA OBJA_TEST_PARTIAL_CUSTOM_DATA = new ObjA(OBJA_CUSTOM_ID).InitializeFieldParamA1(PARAMA1_CUSTOM_VAL);
    public final static ObjB OBJB_TEST_PARTIAL_CUSTOM_DATA = new ObjB(OBJB_CUSTOM_ID).InitializeFieldParamB1(PARAMB_CUSTOM_VAL);

    public final static ObjC OBJC_TEST_PARTIAL_CUSTOM_DATA = new ObjC(OBJC_CUSTOM_ID).InitializeFieldParamCC1(PARAMC1_CUSTOM_VAL);



    public final static String CONFIG_FULL_ALL_CHANGED = "{\n" +
            "  \"registeredObjects\": {\n" +
            "    \"" + OBJA_CUSTOM_ID + "\": {\n" +
            "      \"_myID\": \"" + OBJA_CUSTOM_ID + "\",\n" +
            "      \"paramA1\": " + PARAMA1_CUSTOM_VAL + ",\n" +
            "      \"paramA2\": " + PARAMA2_CUSTOM_VAL + "\n" +
            "    },\n" +
            "    \"" + OBJB_CUSTOM_ID + "\": {\n" +
            "      \"_myID\": \"" + OBJB_CUSTOM_ID + "\",\n" +
            "      \"paramB1\": " + PARAMB_CUSTOM_VAL + ",\n" +
            "      \"paramB2\": \"" + PARAMB2_CUSTOM_VAL + "\"\n" +
            "    },\n" +
            "    \"" + OBJC_CUSTOM_ID + "\": {\n" +
            "      \"_myID\": \"" + OBJC_CUSTOM_ID + "\",\n" +
            "      \"paramCC1\": " + PARAMC1_CUSTOM_VAL + ",\n" +
            "      \"paramCC2\": \"" + PARAMC2_CUSTOM_VAL + "\",\n" +
            "      \"id\": \"" + OBJC_CUSTOM_ID + "\",\n" +
            "      \"paramC\": " + PARAMC_CUSTOM_VAL + "\n" +
            "    }\n" +
            "  }\n" +
            "}";
    public final static String CONFIG_FULL_FIELDS_LACKS_ONE_OBJ_PRE_SAVE = "{\n" +
            "  \"registeredObjects\": {\n" +
            "    \"" + OBJA_CUSTOM_ID + "\": {\n" +
            "      \"_myID\": \"" + OBJA_CUSTOM_ID + "\",\n" +
            "      \"paramA1\": " + PARAMA1_CUSTOM_VAL + ",\n" +
            "      \"paramA2\": " + PARAMA2_CUSTOM_VAL + "\n" +
            "    },\n" +
            "    \"" + OBJB_CUSTOM_ID + "\": {\n" +
            "      \"_myID\": \"" + OBJB_CUSTOM_ID + "\",\n" +
            "      \"paramB1\": " + PARAMB_CUSTOM_VAL + ",\n" +
            "      \"paramB2\": \"" + PARAMB2_CUSTOM_VAL + "\"\n" +
            "    }\n" +
            "  }\n" +
            "}";
    public final static String CONFIG_FULL_FIELDS_LACKS_ONE_OBJ_POST_SAVE = "{\n" +
            "  \"registeredObjects\": {\n" +
            "    \"" + OBJA_CUSTOM_ID + "\": {\n" +
            "      \"_myID\": \"" + OBJA_CUSTOM_ID + "\",\n" +
            "      \"paramA1\": " + PARAMA1_CUSTOM_VAL + ",\n" +
            "      \"paramA2\": " + PARAMA2_CUSTOM_VAL + "\n" +
            "    },\n" +
            "    \"" + OBJB_CUSTOM_ID + "\": {\n" +
            "      \"_myID\": \"" + OBJB_CUSTOM_ID + "\",\n" +
            "      \"paramB1\": " + PARAMB_CUSTOM_VAL + ",\n" +
            "      \"paramB2\": \"" + PARAMB2_CUSTOM_VAL + "\"\n" +
            "    },\n" +
            "    \"" + OBJC_CUSTOM_ID + "\": {\n" +
            "      \"_myID\": \"" + OBJC_CUSTOM_ID + "\",\n" +
            "      \"paramCC1\": " + OBJCC_PARAMC1_DEFAULT + ",\n" +
            "      \"paramCC2\": \"" + OBJCC_PARAMC2_DEFAULT + "\",\n" +
            "      \"id\": \"" + OBJC_CUSTOM_ID + "\",\n" +
            "      \"paramC\": " + OBJC_PARAMC1_DEFAULT + "\n" +
            "    }\n" +
            "  }\n" +
            "}";
    public final static String CONFIG_SAME_OBJC_TYPES = "{\n" +
            "  \"registeredObjects\": {\n" +
            "    \"" + OBJC_CUSTOM_ID + "\": {\n" +
            "      \"_myID\": \"" + OBJC_CUSTOM_ID + "\",\n" +
            "      \"paramCC1\": " + PARAMC1_CUSTOM_VAL + ",\n" +
            "      \"paramCC2\": \"" + PARAMC2_CUSTOM_VAL + "\",\n" +
            "      \"id\": \"" + OBJC_CUSTOM_ID + "\",\n" +
            "      \"paramC\": " + PARAMC_CUSTOM_VAL + "\n" +
            "    },\n" +
            "    \"" + OBJC_CUSTOM_ID2 + "\": {\n" +
            "      \"_myID\": \"" + OBJC_CUSTOM_ID2 + "\",\n" +
            "      \"paramCC1\": " + PARAMC1_CUSTOM_VAL2 + ",\n" +
            "      \"paramCC2\": \"" + PARAMC2_CUSTOM_VAL2 + "\",\n" +
            "      \"id\": \"" + OBJC_CUSTOM_ID2 + "\",\n" +
            "      \"paramC\": " + PARAMC_CUSTOM_VAL2 + "\n" +
            "    }\n" +
            "  }\n" +
            "}";
    public final static String CONFIG_LACKS_FIELDS_FULL_MULTIPLE_OBJ_PRE_SAVE = "{\n" +
            "  \"registeredObjects\": {\n" +
            "    \"" + OBJA_CUSTOM_ID + "\": {\n" +
            "      \"_myID\": \"" + OBJA_CUSTOM_ID + "\",\n" +
            "      \"paramA1\": " + PARAMA1_CUSTOM_VAL + "\n" +
            "    },\n" +
            "    \"" + OBJB_CUSTOM_ID + "\": {\n" +
            "      \"_myID\": \"" + OBJB_CUSTOM_ID + "\",\n" +
            "      \"paramB1\": \"" + PARAMB_CUSTOM_VAL + "\"\n" +
            "    },\n" +
            "    \"" + OBJC_CUSTOM_ID + "\": {\n" +
            "      \"_myID\": \"" + OBJC_CUSTOM_ID + "\",\n" +
            "      \"paramCC1\": " + PARAMC1_CUSTOM_VAL + ",\n" +
            "      \"id\": \"" + OBJC_CUSTOM_ID + "\",\n" +
            "      \"paramC\": " + OBJC_PARAMC1_DEFAULT + "\n" +
            "    }\n" +
            "  }\n" +
            "}";
    public final static String CONFIG_FULL_FIELDS_FULL_OBJ_IMPLICIT_CONV = "{\n" +
            "  \"registeredObjects\": {\n" +
            "    \"" + OBJA_CUSTOM_ID + "\": {\n" +
            "      \"_myID\": \"" + OBJA_CUSTOM_ID + "\",\n" +
            "      \"paramA1\": " + PARAMA1_CUSTOM_VAL + ",\n" +
            "      \"paramA2\": " + PARAMA2_CUSTOM_VAL + "\n" +
            "    },\n" +
            "    \"" + OBJB_CUSTOM_ID + "\": {\n" +
            "      \"_myID\": \"" + OBJB_CUSTOM_ID + "\",\n" +
            "      \"paramB1\": " + PARAMB_CUSTOM_VAL + ",\n" +
            "      \"paramB2\": \"" + PARAMB2_CUSTOM_VAL + "\"\n" +
            "    },\n" +
            "    \"" + OBJC_CUSTOM_ID + "\": {\n" +
            "      \"_myID\": \"" + OBJC_CUSTOM_ID + "\",\n" +
            "      \"paramCC1\": " + PARAMC1_CUSTOM_VAL + ",\n" +
            "      \"paramCC2\": \"" + PARAMC2_CUSTOM_VAL + "\",\n" +
            "      \"id\": \"" + OBJC_CUSTOM_ID + "\",\n" +
            "      \"paramC\": " + PARAMC_CUSTOM_VAL + "\n" +
            "    }\n" +
            "  }\n" +
            "}";
    public final static String CONFIG_LACKS_FIELDS_FULL_MULTIPLE_OBJ_POST_SAVE = "{\n" +
            "  \"registeredObjects\": {\n" +
            "    \"" + OBJA_CUSTOM_ID + "\": {\n" +
            "      \"_myID\": \"" + OBJA_CUSTOM_ID + "\",\n" +
            "      \"paramA1\": " + PARAMA1_CUSTOM_VAL + ",\n" +
            "      \"paramA2\": " + OBJA_PARAMA2_DEFAULT + "\n" +
            "    },\n" +
            "    \"" + OBJB_CUSTOM_ID + "\": {\n" +
            "      \"_myID\": \"" + OBJB_CUSTOM_ID + "\",\n" +
            "      \"paramB1\": " + PARAMB_CUSTOM_VAL + ",\n" +
            "      \"paramB2\": \"" + OBJB_PARAMB2_DEFAULT + "\"\n" +
            "    },\n" +
            "    \"" + OBJC_CUSTOM_ID + "\": {\n" +
            "      \"_myID\": \"" + OBJC_CUSTOM_ID + "\",\n" +
            "      \"paramCC1\": " + PARAMC1_CUSTOM_VAL + ",\n" +
            "      \"paramCC2\": \"" + OBJCC_PARAMC2_DEFAULT + "\",\n" +
            "      \"id\": \"" + OBJC_CUSTOM_ID + "\",\n" +
            "      \"paramC\": " + OBJC_PARAMC1_DEFAULT + "\n" +
            "    }\n" +
            "  }\n" +
            "}";

    public final static String CONFIG_FULL_FIELDS_SINGLE_OBJ = "{\n" +
            "  \"registeredObjects\": {\n" +
            "    \"" + OBJC_CUSTOM_ID + "\": {\n" +
            "      \"_myID\": \"" + OBJC_CUSTOM_ID + "\",\n" +
            "      \"paramCC1\": " + PARAMC1_CUSTOM_VAL + ",\n" +
            "      \"paramCC2\": \"" + PARAMC2_CUSTOM_VAL + "\",\n" +
            "      \"id\": \"" + OBJC_CUSTOM_ID + "\",\n" +
            "      \"paramC\": " + PARAMC_CUSTOM_VAL + "\n" +
            "    }\n" +
            "  }\n" +
            "}";
    public final static String CONFIG_LACKS_FIELDS_SINGLE_OBJ_PRE_SAVE = "{\n" +
            "  \"registeredObjects\": {\n" +
            "    \"" + OBJA_CUSTOM_ID + "\": {\n" +
            "      \"_myID\": \"" + OBJA_CUSTOM_ID + "\",\n" +
            "      \"paramA1\": " + PARAMA1_CUSTOM_VAL + "\n" +
            "    }" +
            "  }\n" +
            "}";
    public final static String CONFIG_LACKS_FIELDS_SINGLE_OBJ_POST_SAVE = "{\n" +
            "  \"registeredObjects\": {\n" +
            "    \"" + OBJA_CUSTOM_ID + "\": {\n" +
            "      \"_myID\": \"" + OBJA_CUSTOM_ID + "\",\n" +
            "      \"paramA1\": " + PARAMA1_CUSTOM_VAL + ",\n" +
            "      \"paramA2\": " + OBJA_PARAMA2_DEFAULT + "\n" +
            "    }\n" +
            "  }\n" +
            "}";
}
