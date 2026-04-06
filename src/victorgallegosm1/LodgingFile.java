/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package victorgallegosm1;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.swing.ImageIcon;
import java.util.ArrayList;
import java.util.List;

import java.sql.*;
import javax.imageio.ImageIO;

/**
 *
 * @author 12546
 */
public class LodgingFile extends DataFile {

    private List<Lodging> lodgings;
    private volatile boolean isLoaded = false;

    public LodgingFile(String filename) {
        super(filename);
        this.lodgings = new ArrayList<>();
    }

    @Override
    public synchronized void load() {
        if (isLoaded) return;

        Thread thread = new Thread(() -> {
            if (!loadFromDatabase()) {
                System.out.println("Falling back to loading from text file.");
                loadFromFile();
            }
            isLoaded = true;
        });

        thread.start();
    }

    private boolean loadFromDatabase() {
        Connection conn = DatabaseManager.getConnection();
        if (conn == null || !DatabaseManager.isConnected()) {
            System.out.println("Database not connected.");
            return false;
        }

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM lodgings")) {

            lodgings.clear();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                double price = rs.getDouble("pricePerNight");
                String type = rs.getString("type");

                Lodging lodge = null;

                if ("House".equalsIgnoreCase(type)) {
                    int numBedrooms = rs.getInt("numberOfBedrooms");
                    boolean hasPool = rs.getBoolean("hasPool");
                    lodge = new House(name, price, numBedrooms, hasPool);
                } else if ("Hotel".equalsIgnoreCase(type)) {
                    int numRooms = rs.getInt("numberOfRooms");
                    boolean hasGym = rs.getBoolean("hasGym");
                    lodge = new Hotel(name, price, numRooms, hasGym);
                }

                if (lodge != null) {
                    lodge.setId(id);
                    lodgings.add(lodge);
                }
            }

            System.out.println("Lodgings loaded from the database.");
            return true;

        } catch (SQLException e) {
            System.out.println("Database load failed: " + e.getMessage());
            return false;
        }
    }

    private void loadFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            lodgings.clear();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                String type = data[0];
                String name = data[1];
                double price = Double.parseDouble(data[2]);

                if ("House".equalsIgnoreCase(type)) {
                    int numBedrooms = Integer.parseInt(data[3]);
                    boolean hasPool = Boolean.parseBoolean(data[4]);
                    lodgings.add(new House(name, price, numBedrooms, hasPool));
                } else if ("Hotel".equalsIgnoreCase(type)) {
                    int numRooms = Integer.parseInt(data[3]);
                    boolean hasGym = Boolean.parseBoolean(data[4]);
                    lodgings.add(new Hotel(name, price, numRooms, hasGym));
                }
            }

            System.out.println("Lodgings loaded from the text file.");

        } catch (IOException e) {
            System.out.println("Error loading from file: " + e.getMessage());
        }
    }

    @Override
    public void save() {
        saveToFile();
        saveToDatabase();
    }

    private void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Lodging lodging : lodgings) {
                if (lodging instanceof House house) {
                    writer.write("House," + house.getName() + "," + house.getPricePerNight()
                            + "," + house.getNumberOfBedrooms() + "," + house.hasPool());
                } else if (lodging instanceof Hotel hotel) {
                    writer.write("Hotel," + hotel.getName() + "," + hotel.getPricePerNight()
                            + "," + hotel.getNumberOfRooms() + "," + hotel.hasGym());
                }
                writer.newLine();
            }
            System.out.println("Lodgings saved to the text file.");
        } catch (IOException e) {
            System.out.println("Error saving to text file: " + e.getMessage());
        }
    }

    private void saveToDatabase() {
        Connection conn = DatabaseManager.getConnection();
        if (conn == null || !DatabaseManager.isConnected()) {
            System.out.println("Database not connected — skipping DB save.");
            return;
        }

        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM lodge_images");
            stmt.executeUpdate("DELETE FROM lodgings");

            String insertSQL = "INSERT INTO lodgings (name, pricePerNight, type, numberOfBedrooms, hasPool, numberOfRooms, hasGym) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement ps = conn.prepareStatement(insertSQL)) {
                for (Lodging lodging : lodgings) {
                    ps.setString(1, lodging.getName());
                    ps.setDouble(2, lodging.getPricePerNight());

                    if (lodging instanceof House h) {
                        ps.setString(3, "House");
                        ps.setInt(4, h.getNumberOfBedrooms());
                        ps.setBoolean(5, h.hasPool());
                        ps.setNull(6, Types.INTEGER);
                        ps.setNull(7, Types.BOOLEAN);
                    } else if (lodging instanceof Hotel h) {
                        ps.setString(3, "Hotel");
                        ps.setNull(4, Types.INTEGER);
                        ps.setNull(5, Types.BOOLEAN);
                        ps.setInt(6, h.getNumberOfRooms());
                        ps.setBoolean(7, h.hasGym());
                    }

                    ps.addBatch();
                }

                ps.executeBatch();
            }

            saveImagesToDatabase(conn);

            System.out.println("Lodgings saved to the database.");

        } catch (SQLException e) {
            System.out.println("Error saving to database: " + e.getMessage());
        }
    }

    private void saveImagesToDatabase(Connection conn) {
        String insertImageSQL = "INSERT INTO lodge_images (lodgeId, imageData) VALUES (?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(insertImageSQL)) {
            for (Lodging lodging : lodgings) {
                int lodgeId = getLodgeId(conn, lodging.getName());

                for (ImageIcon image : lodging.getImages()) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    BufferedImage bufferedImage = new BufferedImage(
                            image.getIconWidth(), image.getIconHeight(), BufferedImage.TYPE_INT_RGB
                    );
                    Graphics g = bufferedImage.getGraphics();
                    image.paintIcon(null, g, 0, 0);
                    g.dispose();

                    ImageIO.write(bufferedImage, "jpg", baos); // or "png"
                    byte[] imageBytes = baos.toByteArray();

                    ps.setInt(1, lodgeId);
                    ps.setBytes(2, imageBytes);
                    ps.addBatch();
                }
            }
            ps.executeBatch();
            System.out.println("Images saved to the database.");

        } catch (SQLException | IOException e) {
            System.out.println("Error saving images to the database: " + e.getMessage());
        }
    }

    private int getLodgeId(Connection conn, String lodgeName) throws SQLException {
        String sql = "SELECT id FROM lodgings WHERE name = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, lodgeName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
            throw new SQLException("Lodge not found: " + lodgeName);
        }
    }
    
    public synchronized List<Lodging> getLodgings() {
        if (!isLoaded) {
            load();
        }
        return lodgings;
    }

    public void addLodging(Lodging lodging) {
        lodgings.add(lodging);
        save();
    }

    public boolean editLodging(int index, Lodging newLodging) {
        if (index >= 0 && index < lodgings.size()) {
            lodgings.set(index, newLodging);
            save();
            return true;
        }
        return false;
    }

    public boolean removeLodging(int index) {
        if (index >= 0 && index < lodgings.size()) {
            lodgings.remove(index);
            save();
            return true;
        }
        return false;
    }

    public void refresh() {
        load();
    }
}
