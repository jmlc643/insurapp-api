# README: Guía de Diseño de APIs REST para Desarrolladores Frontend

Este documento tiene como objetivo proporcionar una guía clara y práctica para que los desarrolladores frontend comprendan cómo interactuar con APIs RESTful. A continuación, se detallan las mejores prácticas y conceptos clave que debes conocer al trabajar con endpoints REST.

---

## **1. Nomenclatura de Endpoints**

### **Recomendaciones Generales**
1. **Usa sustantivos en plural**: Los recursos deben nombrarse en plural para representar colecciones. Ejemplo:
    - `/users` → Todos los usuarios
    - `/events` → Todos los eventos

2. **Evita verbos en los nombres de recursos**: Los verbos se manejan a través de los métodos HTTP. Ejemplo:
    - Incorrecto: `/getUsers`
    - Correcto: `/users` (usando `GET`)

3. **Versionado**: Incluye la versión de la API en el endpoint para facilitar futuras actualizaciones:
    - `/v1/users`
    - `/v2/events`

---

## **2. Métodos HTTP y sus Usos**

| **Método HTTP** | **Acción**                     | **Ejemplo de Endpoint**       |
|------------------|--------------------------------|-------------------------------|
| `GET`           | Obtener un recurso o lista     | `GET /users`                  |
| `POST`          | Crear un nuevo recurso         | `POST /users`                 |
| `PUT`           | Actualizar un recurso completo | `PUT /users/{id}`             |
| `PATCH`         | Actualizar parcialmente        | `PATCH /users/{id}`           |
| `DELETE`        | Eliminar un recurso            | `DELETE /users/{id}`          |

---

## **3. Estructura de Endpoints**

### **Recursos Principales**
- Representan colecciones de datos:
    - `/users` → Todos los usuarios
    - `/events` → Todos los eventos
    - `/categories` → Todas las categorías

### **Recursos Específicos**
- Accede a un recurso específico usando su ID:
    - `/users/{id}` → Usuario específico
    - `/events/{id}` → Evento específico

### **Relaciones entre Recursos**
- Si un recurso está relacionado con otro, puedes anidarlos lógicamente:
    - `/users/{id}/orders` → Órdenes de un usuario
    - `/events/{id}/attendees` → Asistentes de un evento

### **Acciones Personalizadas**
- Para acciones que no encajan en los métodos HTTP estándar, usa sufijos descriptivos:
    - `/users/{id}/activate` → Activar un usuario
    - `/events/{id}/publish` → Publicar un evento

---

## **4. Filtros, Búsqueda y Paginación**

Para filtrar, buscar o paginar datos, usa **query parameters** en lugar de modificar el nombre del endpoint:

- Filtrar por categoría:
  ```
  GET /events?category=concert
  ```

- Paginar resultados:
  ```
  GET /users?page=2&limit=10
  ```

---

## **5. Categorías de Recursos**

### **Collection**
- Representa una colección de recursos.
- Métodos comunes: `GET` (listar), `POST` (crear).
    - Ejemplo: `GET /films` → Obtener todas las películas.

### **Instance/Document**
- Representa una instancia específica de un recurso.
- Métodos comunes: `GET` (obtener), `PUT` (actualizar), `DELETE` (eliminar).
    - Ejemplo: `GET /films/{id}` → Obtener una película específica.

### **Controller**
- Representa acciones específicas que no necesariamente modifican un recurso.
- Usa siempre el método `POST`.
    - Ejemplo: `POST /loans/simulate` → Simular un préstamo.

---

## **6. Granularidad de los Servicios**

La granularidad define cuánto cubre un endpoint:
- **Grano grueso**: Un solo endpoint maneja múltiples casos.
    - Ejemplo: `POST /films` → Crear cualquier tipo de película.

- **Grano fino**: Múltiples endpoints específicos.
    - Ejemplo:
        - `POST /sci-fi-films` → Crear una película de ciencia ficción.
        - `POST /action-films` → Crear una película de acción.

Elige la granularidad según tus necesidades.

---

## **7. Anti-patrones Comunes**

Evita estos errores al trabajar con APIs REST:

1. **Usar verbos en los nombres de recursos**:
    - Incorrecto: `GET /updateCustomer`
    - Correcto: `PUT /customers/{id}`

2. **Usar query params para acciones**:
    - Incorrecto: `GET /services?op=update_customer`
    - Correcto: `PUT /customers/{id}`

3. **Usar nombres en singular**:
    - Incorrecto: `/customer`
    - Correcto: `/customers`

4. **Incluir extensiones de archivo**:
    - Incorrecto: `/car-invoice.pdf`
    - Correcto: `/car-invoice`

---

## **8. Recomendaciones Finales**

- **Nombres en minúsculas**: Usa guiones medios (`-`) para separar palabras. Ejemplo: `/car-invoice`.
- **Independencia del formato**: No incluyas extensiones como `.json` o `.pdf`.
- **Explora APIs públicas**: Revisa APIs bien diseñadas como la [API de Marvel](https://developer.marvel.com/) para inspirarte.

---

## **9. Conclusión**

Diseñar y consumir APIs RESTful requiere seguir convenciones claras y consistentes. Al seguir estas mejores prácticas, podrás interactuar eficientemente con APIs backend y construir aplicaciones frontend robustas y escalables.