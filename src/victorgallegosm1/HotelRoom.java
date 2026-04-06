/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package victorgallegosm1;

/**
 *
 * @author 12546
 */
public class HotelRoom {
    private int roomNumber;
    private boolean hasValetParking;
    
    // Default Constructors
    public HotelRoom(){
        roomNumber = 0;
        hasValetParking = false;
    }
    
    public HotelRoom(int roomNumber, boolean hasValetParking){
        this.roomNumber = roomNumber;
        this.hasValetParking = hasValetParking;
    }
    
    // Getter
    public int getRoomNumber(){
        return roomNumber;
    }
    
    // Setter
    public void setRoomNumber(int roomNumber){
        this.roomNumber = roomNumber;
    }
    
    // Getter
    public boolean getHasValetParking(){
        return hasValetParking;
    }
    
    // Setter
    public void setHasValetParking(boolean hasValetParking){
        this.hasValetParking = hasValetParking;
    }

    @Override
    public String toString(){
        return "Room Number: " + roomNumber + "\nHas Valet Parking: " + hasValetParking;
    }
}