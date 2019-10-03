/**
 * 
 */
package com.balicamp.dao.hibernate.function;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.balicamp.dao.function.FunctionRoleDao;
import com.balicamp.dao.hibernate.admin.AdminGenericDaoImpl;
import com.balicamp.model.function.Function;
import com.balicamp.model.function.FunctionRole;
import com.balicamp.model.function.FunctionRoleId;
import com.balicamp.model.user.Role;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: FunctionRoleDaoHibernate.java 112 2012-12-12 04:14:15Z bagus.sugitayasa $
 */
@Repository("functionRoleDao")
public class FunctionRoleDaoHibernate extends AdminGenericDaoImpl<FunctionRole, Long> implements FunctionRoleDao {

	public FunctionRoleDaoHibernate() {
		super(FunctionRole.class);
	}

	@Override
	public int deleteByRoleId(Long roleId) {
		Query query = getSession().createQuery("delete FunctionRole as fr where fr.id.role.id=:roleId");
		query.setLong("roleId", roleId);
		return query.executeUpdate();
	}
	
	@Override
	public void saveByRoleIdAndFunctionId(Long roleId, Long functionId) {
		Role role = new Role(roleId);
        Function function = new Function(functionId);

        FunctionRoleId id = new FunctionRoleId(function, role);
       /* if (findById(id.getFunction().getId()) == null) {*/
            saveOrUpdate(new FunctionRole(id));
            
       /* }*/
	}

	@Override
	public long getCountByRole(Role role) {
		 Query query = getSession().createQuery("SELECT count(*) FROM FunctionRole fr WHERE fr.id.role.id=:roleId");
	        query.setLong("roleId", role.getId());
	        return (Long) query.uniqueResult();
	}

}
