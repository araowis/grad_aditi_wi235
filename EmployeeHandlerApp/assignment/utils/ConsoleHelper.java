package utils;

import java.util.*;
import persistence.*;
import persistence.operations.*;

public class ConsoleHelper {
    public static int readChoice(String prompt, int min, int max) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.print(prompt);
            try {
                int value = Integer.parseInt(sc.nextLine());
                if (value < min || value > max) {
                    System.out.println("Please enter a number between " + min + " and " + max);
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Try again.");
            }
            sc.close();
        }
    }

    public static EmployeeDAO getDataAcessObject() {
        System.out.println("-----------------------------------------");
        System.out.println("Kindly enter the data source you wish to interact with.");
        System.out.println("1. Filesystem ");
        System.out.println("2. Database ");
        System.out.println("-----------------------------------------");
        int choice  = readChoice("Choice: ", 1, 2);
        do {
        switch (choice) {
            case 1 -> {
                EmployeeDAO dao = new EmployeeDAFile();
                return dao;
            }
            case 2 -> {
                EmployeeDAO dao = new EmployeeDADB();
                return dao;
            }
            default -> System.out.println("Incorrect option, try again.");
        }
    } while (true);
    }
}
