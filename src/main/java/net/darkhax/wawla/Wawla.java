package net.darkhax.wawla;

import java.util.ArrayList;
import java.util.List;

import net.darkhax.icse.ICSE;
import net.darkhax.wawla.common.CommonProxy;
import net.darkhax.wawla.engine.ICSEEngine;
import net.darkhax.wawla.engine.InfoEngine;
import net.darkhax.wawla.lib.Constants;
import net.darkhax.wawla.lib.WawlaConfiguration;
import net.darkhax.wawla.plugins.InfoProvider;
import net.darkhax.wawla.plugins.generic.tiles.PluginBlastResistance;
import net.darkhax.wawla.plugins.generic.tiles.PluginBreakProgression;
import net.darkhax.wawla.plugins.generic.tiles.PluginEnchantmentPower;
import net.darkhax.wawla.plugins.generic.tiles.PluginHardness;
import net.darkhax.wawla.plugins.generic.tiles.PluginHarvestability;
import net.darkhax.wawla.plugins.vanilla.entities.PluginArmorPoints;
import net.darkhax.wawla.plugins.vanilla.entities.PluginEquipment;
import net.darkhax.wawla.plugins.vanilla.entities.PluginTameable;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Constants.MODID, name = Constants.MOD_NAME, version = Constants.VERSION_NUMBER, acceptableRemoteVersions = "*", acceptedMinecraftVersions = "[1.9,1.9.2]", dependencies = "required-after:ICSE")
public class Wawla {
    
    @SidedProxy(serverSide = Constants.SERVER, clientSide = Constants.CLIENT)
    public static CommonProxy proxy;
    
    @Mod.Instance(Constants.MODID)
    public static Wawla instance;
    
    public static InfoEngine engine;
    public static final List<InfoProvider> tileProviders = new ArrayList<InfoProvider>();
    public static final List<InfoProvider> entityProviders = new ArrayList<InfoProvider>();
    
    @EventHandler
    public void preInit (FMLPreInitializationEvent event) {
        
        if (Loader.isModLoaded("ICSE")) {
            
            engine = new ICSEEngine();
            ICSE.plugins.add((ICSEEngine) engine);
        }
        
        // Generic Tiles
        tileProviders.add(new PluginBreakProgression());
        tileProviders.add(new PluginEnchantmentPower());
        tileProviders.add(new PluginBlastResistance());
        tileProviders.add(new PluginHardness());
        tileProviders.add(new PluginHarvestability());
        
        // Generic Entities
        entityProviders.add(new PluginEquipment());
        entityProviders.add(new PluginArmorPoints());
        entityProviders.add(new PluginTameable());
        
        new WawlaConfiguration(event.getSuggestedConfigurationFile());
        proxy.preInit();
    }
}