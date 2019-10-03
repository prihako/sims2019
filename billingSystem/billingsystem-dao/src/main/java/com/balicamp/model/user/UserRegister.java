/**
 * 
 */
package com.balicamp.model.user;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.balicamp.model.ISequencesModel;
import com.balicamp.model.admin.BaseAdminModel;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: UserRegister.java 141 2013-01-03 07:55:27Z bagus.sugitayasa $
 */
@Entity
@Table(name = "s_user_register")
public class UserRegister extends BaseAdminModel implements ISequencesModel {

	private static final long serialVersionUID = 3572477527881092756L;

	public static final String STATUS_NEW_OR_ADDED = "A";
	public static final String STATUS_SUCCESS_OR_REGISTERED = "R";
	public static final String STATUS_DELETE = "D";

	/**
	* <br/>
	* column :id
	**/
	@Id
	@Column(name = "id", nullable = false)
	private Long id;
	/**
	* <br/>
	* column :fullname
	**/
	@Column(name = "fullname", nullable = false)
	private String fullname;
	/**
	* <br/>
	* column :address
	**/
	@Column(name = "address", nullable = false)
	private String address;
	/**
	* <br/>
	* column :city
	**/
	@Column(name = "city", nullable = false)
	private String city;
	/**
	* <br/>
	* column :zip_code
	**/
	@Column(name = "zip_code", nullable = false)
	private String zipCode;
	/**
	* <br/>
	* column :phone_number
	**/
	@Column(name = "phone_number", nullable = false)
	private String phoneNumber;
	/**
	* <br/>
	* column :mother_maiden_name
	**/
	@Column(name = "mother_maiden_name", nullable = false)
	private String motherMaidenName;
	/**
	* <br/>
	* column :card_number
	**/
	@Column(name = "card_number", nullable = false)
	private String cardNumber;
	/**
	* <br/>
	* column :request_date
	**/
	@Column(name = "request_date", nullable = false)
	private Date requestDate;
	/**
	* <br/>
	* column :status
	**/
	@Column(name = "status", nullable = true)
	private String status;
	/**
	* <br/>
	* column :note
	**/
	@Column(name = "note", nullable = true)
	private String note;

	public UserRegister() {
	}

	/**
	* <br/>
	* column :fullname
	**/
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	/**
	* <br/>
	* column :fullname
	**/
	public String getFullname() {
		return this.fullname;
	}

	/**
	* <br/>
	* column :address
	**/
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	* <br/>
	* column :address
	**/
	public String getAddress() {
		return this.address;
	}

	/**
	* <br/>
	* column :city
	**/
	public void setCity(String city) {
		this.city = city;
	}

	/**
	* <br/>
	* column :city
	**/
	public String getCity() {
		return this.city;
	}

	/**
	* <br/>
	* column :zip_code
	**/
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	/**
	* <br/>
	* column :zip_code
	**/
	public String getZipCode() {
		return this.zipCode;
	}

	/**
	* <br/>
	* column :phone_number
	**/
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/**
	* <br/>
	* column :phone_number
	**/
	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	/**
	* <br/>
	* column :mother_maiden_name
	**/
	public void setMotherMaidenName(String motherMaidenName) {
		this.motherMaidenName = motherMaidenName;
	}

	/**
	* <br/>
	* column :mother_maiden_name
	**/
	public String getMotherMaidenName() {
		return this.motherMaidenName;
	}

	/**
	* <br/>
	* column :card_number
	**/
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	/**
	* <br/>
	* column :card_number
	**/
	public String getCardNumber() {
		return this.cardNumber;
	}

	/**
	* <br/>
	* column :request_date
	**/
	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	/**
	* <br/>
	* column :request_date
	**/
	public Date getRequestDate() {
		return this.requestDate;
	}

	/**
	* <br/>
	* column :status
	**/
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	* <br/>
	* column :status
	**/
	public String getStatus() {
		return this.status;
	}

	/**
	* <br/>
	* column :note
	**/
	public void setNote(String note) {
		this.note = note;
	}

	/**
	* <br/>
	* column :note
	**/
	public String getNote() {
		return this.note;
	}

	@Override
	public String toString() {
		return "id=" + id + ", \nfullname=" + fullname + ", \naddress=" + address + ", \ncity=" + city + ", \nzipCode="
				+ zipCode + ", \nphoneNumber=" + phoneNumber + ", \nmotherMaidenName=" + motherMaidenName
				+ ", \ncardNumber=" + cardNumber + ", \nrequestDate=" + requestDate + ", \nstatus=" + status
				+ ", \nnote=" + note;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((cardNumber == null) ? 0 : cardNumber.hashCode());
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((fullname == null) ? 0 : fullname.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((motherMaidenName == null) ? 0 : motherMaidenName.hashCode());
		result = prime * result + ((note == null) ? 0 : note.hashCode());
		result = prime * result + ((phoneNumber == null) ? 0 : phoneNumber.hashCode());
		result = prime * result + ((requestDate == null) ? 0 : requestDate.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((zipCode == null) ? 0 : zipCode.hashCode());
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
		UserRegister other = (UserRegister) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (cardNumber == null) {
			if (other.cardNumber != null)
				return false;
		} else if (!cardNumber.equals(other.cardNumber))
			return false;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (fullname == null) {
			if (other.fullname != null)
				return false;
		} else if (!fullname.equals(other.fullname))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (motherMaidenName == null) {
			if (other.motherMaidenName != null)
				return false;
		} else if (!motherMaidenName.equals(other.motherMaidenName))
			return false;
		if (note == null) {
			if (other.note != null)
				return false;
		} else if (!note.equals(other.note))
			return false;
		if (phoneNumber == null) {
			if (other.phoneNumber != null)
				return false;
		} else if (!phoneNumber.equals(other.phoneNumber))
			return false;
		if (requestDate == null) {
			if (other.requestDate != null)
				return false;
		} else if (!requestDate.equals(other.requestDate))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (zipCode == null) {
			if (other.zipCode != null)
				return false;
		} else if (!zipCode.equals(other.zipCode))
			return false;
		return true;
	}

	@Override
	public String getSequenceName() {
		return "s_user_register_id_seq";
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
		return null;
	}
}
