package me.superdextor.ua.main.config;

import me.superdextor.ua.main.ModUgandanAdventures;
import me.superdextor.ua.main.ReferenceUA;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;

public class GuiConfigUA extends GuiConfig
{
    public GuiConfigUA(GuiScreen parent)
    {
    	super(parent,
        		new ConfigElement(
        				ModUgandanAdventures.config.getConfig().getCategory(Configuration.CATEGORY_GENERAL))
        		.getChildElements(),
        		ReferenceUA.MOD_ID, 
        		false, 
        		false, 
    			"Ugandan Adventures 1.11.2 Configuration");
    	this.titleLine2 = ModUgandanAdventures.config.getConfig().getConfigFile().getAbsolutePath();
    }
    
    @Override
    public void onGuiClosed()
    {
    	super.onGuiClosed();
    	ModUgandanAdventures.config.getConfig().save();
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        super.actionPerformed(button);
        
        try
        {
        	ModUgandanAdventures.config.updateConfigValues();
        }
        catch (Exception e)
        {
        	
		}
    }
}
