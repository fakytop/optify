# 📊 Resumen: Cómo Ver Diagramas e Informes del Proyecto Optify

## 🎯 Solución Implementada

Se ha implementado un sistema completo de visualización de diagramas e informes para el proyecto Optify, que permite a los usuarios y desarrolladores acceder fácilmente a la documentación, arquitectura y estadísticas del sistema.

## 📁 Archivos Creados

### Documentación Principal

1. **README.md** (raíz del proyecto)
   - Descripción general del proyecto
   - Diagrama de arquitectura
   - Modelo de datos (ERD)
   - Instrucciones de instalación y uso
   - Lista completa de endpoints
   - Tecnologías utilizadas

2. **docs/ARCHITECTURE.md**
   - Arquitectura detallada en capas
   - Patrones de diseño utilizados
   - Diagramas de secuencia
   - Modelo de dominio completo
   - Flujos de negocio
   - Estrategias de seguridad y escalabilidad

3. **docs/FLOW_DIAGRAMS.md**
   - Diagramas de flujo de procesos clave:
     * Autenticación JWT
     * Importación de productos
     * Optimización de carrito
     * Matching automático
     * Búsqueda de productos
     * Generación de reportes

4. **docs/USAGE_GUIDE.md**
   - Guía paso a paso para usar el sistema de reportes
   - Ejemplos de código
   - Solución de problemas
   - Tips y mejores prácticas

### Código Backend

5. **src/main/java/com/optify/dto/SystemStatsDto.java**
   - DTO para transportar estadísticas del sistema
   - Contiene métricas de productos, tiendas, usuarios, etc.

6. **src/main/java/com/optify/controllers/ReportController.java**
   - Controller REST con 3 endpoints:
     * `/api/reports/stats` - API JSON con estadísticas
     * `/api/reports/view` - Dashboard HTML interactivo
     * `/api/reports/dashboard` - Información general

## 🌐 Formas de Acceder a los Diagramas e Informes

### 1. 📊 Dashboard Web Interactivo (RECOMENDADO)

```
URL: http://localhost:8080/api/reports/view
```

**Características:**
- ✅ Interfaz visual moderna y atractiva
- ✅ Estadísticas en tiempo real (con autenticación)
- ✅ Diagramas de arquitectura ASCII
- ✅ Modelo de datos ERD
- ✅ Enlaces a documentación adicional
- ✅ No requiere autenticación para ver diagramas (solo para stats)

### 2. 📚 Documentación en Archivos Markdown

**Archivos disponibles en el repositorio:**

```
proyecto/
├── README.md                    # Inicio aquí
└── docs/
    ├── ARCHITECTURE.md          # Arquitectura detallada
    ├── FLOW_DIAGRAMS.md         # Diagramas de flujo
    └── USAGE_GUIDE.md           # Guía de uso
```

**Cómo leer:**
- Directamente en GitHub (renderiza automáticamente)
- Con cualquier editor Markdown
- En VS Code con vista previa
- Con herramientas como Typora, Mark Text, etc.

### 3. 🔌 API REST para Integración

```bash
# Obtener estadísticas del sistema
curl -X GET "http://localhost:8080/api/reports/stats" \
  -H "Authorization: Bearer {token}"

# Respuesta JSON:
{
  "totalProducts": 1250,
  "totalStores": 15,
  "totalUsers": 340,
  "totalCategories": 45,
  "totalStoreProducts": 8900,
  "totalCartItems": 420,
  "totalPendingMatches": 23,
  "totalDiscardedReferences": 156
}
```

### 4. 📖 Swagger UI

```
URL: http://localhost:8080/swagger-ui.html
```

- Documentación interactiva de todos los endpoints
- Posibilidad de probar la API directamente
- Esquemas de request/response

## 🚀 Inicio Rápido

### Para ver SOLO los diagramas (sin autenticación):

1. Inicia el servidor:
   ```bash
   ./mvnw spring-boot:run
   ```

2. Abre tu navegador y navega a:
   ```
   http://localhost:8080/api/reports/view
   ```

3. Verás el dashboard con:
   - Diagramas de arquitectura
   - Modelo de datos (ERD)
   - Enlaces a documentación

### Para ver diagramas + estadísticas (requiere autenticación):

1. Inicia sesión primero:
   ```bash
   curl -X POST "http://localhost:8080/users/login" \
     -H "Content-Type: application/json" \
     -d '{"username":"tu_usuario","password":"tu_contraseña"}'
   ```

2. Guarda el token JWT que recibes

3. El dashboard cargará automáticamente las estadísticas si tienes un token válido en localStorage

### Para desarrolladores (acceso a archivos):

1. Clona el repositorio:
   ```bash
   git clone https://github.com/fakytop/optify.git
   cd optify
   ```

2. Lee la documentación:
   ```bash
   # Descripción general
   cat README.md
   
   # Arquitectura detallada
   cat docs/ARCHITECTURE.md
   
   # Diagramas de flujo
   cat docs/FLOW_DIAGRAMS.md
   
   # Guía de uso
   cat docs/USAGE_GUIDE.md
   ```

## 📋 Contenido de los Diagramas

### Diagramas Disponibles:

1. **Arquitectura en Capas**
   - Capa de Presentación (Controllers)
   - Capa de Negocio (Services)
   - Capa de Persistencia (Repositories)
   - Base de Datos

2. **Modelo de Datos (ERD)**
   - Entidades principales: Product, Store, User, Category
   - Relaciones: StoreProduct, CartItem, ManualMatchPending
   - Cardinalidades y claves foráneas

3. **Diagramas de Secuencia**
   - Flujo de autenticación JWT
   - Importación de productos
   - Búsqueda y comparación de precios
   - Optimización de carrito
   - Matching automático

4. **Patrones de Diseño**
   - Facade Pattern
   - Repository Pattern
   - DTO Pattern
   - Service Layer Pattern

## 🎨 Características del Dashboard HTML

El dashboard incluye:

- **8 Tarjetas de Estadísticas**:
  * Total de Productos
  * Total de Tiendas
  * Total de Usuarios
  * Total de Categorías
  * Productos en Tiendas
  * Items en Carritos
  * Matches Pendientes
  * Referencias Descartadas

- **Diagramas ASCII**:
  * Arquitectura del Sistema
  * Modelo de Datos (ERD)

- **Enlaces Rápidos**:
  * API Swagger
  * Repositorio GitHub
  * API de Estadísticas JSON

- **Diseño Moderno**:
  * Gradientes y efectos visuales
  * Responsive (se adapta a móviles)
  * Animaciones hover
  * Iconos emoji para fácil identificación

## 🔒 Seguridad

- **Dashboard (`/api/reports/view`)**: No requiere autenticación (para que cualquiera pueda ver los diagramas)
- **Estadísticas (`/api/reports/stats`)**: Requiere autenticación JWT con rol ADMIN o USER
- **Dashboard Info (`/api/reports/dashboard`)**: No requiere autenticación

## 💡 Casos de Uso

### Caso 1: Nuevo Desarrollador en el Equipo
1. Clona el repositorio
2. Lee el README.md para entender el proyecto
3. Consulta docs/ARCHITECTURE.md para la arquitectura
4. Revisa docs/FLOW_DIAGRAMS.md para entender los flujos

### Caso 2: Presentación a Stakeholders
1. Inicia el servidor
2. Abre el dashboard en `/api/reports/view`
3. Muestra las estadísticas en tiempo real
4. Explica la arquitectura con los diagramas visuales

### Caso 3: Integración con Herramientas de Monitoreo
1. Usa el endpoint `/api/reports/stats`
2. Integra con Grafana, Datadog, etc.
3. Crea alertas basadas en las métricas

### Caso 4: Documentación para Cliente
1. Comparte el README.md
2. Proporciona acceso al dashboard web
3. Referencia los documentos en /docs/ para más detalles

## 🎯 Beneficios de la Solución

✅ **Accesibilidad**: Múltiples formas de acceder a la información
✅ **Documentación Completa**: Desde overview hasta detalles técnicos
✅ **Visual**: Diagramas ASCII fáciles de leer y mantener
✅ **Actualizable**: Dashboard con estadísticas en tiempo real
✅ **Integrable**: API REST para herramientas externas
✅ **Profesional**: Documentación de calidad enterprise
✅ **Multilingüe**: Todo en español para el equipo
✅ **Mantenible**: Archivos markdown versionados en Git

## 📞 Soporte

Para más información:
- Consulta docs/USAGE_GUIDE.md para instrucciones detalladas
- Revisa docs/ARCHITECTURE.md para entender la estructura
- Explora docs/FLOW_DIAGRAMS.md para los flujos de proceso

## ✅ Checklist de Implementación

- [x] README.md con descripción general y diagramas
- [x] docs/ARCHITECTURE.md con arquitectura detallada
- [x] docs/FLOW_DIAGRAMS.md con diagramas de flujo
- [x] docs/USAGE_GUIDE.md con guía de uso
- [x] SystemStatsDto para transportar estadísticas
- [x] ReportController con endpoints REST
- [x] Dashboard HTML interactivo
- [x] Integración con Swagger
- [x] Documentación en español
- [x] Ejemplos de código y uso

## 🎉 ¡Listo para Usar!

El sistema está completo y listo para ser usado. Simplemente:

1. Inicia el servidor: `./mvnw spring-boot:run`
2. Visita: `http://localhost:8080/api/reports/view`
3. Explora la documentación en la carpeta `/docs/`

---

**Fecha de Creación**: Febrero 2026  
**Versión**: 1.0  
**Proyecto**: Optify - Sistema de Comparación de Precios
