# Diagramas de Flujo - Optify

## 📊 Diagramas de Procesos Clave

Este documento contiene diagramas de flujo de los procesos más importantes del sistema Optify.

## 1. 🔐 Flujo de Autenticación JWT

```
┌─────────────┐
│   Cliente   │
│  (Frontend) │
└──────┬──────┘
       │
       │ POST /users/login
       │ { username, password }
       ▼
┌─────────────────────────────────┐
│     UserController              │
│  @PostMapping("/login")         │
└──────┬──────────────────────────┘
       │
       │ validateCredentials()
       ▼
┌─────────────────────────────────┐
│       UserService               │
│  - findByUsername()             │
│  - verifyPassword()             │
└──────┬──────────────────────────┘
       │
       │ query database
       ▼
┌─────────────────────────────────┐
│     UserRepository              │
│  findByUsername(username)       │
└──────┬──────────────────────────┘
       │
       │ User entity
       ▼
┌─────────────────────────────────┐
│      PostgreSQL                 │
│   SELECT * FROM users...        │
└──────┬──────────────────────────┘
       │
       │ User data
       ▼
┌─────────────────────────────────┐
│      JwtUtil                    │
│  generateToken(user)            │
└──────┬──────────────────────────┘
       │
       │ JWT token
       ▼
┌─────────────────────────────────┐
│   Cliente (Frontend)            │
│  localStorage.setItem('token')  │
└─────────────────────────────────┘
```

## 2. 📦 Flujo de Importación de Productos

```
┌──────────────┐
│  Script de   │
│  Importación │
│  (Externo)   │
└──────┬───────┘
       │
       │ POST /api/products/import
       │ Authorization: API Key
       │ [ProductImportDto...]
       ▼
┌─────────────────────────────────────────┐
│       ProductController                 │
│  @PreAuthorize("hasRole('SCRIPT')")     │
└──────┬──────────────────────────────────┘
       │
       │ importProductsBatch(dtos)
       ▼
┌─────────────────────────────────────────┐
│           Facade                        │
│  Coordina múltiples servicios           │
└──────┬──────────────────────────────────┘
       │
       ├────────────────────────┬─────────────────────┐
       │                        │                     │
       ▼                        ▼                     ▼
┌─────────────┐    ┌────────────────────┐  ┌──────────────────┐
│  Product    │    │  StoreProduct      │  │ MatchManager     │
│  Service    │    │  Service           │  │ Service          │
└──────┬──────┘    └──────┬─────────────┘  └─────┬────────────┘
       │                  │                       │
       │ findByName()     │ findByStore&IdWeb()   │ findSimilar()
       │                  │                       │
       ▼                  ▼                       ▼
┌────────────────────────────────────────────────────────────┐
│                    Repositories                            │
│  ProductRepo | StoreProductRepo | ManualMatchRepo          │
└──────┬─────────────────────────────────────────────────────┘
       │
       ▼
┌────────────────────────────────────────────────────────────┐
│                   PostgreSQL Database                      │
│  Tables: products, store_products, manual_match_pending    │
└────────────────────────────────────────────────────────────┘
       │
       │ Resultados procesados
       ▼
┌────────────────────────────────────────────────────────────┐
│                   Cliente (Script)                         │
│  "[IMPORT] Productos procesados: {150}"                    │
└────────────────────────────────────────────────────────────┘
```

## 3. 🛒 Flujo de Optimización de Carrito

```
┌──────────────┐
│   Usuario    │
│  (Cliente)   │
└──────┬───────┘
       │
       │ 1. Añadir productos
       │ POST /cart/addProduct
       ▼
┌─────────────────────────────────┐
│      CartController             │
│  @PreAuthorize("hasRole('USER')")│
└──────┬──────────────────────────┘
       │
       ▼
┌─────────────────────────────────┐
│       CartService               │
│  addProductToCart()             │
└──────┬──────────────────────────┘
       │
       ▼
┌─────────────────────────────────┐
│     CartItemRepository          │
│  save(cartItem)                 │
└──────┬──────────────────────────┘
       │
       ▼
┌─────────────────────────────────┐
│      PostgreSQL                 │
│  INSERT INTO cart_items...      │
└─────────────────────────────────┘

       Usuario solicita simulación
       │
       │ 2. Simular optimización
       │ GET /cart/simulate
       ▼
┌─────────────────────────────────┐
│      CartController             │
│  simulate()                     │
└──────┬──────────────────────────┘
       │
       ▼
┌─────────────────────────────────────────────┐
│       CartService                           │
│  - Obtener items del carrito                │
│  - Para cada producto:                      │
│    * Buscar todos los precios en tiendas    │
│    * Identificar precio más barato          │
│  - Agrupar por tienda                       │
│  - Calcular ahorro potencial                │
│  - Generar CartSimulation                   │
└──────┬──────────────────────────────────────┘
       │
       ▼
┌─────────────────────────────────┐
│   StoreProductRepository        │
│  findBestPrices()               │
└──────┬──────────────────────────┘
       │
       ▼
┌─────────────────────────────────┐
│      PostgreSQL                 │
│  SELECT sp.*, MIN(price)...     │
└──────┬──────────────────────────┘
       │
       │ CartSimulation con recomendaciones
       ▼
┌─────────────────────────────────────────────┐
│   Cliente (Frontend)                        │
│  Muestra:                                   │
│  - Mejor precio por producto                │
│  - Tienda recomendada                       │
│  - Ahorro total posible                     │
│  - Comparación de opciones                  │
└─────────────────────────────────────────────┘
```

## 4. 🔄 Flujo de Matching Automático

```
┌───────────────┐
│ Nuevo Producto│
│   Importado   │
└───────┬───────┘
        │
        ▼
┌─────────────────────────────────────────┐
│      DataImportService                  │
│  processNewProduct()                    │
└───────┬─────────────────────────────────┘
        │
        │ buscar productos similares
        ▼
┌─────────────────────────────────────────┐
│      MatchManagerService                │
│  findSimilarProducts()                  │
│  Algoritmos:                            │
│  - Levenshtein Distance                 │
│  - Jaro-Winkler Similarity              │
│  - Jaccard Similarity                   │
└───────┬─────────────────────────────────┘
        │
        │ SQL: similarity(name, term)
        ▼
┌─────────────────────────────────────────┐
│      ProductRepository                  │
│  findSimilarByName()                    │
│  (usa operador % de PostgreSQL)         │
└───────┬─────────────────────────────────┘
        │
        │ Lista de productos candidatos
        ▼
┌─────────────────────────────────────────┐
│      ComparisonUtils                    │
│  calculateSimilarity()                  │
│  - Comparar nombres                     │
│  - Comparar marcas                      │
│  - Validar categorías                   │
└───────┬─────────────────────────────────┘
        │
        ├──── Similitud > 0.8 ────┐
        │                          │
        ▼                          ▼
  Match Automático        Match Manual Pendiente
        │                          │
        ▼                          ▼
┌─────────────────┐    ┌──────────────────────┐
│ StoreProduct    │    │ ManualMatchPending   │
│ actualizado     │    │ creado               │
└─────────────────┘    └──────────────────────┘
                                 │
                                 ▼
                       ┌──────────────────────┐
                       │   Administrador      │
                       │   revisa y decide    │
                       │   (confirma/rechaza) │
                       └──────────────────────┘
```

## 5. 🔍 Flujo de Búsqueda de Productos

```
┌──────────────┐
│   Usuario    │
└──────┬───────┘
       │
       │ GET /api/products/search?q=leche
       ▼
┌─────────────────────────────────┐
│      ProductController          │
│  @GetMapping("/search")         │
└──────┬──────────────────────────┘
       │
       ▼
┌─────────────────────────────────┐
│       Facade                    │
│  searchProducts()               │
└──────┬──────────────────────────┘
       │
       ▼
┌─────────────────────────────────┐
│     ProductService              │
│  searchByNameAndCategory()      │
└──────┬──────────────────────────┘
       │
       │ JPA Specification
       ▼
┌─────────────────────────────────────────────┐
│       ProductRepository                     │
│  findAll(specification, pageable)           │
│  Criteria:                                  │
│  - name LIKE %term%                         │
│  - category = categoryId (if specified)     │
│  - Pagination                               │
└──────┬──────────────────────────────────────┘
       │
       ▼
┌─────────────────────────────────┐
│      PostgreSQL                 │
│  SELECT * FROM products         │
│  WHERE name ILIKE '%leche%'     │
│  ORDER BY name                  │
│  LIMIT 30 OFFSET 0              │
└──────┬──────────────────────────┘
       │
       │ Page<Product>
       ▼
┌─────────────────────────────────────────────┐
│       ProductController                     │
│  Para cada producto:                        │
│  - Buscar precios en todas las tiendas      │
│  - Agregar información de disponibilidad    │
└──────┬──────────────────────────────────────┘
       │
       ▼
┌─────────────────────────────────┐
│   StoreProductRepository        │
│  findByProductId()              │
└──────┬──────────────────────────┘
       │
       │ Resultados enriquecidos
       ▼
┌─────────────────────────────────────────────┐
│   Cliente (Frontend)                        │
│  Muestra lista de productos con:            │
│  - Nombre y descripción                     │
│  - Precio más bajo                          │
│  - Tiendas donde está disponible            │
│  - Comparación de precios                   │
└─────────────────────────────────────────────┘
```

## 6. 📊 Flujo de Generación de Reportes

```
┌──────────────┐
│ Administrador│
│   o Usuario  │
└──────┬───────┘
       │
       │ GET /api/reports/view
       ▼
┌─────────────────────────────────┐
│      ReportController           │
│  viewDashboard()                │
└──────┬──────────────────────────┘
       │
       │ Retorna HTML con JavaScript
       ▼
┌─────────────────────────────────────────────┐
│   Navegador del Usuario                     │
│  1. Renderiza HTML del dashboard            │
│  2. JavaScript ejecuta loadStats()          │
└──────┬──────────────────────────────────────┘
       │
       │ Async fetch /api/reports/stats
       ▼
┌─────────────────────────────────┐
│      ReportController           │
│  @PreAuthorize("hasRole(...)")  │
│  getSystemStats()               │
└──────┬──────────────────────────┘
       │
       │ Consulta múltiples repositorios
       ▼
┌─────────────────────────────────────────────┐
│   Repositorios Paralelos                    │
│  - productRepository.count()                │
│  - storeRepository.count()                  │
│  - userRepository.count()                   │
│  - categoryRepository.count()               │
│  - storeProductRepository.count()           │
│  - cartItemRepository.count()               │
│  - manualMatchRepository.count()            │
│  - discardReferenceRepository.count()       │
└──────┬──────────────────────────────────────┘
       │
       ▼
┌─────────────────────────────────┐
│      PostgreSQL                 │
│  SELECT COUNT(*) FROM ...       │
│  (8 consultas diferentes)       │
└──────┬──────────────────────────┘
       │
       │ SystemStatsDto
       ▼
┌─────────────────────────────────────────────┐
│   ReportController                          │
│  Construye SystemStatsDto                   │
│  {                                          │
│    totalProducts: 1250,                     │
│    totalStores: 15,                         │
│    totalUsers: 340,                         │
│    ...                                      │
│  }                                          │
└──────┬──────────────────────────────────────┘
       │
       │ JSON Response
       ▼
┌─────────────────────────────────────────────┐
│   Navegador del Usuario                     │
│  1. Recibe datos JSON                       │
│  2. Actualiza tarjetas con estadísticas     │
│  3. Muestra diagramas y visualizaciones     │
└─────────────────────────────────────────────┘
```

## 🔑 Leyenda de Símbolos

```
┌─────┐
│     │  Componente/Actor
└─────┘

   │
   ▼     Flujo de datos/control

  ┌┴┐
  └─┘    Decisión

  ═══    Proceso asíncrono

  ───    Proceso síncrono
```

## 📝 Notas Importantes

### Seguridad
- Todos los flujos respetan los niveles de autorización
- JWT tokens se validan en cada request protegido
- Los roles (ADMIN, USER, SCRIPT) determinan el acceso

### Performance
- Las búsquedas utilizan índices en PostgreSQL
- La paginación reduce la carga en queries grandes
- El matching usa operadores nativos de PostgreSQL para mejor performance

### Escalabilidad
- Los repositorios pueden cachearse con Spring Cache
- Las importaciones grandes pueden procesarse en background
- El sistema de matching puede distribuirse

## 🔗 Referencias

- Ver [ARCHITECTURE.md](ARCHITECTURE.md) para detalles de arquitectura
- Ver [USAGE_GUIDE.md](USAGE_GUIDE.md) para instrucciones de uso
- Ver [README.md](../README.md) para overview general
