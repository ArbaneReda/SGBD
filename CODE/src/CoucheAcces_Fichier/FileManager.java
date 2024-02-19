package CoucheAcces_Fichier;

import GestionEspaceDisque_et_Buffer.BufferManager;
import GestionEspaceDisque_et_Buffer.DBParams;
import GestionEspaceDisque_et_Buffer.DiskManager;
import GestionEspaceDisque_et_Buffer.PageID;
import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * Class FileManager that manages the files
 */
public class FileManager {

    private static FileManager fileManager = new FileManager();

    /**
     * ----------------   Constructor   ----------------
     * @param : nothing
     */
    private FileManager() {

    }

    // ----------------   Methods   ----------------

    /**
     * Method getFileManager that returns the unique instance of FileManager
     * @return FileManager : the unique instance of FileManager
     */
    public static FileManager getFileManager() {
        return fileManager;
    }

    /**
     * Method createNewHeaderPage which is called to create a new headerPage
     * @param : nothing
     * @return PageID : the pageID of the new headerPage
     */
    public PageID createNewHeaderPage() { 

        DiskManager diskManager = DiskManager.getDiskManager();
        
        PageID pageId = diskManager.getEmptyPage(); // allocate a page

        HeaderPage headerPage = new HeaderPage(pageId); // create a headerPage with the allocated page

        headerPage.finalize(); //write the page

        return pageId; // Return PageId
    }

    /**
     * Method addDataPage which is called to add a new dataPage to a relation
     * @param : tableInfo : the relation of the dataPage
     * @return PageID : the pageID of the new dataPage
     */
    public PageID addDataPage(TableInfo tableInfo) {

        DiskManager diskManager = DiskManager.getDiskManager();
        PageID pageId = diskManager.getEmptyPage(); // allocate a page
       

        DataPages dataPages = new DataPages(pageId); // create the new dataPage

        HeaderPage headerPage = getHeaderPageOfTableInfo(tableInfo); // get the headerPage of tableInfo

        PageID freePageId = headerPage.getFreePage(); // get the currentFreePage

        headerPage.setFreePage(pageId); // set the newFreeDataPages as the currentFreeDataPages
        headerPage.setFullPage(headerPage.getFullPage()); // this line should be deleted but when deleted it seems like
                                                          // the postion of free space is gone
        dataPages.setNextPage(freePageId); // link the newFreeDataPages to the currentFreeDataPages
        dataPages.getPosFreeSpace(); // this line should be deleted but when deleted it seems like the postion of
                                     // free space is gone

        headerPage.finalize(); //Write the HeaderPage
        dataPages.finalize(); //write the dataPage
        //bufferManager.flushAll();
        
        return pageId;
    }

    /**
     * Method getFreePageId which is called to get a freePage with enough space
     * @param : tableInfo : the relation of the dataPage
     * @param : sizeRecord : the size of the record
     * @return PageID : the pageID of the new dataPage
     */
    public PageID getFreePageId(TableInfo tableInfo, int sizeRecord) {

        HeaderPage headerPage = getHeaderPageOfTableInfo(tableInfo); // get the headerPage of tableInfo
        DataPages freePage = getDataPages(headerPage.getFreePage()); // get the first freePage of a tableInfo
        
        headerPage.finalize();  //

        if (freePage.getPageID().isValid()) {
            while (freePage.getAvailableSpace() <= sizeRecord) { // while the freePage
                                                                   // doesn't have
                                                                   // enough space we
                                                                   // check the next

                freePage = getDataPages(freePage.getNextPage()); // get the next freePage
            }

            if (freePage.getPageID().isValid()) { // if there is a dataPage that has enought space we return it
                freePage.finalize();
                return freePage.getPageID();
            }
        }
        return new PageID(); // otherwise we return an non valide page
        
    }

    /**
     * Method writeRecordToDataPage which is called to write a record to a dataPage
     * @param : record : the record to write
     * @param : pageIdDataPage : the pageID of the dataPage
     * @return RecordId : the recordID of the record
     */
    public RecordId writeRecordToDataPage(Record record, PageID pageIdDataPage) {

        int sizeRecord = record.sizeRecord(); // get the size of a record
        DataPages dataPages = getDataPages(pageIdDataPage); // get the dataPage

        int posFreeSpace = dataPages.getPosFreeSpace(); // get the position where we should write the record

        ByteBuffer byteBuffer = dataPages.getByteBuffer(); // get the content of the dataPage

        record.writeToBuffer(byteBuffer, posFreeSpace); // writte the record to the byteBuffer of the dataPage

        int numberSlot = dataPages.getNumberSlot();
        byteBuffer.position(DBParams.SGBDPageSize - (numberSlot * 8 + 8 + 8));// position of the new slot

        // add the slot to the byteBuffer of the dataPage
        byteBuffer.putInt(posFreeSpace);
        byteBuffer.putInt(sizeRecord);

        // update the number of slot and available space
        dataPages.setPosFreeSpace(posFreeSpace + sizeRecord);
        dataPages.setNumberSlot(numberSlot + 1);

        dataPages.finalize(); 
        // check if the page is full
        if (dataPages.getAvailableSpace() <= DBParams.PageFull) {
            updateToFullPage(pageIdDataPage, record.getTableInfo());
        }

        dataPages.finalize();

        return new RecordId(pageIdDataPage, numberSlot);
    }

    /**
     * Method deleteRecordToDataPage which is called to delete a record to a dataPage
     * @param : pageIdDataPage : the pageID of the dataPage
     * @param : indexRecord : the index of the record
     * @return void : nothing
     */
    public void deleteRecordToDataPage(PageID pageIdDataPage,int indexRecord) {

        DataPages dataPages = getDataPages(pageIdDataPage); // get the dataPage

        ByteBuffer byteBuffer = dataPages.getByteBuffer(); // get the content of the dataPage

        byteBuffer.position(DBParams.SGBDPageSize - (indexRecord * 8 + 8 + 8));// position of the new slot

        // set the pos of the record to -1 as deleted
        byteBuffer.putInt(-1);
       

        dataPages.finalize(); 

    }


    /**
     * Method getRecordsInDataPage which is called to get all the records of a dataPage
     * @param : tableInfo : the relation of the dataPage
     * @param : pageId : the pageID of the dataPage
     * @return ArrayList<Record> : the list of all the records of the dataPage
     */
    public ArrayList<Record> getRecordsInDataPage(TableInfo tableInfo, PageID pageId) {

        ArrayList<Record> listRecords = new ArrayList<>(); // create the list of record
        DataPages dataPage = getDataPages(pageId); // create the dataPage
        int numberRecod = dataPage.getNumberSlot(); // get number of record in the dataPage
        

        ByteBuffer byteBuffer = dataPage.getByteBuffer();
        
        for (int i = 0; i < numberRecod; i++) {
            Record record = new Record(tableInfo);// create an empty record
            int positionRecord = dataPage.getPosRecord(i);
            
            
            //if the pos=-1 the record is not read
           if(positionRecord!=-1){
            
                record.readFromBuffer(byteBuffer, positionRecord);

                listRecords.add(record);
           }
           else{
                Record deletedRecord= new Record(tableInfo);
                deletedRecord.setAsDeleted();
                
                listRecords.add(deletedRecord);
           }
        }

        dataPage.finalize(); //free the dataPage 

        return listRecords;
    }

    /**
     * Method getDataPages which is called to get all the dataPages of a relation
     * @param : tableInfo : the relation of the dataPage
     * @return ArrayList<PageID> : the list of all the dataPages of the relation
     */
    public ArrayList<PageID> getDataPages(TableInfo tableInfo) {

        ArrayList<PageID> listDataPagesId = new ArrayList<>(); // create the list
        HeaderPage headerPage = getHeaderPageOfTableInfo(tableInfo); // get the headerPage of tableInfo
        PageID freeDataPageId = headerPage.getFreePage(); // get the first freePageId
        PageID fullPageId = headerPage.getFullPage(); // get the first fullpageId


        headerPage.finalize();

        while (freeDataPageId.isValid()) {
            listDataPagesId.add(freeDataPageId);
            DataPages freeDataPage = getDataPages(freeDataPageId);
            freeDataPageId = freeDataPage.getNextPage();
            freeDataPage.toString();
            freeDataPage.finalize();
        }

        while (fullPageId.isValid()) {
            listDataPagesId.add(fullPageId);
            DataPages fullDataPage = getDataPages(fullPageId);
            fullPageId = fullDataPage.getNextPage();
            fullDataPage.toString();
            fullDataPage.finalize();
        }
        
        return listDataPagesId;
    }

    /**
     * Method insertRecordIntoTable which is called to insert a record into a relation
     * @param : record : the record to insert
     * @return RecordId : the recordID of the record
     */
    public RecordId InsertRecordIntoTable(Record record) {

        TableInfo tableInfo = record.getTableInfo(); // get the relation of a record
        PageID freePageId = getFreePageId(tableInfo, record.sizeRecord()); // get a dataPage that has enough space

        if(!freePageId.isValid()){ 
            freePageId = addDataPage(tableInfo);
        }
        RecordId recordId= writeRecordToDataPage(record, freePageId);

        return recordId;
    }



    /**
     * Method getAllRecords which is called to get all the records of a relation
     * @param : tabInfo : the relation of the dataPage
     * @return ArrayList<Record> : the list of all the records of the relation
     */
    public ArrayList<Record> getAllRecords (TableInfo tabInfo){

        ArrayList <Record> listRecords=new ArrayList<>();  //list of record
        ArrayList<PageID> listPages=getDataPages(tabInfo);  //list of pages inside a relation
        
        for(int i=0;i<listPages.size();i++){
            ArrayList<Record> listRecordsPage=getRecordsInDataPage(tabInfo, listPages.get(i)); //get the record of a page
            
           
            for(Record record:listRecordsPage){
                listRecords.add(record);
            }
        }
        return listRecords;
    }

    /**
     * Method updateToFullPage which is called to update a dataPage to a fullPage
     * @param : newFullPageId : the pageID of the new fullPage
     * @param : tableInfo : the relation of the dataPage
     * @return void : nothing
     */
    public void updateToFullPage(PageID newFullPageId, TableInfo tableInfo) {

        HeaderPage headerPage = getHeaderPageOfTableInfo(tableInfo); // get the headerPage
        PageID fullPage = headerPage.getFullPage();
        DataPages newFullPage = getDataPages(newFullPageId);

        headerPage.finalize();

        DataPages freePage = getDataPages(headerPage.getFreePage()); // get the freePage

        PageID nextFreePageId = headerPage.getFreePage();
        DataPages currentfreePages = getDataPages(nextFreePageId);

        while (!nextFreePageId.equals(newFullPageId)) { // update the nextfreePage of the previous freePage

            nextFreePageId = currentfreePages.getPageID(); // get the pageId of the next freePageId

            if (nextFreePageId.equals(newFullPageId)) {
                DataPages nextFreePage = getDataPages(nextFreePageId);
                currentfreePages.setNextPage(nextFreePage.getNextPage());
                nextFreePage.finalize();
            }
            currentfreePages = getDataPages(currentfreePages.getNextPage());

        }

        // if the newFullPage is the firstFreePage we set the freePage to the next
        // freePage
        if (freePage.getPageID().equals(newFullPageId)) {
            headerPage.setFreePage(currentfreePages.getNextPage());
        }
        // update the fullPage

        newFullPage.setNextPage(fullPage); // link the newfullPage to the fullpage of headerPage
        newFullPage.getPosFreeSpace();

        headerPage.setFullPage(nextFreePageId); // set the fullPage of the headerPage to the newFullPage

        currentfreePages.finalize();
        
        newFullPage.finalize();

    }

    /**
     * Method getHeaderPageOfTableInfo which is called to get the headerPage of a relation
     * @param : tableInfo : the relation of the dataPage
     * @return HeaderPage : the headerPage of the relation
     */
    public HeaderPage getHeaderPageOfTableInfo(TableInfo tableInfo) {

        BufferManager bufferManager = BufferManager.getBufferManager();

        PageID pageIdHeaderPage = tableInfo.getHeaderPageId(); // get the pageId of headerPage
        ByteBuffer byteBufferHeaderPage = bufferManager.getByteBufferPage(pageIdHeaderPage); // get the content of the
                                                                                             // PageIdHeaderPag
        HeaderPage headerPage = new HeaderPage(byteBufferHeaderPage, pageIdHeaderPage); // create a headerPage with the
                                                                                        // content of the byteBuffer
        return headerPage;
    }

    /**
     * Method getDataPages which is called to get a dataPage
     * @param : pageId : the pageID of the dataPage
     * @return DataPages : the dataPage
     */
    public DataPages getDataPages(PageID pageId) {

        BufferManager bufferManager = BufferManager.getBufferManager();

        ByteBuffer byteBuffer = bufferManager.getByteBufferPage(pageId); // get the content of the dataPage
        DataPages dataPages = new DataPages(byteBuffer, pageId); // create the dataPage with the content of the
                                                                 // byteBuffer

        return dataPages;
    }

    /**
     * Method displayPages which is called to display all the dataPages of a relation
     * @param : tableInfo : the relation of the dataPage
     * @return String : the content of all the dataPages of the relation
     */
    public String displayPages(TableInfo tableInfo){
        String content="";
        ArrayList<PageID> listPages=getDataPages(tableInfo);
        content+="number record : "+listPages.size()+"\n";
        for(PageID page:listPages){
            DataPages dataPages=getDataPages(page);
            content+=dataPages.toString()+"\n";
        }
        return content;
    }

    /**
     * Method resetFileDB which is called to reset the database
     * @param : nothing
     * @return void : nothing
     */
    public void resetFileDB(){
        DiskManager diskManager=DiskManager.getDiskManager();
        diskManager.deleteAllDBFiles();
    }


}
