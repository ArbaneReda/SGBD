package CoucheAcces_Fichier;

import java.nio.ByteBuffer;
import GestionEspaceDisque_et_Buffer.*;

/**
 * Class DataPages that represents the data pages
 */
public class DataPages {
    private ByteBuffer byteBuffer;
    private PageID pageId;

    /**
     * ----------------   Constructor   ----------------
     * @param : pageId : the pageID of the dataPage
     */
    public DataPages(PageID pageId) {

        this.pageId = pageId; // set the pageId of the dataPage

        BufferManager bufferManager = BufferManager.getBufferManager(); // Get Buffer and Disk manager

        byteBuffer = bufferManager.getPage(pageId); // the byteBuffer will containe all information (next page/ records/
                                                    // slot .....)

        PageID pageIdNext = new PageID(); // create an empty page as the next page(-1,-1)

        // add the next page
        setNextPage(pageIdNext);

        // add position of available space to the DataPage
        int posFreeSpace = 8;
        setPosFreeSpace(posFreeSpace);

        // add the number of slot
        int numberSlot = 0;
        setNumberSlot(numberSlot);

    }

    // ----------------   Methods   ----------------

    /**
     * Method DataPages which is called to create a dataPage
     * @param : byteBuffer : the byteBuffer of the dataPage
     * @param : pageId : the pageID of the dataPage
     * @return void : nothing
     */
    public DataPages(ByteBuffer byteBuffer, PageID pageId) {

        this.byteBuffer = byteBuffer;
        if (byteBuffer == null) {
            this.pageId = new PageID(); // if nothing is written in the byteBuffer it means that the page is (-1,-1)
        } else {
            this.pageId = pageId;
        }
    }

    /**
     * Method getPageID that returns the pageID of a dataPage
     * @param : nothing
     * @return PageID : the pageID of the dataPage
     */
    public PageID getPageID() {
        return pageId;
    }

    /**
     * Method setPageID that sets the pageID of a dataPage
     * @param : pageId : the pageID of the dataPage
     * @return void : nothing
     */
    public void setPageID(PageID pageId) {
        this.pageId = pageId;
    }

    /**
     * Method setNextPage that sets the next page of the dataPage
     * @param : pageId : the pageID of the next page
     * @return void : nothing
     */
    public void setNextPage(PageID pageId) {
        int pos = 0;
        byteBuffer.position(pos);
        byteBuffer.putInt(pageId.getFileIdx());
        byteBuffer.putInt(pageId.getPageIdx());
    }

    /**
     * Method getNextPage that returns the next page of the dataPage
     * @param : nothing
     * @return PageID : the pageID of the next page
     */
    public PageID getNextPage() {
        int pos = 0;
        byteBuffer.position(pos);
        return new PageID(byteBuffer.getInt(), byteBuffer.getInt());
    }

    /**
     * Method getPosFreeSpace that returns the position of the free space in the dataPage
     * @param : nothing
     * @return int : the position of the free space in the dataPage
     */
    public int getPosFreeSpace() {

        int pos =DBParams.SGBDPageSize-4;
        byteBuffer.position(pos);
        return byteBuffer.getInt();
    }

    /**
     * Method setPosFreeSpace that sets the position of the free space in the dataPage
     * @param : freeSpace : the position of the free space in the dataPage
     * @return void : nothing
     */
    public void setPosFreeSpace(int freeSpace) {
        int pos =DBParams.SGBDPageSize-4;
        byteBuffer.position(pos);
        byteBuffer.putInt(freeSpace);

    }

    /**
     * Method getNumberSlot that returns the number of slot in the dataPage
     * @param : nothing
     * @return int : the number of slot in the dataPage
     */
    public int getNumberSlot() {

        int pos = DBParams.SGBDPageSize - 8;
        byteBuffer.position(pos);
        return byteBuffer.getInt();
    }

    /**
     * Method setNumberSlot that sets the number of slot in the dataPage
     * @param : numberSlot : the number of slot in the dataPage
     * @return void : nothing
     */
    public void setNumberSlot(int numberSlot) {
        int pos = DBParams.SGBDPageSize - 8;
        byteBuffer.position(pos);
        byteBuffer.putInt(numberSlot);
    }

    /**
     * Method getAvailableSpace that returns the available space in the dataPage
     * @param : nothing
     * @return int : the available space in the dataPage
     */
    public int getAvailableSpace() {
        int posFreeSpace = getPosFreeSpace();
        int numberSlot = getNumberSlot();
        int availableSpace = DBParams.SGBDPageSize - (posFreeSpace + (numberSlot * 8) + 8);

        return availableSpace;
    }

    /**
     * Method getPosRecord that returns the position of a record in the dataPage
     * @param : indexRecod : the index of the record
     * @return int : the position of the record in the dataPage
     */
    public int getPosRecord(int indexRecod) {
        int pos = DBParams.SGBDPageSize - (16 + 8 * indexRecod);
        byteBuffer.position(pos);
        return byteBuffer.getInt();
    }

    /**
    * Method getSpaceRecod that returns the space of a record in the dataPage
    * @param : indexRecod : the index of the record
    * @return int : the space of the record in the dataPage
    */
    public int getSpaceRecod(int indexRecod) {
        int pos = DBParams.SGBDPageSize - (12 + 8 * indexRecod);
        byteBuffer.position(pos);
        return byteBuffer.getInt();
    }

    /**
     * Method getByteBuffer that returns the byteBuffer of the dataPage
     * @param : nothing
     * @return ByteBuffer : the byteBuffer of the dataPage
     */
    public ByteBuffer getByteBuffer() {
        return this.byteBuffer;
    }

    /**
     * Method getDataPage that returns the dataPage
     * @param : pageId : the pageID of the dataPage
     * @return ByteBuffer : the byteBuffer of the dataPage
     */
    public static ByteBuffer getDataPage(PageID pageId) {

        BufferManager bufferManager = BufferManager.getBufferManager();

        return bufferManager.getPage(pageId);
    }

    /**
     * Method toString that returns the information of the dataPage
     * @param : nothing
     * @return String : the information of the dataPage
     */
    public String toString() {
        String content = "";
        if (byteBuffer != null) {
            content+="page : "+pageId+"\n";
            content += "next page : " + this.getNextPage() + "\n";
            content += "position free space : " + this.getPosFreeSpace() + "\n";
            content += "number slot : " + this.getNumberSlot() + "\n";
            content += "available space : " + this.getAvailableSpace() + "\n";

        }
        return content;
    }

    /**
     * Method finalize that is called when the garbage collector is called
     * @param : nothing
     * @return void : nothing
     */
    public void finalize() {

        BufferManager bufferManager = BufferManager.getBufferManager();
        bufferManager.freePage(this.pageId, true);
    }

}
