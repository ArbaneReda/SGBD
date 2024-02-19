package CoucheAcces_Fichier.Classes_Testing;

import GestionEspaceDisque_et_Buffer.*;
import CoucheAcces_Fichier.*;

/**
 * Class TestHeaderPage that tests the HeaderPage class
 */
public class TestHeaderPage {
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

        HeaderPage headerPage=new HeaderPage(pageId);  //create a headerPage

        //initialisation
        System.out.println("free page : "+headerPage.getFreePage().toString());  //it should be (-1,-1)
        System.out.println("full page : "+headerPage.getFullPage().toString());  //it should be (-1,-1)

        //set the full and free page
        PageID freePage=new PageID(0,1);  
        PageID fullPage=new PageID(0,3);
        headerPage.setFullPage(freePage);
        headerPage.setFreePage(fullPage);

        System.out.println("free page : "+headerPage.getFreePage().toString());  //it should be (0,1)
        System.out.println("full page : "+headerPage.getFullPage().toString());  //it should be (0,3)

    }
}
