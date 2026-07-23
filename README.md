# FinanceAI — Asistente Inteligente de Salud Financiera 💡📊

API REST del asistente inteligente de salud financiera.  
Parte del MVP desarrollado para el Hackathon Fintech / Educación Financiera en colaboración con [No Country](https://www.nocountry.tech/).

>Transforma datos financieros brutos en conocimiento claro, visual y accionable.


## 📂 Estructura del Proyecto

```text
G9-LATAM-Team-56
├── .gitignore              # Reglas globales de exclusión para Git
├── README.md               # Documentación del proyecto
├── backend/
│   └── api/                # Proyecto Spring Boot 3.5.1 (API REST)
│       ├── pom.xml
│       └── src/main/java/com/finance_ia/api/
│           ├── config/     # Configuraciones
│           ├── controller/ # Controladores REST
│           ├── dto/        # Objetos de Transferencia de Datos
│           ├── infra/      # Manejo de Excepciones y Seguridad
│           ├── model/      # Entidades JPA
│           ├── repository/ # Repositorios Spring Data JPA
│           └── service/    # Lógica de negocio
├── docs/                   # Documentación técnica y reportes
├── oci-infrastructure/     # Configuración y scripts para despliegue en OCI
└── science/                # Notebooks de Python, Análisis Exploratorio (EDA) y Modelos ML
```
<br>

## 🛠️ Requisitos Previos

Antes de ejecutar el proyecto, asegúrate de tener instalado en tu computadora:

1. **IntelliJ IDEA** (Community o Ultimate Edition).
2. **Java JDK 21** (configurado en IntelliJ).
3. **MySQL Server 8.0+** corriendo en tu máquina (vía MySQL Workbench, XAMPP o servicio local).
4. **Git** integrado en IntelliJ o en tu terminal.
<br>

## 🚀 Pasos para Clonar y Ejecutar 

### 1: Clonar el Repositorio

Abre tu terminal y ejecuta:

```Bash
git clone https://github.com/No-Country-simulation/G9-LATAM-Team-56.git
cd G9-LATAM-Team-56
```


### 2. Configurar la Base de Datos MySQL

Dado que el proyecto utiliza Spring Data JPA, requiere una base de datos activa antes de iniciar.

1. Inicia tu servidor MySQL local (XAMPP, MySQL Workbench o desde la terminal).
2. Crea la base de datos necesaria:    

```SQL
CREATE DATABASE finance_ia;
```


### 3. Configurar Credenciales (`application.properties`)

El archivo de propiedades ubicado en `backend/api/src/main/resources/application.properties` está configurado provisionalmente de la siguiente manera:

```Properties
spring.datasource.url=jdbc:mysql://localhost/finance_ia
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

Asegúrate de definir las variables de entorno en tu sistema/IDE, o reemplazar `${DB_USER}` y `${DB_PASSWORD}` por tus credenciales locales de MySQL (ej. `root` y tu contraseña).



### 4. Compilar y Ejecutar la API

Navega a la carpeta del proyecto Spring Boot y ejecuta la aplicación:

1. Abre el archivo `ApiApplication.java`.
2. Haz clic en el botón **Play verde (▶️)** al lado del método `main` o en la barra superior de IntelliJ.
3. Espera a que cargue Maven y Spring Boot. Cuando veas en la consola el mensaje `Started ApiApplication in X seconds`, la API estará corriendo en:

```text
http://localhost:8080/hello
```
   
Si el backend está levantado y conectado a la base de datos, verás el mensaje:
  ```text
¡Hola! La API de FinanceAI está en ejecución y lista para procesar transacciones. 🚀
  ```

> 📖 Para instrucciones detalladas de instalación, consulta la [Guía de Instalación](https://github.com/No-Country-simulation/G9-LATAM-Team-56/wiki/Gu%C3%ADa-de-Instalaci%C3%B3n) en nuestra Wiki.

<br>

## 📚 Documentación

|Recurso|Enlace|
|:--|:--|
|📖 Wiki del Proyecto|[Ver Wiki](https://github.com/No-Country-simulation/G9-LATAM-Team-56/wiki)|
|🛠️ Guía de Instalación Detallada|[Guía de Instalación](https://github.com/No-Country-simulation/G9-LATAM-Team-56/wiki/Gu%C3%ADa-de-Instalaci%C3%B3n)|
|🔌 API y Endpoints|[API y Endpoints](https://github.com/No-Country-simulation/G9-LATAM-Team-56/wiki/API-y-Endpoints)|
|📊 Data Science|[DataSet Inicial](https://github.com/No-Country-simulation/G9-LATAM-Team-56/wiki/DataSet-Inicial)|
<br>

## 📄 Licencia

Proyecto desarrollado en el marco del Hackathon de Educación Financiera / Fintech — No Country Simulation.

