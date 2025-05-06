package org.example.model.domain;

public class TransactionsReportRow {
    private final String date;
    private final String title;
    private final double amount;
    private final String header;

    public TransactionsReportRow(String header, String date, String title, double amount) {
        this.header = header;
        this.date = date;
        this.title = title;
        this.amount = amount;
    }

    public String getHeader() {
        return header;
    }

    public String getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public double getAmount() {
        return amount;
    }
}
