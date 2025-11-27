Guía de Despliegue de Microservicios

Este documento detalla los pasos necesarios para compilar, configurar y desplegar la arquitectura de microservicios del proyecto, incluyendo la infraestructura de base de datos y caché.

📋 Requisitos Previos

Asegúrese de contar con las siguientes herramientas instaladas antes de comenzar:

Java JDK 21

Maven

Docker y Docker Compose

Postman (para pruebas)

🚀 1. Compilación de Artefactos (.jar)

El primer paso consiste en generar los ejecutables para los servicios mscliente y mscuenta. Desde la raíz del proyecto, ejecute la siguiente secuencia de comandos:

Servicio Cliente

cd mscliente
mvn clean package


Servicio Cuenta

cd ../mscuenta
mvn clean package


Una vez finalizada la compilación, regrese al directorio raíz:

cd ..


🐳 2. Despliegue de Infraestructura (Docker)

La infraestructura base consiste en contenedores para PostgreSQL y Redis.

Navegue al directorio de configuración de Docker:

cd docker


Inicie los servicios de infraestructura externa:

docker-compose -f docker-compose-external.yml up -d


Nota: Espere unos segundos hasta que los contenedores de PostgreSQL y Redis estén completamente iniciados y listos para recibir conexiones antes de proceder al siguiente paso.

🗄️ 3. Inicialización de Base de Datos

Una vez que el contenedor de base de datos esté operativo, es necesario ejecutar el script de creación de tablas. Ejecute el siguiente comando para inyectar el esquema SQL:

docker exec -it postgres-container psql -U postgres -f ../BaseDatos.sql


⚙️ 4. Despliegue de Microservicios

Con la infraestructura lista y la base de datos inicializada, proceda a levantar los servicios de negocio:

Ejecute el archivo de composición técnica:

docker-compose -f docker-compose-technical.yml up -d


Verifique que todos los contenedores se estén ejecutando correctamente:

docker ps


🌐 5. Accesos y Puertos

Los microservicios estarán disponibles en los siguientes puertos locales:

Servicio

Puerto

URL Base

mscliente

8081

http://localhost:8081

mscuenta

8082

http://localhost:8082

🧪 6. Pruebas

Para validar la funcionalidad del sistema:

Localice la carpeta postman en la raíz del proyecto.

Importe la colección adjunta en su cliente de Postman.

Ejecute las peticiones configuradas para probar los endpoints de los servicios desplegados.