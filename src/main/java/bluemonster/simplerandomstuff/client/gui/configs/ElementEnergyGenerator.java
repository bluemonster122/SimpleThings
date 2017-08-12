package bluemonster.simplerandomstuff.client.gui.configs;

import bluemonster.simplerandomstuff.handler.ConfigurationHandler;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ElementEnergyGenerator
        extends GuiConfigEntries.CategoryEntry {
    @Override
    protected GuiScreen buildChildScreen() {
        return new GuiConfig(
                this.owningScreen,
                (new ConfigElement(ConfigurationHandler.INSTANCE.configuration.getCategory("power_generators"))).getChildElements(),
                this.owningScreen.modID,
                "power_generators",
                this.configElement.requiresWorldRestart() || this.owningScreen.allRequireWorldRestart,
                this.configElement.requiresMcRestart() || this.owningScreen.allRequireMcRestart,
                GuiConfig.getAbridgedConfigPath(ForgeModContainer.getConfig()
                        .toString())
        );
    }

    public ElementEnergyGenerator(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
        super(owningScreen, owningEntryList, configElement);
    }
}