package model;

public final class Manager extends Employee {
    public Manager() {
        this.salary = 100000;
        this.designation = Designation.MANAGER;
    }

    @Override
    public void raiseSalary() {
        this.salary += 30000;
    }

}
