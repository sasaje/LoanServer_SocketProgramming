package com.example.loanserver2;

public class LoanCalculation {

    private double totalLoanAmount; //total loan amount
    private double annualInterest; //annual Interest
    private double numberOfYears; //number of years

    //Constructor
    public LoanCalculation(double totalLoanAmount, double annualInterest, double numberOfYears){
        this.totalLoanAmount = totalLoanAmount;
        this.annualInterest = annualInterest;
        this.numberOfYears = numberOfYears;
    }

    //Calculation for the monthly payment
    public double getMonthlyPayment(){
        double monthlyInterest = annualInterest/1200; //monthly interest rate
        double monthlyPayment = totalLoanAmount*monthlyInterest/(1-(1/Math.pow(1+monthlyInterest, numberOfYears * 12)));
        System.out.println("Monthly payment is " + monthlyPayment);
        return monthlyPayment;
    }

    //Calculation for the total payment inclusive r for n years
    public double getTotalPayment(){
        return getMonthlyPayment()*numberOfYears*12;
    }
}