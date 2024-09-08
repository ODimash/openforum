package odimash.openforum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import odimash.openforum.domain.entity.Role;
import odimash.openforum.domain.repository.RoleRepository;
import odimash.openforum.infrastructure.database.dto.RoleDTO;
import odimash.openforum.infrastructure.database.mapper.RoleMapper;
import odimash.openforum.exception.EntityNameIsAlreadyTakenException;
import odimash.openforum.exception.EntityNotFoundByIdException;

@Service
public class RoleService {

    private static final Logger logger = LoggerFactory.getLogger(RoleService.class);

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleMapper roleMapper;

    public RoleDTO createRole(RoleDTO roleDTO) {
        validateRoleNameUniqueness(roleDTO.getName(), null);

        logger.info("Creating role with name: {}", roleDTO.getName());
        Role savedRole = roleRepository.save(roleMapper.mapToEntity(roleDTO));
        logger.info("Role created with ID: {}", savedRole.getId());
        return roleMapper.mapToDTO(savedRole);
    }

    public RoleDTO updateRole(RoleDTO roleDTO) {
        validateRoleId(roleDTO.getId());
        validateRoleNameUniqueness(roleDTO.getName(), roleDTO.getId());

        logger.info("Updating role with ID: {}", roleDTO.getId());
        roleRepository.findById(roleDTO.getId())
            .orElseThrow(() -> {
                logger.error("Role with ID '{}' not found", roleDTO.getId());
                return new EntityNotFoundByIdException(Role.class, roleDTO.getId());
            });

        Role updatedRole = roleRepository.save(roleMapper.mapToEntity(roleDTO));
        logger.info("Role updated with ID: {}", updatedRole.getId());
        return roleMapper.mapToDTO(updatedRole);
    }

    public RoleDTO readRole(Long id) {
        validateRoleId(id);

        logger.info("Reading role with ID: {}", id);
        Role role = roleRepository.findById(id)
            .orElseThrow(() -> {
                logger.error("Role with ID '{}' not found", id);
                return new EntityNotFoundByIdException(Role.class, id);
            });

        return roleMapper.mapToDTO(role);
    }

    public void deleteRole(Long id) {
        validateRoleId(id);

        logger.info("Deleting role with ID: {}", id);
        roleRepository.deleteById(id);
    }

    private void validateRoleId(Long id) {
        if (id == null) {
            logger.error("Role ID cannot be null");
            throw new IllegalArgumentException("Role ID cannot be null");
        }
    }

    private void validateRoleNameUniqueness(String name, Long excludeId) {
        boolean roleExists = roleRepository.findByName(name)
            .map(role -> !role.getId().equals(excludeId))
            .orElse(false);

        if (roleExists) {
            logger.error("Role name '{}' is already taken", name);
            throw new EntityNameIsAlreadyTakenException(Role.class, name);
        }
    }
}
