package odimash.openforum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import odimash.openforum.domain.entity.Role;
import odimash.openforum.domain.repository.RoleRepository;
import odimash.openforum.infrastructure.database.dto.RoleDTO;
import odimash.openforum.infrastructure.database.mapper.RoleMapper;
import odimash.openforum.exception.EntityNameIsAlreadyTakenException;
import odimash.openforum.exception.EntityNotFoundByIdException;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleMapper roleMapper;

    public RoleDTO createRole(RoleDTO roleDTO) {
        validateRoleNameUniqueness(roleDTO.getName(), null);
        Role savedRole = roleRepository.save(roleMapper.mapToEntity(roleDTO));
        return roleMapper.mapToDTO(savedRole);
    }

    public RoleDTO updateRole(RoleDTO roleDTO) {
        validateRoleId(roleDTO.getId());
        validateRoleNameUniqueness(roleDTO.getName(), roleDTO.getId());
		roleRepository.findById(roleDTO.getId()).orElseThrow(() -> new EntityNotFoundByIdException(Role.class, roleDTO.getId()));
        Role updatedRole = roleRepository.save(roleMapper.mapToEntity(roleDTO));
        return roleMapper.mapToDTO(updatedRole);
    }

    public RoleDTO readRole(Long id) {
        validateRoleId(id);
        Role role = roleRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundByIdException(Role.class, id));
        return roleMapper.mapToDTO(role);
    }

    public void deleteRole(Long id) {
        validateRoleId(id);
        roleRepository.deleteById(id);
    }

    private void validateRoleId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Role ID can not be null");
        }
    }

    private void validateRoleNameUniqueness(String name, Long excludeId) {
        boolean roleExists = roleRepository.findByName(name)
            .map(role -> !role.getId().equals(excludeId))
            .orElse(false);

        if (roleExists) {
            throw new EntityNameIsAlreadyTakenException(Role.class, name);
        }
    }
}
