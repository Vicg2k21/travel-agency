/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package victorgallegosm1;

/**
 *
 * @author 12546
 */
public class PasswordEncryptor {

    public static String encrypt(String password) {
        StringBuilder encrypted = new StringBuilder();

        for (int i = 0; i < password.length(); i++) {
            char originalChar = password.charAt(i);
            char shiftedChar = (char) (originalChar + 4); // Step 1: Shift character forward
            char distractor = (char) ((originalChar * 2) % 94 + 33); // Step 2: Generate distractor

            encrypted.append(shiftedChar).append(distractor); // Step 3: Add both to encrypted string
        }

        return encrypted.reverse().toString(); // Step 4: Reverse the string
    }

    public static String decrypt(String encryptedPassword) {
        StringBuilder reversed = new StringBuilder(encryptedPassword).reverse(); // Step 1: Reverse string
        StringBuilder decrypted = new StringBuilder();

        // Step 2: Extract every second character (ignoring distractors)
        for (int i = 0; i < reversed.length(); i += 2) {
            char originalChar = (char) (reversed.charAt(i) - 4); // Step 3: Reverse shift
            decrypted.append(originalChar);
        }

        return decrypted.toString();
    }

}
