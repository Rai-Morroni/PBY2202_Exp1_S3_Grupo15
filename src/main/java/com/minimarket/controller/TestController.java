package com.minimarket.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestController {

    // Este endpoint está protegido por defecto en SecurityConfig
    @GetMapping("/private/test")
    @PreAuthorize("hasRole('ADMIN')")
    public String privateEndpoint() {
        return "Acceso autorizado de Administrador al recurso protegido del Minimarket Plus";
    }

    // Este endpoint lo marcamos como .permitAll() en SecurityConfig
    @GetMapping("/public/test")    
    public String publicEndpoint() {
        return "Bienvenido al recurso público del Minimarket Plus";
    }
}