/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package victorgallegosm1;
import java.util.Scanner;
import java.util.ArrayList;

/**
 *
 * @author 12546
 */
public class ManagerState extends State {

    private Scanner scanner;

    public ManagerState(TravelAgency agency) {
        super(agency);
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void enter() {
        System.out.println("Entering Manager State");
    }

    @Override
    public void update() {
        System.out.println("1. Add Employee");
        System.out.println("2. Remove Employee");
        System.out.println("3. Edit Employee");
        System.out.println("4. List Employees");
        System.out.println("5. Make Employee a Manager");
        System.out.println("6. Manage Lodges");
        System.out.println("7. Log out");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                addEmployee();
                break;
            case "2":
                removeEmployee();
                break;
            case "3":
                editEmployee();
                break;
            case "4":
                listEmployees();
                break;
            case "5":
                promoteEmployee();
                break;
            case "6":
                agency.setState(new EmployeeState(agency));
                break;
            case "7":
                agency.setState(new Login(agency));
                break;
            default:
                System.out.println("Invalid option.");
        }
    }

    private void addEmployee() {
        System.out.print("Enter new employee name: ");
        String name = scanner.nextLine();
        System.out.print("Enter new employee password: ");
        String password = scanner.nextLine();
        TravelAgencyEmployee newEmployee = new TravelAgencyEmployee(name, password, false);
        agency.getEmployees().add(newEmployee);
        System.out.println("Employee added.");
    }

    private void removeEmployee() {
        System.out.println("Select employee to remove:");
        for (int i = 0; i < agency.getEmployees().size(); i++) {
            System.out.println(i + 1 + ". " + agency.getEmployees().get(i).getName());
        }
        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine()) - 1;
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            return;
        }

        if (choice >= 0 && choice < agency.getEmployees().size()) {
            System.out.println("Are you sure you want to remove " + agency.getEmployees().get(choice).getName() + "? (yes/no)");
            String confirmation = scanner.nextLine();
            if (confirmation.equalsIgnoreCase("yes")) {
                agency.getEmployeesFile().removeEmployee(choice);
                System.out.println("Employee removed.");
            } else {
                System.out.println("Removal canceled.");
            }
        } else {
            System.out.println("Invalid selection.");
        }
    }

    private void editEmployee() {
        System.out.println("Select employee to edit:");
        for (int i = 0; i < agency.getEmployees().size(); i++) {
            System.out.println(i + 1 + ". " + agency.getEmployees().get(i).getName());
        }
        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine()) - 1;
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            return;
        }

        if (choice >= 0 && choice < agency.getEmployees().size()) {
            TravelAgencyEmployee employee = agency.getEmployees().get(choice);

            System.out.println("Editing employee: " + employee.getName());
            System.out.print("Enter new name (leave blank to keep current): ");
            String newName = scanner.nextLine();
            if (!newName.isEmpty()) {
                employee.setName(newName);
            }

            System.out.print("Enter new password (leave blank to keep current): ");
            String newPassword = scanner.nextLine();
            if (!newPassword.isEmpty()) {
                employee.setPassword(newPassword);
            }

            System.out.print("Make this employee a manager? (yes/no, leave blank to keep current): ");
            String isManagerStr = scanner.nextLine();
            if (!isManagerStr.isEmpty()) {
                employee.setManager(isManagerStr.equalsIgnoreCase("yes"));
            }

            // Update the file with the new data
            agency.getEmployeesFile().editEmployee(choice, employee);
            System.out.println("Employee updated successfully.");
        } else {
            System.out.println("Invalid selection.");
        }
    }

    private void listEmployees() {
        for (int i = 0; i < agency.getEmployees().size(); i++) {
            System.out.println(i + 1 + ". " + agency.getEmployees().get(i).getName());
        }
    }

    private void promoteEmployee() {
        System.out.println("Select employee to promote:");
        for (int i = 0; i < agency.getEmployees().size(); i++) {
            System.out.println(i + 1 + ". " + agency.getEmployees().get(i).getName());
        }
        int choice = Integer.parseInt(scanner.nextLine()) - 1;
        if (choice >= 0 && choice < agency.getEmployees().size()) {
            TravelAgencyEmployee employee = agency.getEmployees().get(choice);
            if (employee.isManager()) {
                System.out.println("This employee is already a manager.");
                return;
            }
            System.out.println("Are you sure you want to promote " + employee.getName() + " to manager? (yes/no)");
            String confirmation = scanner.nextLine();
            if (confirmation.equalsIgnoreCase("yes")) {
                employee.setManager(true);
                System.out.println("Employee promoted to manager.");
            } else {
                System.out.println("Promotion canceled.");
            }
        } else {
            System.out.println("Invalid selection.");
        }
    }
}
