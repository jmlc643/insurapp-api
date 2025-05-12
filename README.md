# InsurApp API

**Backend del aplicativo móvil para la reserva de canchas deportivas**

## 🏟️ Descripción del Proyecto

**InsurApp** es una solución móvil diseñada para facilitar la reserva de canchas deportivas en distintas disciplinas como fútbol, vóley y básquet. Este repositorio contiene la **API REST** que provee los servicios necesarios para el funcionamiento de la aplicación, incluyendo el listado de canchas, gestión de reservas, y procesamiento de pagos.

La API ha sido desarrollada con el objetivo de ofrecer una experiencia fluida, rápida y segura para los usuarios que desean encontrar, seleccionar y reservar espacios deportivos en su localidad.

## 📱 Características Principales

- **Listado de Canchas Deportivas**:  
  Consulta y visualización de canchas disponibles según el tipo de deporte:
  - Fútbol
  - Vóley
  - Básquet

- **Filtrado por Tipo de Cancha**:  
  Los usuarios pueden explorar las canchas según su categoría deportiva, con información detallada de ubicación, horarios disponibles y características del lugar.

- **Selección de Fecha y Hora**:  
  Interfaz para seleccionar el día y la hora deseada para la reserva, validando la disponibilidad en tiempo real.

- **Reserva y Confirmación**:  
  Flujo completo para realizar reservas seguras, almacenando la información del evento para su posterior consulta.

- **Pasarela de Pago Integrada**:  
  Integración con una plataforma de pagos que permite a los usuarios completar el proceso de reserva abonando directamente desde la app.

## 🚀 Cómo Iniciar el Proyecto

```bash
# Clonar el repositorio
git clone https://github.com/jmlc643/insurapp-api.git
cd insurapp-api

# Instalar dependencias
npm install

# Crear archivo .env con las variables necesarias
cp .env.example .env

# Iniciar el servidor en modo desarrollo
npm run dev
```
## 📂 Estructura del Proyecto

```bash
src/
│
├── config/       # Configuraciones generales de la aplicación
├── controllers/  # Lógica que maneja las peticiones HTTP
├── dto/          # Se usan para definir la forma esperada de los datos en entradas y salidas.
├── exceptions/   # Manejo de errores personalizados y clases de excepción
├── models/       # Definiciones de los esquemas y modelos de la base de datos
├── repos/        # Repositorios para la abstracción de acceso a datos
├── services/     # Contiene la lógica de negocio
└── utils/        # Funciones utilitarias y helpers reutilizables
```
