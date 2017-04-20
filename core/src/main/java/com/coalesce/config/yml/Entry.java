package com.coalesce.config.yml;

import com.coalesce.config.IConfig;
import com.coalesce.config.IEntry;

import java.io.IOException;

public final class Entry implements IEntry {
	
	private String key;
	private Object value;
	private final IConfig config;
	
	public Entry(IConfig config, String key) {
		this.config = config;
		this.key = key;
	}
	
	public Entry(IConfig config, String key, Object value) {
		this.config = config;
		this.key = key;
		this.value = value;
	}
	
	@Override
	public String getPath() {
		return key;
	}
	
	@Override
	public Object getValue() {
		return value;
	}
	
	@Override
	public IEntry setPath(String newpath) {
		remove();
		setValue(newpath, value);
		return this;
	}
	
	@Override
	public IEntry setValue(Object value) {
		setValue(key, value);
		return this;
	}
	
	@Override
	public IConfig getConfig() {
		return config;
	}
	
	@Override
	public void remove() {
		setValue(key, null);
	}
	
	private void setValue(String key, Object value) {
		YmlConfig cfg = ((YmlConfig)config);
		cfg.getBase().set(key, value);
		try {
			cfg.getBase().save(cfg.getFile());
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
