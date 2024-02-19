package CoucheOperateursRelationnels;

import CoucheAcces_Fichier.ColInfo;
import CoucheAcces_Fichier.ColumnType;
import CoucheAcces_Fichier.DatabaseInfo;
import CoucheAcces_Fichier.FileManager;
import CoucheAcces_Fichier.Record;
import CoucheAcces_Fichier.TableInfo;
import GestionEspaceDisque_et_Buffer.DBParams;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;



/**
 * Class ImportCommande that represents the command to import a file
 */
public class ImportCommande {
    private String relationName;
    private ArrayList<ColInfo> colsInfo=new ArrayList<>();
    private String filePath;
    private String userCommand; 

    /**
     * ----------------   Constructor   ----------------
     * @param : userCommand : the command to import a file
     */
    public ImportCommande(String userCommand){
        
        this.userCommand=userCommand; 

        String[] tokens = this.userCommand.split(" ");
        this.relationName = tokens[2]; //get the name of the relation

        TableInfo tableInfo = DatabaseInfo.getInstance().GetTableInfo(this.relationName); //get the relation 

        ArrayList<ColInfo> colsInfos = tableInfo.getTableCols();
        
        for(ColInfo colInfo : colsInfos){ //get the colInfo of the relation
            this.colsInfo.add(colInfo);
        }

        this.filePath =DBParams.DBPath+ File.separator + ".." + File.separator + tokens[3]; //get the path of the file
    }

    // ----------------   Methods   ----------------

    /**
     * Method printTableInfo that prints the information of the table
     * @param : nothing
     * @return : nothing
     */
    public void printTableInfo() {
        System.out.println("====    the imported table has the following information:    ====\n");
        System.out.println("Table Name: " + this.relationName);
        System.out.println("name of the file : "+this.filePath);
        System.out.println("Column Information:");

        for (ColInfo col : this.colsInfo) {
            System.out.println("  Column Name: " + col.getNameCol());
            System.out.println("  Column Type: " + col.getTypeCol());
            if (col.getTypeCol() == ColumnType.STRING || col.getTypeCol() == ColumnType.VARSTRING) {
                System.out.println("  Column Size: " + col.getLengthString());
            }
        }
    }

    /**
     * Method Execute which is called to execute the command to import a file
     * @param : nothing
     * @return : nothing
     */
    public void Execute(){

        ArrayList<Record> recordList=createListRecords(); //get the list of records

        // Insert the record into the table
        for(Record record : recordList){
            FileManager.getFileManager().InsertRecordIntoTable(record);
        }
    }

    /**
     * Method createListRecords that creates the list of records
     * @param : nothing
     * @return ArrayList<Record> : the list of records
     */
    public ArrayList<Record> createListRecords(){

        ArrayList<Record> recordList= new ArrayList<>();//list of records
        
        TableInfo tableInfo = DatabaseInfo.getInstance().GetTableInfo(this.relationName); // Get the table info

        try (BufferedReader br = new BufferedReader(new FileReader(this.filePath))) {  //read the file
            String line;
            while ((line = br.readLine()) != null) {
                Record record = new Record(tableInfo); //create a record
                String[] valuesSplit = line.split(",");
                
                for(String value:valuesSplit){
                    record.getRecValues().add(value); //add the values to the record
                }
                recordList.add(record); //add the record to the list of records

            }
            System.out.println("####    the file has been imported successfully !    ####");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return recordList;
    }
}
