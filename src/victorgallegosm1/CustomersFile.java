/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package victorgallegosm1;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import java.sql.*;
/**
 *
 * @author 12546
 */
public class CustomersFile extends DataFile {

    private List<Customer> customers;
    private volatile boolean isLoaded = false;

    public CustomersFile(String filename) {
        super(filename);
        this.customers = new ArrayList<>();
    }

    @Override
    public synchronized void load() {
        if (isLoaded) return;  // Already loaded, skip

        Thread thread = new Thread(() -> {
            customers.clear();

            Connection conn = DatabaseManager.getConnection();
            if (conn != null && DatabaseManager.isConnected()) {
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT * FROM customers")) {

                    while (rs.next()) {
                        String name = rs.getString("name");
                        String phone = rs.getString("phoneNumber");
                        String address = rs.getString("address");
                        String id = rs.getString("idNumber");
                        String login = rs.getString("logInName");
                        String encryptedPassword = rs.getString("password");
                        double spending = rs.getDouble("totalSpending");

                        String decryptedPassword = PasswordEncryptor.decrypt(encryptedPassword);
                        customers.add(new Customer(name, phone, address, id, login, decryptedPassword, spending));
                    }

                    System.out.println("Customers loaded from the database.");
                    isLoaded = true;
                    return;

                } catch (SQLException e) {
                    System.out.println("Failed to load customers from DB: " + e.getMessage());
                }
            }

            // Fallback: load from file
            System.out.println("Loading customers from text file instead...");
            try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] data = line.split(",");
                    if (data.length == 7) {
                        String name = data[0];
                        String phone = data[1];
                        String address = data[2];
                        String id = data[3];
                        String login = data[4];
                        String decryptedPassword = PasswordEncryptor.decrypt(data[5]);
                        double spending = Double.parseDouble(data[6]);

                        customers.add(new Customer(name, phone, address, id, login, decryptedPassword, spending));
                    }
                }
                isLoaded = true;
            } catch (IOException e) {
                System.out.println("Error loading customers file: " + e.getMessage());
            }
        });

        thread.start();
    }

    @Override
    public void save() {
        // Save to file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, false))) {
            for (Customer c : customers) {
                String encryptedPassword = PasswordEncryptor.encrypt(c.getPassword());
                writer.write(c.getName() + "," + c.getPhoneNumber() + "," + c.getAddress() + ","
                        + c.getIdNumber() + "," + c.getLogInName() + "," + encryptedPassword + "," + c.getTotalSpending());
                writer.newLine();
                System.out.println("Saved Customer to file: " + c.getName());
            }
        } catch (IOException e) {
            System.out.println("Error saving customers file: " + e.getMessage());
        }

        // Save to database
        Connection conn = DatabaseManager.getConnection();
        if (conn != null && DatabaseManager.isConnected()) {
            try {
                Statement clearStmt = conn.createStatement();
                clearStmt.executeUpdate("DELETE FROM customers"); // Clear old data

                String insertSQL = "INSERT INTO customers (idNumber, name, phoneNumber, address, logInName, password, totalSpending) VALUES (?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(insertSQL)) {
                    for (Customer c : customers) {
                        stmt.setString(1, c.getIdNumber());
                        stmt.setString(2, c.getName());
                        stmt.setString(3, c.getPhoneNumber());
                        stmt.setString(4, c.getAddress());
                        stmt.setString(5, c.getLogInName());
                        stmt.setString(6, PasswordEncryptor.encrypt(c.getPassword()));
                        stmt.setDouble(7, c.getTotalSpending());
                        stmt.addBatch();
                        System.out.println("Queued DB insert for: " + c.getName());
                    }
                    stmt.executeBatch();
                    System.out.println("Customers saved to the database.");
                }

            } catch (SQLException e) {
                System.out.println("Error saving customers to database: " + e.getMessage());
            }
        } else {
            System.out.println("Database not connected — skipping DB save.");
        }
    }
    
    public synchronized List<Customer> getCustomers() {
        if (!isLoaded) {
            load();  // Trigger lazy load if needed
        }
        return customers;
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
        save();
    }

    public boolean editCustomer(int index, Customer updatedCustomer) {
        if (index >= 0 && index < customers.size()) {
            customers.set(index, updatedCustomer);
            save();
            return true;
        }
        return false;
    }

    public boolean removeCustomer(int index) {
        if (index >= 0 && index < customers.size()) {
            customers.remove(index);
            save();
            return true;
        }
        return false;
    }
}
