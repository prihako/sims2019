package com.balicamp.model.common;

import java.io.Serializable;

import com.balicamp.model.admin.BaseAdminModel;


/**
 * @author <a href="mailto:aananto@balicamp.com">Arif Puji Ananto</a>
 */
public abstract class BaseBankAdminMaintenanceObject<PK extends Serializable> extends BaseAdminModel {    
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1184311858601659023L;
	protected boolean deleted;
	protected boolean locked;
	protected AuditModel auditModel;
	
	//not persist
	protected boolean selected;
	
	public abstract Long getId();
	public abstract void setId(Serializable id);

	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
	public boolean isLocked() {
		return locked;
	}
	public void setLocked(boolean locked) {
		this.locked = locked;
	}
	
	public AuditModel getAuditModel() {
		return auditModel;
	}
	public void setAuditModel(AuditModel auditModel) {
		this.auditModel = auditModel;
	}

	//selected
	public boolean getSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

    public abstract String toString();
    public abstract boolean equals(Object o);
    public abstract int hashCode();
    
}
