package com.optify.views;

import com.optify.domain.Store;
import com.optify.domain.User;
import com.optify.exceptions.AuthenticationException;
import com.optify.exceptions.DataException;
import com.optify.facade.Facade;
import com.optify.utilities.Console;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConsoleView implements CommandLineRunner {

    @Autowired
    private Facade facade;

    @Override
    public void run(String... args) throws Exception {
        Console.println("Bienvenido al sistema Optify");
        mainMenu();
    }

    private void mainMenu() {
        int choice;
        do {
            Console.println("\n*** MENÚ PRINCIPAL ***");
            Console.println("======================");
            Console.println("1. Usuarios");
            Console.println("2. Supermercados");
            Console.println("0. Salir");

            choice = Console.readInt("Seleccione una opción: ");

            switch (choice) {
                case 1:
                    userMenu();
                    break;
                case 2:
                    storeMenu();
                    break;
                case 0:
                    Console.println("Saliendo...");
                    break;
                default:
                    Console.println("Opción no válida.");
            }
        } while (choice != 0);
    }

    private void userMenu() {
        int choice;
        do {
            Console.println("\n*** MENÚ USUARIOS ***");
            Console.println("=====================");
            Console.println("1. Registrarse (Sign In)");
            Console.println("2. Iniciar Sesión (Log In)");
            Console.println("0. Volver al Menú Principal");

            choice = Console.readInt("Seleccione una opción: ");

            switch (choice) {
                case 1:
                    signIn();
                    break;
                case 2:
                    logIn();
                    break;
                case 0:
                    break;
                default:
                    Console.println("Opción no válida.");
            }
        } while (choice != 0);
    }

    private void storeMenu() {
        int choice;
        do {
            Console.println("\n*** MENÚ SUPERMERCADOS ***");
            Console.println("==========================");
            Console.println("1. Agregar Tienda");
            Console.println("2. Actualizar Tienda");
            Console.println("3. Listar Tiendas");
            Console.println("4. Buscar Tienda por RUT");
            Console.println("5. Agregar URL de Categoría (Scrapper)");
            Console.println("0. Volver al Menú Principal");

            choice = Console.readInt("Seleccione una opción: ");

            switch (choice) {
                case 1:
                    addStore();
                    break;
                case 2:
                    updateStore();
                    break;
                case 3:
                    listStores();
                    break;
                case 4:
                    getStore();
                    break;
                case 5:
                    addUrlCategory();
                    break;
                case 0:
                    break;
                default:
                    Console.println("Opción no válida.");
            }
        } while (choice != 0);
    }

    // --- LÓGICA DE STORES ---

    private void addStore() {
        Console.println("***Agregar Tienda***");

        Store store = new Store();
        store.setName(Console.read("Nombre: "));
        store.setRut(Console.readLong("RUT (0 si no lo tiene aún): "));
        store.setFantasyName(Console.read("Nombre Fantasía: "));
        store.setHomePage(Console.read("Home Page: "));

        try {
            // Llama a Facade.addStore, que internamente usa StoreService.createOrUpdateStore (Upsert)
            facade.addStore(store);
            Console.println("Tienda agregada con éxito.");
        } catch (DataException e) {
            Console.println(e.getMessage());
        }
    }

    private void updateStore() {
        Console.println("***Actualizar Tienda***");

        Store store = new Store();
        // El RUT y el resto de datos se cargan para "enriquecer" la tienda existente
        store.setName(Console.read("Nombre de la Tienda a actualizar: ")); // Clave de búsqueda
        store.setRut(Console.readLong("Nuevo o actual RUT (0 para mantener): "));
        store.setFantasyName(Console.read("Nombre Fantasía: "));
        store.setHomePage(Console.read("Home Page: "));

        try {
            // Llama a Facade.updateStore, que internamente usa StoreService.createOrUpdateStore (Upsert)
            facade.updateStore(store);
            Console.println("Tienda actualizada con éxito.");
        } catch (DataException e) {
            Console.println(e.getMessage());
        }
    }

    private void listStores() {
        Console.println("\n*** Lista de Tiendas ***");
        List<Store> stores = facade.getAllStores();
        if (stores.isEmpty()) {
            Console.println("No hay tiendas registradas.");
            return;
        }
        stores.forEach(s -> {
            Console.println("ID: " + s.getId() + ", Nombre: " + s.getName() + ", RUT: " + s.getRut());
        });
    }

    private void getStore() {
        Console.println("***Obtener Tienda por RUT***");
        long rut = Console.readLong("RUT: ");

        try {
            // CORRECCIÓN AQUÍ: Llamamos a getStoreByRut que devuelve Optional
            Store store = facade.getStoreByRut(rut)
                    .orElseThrow(() -> new NoSuchElementException("Tienda no encontrada con el RUT: " + rut));

            Console.println("Tienda: " + store.getName() + ", RUT: " + store.getRut() + ", Home: " + store.getHomePage());
        } catch (NoSuchElementException e) {
            Console.println("Error: " + e.getMessage());
        } catch (Exception e) {
            Console.println("Error inesperado: " + e.getMessage());
        }
    }

    private void addUrlCategory() {
        Console.println("***Agregar URL Categoría***");
        long rut = Console.readLong("RUT: ");
        String category = Console.read("Categoría (Ej: Congelados): ");

        try {
            // Llama a la Facade, que llama al servicio.
            facade.addUrlCategoryByRut(rut, category);
            Console.println("URL de categoría agregada con éxito.");
        } catch (DataException e) {
            Console.println(e.getMessage());
        }
    }

    // --- LÓGICA DE USUARIOS ---

    private void signIn() {
        Console.println("\n*** REGISTRO DE USUARIO ***");
        User user = new User();
        user.setCi(Console.readLong("Cédula de Identidad (CI): "));
        user.setUserName(Console.read("Nombre de Usuario: "));
        user.setName(Console.read("Nombre: "));
        user.setLastName(Console.read("Apellido: "));
        user.setMail(Console.read("Email: "));
        user.setPassword(Console.read("Contraseña: "));

        try {
            facade.signIn(user);
            Console.println("Usuario registrado con éxito.");
        } catch (DataException e) { // ¡CORRECCIÓN! Solo capturamos DataException
            Console.println("Error al registrar: " + e.getMessage());
        } catch (AuthenticationException e) {
            throw new RuntimeException(e);
        }
    }
    private void logIn() {
        Console.println("\n*** INICIO DE SESIÓN ***");
        String username = Console.read("Nombre de Usuario: ");
        String password = Console.read("Contraseña: ");

        try {
            User loggedInUser = facade.logIn(username, password);
            Console.println("Bienvenido, " + loggedInUser.getName() + ".");
        } catch (AuthenticationException e) { // ¡CORRECCIÓN! Solo AuthenticationException
            Console.println("Error de inicio de sesión: " + e.getMessage());
        }
    }
}