package com.perfumelandiaspa.usuario.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.perfumelandiaspa.usuario.Model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer>{
    boolean existsByNombreUsuario(String nombreUsuario);
    boolean existsByGmail(String gmail);
    Usuario findByNombreUsuario(String nombreUsuario);
    List<Usuario> findByRolNombre(String nombreRol);
    void deleteByGmail(String gmail);
    Usuario findByGmail(String gmail);
}
