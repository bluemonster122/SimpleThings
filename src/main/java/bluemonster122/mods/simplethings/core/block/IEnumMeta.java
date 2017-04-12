package bluemonster122.mods.simplethings.core.block;

import net.minecraft.util.IStringSerializable;

public interface IEnumMeta extends IStringSerializable {

    int getMeta();

    default String getName() {
        return ((Enum) this).name().toLowerCase();
    }
}