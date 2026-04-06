/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package victorgallegosm1;

import java.util.List;
import java.util.ArrayList;

import java.sql.*;
import java.time.LocalDate;

/**
 *
 * @author 12546
 */
public class OrdersFile {
    private List<Order> orders;
    private TravelAgency agency; 

    public OrdersFile(TravelAgency agency) {
        this.orders = new ArrayList<>();
        this.agency = agency;
    }

    public void saveOrderToDatabase(Order order) {
        Connection conn = DatabaseManager.getConnection();
        if (conn == null || !DatabaseManager.isConnected()) {
            System.out.println("Cannot save order: No database connection.");
            return;
        }

        String sql = "INSERT INTO orders (customerId, lodgeId, checkIn, checkOut, totalCost) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, order.getCustomerId());
            stmt.setInt(2, order.getLodge().getId());
            stmt.setDate(3, Date.valueOf(order.getStartDate()));
            stmt.setDate(4, Date.valueOf(order.getEndDate()));
            stmt.setDouble(5, order.getTotalCost());

            stmt.executeUpdate();
            orders.add(order);
            System.out.println("Order saved to database.");
        } catch (SQLException e) {
            System.out.println("Failed to save order to DB: " + e.getMessage());
        }
    }

    public List<Order> getOrdersForCustomer(String customerId) {
        List<Order> customerOrders = new ArrayList<>();
        Connection conn = DatabaseManager.getConnection();
        if (conn == null || !DatabaseManager.isConnected()) {
            System.out.println("Cannot load orders: No database connection.");
            return customerOrders;
        }

        String sql = "SELECT * FROM orders WHERE customerId = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, customerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int lodgeId = rs.getInt("lodgeId");
                LocalDate checkIn = rs.getDate("checkIn").toLocalDate();
                LocalDate checkOut = rs.getDate("checkOut").toLocalDate();
                double cost = rs.getDouble("totalCost");

                Lodging lodge = getLodgingById(lodgeId);
                if (lodge != null) {
                    Order order = new Order(lodge, checkIn, checkOut, customerId);
                    order.setTotalCost(cost);
                    customerOrders.add(order);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error loading orders for customer: " + e.getMessage());
        }

        return customerOrders;
    }

    private Lodging getLodgingById(int lodgeId) {
        for (Lodging lodge : agency.getLodgings()) {
            if (lodge.getId() == lodgeId) {
                return lodge;
            }
        }
        return null;
    }
}