# Quiz CRUD Departamentos y Empleados

Estudiante: Andrés Vargas León
Curso: Programación 4

Proyecto en Spring Boot para registrar departamentos y empleados. Cada departamento puede tener varios empleados.

## Versiones

- Java 25
- Spring Boot 4.2.0
- Base de datos H2

## Cómo ejecutarlo

1. Abrir la carpeta del proyecto en VS Code.
2. En la terminal escribir:

```
mvn spring-boot:run
```

3. Entrar en el navegador a http://localhost:8080

Para ver la base de datos: http://localhost:8080/h2-console
(JDBC URL: jdbc:h2:mem:empresadb, usuario: sa, sin contraseña)

## Usuarios de prueba

- admin / admin123 (ADMIN)
- maria / maria123 (USER)

## Archivos que hice

- Modelo: Puesto.java, Departamento.java, Empleado.java
- R1: DepartamentoRepository.java, EmpleadoRepository.java
- R2: DepartamentoService.java
- R3: EmpleadoService.java
- R4: DepartamentoController.java, EmpleadoController.java
- R5: departamentos/lista.html, departamentos/formulario.html, empleados/lista.html, empleados/formulario.html, error.html
- R6: SecurityConfig.java
- R7: DataInitializer.java

También hice la parte de login y usuarios: Usuario.java, UsuarioRepository.java, UsuarioService.java, UsuarioDetailsService.java, LoginController.java, UsuarioController.java, login.html y las vistas de usuarios.