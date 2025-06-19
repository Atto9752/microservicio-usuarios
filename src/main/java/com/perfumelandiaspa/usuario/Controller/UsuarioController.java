package com.perfumelandiaspa.usuario.Controller;

//import org.hibernate.mapping.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.perfumelandiaspa.usuario.Model.Usuario;
import com.perfumelandiaspa.usuario.Service.UsuarioService;
import java.util.Map;

//SWAGGER
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
// ACCEDER A SWAGGER:   http://localhost:8080/swagger-ui.html

@RestController
@RequestMapping("/api/v1/usuarios")
@Tag(name = "Usuarios",description = "Metodos para gestionar usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // TESTING REGISTRO DE USUARIO
    @Operation(
        summary = "Registrar usuario",
        description = "Registra usuario con un rol CLIENTE/VENDEDOR",
        responses = {
            @ApiResponse(responseCode = "200", description = "Usuario registrado.",
                content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud invalida."),
            @ApiResponse(responseCode = "500", description = "Error interno.")
        }
    )

    //POST /autenticar/registro
    @PostMapping("/registro")
    public ResponseEntity<String> registrarUsuario(
        @RequestBody Usuario usuario,
        @RequestParam String rol 
    ) {
        return usuarioService.registrarUsuario(usuario, rol);
    }

    //TESTING BUSCAR USUARIO X ID
    @Operation(
        summary = "Buscar usuario por id",
        description = "Busca un usuario por su id xd",
        responses = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado.",
                content = @Content(schema = @Schema(implementation = Usuario.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud invalida."),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado."),
            @ApiResponse(responseCode = "500", description = "Error interno.")
        }
    )

    //GET /usuarios/{id}
    @GetMapping("/usuarios/{id}")
    public ResponseEntity<?> getUsuario(@PathVariable int id)
    {
        Usuario usuario = usuarioService.buscarPorId(id);
        if (usuario == null) {
            return ResponseEntity.status(404).body("Usuario no encontrado");
        }
        return ResponseEntity.ok(usuario);
    }

    //TESTING BUSCAR USUARIO X ROL
    @Operation(
        summary = "Buscar usuario por rol",
        description = "Busca usuarios por su rol CLIENTE/VENDEDOR",
        responses = {
            @ApiResponse(responseCode = "200", description = "Usuarios encontrados.",
                content = @Content(schema = @Schema(implementation = Usuario.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud o rol invalidos."),
            @ApiResponse(responseCode = "404", description = "No hay usuarios con ese rol."),
            @ApiResponse(responseCode = "500", description = "Error interno.")
        }
    )

    //GET /usuarios/rol/{tipo}
    @GetMapping("/usuarios/rol/{tipo}")
    public ResponseEntity<?> getUsuarioPorRol(@PathVariable String tipo)
    {
        if (!tipo.equals("CLIENTE") && !tipo.equals("VENDEDOR")) {
            return ResponseEntity.badRequest().body("El rol debe ser CLIENTE o VENDEDOR");
        }
        return ResponseEntity.ok(usuarioService.listarPorRol(tipo));
    }

    //TESTING ELIMINAR USUARIO X CORREO
    @Operation(
        summary = "Eliminar usuario por correo",
        description = "Elimina un usuario por su correo.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Usuario eliminado.",
                content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud invalida."),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado."),
            @ApiResponse(responseCode = "500", description = "Error interno.")
        }
    )

    //DELETE /eliminarUsuario
    @PostMapping("/eliminarUsuario")
    public ResponseEntity<String> eliminarPorCorreo(@RequestBody Map<String, String> request)
    {
        //aca se extrae el campo gmail del JSON
        String gmail = request.get("gmail");
        //valida que no este vacio
        if (gmail == null || gmail.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("El campo 'gmail' es requerido");
        }
        //llama al Service para eliminar
        return usuarioService.eliminarUsuarioPorCorreo(gmail);
    }
    
}
