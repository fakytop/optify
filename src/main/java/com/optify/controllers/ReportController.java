package com.optify.controllers;

import com.optify.dto.SystemStatsDto;
import com.optify.repository.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
@Tag(name = "Reports", description = "Endpoints para visualizar reportes y estadísticas del sistema")
public class ReportController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private StoreProductRepository storeProductRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ManualMatchRepository manualMatchRepository;

    @Autowired
    private DiscardReferenceRepository discardReferenceRepository;

    @Operation(summary = "Obtener estadísticas del sistema",
               description = "Retorna métricas generales del sistema incluyendo totales de productos, tiendas, usuarios, etc.")
    @GetMapping("/stats")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<SystemStatsDto> getSystemStats() {
        SystemStatsDto stats = new SystemStatsDto();
        
        stats.setTotalProducts(productRepository.count());
        stats.setTotalStores(storeRepository.count());
        stats.setTotalUsers(userRepository.count());
        stats.setTotalCategories(categoryRepository.count());
        stats.setTotalStoreProducts(storeProductRepository.count());
        stats.setTotalCartItems(cartItemRepository.count());
        stats.setTotalPendingMatches(manualMatchRepository.count());
        stats.setTotalDiscardedReferences(discardReferenceRepository.count());

        return ResponseEntity.ok(stats);
    }

    @Operation(summary = "Ver dashboard de reportes",
               description = "Retorna una página HTML con visualización de diagramas y reportes del sistema")
    @GetMapping("/view")
    public ResponseEntity<String> viewDashboard() {
        String html = """
                <!DOCTYPE html>
                <html lang="es">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Optify - Dashboard de Reportes</title>
                    <style>
                        * {
                            margin: 0;
                            padding: 0;
                            box-sizing: border-box;
                        }
                        
                        body {
                            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                            min-height: 100vh;
                            padding: 20px;
                        }
                        
                        .container {
                            max-width: 1400px;
                            margin: 0 auto;
                        }
                        
                        header {
                            background: white;
                            padding: 30px;
                            border-radius: 15px;
                            box-shadow: 0 10px 30px rgba(0,0,0,0.2);
                            margin-bottom: 30px;
                            text-align: center;
                        }
                        
                        h1 {
                            color: #667eea;
                            font-size: 2.5em;
                            margin-bottom: 10px;
                        }
                        
                        .subtitle {
                            color: #666;
                            font-size: 1.1em;
                        }
                        
                        .stats-grid {
                            display: grid;
                            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
                            gap: 20px;
                            margin-bottom: 30px;
                        }
                        
                        .stat-card {
                            background: white;
                            padding: 25px;
                            border-radius: 10px;
                            box-shadow: 0 5px 15px rgba(0,0,0,0.1);
                            transition: transform 0.3s, box-shadow 0.3s;
                        }
                        
                        .stat-card:hover {
                            transform: translateY(-5px);
                            box-shadow: 0 10px 25px rgba(0,0,0,0.2);
                        }
                        
                        .stat-icon {
                            font-size: 2.5em;
                            margin-bottom: 10px;
                        }
                        
                        .stat-number {
                            font-size: 2.5em;
                            font-weight: bold;
                            color: #667eea;
                            margin: 10px 0;
                        }
                        
                        .stat-label {
                            color: #666;
                            font-size: 1em;
                        }
                        
                        .section {
                            background: white;
                            padding: 30px;
                            border-radius: 15px;
                            box-shadow: 0 10px 30px rgba(0,0,0,0.2);
                            margin-bottom: 30px;
                        }
                        
                        h2 {
                            color: #667eea;
                            margin-bottom: 20px;
                            font-size: 1.8em;
                        }
                        
                        .diagram-container {
                            background: #f8f9fa;
                            padding: 30px;
                            border-radius: 10px;
                            margin: 20px 0;
                            overflow-x: auto;
                        }
                        
                        pre {
                            white-space: pre;
                            font-family: 'Courier New', monospace;
                            font-size: 0.9em;
                            line-height: 1.6;
                            color: #333;
                        }
                        
                        .link-section {
                            display: grid;
                            grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
                            gap: 20px;
                            margin-top: 20px;
                        }
                        
                        .link-card {
                            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                            color: white;
                            padding: 25px;
                            border-radius: 10px;
                            text-decoration: none;
                            display: block;
                            transition: transform 0.3s;
                        }
                        
                        .link-card:hover {
                            transform: scale(1.05);
                        }
                        
                        .link-card h3 {
                            margin-bottom: 10px;
                            font-size: 1.4em;
                        }
                        
                        .link-card p {
                            opacity: 0.9;
                        }
                        
                        .loading {
                            text-align: center;
                            padding: 20px;
                            color: #666;
                        }
                        
                        .error {
                            background: #fee;
                            color: #c33;
                            padding: 15px;
                            border-radius: 5px;
                            margin: 10px 0;
                        }
                        
                        footer {
                            text-align: center;
                            color: white;
                            margin-top: 40px;
                            padding: 20px;
                        }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <header>
                            <h1>📊 Optify Dashboard</h1>
                            <p class="subtitle">Sistema de Gestión de Comparación de Precios</p>
                        </header>
                        
                        <div id="stats-section" class="stats-grid">
                            <div class="loading">Cargando estadísticas...</div>
                        </div>
                        
                        <div class="section">
                            <h2>🏗️ Arquitectura del Sistema</h2>
                            <div class="diagram-container">
                                <pre>
┌─────────────────────────────────────────────────────────────┐
│                  CAPA DE PRESENTACIÓN                        │
│  (API REST - Controllers)                                    │
│  ├─ ProductController    - Gestión de productos             │
│  ├─ StoreController      - Gestión de tiendas              │
│  ├─ CartController       - Gestión de carritos             │
│  ├─ UserController       - Autenticación y usuarios        │
│  ├─ ManualMatchController - Matching manual                │
│  └─ ReportController     - Reportes y estadísticas         │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                     CAPA DE NEGOCIO                          │
│  (Services & Facade)                                         │
│  ├─ ProductService       - Lógica de productos             │
│  ├─ StoreService         - Lógica de tiendas               │
│  ├─ CartService          - Lógica de carritos              │
│  ├─ MatchManagerService  - Matching automático             │
│  └─ CategoryService      - Gestión de categorías           │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                     CAPA DE DATOS                            │
│  (JPA Repositories)                                          │
│  ├─ ProductRepository                                       │
│  ├─ StoreRepository                                         │
│  ├─ CartRepository                                          │
│  └─ UserRepository                                          │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
                    ┌──────────────────┐
                    │   PostgreSQL DB  │
                    └──────────────────┘
                                </pre>
                            </div>
                        </div>
                        
                        <div class="section">
                            <h2>📈 Modelo de Datos (ERD)</h2>
                            <div class="diagram-container">
                                <pre>
┌──────────────┐         ┌──────────────┐         ┌──────────────┐
│   Product    │─────────│ StoreProduct │─────────│    Store     │
├──────────────┤    1..* ├──────────────┤ *..1    ├──────────────┤
│ id           │◄────────│ product_id   │────────►│ rut          │
│ name         │         │ store_rut    │         │ name         │
│ description  │         │ price        │         │ address      │
│ brand        │         │ stock        │         │ city_id      │
│ imageUrl     │         │ id_web       │         │ coordinate   │
│ category_id  │         └──────────────┘         └──────────────┘
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

┌──────────────┐         ┌──────────────┐
│     User     │─────────│   CartItem   │
├──────────────┤    1..* ├──────────────┤
│ id           │◄────────│ user_id      │
│ username     │         │ product_id   │
│ mail         │         │ store_rut    │
│ password     │         │ quantity     │
└──────────────┘         └──────────────┘
                                </pre>
                            </div>
                        </div>
                        
                        <div class="section">
                            <h2>🔗 Enlaces Útiles</h2>
                            <div class="link-section">
                                <a href="/swagger-ui.html" class="link-card">
                                    <h3>📚 API Swagger</h3>
                                    <p>Documentación interactiva de la API REST</p>
                                </a>
                                <a href="https://github.com/fakytop/optify" class="link-card">
                                    <h3>💻 Repositorio GitHub</h3>
                                    <p>Código fuente del proyecto</p>
                                </a>
                                <a href="/api/reports/stats" class="link-card">
                                    <h3>📊 API de Estadísticas</h3>
                                    <p>Consulta las estadísticas en formato JSON</p>
                                </a>
                            </div>
                        </div>
                        
                        <footer>
                            <p>&copy; 2026 Optify - Sistema de Comparación de Precios</p>
                        </footer>
                    </div>
                    
                    <script>
                        // Función para cargar estadísticas
                        async function loadStats() {
                            try {
                                const response = await fetch('/api/reports/stats', {
                                    headers: {
                                        'Authorization': 'Bearer ' + localStorage.getItem('token')
                                    }
                                });
                                
                                if (!response.ok) {
                                    throw new Error('No se pudieron cargar las estadísticas. Por favor, inicia sesión.');
                                }
                                
                                const stats = await response.json();
                                
                                const statsSection = document.getElementById('stats-section');
                                statsSection.innerHTML = `
                                    <div class="stat-card">
                                        <div class="stat-icon">📦</div>
                                        <div class="stat-number">${stats.totalProducts}</div>
                                        <div class="stat-label">Productos</div>
                                    </div>
                                    <div class="stat-card">
                                        <div class="stat-icon">🏪</div>
                                        <div class="stat-number">${stats.totalStores}</div>
                                        <div class="stat-label">Tiendas</div>
                                    </div>
                                    <div class="stat-card">
                                        <div class="stat-icon">👥</div>
                                        <div class="stat-number">${stats.totalUsers}</div>
                                        <div class="stat-label">Usuarios</div>
                                    </div>
                                    <div class="stat-card">
                                        <div class="stat-icon">📂</div>
                                        <div class="stat-number">${stats.totalCategories}</div>
                                        <div class="stat-label">Categorías</div>
                                    </div>
                                    <div class="stat-card">
                                        <div class="stat-icon">🏷️</div>
                                        <div class="stat-number">${stats.totalStoreProducts}</div>
                                        <div class="stat-label">Productos en Tiendas</div>
                                    </div>
                                    <div class="stat-card">
                                        <div class="stat-icon">🛒</div>
                                        <div class="stat-number">${stats.totalCartItems}</div>
                                        <div class="stat-label">Items en Carritos</div>
                                    </div>
                                    <div class="stat-card">
                                        <div class="stat-icon">⏳</div>
                                        <div class="stat-number">${stats.totalPendingMatches}</div>
                                        <div class="stat-label">Matches Pendientes</div>
                                    </div>
                                    <div class="stat-card">
                                        <div class="stat-icon">❌</div>
                                        <div class="stat-number">${stats.totalDiscardedReferences}</div>
                                        <div class="stat-label">Referencias Descartadas</div>
                                    </div>
                                `;
                            } catch (error) {
                                const statsSection = document.getElementById('stats-section');
                                statsSection.innerHTML = `
                                    <div class="error" style="grid-column: 1 / -1;">
                                        ${error.message}
                                        <br><br>
                                        <strong>Nota:</strong> Para ver las estadísticas necesitas estar autenticado.
                                        Las estadísticas se cargarán automáticamente si ya tienes un token JWT válido en tu navegador.
                                    </div>
                                `;
                            }
                        }
                        
                        // Cargar estadísticas al cargar la página
                        loadStats();
                    </script>
                </body>
                </html>
                """;
        
        return ResponseEntity.ok()
                .header("Content-Type", "text/html; charset=UTF-8")
                .body(html);
    }

    @Operation(summary = "Obtener información del dashboard",
               description = "Retorna información general del dashboard sin requerir autenticación")
    @GetMapping("/dashboard")
    public ResponseEntity<String> getDashboardInfo() {
        return ResponseEntity.ok("""
                {
                    "message": "Dashboard de Reportes de Optify",
                    "description": "Sistema de visualización de diagramas e informes del proyecto",
                    "endpoints": {
                        "stats": "/api/reports/stats - Estadísticas del sistema (requiere autenticación)",
                        "view": "/api/reports/view - Visualización HTML del dashboard",
                        "dashboard": "/api/reports/dashboard - Información general del dashboard"
                    },
                    "documentation": {
                        "readme": "Ver README.md en la raíz del proyecto",
                        "architecture": "Ver docs/ARCHITECTURE.md para detalles de arquitectura"
                    }
                }
                """);
    }
}
