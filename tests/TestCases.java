package tests;

import objects.ObjA;
import objects.ObjB;
import objects.ObjC;

public class TestCases {
    public final static ObjA OBJA_TEST_FULL_CUSTOM_DATA = new ObjA("ObjAAleInny",10.0f,11.5f);
    public final static ObjB OBJB_TEST_FULL_CUSTOM_DATA = new ObjB("ObjBBBBB",1000.0f,  "Where is my bowl!");
    public final static ObjC OBJC_TEST_FULL_CUSTOM_DATA = new ObjC("ObjCAAAA",125,121.0f, "AAAA");

    public final static String CONFIG_FULL_ALL_CHANGED = "{\n" +
            "  \"registeredObjects\": {\n" +
            "    \"ObjAAleInny\": {\n" +
            "      \"_myID\": \"ObjAAleInny\",\n" +
            "      \"paramA1\": 10.0,\n" +
            "      \"paramA2\": 11.5\n" +
            "    },\n" +
            "    \"ObjBBBBB\": {\n" +
            "      \"_myID\": \"ObjBBBBB\",\n" +
            "      \"paramB1\": 1000.0,\n" +
            "      \"paramB2\": \"Where is my bowl!\"\n" +
            "    },\n" +
            "    \"ObjCAAAA\": {\n" +
            "      \"_myID\": \"ObjCAAAA\",\n" +
            "      \"paramCC1\": 121.0,\n" +
            "      \"paramCC2\": \"AAAA\",\n" +
            "      \"id\": \"ObjCAAAA\",\n" +
            "      \"paramC\": 125\n" +
            "    }\n" +
            "  }\n" +
            "}";
    public final static String CONFIG_FULL_FIELDS_LACKS_ONE_OBJ = "{\n" +
            "  \"registeredObjects\": {\n" +
            "    \"ObjAAleInny\": {\n" +
            "      \"_myID\": \"ObjAAleInny\",\n" +
            "      \"paramA1\": 10.0,\n" +
            "      \"paramA2\": 11.5\n" +
            "    },\n" +
            "    \"ObjCAAAA\": {\n" +
            "      \"_myID\": \"ObjCAAAA\",\n" +
            "      \"paramCC1\": 121.0,\n" +
            "      \"paramCC2\": \"AAAA\",\n" +
            "      \"id\": \"ObjCAAAA\",\n" +
            "      \"paramC\": 125\n" +
            "    }\n" +
            "  }\n" +
            "}";
    public final static String CONFIG_LACKS_FIELDS_FULL_OBJ = "{\n" +
            "  \"registeredObjects\": {\n" +
            "    \"ObjAAleInny\": {\n" +
            "      \"_myID\": \"ObjAAleInny\",\n" +
            "      \"paramA1\": 10.0,\n" +
            "    },\n" +
            "    \"ObjBBBBB\": {\n" +
            "      \"_myID\": \"ObjBBBBB\",\n" +
            "      \"paramB2\": \"Where is my bowl!\"\n" +
            "    },\n" +
            "    \"ObjCAAAA\": {\n" +
            "      \"_myID\": \"ObjCAAAA\",\n" +
            "      \"paramCC1\": 121.0,\n" +
            "      \"id\": \"ObjCAAAA\",\n" +
            "      \"paramC\": 125\n" +
            "    }\n" +
            "  }\n" +
            "}";
    public final static String CONFIG_FULL_FIELDS_FULL_OBJ_IMPLICIT_CONV = "{\n" +
            "  \"registeredObjects\": {\n" +
            "    \"ObjAAleInny\": {\n" +
            "      \"_myID\": \"ObjAAleInny\",\n" +
            "      \"paramA1\": 10,\n" +
            "    },\n" +
            "    \"ObjBBBBB\": {\n" +
            "      \"_myID\": \"ObjBBBBB\",\n" +
            "      \"paramB2\": \"Where is my bowl!\"\n" +
            "    },\n" +
            "    \"ObjCAAAA\": {\n" +
            "      \"_myID\": \"ObjCAAAA\",\n" +
            "      \"paramCC1\": 121.0,\n" +
            "      \"id\": \"ObjCAAAA\",\n" +
            "      \"paramC\": 125\n" +
            "    }\n" +
            "  }\n" +
            "}";

    public final static String CONFIG_FULL_FIELDS_SINGLE_OBJ = "{\n" +
            "  \"registeredObjects\": {\n" +
            "    \"ObjCAAAA\": {\n" +
            "      \"_myID\": \"ObjCAAAA\",\n" +
            "      \"paramCC1\": 121.0,\n" +
            "      \"paramCC2\": \"AAAA\",\n" +
            "      \"id\": \"ObjCAAAA\",\n" +
            "      \"paramC\": 125\n" +
            "    }\n" +
            "  }\n" +
            "}";
}
