package eu.ha3.matmos;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.function.Predicate;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

import eu.ha3.matmos.util.DefaultConfigHelper;
import eu.ha3.matmos.util.VersionDependentConstants;
import eu.ha3.util.property.simple.ConfigProperty;
import net.minecraft.launchwrapper.Launch;

import static eu.ha3.matmos.util.MAtUtil.getParentSafe;

/**
 * Class for statically initializing and accessing the config. Required to make
 * it possible to access the config from the coremod/tweaker.
 */

public class ConfigManager {

    private static final Logger LOGGER = LogManager.getLogger("matmos");

    private static final ConfigProperty config = new ConfigProperty();
    private static boolean hasInitialized = false;
    
    public static final int DEFAULT_KEY = Keyboard.KEY_F4;
    
    private static final DefaultConfigHelper DEFAULT_CONFIG_HELPER = new DefaultConfigHelper("matmos");

    private static File configFolder = null;

    private static void initConfig() {
        // Create default configuration

        config.setProperty("world.height", 256);
        config.setProperty("start.enabled", true, "If false, MAtmos won't start until the MAtmos key is pressed.");
        config.setProperty("reversed.controls", false);
        config.setProperty("sound.autopreview", true);
        config.setProperty("globalvolume.scale", 1f);
        config.setProperty("key.code", DEFAULT_KEY);
        config.setProperty("useroptions.altitudes.high", true);
        config.setProperty("useroptions.altitudes.low", true);
        config.setProperty("useroptions.biome.override", -1);
        config.setProperty("debug.mode", 0);
        config.setProperty("minecraftsound.ambient.volume", 1f);
        config.setProperty("coremod.replacesoundsystem", "auto",
                "There's a bug in Minecraft's sound system that causes it to crash after some time if looping streams are played.\n"
                        + "Forge provides a fix for this in 1.12.2, but MAtmos has to provide its own fix on 1.7.10, and on LiteLoader versions.\n"
                        + "Use this option to control when the SoundSystem should be overridden.\n\n"
                        + "Allowed values are: always, never, auto (which only overrides if no other mod is present which also overrides it (like DynamicSurroundings on 1.7.10, or Forge itself on 1.12.2))");
        config.setProperty("soundsystem.changechannelcount", "auto",
                "Configure sound system to be able to play more sounds at once. Note: Regardless of the setting, this can get overrriden by mods that change it after us (e.g. Dynamic Surroundings)\n" +
                "True: always do this\n" +
                "Auto: don't do this if another mod has done it before us\n" +
                "False: never do this");
        config.setProperty("soundsystem.changechannelcount.normal", -1, "The number of normal channels (e.g. sound effects). The vanilla value is 28. Set to -1 to decide automatically.");
        config.setProperty("soundsystem.changechannelcount.streaming", -1, "The number of streaming channels (e.g. music, longer soundpack sounds). The vanilla value is 4. Set to -1 to decide automatically.");
        config.setProperty("soundsystem.changestreamqueueformatsmatch", true, "I forgot what this does, but it probably makes things faster. It is not known to cause any issues, but can be disabled here just in case.");
        config.setProperty("dimensions.list", "",
                "Comma-separated list of dimensions. If dimensions.listtype is black, then ambience will NOT be played in these dimensions.\n"
                        + "If it's white, then ambience will ONLY play in these dimensions.\n");
        config.setProperty("dimensions.listtype", "black", "BLACK or WHITE?\n");
        config.setProperty("rain.suppress", "auto",
                "Use this option to control how the conflict should be resolved between MAtmos rain sounds\n" +
                "and rain sounds from vanilla or other mods.\n" +
                "True: rain from other sources is muted\n" +
                "False: rain is muted from MAtmos soundpacks which support this option\n" +
                "Auto: true if there's at least one soundpack which supports this option present, false otherwise\n");
        config.setProperty("rain.soundlist", VersionDependentConstants.RAIN_BLACKLIST,
                "Comma-separated list of rain sounds to suppress if rain.suppress is true");
        config.setProperty("music.overlap", false,
                "Whether soundpack music may play over vanilla music. A soundpack sound is music when its sound event\n" +
                "is in the music category in sounds.json. If false, vanilla music waits while soundpack music plays,\n" +
                "and a vanilla track that is already playing fades out when soundpack music starts.\n");
        config.setProperty("rain.strengththreshold", "-1",
                "Rain strength threshold above which it's considered to be raining by soundpacks\n" +
                "Range: 0~1, or -1 to use the default setting, which is 0.2 in vanilla\n" +
                "Set this to something low like 0 for better compatibility with Weather2\n"
                );
        config.setProperty("dealias.oredict", "true",
                "Dealias oredicted blocks to the lowest id block of the oredict group."
                );
        config.setProperty("dealias.guessfromclass", "true",
                "Guess the aliases of items from their class and name"
                );
        config.setProperty("log.category.aliasmap", "false",
                "Print log messages about alias map construction"
                );
        config.setProperty("log.printcrashestochat", "true",
                "Display message in chat when MAtmos crashes"
                );
        config.commit();

        config.setGlobalDescription("Tip: restart MAtmos to reload the configs without restarting Minecraft");

        // Load configuration from source
        try {
            config.setSource(new File(getConfigFolder(), "userconfig.cfg").getCanonicalPath());
            config.load();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error caused config not to work: " + e.getMessage());
        }

        hasInitialized = true;
    }

    public static ConfigProperty getConfig() {
        if (!hasInitialized) {
            initConfig();
        }
        return config;
    }

    public static File getConfigFolder() {
        if (configFolder == null) {
            configFolder = new File(Launch.minecraftHome, "config/matmos");
        }
        return configFolder;
    }
    
    public static DefaultConfigHelper getDefaultConfigHelper() {
        return DEFAULT_CONFIG_HELPER;
    }
}
