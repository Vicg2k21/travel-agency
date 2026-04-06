/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package victorgallegosm1;

/**
 *
 * @author 12546
 */
public class OrderDetails {
    double balanceOwed;
    String date;
    
    // Default Constructors
    public OrderDetails(){
        balanceOwed = 0.0;
        date = "Not Specified";
    }
    
    public OrderDetails(double balanceOwed, String date){
        this. balanceOwed = balanceOwed;
        this.date = date;
    }
    
    @Override
    public String toString(){
        return "Balance Owed: $" + String.format("%.2f", balanceOwed) + "\nDate: " 
                + date;
    }
}