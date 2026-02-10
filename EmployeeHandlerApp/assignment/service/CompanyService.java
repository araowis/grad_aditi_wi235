package service;

import java.util.*;
import model.*;
import persistence.EmployeeDAO;

public class CompanyService {

    // private final Company company;
    Company company = Company.getInstance();
    private final EmployeeDAO employeeDAO;

    public CompanyService(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
        company.employees = company.getLedger(employeeDAO.getAllEmployees());
    }

    public void addEmployees(HashMap<String, Employee> newEmployees) {
        for (Employee emp : newEmployees.values()) {
            if (company.employees.containsKey(emp.getEmployeeID())) {
                System.out.println("Duplicate employee ID: " + emp.getEmployeeID());
            } else {
                company.employees.put(emp.getEmployeeID(), emp);
            }
        }
        employeeDAO.insertEmployees(newEmployees);
    }

    public void displayEmployeeData() {
        HashMap<String, Employee> employees = new HashMap<>();
        System.out.println("E-ID\t\tNAME\t\tAGE\t\tSALARY\t\tGENDER\\t\\tDESIGNATION");
        employees = employeeDAO.getAllEmployees();
        for (Employee emp : employees.values()) {
            System.out.println(emp.getEmployeeID() + "\t\t" + emp.getName() + "\t\t" + emp.age + "\t\t" + emp.getSalary() + "\t\t" + emp.getGender() + "\t\t" + emp.getDesignation());
        }
    }

    public Collection<Employee> getAllEmployees() {
        return company.employees.values();
    }

    public int getEmployeeCount() {
        return company.employees.size();
    }

    public void raiseSalaryForAll() {
        for (Employee emp : company.employees.values()) {
            emp.raiseSalary();
            try {
                employeeDAO.updateSalary(emp.getEmployeeID(), emp.getSalary());
            } catch (Exception e) {
                System.out.println("DB/File operation messed up.");
                System.out.println(e);
            }
        }
    }
}
