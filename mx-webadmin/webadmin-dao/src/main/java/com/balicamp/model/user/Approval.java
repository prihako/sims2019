package com.balicamp.model.user;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.balicamp.model.ISequencesModel;
import com.balicamp.model.admin.BaseAdminModel;

@Entity
@Table(name = "t_approval")
//@SequenceGenerator(name="approvalId",sequenceName="t_approval_id_seq")
public class Approval extends BaseAdminModel implements ISequencesModel,
Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id", nullable = false,unique=true,insertable=true)
//	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="approvalId")
	private Long id;

	@Column(name = "ref_id", nullable = false)
	private String refId;

	@Column(name = "created_by", nullable = false)
	private Long createdBy;

	@Column(name = "created_date", nullable = false)
	private Date createdDate;

	@Column(name = "status", nullable = false)
	private int status;

	@Column(name = "process_by", nullable = true)
	private Long processBy;

	@Column(name = "process_date", nullable = true)
	private Date processDate;

	@Column(name = "data", nullable = true)
	private String data;

	@Column(name = "flag", nullable = false)
	private int flag;
	
	@Column(name = "process_flag", nullable = false)
	private int processFlag;
	
	public int getProcessFlag() {
		return processFlag;
	}

	public void setProcessFlag(int processFlag) {
		this.processFlag = processFlag;
	}

	@Override
	public Object getPKey() {
		return id;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Long getProcessBy() {
		return processBy;
	}

	public void setProcessBy(Long processBy) {
		this.processBy = processBy;
	}

	public Date getProcessDate() {
		return processDate;
	}

	public void setProcessDate(Date processDate) {
		this.processDate = processDate;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	@Override
	public String getSequenceName() {
		return "t_approval_id_seq";
	}
}