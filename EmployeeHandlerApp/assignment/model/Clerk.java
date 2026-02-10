package model;

public final class Clerk extends Employee {

    public Clerk() {
        this.salary = 20000;
        this.designation = Designation.CLERK;
    }

    @Override
    public void raiseSalary() {
        this.salary += 3000;
    }
}
