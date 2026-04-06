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

import java.io.File;

import java.sql.*;
/**
 *
 * @author 12546
 */
public class EmployeesFile extends DataFile {

    private List<TravelAgencyEmployee> employees;
    private volatile boolean isLoaded = false;

    public EmployeesFile(String filename) {
        super(filename);
        this.employees = new ArrayList<>();
    }

    @Override
    public synchronized void load() {
        if (isLoaded) return;

        Thread thread = new Thread(() -> {
            employees.clear();

            Connection conn = DatabaseManager.getConnection();
            if (conn != null && DatabaseManager.isConnected()) {
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT * FROM travelagencyemployees")) {

                    while (rs.next()) {
                        String name = rs.getString("name");
                        String encryptedPassword = rs.getString("password");
                        boolean isManager = rs.getBoolean("isManager");

                        String decryptedPassword = PasswordEncryptor.decrypt(encryptedPassword);
                        employees.add(new TravelAgencyEmployee(name, decryptedPassword, isManager));
                    }

                    System.out.println("Employees loaded from the database.");
                    isLoaded = true;
                    return;

                } catch (SQLException e) {
                    System.out.println("Failed to load employees from DB: " + e.getMessage());
                }
            }

            // Fallback: load from file
            try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] data = line.split(",");
                    if (data.length == 3) {
                        String name = data[0];
                        String decryptedPassword = PasswordEncryptor.decrypt(data[1]);
                        boolean isManager = Boolean.parseBoolean(data[2]);

                        employees.add(new TravelAgencyEmployee(name, decryptedPassword, isManager));
                    }
                }
                isLoaded = true;
            } catch (IOException e) {
                System.out.println("Error loading employees file: " + e.getMessage());
            }
        });

        thread.start();
    }

    @Override
    public void save() {
        // Save to file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, false))) {
            for (TravelAgencyEmployee e : employees) {
                String encryptedPassword = PasswordEncryptor.encrypt(e.getPassword());
                writer.write(e.getName() + "," + encryptedPassword + "," + e.isManager());
                writer.newLine();
                System.out.println("Saved to file: " + e.getName());
            }
        } catch (IOException e) {
            System.out.println("Error saving employees file: " + e.getMessage());
        }

        // Save to database
        Connection conn = DatabaseManager.getConnection();
        if (conn != null && DatabaseManager.isConnected()) {
            try {
                Statement clearStmt = conn.createStatement();
                clearStmt.executeUpdate("DELETE FROM travelagencyemployees"); // Clear old data

                String insertSQL = "INSERT INTO travelagencyemployees (idNumber, name, phoneNumber, address, password, isManager) VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(insertSQL)) {
                    int i = 0;
                    for (TravelAgencyEmployee e : employees) {
                        stmt.setString(1, "EMP" + (i++)); 
                        stmt.setString(2, e.getName());
                        stmt.setString(3, ""); // Placeholder phone
                        stmt.setString(4, ""); // Placeholder address
                        stmt.setString(5, PasswordEncryptor.encrypt(e.getPassword()));
                        stmt.setBoolean(6, e.isManager());
                        stmt.addBatch();
                        System.out.println("Queued DB insert for: " + e.getName());
                    }
                    stmt.executeBatch();
                    System.out.println("Employees saved to the database.");
                }

            } catch (SQLException e) {
                System.out.println("Error saving employees to database: " + e.getMessage());
            }
        } else {
            System.out.println("Database not connected — skipping DB save.");
        }
    }
    
    public synchronized List<TravelAgencyEmployee> getEmployees() {
        if (!isLoaded) {
            load();
        }
        return employees;
    }

    public void addEmployee(TravelAgencyEmployee employee) {
        employees.add(employee);
        save();
    }

    public boolean editEmployee(int index, TravelAgencyEmployee updatedEmployee) {
        if (index >= 0 && index < employees.size()) {
            employees.set(index, updatedEmployee);
            save();
            return true;
        }
        return false;
    }

    public boolean removeEmployee(int index) {
        if (index >= 0 && index < employees.size()) {
            employees.remove(index);
            save();
            return true;
        }
        return false;
    }
}
