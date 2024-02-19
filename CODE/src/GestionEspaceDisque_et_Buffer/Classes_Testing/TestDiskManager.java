package GestionEspaceDisque_et_Buffer.Classes_Testing;

import GestionEspaceDisque_et_Buffer.*;
import java.nio.ByteBuffer;

/**
 * Class TestDiskManager that tests the DiskManager class
 */
public class TestDiskManager {
    /**
     * ----------------   Main   ----------------
     * @param : args : arguments
     */
    public static void main(String[] args) {
        //initialise variables of database
        DBParams.DBPath="../../DB";
        DBParams.DMFileCount=4;
        DBParams.SGBDPageSize=4096;
        
        String reset = "\u001B[0m"; // Reset to default
        String bold = "\u001B[1m";  // Bold text

        DiskManager diskManager = DiskManager.getDiskManager(); // create only one instance of diskmanager

    // test for allocte ,desalocate and getCurrentCountAllocPages
        System.out.println(bold+"during allocation"+reset);
        for (int i = 0; i < 6; i++) { // alocate 6 pages
            diskManager.AllocPage();          
        }
       

        System.out.println(bold+"during desalocation "+reset); 
        diskManager.DeallocPage(new PageID(0, 0)); // desalocate page(file=0,page=0)
        diskManager.DeallocPage(new PageID(1,1)); // desalocate page(file=1,page=1)
        diskManager.DeallocPage(new PageID(3,3));  //it should send an error
        
        System.out.println(bold+"during second allocation "+reset);
        diskManager.AllocPage(); //allocate page(file=1,page=1)
        
        

        System.out.println(bold+"number of page allocated "+reset);
        int numberOfPageAllocated=diskManager.GetCurrentCountAllocPages();
        System.out.println(numberOfPageAllocated);  //it should return 5

        //get a globale view of the content of filePageId and desalocatedPage
        System.out.println(bold+"the content of filePageId and desalocatedPage : "+reset);
        System.out.println(diskManager.toString());

    //write the data to a file
        System.out.println(bold+"writing a message"+reset);
        String dataToAppend = "this data should be appended to the file 1 page 1";
        byte[] dataBytes = dataToAppend.getBytes(); // convert string to a byte code
        ByteBuffer buffWrite = ByteBuffer.wrap(dataBytes);
        PageID page=new PageID(1, 1);
        diskManager.WritePage(page, buffWrite);

    //read the data from the file
        System.out.println(bold+"reading a message"+reset);
        ByteBuffer buffRead = ByteBuffer.allocate(4096);
        buffRead = DiskManager.ReadPage(page, buffRead);
        System.out.println("was red "+DiskManager.readContentOfBuffer(buffRead));
    
    }
}
