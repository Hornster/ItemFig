import io.github.hornster.itemfig.api.serialization.config.ConfigObjAdapterConfig;
import objects.ObjA;
import objects.ObjB;
import objects.ObjC;
import io.github.hornster.itemfig.serialization.SerializationManager;
import io.github.hornster.itemfig.api.serialization.config.ConfigObj;
import io.github.hornster.itemfig.serialization.config.ConfigObjAdapter;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.*;
import adapters.ConfigObjAAdapterConfig;
import adapters.ConfigObjBAdapterConfig;
import adapters.ConfigObjCAdapterConfig;

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

import static io.github.hornster.itemfig.serialization.common.constants.Constants.EMPTY_CONFIG_READ_WARN;
import static io.github.hornster.itemfig.serialization.common.constants.Constants.EMPTY_CONFIG_SAVE_WARN;
import static org.junit.jupiter.api.Assertions.fail;


public class SerializationManagerTest {
    private final static String OBJ_A_NAME = "ObjA";
    private final static String OBJ_B_NAME = "ObjB";
    private final static String OBJ_C_NAME = "ObjC";



    private SerializationManager _serializationManager;

    @BeforeEach
    void setUp() {
        _serializationManager = new SerializationManager();
    }
    @AfterEach
    void tearDown() {
        _serializationManager = null;
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
            _serializationManager.registerObject(newObjectA, newAdapterObjA);
            _serializationManager.registerObject(newObjectB, newAdapterObjB);
            _serializationManager.registerObject(newObjectC, newAdapterObjC);
        }catch(Exception ex){
            fail("Should not throw any exceptions upon trying to return properly registered config objects!");
        }
    }

    @Test
    @Order(1)
    public void registerObjectsOK() {
        var list = createObjListOK();

        try{
            _serializationManager.registerObjects(list);
        }catch(Exception ex){
            fail("Should not throw any exceptions upon trying to return properly registered config objects!");
        }
    }

    @Test
    @Order(1)
    void registerSaveErrorHandler() {
        try{
            _serializationManager.registerSaveErrorHandler((ass) -> {var ass2 = ass;});
        } catch(Exception ex){
            fail("Should not throw when registering save error handler!");
        }
    }

    @Test
    @Order(1)
    void registerReadErrorHandler() {
        try{
            _serializationManager.registerReadErrorHandler((ass) -> {var ass2 = ass;});
        } catch(Exception ex){
            fail("Should not throw when registering read error handler!");
        }
    }

    @Test
    @Order(1)
    void setConfigFileName() {
        try{
            _serializationManager.setConfigFileName("testName");
        }catch(Exception ex){
            fail("Should not throw any exceptions upon trying to return properly registered config objects! Additional info: " + ex.getMessage());
        }
    }

    @Test
    @Order(1)
    void recreateEmptyConfig(){
        _serializationManager._recreateConfig = true;
        var wasReported = new AtomicBoolean(false);

        Consumer<String> reporter = (String warnMsg) ->{
            System.out.println(warnMsg);
            wasReported.set(true);
            if(!warnMsg.equals(EMPTY_CONFIG_SAVE_WARN)){
                fail("Incorrect warning msg returned about saved empty config!");
            }
        };

        _serializationManager.registerSaveWarningHandler(reporter);

        try{
            _serializationManager.readConfig();
        }
        catch(Exception ex){
            fail("Recreating config should not throw exception in this case! Additional info: " + ex.getMessage());
        }

        if(!wasReported.get()){
            fail("Warning about empty file not reported!");
        }
        _serializationManager._recreateConfig = false;
    }

    @Test
    @Order(2)
    void readEmptyConfig(){
        recreateEmptyConfig();
        var wasReported = new AtomicBoolean(false);

        Consumer<String> reporter = (String warnMsg) ->{
            System.out.println(warnMsg);
            wasReported.set(true);
            if(!warnMsg.equals(EMPTY_CONFIG_READ_WARN)){
                fail("Incorrect warning msg returned about saved empty config!");
            }
        };

        _serializationManager.registerReadWarningHandler(reporter);

        try{
            _serializationManager.readConfig();
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
        var configPath = _serializationManager.getConfigPath() + _serializationManager.getConfigFileName();
        removeConfig(configPath);

        try{
            _serializationManager.registerObjects(objects);
            _serializationManager.readConfig();
        }
        catch(Exception ex){
            fail("Should not throw any exceptions when saving default data to new file! Additional info: " + ex.getMessage());
        }

        chkIfFilePresent(configPath);

        var objectsToValidate = createObjListOK();
        for(var obj : objectsToValidate){
            var configObj = obj.getKey();
            configObj.chkDefaultValues();
            var result = _serializationManager.getItemConfigAutoCast(configObj.getConfigObjId());
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
        var configPath = _serializationManager.getConfigPath() + _serializationManager.getConfigFileName();
        var testObjC = TestCases.OBJC_TEST_FULL_CUSTOM_DATA;
        prepareConfig(TestCases.CONFIG_FULL_FIELDS_SINGLE_OBJ, configPath);

        _serializationManager.registerObject(new ObjC(testObjC.getConfigObjId()), new ConfigObjCAdapterConfig());

        _serializationManager.readConfig();

        var readObjC = (ObjC)_serializationManager.getItemConfig(testObjC.getConfigObjId());
        chkIfFilePresent(configPath);
        chkIfResultPresent(readObjC, testObjC);
        chkIfResultEqualsSource(readObjC, testObjC);
        chkConfigFileContents(TestCases.CONFIG_FULL_FIELDS_SINGLE_OBJ, configPath);
    }
    @Test
    @Order(2)
    void sameObjTypeFullFields(){
        var configPath = _serializationManager.getConfigPath() + _serializationManager.getConfigFileName();
        var testObjC = TestCases.OBJC_TEST_FULL_CUSTOM_DATA;
        var testObjC2 = TestCases.OBJC_TEST_FULL_CUSTOM_DATA2;
        prepareConfig(TestCases.CONFIG_SAME_OBJC_TYPES, configPath);

        var objCAdapter = new ConfigObjCAdapterConfig();

        _serializationManager.registerObject(new ObjC(testObjC.getConfigObjId()), objCAdapter);
        _serializationManager.registerObject(new ObjC(testObjC2.getConfigObjId()), objCAdapter);

        var idsList = new LinkedList<String>();
        idsList.add(testObjC.getConfigObjId());
        idsList.add(testObjC2.getConfigObjId());

        var objMap = new HashMap<String, ConfigObj>();
        objMap.put(testObjC.getConfigObjId(), testObjC);
        objMap.put(testObjC2.getConfigObjId(), testObjC2);

        _serializationManager.readConfig();

        for(var objId : idsList){
            var readObj = (ConfigObj) _serializationManager.getItemConfig(objId);
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
        var configPath = _serializationManager.getConfigPath() + _serializationManager.getConfigFileName();
        var testObjA = TestCases.OBJA_TEST_FULL_CUSTOM_DATA;
        var testObjB = TestCases.OBJB_TEST_FULL_CUSTOM_DATA;
        var testObjC = TestCases.OBJC_TEST_FULL_CUSTOM_DATA;
        prepareConfig(TestCases.CONFIG_FULL_ALL_CHANGED, configPath);

        _serializationManager.registerObject(new ObjA(testObjA.getConfigObjId()), new ConfigObjAAdapterConfig());
        _serializationManager.registerObject(new ObjB(testObjB.getConfigObjId()), new ConfigObjBAdapterConfig());
        _serializationManager.registerObject(new ObjC(testObjC.getConfigObjId()), new ConfigObjCAdapterConfig());

        var idsList = getTestObjIds(testObjA, testObjB, testObjC);

        var objMap = getTestObjectMap(testObjA, testObjB, testObjC);

        _serializationManager.readConfig();

        for(var objId : idsList){
            var readObj = (ConfigObj) _serializationManager.getItemConfig(objId);
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
        var configPath = _serializationManager.getConfigPath() + _serializationManager.getConfigFileName();
        var testObjA = TestCases.OBJA_TEST_PARTIAL_CUSTOM_DATA;
        testObjA.chkDefaultValues();

        prepareConfig(TestCases.CONFIG_LACKS_FIELDS_SINGLE_OBJ_PRE_SAVE, configPath);
        _serializationManager.registerObject(new ObjA(testObjA.getConfigObjId()), new ConfigObjAAdapterConfig());

        _serializationManager.readConfig();

        var readObjA = (ObjA)_serializationManager.getItemConfig(testObjA.getConfigObjId());
        chkIfFilePresent(configPath);
        chkIfResultPresent(readObjA, testObjA);
        chkIfResultEqualsSource(readObjA, testObjA);
        chkConfigFileContents(TestCases.CONFIG_LACKS_FIELDS_SINGLE_OBJ_POST_SAVE, configPath);
    }
    @Test
    @Order(2)
    void readConfigMultipleLacksData() {
        var configPath = _serializationManager.getConfigPath() + _serializationManager.getConfigFileName();
        var testObjA = TestCases.OBJA_TEST_PARTIAL_CUSTOM_DATA;
        var testObjB = TestCases.OBJB_TEST_PARTIAL_CUSTOM_DATA;
        var testObjC = TestCases.OBJC_TEST_PARTIAL_CUSTOM_DATA;
        prepareConfig(TestCases.CONFIG_LACKS_FIELDS_FULL_MULTIPLE_OBJ_PRE_SAVE, configPath);

        _serializationManager.registerObject(new ObjA(testObjA.getConfigObjId()), new ConfigObjAAdapterConfig());
        _serializationManager.registerObject(new ObjB(testObjB.getConfigObjId()), new ConfigObjBAdapterConfig());
        _serializationManager.registerObject(new ObjC(testObjC.getConfigObjId()), new ConfigObjCAdapterConfig());

        var idsList = getTestObjIds(testObjA, testObjB, testObjC);

        var objMap = getTestObjectMap(testObjA, testObjB, testObjC);

        _serializationManager.readConfig();

        for(var objId : idsList){
            var readObj = (ConfigObj) _serializationManager.getItemConfig(objId);
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
        var configPath = _serializationManager.getConfigPath() + _serializationManager.getConfigFileName();
        var testObjA = TestCases.OBJA_TEST_FULL_CUSTOM_DATA;
        var testObjB = TestCases.OBJB_TEST_FULL_CUSTOM_DATA;
        var testObjC = new ObjC(TestCases.OBJC_CUSTOM_ID);

        prepareConfig(TestCases.CONFIG_FULL_FIELDS_LACKS_ONE_OBJ_PRE_SAVE, configPath);

        _serializationManager.registerObject(new ObjA(testObjA.getConfigObjId()), new ConfigObjAAdapterConfig());
        _serializationManager.registerObject(new ObjB(testObjB.getConfigObjId()), new ConfigObjBAdapterConfig());
        _serializationManager.registerObject(new ObjC(testObjC.getConfigObjId()), new ConfigObjCAdapterConfig());

        var idsList = getTestObjIds(testObjA, testObjB, testObjC);

        var objMap = getTestObjectMap(testObjA, testObjB, testObjC);

        _serializationManager.readConfig();

        for(var objId : idsList){
            var readObj = (ConfigObj) _serializationManager.getItemConfig(objId);
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
            _serializationManager.registerObject(newObjectA, newAdapterObjA);
            _serializationManager.registerObject(newObjectB, newAdapterObjB);
            _serializationManager.registerObject(newObjectC, newAdapterObjC);

            var regObj = _serializationManager.getItemConfig(OBJ_A_NAME);
            newObjectA = (ObjA) regObj;
            regObj = _serializationManager.getItemConfig(OBJ_B_NAME);
            newObjectB = (ObjB) regObj;
            regObj = _serializationManager.getItemConfig(OBJ_C_NAME);
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
            _serializationManager.registerObject(newObjectA, newAdapterObjA);
            _serializationManager.registerObject(newObjectB, newAdapterObjB);
            _serializationManager.registerObject(newObjectC, newAdapterObjC);

            newObjectA = _serializationManager.getItemConfigAutoCast(OBJ_A_NAME);
            newObjectB = _serializationManager.getItemConfigAutoCast(OBJ_B_NAME);
            newObjectC = _serializationManager.getItemConfigAutoCast(OBJ_C_NAME);
        }catch(Exception ex){
            fail("Should not throw any exceptions upon trying to return properly registered config objects!");
        }
    }
    @Test
    void getItemConfigBulkRegisterOK() {
        var list = createObjListOK();

        try{
            _serializationManager.registerObjects(list);

            var regObj = _serializationManager.getItemConfig(OBJ_A_NAME);
            ObjA newObjectA = (ObjA) regObj;
            regObj = _serializationManager.getItemConfig(OBJ_B_NAME);
            ObjB newObjectB = (ObjB) regObj;
            regObj = _serializationManager.getItemConfig(OBJ_C_NAME);
            ObjC newObjectC = (ObjC) regObj;
        }catch(Exception ex){
            fail("Should not throw any exceptions upon trying to return properly registered config objects!");
        }
    }

    @Test
    void getItemConfigAutoCastBulkRegisterOK() {
        var list = createObjListOK();

        try{
            _serializationManager.registerObjects(list);

            ObjA newObjectA = _serializationManager.getItemConfigAutoCast(OBJ_A_NAME);
            ObjB newObjectB = _serializationManager.getItemConfigAutoCast(OBJ_B_NAME);
            ObjC newObjectC = _serializationManager.getItemConfigAutoCast(OBJ_C_NAME);
        }catch(Exception ex){
            fail("Should not throw any exceptions upon trying to return properly registered config objects!");
        }
    }
}