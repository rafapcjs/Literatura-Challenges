package com.example.literature.main;

import com.example.literature.model.Author;
import com.example.literature.model.Book;
import com.example.literature.model.BookData;
import com.example.literature.model.Results;
import com.example.literature.repository.AuthorRepository;
import com.example.literature.repository.BookRepository;
import com.example.literature.service.ConsumptionAPI;
import com.example.literature.service.ConvertData;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    private final Scanner sc = new Scanner(System.in);
    private final ConvertData convertData = new ConvertData();
    private final ConsumptionAPI consumptionApi = new ConsumptionAPI();
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    private List<Book> books;
    private List<Author> authors;

    public Main(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public void showMenu() {
        final String menu = """
                \n\t**** Por favor, seleccione una opción ****
                \t1 - Buscar Libro por Título
                \t2 - Lista de Libros Registrados
                \t3 - Lista de Autores Registrados
                \t4 - Buscar Autores Vivos por Año
                \t5 - Buscar Libros por Idioma
                \n\t0 - Salir
                """;
        int option = -1;
        System.out.println("****************************************");
        while (option != 0) {
            System.out.println(menu);
            System.out.print("*** Ingrese la opción que desee consultar ---> ");
            option = sc.nextInt();
            sc.nextLine();
            switch (option) {
                case 1 -> searchBookByTitle();
                case 2 -> listRegisteredBooks();
                case 3 -> listRegisteredAuthors();
                case 4 -> listAuthorsAliveInYear();
                case 5 -> listBooksByLanguage();
                case 0 -> System.out.println("FINALIZANDO LA APLICACIÓN...");
                default -> System.out.println("Opción Inválida");
            }
        }
        System.out.println("****************************************");
    }

    private void searchBookByTitle() {
        System.out.print("Ingrese el título a buscar: ");
        String inTitle = sc.nextLine().replace(" ", "%20");
        var json = consumptionApi.getData(inTitle);
        var data = convertData.getData(json, Results.class);
        if (data.results().isEmpty()) {
            System.out.println("Libro no encontrado.");
        } else {
            BookData bookData = data.results().getFirst();
            Book book = new Book(bookData);
            Author author = new Author().getFirstAuthor(bookData);
            saveData(book, author);
        }
    }

    private void saveData(Book book, Author author) {
        if (bookRepository.findByTitleContains(book.getTitle()).isPresent()) {
            System.out.println("Este libro ya fue registrado.");
        } else {
            try {
                bookRepository.save(book);
                System.out.println("Libro registrado.");
            } catch (Exception e) {
                System.out.println("Error al registrar libro: " + e.getMessage());
            }
        }

        if (authorRepository.findByNameContains(author.getName()).isPresent()) {
            System.out.println("Este autor ya ha sido registrado.");
        } else {
            try {
                authorRepository.save(author);
                System.out.println("Autor registrado.");
            } catch (Exception e) {
                System.out.println("Error al registrar autor: " + e.getMessage());
            }
        }
    }

    private void listRegisteredBooks() {
        System.out.println("Lista de Libros Registrados\n---------------------");
        books = bookRepository.findAll();
        books.stream()
                .sorted(Comparator.comparing(Book::getTitle))
                .forEach(System.out::println);
    }

    private void listRegisteredAuthors() {
        System.out.println("Lista de Autores Registrados\n-----------------------");
        authors = authorRepository.findAll();
        authors.stream()
                .sorted(Comparator.comparing(Author::getName))
                .forEach(System.out::println);
    }

    private void listAuthorsAliveInYear() {
        System.out.print("Listar autores vivos por año determinado. Por favor, introduzca el año: ");
        int year = Integer.parseInt(sc.nextLine());
        authors = authorRepository.findByBirthYearLessThanEqualAndDeathYearGreaterThanEqual(year, year);
        if (authors.isEmpty()) {
            System.out.println("No hay autores vivos en ese año.");
        } else {
            authors.stream()
                    .sorted(Comparator.comparing(Author::getName))
                    .forEach(System.out::println);
        }
    }

    private void listBooksByLanguage() {
        System.out.println("Lista de libros por Idioma\n----------------------");
        System.out.println("""
                \n\t---- Seleccione un Idioma ----
                \ten - English (INGLÉS)
                \tes - Español (ESPAÑOL)
                \tfr - Francés (FRANCÉS)
                \tpt - Portugués (PORTUGUÉS)
                """);
        String lang = sc.nextLine();
        books = bookRepository.findByLanguageContains(lang);
        if (books.isEmpty()) {
            System.out.println("No se encontraron libros en el idioma seleccionado.");
        } else {
            books.stream()
                    .sorted(Comparator.comparing(Book::getTitle))
                    .forEach(System.out::println);
        }
    }
}
