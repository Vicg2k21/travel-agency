/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package victorgallegosm1;
import java.util.Scanner;

/**
 *
 * @author 12546
 */
public class Login extends State {
    private LoginPanel loginPanel;

    public Login(TravelAgency agency) {
        super(agency);
    }

    @Override
    public void enter() {
        // When entering the Login state, create and display the login panel
        loginPanel = new LoginPanel(agency);
        switchPanel(loginPanel); // Switch to the LoginPanel view
    }

    @Override
    public void update() {
        
    }
}