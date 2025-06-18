package com.perfumelandiaspa.usuario.Service;

// import java.util.HashMap;
import java.util.List;
// import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
// import org.springframework.web.client.RestTemplate;

// import com.fasterxml.jackson.databind.ObjectMapper;
import com.perfumelandiaspa.usuario.Model.Rol;
import com.perfumelandiaspa.usuario.Model.Usuario;
import com.perfumelandiaspa.usuario.Repository.RolRepository;
import com.perfumelandiaspa.usuario.Repository.UsuarioRepository;
// import java.util.Map;
// import java.util.HashMap;

import jakarta.transaction.Transactional;

@Service
public class UsuarioService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired 
    private RolRepository rolRepository;

    // @Autowired
    // private RestTemplate restTemplate; //para llegar a otros microservicios

    // @Autowired
    // private ObjectMapper objectMapper; //para pasear por JSON

    //metodo para registrar un usuario y validarlo
    public ResponseEntity<String> registrarUsuario(Usuario usuario, String nombreRol)
    {
        try {
            //aca se validara de que cumpla con CLIENTE o VENDEDOR
            if (!nombreRol.equalsIgnoreCase("CLIENTE") && !nombreRol.equalsIgnoreCase("VENDEDOR")) {
                return ResponseEntity.badRequest().body("Debe registrarse como un CLIENTE o VENDEDOR");
            }

            //validar campos obligatorios
            if (usuario.getNombreUsuario() == null || usuario.getContrasena() == null ||usuario.getGmail() == null) {
                return ResponseEntity.badRequest().body("Nombre de usuario y/o contraseña son obligatorios");
            }

            //ahora verifica si un nombre de usuario o gmail se encuentran disponibles
            if (usuarioRepository.existsByNombreUsuario(usuario.getNombreUsuario())) {
                return ResponseEntity.badRequest().body("El nombre de usuario ya está en uso");
            }

            if (usuario.getGmail() != null && usuarioRepository.existsByGmail(usuario.getGmail())) {
                return ResponseEntity.badRequest().body("El correo electronico ya está registrado");
            }

            //validar contraseña no nula y vacia
            if (usuario.getContrasena() == null || usuario.getContrasena().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("La contraseña es obligatoria");
            }

            //asignacion de rol
            Rol rol = rolRepository.findByNombre(nombreRol);
            if (rol == null) {
                return ResponseEntity.badRequest().body("El rol no existe");
            }
            usuario.setRol(rol);
            usuarioRepository.save(usuario);

            return ResponseEntity.ok("Usuario registrado exitosamente como " + nombreRol);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    //buscar por ID
    public Usuario buscarPorId(int id)
    {
        return usuarioRepository.findById(id).orElse(null);
    }

    //listar usuarios por rol
    public List<Usuario> listarPorRol(String nombreRol)
    {
        return usuarioRepository.findByRolNombre(nombreRol);
    }

    //metodo para elimiar por correo
    @Transactional
    public ResponseEntity<String> eliminarUsuarioPorCorreo(String gmail)
    {
        try {
            //aca se verifica si el usuario existe
            Usuario usuario = usuarioRepository.findByGmail(gmail);
            if (!usuarioRepository.existsByGmail(gmail)) {
                return ResponseEntity.badRequest().body("No existe un usuario con ese correo :(");
            }

            //si no, se elimina al usuario
            usuarioRepository.delete(usuario);
            return ResponseEntity.ok("Usuario eliminado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al eliminar el usuario: " + e.getMessage());
        }
    }

    // private final String vendedores = "http://localhost:8081/vendedores/por-usuario";

    // public Map<String, Object> getUsuarioConRolVendedor(Long usuarioId) {
    // Usuario usuario = usuarioRepository.findById(usuarioId).orElseThrow();
    // Map<String, Object> response = new HashMap<>();

    // // Atributos básicos del usuario
    // response.put("id", usuario.getId());
    // response.put("nombreUsuario", usuario.getNombreUsuario());
    // response.put("gmail", usuario.getGmail());
    // response.put("acceso", usuario.isAcceso());

    // if (usuario.getRol().getNombre().equals("VENDEDOR")) {
    //     RestTemplate restTemplate = new RestTemplate();
    //     String url = VENDEDORES_API + usuarioId;

    //     try {
    //         // Especifica el tipo genérico Map<String, Object> en la respuesta
    //         ResponseEntity<Map<String, Object>> vendedorResponse = 
    //             restTemplate.getForEntity(url, new ParameterizedTypeReference<Map<String, Object>>() {});

    //         Map<String, Object> datosVendedor = vendedorResponse.getBody();

    //         Map<String, Object> rolCombinado = new HashMap<>();
    //         rolCombinado.put("rol_id", usuario.getRol().getId());
    //         rolCombinado.put("rol_nombre", usuario.getRol().getNombre());
    //         rolCombinado.put("sucursal", datosVendedor.get("sucursal"));
    //         rolCombinado.put("meta_mensual", datosVendedor.get("meta_mensual"));

    //         response.put("rol", rolCombinado);

    //     } catch (Exception e) {
    //         Map<String, Object> errorRol = new HashMap<>();
    //         errorRol.put("error", "Microservicio de Vendedores no disponible");
    //         response.put("rol", errorRol);
    //     }
    // } else {
    //     Map<String, Object> rolBasico = new HashMap<>();
    //     rolBasico.put("rol_id", usuario.getRol().getId());
    //     rolBasico.put("rol_nombre", usuario.getRol().getNombre());
    //     response.put("rol", rolBasico);
    // }

    // return response;
    // }
}

    // public ResponseEntity<?> getUsuarioCompleto(Long id)
    // {   
    //     Usuario usuario = usuarioRepository.findById(id).orElse(null);
    //     if (usuario == null) {
    //         return ResponseEntity.notFound().build();
    //     }

    //     String nombreRol = usuario.getRol().getNombre();
    //     String urlMicroservicio = nombreRol.equalsIgnoreCase(nombreRol)

    // }



    // //metodo para convertir usuario a usuarioDTO con detalles de Rol
    // public UsuarioDTO convertirUsuarioDTO(Usuario usuario)
    // {
    //     UsuarioDTO usuarioDTO = new UsuarioDTO();
    //     usuarioDTO.setId(usuario.getId());
    //     usuarioDTO.setNombreUsuario(usuario.getNombreUsuario());
    //     usuarioDTO.setGmail(usuario.getGmail());
    //     usuarioDTO.setAcceso(usuario.isAcceso());

    //     RolDTO rolDTO = new RolDTO();

    //     if (usuario.getRol() != null) {
    //         rolDTO.setNombre((usuario.getRol().getNombre()));
    //     }
    //     rolDTO.setNombre(usuario.getRol().getNombre());

    //     //obtener detalles del rol desde el microservicio correspondiente
    //     String nombreRol = usuario.getRol().getNombre());
    //     String urlMicroservicio = "";


    // }
// }
