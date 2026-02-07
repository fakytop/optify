# Guía de Uso de API - Optify

## 📊 Cómo Ver Diagramas e Informes del Proyecto

Esta guía te explica cómo acceder a los diagramas e informes sobre el proyecto Optify.

## Opciones Disponibles

### 1. 🌐 Dashboard Web Interactivo

Accede al dashboard visual en tu navegador:

```
http://localhost:8080/api/reports/view
```

Este dashboard incluye:
- Estadísticas en tiempo real del sistema
- Diagramas de arquitectura
- Modelo de datos (ERD)
- Enlaces a documentación
- Visualización moderna e interactiva

**Nota**: El dashboard HTML no requiere autenticación para ver los diagramas, pero las estadísticas sí requieren autenticación.

### 2. 📊 API de Estadísticas (JSON)

Obtén estadísticas programáticamente:

```bash
GET /api/reports/stats
```

**Requiere**: Token JWT (Bearer Authentication)

**Respuesta de ejemplo**:
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

**Ejemplo con cURL**:
```bash
curl -X GET "http://localhost:8080/api/reports/stats" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### 3. ℹ️ Información del Dashboard

Obtén información general del sistema de reportes:

```bash
GET /api/reports/dashboard
```

**No requiere autenticación**

Esta endpoint retorna metadatos sobre los reportes disponibles.

### 4. 📚 Documentación Estática

#### README.md
Ubicación: `/README.md` (raíz del proyecto)

Contiene:
- Descripción general del proyecto
- Arquitectura del sistema (diagrama ASCII)
- Modelo de datos simplificado
- Instrucciones de instalación
- Lista de endpoints
- Tecnologías utilizadas

#### Arquitectura Detallada
Ubicación: `/docs/ARCHITECTURE.md`

Contiene:
- Arquitectura en capas detallada
- Patrones de diseño utilizados
- Diagramas de secuencia de flujos principales
- Modelo de dominio completo
- Estrategias de seguridad
- Recomendaciones de escalabilidad

### 5. 🔍 Swagger/OpenAPI

Accede a la documentación interactiva de la API:

```
http://localhost:8080/swagger-ui.html
```

Swagger proporciona:
- Documentación de todos los endpoints
- Posibilidad de probar la API directamente
- Esquemas de request/response
- Información de autenticación

## 🔐 Autenticación

Para acceder a los endpoints protegidos, necesitas un token JWT:

### 1. Iniciar Sesión

```bash
POST /users/login
Content-Type: application/json

{
  "username": "tu_usuario",
  "password": "tu_contraseña"
}
```

### 2. Usar el Token

Incluye el token en el header de tus peticiones:

```
Authorization: Bearer {tu_token_jwt}
```

## 📈 Casos de Uso

### Ver Dashboard Completo

1. Abre tu navegador
2. Navega a: `http://localhost:8080/api/reports/view`
3. El dashboard se cargará con los diagramas
4. Si tienes un token válido guardado, verás también las estadísticas

### Obtener Estadísticas Programáticamente

```bash
# 1. Obtener token
TOKEN=$(curl -X POST "http://localhost:8080/users/login" \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}' \
  | jq -r '.token')

# 2. Consultar estadísticas
curl -X GET "http://localhost:8080/api/reports/stats" \
  -H "Authorization: Bearer $TOKEN"
```

### Integrar en una Aplicación Frontend

```javascript
// JavaScript/React ejemplo
const fetchStats = async () => {
  const token = localStorage.getItem('token');
  
  const response = await fetch('/api/reports/stats', {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  });
  
  const stats = await response.json();
  console.log('Estadísticas del sistema:', stats);
  return stats;
};
```

## 📋 Resumen de Endpoints de Reportes

| Endpoint | Método | Autenticación | Descripción |
|----------|--------|---------------|-------------|
| `/api/reports/view` | GET | No | Dashboard HTML interactivo |
| `/api/reports/stats` | GET | Sí (JWT) | Estadísticas del sistema (JSON) |
| `/api/reports/dashboard` | GET | No | Información general del dashboard |

## 🎨 Personalización

El dashboard HTML está incluido directamente en el `ReportController`. Para personalizarlo:

1. Edita el código HTML en el método `viewDashboard()`
2. Modifica los estilos CSS inline
3. Ajusta el JavaScript para cambiar el comportamiento

## 🔧 Solución de Problemas

### Las estadísticas no cargan

**Problema**: El dashboard muestra un error al cargar estadísticas.

**Solución**: 
- Verifica que estés autenticado (token JWT válido)
- Comprueba que el servidor esté ejecutándose
- Revisa la consola del navegador para errores

### Error 401 Unauthorized

**Problema**: Al acceder a `/api/reports/stats` obtienes un error 401.

**Solución**:
- Necesitas un token JWT válido
- Inicia sesión en `/users/login` primero
- Incluye el token en el header `Authorization`

### No puedo acceder a Swagger

**Problema**: Swagger UI no se carga.

**Solución**:
- Verifica que el servidor esté corriendo
- Intenta acceder a `/swagger-ui/index.html` en lugar de `/swagger-ui.html`
- Comprueba que la dependencia springdoc-openapi esté en el `pom.xml`

## 💡 Tips

1. **Bookmark el Dashboard**: Guarda el link del dashboard para acceso rápido
2. **Actualización Automática**: El dashboard JavaScript carga las estadísticas automáticamente
3. **Modo Desarrollo**: Usa el endpoint JSON (`/stats`) durante desarrollo
4. **Documentación Offline**: Los archivos `.md` están disponibles sin conexión

## 📞 Soporte

Si tienes problemas o necesitas ayuda adicional:
1. Revisa la documentación en `/docs/`
2. Consulta el README.md
3. Revisa los logs del servidor
4. Contacta al equipo de desarrollo

---

**Última actualización**: Febrero 2026
