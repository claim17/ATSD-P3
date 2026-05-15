# Explicación del desarrollador.

## Creación de issues.
 He creado un el Trello Board con las nuevas funcionalidad, con su breve explicación y tal.
  - El enlace para que te puedas meter: [Trello Board - To Do List App](https://trello.com/invite/b/6a057e1780076a27ce1afea6/ATTIe902cf51e3dbaf60a86404b0e850e4adFF2E18FA/e3-atsd-to-do-list-new-app)

También he creado, en Github, 6 issues. Para cada funcionalidad son 2 issues, los he repartido alternandolos segun el 
desarrollo de  cada capa. Las funcionalidades se van a desarrollar en 2 partes, la 
primera parte es el desarrollo del Service y Model, la segunda parte es el desarrollo de los Views y Controllers.


El desarrollo del Service y Model se tienen que desarrollar siguiente el TDD, que consiste en:
- Test: Debes escribir la prueba primero.
- Code: Luego debes escribir el código que pase la prueba (solo el código necesario, no puedes escribir código extra).
- Refactor: Y, si es necesario, realiza una refactorización del código (las pruebas deben seguir pasando después de la refactorización).


Se debe realizar un commit por cada fase Test-Código. Si refactorizas, tendrás que hacerlo en otro commit adicional.



He dado permisos al ./mvn ya que este no los tenia , tambien ya esta puesta la inplementacion de postgresSQL segun indica el documento del profesor , y ya funcionan ambos tests ya que el de integracion llevava dadno fallos 2 dias.

## Desarrollo del issue "Service y Model - Lista de Equipos" - Jhener
He implementado la funcionalidad completa de gestión de equipos siguiendo la metodología TDD. Se han desarrollado las capas Model (entidad Equipo con relación muchos-a-muchos con Usuario), Service (lógica de negocio con validaciones) y Repository, junto con sus correspondientes test de integración.

### Carpetas / Fichero creados.

**Main:**
- src/main/java/todolist/dto/**EquipoData.java**
- src/main/java/todolist/model/**Equipo.java**
- src/main/java/todolist/repository/**EquipoRepository.java**
- src/main/java/todolist/service/**EquipoService.java**
- src/main/java/todolist/service/**EquipoServiceException.java**

**Test:**
- src/test/java/todolist/repository/**EquipoTest.java**
- src/test/java/todolist/service/**EquipoServiceTest.java**

### Métodos Explicados

#### EquipoService

- **crearEquipo(String nombre)**: Crea un nuevo equipo con el nombre proporcionado y lo persiste en la base de datos.
- **recuperarEquipo(Long id)**: Recupera un equipo por su ID. Lanza `EquipoServiceException` si no existe.
- **findAllOrdenadosPorNombre()**: Devuelve la lista de todos los equipos ordenados alfabéticamente por nombre.
- **addUsuarioAEquipo(Long teamId, Long userId)**: Añade un usuario a un equipo. Valida que ambos existan en la BD.
- **usuariosEquipo(Long id)**: Devuelve la lista de usuarios que pertenecen a un equipo específico.
- **equiposUsurio(Long userId)**: Devuelve la lista de equipos a los que pertenece un usuario.

#### Equipo Model

- **addUsuario(Usuario usuario)**: Añade un usuario al equipo actualizando ambas colecciones bidireccionales (equipo-usuario y usuario-equipo).

### Tests

**EquipoServiceTest:**
- crearRecuperarEquipoTest
- listadoEquiposOrdenAlfabeticoTest
- addUsuarioAEquipoTest
- recuperarEquiposDeUsuarioTest
- comprobarExcepcionesTest

**EquipoTest:**
- crearEquipo
- grabarYBuscarEquipo
- comprobarIgualdadEquipos
- comprobarRelacionBaseDatos

## Desarrollo del issue "Vistas, Controladores y Tests - Lista de Equipos"
Se ha completado la funcionalidad de "Lista de Equipos" desarrollando la capa de presentación, el enrutamiento y sus pruebas unitarias.

### Archivos creados
- **Controlador:** `src/main/java/todolist/controller/EquipoController.java`
- **Vistas:** `src/main/resources/templates/equipos.html` y `src/main/resources/templates/equipoDetalle.html`
- **Tests:** `src/test/java/todolist/controller/EquipoControllerTest.java`

### Endpoints Implementados (`EquipoController`)
- **`GET /equipos`**: Muestra el listado de todos los equipos de la empresa ordenados alfabéticamente. Protegido por comprobación de sesión.
- **`GET /equipos/{id}`**: Muestra los detalles de un equipo en concreto junto a la lista de usuarios miembros. Protegido por comprobación de sesión.

### Tests Unitarios (`EquipoControllerTest`)
- **Accesos autorizados:** Verificación de renderizado de las plantillas correctas y carga de datos en el `Model` (`equipos`, `equipo`, `usuarios`) estando logueado.
- **Accesos denegados:** Comprobación de seguridad asegurando la redirección (HTTP 3xx) a `/login` para usuarios sin sesión activa en ambas rutas.

## Desarrollo - Inicialización de Datos de Prueba (Seed)
Se ha creado una clase `DataSeeder` (`CommandLineRunner`) para generar automáticamente 3 equipos ("Equipo Alfa", "Equipo Beta", "Equipo Gamma") en el arranque de la aplicación, con el objetivo de poder visualizar y probar rápidamente el listado de equipos. Además, se añadió el enlace correspondiente en los fragmentos de la barra de navegación.

## Pasos para configurar la Base de Datos.

- Ejecución del Docker (contenedor de una imagen de Postgres):
```
docker run -d -p 5432:5432 --name postgres-develop -e POSTGRES_USER=atsd -e POSTGRES_PASSWORD=atsd -e POSTGRES_DB=atsd postgres:13
```
- Inicio de la aplicación (probar):
  - Si lo haces shell:
  ````
    mvn spring-boot:run '-Dspring-boot.run.profiles=postgres'
  ````
  - Si lo haces con cmd:
  ```
    mvn spring-boot:run "-Dspring-boot.run.profiles=postgres"
  ```
- Configuramos el IntelliJ para lanzarlo desde run.
- Lanzamos los tests en PostgresSQL.
```
  docker run -d -p 5432:5432 --name postgres-test -e POSTGRES_USER=atsd -e POSTGRES_PASSWORD=atsd -e POSTGRES_DB=atsd_test postgres:13
```
y después:
```
  mvn test '-Dspring.profiles.active=postgres'
```