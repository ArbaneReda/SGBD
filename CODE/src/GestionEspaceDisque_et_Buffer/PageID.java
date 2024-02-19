package GestionEspaceDisque_et_Buffer;

import java.nio.ByteBuffer;
import java.io.Serializable;

/**
 * Class PageID that represents the id of a page
 */
public class PageID implements Serializable {

   private int fileIdx;
   private int pageIdx;

   /**
    * ----------------   Constructor   ----------------
    * @param : fileIdx : the index of the file,
    * @param : pageIdx : the index of the page
    */
   public PageID(int fileIdx, int pageIdx) {
      this.fileIdx = fileIdx;
      this.pageIdx = pageIdx;

   }

   /**
    * ----------------   Constructor   ----------------
    * @param : nothing
    */
   public PageID(){
      this.fileIdx = -1;
      this.pageIdx = -1;
   }

   // ----------------   Methods   ----------------

   /**
    * Method readFromBuffer that reads the content of a page
    * @param : byteBuffer : the content of the page
    * @param : pos : the position of the page
    * @return PageID : the id of the page
    */
   public static PageID readFromBuffer(ByteBuffer byteBuffer, int pos){
      byteBuffer.position(pos);
      return new PageID(byteBuffer.getInt(), byteBuffer.getInt());
   }

   /**
    * Method writeFromBuffer that writes the content of a page
    * @param : byteBuffer : the content of the page
    * @param : pos : the position of the page
    * @param : pageId : the id of the page
    * @return nothing
    */
   public static void writeFromBuffer(ByteBuffer byteBuffer, int pos,PageID pageId){

      byteBuffer.position(pos);

      byteBuffer.putInt(pageId.getFileIdx());
      byteBuffer.putInt(pageId.getPageIdx());

   }

   /**
    * Method setFileIdx that sets the index of the file
    * @param : fileIdx : the index of the file
    * @return nothing
    */
   public void setFileIdx(int FileIdx) {
      this.fileIdx = FileIdx;
   }

   /**
    * Method setPageIdx that sets the index of the page
    * @param : pageIdx : the index of the page
    * @return nothing
    */
   public void setPageIdx(int PageIdx) {
      this.pageIdx = PageIdx;
   }

   /**
    * Method getFileIdx that returns the index of the file
    * @param : nothing
    * @return int : the index of the file
    */
   public int getFileIdx() {
      return this.fileIdx;
   }

   /**
    * Method getPageIdx that returns the index of the page
    * @param : nothing
    * @return int : the index of the page
    */
   public int getPageIdx() {
      return this.pageIdx;
   }

   /**
    * Method isValid that checks if the page is valid
    */
   public boolean isValid(){  //check if the page is a valid page
      if(this.fileIdx<0 || this.pageIdx<0 ){
         return false;
      }
      return true;
   }

   /**
    * Method equals that checks if two pages are equal
    * @param : pageId : the id of the page
    * @return boolean : true if the two pages are equal, false otherwise
    */
   public boolean equals(PageID pageId){   //check if two pages are equal
      if(this.fileIdx==pageId.getFileIdx() && this.pageIdx==pageId.getPageIdx()){
         return true;
      }
      return false;
   }

   /**
    * Method toString that returns the id of the page
    * @param : nothing
    * @return String : the id of the page
    */
   @Override
   public String toString(){
      return "("+fileIdx+","+pageIdx+")"+",";
   }

}
