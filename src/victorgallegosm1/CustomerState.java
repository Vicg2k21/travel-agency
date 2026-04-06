/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package victorgallegosm1;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author 12546
 */
public class CustomerState extends State {

    private CustomerPanel customerPanel;

    public CustomerState(TravelAgency agency) {
        super(agency);
    }

    @Override
    public void enter() {
        // Create the CustomerPanel and switch to it
        customerPanel = new CustomerPanel(agency);
        switchPanel(customerPanel);  // Switch to the CustomerPanel view
    }

    @Override
    public void update() {
        
    }
}