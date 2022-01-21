package sem.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sem.enums.RoleName;
import sem.model.Role;
import sem.repository.RoleRepository;

import java.util.ArrayList;
import java.util.Optional;

@Service
@Transactional
public class RoleService {

    @Autowired
    RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public Optional<Role> getByRoleName(RoleName roleName) {
        return roleRepository.findByRoleName(roleName);
    }
    @Transactional
    public void save(Role rol) {
        roleRepository.save(rol);
    }
    
    @Transactional(readOnly = true)
	public  ArrayList<Role>  findAll() {
		return  (ArrayList<Role>)roleRepository.findAll();
	}
}
