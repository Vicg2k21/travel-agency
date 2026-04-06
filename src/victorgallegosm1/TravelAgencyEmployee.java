/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package victorgallegosm1;
/**
 *
 * @author 12546
 */
public class TravelAgencyEmployee {
    private String name;
    private String password;
    private boolean isManager;

    public TravelAgencyEmployee(String name, String password, boolean isManager) {
        this.name = name;
        this.password = password;
        this.isManager = isManager;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public boolean isManager() {
        return isManager;
    }

    public void setManager(boolean manager) {
        isManager = manager;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
}
