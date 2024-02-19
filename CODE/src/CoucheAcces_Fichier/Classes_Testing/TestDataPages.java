package CoucheAcces_Fichier.Classes_Testing;

import CoucheAcces_Fichier.DataPages;
import GestionEspaceDisque_et_Buffer.DBParams;
import GestionEspaceDisque_et_Buffer.PageID;

/**
 * Class TestDataPages that tests the DataPages class
 */
public class TestDataPages {
    /**
     * ----------------   Main   ----------------
     * @param : args : arguments
     */
    public static void main(String[] args){
        DBParams.DBPath = "../../DB";
        DBParams.DMFileCount = 4;
        DBParams.SGBDPageSize = 4096;
        DBParams.FrameCount = 2;
        
        PageID pageId=new PageID(1,2);

        DataPages dataPages=new DataPages(pageId);  //create a dataPages
        
        System.out.println("next page : "+dataPages.getNextPage());  //it should be(-1,-1)
        System.out.println("position of free space : "+dataPages.getPosFreeSpace());  //it should be 8
        System.out.println("number of slot : "+dataPages.getNumberSlot()); //it should be 0
        System.out.println("available space : "+dataPages.getAvailableSpace()); //it should be (4096-8-4-4)

        PageID nextPage=new PageID(0,1);
        int numberSlot=2;  //add two record so we add two slots as well and each record takes 10 bytes
        int posFreeSpace=28;
        dataPages.setNextPage(nextPage);
        dataPages.setPosFreeSpace(posFreeSpace);
        dataPages.setNumberSlot(numberSlot);
        
        System.out.println("next page : "+dataPages.getNextPage()); //it should be (0,1)
        System.out.println("position of free space : "+dataPages.getPosFreeSpace());  //it should be 28
        System.out.println("number of slot : "+dataPages.getNumberSlot()); //it should be 2
        System.out.println("available space : "+dataPages.getAvailableSpace()); //it should be (4096-28-4-4-(2*8))
        
    }
}
