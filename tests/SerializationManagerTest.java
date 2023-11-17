package tests;

import objects.ObjA;
import objects.ObjB;
import objects.ObjC;
import org.junit.jupiter.api.*;
import serialization.IConfigObj;
import serialization.SerializationManager;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.fail;
import static serialization.Constants.EMPTY_CONFIG_READ_WARN;
import static serialization.Constants.EMPTY_CONFIG_SAVE_WARN;


class SerializationManagerTest {
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
    private static List<IConfigObj> createObjListOK(){
        var newObjectA = new ObjA(OBJ_A_NAME);
        var newObjectB = new ObjB(OBJ_B_NAME);
        var newObjectC = new ObjC(OBJ_C_NAME);

        var list = new LinkedList<IConfigObj>();
        list.add(newObjectA);
        list.add(newObjectB);
        list.add(newObjectC);

        return list;
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

    private static boolean chkIfFilePresent(String path){
        var pathToFile= Paths.get(path);
        return Files.exists(pathToFile);
    }

    @Test
    @Order(1)
    void registerObjectOK() {
        var newObjectA = new ObjA(OBJ_A_NAME);
        var newObjectB = new ObjB(OBJ_B_NAME);
        var newObjectC = new ObjC(OBJ_C_NAME);

        try{
            _serializationManager.registerObject(newObjectA);
            _serializationManager.registerObject(newObjectB);
            _serializationManager.registerObject(newObjectC);
        }catch(Exception ex){
            fail("Should not throw any exceptions upon trying to return properly registered config objects!");
        }
    }

    @Test
    @Order(1)
    void registerObjectsOK() {
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
        var configPath = _serializationManager.getConfigPath();
        removeConfig(configPath);

        try{
            _serializationManager.registerObjects(objects);
            _serializationManager.readConfig();
        }
        catch(Exception ex){
            fail("Should not throw any exceptions when saving default data to new file! Additional info: " + ex.getMessage());
        }

        if(!chkIfFilePresent(configPath)){
            fail("Config file was not created!");
        }
        var objectsToValidate = createObjListOK();
        for(var obj : objectsToValidate){
            var result = _serializationManager.getItemConfigAutoCast(obj.getConfigObjId());
            if(result == null){
                fail("Object of id " + obj.getConfigObjId() + " was not saved in the config!");
            }

            var isEqual = obj.equals(result);
            if(!isEqual){
                fail("Object of id " + obj.getConfigObjId() + "Has different field values than its original!");
            }
        }

    }

    @Test
    @Order(2)
    void readConfigSingle() {

    }
    @Test
    @Order(2)
    void readConfigMultiple() {

    }

    @Test
    @Order(2)
    void readConfigSingleLacksData() {

    }
    @Test
    @Order(2)
    void readConfigMultipleLacksData() {

    }
    @Test
    @Order(2)
    void readConfigMultipleLacksObject() {

    }

    @Test
    @Order(2)
    void readConfigMultipleLacksObjectOK() {

    }
    @Test
    void getItemConfigSingleRegisterOK() {
        var newObjectA = new ObjA(OBJ_A_NAME);
        var newObjectB = new ObjB(OBJ_B_NAME);
        var newObjectC = new ObjC(OBJ_C_NAME);

        try{
            _serializationManager.registerObject(newObjectA);
            _serializationManager.registerObject(newObjectB);
            _serializationManager.registerObject(newObjectC);

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

        try{
            _serializationManager.registerObject(newObjectA);
            _serializationManager.registerObject(newObjectB);
            _serializationManager.registerObject(newObjectC);

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