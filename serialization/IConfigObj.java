package serialization;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

public interface IConfigObj {
    /**Checks if any fields were not assigned to during deserialization and
     * assigns default values to them.*/
    void ChkDefaultValues();
    /**
     * Attempts to read values from provided element.
     * @param element The read element that describes this object.
     * @param gson Deserializer in use.
     * @return Deserialized config object.
     */
    IConfigObj DeserializeConfigObj(Gson gson, JsonElement element);
    /**@param gson Serializer that will convert this object into JSON-formatted string.
     *              It has been already configured to beautify the string.
     * @return  itself in form of json string.*/
    String SerializeConfigObj(Gson gson);
    /**@return the unique ID that this object is identified with.*/
    String getConfigObjId();
}
