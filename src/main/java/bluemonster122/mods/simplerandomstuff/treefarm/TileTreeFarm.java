package bluemonster122.mods.simplerandomstuff.treefarm;

import bluemonster122.mods.simplerandomstuff.SRS;
import bluemonster122.mods.simplerandomstuff.client.renderer.BoxRender;
import bluemonster122.mods.simplerandomstuff.core.IHaveGui;
import bluemonster122.mods.simplerandomstuff.core.energy.BatteryST;
import bluemonster122.mods.simplerandomstuff.core.energy.IEnergyRecieverST;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static bluemonster122.mods.simplerandomstuff.treefarm.FRTreeFarm.INSTANCE;

public class TileTreeFarm
        extends TileEntity
        implements ITickable, IEnergyRecieverST, IHaveGui {
    private static final Vec3i[] farmedPositions = new Vec3i[]{
            new Vec3i(-3, 0, -3),
            new Vec3i(-3, 0, -2),
            new Vec3i(
                    -2,
                    0,
                    -3
            ),
            new Vec3i(-2, 0, -2),
            new Vec3i(-3, 0, 3),
            new Vec3i(-3, 0, 2),
            new Vec3i(-2, 0, 3),
            new Vec3i(-2, 0, 2),
            new Vec3i(3, 0, -3),
            new Vec3i(3, 0, -2),
            new Vec3i(2, 0, -3),
            new Vec3i(2, 0, -2),
            new Vec3i(3, 0, 3),
            new Vec3i(3, 0, 2),
            new Vec3i(2, 0, 3),
            new Vec3i(2, 0, 2)
    };

    static NonNullList<ItemStack> sapling = OreDictionary.getOres("sapling");

    static List<Item> validItems = new ArrayList<>();

    static {
        for (ItemStack stack : sapling) {
            validItems.add(stack.getItem());
        }
    }

    public ItemStackHandler inventory = new ItemStackHandler(72);

    public BatteryST battery = new BatteryST(1000000);

    public BoxRender render;

    private int currentPos = -1;

    private int nextTime = 0;

    private TreeChoppa farmer = new TreeChoppa(this);

    @Override
    public void update() {
        if (getWorld().isRemote) {
            //noinspection MethodCallSideOnly
            updateClient();
        } else {
            updateServer();
        }
    }

    public void updateServer() {
        // Mark for updates
        IBlockState state = getWorld().getBlockState(getPos());
        getWorld().notifyBlockUpdate(getPos(), state, state, 3);
        markDirty();

        // Work
        if (SRS.isDev) battery.setEnergy(500000);
        nextTime--;
        if (nextTime > 0) return;
        currentPos++;
        if (currentPos == -1 || currentPos >= farmedPositions.length) {
            currentPos = 0;
        }
        BlockPos current = getPos().add(farmedPositions[currentPos]);
        int energyReq = farmer.scanTree(current, false) * INSTANCE.getBreakEnergy();
        if (battery.getEnergyStored() >= energyReq) {
            battery.extractEnergy(energyReq, false);
            farmer.scanTree(current, true);
            if (world.isAirBlock(current)) plantSapling();
        }
        nextTime = 5;
    }

    @SuppressWarnings("deprecation")
    private void plantSapling() {
        sapling.forEach(i -> validItems.add(i.getItem()));
        for (int i = 0; i < inventory.getSlots(); i++) {
            BlockPos thisPos = getPos().add(farmedPositions[currentPos]);
            ItemStack stack = inventory.getStackInSlot(i);
            Item item = stack.getItem();
            if (validItems.contains(item) && item instanceof ItemBlock) {
                Block block = ((ItemBlock) item).getBlock();
                if (block instanceof IPlantable) {
                    IBlockState dirt = getWorld().getBlockState(thisPos.down());
                    if (dirt.getBlock()
                            .canSustainPlant(dirt, getWorld(), thisPos.down(), EnumFacing.UP, (IPlantable) block
                            ) && world.setBlockState(thisPos, block.getStateFromMeta(stack.getMetadata()), 3)) {
                        stack.shrink(1);
                    }
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void updateClient() {
        if (this.currentPos < 0 || this.currentPos >= farmedPositions.length) return;
        if (render != null) render.cleanUp();
        BlockPos current = pos.add(farmedPositions[this.currentPos]);
        render = BoxRender.create(
                new Color(0, 255, 255, 50),
                new Vec3d(current.getX() - 0.0005f, current.getY() - 0.0005f, current.getZ() - 0.0005f),
                new Vec3d(current.getX() + 1.0005f, current.getY() + 1.0005f, current.getZ() + 1.0005f)
        );
        render.show();
    }

    @Override
    public BatteryST getBattery() {
        return battery;
    }

    @Override
    public void setBattery(BatteryST battery) {
        this.battery = battery;
    }

    @Override
    public BatteryST createBattery() {
        return new BatteryST(1000000);
    }

    @Override
    public Gui createGui(InventoryPlayer player, World world, BlockPos pos) {
        //noinspection NewExpressionSideOnly
        return new GuiTreeFarm(player, this);
    }

    @Override
    public Container createContainer(InventoryPlayer player, World world, BlockPos pos) {
        return new ContainerTreeFarm(player, this);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        currentPos = tag.getInteger("scanner");
        nextTime = tag.getInteger("timer");
        battery.setEnergy(tag.getInteger("energyStored"));
        inventory.deserializeNBT(tag.getCompoundTag("inventory"));
        super.readFromNBT(tag);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag.setInteger("scanner", currentPos);
        tag.setInteger("timer", nextTime);
        tag.setInteger("energyStored", getBattery().getEnergyStored());
        tag.setTag("inventory", inventory.serializeNBT());
        return super.writeToNBT(tag);
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(getPos(), 0, getUpdateTag());
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    @Override
    public void invalidate() {
        if (render != null) //noinspection MethodCallSideOnly
        {
            render.cleanUp();
        }
        super.invalidate();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity nbt) {
        handleUpdateTag(nbt.getNbtCompound());
    }

    @Override
    public void handleUpdateTag(NBTTagCompound tag) {
        readFromNBT(tag);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        if (capability.equals(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)) {
            return true;
        } else if (capability.equals(CapabilityEnergy.ENERGY)) {
            return true;
        } else {
            return super.hasCapability(capability, facing);
        }
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability.equals(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)) {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inventory);
        } else if (capability.equals(CapabilityEnergy.ENERGY)) {
            return CapabilityEnergy.ENERGY.cast(getBattery());
        } else {
            return super.getCapability(capability, facing);
        }
    }

    public void dropContents() {
        for (int slot = inventory.getSlots() - 1; slot >= 0; slot--) {
            getWorld().spawnEntity(new EntityItem(
                    getWorld(),
                    getPos().getX() + 0.5,
                    getPos().getY() + 0.5,
                    getPos().getZ() + 0.5,
                    inventory.getStackInSlot(slot)
            ));
        }
    }
}
