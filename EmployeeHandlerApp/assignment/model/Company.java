package model;

import java.util.*;

public class Company {

    public HashMap<String, Employee> employees = new HashMap<>();

    private Company(HashMap<String, Employee> employees) {
        this.employees = employees;
    }

    private static class CompanySingleton {
        private static final Company INSTANCE = new Company(new HashMap<>());
    }

    public static Company getInstance() {
        return CompanySingleton.INSTANCE;
    }

    public HashMap<String, Employee> getLedger(HashMap<String, Employee> employees) {
        return employees;
    } 
    
    public HashMap<String, Employee> getEmployees() {
        return employees;
    }
}
