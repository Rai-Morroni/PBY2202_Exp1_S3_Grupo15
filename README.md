# Minimarket Plus - Backend Security Layer (JWT & Spring Security)

Este proyecto representa la capa de backend modernizada para la cadena de minimarkets **"MiniMarket Plus"**. Se enfoca en la transición de una arquitectura con estado hacia una arquitectura totalmente robusta y segura **sin estado (Stateless)**, utilizando **Spring Boot 3.x, Spring Security** y **JSON Web Tokens (JWT)** para la protección de datos personales y componentes de negocio.

## 🏗️ Características Principales

* **Autenticación Stateless:** Control de sesiones mediante tokens JWT, eliminando la necesidad de almacenar el estado de la sesión en el servidor y optimizando el backend para futuras arquitecturas de microservicios.
* **Firma Digital HMAC SHA-256:** Integración del algoritmo criptográfico simétrico HMAC con SHA-256 para firmar y validar tokens JWT, garantizando la integridad de las peticiones y evitando ataques de manipulación de identidad o elevación de privilegios.
* **Control de Acceso Basado en Roles (RBAC):** Restricción estricta de recursos a nivel de endpoint mediante la jerarquía de roles de Spring Security (`ROLE_ADMIN`, `ROLE_USER`), garantizando el principio de mínimo privilegio.
* **Protección contra Vulnerabilidades Comunes:**
    * **CSRF:** Neutralizado de raíz al prescindir de cookies de sesión automáticas en el navegador.
    * **Inyección SQL:** Consultas parametrizadas y sanitizadas nativamente mediante Spring Data JPA.
    * **Autenticación:** Gestión de credenciales seguras mediante el hashing adaptativo de **BCrypt** (`BCryptPasswordEncoder`).
* **Inicialización de Datos de Prueba:** Clase configurada (`DataInitializer`) para poblar la base de datos en memoria H2 de forma segura con un usuario administrador listo para testing.

## 🛠️ Tecnologías y Librerías

* **Java 17**
* **Spring Boot 3.4.1**
* **Spring Security**
* **Spring Data JPA**
* **H2 Database** (Base de datos en memoria para desarrollo)
* **JJWT (Java JWT - io.jsonwebtoken)** v0.11.5 (API, Impl, Jackson)
* **Lombok**

## 📂 Estructura del Paquete de Seguridad

La arquitectura de seguridad se encuentra desacoplada modularmente bajo la siguiente jerarquía de paquetes:


```
src/main/java/com/minimarket/
│
├── controller/                 # Controladores REST de negocio y pruebas
├── entity/                     # Entidades de dominio (Usuario, Rol)
├── repository/                 # Repositorios de persistencia JPA
│
└── security/                   # Capa Core de Seguridad
    ├── config/                 # Configuración de SecurityFilterChain y DataInitializer
    ├── filter/                 # Filtro interceptor JWT (OncePerRequestFilter)
    ├── model/                  # Modelos de autenticación (CustomUserDetails, LoginRequest, AuthResponse)
    └── util/                   # Utilidades de token (Generación, firma y extracción con JwtUtil)
```

## ⚙️ Configuración (`application.properties`)

El sistema utiliza las siguientes propiedades base para el entorno de desarrollo y testing:

```properties
spring.application.name=minimarket

# Configuración de H2 en memoria
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.h2.console.enabled=true

# Configuración de JPA e Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect

# JWT Configuration (Clave robusta de 256 bits y expiración de 24 horas)
jwt.secret=ClaveSecretaSumativa1Semana3MinimarketPlus!
jwt.expiration=86400000
```

## 🚀 Endpoints de la API

### 🔑 Autenticación (Público)

* `POST /api/auth/login`: Recibe las credenciales en formato JSON, valida contra la base de datos y retorna el token JWT firmado de vuelta si las credenciales coinciden.
```json
{
  "username": "admin",
  "password": "admin123"
}
```



### 🔓 Recursos Públicos

* `GET /api/public/test`: Endpoint abierto para acceso libre de clientes o visitantes del Minimarket Plus sin requerir tokens de autenticación.

### 🔒 Recursos Protegidos

* `GET /api/private/test`: Endpoint restringido únicamente para usuarios que posean un token válido y cuenten explícitamente con el rol `ROLE_ADMIN`. Retorna `403 Forbidden` en ausencia del token firmado.

## 🧪 Instrucciones para Ejecución y Pruebas

1. Clonar el repositorio localmente.
2. Ejecutar el comando de Maven para levantar el servidor embebido:
```bash
mvn clean spring-boot:run
```


3. Abrir **Postman** y realizar un `POST` al endpoint `/api/auth/login` con el usuario creado por defecto (`admin` / `admin123`).
4. Copiar el token retornado de la propiedad `token` en la respuesta JSON.
5. Realizar una petición `GET` a `/api/private/test` seleccionando en la pestaña **Authorization** el tipo **Bearer Token** y pegando el código generado.

