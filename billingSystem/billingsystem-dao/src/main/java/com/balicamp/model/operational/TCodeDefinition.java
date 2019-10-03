package com.balicamp.model.operational;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the T_CODE_DEFINITION database table.
 * 
 */
@Entity
@Table(name="T_CODE_DEFINITION")
@NamedQuery(name="TCodeDefinition.findAll", query="SELECT t FROM TCodeDefinition t")
public class TCodeDefinition implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="T_CODE_DEFINITION_ID")
	private long tCodeDefinitionId;

	@Column(name="CODE_DESC")
	private String codeDesc;

	@Column(name="CODE_ID")
	private BigDecimal codeId;

	@Column(name="CODE_NAME")
	private String codeName;

	@Column(name="COLUMN_NAME")
	private String columnName;

	@Column(name="TABLE_NAME")
	private String tableName;

	public TCodeDefinition() {
	}

	public long getTCodeDefinitionId() {
		return this.tCodeDefinitionId;
	}

	public void setTCodeDefinitionId(long tCodeDefinitionId) {
		this.tCodeDefinitionId = tCodeDefinitionId;
	}

	public String getCodeDesc() {
		return this.codeDesc;
	}

	public void setCodeDesc(String codeDesc) {
		this.codeDesc = codeDesc;
	}

	public BigDecimal getCodeId() {
		return this.codeId;
	}

	public void setCodeId(BigDecimal codeId) {
		this.codeId = codeId;
	}

	public String getCodeName() {
		return this.codeName;
	}

	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}

	public String getColumnName() {
		return this.columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getTableName() {
		return this.tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

}