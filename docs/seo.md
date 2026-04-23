# SEO en SoleXclusive

Resumen de todas las técnicas de optimización para motores de búsqueda implementadas en la aplicación.

---

## 1. Metaetiquetas HTML

### Páginas públicas indexables

#### Home (`/home`)
| Etiqueta | Valor |
|---|---|
| `<title>` | `SoleXclusive \| Comprar Zapatillas Online - Nike, Adidas, Puma` |
| `<meta name="description">` | Descripción con palabras clave de intención de compra (envío, devolución, marcas) |
| `<meta name="keywords">` | 12 keywords: _comprar zapatillas online_, _sneakers baratos_, _zapatillas Nike_, _zapatillas Adidas_, etc. |
| `<meta name="robots">` | `index, follow` |

#### Ficha de producto (`/home/sneakers/{id}`)
Las metaetiquetas se generan **dinámicamente** con Thymeleaf a partir de los datos del producto:

| Etiqueta | Generación dinámica |
|---|---|
| `<title>` | `{nombre} \| Comprar Online - SoleXclusive` |
| `<meta name="description">` | Incluye nombre, marca, descripción del producto y claim de envío |
| `<meta name="keywords">` | Combina nombre del producto, marca y tipo de sneaker |
| `<meta name="robots">` | `index, follow` |

### Páginas privadas bloqueadas

Todas las páginas de uso interno llevan `<meta name="robots" content="noindex, nofollow">` para evitar que los buscadores las indexen:

| Página | Template |
|---|---|
| Login | `Users/login.html` |
| Crear cuenta | `create_account.html` |
| Carrito | `cart.html` |
| Mi perfil | `profile.html` |
| Historial de compras | `purchase_history.html` |
| Panel admin — pedidos | `admin_orders.html` |

---

## 2. Atributos `alt` en imágenes

Los atributos `alt` son obligatorios para la accesibilidad y relevantes para el SEO de imágenes.

| Imagen | Alt |
|---|---|
| Logo navbar | `SoleXclusive Logo` |
| Carrusel home — diapositiva 1 | `Nike Air Force 1` |
| Carrusel home — diapositiva 2 | `Adidas Samba` |
| Carrusel home — diapositiva 3 | `Puma Lamelo Ball` |
| Tarjetas de producto en home | Dinámico: `${sneaker.name}` |
| Imagen principal en ficha de producto | Dinámico: `${sneaker.name}` |
| Productos relacionados | Dinámico: `${s.name}` |

---

## 3. Jerarquía de encabezados (`<h1>` / `<h2>`)

Se respeta una jerarquía semántica en cada página para que los rastreadores entiendan la estructura del contenido.

| Página | `<h1>` | `<h2>` |
|---|---|---|
| Home | Texto con keywords SEO (catálogo de zapatillas) | — |
| Ficha de producto | Nombre del producto (dinámico) | Precio del producto |
| Ficha de producto | — | `Productos Relacionados` |
| Carrito | `Carrito de Compras` | `Total a Pagar` |
| Perfil | `Mi Perfil` | — |
| Historial | `Historial de Compras` | — |
| Admin — pedidos | `Listado de Pedidos` | — |
| Admin — panel | `Panel de Administración` | — |

---

## 4. Atributo `lang` en `<html>`

Todas las páginas públicas y privadas tienen `lang="es"` en la etiqueta `<html>`, lo que indica a Google el idioma del contenido y evita problemas de localización.

---

## 5. `robots.txt`

Archivo ubicado en `src/main/resources/static/robots.txt`. Spring Boot lo sirve automáticamente en la raíz del dominio (`/robots.txt`).

**Rutas permitidas (`Allow`):**
- `/home` — tienda principal
- `/home/sneakers/` — fichas de producto
- `/login`
- `/home/create-account`

**Rutas bloqueadas (`Disallow`):**
- `/admin/` — panel de administración
- `/brands/`, `/sneakers/`, `/stocks/`, `/users/` — gestión interna
- `/home/cart`, `/home/profile`, `/home/purchase-history`, `/home/order-detail/` — área privada de usuario
- `/profile/` — cambio de contraseña y edición de perfil
- `/images/` — rutas internas de imágenes

**Directiva Sitemap:**
```
Sitemap: https://www.solexclusive.com/sitemap.xml
```
> Actualizar con el dominio real antes del despliegue en producción.

---

## 6. Semántica HTML5

Se utilizan etiquetas semánticas en lugar de `<div>` genéricos para mejorar la comprensión del contenido por parte de los rastreadores:

`<header>`, `<nav>`, `<main>`, `<section>`, `<article>`, `<footer>`

---

## 7. Tipografía: Verdana

La fuente principal es **Verdana** (sistema, sin carga externa), elegida por su alta legibilidad en pantalla. Una buena experiencia de lectura reduce el porcentaje de rebote, que es una señal indirecta de calidad para Google.

---

## Pendiente / Mejoras recomendadas

| Mejora | Prioridad | Descripción |
|---|---|---|
| **`sitemap.xml`** | Alta | Generar un sitemap con `/home` y todas las fichas de producto (`/home/sneakers/{id}`) para facilitar el rastreo |
| **Open Graph** | Media | Añadir `<meta property="og:*">` para controlar cómo se muestran los enlaces en redes sociales |
| **Twitter Cards** | Media | Añadir `<meta name="twitter:*">` para previsualización en X (Twitter) |
| **Datos estructurados** | Alta | Implementar JSON-LD con esquema `Product` en las fichas de producto (precio, disponibilidad, marca) para los _rich results_ de Google |
| **URL canónica** | Media | Añadir `<link rel="canonical">` en home y fichas para evitar contenido duplicado si hay parámetros de filtro en la URL |
| **`noindex` en admin_profile**| Baja | Añadir `<meta name="robots" content="noindex, nofollow">` al template `admin_profile.html` |