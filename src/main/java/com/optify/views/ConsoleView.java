package com.optify.views;

import com.optify.domain.Store;
import com.optify.domain.User;
import com.optify.exceptions.AuthenticationException;
import com.optify.exceptions.DataException;
import com.optify.facade.Facade;
import com.optify.services.CategoryService;
import com.optify.services.StoreService;
import com.optify.utilities.Console;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.optify.utilities.Console.*;

@Component
public class ConsoleView {

    @Autowired
    private Facade instance;
    @Autowired
    private StoreService storeService;
    @Autowired
    private CategoryService categoryService;

    public ConsoleView() {
    }

    public void showConsole() {
        boolean exit = false;
        do {
            int option = printMenu();
            exit = processOption(option);
        } while(!exit);
    }

    public int printMenu() {
        System.out.println("***      MENU      ***");
        System.out.println("======================");

        ArrayList<String> options = new ArrayList();
        options.add("» Salir");
        options.add("+ Registrarse");
        options.add("> Iniciar Sesión");
        options.add("+ Agregar Supermercado");
        options.add("~ Actualizar Supermercado");
        options.add("> Ver todos los Supermercados");
        options.add("> Buscar Supermercado por RUT");
        options.add("+ Agregar URL Categoria");
        options.add("+ Nueva Categoría");
        options.add("~ Actualizar Categoría");
        options.add("- Borrar Categoria");
        options.add("> Ver todas las categorías");
        options.add("> Ver categorías por nombre");
        options.add("> Ver categorías por id");
        return Console.menu(options);
    }

    private boolean processOption(int option) {
        boolean exit = false;
        int n;

        switch(option) {
            case 0:
                exit = true;
                break;
            case 1:
                this.signIn();
                break;
            case 2:
                this.logIn();
                break;
            case 3:
                this.addStore();
                break;
            case 4:
                this.updateStore();
                break;
            case 5:
                this.readStores();
                break;
            case 6:
                this.findStoreByRut();
                break;
            case 7:
                this.addCategoryUrlToStore();
                break;
            case 8:
                this.addNewCategory();
                break;
            case 9:
                this.updateCategory();
                break;
            case 10:
                this.deleteCategory();
                break;
            case 11:
                this.listAllCategories();
                break;
            case 12:
                this.getCategoryByName();
                break;
            case 13:
                this.getCategoryById();
                break;
        }
        return exit;
    }

    private void getCategoryById() {
    }

    private void getCategoryByName() {
        
    }

    private void listAllCategories() {
        
    }

    private void deleteCategory() {
        
    }

    private void updateCategory() {
        
    }

    private void addNewCategory() {
        Console.println("*Agregar Nueva Categoria**");
        Console.println("==========================");
        
    }

    private void addCategoryUrlToStore() {
        Console.println("**Agregar Categoria Url***");
        Console.println("==========================");

        long rut = Console.readLong("Ingrese RUT del super: ");
        boolean finish = false;
        Console.println("Agregue la url deseada, digite 0 para terminar.");
        while(!finish) {
            String url = Console.read("Categoria url: ");
            if("0".equals(url)) {
                finish = true;
            } else {
                try {
                    storeService.addUrlCategoryToStore(rut,url);
                } catch (DataException e) {
                    println(e.getMessage());
                    finish = true;
                }
            }
        }
    }

    private void findStoreByRut() {
        Console.println("***Encontrar por RUT**");
        Console.println("======================");

        Store store = instance.getStoreByRut(readLong("Indique el rut del super: "));
        printStoreData(store);
    }

    private void readStores() {
        Console.println("***Todas las Tiendas**");
        Console.println("======================");

        List<Store> sotores = instance.getAllStores();

        for(Store store : sotores) {
            printStoreData(store);
        }
    }

    private void printStoreData(Store store) {
        Console.println("Super: {RUT: " + store.getRut()
                + ", Nombre: " + store.getName()
                + ", Nombre Fantasía: " + store.getFantasyName()
                + ", Url: " + store.getHomePage()
                + "}");
    }


    private void updateStore() {
        Console.println("***Actualizar Tienda**");
        Console.println("======================");

        Store store = new Store();
        store.setRut(Console.readLong("RUT: "));
        store.setName(Console.read("Nombre: "));
        store.setFantasyName(Console.read("Nombre Fantasía: "));
        store.setHomePage(Console.read("Home Page: "));

        try {
            instance.updateStore(store);
        } catch (DataException e) {
            Console.println(e.getMessage());
        }
    }

    private void addStore() {
        Console.println("*** Agregar Tienda ***");
        Console.println("======================");

        Store store = new Store();
        store.setRut(Console.readLong("RUT: "));
        store.setName(Console.read("Nombre: "));
        store.setFantasyName(Console.read("Nombre Fantasía: "));
        store.setHomePage(Console.read("Home Page: "));

        try {
            instance.addStore(store);
        } catch (DataException e) {
            Console.println(e.getMessage());
        }
    }

    private void signIn() {
        Console.println("***  Registrarse   ***");
        Console.println("======================");

        User user = new User();

        user.setCi(Console.readInt("Ingrese su cédula: "));
        user.setUserName(Console.read("Ingrese su nombre de usuario: "));
        user.setName(Console.read("Ingrese su nombre: "));
        user.setLastName(Console.read("Ingrese su apellido: "));
        user.seteMail(Console.read("Ingrese su e-mail de contacto: "));
        user.setPassword(Console.read("Ingrese su contraseña: "));

        try {
            instance.signIn(user);
        } catch (AuthenticationException e) {
            Console.println(e.getMessage());
        }
    }

    private void logIn() {
        Console.println("*** Iniciar Sesión ***");
        Console.println("======================");

        String userName = Console.read("Nombre de Usuario: ");
        String password = Console.read("Contraseña: ");

        try {
            User user = instance.logIn(userName,password);
            Console.println("Sesión iniciada con éxito.");
            Console.println("Nombre: " + user.getName());
            Console.println("Apellido: " + user.getLastName());
            Console.println("Cédula Id: " + user.getCi());
        } catch (AuthenticationException e) {
            Console.println(e.getMessage());
        }
    }
}
