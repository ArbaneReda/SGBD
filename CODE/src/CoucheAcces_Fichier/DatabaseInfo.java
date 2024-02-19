package CoucheAcces_Fichier;

import GestionEspaceDisque_et_Buffer.DBParams;
import GestionEspaceDisque_et_Buffer.PageID;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;


/**
 * Class DatabaseInfo that represents the information of a database
 */
public class DatabaseInfo implements Serializable {
    // this class should have only and only a unique instance that will be initiallized in the later TPs
    private static DatabaseInfo instance=new DatabaseInfo();
    private String databaseInfoFile="DBInfo.save";
    private ArrayList<TableInfo> informationTable;
    private int counterRelations;

    /**
     * ----------------   Constructor   ----------------
     * @param : informationTable : the list of all the tables in the database, counterRelations : the number of relations in the database
     */
    private DatabaseInfo() {
        informationTable = new ArrayList<>();
        counterRelations = 0;
    }

    // ----------------   Methods   ----------------
    
    /**
     * Method getInstance that returns the unique instance of DatabaseInfo
     * @return DatabaseInfo : the unique instance of DatabaseInfo
     */
    public static DatabaseInfo getInstance() {
       return instance;
    }

    /**
     * Method getHeaderPageID that returns the list of all the tables in the database
     * param : relationName : the name of the relation
     * @return PageID : the pageID of the header page of the relation
     */
    public PageID getHeaderPageID(String relationName) {
        TableInfo tableInfo = GetTableInfo(relationName);
        return tableInfo.getHeaderPageId();
    }

    /**
     * Method Init which is called at the beginning of the program
     * @param : nothing
     * @return void : nothing
     */
    public void Init() {
        Load();
    } // initialize an instance

    /**
     * Method Finish which is called at the end of the program
     * @param : nothing
     * @return void : nothing
     */
    public void Finish() {
        Save();
    } // for cleaning

    /**
     * Method AddTableInfo which adds a tableInfo to the list of all the tables in the database
     * @param : tabInfo : the tableInfo to add
     * @return void : nothing
     */
    public void AddTableInfo(TableInfo tabInfo) {
        informationTable.add(tabInfo);
        System.out.println("TableInfo added to DBInfo");
        counterRelations++;
    }

    /**
     * Method GetTableInfo which returns the tableInfo of a relation given its name
     * @param : nameRelation : the name of the relation
     * @return TableInfo : the tableInfo of the relation or null if the relation doesn't exist
     */
    public TableInfo GetTableInfo(String nameRelation) { // gives us information (from TableInfo) about
        // a certain relation depending on a given name in argument
        for (TableInfo tabInfo : informationTable) { // a for each loop
            System.out.print("Nom de la relation : ");
            System.out.println(tabInfo.getNameRelation());
            if ((tabInfo.getNameRelation()).equals(nameRelation)) {
                System.out.println("Cette table a été trouvée dans la base de données");
                return tabInfo;
            }
        }
        return null; // return null if none of them matches the given relation's name in the argument
    }

    /**
     * Method GetCounterRelations which returns the number of relations in the database
     * @param : nothing
     * @return int : the number of relations in the database
     */
    public int GetCounterRelations() {
        return counterRelations;
    }
    
    /**
     * Method Save which saves the informations of the database in a file
     * @param : nothing
     * @return void : nothing
     */
    public void Save() {
        try {
            // Complete path to the directory DB (depend on how you run the code)
            String filePath = DBParams.DBPath + File.separator + databaseInfoFile; // Combine the directory with fileName

            FileOutputStream fileOut = new FileOutputStream(filePath);
            ObjectOutputStream oos = new ObjectOutputStream(fileOut);
            oos.writeObject(this);
            oos.close();
            fileOut.close();

            System.out.println("####    Data has been written to " + filePath + "    ####");
            System.out.println("####    Data has been saved successfully    ####");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method Load which loads the informations of the database from a file
     * @param : nothing
     * @return void : nothing
     */
    public void Load() {
        try {
            String filePath = DBParams.DBPath + File.separator + databaseInfoFile;
            File file = new File(filePath);
    
            if (file.exists() && file.length() >0) {
                FileInputStream fileIn = new FileInputStream(filePath);
                ObjectInputStream ois = new ObjectInputStream(fileIn);
                DatabaseInfo dbInfo = (DatabaseInfo) ois.readObject();
                ois.close();
                fileIn.close();
                
                // Mettez à jour l'instance actuelle avec les nouvelles informations chargées
                instance = dbInfo;
                System.out.println("####    Data has been loaded to unique instance of DBinfo (updated)  ####");
            } else {
                //if the file doesn't exist we create it
                file = new File(filePath);
                file.createNewFile();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    

    /**
     * Method toString which returns the informations of the database
     * @param : nothing
     * @return String : the informations of the database
     */
    @Override
    public String toString(){
        String informationDatabase="";
        System.out.println("####    size of relation : "+informationTable.size() +"    ####");
        for(TableInfo table: informationTable ){
            informationDatabase+=table.toString();
            informationDatabase+="\n";
        }
        return informationDatabase;
    }

    /**
     * Method resetDataBaseInfo which resets the informations of the database
     * @param : nothing
     * @return void : nothing
     */
    public void resetDataBaseInfo(){
        informationTable = new ArrayList<>();
        counterRelations = 0;
        
    }
}
