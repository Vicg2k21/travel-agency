/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package victorgallegosm1;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author 12546
 */
public abstract class State {

    protected TravelAgency agency;
    protected static JFrame mainFrame;
    protected JPanel currentPanel;

    public State(TravelAgency agency) {
        this.agency = agency;

        // If mainFrame is null, create a new JFrame
        if (mainFrame == null) {
            mainFrame = new JFrame("Travel Agency");
            mainFrame.setSize(1280, 720);
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.setLocationRelativeTo(null); 
            mainFrame.setLayout(new CardLayout()); // CardLayout to switch between panels
            mainFrame.setVisible(true);  // Ensure frame is visible
        }
    }

    public abstract void enter();

    public abstract void update();

    // method to switch panels
    protected void switchPanel(JPanel newPanel) {
        CardLayout cardLayout = (CardLayout) mainFrame.getContentPane().getLayout();

        // If the current panel is already the same, do nothing
        if (currentPanel != newPanel) {
            // Remove the previous panel
            mainFrame.getContentPane().removeAll(); // Clear existing panels

            // Add the new panel
            mainFrame.getContentPane().add(newPanel, newPanel.getName());
            cardLayout.show(mainFrame.getContentPane(), newPanel.getName()); // Show the new panel
            currentPanel = newPanel;  

            // Revalidate and repaint to reflect the layout change
            mainFrame.revalidate();
            mainFrame.repaint();
        }
    }
}
