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
            // 1. Inicializar Administrador (Gestión global, reportes y auditoría)
            if (usuarioRepository.findByUsername("admin").isEmpty()) {
                Usuario admin = new Usuario();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRoles(new HashSet<>());

                Rol rolAdmin = new Rol();
                rolAdmin.setNombre("ROLE_ADMIN");
                admin.getRoles().add(rolAdmin);
                
                usuarioRepository.save(admin);
                System.out.println("====== Usuario Creado: admin / admin123 (ROLE_ADMIN) ======");
            }

            // 2. Inicializar Empleado (Gestión de inventario y cajas de venta)
            if (usuarioRepository.findByUsername("empleado").isEmpty()) {
                Usuario empleado = new Usuario();
                empleado.setUsername("empleado");
                empleado.setPassword(passwordEncoder.encode("empleado123"));
                empleado.setRoles(new HashSet<>());

                Rol rolEmpleado = new Rol();
                rolEmpleado.setNombre("ROLE_EMPLOYEE");
                empleado.getRoles().add(rolEmpleado);
                
                usuarioRepository.save(empleado);
                System.out.println("====== Usuario Creado: empleado / empleado123 (ROLE_EMPLOYEE) ======");
            }

            // 3. Inicializar Cliente (Acceso a fidelización y compras)
            if (usuarioRepository.findByUsername("cliente").isEmpty()) {
                Usuario cliente = new Usuario();
                cliente.setUsername("cliente");
                cliente.setPassword(passwordEncoder.encode("cliente123"));
                cliente.setRoles(new HashSet<>());

                Rol rolCliente = new Rol();
                rolCliente.setNombre("ROLE_USER");
                cliente.getRoles().add(rolCliente);
                
                usuarioRepository.save(cliente);
                System.out.println("====== Usuario Creado: cliente / cliente123 (ROLE_USER) ======");
            }
        };
    }
}