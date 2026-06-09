# Minimarket Plus - Backend Security Layer (JWT & Spring Security) - v2

Este proyecto representa la capa de backend modernizada para la cadena de minimarkets **"MiniMarket Plus"**. Se enfoca en la transición de una arquitectura con estado hacia una arquitectura totalmente robusta y segura **sin estado (Stateless)**, utilizando **Spring Boot 3.x, Spring Security** y **JSON Web Tokens (JWT)** para la protección de datos personales y componentes de negocio.

En esta versión se encuentra completamente integrada y funcional la **Autenticación y Autorización basada en Roles (RBAC)** con control de accesos granular a nivel de método, cubriendo los requerimientos específicos del cliente para los tres perfiles del sistema.

## 🏗️ Características Principales

* **Autenticación Stateless:** Control de sesiones mediante tokens JWT, eliminando la necesidad de almacenar el estado de la sesión en el servidor y optimizando el backend para futuras arquitecturas de microservicios.
* **Firma Digital HMAC SHA-256:** Integración del algoritmo criptográfico simétrico HMAC con SHA-256 para firmar y validar tokens JWT, garantizando la integridad de las peticiones y evitando ataques de manipulación de identidad o elevación de privilegios.
* **Control de Acceso Basado en Roles (RBAC):** Restricción estricta de recursos a nivel de endpoint mediante la jerarquía de roles de Spring Security (`ROLE_ADMIN`, `ROLE_EMPLOYEE`, `ROLE_USER`), garantizando el principio de mínimo privilegio.
* **Protección contra Vulnerabilidades Comunes:**
    * **CSRF:** Neutralizado de raíz al prescindir de cookies de sesión automáticas en el navegador.
    * **Inyección SQL:** Consultas parametrizadas y sanitizadas nativamente mediante Spring Data JPA.
    * **Autenticación Rota:** Gestión de credenciales seguras mediante el hashing adaptativo de **BCrypt** (`BCryptPasswordEncoder`).
* **Inicialización y Sembrado de Datos (Seeding):** Clase configurada (`DataInitializer`) para poblar automáticamente la base de datos en memoria H2 con tres usuarios de prueba correspondientes a cada nivel de acceso del negocio.

## 🛠️ Tecnologías y Librerías

* **Java 17 / 21**
* **Spring Boot 3.4.1**
* **Spring Security**
* **Spring Data JPA**
* **H2 Database** (Base de datos en memoria para desarrollo)
* **JJWT (Java JWT - io.jsonwebtoken)** v0.11.5 (API, Impl, Jackson)
* **Lombok**

## 👥 Matriz de Usuarios y Roles de Prueba

Al iniciar la aplicación, la clase `DataInitializer` se encarga de verificar y sembrar de forma automática las siguientes identidades en la base de datos H2 en memoria con contraseñas encriptadas mediante **BCrypt**:

| Usuario | Contraseña | Rol de Spring Security | Descripción del Nivel de Acceso |
| :--- | :--- | :--- | :--- |
| **admin** | admin123 | `ROLE_ADMIN` | **Nivel Alto:** Control total, reportes financieros corporativos y auditoría. |
| **empleado** | empleado123 | `ROLE_EMPLOYEE` | **Nivel Medio:** Operación de negocio, control de stock y gestión de inventario. |
| **cliente** | cliente123 | `ROLE_USER` | **Nivel Básico:** Perfil de cliente, consulta de catálogo y sistema de fidelización. |

## 🚀 Endpoints de la API y Reglas de Autorización

### 🔑 Autenticación (Público)
* `POST /api/auth/login`: Recibe las credenciales en formato JSON, valida con el `AuthenticationManager` contra los registros persistidos de H2 y retorna un objeto `AuthResponse` que encapsula el token JWT firmado si los datos coinciden.

### 🔓 Módulo de Clientes y Visitantes (Acceso Público)
* `GET /api/public/productos`: Endpoint abierto y libre de tokens. Permite consultar la disponibilidad y catálogo de productos del Minimarket Plus.

### 🔒 Módulo de Fidelización de Clientes (Restringido - Nivel Básico)
* `GET /api/cliente/fidelizacion`: Protegido con `@PreAuthorize("hasRole('USER')")`. Da acceso a la información del perfil del cliente y a su sistema de acumulación de puntos por compras realizadas.

### 📦 Módulo de Operación e Inventario (Restringido - Nivel Medio/Alto)
* `GET /api/empleado/inventario`: Protegido con `@PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")`. Permite a empleados autorizados y al administrador realizar el control de stock en tiempo real en todas las sucursales.

### 📊 Módulo Gerencial (Restringido - Nivel Alto)
* `GET /api/admin/reportes`: Protegido con `@PreAuthorize("hasRole('ADMIN')")`. Permite la generación de reportes financieros analíticos, tendencias de compra y toma de decisiones corporativas.

## 🧪 Instrucciones para Ejecución y Pruebas en Postman

1.  Clonar el repositorio localmente.
2.  Ejecutar el comando de Maven para levantar el servidor embebido:
    ```bash
    mvn clean spring-boot:run
    ```
3.  **Generación de Token por Rol:** Realizar un `POST` al endpoint `/api/auth/login` enviando las credenciales de cualquiera de los tres usuarios configurados (`admin`, `empleado` o `cliente`). Copiar la propiedad `token` del JSON de respuesta.
4.  **Prueba de Protección Cruzada (RBAC):**
    * Intente acceder con el token generado del `cliente` al endpoint `/api/admin/reportes`. **Resultado esperado:** `403 Forbidden` (Demuestra que la restricción estricta por roles de Spring Security está activa).
    * Acceda con el token generado del `admin` o `empleado` al endpoint `/api/empleado/inventario`. **Resultado esperado:** `200 OK` junto con la carga útil correspondiente.