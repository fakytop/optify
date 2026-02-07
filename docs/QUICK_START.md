# 🚀 Guía Rápida - Optify Dashboard

## Inicio Rápido (30 segundos)

1. Inicia el servidor:
   ```bash
   ./mvnw spring-boot:run
   ```

2. Abre tu navegador:
   ```
   http://localhost:8080/api/reports/view
   ```

¡Listo! Verás el dashboard completo con diagramas.

## 📍 Enlaces Importantes

| Recurso | URL | Requiere Auth |
|---------|-----|---------------|
| 🌐 Dashboard Web | `/api/reports/view` | ❌ No |
| 📊 API Estadísticas | `/api/reports/stats` | ✅ Sí (JWT) |
| 📖 Swagger UI | `/swagger-ui.html` | ❌ No |
| 📚 README | `/README.md` | - |
| 🏗️ Arquitectura | `/docs/ARCHITECTURE.md` | - |
| 🔄 Diagramas de Flujo | `/docs/FLOW_DIAGRAMS.md` | - |
| 📝 Guía de Uso | `/docs/USAGE_GUIDE.md` | - |

## 🔑 Obtener Token JWT (si necesitas stats)

```bash
# 1. Login
curl -X POST "http://localhost:8080/users/login" \
  -H "Content-Type: application/json" \
  -d '{"username":"tu_usuario","password":"tu_contraseña"}'

# 2. Usar el token en requests
curl -X GET "http://localhost:8080/api/reports/stats" \
  -H "Authorization: Bearer TU_TOKEN_AQUI"
```

## 📊 Qué Verás en el Dashboard

### Estadísticas (si estás autenticado):
- 📦 Total de Productos
- 🏪 Total de Tiendas
- 👥 Total de Usuarios
- 📂 Total de Categorías
- 🏷️ Productos en Tiendas
- 🛒 Items en Carritos
- ⏳ Matches Pendientes
- ❌ Referencias Descartadas

### Diagramas (siempre visibles):
- 🏗️ Arquitectura del Sistema
- 🗂️ Modelo de Datos (ERD)
- 🔗 Enlaces a documentación

## 📚 Documentación Disponible

### README.md
**Qué incluye:**
- Descripción general del proyecto
- Arquitectura básica
- Modelo de datos
- Lista de endpoints
- Instrucciones de instalación

### docs/ARCHITECTURE.md
**Qué incluye:**
- Arquitectura en capas detallada
- Patrones de diseño (Facade, Repository, DTO)
- Diagramas de secuencia
- Modelo de dominio completo
- Estrategias de seguridad

### docs/FLOW_DIAGRAMS.md
**Qué incluye:**
- 6 diagramas de flujo completos:
  * Autenticación JWT
  * Importación de productos
  * Optimización de carrito
  * Matching automático
  * Búsqueda de productos
  * Generación de reportes

### docs/USAGE_GUIDE.md
**Qué incluye:**
- Guía paso a paso
- Ejemplos de código (curl, JavaScript)
- Casos de uso
- Solución de problemas

### docs/SUMMARY.md
**Qué incluye:**
- Resumen ejecutivo
- Todas las formas de acceder
- Beneficios de la solución
- Checklist de implementación

## 💡 Tips Rápidos

### Ver solo diagramas (sin stats)
➜ Ve directamente a `/api/reports/view`
- No necesitas autenticación
- Verás arquitectura y ERD

### Ver diagramas + estadísticas
➜ Inicia sesión primero
➜ Luego ve a `/api/reports/view`
- El dashboard cargará stats automáticamente

### Integrar en tu app
```javascript
fetch('/api/reports/stats', {
  headers: {
    'Authorization': 'Bearer ' + token
  }
})
.then(r => r.json())
.then(stats => console.log(stats))
```

### Exportar documentación
```bash
# Copiar todos los docs
cp README.md /destino/
cp -r docs/ /destino/
```

## 🔍 Ejemplo de Respuesta API

```json
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

## ❓ Preguntas Frecuentes

**P: ¿Necesito autenticarme para ver diagramas?**
R: No, el endpoint `/api/reports/view` es público. Solo necesitas auth para `/api/reports/stats`.

**P: ¿Dónde está la documentación técnica?**
R: En la carpeta `/docs/`. Comienza con `ARCHITECTURE.md`.

**P: ¿Cómo actualizo las estadísticas?**
R: Las estadísticas se cargan en tiempo real desde la base de datos cada vez que consultas `/api/reports/stats`.

**P: ¿Puedo personalizar el dashboard?**
R: Sí, edita el método `viewDashboard()` en `ReportController.java`.

**P: ¿Los diagramas se actualizan automáticamente?**
R: Los diagramas en markdown son estáticos. Para cambios arquitectónicos, actualiza los archivos `.md`.

## 📞 Más Información

- **Documentación completa**: Ver `/docs/USAGE_GUIDE.md`
- **Detalles técnicos**: Ver `/docs/ARCHITECTURE.md`
- **Flujos de proceso**: Ver `/docs/FLOW_DIAGRAMS.md`
- **Resumen ejecutivo**: Ver `/docs/SUMMARY.md`

---

**Versión**: 1.0  
**Fecha**: Febrero 2026  
**Proyecto**: Optify - Sistema de Comparación de Precios
