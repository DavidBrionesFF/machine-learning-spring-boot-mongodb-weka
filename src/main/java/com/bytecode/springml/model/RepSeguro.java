package com.bytecode.springml.model;

public class RepSeguro {
    private int age;
    private int sex;
    private double bmi;
    private int children;
    private int smoker;
    private double charges;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public double getBmi() {
        return bmi;
    }

    public void setBmi(double bmi) {
        this.bmi = bmi;
    }

    public int getChildren() {
        return children;
    }

    public void setChildren(int children) {
        this.children = children;
    }

    public int getSmoker() {
        return smoker;
    }

    public void setSmoker(int smoker) {
        this.smoker = smoker;
    }

    public double getCharges() {
        return charges;
    }

    public void setCharges(double charges) {
        this.charges = charges;
    }
}
