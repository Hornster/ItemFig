import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import objects.*;
import serialization.ISerializedObj;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;

public class GSONTests {
    private static String _jsonName = "testJson.json";

    public static void writeData(){
        var objA = new ObjA(NewVals.OBJA_PARAMA1, NewVals.OBJA_PARAMA2);
        var objB = new ObjB(NewVals.OBJB_PARAMB1, NewVals.OBJB_PARAMB2);
        var objC = new ObjC(NewVals.OBJC_PARAMC1, NewVals.OBJCC_PARAMC1, NewVals.OBJCC_PARAMC2);

        var dataObj = new DataObj();

        var list = new LinkedList<ISerializedObj> ();
        list.add(objA);
        list.add(objB);
        list.add(objC);

        dataObj.objects = list;

        Gson gson;
        gson = new Gson();
        var jsonData = gson.toJson(dataObj);

        try{
            var fileWriter = new FileWriter(_jsonName);
            fileWriter.write(jsonData);
            fileWriter.close();
        }
        catch(IOException ioe){
            var eh = ioe;
        }
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
