 # Literatura-Challenges 📚

Este proyecto, desarrollado como parte del programa Alura Oracle, implementa una API RESTful de libros utilizando Spring Boot y PostgreSQL. Permite gestionar información relacionada con libros, autores y categorías.

## Funcionalidades 🚀

- Gestión completa de libros, autores y categorías mediante operaciones CRUD (Crear, Leer, Actualizar, Eliminar).
- Endpoint para obtener libros por autor o categoría.
- Endpoint para buscar libros por título.

## Requisitos 🛠️

- Java JDK 11 o superior
- Maven 3.x para la gestión de dependencias
- PostgreSQL 9.x o superior instalado y configurado
- IDE compatible con Spring Boot (por ejemplo, IntelliJ IDEA, Eclipse)

## Instalación y Configuración ⚙️

1. **Clonar el repositorio:**

   ```sh
   git clone https://github.com/rafapcjs/Literatura-Challenges.git
Configurar PostgreSQL:

Crear una base de datos en PostgreSQL.
Actualizar las credenciales de la base de datos en el archivo application.properties.
Construir el proyecto con Maven:

sh
Copiar código
mvn clean install
Ejecutar la aplicación:

sh
Copiar código
mvn spring-boot:run
Uso 📖
Endpoints disponibles:
/books - Gestiona libros (CRUD).
/authors - Gestiona autores (CRUD).
/categories - Gestiona categorías (CRUD).
/books/author/{authorId} - Obtiene libros por autor.
/books/category/{categoryId} - Obtiene libros por categoría.
/books/search?title={title} - Busca libros por título.
