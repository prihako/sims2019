/**
 * 
 */
package com.balicamp.model.admin;

import java.io.Serializable;

import javax.persistence.Embedded;
import javax.persistence.Transient;

import com.balicamp.model.common.AuditModel;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: BaseAdminModel.java 381 2013-03-25 03:55:12Z wayan.agustina $
 */
public abstract class BaseAdminModel implements Serializable {

	private static final long serialVersionUID = -2819456728384455421L;

	/*
	 * @Deprecated
	 * public abstract String getSequenceName();
	 * 
	 * @Deprecated
	 * public abstract Long getId();
	 * 
	 * @Deprecated
	 * public abstract void setId(Long id);
	 */

	protected boolean deleted;
	protected boolean locked;
	private boolean selected;

	@Embedded
	protected AuditModel auditModel;

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
	
	@Transient
	public boolean getSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	/**
	 * Ini adalah untuk mengirim value dari id/object yang berelasi
	 * @return String pKey
	 */
	public abstract Object getPKey();
}
