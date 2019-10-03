/**
 * 
 */
package com.balicamp.model.function;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.balicamp.model.ISequencesModel;
import com.balicamp.model.admin.BaseAdminModel;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: MenuTree.java 141 2013-01-03 07:55:27Z bagus.sugitayasa $
 */
@Entity
@Table(name = "s_menu_tree")
public class MenuTree extends BaseAdminModel implements ISequencesModel, Comparable<MenuTree> {

	private static final long serialVersionUID = -2067261706000696304L;

	/**
	 * <br/>
	 * column :id
	 **/
	@Id
	@Column(name = "id", nullable = false)
	private Long id;
	/**
	 * <br/>
	 * column :menu_url
	 **/
	@Column(name = "menu_url", nullable = true)
	private String menuUrl;
	/**
	 * <br/>
	 * column :menu_title
	 **/
	@Column(name = "menu_title", nullable = true)
	private String menuTitle;
	/**
	 * <br/>
	 * column :menu_parent_id
	 **/
	@Column(name = "menu_parent_id", nullable = true)
	private Long menuParentId;
	/**
	 * <br/>
	 * column :menu_order
	 **/
	@Column(name = "menu_order", nullable = true)
	private Long menuOrder;
	/**
	 * <br/>
	 * column :menu_type
	 **/
	@Column(name = "menu_type", nullable = true)
	private String menuType;
	/**
	 * <br/>
	 * column :function_id
	 **/
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "function_id", nullable = false)
	private Function function;

	/**
	 * <br/>
	 * column :menu_url
	 **/
	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}

	/**
	 * <br/>
	 * column :menu_url
	 **/
	public String getMenuUrl() {
		return this.menuUrl;
	}

	/**
	 * <br/>
	 * column :menu_title
	 **/
	public void setMenuTitle(String menuTitle) {
		this.menuTitle = menuTitle;
	}

	/**
	 * <br/>
	 * column :menu_title
	 **/
	public String getMenuTitle() {
		return this.menuTitle;
	}

	/**
	 * <br/>
	 * column :menu_parent_id
	 **/
	public void setMenuParentId(Long menuParentId) {
		this.menuParentId = menuParentId;
	}

	/**
	 * <br/>
	 * column :menu_parent_id
	 **/
	public Long getMenuParentId() {
		return this.menuParentId;
	}

	/**
	 * <br/>
	 * column :menu_order
	 **/
	public void setMenuOrder(Long menuOrder) {
		this.menuOrder = menuOrder;
	}

	/**
	 * <br/>
	 * column :menu_order
	 **/
	public Long getMenuOrder() {
		return this.menuOrder;
	}

	/**
	 * <br/>
	 * column :menu_type
	 **/
	public void setMenuType(String menuType) {
		this.menuType = menuType;
	}

	/**
	 * <br/>
	 * column :menu_type
	 **/
	public String getMenuType() {
		return this.menuType;
	}

	/**
	 * <br/>
	 * column :function_id
	 **/
	public void setFunction(Function function) {
		this.function = function;
	}

	/**
	 * <br/>
	 * column :function_id
	 **/
	public Function getFunction() {
		return this.function;
	}

	@Override
	public String toString() {
		return "id=" + id + ", \nmenuUrl=" + menuUrl + ", \nmenuTitle=" + menuTitle + ", \nmenuParentId="
				+ menuParentId + ", \nmenuOrder=" + menuOrder + ", \nmenuType=" + menuType + ", \nfunction=" + function;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((function == null) ? 0 : function.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((menuOrder == null) ? 0 : menuOrder.hashCode());
		result = prime * result + ((menuParentId == null) ? 0 : menuParentId.hashCode());
		result = prime * result + ((menuTitle == null) ? 0 : menuTitle.hashCode());
		result = prime * result + ((menuType == null) ? 0 : menuType.hashCode());
		result = prime * result + ((menuUrl == null) ? 0 : menuUrl.hashCode());
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
		MenuTree other = (MenuTree) obj;
		if (function == null) {
			if (other.function != null)
				return false;
		} else if (!function.equals(other.function))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (menuOrder == null) {
			if (other.menuOrder != null)
				return false;
		} else if (!menuOrder.equals(other.menuOrder))
			return false;
		if (menuParentId == null) {
			if (other.menuParentId != null)
				return false;
		} else if (!menuParentId.equals(other.menuParentId))
			return false;
		if (menuTitle == null) {
			if (other.menuTitle != null)
				return false;
		} else if (!menuTitle.equals(other.menuTitle))
			return false;
		if (menuType == null) {
			if (other.menuType != null)
				return false;
		} else if (!menuType.equals(other.menuType))
			return false;
		if (menuUrl == null) {
			if (other.menuUrl != null)
				return false;
		} else if (!menuUrl.equals(other.menuUrl))
			return false;
		return true;
	}

	public int compareTo(MenuTree otherMenuTree) {
		if (otherMenuTree == null || otherMenuTree.getMenuOrder() == null || this.getMenuOrder() == null) {
			return 0;
		}

		if (this.getMenuOrder().intValue() > otherMenuTree.getMenuOrder().intValue()) {
			return 1;
		}

		if (this.getMenuOrder().intValue() < otherMenuTree.getMenuOrder().intValue()) {
			return -1;
		}

		return 0;
	}

	@Override
	public String getSequenceName() {
		return "s_menu_tree_id_seq";
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
