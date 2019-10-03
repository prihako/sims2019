package com.balicamp.model.mx;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.balicamp.model.admin.BaseAdminModel;

@Entity
@Table(name="m_mx_param")
public class ParameterMx extends BaseAdminModel {
	private static final long serialVersionUID = 8303614672746585347L;
	
	@Id
	@Column(name="id_mapping_detail")
	private Integer idMappingDetail;
	
	@Column(name="descriptions")
	private String descriptions;
	
	@Column(name="str_value_lama")
	private String strValueLama;
	
	@Column(name="str_value_baru")
	private String strValueBaru;
	
	@Column(name="value_lama")
	private String valueLama;
	
	@Column(name="value_baru")
	private String valueBaru;
	
	@Column(name="tgl_ubah")
	private Date tglUbah;
	
	@Column(name="user_pengubah")
	private String userPengubah;

	public Integer getIdMappingDetail() {
		return idMappingDetail;
	}

	public void setIdMappingDetail(Integer idMappingDetail) {
		this.idMappingDetail = idMappingDetail;
	}

	public String getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(String descriptions) {
		this.descriptions = descriptions;
	}

	public String getStrValueLama() {
		return strValueLama;
	}

	public void setStrValueLama(String strValueLama) {
		this.strValueLama = strValueLama;
	}

	public String getStrValueBaru() {
		return strValueBaru;
	}

	public void setStrValueBaru(String strValueBaru) {
		this.strValueBaru = strValueBaru;
	}

	public String getValueLama() {
		return valueLama;
	}

	public void setValueLama(String valueLama) {
		this.valueLama = valueLama;
	}

	public String getValueBaru() {
		return valueBaru;
	}

	public void setValueBaru(String valueBaru) {
		this.valueBaru = valueBaru;
	}

	public Date getTglUbah() {
		return tglUbah;
	}

	public void setTglUbah(Date tglUbah) {
		this.tglUbah = tglUbah;
	}

	public String getUserPengubah() {
		return userPengubah;
	}

	public void setUserPengubah(String userPengubah) {
		this.userPengubah = userPengubah;
	}

	@Override
	public Object getPKey() {
		return idMappingDetail;
	}

	@Override
	public String toString() {
		return "ParameterMx [idMappingDetail=" + idMappingDetail + ", desc=" + descriptions + ", strValueLama="
				+ strValueLama + ", strValueBaru=" + strValueBaru
				+ ", valueLama=" + valueLama + ", valueBaru=" + valueBaru
				+ ", tglUbah=" + tglUbah + ", userPengubah=" + userPengubah
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((descriptions == null) ? 0 : descriptions.hashCode());
		result = prime * result
				+ ((idMappingDetail == null) ? 0 : idMappingDetail.hashCode());
		result = prime * result
				+ ((strValueBaru == null) ? 0 : strValueBaru.hashCode());
		result = prime * result
				+ ((strValueLama == null) ? 0 : strValueLama.hashCode());
		result = prime * result + ((tglUbah == null) ? 0 : tglUbah.hashCode());
		result = prime * result
				+ ((userPengubah == null) ? 0 : userPengubah.hashCode());
		result = prime * result
				+ ((valueBaru == null) ? 0 : valueBaru.hashCode());
		result = prime * result
				+ ((valueLama == null) ? 0 : valueLama.hashCode());
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
		ParameterMx other = (ParameterMx) obj;
		if (descriptions == null) {
			if (other.descriptions != null)
				return false;
		} else if (!descriptions.equals(other.descriptions))
			return false;
		if (idMappingDetail == null) {
			if (other.idMappingDetail != null)
				return false;
		} else if (!idMappingDetail.equals(other.idMappingDetail))
			return false;
		if (strValueBaru == null) {
			if (other.strValueBaru != null)
				return false;
		} else if (!strValueBaru.equals(other.strValueBaru))
			return false;
		if (strValueLama == null) {
			if (other.strValueLama != null)
				return false;
		} else if (!strValueLama.equals(other.strValueLama))
			return false;
		if (tglUbah == null) {
			if (other.tglUbah != null)
				return false;
		} else if (!tglUbah.equals(other.tglUbah))
			return false;
		if (userPengubah == null) {
			if (other.userPengubah != null)
				return false;
		} else if (!userPengubah.equals(other.userPengubah))
			return false;
		if (valueBaru == null) {
			if (other.valueBaru != null)
				return false;
		} else if (!valueBaru.equals(other.valueBaru))
			return false;
		if (valueLama == null) {
			if (other.valueLama != null)
				return false;
		} else if (!valueLama.equals(other.valueLama))
			return false;
		return true;
	}
	
	

}
