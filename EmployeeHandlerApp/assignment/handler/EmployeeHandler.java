package handler;

import java.util.*;
import model.*;
import persistence.*;
import service.*;
import utils.*;

public class EmployeeHandler {

    public static void main(String[] args) {

        EmployeeDAO dao = ConsoleHelper.getDataAcessObject();
        EmployeeService eservice = new EmployeeService(dao);
        CompanyService cservice = new CompanyService(dao);

        while (true) {
            System.out.println("-----------------------------------------");
            System.out.println("Kindly enter the choice of operation: ");
            System.out.println("1. Create Employee ");
            System.out.println("2. Display Employee ");
            System.out.println("3. Raise Salary of one Employee ");
            System.out.println("4. Give all Employees a raise ");
            System.out.println("5. Delete Employee ");
            System.out.println("6. Exit ");
            System.out.println("-----------------------------------------");

            int choice = ConsoleHelper.readChoice("Enter choice: ", 1, 6);

            switch (choice) {
                case 1 -> {
                    HashMap<String, Employee> employees = eservice.createEmployees();
                    cservice.addEmployees(employees);
                }
                case 2 -> {
                    System.out.println("\nEmployees in the organisation:");
                    cservice.displayEmployeeData();
                }
                case 3 -> {
                    // eservice.raiseSalaryForAll();                    
                    eservice.raiseSalaryForOne();
                    System.out.println("Salary raised for employee.");
                }
                case 4 -> {
                    cservice.raiseSalaryForAll();
                    System.out.println("Salary raised for all employees.");
                }
                case 5 -> {
                    eservice.deleteEmployee();
                    System.out.println("Employee deletion succesful.");
                }
                case 6 -> {
                    System.out.println("Total Employees: " + cservice.getEmployeeCount());
                    System.out.println("Thank you!");
                    return;
                }
            }
        }
    }
}
