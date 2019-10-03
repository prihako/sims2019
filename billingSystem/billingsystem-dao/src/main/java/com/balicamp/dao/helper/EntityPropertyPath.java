package com.balicamp.dao.helper;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author <a href="mailto:aananto@balicamp.com">Arif Puji Ananto</a>
 */
public class EntityPropertyPath 
	implements Serializable
{
	private static final long serialVersionUID = -2092420913962553607L;
	
	private String entityPath;
	private String propertyNama;
	
	public EntityPropertyPath()
	{
	}

	public EntityPropertyPath(String propertyPath)
	{
		String splitted[] = propertyPath.split("[.]");
		setPropertyNama(splitted[splitted.length-1]);
		
		StringBuffer stringBuffer = new StringBuffer();
		for (int i=0; i<splitted.length-1; i++) {
			if ( stringBuffer.length() > 0 ){
				stringBuffer.append('.');
			}
			stringBuffer.append(splitted[i]);
		}
		setEntityPath(stringBuffer.toString());
	}

	public EntityPropertyPath( String entityPath, String propertyNama )
	{
		setEntityPath(entityPath);
		setPropertyNama(propertyNama);
	}

	//getter setter
	public String getEntityPath() {
		return entityPath;
	}
	public void setEntityPath(String entityPath) {
		this.entityPath = entityPath;
	}
	public String getPropertyNama() {
		return propertyNama;
	}
	public void setPropertyNama(String propertyNama) {
		this.propertyNama = propertyNama;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
			.append("entityPath", this.entityPath)
			.append("propertyNama", this.propertyNama)
			.toString();
	}
	

}
