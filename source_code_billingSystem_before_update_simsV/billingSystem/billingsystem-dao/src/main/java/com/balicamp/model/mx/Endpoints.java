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
 * Endpoints generated by hbm2java
 * @version $Id: Endpoints.java 503 2013-05-24 08:13:16Z rudi.sadria $
 */
@Entity
@Table(name = "endpoints", uniqueConstraints = @UniqueConstraint(columnNames = "code"))
public class Endpoints extends BaseAdminModel implements java.io.Serializable, PropertySelectionData {

	private static final long serialVersionUID = 1L;

	private int id;
	private String code;
	private String name;
	private String state;

	@SuppressWarnings("unused")
	private String concatCodeName;
	private String psdLabel;

	private Set<TransactionFee> transactionFees = new HashSet<TransactionFee>(0);
	private Set<EndpointRcs> endpointRcses = new HashSet<EndpointRcs>(0);
	private Set<Mappings> mappingses = new HashSet<Mappings>(0);
	private Set<StanCounters> stanCounterses = new HashSet<StanCounters>(0);

	public Endpoints() {
	}

	public Endpoints(int id) {
		this.id = id;
	}

	public Endpoints(int id, String code, String name, String state) {
		this.id = id;
		this.code = code;
		this.name = name;
		this.state = state;
	}

	public Endpoints(int id, String code, String name, String state, Set<TransactionFee> transactionFees,
			Set<EndpointRcs> endpointRcses, Set<Mappings> mappingses, Set<StanCounters> stanCounterses) {
		this.id = id;
		this.code = code;
		this.name = name;
		this.state = state;
		this.transactionFees = transactionFees;
		this.endpointRcses = endpointRcses;
		this.mappingses = mappingses;
		this.stanCounterses = stanCounterses;
	}

	@Id
	@Column(name = "id", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "code", unique = true, nullable = false, length = 10)
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

	@Column(name = "state", nullable = false, length = 50)
	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "endpoints")
	public Set<TransactionFee> getTransactionFees() {
		return this.transactionFees;
	}

	public void setTransactionFees(Set<TransactionFee> transactionFees) {
		this.transactionFees = transactionFees;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "endpoints")
	public Set<EndpointRcs> getEndpointRcses() {
		return this.endpointRcses;
	}

	public void setEndpointRcses(Set<EndpointRcs> endpointRcses) {
		this.endpointRcses = endpointRcses;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "endpoints")
	public Set<Mappings> getMappingses() {
		return this.mappingses;
	}

	public void setMappingses(Set<Mappings> mappingses) {
		this.mappingses = mappingses;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "endpoints")
	public Set<StanCounters> getStanCounterses() {
		return this.stanCounterses;
	}

	public void setStanCounterses(Set<StanCounters> stanCounterses) {
		this.stanCounterses = stanCounterses;
	}

	@Transient
	public String getConcatCodeName() {
		return this.code + " - " + this.name;
	}

	public void setConcatCodeName(String concatCodeName) {
		this.concatCodeName = concatCodeName;
	}

	@Override
	@Transient
	public String getPKey() {
		return new String("" + this.getId());
	}

	@Override
	@Transient
	public String getPsdValue() {
		if (getCode() == null)
			return "";
		return getCode();
	}

	public void setPsdLabel(String psdLabel) {
		this.psdLabel = psdLabel;
	}

	@Override
	@Transient
	public String getPsdLabel() {
		if (!CommonUtil.isEmpty(psdLabel))
			return psdLabel;

		StringBuilder result = new StringBuilder();
		if (getConcatCodeName() != null)
			result.append(getConcatCodeName());
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
