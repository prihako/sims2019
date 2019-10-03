package com.balicamp.webapp.action;

import java.io.Serializable;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.annotations.Persist;

import com.balicamp.exception.ApplicationException;
import com.balicamp.model.admin.BaseAdminModel;
import com.balicamp.service.IManager;

/**
 * @author <a href="mailto:aananto@balicamp.com">Arif Puji Ananto</a>
 */
@SuppressWarnings("unchecked")
public abstract class BasePageForm<T extends BaseAdminModel, PK extends Serializable> extends AdminBasePage {

	protected final Log log = LogFactory.getLog(getClass());

	@Persist("session")
	public abstract void setEntityId(PK id);

	public abstract PK getEntityId();

	@Persist("session")
	public abstract void setEntity(T entity);

	public abstract T getEntity();

	protected void initializedEntity(IManager manager, Class<T> persistentClass) {
		//reset session
		if (getRequest().getRequestURL().indexOf(".html") != -1) {
			setEntityId(null);
			setEntity(null);
		}

		try {
			if (getEntity() != null) {
				PK primaryKey = (PK) PropertyUtils.getProperty(getEntity(), "id");
				setEntityId(primaryKey);
			} else {
				if (getEntityId() == null) {
					setEntity(persistentClass.newInstance());
				} else {
					setEntity((T) manager.findById(getEntityId()));
				}
			}
		} catch (Exception e) {
			log.error("", e);
			throw new ApplicationException("fail initialized entry", e);
		}
	}

	public void resetSession() {
		setEntityId(null);
		setEntity(null);
	}

}
