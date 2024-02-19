package CoucheAcces_Fichier.Classes_Testing;

import GestionEspaceDisque_et_Buffer.*;
import CoucheAcces_Fichier.*;
import java.util.ArrayList;

/**
 * Class TestDatabaseInfo that tests the DatabaseInfo class
 */
public class TestDatabaseInfo {
    /**
     * ----------------   Main   ----------------
     * @param : args : arguments
     */
    public static void main(String[] args) {

        DBParams.DBPath = "../../DB";

        DatabaseInfo databaseInfo = DatabaseInfo.getInstance(); // unique instance of DatabaseInfo

       // creating instances of the different classes in "CoucheAcces_Ficher" to test them
        ArrayList<ColInfo> tableCols = new ArrayList<>();
        tableCols.add(new ColInfo("Column1", ColumnType.INT, 0)); // Calling the constructor to instantiate the class with the specified arguments
        tableCols.add(new ColInfo("Column2", ColumnType.FLOAT, 0)); 

        PageID headerPageId = new PageID(0, 0);

        // Same thing, we are calling the constructor to instantiate the class
        TableInfo tableInfo1 = new TableInfo("Table1", 1, tableCols , headerPageId);
        TableInfo tableInfo2 = new TableInfo("Table2", 2, tableCols , headerPageId);

        databaseInfo.AddTableInfo(tableInfo1);
        databaseInfo.AddTableInfo(tableInfo2);

        // Save the information in the file
        databaseInfo.Finish();

        // Load the information from the file
         databaseInfo.Init();

        // Display the information
         System.out.println(databaseInfo.toString());
    }
}
