package com.balicamp.model.mx;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.balicamp.model.admin.BaseAdminModel;
import com.balicamp.model.user.User;

@Entity
@Table(name = "s_non_business_day")
@SequenceGenerator(name="nonBusinessDayId",sequenceName="s_non_business_day_id_seq")
public class DayOff extends BaseAdminModel implements Serializable {
	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="nonBusinessDayId")
	private Long id;

	@Column(name = "non_business_date", nullable = false)
	private Date nonBusinessDate;

	@Column(name = "description", nullable = false)
	private String description;

	@ManyToOne(targetEntity = User.class)
	@JoinColumn(name="created_by")
	private User createdBy;

	@ManyToOne(targetEntity = User.class)
	@JoinColumn(name="updated_by")
	private User updatedBy;

	@Column(name = "created_date", nullable = false)
	private Date createdDate;

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	@Column(name = "updated_date", nullable = false)
	private Date updatedDate;

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public User getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(User updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getNonBusinessDate() {
		return nonBusinessDate;
	}

	public void setNonBusinessDate(Date nonBusinessDate) {
		this.nonBusinessDate = nonBusinessDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String getPKey() {
		return this.id.toString();
	}
}
