package com.minimarket.security.config;

import com.minimarket.entity.Rol;
import com.minimarket.entity.Usuario;
import com.minimarket.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (usuarioRepository.findByUsername("admin").isEmpty()) {
                Usuario admin = new Usuario();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123")); 
                
                // Inicializamos el Set para evitar NullPointerException
                admin.setRoles(new HashSet<>());

                Rol rolAdmin = new Rol();
                rolAdmin.setNombre("ROLE_ADMIN"); 
                
                admin.getRoles().add(rolAdmin);

                // Si te da un error de "TransientObjectException" al guardar, 
                // necesitarás guardar 'rolAdmin' primero usando un RolRepository, 
                // o bien agregar 'cascade = CascadeType.ALL' en la relación @ManyToMany de tu clase Usuario.
                usuarioRepository.save(admin);
                
                System.out.println("====== Usuario de prueba creado en H2: admin / admin123 con ROLE_ADMIN ======");
            }
        };
    }
}