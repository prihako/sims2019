/**
 * 
 */
package com.balicamp.model.function;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.balicamp.model.admin.BaseAdminModel;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: FunctionRole.java 141 2013-01-03 07:55:27Z bagus.sugitayasa $
 */
@Entity
@Table(name = "s_function_role")
public class FunctionRole extends BaseAdminModel {

	private static final long serialVersionUID = -3513903392796435582L;

	/**
	* <br/>
	* column :role_id
	**/
	@EmbeddedId
	private FunctionRoleId id;

	public FunctionRole() {
	}

	public FunctionRole(FunctionRoleId id) {
		this.id = id;
	}

	public FunctionRoleId getId() {
		return id;
	}

	public void setId(FunctionRoleId id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		FunctionRole other = (FunctionRole) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FunctionRole [id=" + id + "]";
	}

	@Override
	public String getPKey() {
		// TODO Auto-generated method stub
		return null;
	}

}
