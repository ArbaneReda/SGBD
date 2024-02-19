package CoucheAcces_Fichier;

import GestionEspaceDisque_et_Buffer.PageID;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class TableInfo that represents the information of a relation
 */
public class TableInfo implements Serializable{ // a set of usefull information about a relation
    private String nameRelation;
    private int numberCols;
    private ArrayList<ColInfo> tableCols; // name and type of columns are regrouped in a separated class

    private PageID headerPageId;

    /**
     * ----------------   Constructor   ----------------
     * @param : nameRelation : the name of the relation
     * @param : numberCols : the number of columns in the relation
     * @param : tableCols : the list of all the columns in the relation
     * @param : headerPageId : the pageID of the header page of the relation
     */
    public TableInfo(String nameRelation, int numberCols, ArrayList<ColInfo> tableCols, PageID headerPageId) {
        this.nameRelation = nameRelation;
        this.numberCols = numberCols;
        this.tableCols = tableCols;
        this.headerPageId = headerPageId;
    }

    // ----------------   Methods   ----------------

    /**
     * Method GetNameRelation which is called to get the name of the relation
     * @param : nothing
     * @return String : the name of the relation
     */
    public String getNameRelation() {
        return nameRelation;
    }

    /**
     * Method GetNumberCols which is called to get the number of columns in the relation
     * @param : nothing
     * @return int : the number of columns in the relation
     */
    public int getNumberCols() {
        return numberCols;
    }

    /**
     * Method GetTableCols which is called to get the list of all the columns in the relation
     * @param : nothing
     * @return ArrayList<ColInfo> : the list of all the columns in the relation
     */
    public ArrayList<ColInfo> getTableCols() {
        return tableCols;
    }

    /**
     * Method getHeaderPageId which is called to get the pageID of the header page of the relation
     * @param : nothing
     * @return PageID : the pageID of the header page of the relation
     */
    public PageID getHeaderPageId(){
        return headerPageId;
    }

    /**
     * Method toString which is called to get the string representation of the tableInfo
     * @param : nothing
     * @return String : the string representation of the tableInfo
     */
    public String toString(){
        String TableInfo="|"+nameRelation+"|";
        for(ColInfo col:tableCols){
            TableInfo+=col.toString();
        }
        TableInfo+="| number of colomn : "+numberCols+"|";
        return TableInfo;
    }

    /**
     * Method printTableInfo which is called to display the tableInfo
     * @param : nothing
     * @return void : nothing
     */
    public void printTableInfo(){
        System.out.println("-----------------------------------------------------");
        System.out.println("Table name: "+nameRelation);
        System.out.println("Number of columns: "+numberCols);
        System.out.println("Columns: ");
        for(ColInfo col:tableCols){
            System.out.println(col.toString());
        }
        System.out.println("-----------------------------------------------------");
    }

    /*
     * Method to get the index of a column in the table
     * 
     * @param colName : the name of the column
     */
    public int getColIndex(String colName){
        for(int i=0;i<tableCols.size();i++){
            if(tableCols.get(i).getNameCol().equals(colName)){
                return i;
            }
        }
        return -1;
    }

}
