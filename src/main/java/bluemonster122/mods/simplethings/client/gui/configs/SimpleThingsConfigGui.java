package bluemonster122.mods.simplethings.client.gui.configs;

import bluemonster122.mods.simplethings.reference.ModInfo;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.client.config.DummyConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class SimpleThingsConfigGui extends GuiConfig {
    public SimpleThingsConfigGui(GuiScreen parentScreen) {
        super(parentScreen, getConfigElements(), ModInfo.MOD_ID, false, false,
                I18n.format("simplethings.guiconfig.title")
        );
    }

    private static List<IConfigElement> getConfigElements() {
        List<IConfigElement> list = new ArrayList<IConfigElement>();
        list.add(
                new DummyConfigElement.DummyCategoryElement("Tree Farm Configs", "simplethings.guiconfig.treefarmcategory",
                        ElementTreeFarm.class
                ));
        list.add(new DummyConfigElement.DummyCategoryElement("Cobblestone Generator Configs",
                "simplethings.guiconfig.cobblestonegeneratorcategory",
                ElementCobblestoneGenerator.class
        ));
        list.add(new DummyConfigElement.DummyCategoryElement("Energy Generator Configs",
                "simplethings.guiconfig.energygeneratorcategory",
                ElementEnergyGenerator.class
        ));
        return list;
    }
}
