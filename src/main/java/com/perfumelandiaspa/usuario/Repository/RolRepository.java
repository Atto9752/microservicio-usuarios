package com.perfumelandiaspa.usuario.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.perfumelandiaspa.usuario.Model.Rol;

public interface RolRepository extends JpaRepository<Rol, Integer>{
    Rol findByNombre(String nombre);
}
