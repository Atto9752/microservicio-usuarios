package com.perfumelandiaspa.usuario;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
//KEVIN
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.perfumelandiaspa.usuario.Model.Rol;
import com.perfumelandiaspa.usuario.Model.Usuario;
import com.perfumelandiaspa.usuario.Repository.RolRepository;
import com.perfumelandiaspa.usuario.Repository.UsuarioRepository;
import com.perfumelandiaspa.usuario.Service.UsuarioService;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {
    
    @Mock
    private UsuarioRepository usuarioRepository; // Simulación del repositorio de usuario
    @Mock
    private RolRepository rolRepository;         // Simulación del repositorio de rol

    @InjectMocks
    private UsuarioService usuarioService;  // Servicio que se va a probar
    private Usuario usuario;                // Usuario de prueba
    private Rol rol;                        // Rol de prueba

    @BeforeEach
    void setUp() {
        // Se inicializa el usuario de prueba
        usuario = new Usuario();
        usuario.setIdUsuario(1);
        usuario.setNombreUsuario("Pruebito");
        usuario.setContrasena("pruebita123");
        usuario.setGmail("testing@test.com");
        usuario.setAcceso(true);

        // Se inicializa el rol de prueba para poder setearlo en el usuario
        rol = new Rol();
        rol.setId(1);
        rol.setNombre("CLIENTE");

        // Se asigna el rol al usuario
        usuario.setRol(rol);
    }



    @Test
    @DisplayName("Test registrar usuario")
    void testRegistrarUsuario() {
        // Se simulan las funciones de Repository
        when(rolRepository.findByNombre("CLIENTE")).thenReturn(rol);
        when(usuarioRepository.existsByNombreUsuario("Pruebito")).thenReturn(false);
        when(usuarioRepository.existsByGmail("testing@test.com")).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // Se llama al metodo en Service
        ResponseEntity<String> respuesta = usuarioService.registrarUsuario(usuario, "CLIENTE");

        // Se verifica que los resultados sean los esperados
        assertEquals(200, respuesta.getStatusCodeValue());
        assertEquals("Usuario registrado exitosamente como CLIENTE", respuesta.getBody());
        verify(rolRepository).findByNombre("CLIENTE");
        verify(usuarioRepository).existsByNombreUsuario("Pruebito");
        verify(usuarioRepository).existsByGmail("testing@test.com");
        verify(usuarioRepository).save(any(Usuario.class));
        
    }



    @Test
    @DisplayName("Test buscar usuario por id")
    void testBuscarUsuarioPorId() {
        // Simular Repository
        when(usuarioRepository.findById(1)).thenReturn(java.util.Optional.of(usuario));

        // Llama al método
        Usuario resultado = usuarioService.buscarPorId(1);

        // Verificaciones
        assertEquals(usuario, resultado);
        verify(usuarioRepository).findById(1);
    }



    @Test
    @DisplayName("Test listar usuarios por rol")
    void testListarPorRol() {
        // Simula repository...
        List<Usuario> usuarios = Collections.singletonList(usuario);
        when(usuarioRepository.findByRolNombre("CLIENTE")).thenReturn(usuarios);
        
        // Llama al método
        List<Usuario> resultado = usuarioService.listarPorRol("CLIENTE");
        
        // Verificaciones
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("Pruebito", resultado.get(0).getNombreUsuario());
        verify(usuarioRepository).findByRolNombre("CLIENTE");
    }



    @Test
    @DisplayName("Test eliminar usuario por correo")
    void testEliminarUsuarioPorCorreoExitoso() {
        when(usuarioRepository.existsByGmail("testing@test.com")).thenReturn(true);
        when(usuarioRepository.findByGmail("testing@test.com")).thenReturn(usuario);
        doNothing().when(usuarioRepository).delete(usuario);
        
        // Ejecución del método a testear
        ResponseEntity<String> respuesta = usuarioService.eliminarUsuarioPorCorreo("testing@test.com");
        
        // Verificaciones
        assertEquals(200, respuesta.getStatusCodeValue());
        assertEquals("Usuario eliminado exitosamente", respuesta.getBody());
        verify(usuarioRepository).existsByGmail("testing@test.com");
        verify(usuarioRepository).findByGmail("testing@test.com");
        verify(usuarioRepository).delete(usuario);
    }


}
