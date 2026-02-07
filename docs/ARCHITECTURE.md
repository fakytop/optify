# Arquitectura Detallada de Optify

## Índice
1. [Visión General](#visión-general)
2. [Arquitectura de Capas](#arquitectura-de-capas)
3. [Patrones de Diseño](#patrones-de-diseño)
4. [Diagramas de Secuencia](#diagramas-de-secuencia)
5. [Modelo de Dominio](#modelo-de-dominio)

## Visión General

Optify utiliza una arquitectura en capas basada en Spring Boot, siguiendo los principios de Clean Architecture y Domain-Driven Design (DDD).

## Arquitectura de Capas

### 1. Capa de Presentación (Controllers)

La capa de presentación expone endpoints REST para la interacción con el cliente.

```
Controllers
├── ProductController      - Gestión de productos
├── StoreController        - Gestión de tiendas
├── CartController         - Gestión de carritos
├── UserController         - Autenticación y usuarios
├── ManualMatchController  - Matching manual de productos
└── ReportController       - Reportes y estadísticas
```

**Responsabilidades:**
- Validación de entrada
- Transformación de DTOs
- Manejo de respuestas HTTP
- Aplicación de seguridad (JWT)

### 2. Capa de Negocio (Services & Facade)

```
Services
├── ProductService          - Lógica de productos
├── StoreService           - Lógica de tiendas
├── CartService            - Lógica de carritos
├── UserService            - Lógica de usuarios
├── MatchManagerService    - Matching automático
├── CategoryService        - Gestión de categorías
├── DataImportService      - Importación de datos
├── ProductMergeService    - Fusión de productos
└── StoreProductService    - Relación tienda-producto
```

**Facade Pattern:**
```
┌──────────────┐
│   Facade     │
└──────┬───────┘
       │
       ├──► ProductService
       ├──► StoreService
       ├──► CartService
       ├──► MatchManagerService
       └──► CategoryService
```

### 3. Capa de Persistencia (Repositories)

Utiliza Spring Data JPA para el acceso a datos.

```
Repositories (JPA)
├── ProductRepository
├── StoreRepository
├── CartRepository
├── UserRepository
├── CategoryRepository
├── StoreProductRepository
└── ManualMatchPendingRepository
```

## Patrones de Diseño

### 1. Facade Pattern
El Facade centraliza el acceso a múltiples servicios, simplificando la interacción desde los controllers.

### 2. Repository Pattern
Spring Data JPA implementa el patrón Repository para abstraer el acceso a datos.

### 3. DTO Pattern
Se utilizan DTOs (Data Transfer Objects) para transferir datos entre capas:
- `ProductImportDto`
- `UserLoginDto`
- `CartSimulationDto`

### 4. Service Layer Pattern
La lógica de negocio está encapsulada en servicios reutilizables.

## Diagramas de Secuencia

### Flujo de Importación de Productos

```
Cliente          Controller       Facade        Service         Repository      DB
  │                  │               │             │                │           │
  ├─POST import────►│               │             │                │           │
  │                  ├──validate────►│             │                │           │
  │                  │               ├──import────►│                │           │
  │                  │               │             ├──findByName──►│           │
  │                  │               │             │                ├──query──►│
  │                  │               │             │                │◄─result─┤
  │                  │               │             │◄──product─────┤           │
  │                  │               │             ├──save────────►│           │
  │                  │               │             │                ├──insert─►│
  │                  │               │             │                │◄─success┤
  │                  │               │◄──result───┤                │           │
  │                  │◄──response───┤             │                │           │
  │◄─200 OK─────────┤               │             │                │           │
```

### Flujo de Búsqueda y Comparación de Precios

```
Cliente          Controller       Service        Repository      DB
  │                  │               │                │           │
  ├─GET search──────►│               │                │           │
  │                  ├──search──────►│                │           │
  │                  │               ├──findProducts►│           │
  │                  │               │                ├──query──►│
  │                  │               │                │◄─results┤
  │                  │               │◄──products────┤           │
  │                  │               ├──compare─────►│           │
  │                  │               │  prices       │           │
  │                  │◄──results────┤                │           │
  │◄─200 OK─────────┤               │                │           │
```

### Flujo de Autenticación

```
Cliente          UserController   UserService   JwtUtil   Repository   DB
  │                  │               │            │           │         │
  ├─POST login──────►│               │            │           │         │
  │                  ├──validate────►│            │           │         │
  │                  │               ├──findUser─►│           │         │
  │                  │               │            │           ├─query──►│
  │                  │               │            │           │◄result─┤
  │                  │               │◄──user────┤           │         │
  │                  │               ├──verify password       │         │
  │                  │               ├──generate token───────►│         │
  │                  │               │            │◄──JWT────┤         │
  │                  │◄──JWT token──┤            │           │         │
  │◄─200 + JWT──────┤               │            │           │         │
```

## Modelo de Dominio

### Entidades Principales

#### Product
```java
Product
├── id: Integer
├── name: String
├── description: String
├── brand: String
├── imageUrl: String
└── category: Category
```

#### Store
```java
Store
├── id: Integer
├── name: String
├── address: String
├── city: City
├── coordinate: String
└── products: List<StoreProduct>
```

#### StoreProduct (Tabla de Relación)
```java
StoreProduct
├── product: Product
├── store: Store
├── price: BigDecimal
├── stock: Integer
└── lastUpdated: Timestamp
```

#### User
```java
User
├── id: Integer
├── username: String
├── email: String
├── password: String (encrypted)
├── role: Role
└── cart: List<CartItem>
```

### Relaciones

```
Product ───1:N──► StoreProduct ◄───N:1─── Store
   │                                          │
   │                                          │
  1:N                                        1:1
   │                                          │
   ▼                                          ▼
Category                                    City


User ───1:N──► CartItem ◄───N:1─── StoreProduct
```

## Flujos de Negocio Principales

### 1. Matching Automático de Productos

El sistema utiliza algoritmos de similitud de strings para emparejar productos similares entre diferentes tiendas:

1. Se importa un nuevo producto de una tienda
2. El sistema busca productos similares usando:
   - Similitud de nombres (Levenshtein, Jaro-Winkler)
   - Similitud de marcas
   - Categoría
3. Si encuentra coincidencias probables, crea un `ManualMatchPending`
4. Un administrador confirma o rechaza el match

### 2. Optimización de Carrito

El sistema optimiza el carrito de compra del usuario:

1. Usuario añade productos a su carrito
2. Sistema calcula el mejor precio para cada producto
3. Sistema sugiere la mejor combinación de tiendas
4. Considera:
   - Precios
   - Disponibilidad
   - Proximidad de tiendas

### 3. Importación de Datos

Flujo para importar datos de productos desde tiendas externas:

1. Script externo envía lote de productos
2. Sistema valida datos
3. Para cada producto:
   - Busca producto existente
   - Si existe, actualiza precio y stock
   - Si no existe, crea nuevo producto y busca matches
4. Retorna resumen de importación

## Seguridad

### JWT Authentication Flow

```
┌─────────────────────────────────────────────────────────┐
│  Cliente envía credenciales                             │
└────────────────┬────────────────────────────────────────┘
                 │
                 ▼
┌─────────────────────────────────────────────────────────┐
│  UserController recibe y valida                         │
└────────────────┬────────────────────────────────────────┘
                 │
                 ▼
┌─────────────────────────────────────────────────────────┐
│  UserService verifica password                          │
└────────────────┬────────────────────────────────────────┘
                 │
                 ▼
┌─────────────────────────────────────────────────────────┐
│  JwtUtil genera token JWT                               │
└────────────────┬────────────────────────────────────────┘
                 │
                 ▼
┌─────────────────────────────────────────────────────────┐
│  Cliente guarda token y lo envía en cada request        │
└────────────────┬────────────────────────────────────────┘
                 │
                 ▼
┌─────────────────────────────────────────────────────────┐
│  JwtAuthenticationFilter valida token en cada request   │
└─────────────────────────────────────────────────────────┘
```

### Roles y Permisos

- **ADMIN**: Acceso completo al sistema
- **USER**: Acceso a funcionalidades de usuario (carrito, búsqueda)
- **SCRIPT**: Acceso limitado para importación automática de datos

## Escalabilidad y Rendimiento

### Estrategias Implementadas

1. **Paginación**: Todos los endpoints de listado soportan paginación
2. **Índices de Base de Datos**: En campos frecuentemente consultados
3. **Caching**: (Potencial mejora futura con Spring Cache)
4. **Búsqueda Optimizada**: Uso de especificaciones JPA para queries complejas

### Mejoras Futuras

- Implementar Redis para caché de productos populares
- Añadir búsqueda full-text con Elasticsearch
- Implementar queue system (RabbitMQ/Kafka) para importaciones grandes
- Añadir CDN para imágenes de productos

## Deployment

El sistema está preparado para deployment en:
- Google Cloud Platform (Cloud SQL para PostgreSQL)
- Docker (Dockerfile incluido)
- Cualquier servidor de aplicaciones compatible con Spring Boot

## Monitoreo

Se recomienda implementar:
- Spring Boot Actuator para métricas
- Prometheus + Grafana para visualización
- Logging centralizado con ELK stack
