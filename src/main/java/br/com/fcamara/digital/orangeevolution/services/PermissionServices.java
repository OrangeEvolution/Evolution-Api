package br.com.fcamara.digital.orangeevolution.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fcamara.digital.orangeevolution.model.Permission;
import br.com.fcamara.digital.orangeevolution.repository.PermissionRepository;

@Service
public class PermissionServices {

	@Autowired
	PermissionRepository repository;

	public Permission findPermission(Long id) {
		return repository.findById(id).get();
	}

}
