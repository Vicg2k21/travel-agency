/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package victorgallegosm1;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author 12546
 */
public class TravelAgency {
    private EmployeesFile employeesFile;
    private CustomersFile customersFile;
    private LodgingFile lodgingsFile;
    private OrdersFile ordersFile; 

    private State currentState;
    private Customer currentCustomer; // Store the logged-in customer

    public TravelAgency() {
        employeesFile = new EmployeesFile("employees.txt");
        customersFile = new CustomersFile("customers.txt");
        lodgingsFile = new LodgingFile("lodgings.txt");
        ordersFile = new OrdersFile(this); 

        // ****************** needed to comment out the load() methods for all these class files ***************
        // to enable lazy loading
        // employeesFile.load();
        // customersFile.load();
        // lodgingsFile.load();
        // *************************************************************************************************

        currentState = new Login(this);
    }
    
    public List<Lodging> reloadLodgings() {
        lodgingsFile.load();  // Reloads from DB or file
        return lodgingsFile.getLodgings();  // Return updated list
    }

    // Getters/Setters
    public Customer getCurrentCustomer() {
        return currentCustomer;
    }

    public void setCurrentCustomer(Customer currentCustomer) {
        this.currentCustomer = currentCustomer;
    }

    public State getCurrentState() {
        return currentState;
    }

    public void setState(State newState) {
        currentState = newState;
        currentState.enter();
    }

    public List<TravelAgencyEmployee> getEmployees() {
        return employeesFile.getEmployees();
    }

    public List<Customer> getCustomers() {
        return customersFile.getCustomers();
    }

    public List<Lodging> getLodgings() {
        return lodgingsFile.getLodgings();
    }

    public EmployeesFile getEmployeesFile() {
        return employeesFile;
    }

    public CustomersFile getCustomersFile() {
        return customersFile;
    }

    public LodgingFile getLodgingsFile() {
        return lodgingsFile;
    }

    public OrdersFile getOrdersFile() {
        return ordersFile;
    }
}
