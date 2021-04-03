package sample;

import java.text.DecimalFormat;

public class Payment extends Record {

    private int paymentNumber;
    private int durationMonths;
    private double monthlyPercent;
    private double monthlyPayment;
    private double monthlyInterest;
    private double totalMonthlyPayment;
    private double thisPaymentLeft;
    private static double paymentLeft;
    private static int numberOfPayments;
    DecimalFormat df1 = new DecimalFormat("0.00");

    Payment(double aLoan, int aEndDateYear, int aEndDateMonth, String aLoanType, Double aAnnualPercent) {
        super(aLoan, aEndDateYear, aEndDateMonth, aLoanType, aAnnualPercent);
        if (numberOfPayments == 0) {
            paymentLeft = getLoan();
            thisPaymentLeft = paymentLeft;
        }
        thisPaymentLeft = paymentLeft;
        durationMonths = countDurationMonths();
        monthlyPercent = countMonthlyPercent();
        increaseNumberOfPayments();
        paymentNumber = numberOfPayments;
        monthlyInterest = countMonthlyInterest();
        if (aLoanType == "Anuiteto") {
            totalMonthlyPayment = countTotalMonthlyPayment();
            monthlyPayment = totalMonthlyPayment - monthlyInterest;
        }
        else {
            monthlyPayment = super.getLoan() / durationMonths;
            totalMonthlyPayment = countTotalMonthlyPayment();
        }
        thisPaymentLeft = countPaymentLeft();
        paymentLeft = thisPaymentLeft;
    }

    public static void setNumberOfPayments(int number) {
        numberOfPayments = number;
    }

    public static void increaseNumberOfPayments() {
        ++numberOfPayments;
    }

    public int getPaymentNumber() {
        return paymentNumber;
    }

    public String getMonthlyPayment() {
        return df1.format(monthlyPayment) + "€";
    }

    public String getMonthlyInterest() {
        return df1.format(monthlyInterest) + "€";
    }

    public String getTotalMonthlyPayment() {
        return df1.format(totalMonthlyPayment) + "€";
    }

    public String getThisPaymentLeft() {
        return df1.format(thisPaymentLeft + 0.0) + "€";
    }

    public Double getThisPaymentLeftNumber() {
        return thisPaymentLeft;
    }

    public Double getMonthlyInterestNumber() {
        return monthlyInterest;
    }

    public Double getMonthlyPaymentNumber() {
        return monthlyPayment;
    }

    public Double getTotalMonthlyPaymentNumber() {
        return totalMonthlyPayment;
    }

    public int getDurationMonths() {
        return durationMonths;
    }

    public static int getNumberOfPayments() {
        return numberOfPayments;
    }

    public int countDurationMonths() {
        Date endDate = new Date();
        endDate = super.getEndDate();
        Date startDate = new Date();
        startDate.setYear(getStartYear());
        startDate.setMonth(getStartMonth());

        int durationMonths;
        durationMonths = (endDate.getYear() - startDate.getYear()) * 12;
        durationMonths += endDate.getMonth() - startDate.getMonth();

        return durationMonths;
    }

    public double countMonthlyPercent() {
        double monthlyPercent;
        monthlyPercent = super.getAnnualPercent() / 12;
        return monthlyPercent;
    }

    public double countTotalMonthlyPayment() {
        double s = getLoan();
        double r = monthlyPercent;
        double n = durationMonths;
        if (super.getLoanType() == "Anuiteto") {
            return ( (r*s) / (1 - Math.pow((1 + r), (-n))) );
        }
        else {
            return ( monthlyPayment + monthlyInterest);
        }
    }

    public double countMonthlyInterest() {
        double s = thisPaymentLeft;
        double r = monthlyPercent;

        if (super.getLoanType() == "Anuiteto") {
            return (r * s);
        }
        else {
            return (r * s);
        }
    }

    public double countPaymentLeft() {
        double s = thisPaymentLeft;
        return (s - monthlyPayment);
    }

}
