package model;

import java.io.*;
import java.util.*;
import java.util.function.*;

// E is fixed, C/P for 2nd letter, then 4 numbers
public abstract class Employee implements Serializable {
    private static final long serialVersionUID = 1L;
    private String employeeID;
    private String name;
    public int age;
    protected double salary;
    protected Gender gender;
    protected Designation designation;

    public Employee() {
        this.name = "";
        this.age = 18;
        this.salary = 0.0;
        this.designation = Designation.UNASSIGNED;
        this.employeeID = "";
        this.gender = Gender.MALE;
    }

    public Employee(String employeeID, String name, int age, String gender) {
        setEmployeeID(employeeID);
        setName(name);
        setAge(age);
        setGender(gender);
    }

    private static final Map<Designation, Supplier<Employee>> REGISTRY = Map.of(
        Designation.CLERK, Clerk::new,
        Designation.PROGRAMMER, Programmer::new,
        Designation.MANAGER, Manager::new
    );

    public Employee(String employeeID, String name, int age, double salary, Gender gender, Designation designation) {
        this.name = name;
        this.age = age;
        this.salary = salary;
        this.designation = designation;
        this.employeeID = employeeID;
        this.gender = gender;
    }

    public void addDetails() {
        Scanner sc = new Scanner(System.in);
        boolean valid = false;

        System.out.println("Kindly enter the name of the employee\n");
        do {
            try {
                String name = sc.nextLine();
                setName(name);
                valid = true;
            } catch (InputMismatchException e) {
                System.out.println(".()");
                // sc.next();
            }
        } while (!valid);

        valid = false;

        System.out.println("Kindly enter the age of the employee\n");
        do {
            try {
                int age = Integer.parseInt(sc.nextLine());
                setAge(age);
                valid = true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number between 15 and 75.");
                // sc.next();
            }
        } while (!valid);

        valid = false;

        System.out.println("Kindly enter the gender of the employee\n");
        do {
            try {
                String gender = sc.nextLine();
                setGender(gender);
                valid = true;
            } catch (InputMismatchException e) {
                System.out.println("Kindly enter either female or male (case insensitive)");
                // sc.next();
            }

        } while (!valid);

        valid = false;

        System.out.println("Kindly enter the employee ID\n");
        do {
            try {
                String employeeID = sc.nextLine();
                valid = employeeIDVerification(employeeID);
            } catch (InputMismatchException e) {
                System.out.println("Kindly enter the correct employee ID\n");
            }
        } while (!valid);
        sc.close();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDesignation(Designation designation) {
        this.designation = designation;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public void setAge(int age) {
        if (age < 15 || age > 75) {
            throw new IllegalArgumentException(age + " is an invalid age.");
        }
        this.age = age;
    }

    public void setGender(String gender) {
        do {
            if (gender.equalsIgnoreCase("female")) {
                this.gender = Gender.FEMALE;
                break;
            } else if (gender.equalsIgnoreCase("male")) {
                this.gender = Gender.MALE;
                break;
            } else {
                this.gender = Gender.UNKNOWN;
                throw new InputMismatchException(gender + " is not a valid gender. Please try again.");
            }
        } while (true);
        System.out.println("Gender set to: " + this.gender);
    }

    public void setEmployeeID(String employeeID) {
        employeeIDVerification(employeeID);
    }

    public String getName() {
        return this.name;
    }

    public String getDesignation() {
        return this.designation.toString();
    }

    public String getGender() {
        return this.gender.toString();
    }

    public String getEmployeeID() {
        return this.employeeID;
    }

    public double getSalary() {
        return this.salary;
    }

    public final void printEmployeeDetails() {
        System.out.println("Employee name " + this.name);
        System.out.println("Employee Age " + this.age);
        System.out.println("Employee Salary " + this.salary);
        System.out.println("Employee Designation " + this.designation);
        System.out.println("Employee Gender " + this.gender);
        System.out.println("Employee ID " + this.employeeID);
    }

    public abstract void raiseSalary();

    private boolean employeeIDVerification(String employeeID) {
        do {
            String regex = "^[E]{1}[PCM]{1}[0-9]{4}$";
            if (!employeeID.matches(regex)) {
                System.out.println("Please, try again. Invalid employee ID format");
                return false;
            } else {
                this.employeeID = employeeID;
                return true;
            }
        } while (true);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Employee)) {
            return false;
        }
        Employee other = (Employee) o;
        return Objects.equals(employeeID, other.employeeID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeID);
    }

}
