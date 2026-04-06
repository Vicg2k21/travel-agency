/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package victorgallegosm1;

/**
 *
 * @author 12546
 */
public abstract class DataFile {
    protected String filename;

    public DataFile(String filename) {
        this.filename = filename;
    }

    public abstract void load();
    public abstract void save();
}
