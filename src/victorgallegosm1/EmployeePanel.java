/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package victorgallegosm1;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.ImageIcon;
import java.util.List;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
/**
 *
 * @author 12546
 */
public class EmployeePanel extends JPanel {
    private final TravelAgency agency;
    private JList<String> lodgeList;
    private JTextArea lodgeDisplayArea;
    private CustomButton addButton, removeButton, editButton, listButton, logoutButton, reportButton, addImagesButton;
    private CustomLabel statusLabel;
    private JLabel connectionStatusLabel;  // Shows DB connection status
    private JProgressBar uploadProgressBar;
    private JComboBox<String> startMonthBox, endMonthBox;
    private JComboBox<Integer> startDayBox, endDayBox;
    private JPanel thumbnailPanel;
    private JLabel mainThumbnailLabel;
    
    public EmployeePanel(TravelAgency agency) {
        this.agency = agency;
        setLayout(new BorderLayout());
        setBackground(new Color(240, 248, 255)); // Panel background
        setupButtons();
        setupLodgeListAndDisplay();
        setupDatePickersAndStatus();
        setupEventHandlers();
    }
    
    private void setupButtons() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(new Color(230, 230, 250));
        Font font = new Font("Arial", Font.BOLD, 16);
        Dimension size = new Dimension(160, 40);
        
        addButton = createButton("Add Lodge", font, size);
        removeButton = createButton("Remove Lodge", font, size);
        editButton = createButton("Edit Lodge", font, size);
        listButton = createButton("List Lodges", font, size);
        logoutButton = createButton("Back to Login", font, size);
        addImagesButton = createButton("Add Images", font, size); // New Button
        
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(editButton);
        buttonPanel.add(listButton);
        buttonPanel.add(addImagesButton); // Add the new button 
        buttonPanel.add(logoutButton);
        add(buttonPanel, BorderLayout.NORTH);
    }
    
    private void setupLodgeListAndDisplay() {
        JPanel centerPanel = new JPanel(new BorderLayout()); 
        lodgeList = new JList<>();
        lodgeList.setFont(new Font("Arial", Font.PLAIN, 16));
        lodgeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane listScrollPane = new JScrollPane(lodgeList);
        listScrollPane.setPreferredSize(new Dimension(200, 400));
        
        lodgeDisplayArea = new JTextArea();
        lodgeDisplayArea.setEditable(false);
        lodgeDisplayArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        lodgeDisplayArea.setMargin(new Insets(10, 10, 10, 10));
        JScrollPane detailsScrollPane = new JScrollPane(lodgeDisplayArea);
        
        // ********************* for image preview ************************************************
        thumbnailPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JScrollPane thumbnailScrollPane = new JScrollPane(thumbnailPanel);
        thumbnailScrollPane.setPreferredSize(new Dimension(200, 100));
        mainThumbnailLabel = new JLabel("Main Preview");
        mainThumbnailLabel.setPreferredSize(new Dimension(200, 200));
        mainThumbnailLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        mainThumbnailLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Add thumbnails to the right of lodgeDisplayArea
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.add(mainThumbnailLabel);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(thumbnailScrollPane);
        centerPanel.add(listScrollPane, BorderLayout.WEST);
        centerPanel.add(detailsScrollPane, BorderLayout.CENTER);
        centerPanel.add(rightPanel, BorderLayout.EAST); 
        // *******************************************************************************************
        
        add(centerPanel, BorderLayout.CENTER);
    }
    
    private void setupDatePickersAndStatus() {
        JPanel southPanel = new JPanel(new BorderLayout());
        
        // ---------- Status and Progress Bar ----------
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusLabel = new CustomLabel("Ready.");
        statusLabel.setForeground(new Color(64, 64, 122));
        connectionStatusLabel = new JLabel("Checking database connection...");
        connectionStatusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        connectionStatusLabel.setForeground(Color.DARK_GRAY);
        
        // Stack both labels vertically
        JPanel labelStack = new JPanel();
        labelStack.setLayout(new BoxLayout(labelStack, BoxLayout.Y_AXIS));
        labelStack.setBackground(statusPanel.getBackground());
        labelStack.add(statusLabel);
        labelStack.add(connectionStatusLabel);
        // ***********************************************************************************
        
        uploadProgressBar = new JProgressBar();
        uploadProgressBar.setStringPainted(true);
        uploadProgressBar.setVisible(false); // Hidden until uploading
        statusPanel.add(labelStack, BorderLayout.NORTH); // Replaces single statusLabel
        statusPanel.add(uploadProgressBar, BorderLayout.SOUTH);
        southPanel.add(statusPanel, BorderLayout.NORTH);
        
        // ---------- Date Selectors and Report Button ----------
        JPanel datePanel = new JPanel();
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        startMonthBox = new JComboBox<>(months);
        endMonthBox = new JComboBox<>(months);
        startDayBox = new JComboBox<>(createDayList());
        endDayBox = new JComboBox<>(createDayList());
        reportButton = new CustomButton("Get Report");
        datePanel.add(new JLabel("Start:"));
        datePanel.add(startMonthBox);
        datePanel.add(startDayBox);
        datePanel.add(new JLabel("End:"));
        datePanel.add(endMonthBox);
        datePanel.add(endDayBox);
        datePanel.add(reportButton);
        southPanel.add(datePanel, BorderLayout.SOUTH);
        add(southPanel, BorderLayout.SOUTH);
        
        // Register listener
        DatabaseManager.addConnectionListener(connected -> SwingUtilities.invokeLater(() -> {
            if (connected) {
                connectionStatusLabel.setText("Database Connected");
                connectionStatusLabel.setForeground(new Color(0, 128, 0)); // Green
            } else {
                connectionStatusLabel.setText("Database Disconnected");
                connectionStatusLabel.setForeground(Color.RED);
            }
        }));

        // Immediately reflect current connection status
        if (DatabaseManager.isConnected()) {
            connectionStatusLabel.setText("Database Connected");
            connectionStatusLabel.setForeground(new Color(0, 128, 0));
        } else {
            connectionStatusLabel.setText("Database Disconnected");
            connectionStatusLabel.setForeground(Color.RED);
        }
    }
    
    private void setupEventHandlers() {
        addButton.addActionListener(e -> handleAddLodge());
        removeButton.addActionListener(e -> handleRemoveLodge());
        editButton.addActionListener(e -> handleEditLodge());
        listButton.addActionListener(e -> displayLodges());
        logoutButton.addActionListener(e -> agency.setState(new Login(agency)));
        addImagesButton.addActionListener(e -> handleAddImages()); // New event
        lodgeList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) displayLodgeDetails();
        });
        
        // reportButton.addActionListener(e -> statusLabel.setText("Report functionality coming soon."));
        // new reportbutton functionality ***********************************************************************
        reportButton.addActionListener(e -> {
            String startDate = getSelectedDate(startMonthBox, startDayBox);
            String endDate = getSelectedDate(endMonthBox, endDayBox);
            List<Customer> customers = agency.getCustomers(); 
            File reportFile = ReportGenerator.saveEmployeeReport(customers, startDate, endDate);
            try {
                Desktop.getDesktop().browse(reportFile.toURI());
                statusLabel.setText("Employee report opened.");
            } catch (IOException ex) {
                statusLabel.setText("Failed to open report.");
            }
        });
    }
    
    private void displayLodges() {
        DefaultListModel<String> model = new DefaultListModel<>();
        for (Lodging l : agency.getLodgings()) model.addElement(l.getName());
        lodgeList.setModel(model);
        statusLabel.setText("Lodges listed.");
    }
    
    private void displayLodgeDetails() {
        String selected = lodgeList.getSelectedValue();
        if (selected != null) {
            for (Lodging l : agency.getLodgings()) {
                if (l.getName().equals(selected)) {
                    lodgeDisplayArea.setText(l.toString()); // Show basic info immediately
                    thumbnailPanel.removeAll();             // Clear previous thumbnails
                    mainThumbnailLabel.setIcon(null);       // Clear main preview
                    thumbnailPanel.revalidate();
                    thumbnailPanel.repaint();
                    
                    // Load images in a background thread
                    new Thread(() -> {
                        try {
                            Connection conn = DatabaseManager.getConnection();
                            PreparedStatement stmt = conn.prepareStatement(
                                    "SELECT imageData FROM lodge_images WHERE lodgeId = ?"
                            );
                            stmt.setInt(1, l.getId());
                            ResultSet rs = stmt.executeQuery();
                            l.clearImages(); // clear previously loaded images to avoid duplication
                            while (rs.next()) {
                                byte[] bytes = rs.getBytes("imageData");
                                ImageIcon icon = new ImageIcon(bytes);
                                l.addImage(icon);
                                
                                SwingUtilities.invokeLater(() -> {
                                    JLabel thumbLabel = new JLabel(getScaledIcon(icon, 80, 80));
                                    thumbLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                                    int index = l.getImages().indexOf(icon); // Needed for carousel
                                    thumbLabel.addMouseListener(new MouseAdapter() {
                                        public void mouseClicked(MouseEvent e) {
                                            openCarousel(l.getImages(), index);
                                        }
                                    });
                                    thumbnailPanel.add(thumbLabel);
                                    thumbnailPanel.revalidate();
                                    thumbnailPanel.repaint();
                                });
                            }
                            // Set main thumbnail preview if at least one image is loaded
                            if (!l.getImages().isEmpty()) {
                                ImageIcon main = getScaledIcon(l.getImages().get(0), 200, 200);
                                SwingUtilities.invokeLater(() -> mainThumbnailLabel.setIcon(main));
                            }
                        } catch (SQLException e) {
                            SwingUtilities.invokeLater(() -> statusLabel.setText("Error loading images."));
                            e.printStackTrace();
                        }
                    }).start();
                    break;
                }
            }
        }
    }
    
    private void handleAddLodge() {
        String[] options = {"House", "Hotel"};
        int choice = JOptionPane.showOptionDialog(this, "Choose Lodge Type", "Add Lodge",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        if (choice == 0 || choice == 1) {
            JTextField nameField = new JTextField();
            JTextField priceField = new JTextField();
            JTextField numberField = new JTextField();
            JCheckBox featureBox = new JCheckBox(choice == 0 ? "Has Pool" : "Has Gym");
            Object[] input = {
                    "Name:", nameField,
                    "Price per Night:", priceField,
                    choice == 0 ? "Number of Bedrooms:" : "Number of Rooms:", numberField,
                    featureBox
            };
            int ok = JOptionPane.showConfirmDialog(this, input, "Add " + options[choice], JOptionPane.OK_CANCEL_OPTION);
            if (ok == JOptionPane.OK_OPTION) {
                try {
                    String name = nameField.getText().trim();
                    double price = Double.parseDouble(priceField.getText());
                    int num = Integer.parseInt(numberField.getText());
                    boolean feature = featureBox.isSelected();
                    Lodging lodge = (choice == 0)
                            ? new House(name, price, num, feature)
                            : new Hotel(name, price, num, feature);
                    
                    agency.getLodgingsFile().addLodging(lodge);  // Triggers save
                    displayLodges();
                    statusLabel.setText(options[choice] + " added: " + name);
                } catch (NumberFormatException ex) {
                    statusLabel.setText("Invalid input. Check number fields.");
                    statusLabel.setForeground(Color.RED);
                }
            }
        }
    }
    
    private void handleRemoveLodge() {
        String selected = lodgeList.getSelectedValue();
        if (selected == null) {
            statusLabel.setText("Select a lodge to remove.");
            return;
        }
        List<Lodging> lodges = agency.getLodgings();
        for (int i = 0; i < lodges.size(); i++) {
            if (lodges.get(i).getName().equals(selected)) {
                agency.getLodgingsFile().removeLodging(i); // Includes image cleanup
                displayLodges();
                statusLabel.setText("Lodge removed: " + selected);
                return;
            }
        }
    }
    
    private void handleEditLodge() {
        String selected = lodgeList.getSelectedValue();
        if (selected == null) {
            statusLabel.setText("Select a lodge to edit.");
            return;
        }
        List<Lodging> lodges = agency.getLodgings();
        for (int i = 0; i < lodges.size(); i++) {
            Lodging lodge = lodges.get(i);
            if (lodge.getName().equals(selected)) {
                JTextField nameField = new JTextField(lodge.getName());
                JTextField priceField = new JTextField(String.valueOf(lodge.getPricePerNight()));
                Object[] input = {
                    "Name:", nameField,
                    "Price per Night:", priceField
                };
                int ok = JOptionPane.showConfirmDialog(this, input, "Edit Lodge", JOptionPane.OK_CANCEL_OPTION);
                if (ok == JOptionPane.OK_OPTION) {
                    try {
                        String newName = nameField.getText().trim();
                        double newPrice = Double.parseDouble(priceField.getText());
                        Lodging updatedLodge;
                        if (lodge instanceof House) {
                            House h = (House) lodge;
                            updatedLodge = new House(newName, newPrice, h.getNumberOfBedrooms(), h.hasPool());
                        } else if (lodge instanceof Hotel) {
                            Hotel h = (Hotel) lodge;
                            updatedLodge = new Hotel(newName, newPrice, h.getNumberOfRooms(), h.hasGym());
                        } else {
                            throw new IllegalStateException("Unknown lodging type.");
                        }
                        agency.getLodgingsFile().editLodging(i, updatedLodge);
                        displayLodges();
                        statusLabel.setText("Lodge updated: " + newName);
                    } catch (NumberFormatException ex) {
                        statusLabel.setText("Invalid price entered.");
                        statusLabel.setForeground(Color.RED);
                    }
                }
                return;
            }
        }
    }
    
    private void handleAddImages() {
        String selected = lodgeList.getSelectedValue();
        if (selected == null) {
            statusLabel.setText("Select a lodge to add images.");
            return;
        }
        Lodging foundLodge = null;
        for (Lodging l : agency.getLodgings()) {
            if (l.getName().equals(selected)) {
                foundLodge = l;
                break;
            }
        }
        if (foundLodge == null) {
            statusLabel.setText("Lodge not found.");
            return;
        }
        final Lodging targetLodge = foundLodge;  
        JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(true);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files (JPG, PNG)", "jpg", "jpeg", "png");
        chooser.setFileFilter(filter);
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File[] files = chooser.getSelectedFiles();
            if (files.length == 0) {
                statusLabel.setText("No images selected.");
                return;
            }
            
            uploadProgressBar.setMinimum(0);
            uploadProgressBar.setMaximum(files.length);
            uploadProgressBar.setValue(0);
            uploadProgressBar.setVisible(true);
            statusLabel.setText("Uploading images...");
            
            new Thread(() -> {
                int count = 0;
                for (File file : files) {
                    if (file.exists()) {
                        ImageIcon icon = new ImageIcon(file.getAbsolutePath());
                        icon.setDescription(file.getAbsolutePath());
                        
                        targetLodge.addImage(icon);
                        count++;
                        final int progress = count;
                        
                        SwingUtilities.invokeLater(() -> {
                            // Create a thumbnail JLabel for this image
                            JLabel thumbLabel = new JLabel(getScaledIcon(icon, 80, 80));
                            thumbLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                            int index = targetLodge.getImages().indexOf(icon);
                            thumbLabel.addMouseListener(new MouseAdapter() {
                                @Override
                                public void mouseClicked(MouseEvent e) {
                                    openCarousel(targetLodge.getImages(), index);
                                }
                            });
                            
                            // Add the thumbnail to the thumbnail panel
                            thumbnailPanel.add(thumbLabel);
                            thumbnailPanel.revalidate();
                            thumbnailPanel.repaint();
                            
                            // Update progress bar
                            uploadProgressBar.setValue(progress);
                            uploadProgressBar.setString("Uploaded " + progress + " of " + files.length);
                        });
                    }
                }
                
                agency.getLodgingsFile().save();
                SwingUtilities.invokeLater(() -> {
                    statusLabel.setText("Images added to: " + targetLodge.getName());
                    uploadProgressBar.setVisible(false);
                    if (!targetLodge.getImages().isEmpty()) {
                        ImageIcon mainIcon = getScaledIcon(targetLodge.getImages().get(0), 200, 200);
                        mainThumbnailLabel.setIcon(mainIcon);
                    }
                });
            }).start();
        } else {
            statusLabel.setText("Image selection canceled.");
        }
    }
    
    // **************** for carousel ************************************************************************
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
    // *****************************************************************************************************
    
    private void updateImage(JDialog dialog, List<ImageIcon> images, int imageIndex, JLabel imageLabel, JPanel navPanel) {
        int maxWidth = dialog.getContentPane().getWidth();
        int maxHeight = dialog.getContentPane().getHeight() - navPanel.getHeight();
        
        ImageIcon originalIcon = images.get(imageIndex);
        ImageIcon scaledIcon = getScaledIcon(originalIcon, maxWidth, maxHeight);
        imageLabel.setIcon(scaledIcon);
    }
    
    private void openCarousel(List<ImageIcon> images, int startIndex) {
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
                updateImage();
            }
            private void updateImage() {
                int maxWidth = dialog.getContentPane().getWidth();
                int maxHeight = dialog.getContentPane().getHeight() - navPanel.getHeight();
                ImageIcon originalIcon = images.get(index[0]);
                ImageIcon scaledIcon = getScaledIcon(originalIcon, maxWidth, maxHeight);
                imageLabel.setIcon(scaledIcon);
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
    
    private CustomButton createButton(String text, Font font, Dimension size) {
        CustomButton btn = new CustomButton(text);
        btn.setFont(font);
        btn.setPreferredSize(size);
        return btn;
    }
    
    private Integer[] createDayList() {
        Integer[] days = new Integer[31];
        for (int i = 1; i <= 31; i++) days[i - 1] = i;
        return days;
    }
    
    private String getSelectedDate(JComboBox<String> monthBox, JComboBox<Integer> dayBox) {
        String monthName = (String) monthBox.getSelectedItem();
        int day = (Integer) dayBox.getSelectedItem();
        int year = LocalDate.now().getYear(); // Use current year
        // Convert short month name like "Jan" to a number (1-12)
        DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("MMM", Locale.ENGLISH);
        Month monthEnum = Month.from(inputFormat.parse(monthName));
        int month = monthEnum.getValue();
        // Format as MM/dd/yyyy
        return String.format("%02d/%02d/%d", month, day, year);
    }
}