package com.optify.utilities;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Console {
    protected static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

    public static void print(String s) {
        System.out.print(s);
    }

    public static void println(String s) {
        System.out.println(s);
    }

    public static void print(int i) {
        System.out.print(i);
    }

    public static void println(int i) {
        System.out.println(i);
    }

    public static String read() {
        try {
            return in.readLine();
        } catch(Exception e) {
            return null;
        }
    }

    public static String read(String s) {
        print(s);
        return read();
    }

    public static int readInt() {
        return readInt(null);
    }

    public static long readLong(String msg) {
        long n = -1;
        boolean ok = false;
        while(!ok) {
            try {
                if(msg!= null) {
                    print(msg);
                }
                n = Long.parseLong(read());
                ok = true;
            } catch (Exception e) {

            }
        }
        return n;
    }

    public static int readInt(String msg) {
        int n = -1;
        boolean ok = false;
        while(!ok) {
            try{
                if(msg!= null) {
                    print(msg);
                }
                n = Integer.parseInt(read());
                ok = true;
            } catch(Exception e) {

            }
        }
        return n;
    }

    public static int menu(List options) {
        for(int i = 0; i < options.size(); i++) {
            println((i+1) + " - " + options.get(i).toString());
        }
        int option;
        do {
            option = readInt("Opcion: ") - 1;
        } while(option < 0 || option >= options.size());
        return option;
    }

    public static int menu(ArrayList options) {
        for(int i = 0; i < options.size(); i++) {
            if(i < 9) {
                println("0"+(i+1) + options.get(i).toString());
            } else {
                println((i+1) + options.get(i).toString());
            }
        }
        int option;
        do {
            option = readInt("Opcion: ") - 1;
        } while(option < 0 || option >= options.size());
        return option;
    }
}
