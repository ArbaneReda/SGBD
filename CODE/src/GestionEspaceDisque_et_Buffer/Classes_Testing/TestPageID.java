package GestionEspaceDisque_et_Buffer.Classes_Testing;

import GestionEspaceDisque_et_Buffer.PageID;

/**
 * Class TestPageID that tests the PageID class
 */
public class TestPageID {
    /**
     * ----------------   Main   ----------------
     * @param : args : arguments
     */
    public static void main(String[] args){
        PageID page1=new PageID();
        System.out.println("file : "+page1.getFileIdx()+" page : "+page1.getPageIdx()); //we should get file : -1 page : -1

        PageID page2=new PageID(2,3);
        System.out.println("file : "+page2.getFileIdx()+" page : "+page2.getPageIdx()); //we should get file : 2 page : 3 
    }
}
