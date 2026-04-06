/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package victorgallegosm1;

/**
 *
 * @author 12546
 */
public class Hotel extends Lodging {
    private int numberOfRooms;
    private boolean hasGym;

    public Hotel(String name, double pricePerNight, int numberOfRooms, boolean hasGym) {
        super(name, pricePerNight);
        this.numberOfRooms = numberOfRooms;
        this.hasGym = hasGym;
    }

    public int getNumberOfRooms() {
        return numberOfRooms;
    }
    
    public void setNumRooms(int numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public boolean hasGym() {
        return hasGym;
    }
    
    public void setHasGym(boolean hasGym) {
        this.hasGym = hasGym;
    }

    // Overriding toString() to display lodge details
    @Override
    public String toString() {
        return String.format("Hotel: %s\nPrice per Night: $%.2f\nNumber of Rooms: %d\nHas Gym: %b", 
                             getName(), getPricePerNight(), numberOfRooms, hasGym);
    }
}