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

    public static void registerObject(ConfigObj object, ConfigObjAdapter<?> adapter) {
        _serializationManager.registerObject(object, adapter);
    }

    public static void registerObjects(List<Pair<ConfigObj, ConfigObjAdapter<?>>> objects){
        _serializationManager.registerObjects(objects);
    }

    public static void registerSaveErrorHandler(Consumer<String> handler) {
        _serializationManager.registerSaveErrorHandler(handler);
    }
    public static void registerSaveWarningHandler(Consumer<String> handler) {
        _serializationManager.registerSaveWarningHandler(handler);
    }

    public static void registerReadErrorHandler(Consumer<String> handler) {
        _serializationManager.registerReadErrorHandler(handler);
    }
    public static void registerReadWarningHandler(Consumer<String> handler) {
        _serializationManager.registerReadWarningHandler(handler);
    }

    /**
     * Sets the name of the result json file. Extension is not needed.
     */
    public static void setConfigFileName(String name) {
        _serializationManager.setConfigFileName(name);
    }
    public static String getConfigFileName(){
        return _serializationManager.getConfigFileName();
    }
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
