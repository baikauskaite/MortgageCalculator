package sample;

public class Date {

    public static final int START_YEAR = 2021;
    public static final int START_MONTH = 3;
    private int year; // over 2021
    private int month; // 1-12 months

    Date() {
        this.year = START_YEAR;
        this.month = START_MONTH;
    }

    public void setYear(int aYear) {
        this.year = aYear;
    }

    public void setMonth(int aMonth) {
        this.month = aMonth;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    static int getStartYear() {
        return START_YEAR;
    }

    static int getStartMonth() {
        return START_MONTH;
    }

}
