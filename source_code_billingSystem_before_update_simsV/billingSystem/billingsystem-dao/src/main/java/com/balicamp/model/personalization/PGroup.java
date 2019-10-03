/**
 * 
 */
package com.balicamp.model.personalization;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.balicamp.model.ISequencesModel;
import com.balicamp.model.admin.BaseAdminModel;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: PGroup.java 141 2013-01-03 07:55:27Z bagus.sugitayasa $
 */
@Entity
@Table(name = "m_pgroup")
public class PGroup extends BaseAdminModel implements ISequencesModel {

	private static final long serialVersionUID = 5732607462318267762L;

	/**
	* id group ini merupakan FK dari t_transaction, karena tabel t_pbs_grp_mutasi ini adalah detail dari table t_transaction<br/>
	* column :id
	**/
	@Id
	@Column(name = "id", nullable = false)
	private Long id;
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
	@Column(name = "description", nullable = false)
	private String description;
	/**
	* <br/>
	* column :editable
	**/
	@Column(name = "editable", nullable = false)
	private String editable;
	/**
	* <br/>
	* column :has_kupu
	**/
	@Column(name = "has_kupu", nullable = true)
	private String hasKupu;
	/**
	* <br/>
	* column :min_reg_days
	**/
	@Column(name = "min_reg_days", nullable = true)
	private Long minRegDays;
	/**
	* <br/>
	* column :min_pu_count
	**/
	@Column(name = "min_pu_count", nullable = true)
	private Long minPuCount;
	/**
	* <br/>
	* column :min_pu_days
	**/
	@Column(name = "min_pu_days", nullable = true)
	private Long minPuDays;
	/**
	* <br/>
	* column :min_pa_count
	**/
	@Column(name = "min_pa_count", nullable = true)
	private Long minPaCount;
	/**
	* <br/>
	* column :min_pa_days
	**/
	@Column(name = "min_pa_days", nullable = true)
	private Long minPaDays;
	/**
	* <br/>
	* column :min_tr_count
	**/
	@Column(name = "min_tr_count", nullable = true)
	private Long minTrCount;
	/**
	* <br/>
	* column :min_tr_days
	**/
	@Column(name = "min_tr_days", nullable = true)
	private Long minTrDays;
	/**
	* <br/>
	* column :provinsi
	**/
	@Column(name = "provinsi", nullable = true)
	private String provinsi;
	/**
	* <br/>
	* column :kota
	**/
	@Column(name = "kota", nullable = true)
	private String kota;
	/**
	* <br/>
	* column :kecamatan
	**/
	@Column(name = "kecamatan", nullable = true)
	private String kecamatan;
	/**
	* <br/>
	* column :kelurahan
	**/
	@Column(name = "kelurahan", nullable = true)
	private String kelurahan;
	/**
	* <br/>
	* column :for_merchant
	**/
	@Column(name = "for_merchant", nullable = true)
	private String forMerchant;

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
	* column :editable
	**/
	public void setEditable(String editable) {
		this.editable = editable;
	}

	/**
	* <br/>
	* column :editable
	**/
	public String getEditable() {
		return this.editable;
	}

	/**
	* <br/>
	* column :has_kupu
	**/
	public void setHasKupu(String hasKupu) {
		this.hasKupu = hasKupu;
	}

	/**
	* <br/>
	* column :has_kupu
	**/
	public String getHasKupu() {
		return this.hasKupu;
	}

	/**
	* <br/>
	* column :min_reg_days
	**/
	public void setMinRegDays(Long minRegDays) {
		this.minRegDays = minRegDays;
	}

	/**
	* <br/>
	* column :min_reg_days
	**/
	public Long getMinRegDays() {
		return this.minRegDays;
	}

	/**
	* <br/>
	* column :min_pu_count
	**/
	public void setMinPuCount(Long minPuCount) {
		this.minPuCount = minPuCount;
	}

	/**
	* <br/>
	* column :min_pu_count
	**/
	public Long getMinPuCount() {
		return this.minPuCount;
	}

	/**
	* <br/>
	* column :min_pu_days
	**/
	public void setMinPuDays(Long minPuDays) {
		this.minPuDays = minPuDays;
	}

	/**
	* <br/>
	* column :min_pu_days
	**/
	public Long getMinPuDays() {
		return this.minPuDays;
	}

	/**
	* <br/>
	* column :min_pa_count
	**/
	public void setMinPaCount(Long minPaCount) {
		this.minPaCount = minPaCount;
	}

	/**
	* <br/>
	* column :min_pa_count
	**/
	public Long getMinPaCount() {
		return this.minPaCount;
	}

	/**
	* <br/>
	* column :min_pa_days
	**/
	public void setMinPaDays(Long minPaDays) {
		this.minPaDays = minPaDays;
	}

	/**
	* <br/>
	* column :min_pa_days
	**/
	public Long getMinPaDays() {
		return this.minPaDays;
	}

	/**
	* <br/>
	* column :min_tr_count
	**/
	public void setMinTrCount(Long minTrCount) {
		this.minTrCount = minTrCount;
	}

	/**
	* <br/>
	* column :min_tr_count
	**/
	public Long getMinTrCount() {
		return this.minTrCount;
	}

	/**
	* <br/>
	* column :min_tr_days
	**/
	public void setMinTrDays(Long minTrDays) {
		this.minTrDays = minTrDays;
	}

	/**
	* <br/>
	* column :min_tr_days
	**/
	public Long getMinTrDays() {
		return this.minTrDays;
	}

	/**
	* <br/>
	* column :provinsi
	**/
	public void setProvinsi(String provinsi) {
		this.provinsi = provinsi;
	}

	/**
	* <br/>
	* column :provinsi
	**/
	public String getProvinsi() {
		return this.provinsi;
	}

	/**
	* <br/>
	* column :kota
	**/
	public void setKota(String kota) {
		this.kota = kota;
	}

	/**
	* <br/>
	* column :kota
	**/
	public String getKota() {
		return this.kota;
	}

	/**
	* <br/>
	* column :kecamatan
	**/
	public void setKecamatan(String kecamatan) {
		this.kecamatan = kecamatan;
	}

	/**
	* <br/>
	* column :kecamatan
	**/
	public String getKecamatan() {
		return this.kecamatan;
	}

	/**
	* <br/>
	* column :kelurahan
	**/
	public void setKelurahan(String kelurahan) {
		this.kelurahan = kelurahan;
	}

	/**
	* <br/>
	* column :kelurahan
	**/
	public String getKelurahan() {
		return this.kelurahan;
	}

	/**
	* <br/>
	* column :for_merchant
	**/
	public void setForMerchant(String forMerchant) {
		this.forMerchant = forMerchant;
	}

	/**
	* <br/>
	* column :for_merchant
	**/
	public String getForMerchant() {
		return this.forMerchant;
	}

	@Override
	public String toString() {
		return "id=" + id + ", \nname=" + name + ", \ndescription=" + description + ", \neditable=" + editable
				+ ", \nhasKupu=" + hasKupu + ", \nminRegDays=" + minRegDays + ", \nminPuCount=" + minPuCount
				+ ", \nminPuDays=" + minPuDays + ", \nminPaCount=" + minPaCount + ", \nminPaDays=" + minPaDays
				+ ", \nminTrCount=" + minTrCount + ", \nminTrDays=" + minTrDays + ", \nprovinsi=" + provinsi
				+ ", \nkota=" + kota + ", \nkecamatan=" + kecamatan + ", \nkelurahan=" + kelurahan + ", \nforMerchant="
				+ forMerchant;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((editable == null) ? 0 : editable.hashCode());
		result = prime * result + ((forMerchant == null) ? 0 : forMerchant.hashCode());
		result = prime * result + ((hasKupu == null) ? 0 : hasKupu.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((kecamatan == null) ? 0 : kecamatan.hashCode());
		result = prime * result + ((kelurahan == null) ? 0 : kelurahan.hashCode());
		result = prime * result + ((kota == null) ? 0 : kota.hashCode());
		result = prime * result + ((minPaCount == null) ? 0 : minPaCount.hashCode());
		result = prime * result + ((minPaDays == null) ? 0 : minPaDays.hashCode());
		result = prime * result + ((minPuCount == null) ? 0 : minPuCount.hashCode());
		result = prime * result + ((minPuDays == null) ? 0 : minPuDays.hashCode());
		result = prime * result + ((minRegDays == null) ? 0 : minRegDays.hashCode());
		result = prime * result + ((minTrCount == null) ? 0 : minTrCount.hashCode());
		result = prime * result + ((minTrDays == null) ? 0 : minTrDays.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((provinsi == null) ? 0 : provinsi.hashCode());
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
		PGroup other = (PGroup) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (editable == null) {
			if (other.editable != null)
				return false;
		} else if (!editable.equals(other.editable))
			return false;
		if (forMerchant == null) {
			if (other.forMerchant != null)
				return false;
		} else if (!forMerchant.equals(other.forMerchant))
			return false;
		if (hasKupu == null) {
			if (other.hasKupu != null)
				return false;
		} else if (!hasKupu.equals(other.hasKupu))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (kecamatan == null) {
			if (other.kecamatan != null)
				return false;
		} else if (!kecamatan.equals(other.kecamatan))
			return false;
		if (kelurahan == null) {
			if (other.kelurahan != null)
				return false;
		} else if (!kelurahan.equals(other.kelurahan))
			return false;
		if (kota == null) {
			if (other.kota != null)
				return false;
		} else if (!kota.equals(other.kota))
			return false;
		if (minPaCount == null) {
			if (other.minPaCount != null)
				return false;
		} else if (!minPaCount.equals(other.minPaCount))
			return false;
		if (minPaDays == null) {
			if (other.minPaDays != null)
				return false;
		} else if (!minPaDays.equals(other.minPaDays))
			return false;
		if (minPuCount == null) {
			if (other.minPuCount != null)
				return false;
		} else if (!minPuCount.equals(other.minPuCount))
			return false;
		if (minPuDays == null) {
			if (other.minPuDays != null)
				return false;
		} else if (!minPuDays.equals(other.minPuDays))
			return false;
		if (minRegDays == null) {
			if (other.minRegDays != null)
				return false;
		} else if (!minRegDays.equals(other.minRegDays))
			return false;
		if (minTrCount == null) {
			if (other.minTrCount != null)
				return false;
		} else if (!minTrCount.equals(other.minTrCount))
			return false;
		if (minTrDays == null) {
			if (other.minTrDays != null)
				return false;
		} else if (!minTrDays.equals(other.minTrDays))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (provinsi == null) {
			if (other.provinsi != null)
				return false;
		} else if (!provinsi.equals(other.provinsi))
			return false;
		return true;
	}

	@Override
	public String getSequenceName() {
		return "m_pgroup_id_seq";
	}

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String getPKey() {
		// TODO Auto-generated method stub
		return null;
	}
}
