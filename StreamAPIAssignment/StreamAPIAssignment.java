import java.util.*;
import java.util.stream.*;

record Employee(String name, int age, String gender, String designation, String department, int salary) {
    void display() {
        System.out.println("Name: " + name);
        System.out.println("Age: " + age);
        System.out.println("Designation: " + designation);
        System.out.println("Department: " + department);
        System.out.println("Salary: " + salary);
    }
}

public class StreamAPIAssignment {
    public static void main(String[] args) {
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee("Jem", 30, "Male", "Engineer", "IT", 75000));
        employees.add(new Employee("Lily", 28, "Female", "Engineer", "IT", 72000));
        employees.add(new Employee("Harry", 35, "Male", "Manager", "Finance", 120000));
        employees.add(new Employee("Charl", 30, "Male", "Analyst", "IT", 68000));
        employees.add(new Employee("Di", 26, "Female", "Designer", "Marketing", 65000));
        employees.add(new Employee("Et", 32, "Male", "Engineer", "IT", 78000));
        employees.add(new Employee("Fiona", 29, "Female", "Analyst", "Finance", 70000));

        // highest
        employees.add(new Employee("Gerog", 40, "Male", "Director", "Operations", 150000));
        employees.add(new Employee("Hanah", 27, "Female", "Engineer", "IT", 71000));
        employees.add(new Employee("Ian", 31, "Male", "Consultant", "Finance", 90000));
        employees.add(new Employee("Kara", 28, "Female", "Designer", "Marketing", 66000));
        employees.add(new Employee("Liam", 33, "Male", "Analyst", "Finance", 72000));
        employees.add(new Employee("Mia", 25, "Female", "Engineer", "IT", 68000));
        employees.add(new Employee("Noah", 36, "Male", "Consultant", "Operations", 95000));
        employees.add(new Employee("Olivia", 29, "Female", "Manager", "Marketing", 110000));
        employees.add(new Employee("Paul", 34, "Male", "Engineer", "IT", 77000));
        employees.add(new Employee("Quinn", 27, "Female", "Analyst", "Finance", 69000));
        employees.add(new Employee("Ryan", 32, "Male", "Designer", "Marketing", 70000));
        employees.add(new Employee("Sophia", 26, "Female", "Engineer", "IT", 70000));
        employees.add(new Employee("Tom", 31, "Male", "Consultant", "Finance", 92000));
        employees.add(new Employee("Uma", 28, "Female", "Analyst", "Operations", 71000));
        employees.add(new Employee("Victor", 35, "Male", "Engineer", "IT", 80000));
        employees.add(new Employee("Wendy", 30, "Female", "Designer", "Marketing", 68000));
        employees.add(new Employee("Xander", 29, "Male", "Consultant", "Finance", 91000));
        employees.add(new Employee("Yara", 27, "Female", "Engineer", "IT", 72000));

        System.out.println("Highest paid employee:");
        Optional<Employee> emp = employees.stream().reduce((emp1, emp2) -> {
            if (emp1.salary() > emp2.salary()) {
                return emp1;
            } else {
                return emp2;
            }
        });
        if (emp.isPresent()) {
            emp.get().display();
        } else {
            System.out.println("Skill issue");
        }

        System.out.println("=========================================");

        System.out.println("Number of males and females in the organization: ");
        // Optional<Integer> nummberOfMales = employees.stream().filter(em -> em.gender().equals("Male")).map(em -> 1).reduce((x,y) -> x+y);
        // Optional<Integer> nummberOfFemales = employees.stream().filter(em -> em.gender().equals("Female")).map(em -> 1).reduce((x,y) -> x+y);
        // System.out.println("Number of males: " + nummberOfMales.get());
        // System.out.println("Number of females: " + nummberOfFemales.get());

        // grouping by uses more resources than partitioning by, because it creates a new map for each group, while partitioning by creates only one map with two entries (true and false)
        employees.stream().collect(Collectors.partitioningBy(e -> e.gender().equals("Male"), Collectors.counting())).forEach((bool, count) -> {
            System.out.println("Gender: " + (bool?"Male":"Female"));
            System.out.println("Count: " + count);
        });

        System.out.println("=========================================");

        System.out.println("Total Department-wise expenditure: ");
        employees.stream().collect(Collectors.groupingBy(Employee::department, Collectors.summingInt(Employee::salary))).forEach((dept, total) -> {
            System.out.println("Department: " + dept);
            System.out.println("Total Expense: " + total);
            System.out.println("--------------------------------");
        });

        System.out.println("=========================================");

        System.out.println("Top five seniormost employees: ");
        employees.stream().sorted((e1, e2) -> e2.age() - e1.age()).limit(5).forEach(e -> {
            e.display();
            System.out.println("--------------------------------");
        });

        System.out.println("=========================================");

        System.out.println("All managers in the organization: ");
        employees.stream().filter(e -> e.designation().equals("Manager")).forEach(e -> System.out.println(e.name()));

        System.out.println("=========================================");

        System.out.println("Hike salary of everyone by 20%, except managers");
        employees.stream().map(e -> e.designation().equals("Manager")? e: new Employee(e.name(),e.age(),e.gender(), e.designation(), e.department(), (int)(e.salary() * 1.2))).forEach(System.out::println);

        System.out.println("=========================================");
        System.out.println("Total number of employees:");
        System.out.println(employees.size());
        
    }
}
