package com.minimarket.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestController {

    // 1. Endpoint completamente público (Consulta de productos para Clientes y Visitantes)
    @GetMapping("/public/productos")
    public String consultarProductos() {
        return "Catálogo de Productos del Minimarket Plus (Acceso Libre).";
    }

    // 2. Endpoint exclusivo para Clientes Autenticados (ROLE_USER)
    @GetMapping("/cliente/fidelizacion")
    @PreAuthorize("hasRole('USER')")
    public String consultarPuntosFidelizacion() {
        return "Acceso Autorizado: Perfil del Cliente - Sistema de puntos acumulados por compras.";
    }

    // 3. Endpoint para Empleados y Administradores (ROLE_EMPLOYEE o ROLE_ADMIN)
    @GetMapping("/empleado/inventario")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
    public String gestionarInventario() {
        return "Acceso Autorizado: Panel de Gestión de Stock en tiempo real y Control de Inventarios.";
    }

    // 4. Endpoint exclusivo para el Administrador (ROLE_ADMIN)
    @GetMapping("/admin/reportes")
    @PreAuthorize("hasRole('ADMIN')")
    public String generarReportesGerenciales() {
        return "Acceso Restringido Otorgado: Reportes consolidados de ventas financieras y tendencias de compra corporativas.";
    }
}