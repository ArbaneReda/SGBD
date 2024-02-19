package CoucheOperateursRelationnels;

import CoucheAcces_Fichier.DatabaseInfo;
import CoucheAcces_Fichier.TableInfo;
import CoucheAcces_Fichier.FileManager;

import java.lang.Integer;
import java.util.ArrayList;
import java.util.List;

import CoucheAcces_Fichier.Record;


//Class CreateIndexCommand that represents the creation of an index
 
public class CreateIndexCommand {

    @SuppressWarnings("unused")
    private String userCommand;
    private String relationName;
    private String columnName;
    private String order;
    private IndexBPlusTree indexBTree;

    // Constructor for the CreateIndexCommand class using the parsing of the
    // userCommand
    public CreateIndexCommand(String userCommand) {
        this.userCommand = userCommand;

        // Split the user command to extract relevant information
        String[] commandSplit = userCommand.split(" ");
        this.relationName = commandSplit[2].trim(); // Relation name is the 3rd word in the command

        this.columnName = commandSplit[3].split("=")[1].trim(); // Extracting the column name from KEY=nomColonne

        this.order = commandSplit[4].split("=")[1].trim(); // Extracting the order from ORDER=ordre
    }

    //Method to get the indexBTree of the created index
    public IndexBPlusTree getCreatedIndexBTree() {
        return this.indexBTree;
    }

    // Method to execute the CreateIndexCommand
    public void Execute() {
        // Récupérer les informations sur la table
        TableInfo tableInfo = DatabaseInfo.getInstance().GetTableInfo(this.relationName);

        // Créer un nouvel index B+Tree en mémoire
        this.indexBTree = new IndexBPlusTree(Integer.parseInt(this.order)); 

        // Collecter les valeurs de la colonne spécifiée
        FileManager fileManager = FileManager.getFileManager();
        ArrayList<Record> records = fileManager.getAllRecords(tableInfo);

        // récupérer dans chaque record la valeur de la colonne spécifiée
        for (Record record : records) {
            String columnValue = record.getRecValue(tableInfo.getColIndex(this.columnName));

            // Ajouter la valeur à l'index
            indexBTree.insert(columnValue, record.getRecordId());

        }

        // Imprimer les clés de l'arbre après l'insertion
        System.out.println("");
        System.out.println("====    Keys in the B+Tree:   ====");
        List<String> keys = indexBTree.getKeys();
        for (String key : keys) {
            System.out.println("Key: " + key);
        }

        System.out.println("######    Index created successfully   ######");

    }
}