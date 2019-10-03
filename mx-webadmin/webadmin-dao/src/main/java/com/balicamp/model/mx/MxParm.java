/**
 * 
 */
package com.balicamp.model.mx;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.balicamp.model.ISequencesModel;
import com.balicamp.model.admin.BaseAdminModel;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: Role.java 381 2013-03-25 03:55:12Z wayan.agustina $
 */
@Entity
@Table(name = "mx_parm")
@SequenceGenerator(name="mxParmId",sequenceName="mx_parm_id_seq")
public class MxParm extends BaseAdminModel implements java.io.Serializable {

	private static final long serialVersionUID = -4673887043166067461L;

	/**
	 * <br/>
	 * column :id
	 **/
	@Id
	@Column(name = "id", nullable = false,unique=true,insertable=true)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="mxParmId")
	private Long id;
	/**
	 * <br/>
	 * column :name
	 **/
	@Column(name = "identifier", nullable = true)
	private String identifier;
	/**
	 * <br/>
	 * column :description
	 **/
	@Column(name = "values", nullable = true)
	private String values;

	public MxParm() {
	}

	public MxParm(Long id) {
		this.id = id;
	}

	/**
	 * <br/>
	 * column :identifier
	 **/
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	/**
	 * <br/>
	 * column :name
	 **/
	public String getIdentifier() {
		return this.identifier;
	}

	/**
	 * <br/>
	 * column :values
	 **/
	public void setValues(String values) {
		this.values = values;
	}

	/**
	 * <br/>
	 * column :values
	 **/
	public String getValues() {
		return this.values;
	}

	@Override
	public String toString() {
		return "id=" + id + ", \nidentifier=" + identifier + ", \nvalues=" + values;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((identifier == null) ? 0 : identifier.hashCode());
		result = prime * result + ((values == null) ? 0 : values.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		MxParm other = (MxParm) obj;
		if (identifier == null) {
			if (other.identifier != null)
				return false;
		} else if (!identifier.equals(other.identifier))
			return false;
		if (values == null) {
			if (other.values != null)
				return false;
		} else if (!values.equals(other.values))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String getPKey() {
		// TODO Auto-generated method stub
		return this.id.toString();
	}
}
