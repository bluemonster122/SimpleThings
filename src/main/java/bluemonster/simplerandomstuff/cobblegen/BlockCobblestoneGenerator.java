package bluemonster.simplerandomstuff.cobblegen;

import bluemonster.simplerandomstuff.core.block.BlockSRS;
import bluemonster.simplerandomstuff.reference.Names;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockCobblestoneGenerator
        extends BlockSRS
        implements ITileEntityProvider {
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileCobblestoneGenerator();
    }

    public BlockCobblestoneGenerator() {
        super(Names.Blocks.COBBLESTONE_GENERATOR, Material.IRON);
        setHardness(5f);
        setResistance(5f);
        setHarvestLevel("pickaxe", 0);
    }
}