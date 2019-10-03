package com.balicamp.model.mx;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.balicamp.model.ISequencesModel;
import com.balicamp.model.admin.BaseAdminModel;

/**
 * @author <a href="mailto:snurma.wijayanti@gmail.com">antin</a>
 *
 */

@Entity
@Table(name = "merchant_group_details")
public class MerchantGroupDetails extends BaseAdminModel implements
		ISequencesModel, Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * id int4 false merchant_group_id int4 false termid varchar 50 false
	 * channel_code varchar 50 false
	 */

	@Id
	@Column(name = "id")
	private Long id;

	@Column(name = "merchant_group_id")
	private Long merchantGroupId;

	@Column(name = "termid")
	private String termid;

	@Column(name = "channel_code")
	private String channelCode;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "merchant_group_id", nullable = false, insertable = false, updatable = false)
	private MerchantGroup merchantGroup;

	@Transient
	private String merchantTr;

	@Transient
	private boolean selected;

	public boolean isSelected() {
		return selected;
	}

	@Override
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public String getMerchantTr() {
		if (getMerchantGroup() != null) {
			this.merchantTr = merchantGroup.getDescTr();
		}
		return merchantTr;
	}

	public void setMerchantTr(String merchantTr) {
		this.merchantTr = merchantTr;
	}

	@Override
	public String getSequenceName() {
		return "merchant_group_details_id_seq";
	}

	public Long getMerchantGroupId() {
		return merchantGroupId;
	}

	public void setMerchantGroupId(Long merchantGroupId) {
		this.merchantGroupId = merchantGroupId;
	}

	public String getTermid() {
		return termid;
	}

	public void setTermid(String termid) {
		this.termid = termid;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public MerchantGroup getMerchantGroup() {
		return merchantGroup;
	}

	public void setMerchantGroup(MerchantGroup merchantGroup) {
		this.merchantGroup = merchantGroup;
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
