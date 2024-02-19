package GestionEspaceDisque_et_Buffer;
import java.nio.ByteBuffer;

/**
 * Class Frame that represents a frame
 */
public class Frame {

    private ByteBuffer byteBuff;
    private PageID pageId;
    private int pinCount ;
    private boolean flagDirty;

    /**
     * ----------------   Constructor   ----------------
     * @param : nothing
     */
    public Frame() {
        this.byteBuff =null;
        this.pageId = new PageID();
        this.pinCount = 0;
        this.flagDirty = false;

    }

    // ----------------   Methods   ----------------

    /**
     * Method setByteBuffer that sets the content of a page
     * @param : byteBuff : the content of the page
     * @return nothing
     */
    public void setByteBuffer(ByteBuffer byteBuff) {
        this.byteBuff = byteBuff;
    }

    /**
     * Method setPage that sets the id of a page
     * @param : pageId : the id of the page
     * @return nothing
     */
    public void setPage(PageID page) {
        this.pageId = page;
    }

    /**
     * Method setPinCount that sets the pincount of a frame
     * @param : pinCount : the pincount of the frame
     * @return nothing
     */
    public void setPinCount(int pinCount) {
        this.pinCount = pinCount;
    }

    /**
     * Method setFlagDirty that sets the flag dirty of a frame
     * @param : flagDirty : the flag dirty of the frame
     * @return nothing
     */
    public void setFlagDirty(boolean flagDirty) {
        this.flagDirty = flagDirty;
    }

    /**
     * Method getByteBuffer that returns the content of a page
     * @param : nothing
     * @return ByteBuffer : the content of the page
     */
    public ByteBuffer getByteBuffer() {
        return this.byteBuff;
    }

    /**
     * Method getPageId that returns the id of a page
     * @param : nothing
     * @return PageID : the id of the page
     */
    public PageID getPageId() {
        return this.pageId;
    }

    /**
     * Method getPinCount that returns the pincount of a frame
     * @param : nothing
     * @return int : the pincount of the frame
     */
    public int getPinCount() {
        return this.pinCount;
    }

    /**
     * Method getFlagDirty that returns the flag dirty of a frame
     * @param : nothing
     * @return boolean : the flag dirty of the frame
     */
    public boolean getFlagDirty() {
        return this.flagDirty;
    }

    /**
     * Method addPinCount that increments the pincount of a frame
     * @param : nothing
     * @return nothing
     */
    public void addPinCount() {
        this.pinCount++;
    }

    /**
     * Method compareFrames that compares the id of a page with the id of a frame
     * @param : page : the id of the page
     * @return boolean : true if the id of the page is the same as the id of the frame, false otherwise
     */
    public boolean compareFrames(PageID page){ 
        if(this.pageId.getFileIdx()==page.getFileIdx() && this.pageId.getPageIdx()==page.getPageIdx()){
            return true;
        }
        return false;
    }

    /**
     * Method frameIsEmpty that checks if a frame is empty
     * @param : nothing
     * @return boolean : true if the id of the frame is -1, false otherwise
     */
    public boolean frameIsEmpty(){
        if(this.pageId.getFileIdx()==-1 && this.pageId.getPageIdx()==-1){
            return true;
        }
        return false;
    }

    /**
     * Method cleanFrame that resets the frame
     * @param : nothing
     * @return nothing
     */
    public void cleanFrame() {
        pageId = new PageID();
        pinCount = 0;
        flagDirty = false;
    }
}
