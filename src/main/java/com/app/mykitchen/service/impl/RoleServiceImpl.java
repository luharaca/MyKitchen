package com.app.mykitchen.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.mykitchen.common.BusinessException;
import com.app.mykitchen.domain.security.Role;
import com.app.mykitchen.repository.RoleRepository;
import com.app.mykitchen.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	RoleRepository roleRepository;

	@Override
	public Role findByName(String name) {
		return roleRepository.findByName(name);
	}

	@Override
	public Role createRole(Role role) throws BusinessException {

		if (role != null) {
			if (roleRepository.findByName(role.getName()) != null) {
				throw new BusinessException("The role " + role.getName() + " already exists");
			}

			return roleRepository.save(role);
		}

		throw new BusinessException("Role object is null");
	}

	@Override
	public List<Role> findAllRoles() {
		return  (List<Role>)roleRepository.findAll();
	}

}
