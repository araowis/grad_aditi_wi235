package model;

public final class Programmer extends Employee {
    public Programmer() {
        this.salary = 50000;
        this.designation = Designation.PROGRAMMER;
    }

    @Override
    public void raiseSalary() {
        this.salary += 10000;
    }
}
