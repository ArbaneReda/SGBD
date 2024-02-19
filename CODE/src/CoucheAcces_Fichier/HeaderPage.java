package CoucheAcces_Fichier;

import GestionEspaceDisque_et_Buffer.*;

import java.nio.ByteBuffer;

/**
 * Class HeaderPage that represents the header page of a directory page
 */
public class HeaderPage {

    private ByteBuffer byteBuffer;
    private PageID pageId;

    /**
     * ----------------   Constructor   ----------------
     * @param : pageId : the pageID of the headerPage
     */
    public HeaderPage(PageID pageId) {

        this.pageId = pageId;

        BufferManager bufferManager = BufferManager.getBufferManager(); // Get Buffer and Disk manager
        // DiskManager diskManager = DiskManager.getDiskManager();

        byteBuffer = bufferManager.getPage(pageId);

        PageID freePageId = new PageID(); // the page with free space
        PageID fullPageId = new PageID(); // the full page

        setFreePage(freePageId);
        setFullPage(fullPageId);
    }

    /**
     * ----------------   Constructor   ----------------
     * @param : byteBuffer : the byteBuffer of the headerPage
     * @param : pageId : the pageID of the headerPage
    */
    public HeaderPage(ByteBuffer byteBuffer, PageID pageId) {
        this.byteBuffer = byteBuffer;
        this.pageId = pageId;
    }

    // ----------------   Methods   ----------------
    
    /**
     * Method setFreePage which is called to set the pageID of the freePage
     * @param : pageId : the pageID of the freePage
     * @return void : nothing
     */
    public void setFreePage(PageID pageId) {
        int pos = 0;
        byteBuffer.position(pos);
        int freeFileIdx = pageId.getFileIdx();
        int freePageIdx = pageId.getPageIdx();

        byteBuffer.putInt(freeFileIdx);
        byteBuffer.putInt(freePageIdx);
    }

    /**
     * Method setFullPage which is called to set the pageID of the fullPage
     * @param : pageId : the pageID of the fullPage
     * @return void : nothing
     */
    public void setFullPage(PageID pageId) {
        int pos = 8;
        byteBuffer.position(pos);
        int fullFileIdx = pageId.getFileIdx();
        int fullPageIdx = pageId.getPageIdx();

        byteBuffer.putInt(fullFileIdx);
        byteBuffer.putInt(fullPageIdx);

    }

    /**
     * Method getFreePage which is called to get the pageID of the freePage
     * @param : nothing
     * @return PageID : the pageID of the freePage
     */
    public PageID getFreePage() {

        int pos = 0;
        byteBuffer.position(pos);
        return new PageID(byteBuffer.getInt(), byteBuffer.getInt());
    }

    /**
     * Method getFullPage which is called to get the pageID of the fullPage
     * @param : nothing
     * @return PageID : the pageID of the fullPage
     */
    public PageID getFullPage() {
        int pos = 8;
        byteBuffer.position(pos);
        return new PageID(byteBuffer.getInt(), byteBuffer.getInt());
    }

    /**
     * Method getPageID which is called to get the pageID of the headerPage
     * @param : nothing
     * @return PageID : the pageID of the headerPage
     */
    public PageID getPageID() {
        return pageId;
    }

    /**
     * Method setPageID which is called to set the pageID of the headerPage
     * @param : pageId : the pageID of the headerPage
     * @return void : nothing
     */
    public void setPageID(PageID pageId) {
        this.pageId = pageId;
    }

    /**
     * Method getHeaderPage which is called to get the content of a created HeaderPage that has been written in a file
     * @param : pageId : the pageID of the headerPage
     * @return ByteBuffer : the content of the headerPage
     */
    public static ByteBuffer getHeaderPage(PageID pageId) {

        BufferManager bufferManager = BufferManager.getBufferManager();

        ByteBuffer byteBuffer = bufferManager.getPage(pageId);
        return byteBuffer;
    }

    /**
     * Method toString which is called to display the content of the headerPage
     * @param : nothing
     * @return String : the content of the headerPage
     */
    public String toString() {

        String content = "free page : " + this.getFreePage().toString() + "\n";
        content += "full page : " + this.getFullPage().toString() + "\n";

        return content;
    }

    /**
     * Method finalize which is called to free the headerPage and write its content
     * @param : nothing
     * @return void : nothing
     */
    public void finalize(){

        BufferManager bufferManager = BufferManager.getBufferManager();
        bufferManager.freePage(this.pageId, true);
    }
}
