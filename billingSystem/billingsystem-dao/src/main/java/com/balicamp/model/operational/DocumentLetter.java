package com.balicamp.model.operational;

import java.io.Serializable;

import javax.persistence.*;

import com.balicamp.model.ISequencesModel;
import com.balicamp.model.admin.BaseAdminModel;
import com.balicamp.model.ui.PropertySelectionData;

import java.math.BigDecimal;


/**
 * The persistent class for the V_LETTER_DOC database table.
 * 
 */
@Entity
@Table(name="V_LETTER_DOC")
//@NamedQuery(name="VLetterDoc.findAll", query="SELECT v FROM VLetterDoc v")
public class DocumentLetter extends BaseAdminModel implements ISequencesModel, PropertySelectionData, Serializable  {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="LE_ID")
	private BigDecimal letterId;
	
	@Lob
	@Column(name="DCIT_DOC")
	private byte[] letterDoc;

	public DocumentLetter() {
	}

	public byte[] getDcitDoc() {
		return this.letterDoc;
	}

	public void setDcitDoc(byte[] dcitDoc) {
		this.letterDoc = dcitDoc;
	}

	public BigDecimal getLeId() {
		return this.letterId;
	}

	public void setLeId(BigDecimal leId) {
		this.letterId = leId;
	}

	@Override
	public String getPsdValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPsdLabel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isPsdDisabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getSequenceName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setId(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object getPKey() {
		// TODO Auto-generated method stub
		return null;
	}

}