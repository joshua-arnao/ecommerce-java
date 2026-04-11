<p align="center">
    <b>Selecciona el idioma:</b><br>
    <a href="README.md">🇺🇸 English</a> |
    <a href="README.sp.md">🇪🇸 Español</a>
</p>

---

<h1 align="center">🏋️ Sports Center — Backend E-commerce</h1>
<p align="center">Un backend Spring Boot refactorizado — de proyecto de curso a arquitectura lista para producción</p>

---

## ¿Qué es este proyecto?

Sports Center es una API REST backend para una plataforma de e-commerce de productos deportivos. Este proyecto comenzó como un proyecto de un curso de Udemy y fue completamente refactorizado para aplicar estándares profesionales reales: estructura orientada a dominio, seguridad JWT, tipos de dinero correctos, atomicidad y principios de código limpio.

El proceso de refactorización en sí es parte del valor — demuestra la capacidad de trabajar sobre bases de código existentes, identificar problemas y mejorarlos con criterio técnico sólido.

---

## Qué se refactorizó y por qué

### 🔴 Correcciones críticas

**Vulnerabilidad de seguridad — todos los endpoints estaban abiertos**
El `SecurityConfig` original tenía `anyRequest().permitAll()`, lo que dejaba órdenes, carritos y todos los endpoints completamente públicos. Corregido a `anyRequest().authenticated()` con rutas públicas explícitas solamente.

**Inconsistencia de datos en la creación de órdenes**
`createOrder` guardaba en la base de datos y luego eliminaba el carrito sin `@Transactional`. Si el delete fallaba, la base de datos quedaba con una orden duplicada y un carrito que seguía existiendo. Corregido con `@Transactional` para garantizar atomicidad.

**Tipos de dinero incorrectos**
`price`, `subTotal` y `deliveryFee` usaban `Long` y `Double`. Ambos son inaceptables para datos financieros — `Long` no puede representar decimales, `double` tiene errores de precisión de punto flotante. Todos los valores monetarios migrados a `BigDecimal`.

**`@Data` en entidades JPA con relaciones lazy**
`@Data` genera `equals()` y `hashCode()` usando todos los campos, incluyendo relaciones cargadas de forma lazy. Esto dispara queries N+1 no deseados o `LazyInitializationException`. Reemplazado por `@Getter @Setter` y `@EqualsAndHashCode(onlyExplicitlyIncluded = true)` solo en el campo `id`.

### 🟠 Mejoras de arquitectura

**Estructura por capa → Estructura por dominio**

Antes:
```
controllers/
services/
repositories/
entities/
```

Después:
```
product/   → entity, dto, repository, service, controller
order/     → entity, dto, repository, service, controller, mapper
cart/      → entity, dto, repository, service, controller
auth/      → controller, dto
user/      → entity, dto, repository, service
shared/    → security, exceptions, error, config
```

Cada dominio posee todas sus capas. Agregar una funcionalidad significa tocar un paquete, no cinco.

**Autenticación real de usuarios**
El proyecto original no tenía entidad `User` — las credenciales estaban hardcodeadas en memoria. Se construyó un dominio de usuario completo con registro, login, hasheo de contraseñas con BCrypt y autenticación stateless con JWT.

**Seguridad del carrito — userId del token, no del body**
El carrito original aceptaba `shoppingCartId` del body del request — cualquier cliente podía crear o leer el carrito de otro usuario. Corregido para extraer el `userId` del token JWT autenticado en el backend.

**Retornos null → excepciones**
Los métodos retornaban `null` cuando no encontraban recursos, obligando al controller a hacer verificaciones `if (result == null)`. Reemplazado por excepciones de dominio específicas capturadas por `GlobalExceptionHandler` que devuelven respuestas HTTP limpias.

---

## Arquitectura

```
com.ecommerce.sportscenter
    ├── product/            → Catálogo, marcas, tipos, paginación
    ├── order/              → Ciclo de vida de órdenes, envío, estado
    ├── cart/               → Carrito temporal en Redis
    ├── auth/               → Endpoints de registro y login
    ├── user/               → Entidad usuario, integración JWT
    └── shared/
        ├── security/       → JwtService, JwtAuthFilter, SecurityConfig, PasswordConfig
        ├── exceptions/     → Excepciones de dominio + GlobalExceptionHandler
        ├── error/          → Respuesta de error estandarizada
        └── config/         → CorsConfig
```

### ¿Por qué Redis para el carrito?

Redis almacena datos en memoria — las lecturas y escrituras son extremadamente rápidas. El carrito es dato temporal: existe mientras el usuario compra y desaparece cuando se crea la orden. Guardar datos temporales en MySQL (persistente) sería un desperdicio. Redis es la herramienta correcta para datos de vida corta y acceso rápido.

### ¿Por qué `shoppingCartId = userId`?

Un usuario = un carrito activo. El ID del carrito es el email del usuario extraído del token JWT. Esto garantiza que ningún usuario puede acceder al carrito de otro, y el frontend no necesita rastrear un ID de carrito separado.

---

## Stack Tecnológico

| Tecnología | Versión | Propósito |
|---|---|---|
| Java | 21 | Lenguaje — Records, APIs modernas |
| Spring Boot | 4.0.x | Framework de aplicación |
| Spring Security | 7.x | Autenticación y autorización |
| Spring Data JPA | — | Persistencia en MySQL |
| Spring Data Redis | — | Almacenamiento del carrito |
| MySQL | 8.0 | Base de datos relacional |
| Redis | 7.0 | Almacenamiento en memoria del carrito |
| Lombok | — | Reducción de código repetitivo |
| MapStruct | 1.4.x | Mapeo de objetos |
| JJWT | 0.12.3 | Generación y validación de JWT |
| SpringDoc OpenAPI | 2.8.6 | Swagger UI |
| Docker | — | Entorno local |

---

## Endpoints de la API

**Autenticación** — públicos
```
POST /api/auth/register   → Registrar nuevo usuario
POST /api/auth/login      → Login y recibir token JWT
```

**Productos** — GET es público, el resto requiere token
```
GET  /api/products                → Listar productos (paginado, filtrable)
GET  /api/products/{id}           → Obtener producto por ID
GET  /api/products/brands         → Listar todas las marcas
GET  /api/products/types          → Listar todos los tipos
```

**Carrito** — requiere token (el carrito pertenece al usuario autenticado)
```
POST   /api/shopping-carts        → Crear o actualizar carrito
GET    /api/shopping-carts        → Obtener carrito del usuario actual
DELETE /api/shopping-carts        → Eliminar carrito
```

**Órdenes** — requiere token
```
POST   /api/orders                → Crear orden desde el carrito (carrito se elimina)
GET    /api/orders                → Obtener órdenes del usuario autenticado
GET    /api/orders/{id}           → Obtener orden por ID
DELETE /api/orders/{id}           → Eliminar orden
```

---

## Modelo de Seguridad

```
Público:          POST /api/auth/**  |  GET /api/products/**  |  /swagger-ui
Autenticado:      Todo lo demás
```

Cada request autenticado pasa por `JwtAuthFilter` que:
1. Extrae el token del header `Authorization: Bearer <token>`
2. Valida la firma y la expiración
3. Carga el usuario desde la base de datos
4. Establece la autenticación en `SecurityContextHolder`

El `userId` siempre se extrae del token — nunca se confía en el body del request.

---

## Ejecutar Localmente

### Prerrequisitos
- Java 21
- Docker

### Pasos

**1. Clonar el repositorio**
```bash
git clone https://github.com/joshua-arnao/ecommerce-java.git
cd ecommerce-java
```

**2. Levantar MySQL y Redis con Docker**
```bash
docker-compose up -d
```

**3. Configurar el entorno**

Copia `application.yaml.example` a `application.yaml` y completa:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/data_ecommerce
    username: root
    password: tu_password
jwt:
  secret: tu-clave-secreta-en-base64
  expiration: 86400000
```

**4. Ejecutar la aplicación**
```bash
./mvnw spring-boot:run
```

**5. Acceder a Swagger UI**
```
http://localhost:8081/swagger-ui
```

---

## Decisiones Técnicas Clave

**`BigDecimal` para todos los valores monetarios** — `double` y `float` tienen errores de precisión inaceptables en cualquier contexto financiero. `Long` no puede representar decimales. `BigDecimal` provee aritmética decimal exacta.

**`@EqualsAndHashCode(onlyExplicitlyIncluded = true)`** — Las entidades JPA son iguales cuando tienen el mismo ID de base de datos, no cuando todos sus campos coinciden. Usar `@Data` o `equals()` por defecto en entidades con relaciones lazy dispara queries N+1 silenciosamente.

**`@Transactional` en `createOrder`** — La creación de una orden involucra guardar en MySQL y eliminar de Redis. Sin un límite transaccional, un fallo parcial deja la base de datos inconsistente. `@Transactional` garantiza todo o nada.

**Mensajes de error genéricos en endpoints de autenticación** — `UserNotFoundException` devuelve `"Invalid credentials"` en vez de `"User not found"`. Esto previene ataques de enumeración de usuarios donde un atacante podría determinar qué emails están registrados.

**`anyRequest().authenticated()` como default** — Las reglas de seguridad se evalúan en orden y se detienen en la primera coincidencia. Partir del default más restrictivo significa que cualquier endpoint nuevo está automáticamente protegido sin necesidad de recordar agregarlo a la configuración de seguridad.

---

## Relacionado

- [Sports Center Frontend](https://github.com/joshua-arnao/ecommerce-angular) — Cliente Angular

---

## Autor

**Joshua Arnao**  
Desarrollador Java autodidacta enfocado en arquitectura backend y código limpio.
