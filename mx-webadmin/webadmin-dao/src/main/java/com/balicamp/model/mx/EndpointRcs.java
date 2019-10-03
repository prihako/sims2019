package com.balicamp.model.mx;

// Generated Dec 13, 2012 12:58:42 PM by Hibernate Tools 3.4.0.CR1

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import com.balicamp.model.admin.BaseAdminModel;

/**
 * EndpointRcs generated by hbm2java
 * @version $Id: EndpointRcs.java 503 2013-05-24 08:13:16Z rudi.sadria $
 */
@Entity
@Table(name = "endpoint_rcs", uniqueConstraints = @UniqueConstraint(columnNames = { "endpoint_id", "rc" }))
public class EndpointRcs extends BaseAdminModel implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private Endpoints endpoints;
	private String rc;
	private String description;
	private String isHidden;
	private Set<RcMappings> rcMappingsesForTargetRcId = new HashSet<RcMappings>(0);
	private Set<RcMappings> rcMappingsesForSourceRcId = new HashSet<RcMappings>(0);

	public EndpointRcs() {
	}

	public EndpointRcs(int id, String description) {
		this.id = id;
		this.description = description;
	}

	public EndpointRcs(int id, Endpoints endpoints, String rc, String description,
			Set<RcMappings> rcMappingsesForTargetRcId, Set<RcMappings> rcMappingsesForSourceRcId) {
		this.id = id;
		this.endpoints = endpoints;
		this.rc = rc;
		this.description = description;
		this.rcMappingsesForTargetRcId = rcMappingsesForTargetRcId;
		this.rcMappingsesForSourceRcId = rcMappingsesForSourceRcId;
	}

	@Id
	@Column(name = "id", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "endpoint_id")
	public Endpoints getEndpoints() {
		return this.endpoints;
	}

	public void setEndpoints(Endpoints endpoints) {
		this.endpoints = endpoints;
	}

	@Column(name = "rc", length = 50)
	public String getRc() {
		return this.rc;
	}

	public void setRc(String rc) {
		this.rc = rc;
	}

	@Column(name = "description", nullable = false)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "endpointRcsByTargetRcId")
	public Set<RcMappings> getRcMappingsesForTargetRcId() {
		return this.rcMappingsesForTargetRcId;
	}

	public void setRcMappingsesForTargetRcId(Set<RcMappings> rcMappingsesForTargetRcId) {
		this.rcMappingsesForTargetRcId = rcMappingsesForTargetRcId;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "endpointRcsBySourceRcId")
	public Set<RcMappings> getRcMappingsesForSourceRcId() {
		return this.rcMappingsesForSourceRcId;
	}

	public void setRcMappingsesForSourceRcId(Set<RcMappings> rcMappingsesForSourceRcId) {
		this.rcMappingsesForSourceRcId = rcMappingsesForSourceRcId;
	}

	@Override
	@Transient
	public String getPKey() {
		return new String("" + getId());
	}

	@Column(name = "is_hidden", nullable = false)
	public String getIsHidden() {
		return isHidden;
	}

	public void setIsHidden(String isHidden) {
		this.isHidden = isHidden;
	}

}
