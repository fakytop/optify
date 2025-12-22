package com.optify.views;

import com.optify.domain.*;
import com.optify.exceptions.AuthenticationException;
import com.optify.exceptions.DataException;
import com.optify.facade.Facade;
import com.optify.services.CategoryService;
import com.optify.services.StoreService;
import com.optify.utilities.Console;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.optify.utilities.Console.*;

@Component
public class ConsoleView {

    @Autowired
    private Facade instance;

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
        System.out.println("╔═══════════════════════════╗");
        System.out.println("║    MENU                   ║");
        System.out.println("╚═══════════════════════════╝");

        ArrayList<String> options = new ArrayList();
        options.add("» Salir");
        options.add("+ Registrarse");
        options.add("> Iniciar Sesión");
        options.add("+ Agregar Supermercado");
        options.add("+ Agregar Nuevo Producto");
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
                this.saveProductStore();
                break;
        }
        return exit;
    }

    private boolean findProduct(Product product) {
        String ean = Console.read("Ingrese el código de barras: ");
        try {
            product = instance.getProductByEan(ean);
            return true;
        } catch (DataException e) {
            Console.println(e.getMessage());
        }
        String name = Console.read("Ingrese el nombre del producto: ");
        try {
            product = instance.getProductByName(name);
            return true;
        } catch (DataException e) {
            Console.println(e.getMessage());
        }
        product.setEan(ean);
        product.setName(name);
        return false;
    }

    private void saveProduct(Product product) {
        product.setGtin(Console.read("Ingrese Gtin: "));
        product.setDescription(Console.read("Ingrese Descripcion: "));
        product.setImageUrl(Console.read("Ingrese Url de Imagen: "));
        product.setBrand(Console.read("Ingrese Nombre de Marca: "));
        Category category = new Category();
        saveCategory(category);
        product.setCategory(category);
        try {
            instance.addProduct(product);
        } catch (DataException e) {
            Console.println(e.getMessage());
        }
    }

    private void saveCategory(Category category) {
        String categoryName = null;

        try {
            categoryName = Console.read("Ingrese Nombre de Categoría: ");
            category = instance.getCategoryByName(categoryName);
            return;
        } catch (DataException e) {
            Console.println(e.getMessage());
        }

        category.setName(categoryName);
        category.setDescription(Console.read("Ingrese descripción de la categoría: "));
        try {
            instance.addCategory(category);
        } catch (DataException ex) {
            Console.println(ex.getMessage());
        }
    }

    private void saveProductStore() {
        System.out.println("╔═══════════════════════════╗");
        System.out.println("║ Guardar Producto del Super║");
        System.out.println("╚═══════════════════════════╝");

        Product product = new Product();
        boolean productFound = findProduct(product);

        if(!productFound) {
            saveProduct(product);
        }

        StoreProduct storeProduct = new StoreProduct();
        storeProduct.setProduct(product);
        Store store = null;
        try {
            store = instance.getStoreByRut(Console.readLong("Ingrese RUT del supermercado: "));
            storeProduct.setStore(store);
        } catch (DataException e) {
            Console.println(e.getMessage());
            return;
        }
        storeProduct.setUrlProduct(Console.read("Ingrese URL del producto: "));
        String price = Console.read("Ingrese Precio: $ ");
        storeProduct.setPrice(Double.parseDouble(price));
        try {
            instance.saveStoreProduct(storeProduct);
        } catch (DataException e) {
            Console.println(e.getMessage());
        }

    }

    private void addStore() {
        System.out.println("╔═══════════════════════════╗");
        System.out.println("║    Agregar Supermercado   ║");
        System.out.println("╚═══════════════════════════╝");

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
        System.out.println("╔═══════════════════════════╗");
        System.out.println("║    Registrarse            ║");
        System.out.println("╚═══════════════════════════╝");

        User user = new User();

        user.setCi(Console.readInt("Ingrese su cédula: "));
        user.setUserName(Console.read("Ingrese su nombre de usuario: "));
        user.setName(Console.read("Ingrese su nombre: "));
        user.setLastName(Console.read("Ingrese su apellido: "));
        user.seteMail(Console.read("Ingrese su e-mail de contacto: "));
        user.setPassword(Console.read("Ingrese su contraseña: "));
        Console.println("Indique el Supermercado de preferencia:");

        List<Store> stores = instance.getAllStores();
        int option = Console.menu(stores);
        Store store = instance.getAllStores().get(option);
        user.setPreferredStore(store);

        Console.println("Indique día preferido de la semana:");

        ArrayList<String> weekDays = getWeekDays();
        int day = Console.menu(weekDays);
        user.setPreferredDay(day);

        try {
            instance.signIn(user);
        } catch (AuthenticationException e) {
            Console.println(e.getMessage());
        }
    }

    private ArrayList<String> getWeekDays() {
        ArrayList<String> weekDays =  new ArrayList<>();
        weekDays.add("Domingo");
        weekDays.add("Lunes");
        weekDays.add("Martes");
        weekDays.add("Miércoles");
        weekDays.add("Jueves");
        weekDays.add("Viernes");
        weekDays.add("Sábado");
        return weekDays;
    }

    private void logIn() {
        System.out.println("╔═══════════════════════════╗");
        System.out.println("║    Iniciar Sesión         ║");
        System.out.println("╚═══════════════════════════╝");

        String userName = Console.read("Nombre de Usuario: ");
        String password = Console.read("Contraseña: ");

        try {
            User user = instance.logIn(userName,password);
            Console.println("*******************************");
            Console.println("* Usuario loggeado con éxito. *");
            Console.println("*******************************");
            Console.println(user.toString());
        } catch (AuthenticationException e) {
            Console.println(e.getMessage());
        }
    }
}
