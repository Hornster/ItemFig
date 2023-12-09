package net.crazedaerialcable.itemfig.api.serialization;

import net.crazedaerialcable.itemfig.serialization.SerializationManager;
import net.crazedaerialcable.itemfig.serialization.config.ConfigObj;
import net.crazedaerialcable.itemfig.serialization.config.ConfigObjAdapter;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.function.Consumer;

/**
 * The main class of the mod where magic is done and dragons reside.
 * <br><br>
 * Usual order of operations:
 * <br>
 * <ul>
 *     <li>Register config objects for your items and their respective adapters.</li>
 *     <li>Register message handlers if logging in .log file is not enough.</li>
 *     <li>Set config file name. You can also set path. By default the config lands in /config/ folder.</li>
 *     <li>Configure the serialization manager with flags if you need to.</li>
 *     <li>Call {@link #readConfig()} to attempt reading the file.</li>
 *     <li>Read contents using {@link #getItemConfig(String)} and/or {@link #getItemConfigAutoCast(String)} methods.</li>
 * </ul>
 * <br><br>
 * By default, if config file is absent, it is automatically recreated using defined default values. Any missing
 * values or registered objects will be added to the config file, too, using defined by you defaults.
 * */
public class ItemFigApi{
    /**
     * A!! :j
     * */
    private static SerializationManager _serializationManager = new SerializationManager();

    /**
     * Force saving of the config no matter if any deserialization errors occurred.
     * DANGER OF LOSING DATA IF SET TO TRUE!
     * By default, false.
     */
    public static void setForceConfigSave(boolean shouldForceConfigSave){
        _serializationManager._forceConfigSave = shouldForceConfigSave;
    }
    /**
     * Update the config with default values and new items if any were added.
     * Will not work if deserialization errors occurred.
     * By default, true.
     */
    public static void setUpdateConfigSave (boolean shouldUpdateConfigSave){
        _serializationManager._updateConfigSave = shouldUpdateConfigSave;
    }
    /**
     * Wipe current config clean and recreate new one from default values.
     * By default, false.
     */
    public static void setRecreateConfig(boolean shouldRecreateConfig){
        _serializationManager._recreateConfig = shouldRecreateConfig;
    }
    /**
     * Registers a config object for reading and saving. The param type of the adapter
     * has to be the same as the one of registered object.*/
    public static void registerObject(ConfigObj object, ConfigObjAdapter<?> adapter) {
        _serializationManager.registerObject(object, adapter);
    }
    /**
     * Registers multiple objects utilizing a list of pairs. The param type of the adapters
     * have to be the same as the ones of registered objects'.*/
    public static void registerObjects(List<Pair<ConfigObj, ConfigObjAdapter<?>>> objects){
        _serializationManager.registerObjects(objects);
    }
    /**
     * If logging of a serialization (saving) error is not enough, you can
     * attach a handler here. Errors mean something went wrong and
     * something probably failed to be saved.
     * @param handler The string argument will contain the error message.*/
    public static void registerSaveErrorHandler(Consumer<String> handler) {
        _serializationManager.registerSaveErrorHandler(handler);
    }
    /**
     * If logging of a serialization (saving) warning is not enough, you can
     * attach a handler here. Warnings are reported when something probably unwanted
     * happens, but does not cause errors or exceptions. An example would be a warning
     * about an empty file saving (no registered objects).
     * @param handler The string argument will contain the error message.*/
    public static void registerSaveWarningHandler(Consumer<String> handler) {
        _serializationManager.registerSaveWarningHandler(handler);
    }
    /**
     * If logging of a deserialization (loading) error is not enough, you can
     * attach a handler here. Errors mean something went wrong and
     * something probably failed to be loaded.
     * @param handler The string argument will contain the error message.*/
    public static void registerReadErrorHandler(Consumer<String> handler) {
        _serializationManager.registerReadErrorHandler(handler);
    }
    /**
     * If logging of a deserialization (load) warning is not enough, you can
     * attach a handler here. Warnings are reported when something probably unwanted
     * happens, but does not cause errors or exceptions. An example would be a warning
     * about an empty file being read (no saved objects).
     * @param handler The string argument will contain the error message.*/
    public static void registerReadWarningHandler(Consumer<String> handler) {
        _serializationManager.registerReadWarningHandler(handler);
    }

    /**
     * Sets the name of the result json file. Extension is not needed.
     */
    public static void setConfigFileName(String name) {
        _serializationManager.setConfigFileName(name);
    }
    /**
     * Returns the name of the config file, with .json extension attached even if it was
     * not provided earlier upon changing the config name.*/
    public static String getConfigFileName(){
        return _serializationManager.getConfigFileName();
    }
    /**
     * Returns the absolute path to the folder where the config will be saved, without
     * the name of the config itself.*/
    public static String getConfigPath(){
        return _serializationManager.getConfigPath();
    }
    /**
     * Read config from the config file. If config is not found, it will be recreated using default values
     * of registered objects. If read config lacks data, it will be added automatically.
     */
    public static void readConfig(){
        _serializationManager.readConfig();
    }
    /**
     * Returns the config item. Does NOT cause a read. readConfig() method should be called before using this one.
     * @param itemId ID of the item that shall be retrieved.*/
    public static ConfigObj getItemConfig(String itemId){
        return _serializationManager.getItemConfig(itemId);
    }
    /**
     * Gets the config for provided ID. Should be called after saveConfig method.
     *
     * @param itemId ID of the registered item used to find the item.
     * @return Automatically casts returned config object to provided type.
     */
    public static <T extends ConfigObj> T getItemConfigAutoCast(String itemId){
        return _serializationManager.getItemConfigAutoCast(itemId);
    }
    /**Removes current manager, creating a blank, clean one. All configuration is lost.*/
    public static void resetManager(){
        _serializationManager = new SerializationManager();
    }
}
