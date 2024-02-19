package CoucheOperateursRelationnels;

import CoucheAcces_Fichier.DatabaseInfo;
import CoucheAcces_Fichier.FileManager;
import CoucheAcces_Fichier.Record;
import CoucheAcces_Fichier.TableInfo;
import GestionEspaceDisque_et_Buffer.PageID;
import java.util.ArrayList;

/**
 * Class DeleteRecordsCommand that represents the command to delete records
 */
public class DeleteRecordsCommand {
    
    @SuppressWarnings("unused") // it's used only in the constructor
    private String userCommand;
    private ArrayList<String> listRelationNames;
    private ArrayList<SelectCondition> conditions;
    private boolean condition=false;
    
    /**
     * ----------------   Constructor   ----------------
     * @param : userCommand : the command to delete records
     */
    public DeleteRecordsCommand(String userCommand) {
        this.userCommand = userCommand;

        // Extraction of the relation name
        String[] commandSplit = userCommand.split(" ");

        for(String elements:commandSplit){
            if(elements.equals("WHERE")){
                this.condition=true;
                break;

            }
        }
       

        String relationNames = commandSplit[3]; // get the raltions in the command
        this.listRelationNames = parseRelationNames(relationNames); // get the relations 

        //verifie if the select have a condition 

        if(this.condition==true){
            
            String conditionsStr = ""; // parse the conditions
            int whereIndex = userCommand.indexOf("WHERE");
            
            if (whereIndex != -1) {
                conditionsStr = userCommand.substring(whereIndex + 6).trim(); // 6 because "WHERE" has 5 characters and we
                                                                            // want to skip the space after it
            }

            this.conditions = parseConditions(conditionsStr);
            // conditions now contains the parsed conditions (only the String part after the
            // WHERE)
          
        }
    }

    // ----------------   Methods   ----------------
    
    /**
     * Method parseRelationNames which is called to parse the relation names
     * @param : relationNames : the relation names
     * @return ArrayList<String> : the parsed relation names
     */
    private ArrayList<String> parseRelationNames(String relationNames){
        ArrayList<String> parsedRelationNames = new ArrayList<>();

        if(!relationNames.isEmpty()){
            String[] relationNamesSplit = relationNames.split(","); // split the relations based on the comma
            for(String relationName:relationNamesSplit){
                parsedRelationNames.add(relationName.trim()); // add the relation name to the ArrayList
            }
        }
        else{
            parsedRelationNames.add(relationNames.trim()); // add the relation name to the ArrayList
        }
        
        return parsedRelationNames;
    }


    /**
     * Method parseConditions which is called to parse the conditions
     * @param : conditionsStr : the conditions
     * @return ArrayList<SelectCondition> : the parsed conditions
     */
    private ArrayList<SelectCondition> parseConditions(String conditionsStr) {
        ArrayList<SelectCondition> parsedConditions = new ArrayList<>();

        // check if there are conditions
        if (!conditionsStr.isEmpty()) {
            // parse the conditions and add them to the ArrayList
            String[] conditionsSplit = conditionsStr.split(" AND "); // spaces included in the AND
            for (String conditionStr : conditionsSplit) {
                // parse the condition
                parsedConditions.add(parseEachCondition(conditionStr.trim()));
            }
        } else {
            
            // No conditions specified in the command
            parsedConditions.add(new SelectCondition()); // add an empty condition
        }

      

        return parsedConditions;
    }

    /**
     * Method parseEachCondition which is called to parse each condition, it's analyzed each condition and return a SelectCondition
     * @param : conditionStr : the condition
     * @return SelectCondition : the parsed condition
     */
    private SelectCondition parseEachCondition(String conditionStr) {

        return parseConditionValue(conditionStr);
                
    }

    /**
     * Method parseConditionValue which is called to parse the condition value
     * @param : conditionStr : the condition
     * @return SelectCondition : the parsed condition
     */
    private SelectCondition parseConditionValue(String conditionStr) {
        // parse the condition

         // parse the condition
        String[] parts = conditionStr.split("<=|>=|<>|<|>|="); // split the condition based on the operator

        String [] parseColumnName = parts[0].split("\\.");
        
        String relationName=parseColumnName[0].trim(); // the first part is the relation name
        String columnName = parseColumnName[1].trim(); // the second part is the column name
        
        String value = parts[1].trim(); // the second part is the value (it's technically the third part and the
                                        // operator is the second part)
        String operator = conditionStr.substring(relationName.length()+columnName.length()+1, conditionStr.length() - value.length()).trim();
        // the operator is the remaining part of the condition after removing the column
        // name and the value

        return new SelectCondition(columnName, operator, value, relationName);
    }

    /**
     * Method Execute which is called to execute the command to delete records
     * @param : nothing
     * @return nothing
     */
    public void Execute() {
        int nbDeletedRecords=0;
        // get the table info
        TableInfo tableInfo = DatabaseInfo.getInstance().GetTableInfo(this.listRelationNames.get(0));

        // get all the records from the table
        
        ArrayList<Record> records = FileManager.getFileManager().getAllRecords(tableInfo);

        // filter the records based on the conditions
        ArrayList<Record> selectedRecords = new ArrayList<>();

        //filter records if there is a condition
        if(this.condition){
            
            for (Record record : records) {
                if(record.isDeleted()==false){
                    System.out.println("record : "+record.getRecValues().toString()+"  is deleted");
                    if (satisfiesConditions(record)) {
                        selectedRecords.add(record);
                    }
                }
            }

        }

        //select all the records
        else{  
            for (Record record : records) {
                if(record.isDeleted()==false){
                    selectedRecords.add(record);
                }
                
            }
        }

        System.out.println("selected Recods  "+selectedRecords.size());

        // print the selected records
        //printSelectedRecords(selectedRecords);

        FileManager fileManager=FileManager.getFileManager();
        ArrayList<PageID> listDataPages =fileManager.getDataPages(tableInfo) ; //get all dataPages of a relation
        

        for(PageID pageID : listDataPages){
           int indexRecord=0;
           
                        
            ArrayList<Record> recordsInPage = fileManager.getRecordsInDataPage(tableInfo, pageID); //get all records of a dataPage

            for(Record record : recordsInPage){

                for(Record selectedRecord : selectedRecords){
                    if(selectedRecord.isDeleted()==false){
                        
                        if(record.compare(selectedRecord)){
                            
                            fileManager.deleteRecordToDataPage(pageID, indexRecord);
                            nbDeletedRecords++;
                        }
                    }
                }

                indexRecord++;
            }
        }
        System.out.println("number of records deleted : "+nbDeletedRecords);

    }

    /**
     * Method satisfiesConditions which is called to check if the record satisfies the conditions
     * @param : record : the record
     * @return boolean : true if the record satisfies the conditions, false otherwise
     */
     private boolean satisfiesConditions(Record record) {
        
        for (SelectCondition condition : conditions) {
            if(condition.getFirstRelationName().equals(record.getTableInfo().getNameRelation()) && !condition.getTypeCondition()){ //if the condition is applied to the record
                if (!condition.isSatisfiedBy(record)) {
                    return false; // if one condition is not satisfied, the record is not selected
                }
            }
        }

        return true; // all conditions are satisfied
    }



}
