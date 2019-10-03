package com.balicamp.model.common;

import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.balicamp.Constants;

/**
 * @author <a href="mailto:aananto@balicamp.com">Arif Puji Ananto</a>
 */
public class FileLocal 
	extends BaseObject
{
	
	private static final long serialVersionUID = 1683083151236114322L;
	
	private String name;
	private String path;
	private Date lastModified;
	private long size;
	
	private String alias;
	
	public FileLocal(){
	}

	public FileLocal(String name){
		setName(name);
	}

	public FileLocal(String name, String path, Date lastModified, long size){
		setName(name);
		setPath(path);
		setLastModified(lastModified);
		setSize(size);
	}
	
	public String getCompletePathFile(){
		return new StringBuilder()
			.append(getPath())
			.append(Constants.FILE_SEP)
			.append(getName())
			.toString();
	}

	//name
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	//path
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}

	//lastModified
	public Date getLastModified() {
		return lastModified;
	}
	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	//size
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}

	//alias
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}

	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
			.append("name", this.getName())
			.append("path", this.getPath())
			.append("size", this.getSize())
			.append("lastModified", this.getLastModified())
			.toString();
	}



}
