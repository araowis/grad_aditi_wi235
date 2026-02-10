package persistence.operations;

import java.io.*;
import java.util.HashMap;
import model.*;
import persistence.*;

public class EmployeeDAFile implements EmployeeDAO {

    private static final String FILE = "employees.dat";

    @SuppressWarnings("unchecked")
    private HashMap<String, Employee> read() {
        File file = new File(FILE);
        if (!file.exists()) return new HashMap<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (HashMap<String, Employee>) ois.readObject();
        } catch (Exception e) {
            System.out.println(e);
        }
        return new HashMap<>();
    }

    private void write(HashMap<String, Employee> employees) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE))) {
            oos.writeObject(employees);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    @Override
    public void insertEmployees(HashMap<String, Employee> employees) {
        HashMap<String, Employee> data = read();
        data.putAll(employees);
        write(data);
    }

    @Override
    public void deleteEmployee(String employeeID) {
        HashMap<String, Employee> data = read();
        data.remove(employeeID);
        write(data);
    }

    @Override
    public HashMap<String, Employee> getAllEmployees() {
        return read();
    }

    @Override
    public Employee getEmployeeById(String employeeID) {
        return read().get(employeeID);
    }

    @Override
    public void updateSalary(String employeeID, double salary) {
        HashMap<String, Employee> data = read();
        Employee emp = data.get(employeeID);
        if (emp != null) {
            emp.setSalary(salary);
            write(data);
        }
    }
}
