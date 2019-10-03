/**
 * 
 */
package com.balicamp.model.user;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.balicamp.model.ISequencesModel;
import com.balicamp.model.admin.BaseAdminModel;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: UserCardNumber.java 141 2013-01-03 07:55:27Z bagus.sugitayasa $
 */
@Entity
@Table(name = "s_user_cardnumber")
public class UserCardNumber extends BaseAdminModel implements ISequencesModel {

	private static final long serialVersionUID = 8680819387998177831L;

	/**
	* <br/>
	* column :id
	**/
	@Id
	@Column(name = "id", nullable = false)
	private Long id;
	/**
	* <br/>
	* column :user_id
	**/
	@Column(name = "user_id", nullable = true, updatable = false, insertable = false)
	private User userId;
	/**
	* <br/>
	* column :card_number
	**/
	@Column(name = "card_number", nullable = true, updatable = false, insertable = false)
	private String cardNumber;
	/**
	* <br/>
	* column :created_date
	**/
	@Column(name = "created_date", nullable = true, updatable = false, insertable = false)
	private Date createdDate;

	@ManyToOne(targetEntity = com.balicamp.model.user.User.class)
	// @Column(name = "user_id")
	private User user;

	/**
	* <br/>
	* column :user_id
	**/
	public void setUserId(User userId) {
		this.userId = userId;
	}

	/**
	* <br/>
	* column :user_id
	**/
	public User getUserId() {
		return this.userId;
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
	* column :created_date
	**/
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	/**
	* <br/>
	* column :created_date
	**/
	public Date getCreatedDate() {
		return this.createdDate;
	}

	@Override
	public String toString() {
		return "id=" + id + ", \nuserId=" + userId + ", \ncardNumber=" + cardNumber + ", \ncreatedDate=" + createdDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cardNumber == null) ? 0 : cardNumber.hashCode());
		result = prime * result + ((createdDate == null) ? 0 : createdDate.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
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
		UserCardNumber other = (UserCardNumber) obj;
		if (cardNumber == null) {
			if (other.cardNumber != null)
				return false;
		} else if (!cardNumber.equals(other.cardNumber))
			return false;
		if (createdDate == null) {
			if (other.createdDate != null)
				return false;
		} else if (!createdDate.equals(other.createdDate))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String getSequenceName() {
		return "s_user_cardnumber_id_seq";
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
