Check list for correct starting the app:
1. Ensure that PostgreSQL is installed
2. Execute init_script.sql into your database
3. Create file application.properties in the project root and fill it with properties from the file
   application.properties.original

Or just use docker-compose.yml for start db and the application

View endpoints and endpoints description by swagger: http://{host:port}/swagger-ui/index.html, 
for example http://localhost:8080/swagger-ui/index.html