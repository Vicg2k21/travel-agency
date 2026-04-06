/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package victorgallegosm1;

/**
 *
 * @author 12546
 */
public class LodgingReview {
    String comments;
    int rating;
    
    // Default Contructors
    public LodgingReview(){
        comments = "No comments";
        rating  = 1;
    }
    
    public LodgingReview(String comments, int rating){
        this.comments = comments;
        this.rating = rating;
    }
    
    @Override
    public String toString(){
        return "Comments: " + comments + "\nRating: " + rating;
    }
}