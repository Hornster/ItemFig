import adapters.ConfigObjAAdapterConfig;
import adapters.ConfigObjBAdapterConfig;
import adapters.ConfigObjCAdapterConfig;
import io.github.hornster.itemfig.api.serialization.ItemFigApi;
import io.github.hornster.itemfig.api.serialization.config.ConfigObj;
import io.github.hornster.itemfig.api.serialization.config.ConfigObjAdapterConfig;

import objects.ObjA;
import objects.ObjB;
import objects.ObjC;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import static io.github.hornster.itemfig.serialization.common.constants.Constants.*;
import static org.junit.jupiter.api.Assertions.fail;


public class ItemFigApiTest {
    private final static String OBJ_A_NAME = "ObjA";
    private final static String OBJ_B_NAME = "ObjB";
    private final static String OBJ_C_NAME = "ObjC";

    @BeforeEach
    void setUp() {
        ItemFigApi.resetManager();
    }
    private static List<Pair<ConfigObj, ConfigObjAdapterConfig<?>>> createObjListOK(){
        var newObjectA = new ObjA(OBJ_A_NAME);
        var newObjectB = new ObjB(OBJ_B_NAME);
        var newObjectC = new ObjC(OBJ_C_NAME);

        var newAdapterObjA = new ConfigObjAAdapterConfig();
        var newAdapterObjB = new ConfigObjBAdapterConfig();
        var newAdapterObjC = new ConfigObjCAdapterConfig();

        var list = new LinkedList<Pair<ConfigObj, ConfigObjAdapterConfig<?>>>();
        list.add(new MutablePair<>(newObjectA, newAdapterObjA));
        list.add(new MutablePair<>(newObjectB, newAdapterObjB));
        list.add(new MutablePair<>(newObjectC, newAdapterObjC));

        return list;
    }
    private static void prepareConfig(String data, String configPath){
        try{
            var configFile = new FileWriter(configPath);
            configFile.write(data);
            configFile.close();
        }
        catch(IOException ex){
            fail("Could not prepare config! Additional info: " + ex.getMessage());
        }

    }
    private static void removeConfig(String path){
        var pathToFile= Paths.get(path);
        try{
            Files.delete(pathToFile);
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    private static void chkIfFilePresent(String path){
        var pathToFile= Paths.get(path);
        var chkResult =  Files.exists(pathToFile);
        if(!chkResult){
            fail("Config file was not created/stopped existing unrightfully!");
        }
    }
    private static void chkConfigFileContents(String originalContents, String configPath){
        try{
            var filePath = Path.of(configPath);
            String content = Files.readString(filePath);
            if(!content.equals(originalContents)){
                fail("Saved and original contents in config file differ!");
            }
        }
        catch(IOException ex){
            fail("Could not prepare config! Additional info: " + ex.getMessage());
        }
    }

    private static void chkIfResultPresent(ConfigObj result, ConfigObj configSrc){
        if(result == null){
            fail("Object of id " + configSrc.getConfigObjId() + " was not saved in the config!");
        }
    }
    private static void chkIfResultEqualsSource(ConfigObj result, ConfigObj configSrc){
        var isEqual = configSrc.equals(result);
        if(!isEqual){
            fail("Object of id " + configSrc.getConfigObjId() + "Has different field values than its original!");
        }
    }

    @Test
    @Order(1)
    public void registerObjectOK() {
        var newObjectA = new ObjA(OBJ_A_NAME);
        var newObjectB = new ObjB(OBJ_B_NAME);
        var newObjectC = new ObjC(OBJ_C_NAME);

        var newAdapterObjA = new ConfigObjAAdapterConfig();
        var newAdapterObjB = new ConfigObjBAdapterConfig();
        var newAdapterObjC = new ConfigObjCAdapterConfig();

        try{
            ItemFigApi.registerObject(newObjectA, newAdapterObjA);
            ItemFigApi.registerObject(newObjectB, newAdapterObjB);
            ItemFigApi.registerObject(newObjectC, newAdapterObjC);
        }catch(Exception ex){
            fail("Should not throw any exceptions upon trying to return properly registered config objects!");
        }
    }

    @Test
    @Order(1)
    public void registerObjectsOK() {
        var list = createObjListOK();

        try{
            ItemFigApi.registerObjects(list);
        }catch(Exception ex){
            fail("Should not throw any exceptions upon trying to return properly registered config objects!");
        }
    }

    @Test
    @Order(1)
    void registerSaveErrorHandler() {
        try{
            ItemFigApi.registerSaveErrorHandler((ass) -> {var ass2 = ass;});
        } catch(Exception ex){
            fail("Should not throw when registering save error handler!");
        }
    }

    @Test
    @Order(1)
    void registerReadErrorHandler() {
        try{
            ItemFigApi.registerReadErrorHandler((ass) -> {var ass2 = ass;});
        } catch(Exception ex){
            fail("Should not throw when registering read error handler!");
        }
    }

    @Test
    @Order(1)
    void setConfigFileName() {
        try{
            ItemFigApi.setConfigFileName("testName");
        }catch(Exception ex){
            fail("Should not throw any exceptions upon trying to return properly registered config objects! Additional info: " + ex.getMessage());
        }
    }

    @Test
    @Order(1)
    void recreateEmptyConfig(){
        ItemFigApi.setRecreateConfig(true);
        var wasReported = new AtomicBoolean(false);

        Consumer<String> reporter = (String warnMsg) ->{
            System.out.println(warnMsg);
            wasReported.set(true);
            if(!warnMsg.equals(NO_CONFIG_FILES_WARN)){
                fail("Incorrect warning msg returned about saved empty config!");
            }
        };

        ItemFigApi.registerReadWarningHandler(reporter);

        try{
            ItemFigApi.readConfig();
        }
        catch(Exception ex){
            fail("Recreating config should not throw exception in this case! Additional info: " + ex.getMessage());
        }

        if(!wasReported.get()){
            fail("Warning about empty file not reported!");
        }
        ItemFigApi.setRecreateConfig(false);
    }

    @Test
    @Order(2)
    void readEmptyConfig(){
        recreateEmptyConfig();
        var wasReported = new AtomicBoolean(false);

        Consumer<String> reporter = (String warnMsg) ->{
            System.out.println(warnMsg);
            wasReported.set(true);
            if(!warnMsg.equals(NO_CONFIG_FILES_WARN)){
                fail("Incorrect warning msg returned about saved empty config!");
            }
        };

        ItemFigApi.registerReadWarningHandler(reporter);

        try{
            ItemFigApi.readConfig();
        }
        catch(Exception ex){
            fail("Recreating config should not throw exception in this case! Additional info: " + ex.getMessage());
        }

        if(!wasReported.get()){
            fail("Warning about empty file not reported!");
        }
    }

    @Test
    @Order(2)
    void recreateConfigFromDefaultsNoFileNoForce(){
        var objects = createObjListOK();
        var configPath = ItemFigApi.getConfigPath() + ItemFigApi.getConfigFileName();
        removeConfig(configPath);

        try{
            ItemFigApi.registerObjects(objects);
            ItemFigApi.readConfig();
        }
        catch(Exception ex){
            fail("Should not throw any exceptions when saving default data to new file! Additional info: " + ex.getMessage());
        }

        chkIfFilePresent(configPath);

        var objectsToValidate = createObjListOK();
        for(var obj : objectsToValidate){
            var configObj = obj.getKey();
            configObj.chkDefaultValues();
            var result = ItemFigApi.getItemConfigAutoCast(configObj.getConfigObjId());
            if(result == null){
                fail("Object of id " + configObj.getConfigObjId() + " was not saved in the config!");
            }

            var isEqual = configObj.equals(result);
            if(!isEqual){
                fail("Object of id " + configObj.getConfigObjId() + "Has different field values than its original!");
            }
        }

    }

    @Test
    @Order(2)
    void readConfigSingleWithHierarchy() {
        var configPath = ItemFigApi.getConfigPath() + ItemFigApi.getConfigFileName();
        var testObjC = TestCases.OBJC_TEST_FULL_CUSTOM_DATA;
        prepareConfig(TestCases.CONFIG_FULL_FIELDS_SINGLE_OBJ, configPath);

        ItemFigApi.registerObject(new ObjC(testObjC.getConfigObjId()), new ConfigObjCAdapterConfig());

        ItemFigApi.readConfig();

        var readObjC = (ObjC) ItemFigApi.getItemConfig(testObjC.getConfigObjId());
        chkIfFilePresent(configPath);
        chkIfResultPresent(readObjC, testObjC);
        chkIfResultEqualsSource(readObjC, testObjC);
        chkConfigFileContents(TestCases.CONFIG_FULL_FIELDS_SINGLE_OBJ, configPath);
    }
    @Test
    @Order(2)
    void sameObjTypeFullFields(){
        var configPath = ItemFigApi.getConfigPath() + ItemFigApi.getConfigFileName();
        var testObjC = TestCases.OBJC_TEST_FULL_CUSTOM_DATA;
        var testObjC2 = TestCases.OBJC_TEST_FULL_CUSTOM_DATA2;
        prepareConfig(TestCases.CONFIG_SAME_OBJC_TYPES, configPath);

        var objCAdapter = new ConfigObjCAdapterConfig();

        ItemFigApi.registerObject(new ObjC(testObjC.getConfigObjId()), objCAdapter);
        ItemFigApi.registerObject(new ObjC(testObjC2.getConfigObjId()), objCAdapter);

        var idsList = new LinkedList<String>();
        idsList.add(testObjC.getConfigObjId());
        idsList.add(testObjC2.getConfigObjId());

        var objMap = new HashMap<String, ConfigObj>();
        objMap.put(testObjC.getConfigObjId(), testObjC);
        objMap.put(testObjC2.getConfigObjId(), testObjC2);

        ItemFigApi.readConfig();

        for(var objId : idsList){
            var readObj = (ConfigObj) ItemFigApi.getItemConfig(objId);
            var testObj = objMap.get(objId);
            chkIfFilePresent(configPath);
            chkIfResultPresent(readObj, testObj);
            chkIfResultEqualsSource(readObj, testObj);
        }
        chkConfigFileContents(TestCases.CONFIG_SAME_OBJC_TYPES, configPath);
    }
    @Test
    @Order(2)
    void readConfigMultiple() {
        var configPath = ItemFigApi.getConfigPath() + ItemFigApi.getConfigFileName();
        var testObjA = TestCases.OBJA_TEST_FULL_CUSTOM_DATA;
        var testObjB = TestCases.OBJB_TEST_FULL_CUSTOM_DATA;
        var testObjC = TestCases.OBJC_TEST_FULL_CUSTOM_DATA;
        prepareConfig(TestCases.CONFIG_FULL_ALL_CHANGED, configPath);

        ItemFigApi.registerObject(new ObjA(testObjA.getConfigObjId()), new ConfigObjAAdapterConfig());
        ItemFigApi.registerObject(new ObjB(testObjB.getConfigObjId()), new ConfigObjBAdapterConfig());
        ItemFigApi.registerObject(new ObjC(testObjC.getConfigObjId()), new ConfigObjCAdapterConfig());

        var idsList = getTestObjIds(testObjA, testObjB, testObjC);

        var objMap = getTestObjectMap(testObjA, testObjB, testObjC);

        ItemFigApi.readConfig();

        for(var objId : idsList){
            var readObj = (ConfigObj) ItemFigApi.getItemConfig(objId);
            var testObj = objMap.get(objId);
            chkIfFilePresent(configPath);
            chkIfResultPresent(readObj, testObj);
            chkIfResultEqualsSource(readObj, testObj);
        }
        chkConfigFileContents(TestCases.CONFIG_FULL_ALL_CHANGED, configPath);
    }

    private static LinkedList<String> getTestObjIds(ObjA testObjA, ObjB testObjB, ObjC testObjC) {
        var idsList = new LinkedList<String>();
        idsList.add(testObjA.getConfigObjId());
        idsList.add(testObjB.getConfigObjId());
        idsList.add(testObjC.getConfigObjId());
        return idsList;
    }

    @Test
    @Order(2)
    void readConfigSingleLacksData() {
        var configPath = ItemFigApi.getConfigPath() + ItemFigApi.getConfigFileName();
        var testObjA = TestCases.OBJA_TEST_PARTIAL_CUSTOM_DATA;
        testObjA.chkDefaultValues();

        prepareConfig(TestCases.CONFIG_LACKS_FIELDS_SINGLE_OBJ_PRE_SAVE, configPath);
        ItemFigApi.registerObject(new ObjA(testObjA.getConfigObjId()), new ConfigObjAAdapterConfig());

        ItemFigApi.readConfig();

        var readObjA = (ObjA) ItemFigApi.getItemConfig(testObjA.getConfigObjId());
        chkIfFilePresent(configPath);
        chkIfResultPresent(readObjA, testObjA);
        chkIfResultEqualsSource(readObjA, testObjA);
        chkConfigFileContents(TestCases.CONFIG_LACKS_FIELDS_SINGLE_OBJ_POST_SAVE, configPath);
    }
    @Test
    @Order(2)
    void readConfigMultipleLacksData() {
        var configPath = ItemFigApi.getConfigPath() + ItemFigApi.getConfigFileName();
        var testObjA = TestCases.OBJA_TEST_PARTIAL_CUSTOM_DATA;
        var testObjB = TestCases.OBJB_TEST_PARTIAL_CUSTOM_DATA;
        var testObjC = TestCases.OBJC_TEST_PARTIAL_CUSTOM_DATA;
        prepareConfig(TestCases.CONFIG_LACKS_FIELDS_FULL_MULTIPLE_OBJ_PRE_SAVE, configPath);

        ItemFigApi.registerObject(new ObjA(testObjA.getConfigObjId()), new ConfigObjAAdapterConfig());
        ItemFigApi.registerObject(new ObjB(testObjB.getConfigObjId()), new ConfigObjBAdapterConfig());
        ItemFigApi.registerObject(new ObjC(testObjC.getConfigObjId()), new ConfigObjCAdapterConfig());

        var idsList = getTestObjIds(testObjA, testObjB, testObjC);

        var objMap = getTestObjectMap(testObjA, testObjB, testObjC);

        ItemFigApi.readConfig();

        for(var objId : idsList){
            var readObj = (ConfigObj) ItemFigApi.getItemConfig(objId);
            var testObj = objMap.get(objId);

            testObj.chkDefaultValues();

            chkIfFilePresent(configPath);
            chkIfResultPresent(readObj, testObj);
            chkIfResultEqualsSource(readObj, testObj);
        }
        chkConfigFileContents(TestCases.CONFIG_LACKS_FIELDS_FULL_MULTIPLE_OBJ_POST_SAVE, configPath);
        //TODO use CONFIG_LACKS_FIELDS_FULL_PRE_SAVE_OBJ and CONFIG_FULL_FIELDS_FULL_OBJ_IMPLICIT_CONV here
    }

    private static HashMap<String, ConfigObj> getTestObjectMap(ObjA testObjA, ObjB testObjB, ObjC testObjC) {
        var objMap = new HashMap<String, ConfigObj>();
        objMap.put(testObjA.getConfigObjId(), testObjA);
        objMap.put(testObjB.getConfigObjId(), testObjB);
        objMap.put(testObjC.getConfigObjId(), testObjC);
        return objMap;
    }

    @Test
    @Order(3)
    void readConfigMultipleLacksObject() {
        var configPath = ItemFigApi.getConfigPath() + ItemFigApi.getConfigFileName();
        var testObjA = TestCases.OBJA_TEST_FULL_CUSTOM_DATA;
        var testObjB = TestCases.OBJB_TEST_FULL_CUSTOM_DATA;
        var testObjC = new ObjC(TestCases.OBJC_CUSTOM_ID);

        prepareConfig(TestCases.CONFIG_FULL_FIELDS_LACKS_ONE_OBJ_PRE_SAVE, configPath);

        ItemFigApi.registerObject(new ObjA(testObjA.getConfigObjId()), new ConfigObjAAdapterConfig());
        ItemFigApi.registerObject(new ObjB(testObjB.getConfigObjId()), new ConfigObjBAdapterConfig());
        ItemFigApi.registerObject(new ObjC(testObjC.getConfigObjId()), new ConfigObjCAdapterConfig());

        var idsList = getTestObjIds(testObjA, testObjB, testObjC);

        var objMap = getTestObjectMap(testObjA, testObjB, testObjC);

        ItemFigApi.readConfig();

        for(var objId : idsList){
            var readObj = (ConfigObj) ItemFigApi.getItemConfig(objId);
            var testObj = objMap.get(objId);

            testObj.chkDefaultValues();

            chkIfFilePresent(configPath);
            chkIfResultPresent(readObj, testObj);
            chkIfResultEqualsSource(readObj, testObj);
        }
        chkConfigFileContents(TestCases.CONFIG_FULL_FIELDS_LACKS_ONE_OBJ_POST_SAVE, configPath);
    }

    @Test
    void getItemConfigSingleRegisterOK() {
        var newObjectA = new ObjA(OBJ_A_NAME);
        var newObjectB = new ObjB(OBJ_B_NAME);
        var newObjectC = new ObjC(OBJ_C_NAME);

        var newAdapterObjA = new ConfigObjAAdapterConfig();
        var newAdapterObjB = new ConfigObjBAdapterConfig();
        var newAdapterObjC = new ConfigObjCAdapterConfig();

        try{
            ItemFigApi.registerObject(newObjectA, newAdapterObjA);
            ItemFigApi.registerObject(newObjectB, newAdapterObjB);
            ItemFigApi.registerObject(newObjectC, newAdapterObjC);

            var regObj = ItemFigApi.getItemConfig(OBJ_A_NAME);
            newObjectA = (ObjA) regObj;
            regObj = ItemFigApi.getItemConfig(OBJ_B_NAME);
            newObjectB = (ObjB) regObj;
            regObj = ItemFigApi.getItemConfig(OBJ_C_NAME);
            newObjectC = (ObjC) regObj;
        }catch(Exception ex){
            fail("Should not throw any exceptions upon trying to return properly registered config objects!");
        }
    }

    @Test
    void getItemConfigAutoCastSingleRegisterOK() {
        var newObjectA = new ObjA(OBJ_A_NAME);
        var newObjectB = new ObjB(OBJ_B_NAME);
        var newObjectC = new ObjC(OBJ_C_NAME);

        var newAdapterObjA = new ConfigObjAAdapterConfig();
        var newAdapterObjB = new ConfigObjBAdapterConfig();
        var newAdapterObjC = new ConfigObjCAdapterConfig();

        try{
            ItemFigApi.registerObject(newObjectA, newAdapterObjA);
            ItemFigApi.registerObject(newObjectB, newAdapterObjB);
            ItemFigApi.registerObject(newObjectC, newAdapterObjC);

            newObjectA = ItemFigApi.getItemConfigAutoCast(OBJ_A_NAME);
            newObjectB = ItemFigApi.getItemConfigAutoCast(OBJ_B_NAME);
            newObjectC = ItemFigApi.getItemConfigAutoCast(OBJ_C_NAME);
        }catch(Exception ex){
            fail("Should not throw any exceptions upon trying to return properly registered config objects!");
        }
    }
    @Test
    void getItemConfigBulkRegisterOK() {
        var list = createObjListOK();

        try{
            ItemFigApi.registerObjects(list);

            var regObj = ItemFigApi.getItemConfig(OBJ_A_NAME);
            ObjA newObjectA = (ObjA) regObj;
            regObj = ItemFigApi.getItemConfig(OBJ_B_NAME);
            ObjB newObjectB = (ObjB) regObj;
            regObj = ItemFigApi.getItemConfig(OBJ_C_NAME);
            ObjC newObjectC = (ObjC) regObj;
        }catch(Exception ex){
            fail("Should not throw any exceptions upon trying to return properly registered config objects!");
        }
    }

    @Test
    void getItemConfigAutoCastBulkRegisterOK() {
        var list = createObjListOK();

        try{
            ItemFigApi.registerObjects(list);

            ObjA newObjectA = ItemFigApi.getItemConfigAutoCast(OBJ_A_NAME);
            ObjB newObjectB = ItemFigApi.getItemConfigAutoCast(OBJ_B_NAME);
            ObjC newObjectC = ItemFigApi.getItemConfigAutoCast(OBJ_C_NAME);
        }catch(Exception ex){
            fail("Should not throw any exceptions upon trying to return properly registered config objects!");
        }
    }
}