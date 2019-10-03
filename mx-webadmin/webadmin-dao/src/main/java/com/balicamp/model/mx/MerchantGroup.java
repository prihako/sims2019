package com.balicamp.model.mx;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.balicamp.model.ISequencesModel;
import com.balicamp.model.admin.BaseAdminModel;

/**
 * @author <a href="mailto:snurma.wijayanti@gmail.com">antin</a>
 *
 */
@Entity
@Table(name = "merchant_group")
public class MerchantGroup extends BaseAdminModel implements ISequencesModel,
		Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * id int4 false code varchar 50 false description varchar 50 false
	 */

	@Id
	@Column(name = "id")
	private Long id;

	@Column(name = "code")
	private String code;

	@Column(name = "description")
	private String description;

	@Transient
	private boolean selected;

	public boolean isSelected() {
		return selected;
	}

	@Transient
	private String descTr;

	public String getDescTr() {
		this.descTr = code + " - " +description;
		return descTr;
	}

	public void setDescTr(String descTr) {
		this.descTr = descTr;
	}

	@Override
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String getSequenceName() {
		return "merchant_group_id_seq";
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public Object getPKey() {
		return id;
	}
}
