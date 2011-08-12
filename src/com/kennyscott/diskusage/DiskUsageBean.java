
package com.kennyscott.diskusage;

import java.io.Serializable;
import java.util.Formatter;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import javax.faces.bean.ManagedBean;

@ManagedBean
public class DiskUsageBean implements Serializable {

	private static final long serialVersionUID = -4573964325790558574L;
	private int size = 0;
	private Map<String, DiskUsageBean> dirs = null;
	private String name = null;

	public DiskUsageBean() {
		this( 0 );
	}

	public DiskUsageBean( String name ) {
		this( 0 );
		this.name = name;
	}

	public DiskUsageBean( int size ) {
		this.setSize( size );
		dirs = new TreeMap<String, DiskUsageBean>();
	}

	public String getName() {
		return name;
	}

	public void setName( String name ) {
		this.name = name;
	}

	public Map<String, DiskUsageBean> getDirs() {
		return dirs;
	}

	public void setDirs( Map<String, DiskUsageBean> dirs ) {
		this.dirs = dirs;
	}

	public int getSize() {
		return size;
	}

	public void setSize( int size ) {
		this.size = size;
	}

	public void addDiskUsageBean( String key, DiskUsageBean dub ) {
		dirs.put( key, dub );
	}

	public DiskUsageBean getDiskUsageBean( String key ) {
		return dirs.get( key );
	}

	public String getSizeInGb() {
		double gb = size / (double) 1000000;
		StringBuilder sb = new StringBuilder();
		Formatter formatter = new Formatter( sb, Locale.UK );
		formatter.format( "%10.3f GB", gb );
		return sb.toString();
	}

}
