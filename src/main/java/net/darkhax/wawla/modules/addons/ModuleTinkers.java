package net.darkhax.wawla.modules.addons;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.darkhax.wawla.modules.Module;
import net.darkhax.wawla.util.Constants;
import net.darkhax.wawla.util.Utilities;
import net.minecraft.item.ItemStack;

public class ModuleTinkers extends Module {

    private String showDryerTime = "wawla.tinkers.showDryerTime";
    private String showFurnaceItem = "wawla.tinkers.showFurnace";
    private String hideLandmine = "wawla.tinkers.hideLandmine";

    public static boolean isEnabled = false;
    public static Class classHarvestTool = null;
    public static Class classDualHarvestTool = null;
    public static Class classLandmine = null;
    public static Class classDryingRackLogic = null;
    public static Class classFurnaceLogic = null;
    public static Method getHarvestType = null;
    public static Method getSecondHarvestType = null;

    public ModuleTinkers(boolean enabled) {

        super(enabled);

        if (enabled) {

            isEnabled = enabled;

            try {

                classHarvestTool = Class.forName("tconstruct.library.tools.HarvestTool");
                classDualHarvestTool = Class.forName("tconstruct.library.tools.DualHarvestTool");
                classLandmine = Class.forName("tconstruct.mechworks.blocks.BlockLandmine");
                classDryingRackLogic = Class.forName("tconstruct.blocks.logic.DryingRackLogic");
                classFurnaceLogic = Class.forName("tconstruct.tools.logic.FurnaceLogic");
                getHarvestType = classHarvestTool.getDeclaredMethod("getHarvestType");
                getSecondHarvestType = classDualHarvestTool.getDeclaredMethod("getSecondHarvestType");
                getHarvestType.setAccessible(true);
                getSecondHarvestType.setAccessible(true);
            }

            catch (ClassNotFoundException e) {

                Constants.LOG.info("The Tinkers Construct mod can not be detected. Module ignored.");
            }

            catch (NoSuchMethodException e) {

                Constants.LOG.info("There was in issue loading the Tinkers Construct module. It will be ignored.");
            }

            catch (SecurityException e) {

                e.printStackTrace();
            }
        }
    }

    @Override
    public ItemStack onBlockOverride(ItemStack stack, IWailaDataAccessor accessor, IWailaConfigHandler config) {

        // Hides landmines
        if (config.getConfig(hideLandmine) && accessor.getTileEntity() != null && Utilities.compareByClass(classLandmine, accessor.getBlock().getClass())) {

            if (accessor.getNBTData() != null) {

                ItemStack cover = Utilities.getInventoryStacks(accessor.getNBTData(), 4)[3];

                if (cover != null)
                    return cover;
            }
        }

        return stack;
    }

    public void onWailaBlockDescription(ItemStack stack, List<String> tooltip, IWailaDataAccessor access, IWailaConfigHandler config) {

        if (access.getBlock() != null && access.getTileEntity() != null) {

            if (Utilities.compareTileEntityByClass(access.getTileEntity(), classDryingRackLogic)) {

                double percent = Utilities.round(Utilities.getProgression(access.getNBTData().getInteger("Time"), access.getNBTData().getInteger("MaxTime")), 2);

                if (percent > 0 && !(percent > 100))
                    tooltip.add("Dryness: " + percent + "%");
            }
        }
    }

    @Override
    public void onWailaRegistrar(IWailaRegistrar register) {

        if (classDryingRackLogic != null)
            register.registerSyncedNBTKey("*", classDryingRackLogic);

        if (classFurnaceLogic != null)
            register.registerSyncedNBTKey("*", classFurnaceLogic);

        register.addConfig("Tinkers", hideLandmine);
        register.addConfig("Tinkers", showDryerTime);
        register.addConfig("Tinkers", showFurnaceItem);
    }

    /**
     * This is a special method added to allow the ModuleHarvest to apply to tinkers construct ools and
     * ores.
     * 
     * @param item: The item stack being checked.
     * @param required: The tool type required for the block.
     * @return true: When the item is the right type.
     * @return false: When the item is not the right type.
     */
    public static boolean canHarvest(ItemStack item, String required) {

        if (isEnabled) {

            List<String> tooltypes = new ArrayList<String>();

            if (classDualHarvestTool.isInstance(item.getItem())) {

                try {

                    tooltypes.add((String) getSecondHarvestType.invoke(item.getItem()));
                }

                catch (Exception e) {

                    e.printStackTrace();
                }
            }

            if (classHarvestTool.isInstance(item.getItem())) {

                try {

                    tooltypes.add((String) getHarvestType.invoke(item.getItem()));
                }

                catch (Exception e) {

                    e.printStackTrace();
                }
            }

            return (tooltypes.contains(required)) ? true : false;
        }

        return false;
    }
}