package lk.ijse.dep10.app.model;

import lk.ijse.dep10.app.db.DBConnection;

import java.io.Serializable;
import java.sql.*;

public class Payroll implements Serializable {
    private int id;
    private Date date;
    private String name;
    private int basicSalary;
    private int overTimePayment;
    private int bonus;
    private double tax;
    private double epf;
    private double etf;
    private double unionFee;

    public Payroll() {
    }

    public Payroll(int id, Date date, String name, int basicSalary, int overTimePayment, int bonus, double tax, double epf, double etf, double unionFee) {
        this.id = id;
        this.date = date;
        this.name = name;
        this.basicSalary = basicSalary;
        this.overTimePayment = overTimePayment;
        this.bonus = bonus;
        this.tax = tax;
        this.epf = epf;
        this.etf = etf;
        this.unionFee = unionFee;
    }

    public int getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public String getBasicSalary() {
        return String.format("Rs. %,.2f", (double) basicSalary);
    }

    public String getOverTimePayment() {
        return String.format("Rs. %,.2f", (double) overTimePayment);
    }

    public String getBonus() {
        return String.format("Rs. %,.2f", (double) bonus);
    }

    public double getTax() {
        return tax;
    }

    public double getEpf() {
        return epf;
    }

    public double getEtf() {
        return etf;
    }

    public double getUnionFee() {
        return unionFee;
    }

    public int getYear() {
        return date.toLocalDate().getYear();
    }

    public String getMonth() {
        return date.toLocalDate().getMonth().toString();
    }

    public String getDeductionsStr() {
        return (String.format("Rs. %,.2f", epf + etf + tax + unionFee));
    }

    public String getNetPayStr () {
        return (String.format("Rs. %,.2f", (basicSalary + overTimePayment + bonus - epf - etf - tax - unionFee)));
    }

    public String getFormattedId() {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery("SELECT * FROM Employee WHERE id=" + id);
            rst.next();
            Date joinedDate = rst.getDate("joined_date");
            int year = joinedDate.toLocalDate().getYear();
            return "E-" + year + "-" + id;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
