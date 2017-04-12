package bluemonster122.mods.simplethings.tanks;

import bluemonster122.mods.simplethings.SimpleThings;
import bluemonster122.mods.simplethings.core.block.IEnumMeta;
import bluemonster122.mods.simplethings.reference.ModInfo;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemTankUpgrade extends Item {
    public ItemTankUpgrade() {
        super();
        setRegistryName("tank_upgrade");
        setHasSubtypes(true);
        setCreativeTab(SimpleThings.theTab);
    }


    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (world.isRemote) return EnumActionResult.SUCCESS;
        IBlockState state = world.getBlockState(pos);
        ItemStack stack = player.getHeldItem(hand);
        if (state.getBlock().equals(FRTank.tank)) {
            switch (state.getValue(BlockTank.VARIANT)) {
                case GLASS:
                    if (Types.GLASS_TO_IRON.getMeta() == stack.getMetadata()){
                        world.setBlockState(pos, state.withProperty(BlockTank.VARIANT, BlockTank.Types.IRON), 3);
                        stack.shrink(1);
                        player.setHeldItem(hand, stack);
                    }
                    break;
                case IRON:
                    if (Types.IRON_TO_GOLD.getMeta() == stack.getMetadata()){
                        world.setBlockState(pos, state.withProperty(BlockTank.VARIANT, BlockTank.Types.GOLD), 3);
                        stack.shrink(1);
                        player.setHeldItem(hand, stack);
                    }
                    break;
                case GOLD:
                    if (Types.GOLD_TO_OBSIDIAN.getMeta() == stack.getMetadata()){
                        world.setBlockState(pos, state.withProperty(BlockTank.VARIANT, BlockTank.Types.OBSIDIAN), 3);
                        stack.shrink(1);
                        player.setHeldItem(hand, stack);
                    }
                    break;
                case OBSIDIAN:
                    if (Types.OBSIDIAN_TO_DIAMOND.getMeta() == stack.getMetadata()){
                        world.setBlockState(pos, state.withProperty(BlockTank.VARIANT, BlockTank.Types.DIAMOND), 3);
                        stack.shrink(1);
                        player.setHeldItem(hand, stack);
                    }
                    break;
                case DIAMOND:
                    break;
            }
        }
        return EnumActionResult.SUCCESS;
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        return String.format("item.%s:%s.%s", ModInfo.MOD_ID, "tank_upgrade", Types.byMeta(itemStack.getMetadata()).getName());
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, NonNullList<ItemStack> itemList) {
        for (Types type : Types.VARIANTS) {
            itemList.add(new ItemStack(item, 1, type.getMeta()));
        }
    }

    public enum Types implements IEnumMeta {
        GLASS_TO_IRON,
        IRON_TO_GOLD,
        GOLD_TO_OBSIDIAN,
        OBSIDIAN_TO_DIAMOND;

        protected static final Types[] VARIANTS = values();
        private int meta;

        Types() {
            meta = ordinal();
        }

        public static Types byMeta(int meta) {
            return VARIANTS[Math.abs(meta) % VARIANTS.length];
        }

        @Override
        public int getMeta() {
            return meta;
        }
    }
}