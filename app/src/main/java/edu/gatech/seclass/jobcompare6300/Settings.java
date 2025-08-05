package edu.gatech.seclass.jobcompare6300;

import android.content.Context;

import java.io.Serializable;

public class Settings implements Serializable {
    private int yearlySalaryWeight;
    private int yearlyBonusWeight;
    private int tuitionReimbursementWeight;
    private int healthInsuranceWeight;
    private int employeeDiscountWeight;
    private int adoptionAssistanceWeight;

    public Settings(int _salary, int _bonus, int _tuition, int _health, int _employee, int _adoption) {
        yearlySalaryWeight = _salary;
        yearlyBonusWeight = _bonus;
        tuitionReimbursementWeight = _tuition;
        healthInsuranceWeight = _health;
        employeeDiscountWeight = _employee;
        adoptionAssistanceWeight = _adoption;
    }
    public Settings() {
        yearlySalaryWeight = 1;
        yearlyBonusWeight = 1;
        tuitionReimbursementWeight = 1;
        healthInsuranceWeight = 1;
        employeeDiscountWeight = 1;
        adoptionAssistanceWeight = 1;
    }
    public void setSalaryWeight(int _salary) {
        yearlySalaryWeight = _salary;
    }
    public void setBonusWeight(int _bonus) {
        yearlyBonusWeight = _bonus;
    }
    public void setTuitionWeight(int _tuition) {
        tuitionReimbursementWeight = _tuition;
    }
    public void setHealthWeight(int _health) {
        healthInsuranceWeight = _health;
    }
    public void setEmployeeWeight(int _employee) {
        employeeDiscountWeight = _employee;
    }
    public void setAdoptionWeight(int _adopt) {
        adoptionAssistanceWeight = _adopt;
    }
    public int getSalaryWeight() {
        return yearlySalaryWeight;
    }
    public int getBonusWeight() {
        return yearlyBonusWeight;
    }
    public int getTuitionWeight() {
       return tuitionReimbursementWeight;
    }
    public int getHealthWeight() {
        return healthInsuranceWeight;
    }
    public int getEmployeeWeight() {
        return employeeDiscountWeight;
    }
    public int getAdoptionWeight() {
        return adoptionAssistanceWeight;
    }
}
