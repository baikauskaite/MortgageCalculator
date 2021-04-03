package sample;

public class Record extends Date {

    private double loan;
    private Date endDate;
    private String loanType;
    private double annualPercent;

    Record() {
        loan = 0;
        endDate = new Date();
        loanType = "Annuity";
        annualPercent = 0;
    }
    Record(double aLoan, int aEndDateYear, int aEndDateMonth, String aLoanType, Double aAnnualPercent) {
        loan = aLoan;
        endDate = new Date();
        endDate.setYear(aEndDateYear);
        endDate.setMonth(aEndDateMonth);
        loanType = aLoanType;
        annualPercent = aAnnualPercent/100;
    }

    public void setLoan(double aLoan) {
        this.loan = aLoan;
    }

    public void setDate(int aYear, int aMonth) {
        this.endDate.setYear(aYear);
        this.endDate.setMonth(aMonth);
    }

    public void setLoanType(String aLoanType) {
        this.loanType = aLoanType;
    }

    public void setAnnualPercent(double aAnnualPercent) {
        this.annualPercent = aAnnualPercent;
    }

    double getLoan() {
        return loan;
    }

    Date getEndDate() {
        return endDate;
    }

    String getLoanType() {
        return loanType;
    }

    double getAnnualPercent() {
        return annualPercent;
    }

}
