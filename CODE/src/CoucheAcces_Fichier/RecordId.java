package CoucheAcces_Fichier;

import GestionEspaceDisque_et_Buffer.PageID;

/**
 * Class RecordId that represents the recordID
 */
public class RecordId {

    private PageID pageId;
    private int slotIdx;
    private Record record;  // the record corresponding to this RecordId

    /**
     * ----------------   Constructor   ----------------
     * @param : pageId : the pageID of the record
     * @param : slotIdx : the index of the record in the page
     */
    public RecordId (PageID pageId, int slotIdx){
        this.pageId = pageId;
        this.slotIdx = slotIdx;
    }

    /**
     * ----------------   Constructor   ----------------
     * @param : nothing
     */
    public RecordId(){
        this.pageId=new PageID();
        this.slotIdx=-1;
    }

    // ----------------   Methods   ----------------

    /**
     * Method isValid which is called to check if the RecordId is valid
     * @param : nothing
     * @return boolean : true if the RecordId is valid, false otherwise
     */
    public boolean isValid(){
        if(this.pageId.isValid()){
            return true;
        }
        return false;
    }

    /**
     * Method toString which is called to get the string representation of the RecordId
     * @param : nothing
     * @return String : the string representation of the RecordId
     */
    public String toString(){
        return pageId.toString()+" | "+slotIdx;
    }

    /*
     * Method to get the corresponding Record to this RecordId
     */
    public Record getRecord() {
        return record;
    }

}
