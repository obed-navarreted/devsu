1. Ubicarse en la raiz de este directorio.
   2. Para construir los .jar de los dos servicios, moverse a cada uno de los directorios de los servicios y ejecutar el siguiente comando:

      ```bash
      cd mscliente
      ```
   
       ```bash
      mvn clean package
      ```
   
       ```bash
       cd ../mscuenta
         ```
    
         ```bash
        mvn clean package
         ```
      
    3. Moverse nuevamente a la raiz del directorio.
   
       ```bash
       cd ..
       ```
       
4. Moverse a la carpeta docker
   
   ```bash
   cd docker
   ```
   
5. Ejecutar el primer docker compose:

docker-compose -f docker-compose-external.yml up -d

Ese docker compose levantara un contenedor postgresql y uno de redis

6. Esperar unos segundos a que los contenedores de postgres y redis esten completamente levantados.
7. Ejecutar el Script de creacion de tablas en la base de datos postgres. Para esto, conectarse al contenedor de postgres y ejecutar el script:

   ```bash
   docker exec -it postgres-container psql -U postgres -f ../BaseDatos.sql
   ```
   
8. Ejecutar el segundo docker compose para levantar los microservicios:
   
   ```bash
   docker-compose -f docker-compose-technical.yml up -d
   ```
   
9. Verificar que los contenedores esten corriendo correctamente con el siguiente comando:
   
   ```bash
   docker ps
   ```
   
10. Los microservicios deberian estar corriendo en los siguientes puertos:
    - mscliente: http://localhost:8081
    - mscuenta: http://localhost:8082

11. Hacer las pruebas respectivas con la coleccion postman adjunta en el directorio postman en la raiz del proyecto.