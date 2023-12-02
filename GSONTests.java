import com.google.gson.Gson;
import objects.*;
import serialization.config.ConfigObj;
import serialization.SerializationManager;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;

public class GSONTests {
    private static String _jsonName = "testJson.json";

    public static void writeData(){
        var objA = new ObjA("ObjA", NewVals.OBJA_PARAMA1, NewVals.OBJA_PARAMA2);
        var objA2 = new ObjA("ObjA2", NewVals.OBJA_PARAMA1, NewVals.OBJA_PARAMA2);
        var objB = new ObjB("ObjB", NewVals.OBJB_PARAMB1, NewVals.OBJB_PARAMB2);
        var objC = new ObjC("ObjC", NewVals.OBJC_PARAMC1, NewVals.OBJCC_PARAMC1, NewVals.OBJCC_PARAMC2);

        var dataObj = new DataObj();

        var list = new LinkedList<ConfigObj> ();
        list.add(objA);
        list.add(objA2);
        list.add(objB);
        list.add(objC);

        dataObj.objects = list;

        var testReg = new SerializationManager();


//        testReg.registerObjects(list);
//        testReg.readConfig();
//
//        var obj = testReg.getItemConfigAutoCast("ObjA");

    }
    public static void readData(){
        String readData = "";
        try{
            var path = Paths.get(_jsonName);
            var readLines = Files.readAllLines(path);
            readData = new StringBuilder().append(readLines).toString();
            var gson = new Gson();
            gson.fromJson(new FileReader(_jsonName), DataObj.class);
        }
        catch(IOException ioe){
            var eh = ioe;
        }
        var gson = new Gson();
        var result = gson.fromJson(readData, DataObj.class);
        System.out.println(result);
    }
    public static void main(String[] args){
        writeData();
        readData();
    }
}
