# Sistema de Gestión de Biblioteca Escolar

## Descripción
Aplicación de escritorio desarrollada en Java para gestionar libros, estudiantes, préstamos, devoluciones y reportes de una biblioteca escolar.

## Tecnologías utilizadas
- Java 23
- Swing (interfaz gráfica)
- MySQL
- JDBC y MySQL Connector/J 9.4.0
- Maven
- IntelliJ IDEA

## Funcionalidades
- Inicio de sesión con validación de credenciales y roles.
- Gestión de libros: agregar, listar, modificar y eliminar.
- Gestión de estudiantes.
- Registro de préstamos y devoluciones.
- Actualización del stock de libros.
- Reportes de libros más prestados, historial de estudiantes y préstamos vigentes.

## Arquitectura y diseño
- **MVC:** separación entre modelos, vistas y controlador.
- **DAO:** acceso a los datos mediante clases DAO.
- **Singleton:** instancia única de `DatabaseConnection`.
- **Concurrencia:** uso de `SwingWorker` para ejecutar operaciones de préstamo en segundo plano.
- **Sincronización:** uso de transacciones y métodos `synchronized` para proteger las operaciones de préstamo y stock.
- **POO:** clase abstracta `Usuario`, herencia en `Estudiante` e interfaz `Identificable`.

## Requisitos
- Java 23 o una versión compatible con la compilación del proyecto.
- MySQL instalado y en ejecución.
- Base de datos `biblioteca` creada y configurada.
- Credenciales de MySQL configuradas en `conexion.DatabaseConnection`.

## Ejecución
1. Crear la base de datos utilizando los scripts SQL incluidos.
2. Configurar las credenciales de MySQL en `DatabaseConnection`.
3. Ejecutar `BibliotecaEscolar-1.0-SNAPSHOT.jar` desde una terminal con:

   `java -jar BibliotecaEscolar-1.0-SNAPSHOT.jar`

4. Iniciar sesión con un usuario existente en la base de datos.

## Usuarios de prueba
- Bibliotecario: `antonia@correo.cl`
- Estudiante: `carlos@correo.cl`

Utilizar la contraseña de prueba definida en el script SQL de datos iniciales.

## Autor
Proyecto académico desarrollado para la Evaluación Final Transversal de Desarrollo Orientado a Objetos II.