package CoucheOperateursRelationnels;

import CoucheAcces_Fichier.ColInfo;
import CoucheAcces_Fichier.ColumnType;
import CoucheAcces_Fichier.DatabaseInfo;
import CoucheAcces_Fichier.FileManager;
import CoucheAcces_Fichier.TableInfo;
import GestionEspaceDisque_et_Buffer.PageID;
import java.util.ArrayList;

/**
 * Class CreateTableCommand that represents the command to create a table
 */
public class CreateTableCommand {
    private String tableName;
    private int nbCol;
    private ArrayList<ColInfo> colsInfo;
    private String userCommand; 


    /**
     * ----------------   Constructor   ----------------
     * @param : userCommand : the command to create a table
     */
    public CreateTableCommand(String userCommand) {

        this.userCommand=userCommand;

        String[] tokens = this.userCommand.split(" ");
        this.tableName = tokens[2];
        String colsStr = tokens[3].substring(1, tokens[3].length() - 1); // delete the parenthesis
        String[] cols = colsStr.split(",");
        this.nbCol = cols.length;
        
        this.colsInfo = new ArrayList<>();

        for (int i = 0; i < nbCol; i++) {
            String[] col = cols[i].split(":");
            String colName = col[0].trim(); // delete the spaces before and after the name of the column
            ColumnType colType = null; 

            if (col.length >= 2) {
                // Vérifiez le type de colonne
                String colTypeStr = col[1].trim();
                if (colTypeStr.equals("INT")) {
                    colType = ColumnType.INT;
                } else if (colTypeStr.equals("FLOAT")) {
                    colType = ColumnType.FLOAT;
                } else if (colTypeStr.startsWith("STRING")) {
                    colType = ColumnType.STRING;
                } else if (colTypeStr.startsWith("VARSTRING")) {
                    colType = ColumnType.VARSTRING;
                } else {
                    System.out.println("Invalid column type: " + colTypeStr);
                }
            }

            int lengthString = 0;
            if (colType != null && colType != ColumnType.INT && colType != ColumnType.FLOAT) {

                if (colType == ColumnType.STRING || colType == ColumnType.VARSTRING) { // if the column is a string or a varstring
                    lengthString = 1;
                }



                // Check the length of the string
                String lengthStringStr = col[1].replaceAll("[^0-9]", "");
                try {
                    lengthString = Integer.parseInt(lengthStringStr);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid column size: " + col[1]);
                }

            }

            ColInfo colInfo = new ColInfo(colName, colType, lengthString);
            colsInfo.add(colInfo);

            // add the table to the databaseInfo
        }

       
    }
    
    // ----------------   Methods   ----------------

    /**
     * Method Execute which is called to execute the command to create a table
     * @param : nothing
     * @return void : nothing
     */
    public void Execute() {
        
        DatabaseInfo databaseInfo = DatabaseInfo.getInstance(); 
        FileManager fileManager = FileManager.getFileManager();
        PageID headerPageID =fileManager.createNewHeaderPage(); //crete a headerPage for the relation !!
        TableInfo tableInfo = new TableInfo(tableName, nbCol, colsInfo, headerPageID); //create a tableInfo for the relation
        databaseInfo.AddTableInfo(tableInfo); //add the table to the database
        tableInfo.printTableInfo(); //print the tableInfo
        System.out.println("");
        System.out.println("####    Table created successfully  ####");

    }

}