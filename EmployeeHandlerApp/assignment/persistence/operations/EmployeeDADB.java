package persistence.operations;

import java.sql.*;
import java.util.*;
import model.*;
import persistence.*;

public class EmployeeDADB implements EmployeeDAO {

    private static Connection createConn() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/company", "postgres", "postgres");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return conn;
    }

    @Override
    public void insertEmployees(HashMap<String, Employee> employees) {
        String sql = "INSERT INTO EMPLOYEE (employee_id, employee_name, designation, age, salary, gender) VALUES (?,?,?,?,?,?)";
        try (Connection conn = createConn()) {
            conn.setAutoCommit(false);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            for (Employee emp : employees.values()) {
                pstmt.setString(1, emp.getEmployeeID());
                pstmt.setString(2, emp.getName());
                pstmt.setObject(3, emp.getDesignation());
                pstmt.setInt(4, emp.age);
                pstmt.setDouble(5, emp.getSalary());
                pstmt.setObject(6, emp.getGender());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            conn.commit();
            System.out.println("Employees succesfully inserted in DB!");
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    @Override
    public void deleteEmployee(String employeeID) {
        String sql = "DELETE FROM EMPLOYEE WHERE employee_id = ?";
        try (Connection conn = createConn(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
            conn.setAutoCommit(false);
            pstmt.setString(1, employeeID);
            try {
                pstmt.execute();
                System.out.println("Employee with E-ID " + employeeID + " deleted from Database.");
            } catch (Exception e) {
                System.out.println(e);
            }
            conn.commit();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    @Override
    public HashMap<String, Employee> getAllEmployees() {
        HashMap<String, Employee> employees = new HashMap<>();
        try (Connection conn = createConn(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM EMPLOYEE")) {
            while (rs.next()) {
                // System.out.println(rs.getString("employee_id") + "\t\t" + rs.getString("employee_name") + "\t\t" + rs.getDouble("age") + "\t\t" + rs.getDouble("salary") + "\t\t" + rs.getString("gender") + "\t\t" + rs.getString("designation"));
                Employee emp = new Employee(rs.getString("employee_id"), rs.getString("employee_name"), rs.getInt("age"), rs.getDouble("salary"), Gender.toGender(rs.getString("gender")), Designation.toDesgination(rs.getString("designation")));
                employees.put(rs.getString("employee_id"), emp);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return employees;
    }

    @Override
    public Employee getEmployeeById(String employeeID) {
        String sql = "SELECT * FROM EMPLOYEE WHERE employee_id = ?";
        try (Connection conn = createConn(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
            pstmt.setString(1, employeeID);
            try (ResultSet rs = pstmt.executeQuery();) {
                if (rs.next()) {
                    return new Employee(rs.getString("employee_id"), rs.getString("employee_name"), rs.getInt("age"), rs.getDouble("salary"), Gender.toGender(rs.getString("gender")), Designation.toDesgination(rs.getString("designation")));
                } else {
                    System.out.println("Could not find employee in DB.");
                    return null;
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public void updateSalary(String employeeID, double salary) {
        try (Connection conn = createConn();) {
            String sql = "UPDATE EMPLOYEE SET salary = ? WHERE employee_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setDouble(1, salary);
            pstmt.setString(2, employeeID);
            pstmt.execute();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

}
