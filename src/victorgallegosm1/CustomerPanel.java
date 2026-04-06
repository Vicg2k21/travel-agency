/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package victorgallegosm1;

// June 18 25 ***********************************************************************************
//import com.sun.jdi.connect.spi.Connection;
import java.sql.Connection;
import java.sql.SQLException;
// June 18 25 ************************************************************************************

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.ImageIcon;
import java.util.List;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author 12546
 */
public class CustomerPanel extends JPanel {
    private final TravelAgency agency;
    private JList<String> lodgeList;
    private JLabel lodgeDetailsLabel, thumbnailLabel;
    private JComboBox<String>[] checkInBoxes, checkOutBoxes;
    private Order currentOrder;
    private JPanel thumbnailPanel; // New scrollable thumbnail panel
    // June 24 25 ***********************************************************************
    private JLabel connectionStatusLabel;  // Shows DB connection status
    // June 24 25 ***********************************************************************

    public CustomerPanel(TravelAgency agency) {
        this.agency = agency;
        setLayout(new BorderLayout());
        setBackground(new Color(173, 216, 230)); 

        add(createTitlePanel(), BorderLayout.NORTH);
        add(createMainContent(), BorderLayout.CENTER);
        
        // June 24 25 *****************************************************************************
        updateConnectionStatus(DatabaseManager.isConnected());
        // June 24 25 *****************************************************************************
    }

    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(255, 165, 0));
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Welcome to Travel Agency");
        JLabel subtitle = new JLabel("Entering Customer State");
        styleLabel(title, 24, Font.BOLD);
        styleLabel(subtitle, 16, Font.ITALIC);

        // Connection Status Label
        connectionStatusLabel = new JLabel("Checking database connection...");
        connectionStatusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        connectionStatusLabel.setForeground(Color.DARK_GRAY);
        connectionStatusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        DatabaseManager.addConnectionListener(connected -> SwingUtilities.invokeLater(() -> {
            if (connected) {
                connectionStatusLabel.setText("Database Connected");
                connectionStatusLabel.setForeground(new Color(0, 128, 0));  // Dark green
            } else {
                connectionStatusLabel.setText("Database Disconnected");
                connectionStatusLabel.setForeground(Color.RED);
            }
        }));

        titlePanel.add(title);
        titlePanel.add(subtitle);
        titlePanel.add(Box.createVerticalStrut(5));
        titlePanel.add(connectionStatusLabel);  // Add to title panel

        return titlePanel;
    }
    
    // June 24 25 *****************************************************************************
    private void updateConnectionStatus(boolean connected) {
        if (connected) {
            connectionStatusLabel.setText("Database Connected");
            connectionStatusLabel.setForeground(new Color(0, 128, 0));
        } else {
            connectionStatusLabel.setText("Database Disconnected");
            connectionStatusLabel.setForeground(Color.RED);
        }
    }
    // June 24 25 *****************************************************************************

    private JPanel createMainContent() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        JPanel splitWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        splitWrapper.setOpaque(false);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createLodgePanel(), createDetailPanel());
        splitPane.setDividerLocation(400);
        splitPane.setPreferredSize(new Dimension(850, 500));
        splitPane.setOneTouchExpandable(true);
        splitWrapper.add(splitPane);

        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(splitWrapper);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(createDatePanel());
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(createButtonPanel());

        return contentPanel;
    }

    private JPanel createLodgePanel() {
        DefaultListModel<String> model = new DefaultListModel<>();
        for (Lodging lodge : agency.getLodgings()) model.addElement(lodge.getName());

        lodgeList = new JList<>(model);
        lodgeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lodgeList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int index = lodgeList.getSelectedIndex();
                if (index != -1) updateLodgeDetails(agency.getLodgings().get(index));
            }
        });

        JScrollPane scrollPane = new JScrollPane(lodgeList);
        scrollPane.setPreferredSize(new Dimension(380, 300));
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(scrollPane);

        return panel;
    }

    private JPanel createDetailPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        thumbnailLabel = new JLabel("Main Image Preview");
        thumbnailLabel.setPreferredSize(new Dimension(200, 200));
        thumbnailLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        thumbnailLabel.setHorizontalAlignment(SwingConstants.CENTER);
        thumbnailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        lodgeDetailsLabel = new JLabel("Select a lodge to view details.");
        lodgeDetailsLabel.setPreferredSize(new Dimension(300, 120));
        lodgeDetailsLabel.setVerticalAlignment(SwingConstants.TOP);
        lodgeDetailsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        thumbnailPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JScrollPane thumbnailScroll = new JScrollPane(thumbnailPanel);
        thumbnailScroll.setPreferredSize(new Dimension(300, 120));

        panel.add(thumbnailLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(thumbnailScroll); // Scrollable preview thumbnails
        panel.add(Box.createVerticalStrut(15));
        panel.add(lodgeDetailsLabel);

        return panel;
    }

    private void updateLodgeDetails(Lodging lodge) {
        lodgeDetailsLabel.setText("Loading lodge details...");

        // Clear existing previews
        thumbnailLabel.setIcon(null);
        thumbnailPanel.removeAll();
        thumbnailPanel.revalidate();
        thumbnailPanel.repaint();

        // Load basic lodge details immediately
        SwingUtilities.invokeLater(() -> {
            StringBuilder sb = new StringBuilder("<html><b>Lodge:</b> ").append(lodge.getName()).append("<br>")
                    .append("<b>Price/night:</b> $").append(String.format("%.2f", lodge.getPricePerNight())).append("<br>");

            if (lodge instanceof House h) {
                sb.append("<b>Bedrooms:</b> ").append(h.getNumberOfBedrooms()).append("<br>")
                        .append("<b>Has Pool:</b> ").append(h.hasPool() ? "Yes" : "No");
            } else if (lodge instanceof Hotel h) {
                sb.append("<b>Rooms:</b> ").append(h.getNumberOfRooms()).append("<br>")
                        .append("<b>Has Gym:</b> ").append(h.hasGym() ? "Yes" : "No");
            }

            lodgeDetailsLabel.setText(sb.append("</html>").toString());
        });

        // Load images from the database in background
        new Thread(() -> {
            Connection conn = DatabaseManager.getConnection();

            lodge.loadImagesFromDatabase(conn); 

            List<ImageIcon> images = lodge.getImages();
            if (!images.isEmpty()) {
                SwingUtilities.invokeLater(()
                        -> thumbnailLabel.setIcon(getScaledIcon(images.get(0), 200, 200))
                );

                for (int i = 0; i < images.size(); i++) {
                    final int imgIndex = i;
                    ImageIcon original = images.get(imgIndex);
                    ImageIcon thumb = getScaledIcon(original, 80, 80);

                    final JLabel thumbLabel = new JLabel(thumb);
                    thumbLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    thumbLabel.addMouseListener(new MouseAdapter() {
                        public void mouseClicked(MouseEvent e) {
                            openCarouselWindow(images, imgIndex);
                        }
                    });

                    SwingUtilities.invokeLater(() -> {
                        thumbnailPanel.add(thumbLabel);
                        thumbnailPanel.revalidate();
                        thumbnailPanel.repaint();
                    });
                }
            } else {
                SwingUtilities.invokeLater(() -> thumbnailLabel.setIcon(null));
            }
        }).start();
    }
   
    // *********************************** new method on June 14 2025 *************************************
    // Update lodge list after reloading lodgings
    private void updateLodgeList(List<Lodging> lodgings) {
        DefaultListModel<String> model = new DefaultListModel<>();
        for (Lodging lodge : lodgings) {
            model.addElement(lodge.getName());
        }
        lodgeList.setModel(model);
        thumbnailLabel.setIcon(null);
        lodgeDetailsLabel.setText("Select a lodge to view details.");
        thumbnailPanel.removeAll();
        thumbnailPanel.revalidate();
        thumbnailPanel.repaint();
    }
    // ******************************************************************************************************
    
    // method to scale image proportionally
    private ImageIcon getScaledIcon(ImageIcon icon, int maxWidth, int maxHeight) {
        Image img = icon.getImage();
        int width = img.getWidth(null);
        int height = img.getHeight(null);

        if (width <= 0 || height <= 0) {
            return icon;
        }

        double widthRatio = (double) maxWidth / width;
        double heightRatio = (double) maxHeight / height;
        double scaleRatio = Math.min(widthRatio, heightRatio);

        int scaledWidth = (int) (width * scaleRatio);
        int scaledHeight = (int) (height * scaleRatio);

        Image scaledImg = img.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImg);
    }
    
    private void updateImage(JDialog dialog, List<ImageIcon> images, int imageIndex, JLabel imageLabel, JPanel navPanel) {
        int maxWidth = dialog.getContentPane().getWidth();
        int maxHeight = dialog.getContentPane().getHeight() - navPanel.getHeight();

        ImageIcon originalIcon = images.get(imageIndex);
        ImageIcon scaledIcon = getScaledIcon(originalIcon, maxWidth, maxHeight);
        imageLabel.setIcon(scaledIcon);
    }
    
    private void openCarouselWindow(List<ImageIcon> images, int startIndex) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Image Carousel", true);
        dialog.setSize(800, 600);
        dialog.setLayout(new BorderLayout());

        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        dialog.add(imageLabel, BorderLayout.CENTER);

        JButton prev = new JButton("Previous");
        JButton next = new JButton("Next");

        JPanel navPanel = new JPanel();
        navPanel.add(prev);
        navPanel.add(next);
        dialog.add(navPanel, BorderLayout.SOUTH);

        final int[] index = {startIndex};

        dialog.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                updateImage(dialog, images, index[0], imageLabel, navPanel);
            }
        });

        prev.addActionListener(e -> {
            index[0] = (index[0] - 1 + images.size()) % images.size();
            updateImage(dialog, images, index[0], imageLabel, navPanel);
        });

        next.addActionListener(e -> {
            index[0] = (index[0] + 1) % images.size();
            updateImage(dialog, images, index[0], imageLabel, navPanel);
        });

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private JPanel createDatePanel() {
        JPanel panel = new JPanel(new GridLayout(2, 4, 10, 10));
        panel.setOpaque(false);

        checkInBoxes = createDatePickers(panel, "Check-in:");
        checkOutBoxes = createDatePickers(panel, "Check-out:");

        return panel;
    }

    private JComboBox<String>[] createDatePickers(JPanel panel, String label) {
        panel.add(new JLabel(label));

        JComboBox<String> monthBox = new JComboBox<>(getMonthNames());
        JComboBox<String> dayBox = new JComboBox<>();
        JComboBox<String> yearBox = new JComboBox<>();

        for (int year = 2023; year <= 2030; year++) yearBox.addItem(String.valueOf(year));
        updateDaysCombo(monthBox, yearBox, dayBox);

        monthBox.addActionListener(e -> updateDaysCombo(monthBox, yearBox, dayBox));
        yearBox.addActionListener(e -> updateDaysCombo(monthBox, yearBox, dayBox));

        panel.add(monthBox);
        panel.add(dayBox);
        panel.add(yearBox);

        return new JComboBox[]{monthBox, dayBox, yearBox};
    }

    private void updateDaysCombo(JComboBox<String> monthBox, JComboBox<String> yearBox, JComboBox<String> dayBox) {
        int month = monthBox.getSelectedIndex();
        int year = Integer.parseInt((String) yearBox.getSelectedItem());

        int days = new GregorianCalendar(year, month, 1).getActualMaximum(Calendar.DAY_OF_MONTH);
        dayBox.removeAllItems();
        for (int i = 1; i <= days; i++) dayBox.addItem(String.valueOf(i));
    }

    private String[] getMonthNames() {
        return new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);

        CustomButton selectButton = new CustomButton("Select Lodge & Date");
        CustomButton reportButton = new CustomButton("Get Report");
        CustomButton backButton = new CustomButton("Back to Login");

        CustomButton refreshButton = new CustomButton("Refresh Lodgings");
        refreshButton.addActionListener(e -> {
            List<Lodging> lodgings = agency.reloadLodgings(); 
            updateLodgeList(lodgings);                         
        });

        selectButton.addActionListener(e -> handleBooking());
        reportButton.addActionListener(e -> handleReportGeneration());
        backButton.addActionListener(e -> agency.setState(new Login(agency)));

        // Add buttons to panel
        panel.add(selectButton);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(reportButton);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(refreshButton); 
        panel.add(Box.createHorizontalStrut(10));
        panel.add(backButton);

        return panel;
    }
    
    private void handleBooking() {
        int index = lodgeList.getSelectedIndex();
        if (index == -1 || checkInBoxes[1].getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please select a lodge and valid dates.");
            return;
        }

        LocalDate checkIn = getDateFromComboBox(checkInBoxes);
        LocalDate checkOut = getDateFromComboBox(checkOutBoxes);

        if (!checkOut.isAfter(checkIn)) {
            JOptionPane.showMessageDialog(this, "Check-out must be after check-in.");
            return;
        }

        Lodging lodge = agency.getLodgings().get(index);

        // Check if the user is logged in
        String customerId = (agency.getCurrentCustomer() != null) ? agency.getCurrentCustomer().getIdNumber() : null;
        currentOrder = new Order(lodge, checkIn, checkOut, customerId);

        double cost = currentOrder.calculateTotalCost();
        int confirm = JOptionPane.showConfirmDialog(this,
                "Total: $" + String.format("%.2f", cost) + "\nConfirm booking?",
                "Confirm Booking", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this, "Booking confirmed!");

            if (customerId != null) {
                agency.getOrdersFile().saveOrderToDatabase(currentOrder);   // Only save for logged-in customers
            } else {
                JOptionPane.showMessageDialog(this,
                        "As a guest, your booking is not saved for future reference.\nCreate an account to view order history.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Booking canceled.");
        }
    }
    
    private void handleReportGeneration() {
        Customer customer = agency.getCurrentCustomer();
        if (customer == null) {
            JOptionPane.showMessageDialog(this,
                    "You must create an account and be logged in to view order history reports.",
                    "Login Required",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        LocalDate startDate = getDateFromComboBox(checkInBoxes);
        LocalDate endDate = getDateFromComboBox(checkOutBoxes);

        if (!endDate.isAfter(startDate)) {
            JOptionPane.showMessageDialog(this, "End date must be after start date.");
            return;
        }

        String start = startDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        String end = endDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));

        List<Order> orders = agency.getOrdersFile()
                .getOrdersForCustomer(customer.getIdNumber())
                .stream()
                .filter(o -> !o.getEndDate().isBefore(startDate) && !o.getStartDate().isAfter(endDate))
                .toList();

        if (orders.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No orders found in selected date range.");
            return;
        }

        File report = ReportGenerator.saveCustomerReport(customer.getName(), orders, start, end);

        if (report != null && report.exists()) {
            try {
                Desktop.getDesktop().browse(report.toURI()); // Open in browser
                JOptionPane.showMessageDialog(this,
                        "Report saved and opened in your default browser:\n" + report.getAbsolutePath(),
                        "Report Created",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error opening report in browser.");
            }
        }
    }

    private LocalDate getDateFromComboBox(JComboBox<String>[] boxes) {
        int month = boxes[0].getSelectedIndex() + 1;
        int day = Integer.parseInt((String) boxes[1].getSelectedItem());
        int year = Integer.parseInt((String) boxes[2].getSelectedItem());
        return LocalDate.of(year, month, day);
    }

    private void styleLabel(JLabel label, int size, int style) {
        label.setFont(new Font("Arial", style, size));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setForeground(Color.WHITE);
    }
}
