package CoucheOperateursRelationnels;

import CoucheAcces_Fichier.DatabaseInfo;
import CoucheAcces_Fichier.FileManager;
import CoucheAcces_Fichier.Record;
import CoucheAcces_Fichier.RecordId;
import CoucheAcces_Fichier.TableInfo;

import java.util.ArrayList;
import java.util.List;

public class SelectIndexCommand {

    @SuppressWarnings("unused")
    private String userCommand;
    private String relationName;
    @SuppressWarnings("unused")
    private String columnName;
    private String value;
    private IndexBPlusTree indexBTree;

    // Constructor for the SelectIndexCommand class using the parsing of the userCommand
    public SelectIndexCommand(String userCommand, CreateIndexCommand createIndexCommand) {
        this.indexBTree = createIndexCommand.getCreatedIndexBTree();
        this.userCommand = userCommand;

        // Split the user command to extract relevant information
        String[] commandSplit = userCommand.split(" ");
        this.relationName = commandSplit[3].trim(); // Relation name is the 4th word in the command

        this.columnName = commandSplit[5].split("=")[0].trim(); // Extracting the column name from nomColonne=valeur
        this.value = commandSplit[5].split("=")[1].trim(); // Extracting the value from nomColonne=valeur
    }

    // Method to execute the SelectIndexCommand
public void Execute() {
    // Récupérer les informations sur la table
    TableInfo tableInfo = DatabaseInfo.getInstance().GetTableInfo(this.relationName);

    // Récupérer l'index de la colonne spécifiée
    //int columnIndex = tableInfo.getColIndex(this.columnName);

    // Récupérer les RecordId correspondant à la valeur spécifiée
    List<RecordId> matchingRIDs = this.indexBTree.search(this.value);

    // Print the header before the loop
    System.out.println("======================= Selected Records: =======================");

    // Flag to check if any records were selected
    boolean recordsSelected = false;

    // Récupérer le record correspondant au RecordId
    FileManager fileManager = FileManager.getFileManager();
    ArrayList<Record> records = fileManager.getAllRecords(tableInfo);

    for (Record record : records) {
        if (matchingRIDs.contains(record.getRecordId())) {
            // Print record details
            String recordValues = String.join("; ", record.getRecValues());
            System.out.println(recordValues + ".");
            
            // Set the flag to true if at least one record is selected
            recordsSelected = true;
        }
    }

    // Print a message if no records were selected
    if (!recordsSelected) {
        System.out.println("No records found for the specified SELECTION.");
    }

    System.out.println("###### Selection completed successfully ######");
}

}
