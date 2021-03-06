package net.darkhax.wawla;

import java.text.DecimalFormat;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.FMLNetworkConstants;

@Mod("wawla")
public class Wawla {
    
    public static final Logger LOG = LogManager.getLogger("WAWLA");
    public static final DecimalFormat FORMAT = new DecimalFormat("#.##");
    
    public Wawla() {
        
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of( () -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));
        
        if (!ModList.get().isLoaded("waila")) {
            
            LOG.error("Hwyla is not installed, this is a required mod! Their 1.16.2 file works on 1.16.5! https://www.curseforge.com/minecraft/mc-mods/hwyla/files/all?filter-game-version=2020709689%3A7498");
        }
    }
}