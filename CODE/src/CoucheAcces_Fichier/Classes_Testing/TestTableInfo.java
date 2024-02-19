package CoucheAcces_Fichier.Classes_Testing;

import CoucheAcces_Fichier.*;
import java.util.ArrayList;
import GestionEspaceDisque_et_Buffer.PageID;

/**
 * Class TestTableInfo that tests the TableInfo class
 */
public class TestTableInfo {
    /**
     * ----------------   Main   ----------------
     * @param : args : arguments
     */
    public static void main(String[] args) {
        // Creating the list of columns for the table
        ArrayList<ColInfo> tableCols = new ArrayList<>();
        tableCols.add(new ColInfo("Column1", ColumnType.INT, 0)); 
        tableCols.add(new ColInfo("Column2", ColumnType.FLOAT, 0)); 
        tableCols.add(new ColInfo("Column3", ColumnType.STRING, 15)); 
        tableCols.add(new ColInfo("Column4", ColumnType.VARSTRING, 50)); 

        PageID headerPageId = new PageID(/* initialisez le PageID avec les informations nécessaires */);

        TableInfo tableInfo = new TableInfo("Table1", 4, tableCols, headerPageId);

        System.out.println("Table Name: " + tableInfo.getNameRelation());
        System.out.println("Number of Columns: " + tableInfo.getNumberCols());

        ArrayList<ColInfo> columns = tableInfo.getTableCols();
        for (ColInfo col : columns) {
            System.out.println("Column Name: " + col.getNameCol());
            System.out.println("Column Type: " + col.getTypeCol());
            System.out.println("Column Length: " + col.getLengthString());
        }
    }
}
