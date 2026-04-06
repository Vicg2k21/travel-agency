/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package victorgallegosm1;

/**
 *
 * @author 12546
 */
public class House extends Lodging {
    private int numberOfBedrooms;
    private boolean hasPool;

    public House(String name, double pricePerNight, int numberOfBedrooms, boolean hasPool) {
        super(name, pricePerNight);
        this.numberOfBedrooms = numberOfBedrooms;
        this.hasPool = hasPool;
    }

    public int getNumberOfBedrooms() {
        return numberOfBedrooms;
    }

    public boolean hasPool() {
        return hasPool;
    }
    
    public void setHasPool(boolean hasPool) {
        this.hasPool = hasPool;
    }
    
    public void setNumberOfBedrooms(int numberOfBedrooms) {
        this.numberOfBedrooms = numberOfBedrooms;
    }

    // Overriding toString() to display lodge details
    @Override
    public String toString() {
        return String.format("House: %s\nPrice per Night: $%.2f\nNumber of Bedrooms: %d\nHas Pool: %b", 
                             getName(), getPricePerNight(), numberOfBedrooms, hasPool);
    }
}