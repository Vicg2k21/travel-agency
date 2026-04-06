/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package victorgallegosm1;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 *
 * @author 12546
 */
public class Order {
    private Lodging lodge;
    private LocalDate startDate;
    private LocalDate endDate;
    private double totalCost;
    private String customerId;

    public Order(Lodging lodge, LocalDate startDate, LocalDate endDate, String customerId) {
        this.lodge = lodge;
        this.startDate = startDate;
        this.endDate = endDate;
        this.customerId = customerId;
        this.totalCost = calculateTotalCost();
    }

    public Lodging getLodge() {
        return lodge;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public int getNumberOfNights() {
        return (int) ChronoUnit.DAYS.between(startDate, endDate);
    }

    public double calculateTotalCost() {
        return lodge.getPricePerNight() * getNumberOfNights();
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getFormattedStartDate() {
        return startDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
    }

    public String getFormattedEndDate() {
        return endDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
    }

    public void printOrder() {
        System.out.println("Customer ID: " + customerId);
        System.out.println("Lodge: " + lodge.getName());
        System.out.println("Start Date: " + getFormattedStartDate());
        System.out.println("End Date: " + getFormattedEndDate());
        System.out.println("Number of Nights: " + getNumberOfNights());
        System.out.printf("Total Cost: %.2f\n", totalCost);
    }

    // ADDED METHODS FOR REPORTS

    public String getItemName() {
        return lodge != null ? lodge.getName() : "Unknown";
    }

    public String getDate() {
        return getFormattedStartDate(); 
    }

    public double getTotal() {
        return totalCost;
    }
}
