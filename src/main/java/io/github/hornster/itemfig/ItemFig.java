package io.github.hornster.itemfig;

import com.mojang.logging.LogUtils;
import io.github.hornster.itemfig.api.serialization.ItemFigApi;
import io.github.hornster.itemfig.serialization.SerializationManager;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// The value here should match an entry in the META-INF/mods.toml file
public class ItemFig implements ModInitializer
{
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "itemfig";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        LOGGER.info("ItemFig getting up!");
    }
}
