/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package victorgallegosm1;

import javax.swing.*;

/**
 *
 * @author 12546
 */
public class VictorGallegosM1 {

    public static void main(String[] args) {
        TravelAgency agency = new TravelAgency();

        agency.getCurrentState().enter(); 

        // After setting the initial state, ensure the frame is visible
        State.mainFrame.setVisible(true);
    }
}