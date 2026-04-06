/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package victorgallegosm1;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author 12546
 */
public class Customer extends Person {
    private String logInName;
    private String password;
    private double totalSpending;

    // FOR ORDER HISTORY REPORTS
    private List<Order> orders = new ArrayList<>();

    public Customer() {
        super("Unknown", "Unknown", "Unknown", "Unknown");
        this.logInName = "Unknown";
        this.password = "Unknown";
        this.totalSpending = 0.0;
    }

    public Customer(String name, String phoneNumber, String address, String idNumber,
                    String logInName, String password, double totalSpending) {
        super(name, phoneNumber, address, idNumber);
        this.logInName = logInName;
        this.password = password;
        this.totalSpending = totalSpending;
    }

    public String getLogInName() {
        return logInName;
    }

    public void setLogInName(String logInName) {
        this.logInName = logInName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getTotalSpending() {
        return totalSpending;
    }

    public void setTotalSpending(double totalSpending) {
        this.totalSpending = totalSpending;
    }

    @Override
    public String toString() {
        return super.toString() + "\nLogin: " + logInName + "\nTotal Spending: $"
                + String.format("%.2f", totalSpending);
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public List<Order> getOrders() {
        return orders;
    }
    
    public List<Order> getOrdersWithin(String startDate, String endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate start = LocalDate.parse(startDate, formatter);
        LocalDate end = LocalDate.parse(endDate, formatter);

        return orders.stream()
            .filter(order -> !(order.getStartDate().isBefore(start) || order.getEndDate().isAfter(end)))
            .collect(Collectors.toList());
    }
}
