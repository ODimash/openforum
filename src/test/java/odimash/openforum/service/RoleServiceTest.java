package odimash.openforum.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import odimash.openforum.domain.entity.Role;
import odimash.openforum.domain.repository.RoleRepository;
import odimash.openforum.exception.EntityNameIsAlreadyTakenException;
import odimash.openforum.exception.EntityNotFoundByIdException;
import odimash.openforum.infrastructure.database.dto.RoleDTO;
import odimash.openforum.infrastructure.database.mapper.RoleMapper;


public class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private RoleMapper roleMapper;

    @InjectMocks
    private RoleService roleService;

    private Role role;
    private RoleDTO roleDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        role = new Role(1L, "Admin", null, null, null);
        roleDTO = new RoleDTO(1L, "Admin", null, null);

        when(roleMapper.mapToDTO(role)).thenReturn(roleDTO);
        when(roleMapper.mapToEntity(roleDTO)).thenReturn(role);
        when(roleRepository.save(role)).thenReturn(role);
    }

    @Test
    public void testCreateRole_Success() {
        when(roleRepository.findByName(roleDTO.getName())).thenReturn(Optional.empty());
        RoleDTO createdRole = roleService.createRole(roleDTO);
        assertThat(createdRole).isEqualTo(roleDTO);
    }

    @Test
    public void testCreateRole_WhenNameAlreadyExist() {
        when(roleRepository.findByName(roleDTO.getName())).thenReturn(Optional.of(role));
        EntityNameIsAlreadyTakenException thrown = assertThrows(
            EntityNameIsAlreadyTakenException.class,
            () -> roleService.createRole(roleDTO));

        assertThat(thrown.getMessage()).isEqualTo(
            String.format("The name \"%s\" of the \"Role\" is already taken", roleDTO.getName()));
    }

    @Test
    public void testUpdateRole_Success() {
        when(roleRepository.findById(roleDTO.getId())).thenReturn(Optional.of(role));
        RoleDTO updatedRoleDTO = roleService.updateRole(roleDTO);
        assertThat(updatedRoleDTO).isEqualTo(roleDTO);
    }

    @Test
    public void testUpdateRole_WhenIdNull() {
        roleDTO.setId(null);
        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> roleService.updateRole(roleDTO));

        assertThat(thrown.getMessage()).isEqualTo("Role ID cannot be null");
    }

    @Test
    public void testUpdateRole_WhenRoleNotFound() {
        when(roleRepository.findById(roleDTO.getId())).thenReturn(Optional.empty());
        EntityNotFoundByIdException thrown = assertThrows(
            EntityNotFoundByIdException.class,
            () -> roleService.updateRole(roleDTO));

        assertThat(thrown.getMessage()).isEqualTo(
            String.format("Not found \"Role\" by ID %s", roleDTO.getId().toString()));
    }

    @Test
    public void testReadRole_Success() {
        when(roleRepository.findById(roleDTO.getId())).thenReturn(Optional.of(role));
        RoleDTO foundRole = roleService.readRole(roleDTO.getId());
        assertThat(foundRole).isEqualTo(roleDTO);
    }

    @Test
    public void testReadRole_WhenIdNull() {
        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> roleService.readRole(null));

        assertThat(thrown.getMessage()).isEqualTo("Role ID cannot be null");
    }

    @Test
    public void testReadRole_WhenRoleNotFound() {
        when(roleRepository.findById(roleDTO.getId())).thenReturn(Optional.empty());
        EntityNotFoundByIdException thrown = assertThrows(
            EntityNotFoundByIdException.class,
            () -> roleService.readRole(roleDTO.getId()));

        assertThat(thrown.getMessage()).isEqualTo(
            String.format("Not found \"Role\" by ID %s", roleDTO.getId().toString()));
    }

    @Test
    public void testDeleteRole_Success() {
        doNothing().when(roleRepository).deleteById(roleDTO.getId());
        roleService.deleteRole(roleDTO.getId());
        verify(roleRepository, times(1)).deleteById(roleDTO.getId());
    }

    @Test
    public void testDeleteRole_WhenIdNull() {
        roleDTO.setId(null);
        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> roleService.deleteRole(roleDTO.getId()));

        assertThat(thrown.getMessage()).isEqualTo("Role ID cannot be null");
        verify(roleRepository, never()).deleteById(anyLong());
    }
}
