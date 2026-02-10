package service;

import java.util.*;
import model.*;
import persistence.*;
import utils.*;

public class EmployeeService {

    private final EmployeeDAO employeeDAO;

    public EmployeeService(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    public HashMap<String, Employee> createEmployees() {
        HashMap<String, Employee> employees = new HashMap<>();

        while (true) {
            System.out.println("-----------------------------------------");
            System.out.println("Kindly enter the Employee type you're creating.");
            System.out.println("1. Enter 1 for Clerk ");
            System.out.println("2. Enter 2 for Programmer ");
            System.out.println("3. Enter 3 for Manager ");
            System.out.println("4. Enter 4 to end new Employee entry ");
            System.out.println("-----------------------------------------");

            int choice = ConsoleHelper.readChoice("Choice: ", 1, 4);
            Employee emp;

            switch (choice) {
                case 1 -> {
                    emp = new model.Clerk();
                }
                case 2 -> {
                    emp = new model.Programmer();
                }
                case 3 -> {
                    emp = new model.Manager();
                }
                case 4 -> {
                    return employees;
                }
                default -> throw new IllegalStateException();
            };

            emp.addDetails();

            if (employees.containsKey(emp.getEmployeeID())) {
                System.out.println("Duplicate employee ID. Try again.");
            } else {
                employees.put(emp.getEmployeeID(), emp);
                System.out.println("Employee added.");
            }
        }
    }

    public void raiseSalaryForOne() {
        try (Scanner sc = new Scanner(System.in)) {
            System.out.println("Kindly enter the ID of the employee to be deleted.");
            String employeeID = sc.nextLine();
            Employee emp = employeeDAO.getEmployeeById(employeeID);
            emp.raiseSalary();
            employeeDAO.updateSalary(employeeID, emp.getSalary());
        }
    }

    public void deleteEmployee() {
        try (Scanner sc = new Scanner(System.in)) {
            System.out.println("Kindly enter the ID of the employee to be deleted.");
            String employeeID = sc.nextLine();
            employeeDAO.deleteEmployee(employeeID);
        }
    }
}
