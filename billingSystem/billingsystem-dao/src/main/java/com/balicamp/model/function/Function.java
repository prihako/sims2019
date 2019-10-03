/**
 * 
 */
package com.balicamp.model.function;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.acegisecurity.GrantedAuthority;

import com.balicamp.model.ISequencesModel;
import com.balicamp.model.admin.BaseAdminModel;
import org.hibernate.annotations.Type;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: Function.java 141 2013-01-03 07:55:27Z bagus.sugitayasa $
 */
@Entity
@Table(name = "s_function")
public class Function extends BaseAdminModel implements GrantedAuthority, ISequencesModel {

	private static final long serialVersionUID = -1891990644350250239L;

	/**
	* <br/>
	* column :id
	**/
	@Id
	@Column(name = "id", nullable = false)
	private Long id;
	/**
	* <br/>
	* column :type
	**/
	@Column(name = "type", nullable = true)
	private String type;
	/**
	* <br/>
	* column :name
	**/
	@Column(name = "name", nullable = false)
	private String name;
	/**
	* <br/>
	* column :description
	**/
	@Column(name = "description", nullable = true)
	private String description;
	/**
	* <br/>
	* column :pages
	**/
	@Column(name = "pages", nullable = true)
	private String pages;
	/**
	* <br/>
	* column :system_only
	**/
	@Column(name = "system_only", nullable = true)
    @Type(type = "yes_no")
	private Boolean systemOnly;

	public Function() {
	}

	public Function(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	* <br/>
	* column :type
	**/
	public void setType(String type) {
		this.type = type;
	}

	/**
	* <br/>
	* column :type
	**/
	public String getType() {
		return this.type;
	}

	/**
	* <br/>
	* column :name
	**/
	public void setName(String name) {
		this.name = name;
	}

	/**
	* <br/>
	* column :name
	**/
	public String getName() {
		return this.name;
	}

	/**
	* <br/>
	* column :description
	**/
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	* <br/>
	* column :description
	**/
	public String getDescription() {
		return this.description;
	}

	/**
	* <br/>
	* column :pages
	**/
	public void setPages(String pages) {
		this.pages = pages;
	}

	/**
	* <br/>
	* column :pages
	**/
	public String getPages() {
		return this.pages;
	}

	/**
	* <br/>
	* column :system_only
	**/
	public void setSystemOnly(Boolean systemOnly) {
		this.systemOnly = systemOnly;
	}

	/**
	* <br/>
	* column :system_only
	**/
	public Boolean getSystemOnly() {
		return this.systemOnly;
	}

	@Override
	public String toString() {
		return "id=" + id + ", \ntype=" + type + ", \nname=" + name + ", \ndescription=" + description + ", \npages="
				+ pages + ", \nsystemOnly=" + systemOnly;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((pages == null) ? 0 : pages.hashCode());
		result = prime * result + ((systemOnly == null) ? 0 : systemOnly.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Function other = (Function) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (pages == null) {
			if (other.pages != null)
				return false;
		} else if (!pages.equals(other.pages))
			return false;
		if (systemOnly == null) {
			if (other.systemOnly != null)
				return false;
		} else if (!systemOnly.equals(other.systemOnly))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	public String getAuthority() {
		return getName();
	}

	@Override
	public String getSequenceName() {
		return "s_function_id_seq";
	}

	@Override
	public String getPKey() {
		// TODO Auto-generated method stub
		return null;
	}

}
