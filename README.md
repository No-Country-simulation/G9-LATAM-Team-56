# FinanceAI — Asistente Inteligente de Salud Financiera 💡📊

MVP desarrollado para el análisis inteligente de hábitos financieros, categorización automática de gastos y evaluación del perfil financiero del usuario mediante Inteligencia Artificial y la nube de OCI.


**FinanceAI** es una solución webapp orientada al sector Fintech y de educación financiera[cite: 1]. Permite a los usuarios comprender mejor sus hábitos de consumo a partir del análisis de sus transacciones, transformando datos brutos en conocimiento claro, visual y accionable.
<br>

### Funcionalidades Clave (MVP):
- 🏷️ **Clasificación automática de transacciones:** Categorización inteligente en sectores como Alimentación, Transporte, Salud, Vivienda, Educación, Ocio y Servicios.
- 📈 **Análisis de Perfil Financiero:** Clasificación del nivel de salud financiera del usuario (*Saludable*, *En observación*, *En riesgo*).
- 💡 **Recomendaciones Personalizadas:** Sugerencias automáticas y objetivas para reducir gastos desmedidos e incentivar el ahorro.
- 🔌 **API REST:** Respuestas en formato JSON preparadas para consumo frontend e integración con modelos de Ciencia de Datos.
- ☁️ **Integración OCI:** Infraestructura respaldada por servicios de Oracle Cloud Infrastructure.
<br>

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

## 🛠️ Requisitos Previos

Antes de ejecutar el proyecto, asegúrate de tener instalado en tu computadora:

1. **IntelliJ IDEA** (Community o Ultimate Edition).
2. **Java JDK 21** (configurado en IntelliJ).
3. **MySQL Server 8.0+** corriendo en tu máquina (vía MySQL Workbench, XAMPP o servicio local).
4. **Git** integrado en IntelliJ o en tu terminal.

## 🚀 Pasos para Clonar y Ejecutar 

### 1: Clonar el Repositorio

Abre tu terminal y ejecuta:

```Bash
git clone https://github.com/No-Country-simulation/G9-LATAM-Team-56.git
cd G9-LATAM-Team-56
```

### 1.1 Clonar el repositorio en IntelliJ IDEA

1. Abre **IntelliJ IDEA**.
2. En la pantalla de bienvenida, selecciona **Git / Get from Version Control...** (o ve al menú `File` > `New` > `Project from Version Control...`).
3. Pega la URL del repositorio de GitHub: `[https://github.com/TuUsuario/G9-LATAM-Team-56.git](https://github.com/TuUsuario/G9-LATAM-Team-56.git)`
4. Haz clic en **Clone**.

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

```    Plaintext
http://localhost:8080
```


## 📄 Licencia

Proyecto desarrollado en el marco del Hackathon de Educación Financiera / Fintech.

