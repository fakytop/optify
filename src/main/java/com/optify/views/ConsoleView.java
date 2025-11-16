package com.optify.views;

import com.optify.domain.User;
import com.optify.exceptions.AuthenticationException;
import com.optify.services.UserService;
import com.optify.utilities.Console;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

import static com.optify.utilities.Console.print;
import static com.optify.utilities.Console.readInt;
@Component
public class ConsoleView {

    @Autowired
    public UserService userService;

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
        options.add("Salir");
        options.add("Registrarse");
        options.add("Iniciar Sesión");

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
        }
        return exit;
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
            userService.signIn(user);
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
            User user = userService.logIn(userName,password);
            Console.println("Sesión iniciada con éxito.");
            Console.println("Nombre: " + user.getName());
            Console.println("Apellido: " + user.getLastName());
            Console.println("Cédula Id: " + user.getCi());
        } catch (AuthenticationException e) {
            Console.println(e.getMessage());
        }
    }
}
