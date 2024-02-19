package GestionEspaceDisque_et_Buffer;

import java.io.File;
import java.io.IOException;

/**
 * Class DBParams that represents the parameters of the database
 */
public class DBParams {
    public static String DBPath; // the path of the database
    public static int SGBDPageSize; //  the size of page
    public static int DMFileCount;   // number of file
    public static int NumFichier; 
    public static int FrameCount;  // the number of frame
    public static int PageFull;  // we assume that a page is full when the page only have PageFull bytes remaining
    public static int nbPageFile;  //number of page in a file

    // ----------------   Methods   ----------------

    /**
     * Method createFile that creates a file, if the file doesn't exist it will be created
     * @param : nothing
     * @return int : the number of the file
     */
    public static int createFile() {
        NumFichier = 0;
        String filePatheName;
        while (true) {
             
            filePatheName = DBPath + File.separator + "F" + NumFichier + ".data";
            
            File file = new File(filePatheName);
            if (!file.exists()) {
                try {
                    if (file.createNewFile()) {
                        System.out.println("####    the file has been created at the path : " + filePatheName + "    ####");
                    } else {
                        System.out.println("####   Error while creating the file !    ####");
                    }
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
                break;
            } else {
                NumFichier++;
            }

        }
        return NumFichier;
    }

}