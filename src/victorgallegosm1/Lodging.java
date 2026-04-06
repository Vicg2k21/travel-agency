/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package victorgallegosm1;
import java.awt.Cursor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author 12546
 */
public abstract class Lodging {
    protected int id; // Unique ID of the lodging
    protected String name;
    protected double pricePerNight;
    protected List<ImageIcon> images; // Stores images as ImageIcons

    public Lodging(String name, double pricePerNight) {
        this.name = name;
        this.pricePerNight = pricePerNight;
        this.images = new ArrayList<>();
    }

    // Getter and setter for ID
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getter and setter for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and setter for price
    public double getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    // Image handling methods
    public void addImage(ImageIcon icon) {
        if (icon != null) {
            images.add(icon);
        }
    }

    public List<ImageIcon> getImages() {
        return images;
    }

    public void clearImages() {
        images.clear();
    }

    // Scales the first image for use as a thumbnail
    public ImageIcon getScaledImageIcon(int width, int height) {
        if (!images.isEmpty()) {
            ImageIcon icon = images.get(0);  // Get the first image as a thumbnail
            Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        }
        return null; // No images
    }
    
    public void loadImagesFromDatabase(Connection conn) {
        String imageQuery = "SELECT imageData FROM lodge_images WHERE lodgeId = ?";

        try (PreparedStatement imgStmt = conn.prepareStatement(imageQuery)) {
            imgStmt.setInt(1, this.id);
            ResultSet imgRs = imgStmt.executeQuery();

            images.clear(); // Clear old images before loading new ones

            while (imgRs.next()) {
                byte[] imageBytes = imgRs.getBytes("imageData");
                if (imageBytes != null && imageBytes.length > 0) {
                    ImageIcon icon = new ImageIcon(imageBytes);
                    this.addImage(icon);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error loading images for lodge ID " + this.id + ": " + e.getMessage());
        }
    }
}


