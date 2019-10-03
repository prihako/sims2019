package com.balicamp.model.mx;

// Generated Dec 13, 2012 12:58:42 PM by Hibernate Tools 3.4.0.CR1

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import com.balicamp.model.admin.BaseAdminModel;
import com.balicamp.model.ui.PropertySelectionData;
import com.balicamp.util.CommonUtil;

/**
 * Transactions generated by hbm2java
 * @version $Id: Transactions.java 503 2013-05-24 08:13:16Z rudi.sadria $
 */
@Entity
@Table(name = "transactions", uniqueConstraints = @UniqueConstraint(columnNames = "code"))
public class Transactions extends BaseAdminModel implements java.io.Serializable, PropertySelectionData {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String code;
	private String name;
	private Set<TransactionFee> transactionFees = new HashSet<TransactionFee>(0);
	private Set<Routes> routeses = new HashSet<Routes>(0);

	public Transactions() {
	}

	public Transactions(int id) {
		this.id = id;
	}

	public Transactions(int id, String code, String name) {
		this.id = id;
		this.code = code;
		this.name = name;
	}

	public Transactions(int id, String code, String name, Set<TransactionFee> transactionFees, Set<Routes> routeses) {
		this.id = id;
		this.code = code;
		this.name = name;
		this.transactionFees = transactionFees;
		this.routeses = routeses;
	}

	@Id
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "code", unique = true, nullable = false, length = 50)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "name", nullable = false, length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "transactions")
	public Set<TransactionFee> getTransactionFees() {
		return this.transactionFees;
	}

	public void setTransactionFees(Set<TransactionFee> transactionFees) {
		this.transactionFees = transactionFees;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "transactions")
	public Set<Routes> getRouteses() {
		return this.routeses;
	}

	public void setRouteses(Set<Routes> routeses) {
		this.routeses = routeses;
	}

	@Override
	@Transient
	public String getPKey() {
		return new String("" + getId());
	}

	@Override
	@Transient
	public String getPsdValue() {
		if (getId() == null)
			return "";
		return getId().toString();
	}

	private String psdLabel;

	@Override
	@Transient
	public String getPsdLabel() {
		if (!CommonUtil.isEmpty(psdLabel))
			return psdLabel;

		StringBuilder result = new StringBuilder();
		if (getName() != null)
			result.append(getName());
		else
			result.append(getCode());

		return result.toString();
	}

	@Override
	@Transient
	public boolean isPsdDisabled() {
		return false;
	}

}
