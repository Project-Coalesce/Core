package com.coalesce.ttb.config;

import com.coalesce.config.yml.YamlEntry;
import com.coalesce.config.yml.YamlConfig;
import com.coalesce.plugin.CoPlugin;
import org.bukkit.Material;


public final class FontsConfig extends YamlConfig {
	
	private final CoPlugin plugin;
	private final YamlConfig config;
	
	public FontsConfig(CoPlugin plugin) {
		super("config", plugin);
		this.plugin = plugin;
		this.config = (FontsConfig) plugin.getConfig("config");
		config.addEntry(new YamlEntry(config, "font.maxFontSize", 100));
		config.addEntry(new YamlEntry(config, "font.fallbackFontSize", 12));
		config.addEntry(new YamlEntry(config, "font.fallbackFont", "blocked"));
		config.addEntry(new YamlEntry(config, "font.fallbackMaterial", Material.QUARTZ_BLOCK.name()));
		config.addEntry(new YamlEntry(config, "operations.historySize", 10));
	}
	
	public int getMaxFontSize() {
		return (int) getEntry("font.maxFontSize").getValue();
	}
	
	public void setMaxFontSize(int maxFontSize) {
		getEntry("font.maxFontSize").setValue(maxFontSize);
	}
	
	public int getMaxOperations() {
		return (int) getEntry("operations.historySize").getValue();
	}
	
	public void setMaxOperations(int maxOperations) {
		getEntry("operations.historySize").setValue(maxOperations);
	}
	
	public String getFallbackFont() {
		return (String) config.getEntry("font.fallbackFont").getValue();
	}
	
	public void setFallbackFont(String fallbackFont) {
		getEntry("font.fallbackFont").setValue(fallbackFont);
	}
	
	public float getFallbackFontSize() {
		return (float) getEntry("font.fallbackFontSize").getValue();
	}
	
	public void setFallbackFontSize(float fallbackFontSize) {
		config.getEntry("font.fallbackFontSize").setValue(fallbackFontSize);
	}
	
	public Material getFallbackMaterial() {
		return (Material) getEntry("font.fallbackMaterial").getValue();
	}
	
	public void setFallbackMaterial(Material material) {
		getEntry("font.fallbackMaterial").setValue(material.name());
	}
	
}
