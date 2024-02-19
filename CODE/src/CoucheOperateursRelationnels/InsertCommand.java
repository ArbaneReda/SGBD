package CoucheOperateursRelationnels;

import CoucheAcces_Fichier.DatabaseInfo;
import CoucheAcces_Fichier.FileManager;
import CoucheAcces_Fichier.Record;
import CoucheAcces_Fichier.TableInfo;

import java.util.ArrayList;

/**
 * Class InsertCommand that represents the command to insert a record
 */
public class InsertCommand {
    private String userCommand;
    private String relationName;
    private ArrayList<String> values;

    /**
     * ----------------   Constructor   ----------------
     * @param : userCommand : the command to insert a record
     */
    public InsertCommand(String userCommand) {

        this.userCommand = userCommand;
        
        String[] commandSplit = userCommand.split(" ");
        this.relationName = commandSplit[2];
        this.relationName = commandSplit[2]; // 3 because "INSERT INTO" has 2 words and we want to skip the space after it

        // Parsing des valeurs entre parenthèses
        String valuesStr = userCommand.substring(userCommand.indexOf('(') + 1, userCommand.lastIndexOf(')'));
        String[] valuesSplit = valuesStr.split(",");

        // Initialisation de la liste des valeurs
        this.values = new ArrayList<>();
        for (String value : valuesSplit) {
            // Nettoyage des espaces autour de la valeur et ajout à la liste
            this.values.add(value.trim());
        }
    }

    // ----------------   Methods   ----------------

    /**
     * Method printInsertCommand that prints the command to insert a record
     * @param : nothing
     * @return : nothing
     */
    public void printInsertCommand() {
        System.out.println("====    the insert command has the following information:    ====\n");
        System.out.println("User command: " + this.userCommand);
        System.out.println("Relation name: " + this.relationName);
        System.out.println("Values: " + this.values);
    }

   

    /**
     * Method Execute that executes the command to insert a record
     * @param : nothing
     * @return : nothing
     */
    public void Execute() {
        // Get the table info
        TableInfo tableInfo = DatabaseInfo.getInstance().GetTableInfo(this.relationName);

        // Create a record
        Record record = new Record(tableInfo);
        for (int i = 0; i < this.values.size(); i++) {
            record.getRecValues().add(this.values.get(i));
        }

        // Insert the record into the table
        FileManager.getFileManager().InsertRecordIntoTable(record);       
    }

}
