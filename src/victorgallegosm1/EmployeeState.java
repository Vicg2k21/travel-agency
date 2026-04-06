/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package victorgallegosm1;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author 12546
 */
public class EmployeeState extends State {

    private EmployeePanel employeePanel;

    public EmployeeState(TravelAgency agency) {
        super(agency);
    }

    @Override
    public void enter() {
        // Initialize the EmployeePanel and switch to it
        employeePanel = new EmployeePanel(agency);
        switchPanel(employeePanel);  // Switch to the EmployeePanel view
    }

    @Override
    public void update() {
       
    }
}