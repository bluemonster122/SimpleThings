package bluemonster122.simplethings.util;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.tileentity.TileEntity;

public interface ITileEntityProvider1 extends ITileEntityProvider
{
	Class<? extends TileEntity> getTileClass();
}
