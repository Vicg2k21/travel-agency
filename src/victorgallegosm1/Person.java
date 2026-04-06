/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package victorgallegosm1;
/**
 *
 * @author 12546
 */
public class Person {
    private String name;
    private String phoneNumber;
    private String address;
    private String idNumber;

    // Constructor
    public Person(String name, String phoneNumber, String address, String idNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.idNumber = idNumber;
    }

    // Getter and Setter methods for Person class
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    // Override toString() to return a string representation of the Person
    @Override
    public String toString() {
        return "Name: " + name + "\nPhone: " + phoneNumber + "\nAddress: " + address + "\nID Number: " + idNumber;
    }
}
