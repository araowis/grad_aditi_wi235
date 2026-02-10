package model;

public final class CEO extends Employee {

    public CEO() {
        this.salary = 400000;
        this.designation = Designation.CLERK;
    }

    @Override
    public void raiseSalary() {
        this.salary += 40000;
    }
}
