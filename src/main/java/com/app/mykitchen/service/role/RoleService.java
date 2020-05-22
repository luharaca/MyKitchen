package com.app.mykitchen.service.role;

import java.util.List;

import com.app.mykitchen.common.BusinessException;
import com.app.mykitchen.domain.security.Role;

public interface RoleService {
	Role findByName(String name);
	
	Role createRole(Role role) throws BusinessException;

	List<Role> findAllRoles();
}
