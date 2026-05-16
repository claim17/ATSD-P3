# 🧪 Test-Driven Development (TDD) en To-Do List App

Este documento detalla la metodología y las prácticas de **Desarrollo Guiado por Pruebas (TDD)** que el equipo ha adoptado para garantizar la máxima calidad y fiabilidad del código durante el desarrollo de esta aplicación.

---

## 🔄 El Ciclo TDD (Red - Green - Refactor)

Todo el desarrollo de la lógica de negocio (Models y Services) se ha regido estrictamente por el ciclo de vida de TDD:

1. **🔴 Test (Red):** Se escribe primero una prueba automatizada para una funcionalidad que aún no existe. Al ejecutarla, la prueba **falla** (Red).
2. **🟢 Code (Green):** Se escribe **únicamente** el código mínimo y estrictamente necesario de producción para lograr que la prueba pase con éxito (Green).
3. **🔵 Refactor (Refactorización):** Una vez que la prueba pasa, se limpia y optimiza el código sin alterar su comportamiento, asegurando que las pruebas sigan pasando en verde.

> 📌 **Política de Commits:** En nuestro flujo de trabajo, se realiza un *commit* obligatoriamente al finalizar cada fase Test-Código. Si se aplica una refactorización (Fase 3), esta se registra en un *commit* adicional.

---

## ⚙️ Entorno y Herramientas de Pruebas

Para nuestras pruebas hemos utilizado el ecosistema de **Spring Boot Test** junto con **JUnit 5**, **Mockito** y **AssertJ** para aserciones fluidas.

### Configuración de Base de Datos para Tests
A diferencia de usar bases de datos en memoria simplificadas de forma permanente, la aplicación ejecuta pruebas de integración reales conectadas a una instancia **PostgreSQL** para mayor fidelidad con producción. 

Para preparar el entorno de tests, los desarrolladores levantan el siguiente contenedor Docker:
```bash
docker run -d -p 5432:5432 --name postgres-test -e POSTGRES_USER=atsd -e POSTGRES_PASSWORD=atsd -e POSTGRES_DB=atsd_test postgres:13
```
Los tests se lanzan apuntando a este perfil:
```bash
mvn test "-Dspring.profiles.active=postgres"
```

* **`@Sql(scripts = "/clean-db.sql")`**: Para evitar problemas con el estado de la base de datos (y dejar de depender del polémico uso del `@Transactional` en las pruebas), hemos implementado este script que se ejecuta antes de cada test para limpiar las tablas, garantizando que todos los tests sean completamente **aislados e idempotentes**.

---

## 🗂️ Niveles de Pruebas Implementados

### 1. Pruebas de Dominio y Repositorios (`*Test.java` en capa Repository)
En esta capa aplicamos TDD comprobando que las entidades JPA y sus relaciones funcionen correctamente, tanto a nivel de memoria (sin persistir) como una vez persistidas en Base de Datos.
* *Ejemplo en `EquipoTest.java`:* Primero diseñamos pruebas unitarias como `comprobarIgualdadEquipos` comprobando el método `equals()` y `hashCode()`, y luego pruebas de integración `@Transactional` como `comprobarRelacionBaseDatos` que verifica que la relación Bidireccional `Equipo <-> Usuario` se sincroniza adecuadamente con la BD.

### 2. Pruebas de Servicio (`*ServiceTest.java`)
Aquí garantizamos la integridad de la lógica de negocio y validamos que las **excepciones** correctas se lancen en los escenarios esperados.
* *Ejemplo en `EquipoServiceTest`:* Verificamos que si se busca un equipo inexistente a través de `recuperarEquipo(Long id)`, se lance una excepción `EquipoServiceException`. También probamos escenarios complejos como `addUsuarioAEquipoTest` asegurándonos de que la lógica de unión es robusta.

### 3. Pruebas Web y de Controladores (`*WebTest.java` o `*ControllerTest.java`)
Utilizamos **`MockMvc`** para simular las peticiones HTTP realizadas desde el navegador. El foco en esta capa es garantizar:
1. **Comportamiento del Endpoint:** Validar los códigos HTTP devueltos (`200 OK`, `3xx Redirection`).
2. **Seguridad:** A través de Mockito (`@MockBean`) moqueamos nuestro `ManagerUserSession`. Probamos los accesos denegados, confirmando que si un usuario sin sesión intenta acceder a `/equipos/nuevo`, el sistema lo rechaza y lo redirige correctamente a `/login`.
3. **Renderizado del DOM:** Verificamos utilizando comparadores (`containsString(...)`) que el modelo que se pasa al HTML genera las vistas y formularios esperados (Ej: el test `postNuevaTareaDevuelveRedirectYAñadeTarea`).

---

## 💡 Ejemplo Práctico: Implementación de Listado de Equipos

A continuación, ilustramos cómo abordó un desarrollador del equipo la creación de la funcionalidad de la *Lista de Equipos* siguiendo TDD:

1. **Creación del Issue en Github & Trello:** Dividimos la tarea en dos partes: "Service y Model" y "Vistas y Controladores".
2. **Test Inicial (Rojo):** Se escribió el esqueleto en `EquipoServiceTest.java` intentando llamar a un método `findAllOrdenadosPorNombre()` que ni siquiera existía en el código.
3. **Código Funcional (Verde):** Se creó la clase `Equipo`, su `Repository`, y el método pertinente en `EquipoService` que consultaba la base de datos. Se ejecutó el test, pasando a verde.
4. **Comprobaciones y Relaciones (Refactor):** Se introdujeron las colecciones `Set<Usuario> usuarios` y métodos helpers `addUsuario(Usuario)` para la relación *Many-To-Many*, y se adaptaron los tests para que siguieran en verde.
5. **Controlador:** Posteriormente se procedió de la misma manera en `EquipoControllerTest.java`, asegurando que el modelo inyectaba la lista antes de diseñar finalmente la plantilla `equipos.html` en Thymeleaf.