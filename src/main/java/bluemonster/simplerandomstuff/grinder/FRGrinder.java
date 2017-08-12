package bluemonster.simplerandomstuff.grinder;

import bluemonster.simplerandomstuff.core.block.BlockSRS;
import bluemonster.simplerandomstuff.reference.Names;
import bluemonster.simplerandomstuff.util.IFeatureRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

public class FRGrinder
        implements IFeatureRegistry {
    public static final FRGrinder INSTNACE = new FRGrinder();
    public static BlockSRS grinder = new BlockGrinder();

    @Override
    public void registerBlocks(IForgeRegistry<Block> registry) {
        registry.registerAll(grinder);
    }

    @Override
    public void registerItems(IForgeRegistry<Item> registry) {

    }

    @Override
    public void registerTileEntities() {

    }

    @Override
    public void loadConfigs(Configuration configuration) {

    }

    @Override
    public void registerEvents() {

    }

    @Override
    public void registerOreDict() {

    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerRenders() {

    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerClientEvents() {

    }

    @Override
    public String getName() {
        return Names.Features.GRINDER;
    }

    private FRGrinder() {
    }
}