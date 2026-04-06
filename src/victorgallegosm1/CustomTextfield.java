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
public class CustomTextfield extends JTextField {
    public CustomTextfield(int columns) {
        super(columns);
        setFont(new Font("Arial", Font.PLAIN, 20));
        setForeground(Color.BLACK);
        setBackground(Color.WHITE);
    }
}