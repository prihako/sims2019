package com.balicamp.model.function;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.balicamp.model.common.BaseObject;
import com.balicamp.model.user.Role;

@Embeddable
public class FunctionRoleId extends BaseObject {
	private static final long serialVersionUID = -582161402621849694L;
	
	@ManyToOne(cascade = CascadeType.ALL, targetEntity = Function.class,fetch = FetchType.EAGER)
	@JoinColumn(name = "function_id")
	private Function function;
	
	@ManyToOne(cascade = CascadeType.ALL, targetEntity = Role.class,fetch = FetchType.EAGER)
	@JoinColumn(name = "role_id")
	private Role role;

	public FunctionRoleId() {
	}

	public FunctionRoleId( Function function, Role role ) {
		setFunction(function);
		setRole(role);
	}

	/**
	 * @hibernate.many-to-one 
	 * inverse="true" 
	 * column="ROLE_ID" 
	 * class="com.balicamp.model.user.Role"
	 */
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	/**
	 * @hibernate.many-to-one 
	 * inverse="true" 
	 * column="FUNCTION_ID" 
	 * class="com.balicamp.model.function.Function"
	 */
	public Function getFunction() {
		return function;
	}

	public void setFunction(Function function) {
		this.function = function;
	}

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) {
		if (!(object instanceof FunctionRoleId)) {
			return false;
		}
		FunctionRoleId rhs = (FunctionRoleId) object;
		return new EqualsBuilder().append(this.role.getId(), rhs.role.getId())
				.append(this.function.getId(), rhs.function.getId()).isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return new HashCodeBuilder(-417499217, 229237521).append(this.role.getId()).append(this.function.getId())
				.toHashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this).append("role", this.role).append("function", this.function).toString();
	}

}
