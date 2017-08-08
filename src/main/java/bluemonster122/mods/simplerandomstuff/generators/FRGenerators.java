package bluemonster122.mods.simplerandomstuff.generators;

import bluemonster122.mods.simplerandomstuff.core.block.BlockSRS;
import bluemonster122.mods.simplerandomstuff.generators.BlockGenerator.Types;
import bluemonster122.mods.simplerandomstuff.reference.Names;
import bluemonster122.mods.simplerandomstuff.util.IFeatureRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;

import static bluemonster122.mods.simplerandomstuff.util.ModelHelpers.registerIEnumMeta;

public class FRGenerators
        implements IFeatureRegistry {
    public static final FRGenerators INSTANCE = new FRGenerators();
    public static int Fire_RF = 1;
    public static int Sugar_Burntime = 10;
    public static int Sugar_RF = 10;
    public static BlockSRS generators = new BlockGenerator();

    private FRGenerators() {
    }

    @Override
    public void registerBlocks(IForgeRegistry<Block> registry) {
        registry.registerAll(generators);
    }

    @Override
    public void registerItems(IForgeRegistry<Item> registry) {
        registry.registerAll(generators.createItemBlock());
    }

    @Override
    public void registerTileEntities() {
        GameRegistry.registerTileEntity(TileGeneratorSugar.class, "simplerandomstuff:sugar_generator");
        GameRegistry.registerTileEntity(TileGeneratorFire.class, "simplerandomstuff:fire_generator");
    }

    @Override
    public void loadConfigs(Configuration configuration) {
        Sugar_RF = configuration.getInt(
                Names.Features.Configs.GENERATORS_SUGAR_RFPERT,
                Names.Features.GENERATORS,
                10,
                1,
                Integer.MAX_VALUE,
                "Set to any number larger than 0."
        );
        Sugar_Burntime = configuration.getInt(
                Names.Features.Configs.GENERATORS_SUGAR_BURNTIME,
                Names.Features.GENERATORS,
                10,
                1,
                Integer.MAX_VALUE,
                "Set to any number larger than 0."
        );
        Fire_RF = configuration.getInt(
                Names.Features.Configs.GENERATORS_FIRE_RFPERT,
                Names.Features.GENERATORS,
                1,
                1,
                Integer.MAX_VALUE,
                "Set to any number larger than 0."
        );
    }

    @Override
    public void registerEvents() {

    }

    @Override
    public void registerOreDict() {
        OreDictionary.registerOre("sugar", new ItemStack(Items.SUGAR));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerRenders() {
        registerIEnumMeta(generators, Types.VARIANTS);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerClientEvents() {

    }

    @Override
    public String getName() {
        return Names.Features.GENERATORS;
    }
}
