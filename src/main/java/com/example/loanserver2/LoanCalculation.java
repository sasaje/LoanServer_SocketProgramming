package com.example.loanserver2;

public class LoanCalculation {

    private double k0; //total loan amount
    private double r; // annualInterest
    private int n; //number of years

    //Constructor
    public LoanCalculation(double k0, double r, int n){
        this.k0 = k0;
        this.r = r;
        this.n = n;
    }

    //Calculation for the monthly payment
    public double getMonthlyPayment(){
        double mr = r/1200; //monthly interest rate
        double mp = k0*mr/(1-(1/Math.pow(1+mr, n * 12)));
        System.out.println("MP is " + mp);
        return mp;
    }

    //Calculation for the total payment inclusive r for n years
    public double getTotalPayment(){
        double tp = getMonthlyPayment()*n*12;
        return tp;
    }
}