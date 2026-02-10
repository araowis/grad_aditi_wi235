package persistence;

import java.util.HashMap;
import model.Employee;

public interface EmployeeDAO {
    void insertEmployees(HashMap<String, Employee> employees);
    void deleteEmployee(String employeeID);
    HashMap<String, Employee> getAllEmployees();
    Employee getEmployeeById(String employeeID);
    void updateSalary(String employeeID, double salary);
}
