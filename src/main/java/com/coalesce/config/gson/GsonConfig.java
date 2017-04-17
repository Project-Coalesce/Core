package com.coalesce.config.gson;

import com.coalesce.config.IConfig;
import com.coalesce.config.IEntry;

import java.io.File;
import java.util.Collection;

public class GsonConfig implements IConfig {
	
	@Override
	public IEntry getEntry(String path) {
		return null;
	}
	
	@Override
	public IEntry getEntryFromValue(Object value) {
		return null;
	}
	
	@Override
	public Collection<IEntry> getEntries() {
		return null;
	}
	
	@Override
	public void addEntry(IEntry entry) {
		
	}
	
	@Override
	public void removeEntry(IEntry entry) {
		
	}
	
	@Override
	public IConfig getConfig() {
		return null;
	}
	
	@Override
	public void clear() {
		
	}
	
	@Override
	public void backup() {
		
	}
	
	@Override
	public void delete() {
		
	}
	
	@Override
	public String getName() {
		return null;
	}
	
	@Override
	public File getFile() {
		return null;
	}
}
