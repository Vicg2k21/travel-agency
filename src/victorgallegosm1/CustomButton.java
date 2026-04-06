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
public class CustomButton extends JButton { 
    public CustomButton(String text) {
        super(text);
        setBackground(new Color(0, 0, 139)); // Dark blue color
        setForeground(Color.WHITE);           // White text
        setFont(new Font("Arial", Font.BOLD, 25));
        setFocusPainted(false);
    }
}
