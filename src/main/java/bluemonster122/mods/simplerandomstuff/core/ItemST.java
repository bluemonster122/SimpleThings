package bluemonster122.mods.simplerandomstuff.core;

import bluemonster122.mods.simplerandomstuff.SimpleRandomStuff;
import net.minecraft.item.Item;

public class ItemST extends Item {
    public ItemST(String name, boolean hasSubTypes) {
        setRegistryName(name);
        setHasSubtypes(hasSubTypes);
        setCreativeTab(SimpleRandomStuff.theTab);
    }
}