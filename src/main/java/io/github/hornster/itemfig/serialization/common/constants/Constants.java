package io.github.hornster.itemfig.serialization.common.constants;

public class Constants {
    public static final String EMPTY_CONFIG_SAVE_WARN = "Saved config is empty! You ought to register something before calling reading methods and set recreation flag to true.";    public static final String EMPTY_READ_CONFIG_WARN = "";
    public static final String EMPTY_CONFIG_READ_WARN = "Read config is empty! You ought to run the deserialization with the config recreation flag set to true.";
    public static final String NO_CONFIG_FILES_WARN = "No config files registered! Config file is registered automatically upon registering first config object that is to be saved in it.";
    public static final String CONFIG_OBJ_CANNOT_BE_NULL_EX = "Registered object cannot be null!";


    public static final String ID_FIELD_NAME = "_myID";
    public static final String JSON_EXTENSION = ".json";
    public static final String DEFAULT_JSON_FILE_NAME = "itemfig-mod-config";
}
