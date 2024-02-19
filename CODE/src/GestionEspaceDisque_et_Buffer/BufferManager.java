package GestionEspaceDisque_et_Buffer;

import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * Class BufferManager that represents the buffer manager
 */
public class BufferManager {

    private static BufferManager bufferManager = new BufferManager(); 
    private ArrayList<Frame> listFrames = new ArrayList<Frame>();

    /**
     * ----------------   Constructor   ----------------
     * @param : nothing
     */
    private BufferManager() {
        for (int i = 0; i < DBParams.FrameCount; i++) {
            listFrames.add(new Frame());
            
        }
        
    }

    // ----------------   Methods   ----------------

    /**
     * Method getBufferManager that returns the buffer manager
     * @param : nothing
     * @return BufferManager : the buffer manager
     */
    public static BufferManager getBufferManager() {
        return bufferManager;
    }

    /**
     * Method getPage that returns the content of a page
     * @param : pageId : the id of the page
     * @return ByteBuffer : the content of the page
     */
    public ByteBuffer getPage(PageID pageId) {

        if (!pageId.isValid()) { // if the page is (-1,-1) we shouldn't read its content
            return null;
        }
        
        ByteBuffer Bf = ByteBuffer.allocate(DBParams.SGBDPageSize);
        //DiskManager diskManager = DiskManager.getDiskManager();

        // if the page already exist in a frame
        for (Frame frame : listFrames) { // increment the pincount of the frame if the frame contains the page
            if (frame.compareFrames(pageId)) {
                frame.addPinCount();
                frame.setByteBuffer(DiskManager.ReadPage(pageId, Bf)); // get the content of the page
                return Bf;
            }
        }

        for (Frame frame : listFrames) { // add the page to an empty frame
            if (frame.frameIsEmpty()) {
                frame.setPage(pageId);
                frame.addPinCount();
                frame.setByteBuffer(DiskManager.ReadPage(pageId, Bf));
                Bf = frame.getByteBuffer();
                return Bf;

            }
        }

        // otherwise we will replace the page of the frame with the smallest pincount
        Frame minFrame = getFrameWithSmallestPinCount();
        minFrame.setPage(pageId);
        minFrame.setPinCount(1);
        minFrame.setByteBuffer(DiskManager.ReadPage(pageId, Bf));
        Bf = minFrame.getByteBuffer();

        // we need to check if the dirty flag of the page that we will replace is 1
        return Bf;
    }

    /**
     * Method getFrameWithSmallestPinCount that returns the frame with the smallest pin count
     * @param : nothing
     * @return Frame : the frame with the smallest pin count
     */
    public Frame getFrameWithSmallestPinCount() { // on le met en public mais a voir avec Yanis
        if (listFrames.isEmpty()) {
            // La liste est vide, renvoyez une valeur par défaut ou effectuez une autre action appropriée.
            // Par exemple, vous pouvez renvoyer null ou générer une exception appropriée.
            return null;
        }
    
        Frame minFrame = listFrames.get(0);
        for (Frame frame : listFrames) {
            if (frame.getPinCount() < minFrame.getPinCount()) {
                minFrame = frame;
            }
        }
        System.out.println("minFrame : " + minFrame.getPageId() + " " + minFrame.getFlagDirty());
        if (minFrame.getFlagDirty()) {
            DiskManager diskManager = DiskManager.getDiskManager();
            diskManager.WritePage(minFrame.getPageId(), minFrame.getByteBuffer());
        }
    
        // Retournez le frame avec le pin count le plus bas
        return minFrame;
    }
    

    /**
     * Method freePage that frees a page
     * @param : pageId : the id of the page
     * @param : flagDirty : the flag dirty
     * @return void : nothing
     */
    public void freePage(PageID pageId, boolean flagDirty) {
        for (Frame frame : listFrames) { // We search the frame of the pageId
            if (frame.compareFrames(pageId)) { // If we find

                frame.setPinCount(frame.getPinCount() - 1); // Decrement PinCount
                
                if(frame.getFlagDirty()!=true){ //if the flag is true it should remain true
                    frame.setFlagDirty(flagDirty); // Set flagDirty
                }

                return; // Finish the function
            }
        }
    }

    /**
     * Method flushAll that resets all the frames and write the content of the frames whose flag dirty is true
     * @param : nothing
     * @return void : nothing
     */
    public void flushAll() {

        DiskManager diskManager = DiskManager.getDiskManager(); // Get diskManager

        for (Frame frame : listFrames) { // All the frame
            if (frame.getFlagDirty()) {
                diskManager.WritePage(frame.getPageId(), frame.getByteBuffer()); // write the buffer
            }
            frame.cleanFrame(); // Clean the frame;

        }
    }

    /**
     * Method getFrame that returns the frame of a page
     * @param : pageId : the id of the page
     * @return Frame : the frame of the page
     */
    public Frame getFrame(PageID pageId) {
        for (Frame frame : listFrames) {
            if (frame.getPageId().equals(pageId)) {
                return frame;
            }
        }
        return null;
    }

    /**
     * Method getByteBufferPage that returns the byte buffer of a page
     * @param : pageId : the id of the page
     * @return ByteBuffer : the byte buffer of the page
     */
    public ByteBuffer getByteBufferPage(PageID pageId) {
        for (Frame frame : listFrames) {
            if (frame.getPageId().equals(pageId)) { // if the page existe in the listFrame
                return frame.getByteBuffer();
            }
        }
        // System.out.println("for page : "+pageId.toString()+" we should get data from
        // file");

        return getPage(pageId); // otherwise we need to write the content of the page straight from the file
    }

    /**
     * Method displaySatetOfFrames that displays the state of the frames
     * @param : nothing
     * @return void : nothing
     */
    public void displaySatetOfFrames() { // just to check if everything work as expected
        for (int i = 0; i < DBParams.FrameCount; i++) {
            if (listFrames.get(i).getPageId().isValid()) {
                System.out.println("frame : " + i + " page : " + listFrames.get(i).getPageId().getFileIdx() + " "
                        + listFrames.get(i).getPageId().getPageIdx() + " pinCount : "
                        + listFrames.get(i).getPinCount());
                System.out.print(" content of the frame : ");
                System.out.println(DiskManager.readContentOfBuffer(listFrames.get(i).getByteBuffer()));
            }
        }
    }

}
