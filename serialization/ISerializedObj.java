package serialization;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

public interface ISerializedObj  {
    /**Checks if any fields were not assigned to during deserialization and
     * assigns default values to them.*/
    void ChkDefaultValues();
    /**Attempts to read values from provided element.*/
    void Deserialize(Gson gson, JsonElement element);
    /**Returns itself in form of json string.*/
    String Serialize(Gson gson);
    /**Returns the ID that this object is identified as.*/
    String getId();
}
