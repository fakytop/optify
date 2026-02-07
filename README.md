# Optify - Aplicación de Ahorro en Compras

## 📋 Descripción del Proyecto

Optify es una aplicación web diseñada para ayudar a los usuarios a ahorrar dinero en sus compras habituales mediante la comparación de precios entre diferentes tiendas. La aplicación permite gestionar productos, tiendas, carritos de compra y realizar comparaciones de precios para encontrar las mejores ofertas.

## 🏗️ Arquitectura del Sistema

```
┌─────────────────────────────────────────────────────────────┐
│                     CAPA DE PRESENTACIÓN                     │
│  (API REST - Controllers)                                    │
│  - ProductController                                         │
│  - StoreController                                          │
│  - CartController                                           │
│  - UserController                                           │
│  - ManualMatchController                                    │
│  - ReportController (Nuevo)                                 │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                     CAPA DE NEGOCIO                          │
│  (Services & Facade)                                         │
│  - ProductService                                           │
│  - StoreService                                            │
│  - CartService                                             │
│  - UserService                                             │
│  - MatchManagerService                                     │
│  - CategoryService                                         │
│  - DataImportService                                       │
│  - ProductMergeService                                     │
│  - StoreProductService                                     │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                     CAPA DE DATOS                            │
│  (JPA Repositories)                                          │
│  - ProductRepository                                        │
│  - StoreRepository                                         │
│  - CartRepository                                          │
│  - UserRepository                                          │
│  - CategoryRepository                                      │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
                    ┌──────────────────┐
                    │   PostgreSQL DB  │
                    └──────────────────┘
```

## 🔑 Características Principales

- **Gestión de Productos**: Importación masiva y manual de productos
- **Gestión de Tiendas**: CRUD completo de tiendas y sus productos
- **Comparación de Precios**: Matching automático y manual de productos similares
- **Carritos de Compra**: Creación y optimización de carritos con mejores precios
- **Autenticación**: Sistema seguro con JWT y Spring Security
- **API RESTful**: Documentación con Swagger/OpenAPI
- **Reportes**: Visualización de estadísticas y métricas del sistema

## 📊 Modelo de Datos (ERD Simplificado)

```
┌──────────────┐         ┌──────────────┐         ┌──────────────┐
│   Product    │─────────│  StoreProduct│─────────│    Store     │
├──────────────┤    1..* ├──────────────┤ *..1    ├──────────────┤
│ id           │◄────────│ product_id   │────────►│ id           │
│ name         │         │ store_id     │         │ name         │
│ description  │         │ price        │         │ address      │
│ brand        │         │ stock        │         │ city_id      │
│ imageUrl     │         └──────────────┘         │ coordinate   │
│ category_id  │                                  └──────────────┘
└──────────────┘                                          │
       │                                                  │
       │ *..1                                        1..1 │
       ▼                                                  ▼
┌──────────────┐                                  ┌──────────────┐
│  Category    │                                  │    City      │
├──────────────┤                                  ├──────────────┤
│ id           │                                  │ id           │
│ name         │                                  │ name         │
└──────────────┘                                  └──────────────┘

┌──────────────┐         ┌──────────────┐         ┌──────────────┐
│     User     │─────────│   CartItem   │─────────│  StoreProduct│
├──────────────┤    1..* ├──────────────┤ *..1    ├──────────────┤
│ id           │◄────────│ user_id      │────────►│ product_id   │
│ username     │         │ product_id   │         │ store_id     │
│ email        │         │ store_id     │         └──────────────┘
│ password     │         │ quantity     │
└──────────────┘         └──────────────┘
```

## 🚀 Tecnologías Utilizadas

- **Framework**: Spring Boot 4.0.0-SNAPSHOT
- **Java**: JDK 21
- **Base de Datos**: PostgreSQL
- **ORM**: Spring Data JPA
- **Seguridad**: Spring Security + JWT
- **Documentación API**: SpringDoc OpenAPI 3
- **String Similarity**: java-string-similarity (matching de productos)
- **Cloud**: Google Cloud SQL (PostgreSQL)
- **Build Tool**: Maven

## 📦 Dependencias Principales

```xml
- spring-boot-starter-web
- spring-boot-starter-data-jpa
- spring-boot-starter-security
- spring-boot-starter-validation
- postgresql
- jjwt (JWT tokens)
- springdoc-openapi-starter-webmvc-ui
- java-string-similarity
```

## 🛠️ Instalación y Configuración

### Prerrequisitos
- Java 21
- Maven 3.x
- PostgreSQL 12+

### Pasos de Instalación

1. Clonar el repositorio:
```bash
git clone https://github.com/fakytop/optify.git
cd optify
```

2. Configurar la base de datos en `src/main/resources/application.properties`

3. Compilar el proyecto:
```bash
./mvnw clean install
```

4. Ejecutar la aplicación:
```bash
./mvnw spring-boot:run
```

## 📍 Endpoints Principales

### Productos
- `POST /api/products/import` - Importación masiva de productos
- `GET /api/products/categories` - Listar categorías
- `GET /api/products/search` - Buscar productos
- `POST /api/products/mergeProducts` - Fusionar productos duplicados

### Tiendas
- `GET /stores/getAllStores` - Obtener todas las tiendas
- `POST /stores/addStore` - Crear nueva tienda
- `PUT /stores/updateStore` - Actualizar tienda
- `DELETE /stores/deleteStore` - Eliminar tienda

### Carrito
- `POST /cart/addProduct` - Añadir producto al carrito
- `GET /cart/simulate` - Simular optimización del carrito

### Usuarios
- `POST /users/login` - Iniciar sesión
- `POST /users/register` - Registrar nuevo usuario

### Reportes (Nuevo)
- `GET /api/reports/dashboard` - Obtener estadísticas del sistema
- `GET /api/reports/stats` - Métricas generales
- `GET /api/reports/view` - Visualizar panel de reportes (HTML)

## 📊 Visualización de Reportes

Para ver los diagramas e informes del proyecto:

1. **Acceso al Dashboard**: Navega a `http://localhost:8080/api/reports/view`
2. **API de Estadísticas**: Consulta `http://localhost:8080/api/reports/stats` para obtener métricas en JSON
3. **Documentación API**: Accede a `http://localhost:8080/swagger-ui.html` para la documentación interactiva

## 🔐 Seguridad

- Autenticación basada en JWT (JSON Web Tokens)
- Roles de usuario: ADMIN, USER, SCRIPT
- Endpoints protegidos con `@PreAuthorize`
- Passwords encriptados con BCrypt

## 🧪 Testing

Ejecutar las pruebas:
```bash
./mvnw test
```

## 📝 Documentación Adicional

Para más información sobre la arquitectura y diagramas detallados, consulta la carpeta `/docs`.

## 🤝 Contribución

Las contribuciones son bienvenidas. Por favor, abre un issue para discutir los cambios propuestos.

## 📄 Licencia

Este proyecto está bajo una licencia propietaria.

## 👥 Desarrolladores

Desarrollado por el equipo de Optify.

## 📞 Contacto

Para más información, contacta al equipo de desarrollo.
