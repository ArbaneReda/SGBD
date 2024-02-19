package CoucheAcces_Fichier.Classes_Testing;

import GestionEspaceDisque_et_Buffer.*;
import CoucheAcces_Fichier.*;
import CoucheAcces_Fichier.Record;

import java.util.ArrayList;

/**
 * Class TestFileManager that tests the FileManager class
 */
public class TestFileManager {
    /**
     * ----------------   Main   ----------------
     * @param : args : arguments
     */
    public static void main(String[] args) {
        DBParams.DBPath = "../../DB";
        DBParams.DMFileCount = 4;
        DBParams.SGBDPageSize = 4096;
        DBParams.FrameCount = 20;
        DBParams.PageFull = 4000;

        FileManager fileManager = FileManager.getFileManager();
        BufferManager bufferManager = BufferManager.getBufferManager();
        //DiskManager diskManager = DiskManager.getDiskManager();   Yanis l'a mit mais on l'utilise pas (vous avez raison)

        
        ArrayList<ColInfo> tableCols = new ArrayList<>();
        tableCols.add(new ColInfo("Column1", ColumnType.INT, 0));
        tableCols.add(new ColInfo("Column2", ColumnType.FLOAT, 0));
        tableCols.add(new ColInfo("Column3", ColumnType.STRING, 15));
        tableCols.add(new ColInfo("Column4", ColumnType.VARSTRING, 50));

      
        ArrayList<String> recValues = new ArrayList<>();
        recValues.add("1247"); // INT
        recValues.add("469.85"); // FLOAT
        recValues.add("HelloTest"); // STRING
        recValues.add("Licence2Wirdo"); // VARSTRING

        PageID headerPageId=fileManager.createNewHeaderPage(); //create a headerPage
        //PageID headerPageId=new PageID(0,0); diskManager.AllocPage(); 
        TableInfo tableInfo = new TableInfo("Table1", 4, tableCols, headerPageId); // Create the tableInfo

        Record record = new Record(tableInfo); // create the record
        record.setRecValues(recValues);
        
        //insert 3 records
        fileManager.InsertRecordIntoTable(record);
        //fileManager.displayPages(tableInfo);
        //bufferManager.flushAll();
        fileManager.InsertRecordIntoTable(record);
        //fileManager.displayPages(tableInfo);
        //bufferManager.flushAll();
        fileManager.InsertRecordIntoTable(record);
        //fileManager.displayPages(tableInfo);

        fileManager.InsertRecordIntoTable(record);
        //fileManager.displayPages(tableInfo);

        bufferManager.flushAll(); //write the record into the disk


        ArrayList<Record>listRecord=fileManager.getAllRecords(tableInfo);//get list of record

        //display records
        System.out.println("number of record : "+listRecord.size());
        for(int i=0;i<listRecord.size();i++){
            listRecord.get(i).displayRecord();
            System.out.println("\n");
        }
    }
}