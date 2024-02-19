package GestionEspaceDisque_et_Buffer.Classes_Testing;

import GestionEspaceDisque_et_Buffer.DiskManager;
import GestionEspaceDisque_et_Buffer.PageID;
import GestionEspaceDisque_et_Buffer.BufferManager;
import GestionEspaceDisque_et_Buffer.DBParams;
import GestionEspaceDisque_et_Buffer.Frame;

import java.io.File;
import java.nio.ByteBuffer;

/**
 * Class TestBufferManager that tests the buffer manager
 */
public class TestBufferManager {

    /**
     * ----------------   Main   ----------------
     * @param : args : arguments
     */
    public static void main(String[] args){
        DBParams.DBPath = ".." + File.separator + ".." + File.separator + "DB";
        DBParams.DMFileCount=4;
        DBParams.SGBDPageSize=4096;
        DBParams.FrameCount=2;

        DiskManager diskManager=DiskManager.getDiskManager();
        BufferManager bufferManager=BufferManager.getBufferManager();

        
        PageID page1=diskManager.AllocPage();
        bufferManager.getPage(page1);  //assign the page 0/0 to frame0  [(0,0)|(-1,-1)]
        
        PageID page2=diskManager.AllocPage();
        bufferManager.getPage(page2);  // assign the page 1/0 to frame1 [(0,0)|(1,0)]

        PageID page3=diskManager.AllocPage();   //the page 0/0 of the frame0 should be replaced by page 2/0   [(2,0)|(1,0)] 
        bufferManager.getPage(page3);
        bufferManager.getPage(page3);         //pinCount of page(2,0) should be 2     [(2,0)|(1,0)]

        PageID page4=diskManager.AllocPage();
        bufferManager.getPage(page4);   //assign the page (3,0) to the frame1   [(2,0)|(3,0)]

        bufferManager.freePage(page3, false);  //pinCount of page(2,0) should pass to 1
        bufferManager.freePage(page3, true);  //pinCount of page(2,0) should pass to 0 and the flagDirty to true

        Frame frame=bufferManager.getFrame(page3);     //get the frame0 whose page is (2,0)
        ByteBuffer buffWrite=frame.getByteBuffer();

        String dataToAppend = "this data should be appended to the file 2 page 0";
        byte[] dataBytes = dataToAppend.getBytes(); // convert string to a byte code
        buffWrite = ByteBuffer.wrap(dataBytes);    //  simulate that the content of page (2,0) was modified

        frame.setByteBuffer(buffWrite);  // change the content of frame

        bufferManager.flushAll();   // the content of frame0 should be written in page (2,0)  [(-1,-1)|(-1,-1)]
    
        bufferManager.displaySatetOfFrames();   //display the state of the frame


    }
}
