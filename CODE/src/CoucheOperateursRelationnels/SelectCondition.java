package CoucheOperateursRelationnels;

import CoucheAcces_Fichier.ColInfo;
import CoucheAcces_Fichier.ColumnType;
import CoucheAcces_Fichier.DatabaseInfo;
import CoucheAcces_Fichier.Record;
import CoucheAcces_Fichier.TableInfo;
import java.util.ArrayList;
import java.util.List;



/**
 * Class SelectCondition that represents the condition of a select command
 */
public class SelectCondition {
    private String operator; // =, <, >, <=, >=, !=
    private String value; // the value to compare with
    private String firstColumnName;
    private String secondColumnName;
    private String firstRelationName;
    private String secondRelationName;
    private Boolean isJointCondition=false;

    /**
     * ----------------   Constructor   ----------------
     * @param : columnName : the name of the column
     * @param : operator : the operator of the condition
     * @param : value : the value to compare with
     * @param : relationName : the name of the relation
     */
    public SelectCondition(String columnName, String operator, String value,String relationName) {
        this.firstColumnName = columnName;
        this.firstRelationName=relationName;
        this.operator = operator;
        this.value = value;
        this.secondColumnName=null;
        this.secondRelationName=null;
    }

    /**
     * ----------------   Constructor   ----------------
     * @param : firstRelationName : the name of the first relation
     * @pram : firstColumnName : the name of the first column, 
     * @param : secondRelationName : the name of the second relation
     * @param : secondColumnName : the name of the second column
     * @param : operator : the operator of the condition
     * secondRelationName : the name of the second relation, secondColumnName : the name of the second column, operator : the operator of the condition
     */
    public SelectCondition(String firstRelationName,String firstColumnName,String secondRelationName,String secondColumnName,String operator){
        this.firstColumnName=firstColumnName;
        this.firstRelationName=firstRelationName;
        this.secondColumnName=secondColumnName;
        this.secondRelationName=secondRelationName;
        this.operator=operator;
        this.value=null;
        this.isJointCondition=true;
    }

    /**
     * ----------------   Constructor   ----------------
     * @param : nothing
     */
    public SelectCondition() {
        this.firstColumnName = null;
        this.secondColumnName=null;
        this.operator = null;
        this.value = null;
        this.secondRelationName=null;
        this.firstRelationName=null;
    }

    // ----------------   Methods   ----------------

    /**
     * Method getFirstColumnName that returns the name of the first column
     * @param : nothing
     * @return String : name of the first column
     */
    public String getFirstColumnName() {
        return this.firstColumnName;
    }
    
    /**
     * Method getSecondColumnName that returns the name of the second column
     * @param : nothing
     * @return String : name of the second column
     */
    public String getSecondColumnName(){
        return this.secondColumnName;
    }
    
    /**
     * Method getOperator that returns the operator of the condition
     * @param : nothing
     * @return String : operator of the condition
     */
    public String getOperator() {
        return this.operator;
    }

    /**
     * Method getValue that returns the value of the condition
     * @param : nothing
     * @return String : value of the condition
     */
    public String getValue() {
        return this.value;
    }

    /**
     * Method getFirstRelationName that returns the name of the first relation
     * @param : nothing
     * @return String : name of the first relation
     */
    public String getFirstRelationName(){
        return this.firstRelationName;
    }

    /**
     * Method getSecondRelationName that returns the name of the second relation
     * @param : nothing
     * @return String : name of the second relation
     */
    public String getSecondRelationName(){
        return this.secondRelationName;
    }

    /**
     * Method getTypeCondition that returns the type of the condition
     * @param : nothing
     * @return Boolean : type of the condition
     */
    public Boolean getTypeCondition(){
        return this.isJointCondition;
    }

    /**
     * Method isSatisfiedBy that check if the condition is satisfied by the record
     * @param : record : the record to check
     * @return Boolean : true if the condition is satisfied by the record, false otherwise
     */
    public boolean isSatisfiedBy(Record record) {
        // Get the value of the column in the record
        TableInfo tableInfo =record.getTableInfo();

        int columnIndex = getColumnIndex(firstColumnName, tableInfo);
        ColumnType columnType=getColumnType(firstColumnName,tableInfo);
        

        String columnValue = record.getRecValues().get(columnIndex);
        if (columnValue == null) {
            // Handle the case where the column value is null
            System.out.println(
                    "The column " + firstColumnName + " has a null value in the record because of a certain problem");
            return false;
        }

        String newValue=value;

        if(columnType==ColumnType.FLOAT){
            newValue=value.replace(",", ".");
        }
        else{
            newValue=value;
        }

    

        // Compare the value with the condition
        switch (operator) {
            
            case "=":
            if(columnType==ColumnType.INT || columnType==ColumnType.FLOAT){
                
                

                return Float.parseFloat(columnValue)==(Float.parseFloat(newValue));
            }
                
            else{
                return columnValue.equals(value); //string
            }
                
            case "<":
             if(columnType==ColumnType.INT || columnType==ColumnType.FLOAT){
                 return Float.parseFloat(columnValue)<(Float.parseFloat(newValue));
                }
                else{
                    return columnValue.compareTo(value) < 0;
                }
               
            case ">":
                if(columnType==ColumnType.INT || columnType==ColumnType.FLOAT){
                 return Float.parseFloat(columnValue)>(Float.parseFloat(newValue));
                }
                else{
                    return columnValue.compareTo(value) > 0;
                }
                
            case "<=":
                if(columnType==ColumnType.INT || columnType==ColumnType.FLOAT){
                     
                     return Float.parseFloat(columnValue)<=(Float.parseFloat(newValue));
                }
                else{
                    return columnValue.compareTo(value) <= 0;
                }
               
            case ">=":
                if(columnType==ColumnType.INT || columnType==ColumnType.FLOAT){
                   
                    return Float.parseFloat(columnValue)>=(Float.parseFloat(newValue));
                }
                else{
                    return columnValue.compareTo(value) >= 0;
                }
                
            case "<>":
                 if(columnType==ColumnType.INT || columnType==ColumnType.FLOAT){

                    return Float.parseFloat(columnValue)!=(Float.parseFloat(newValue));
                    }
                
                else{
                    return !columnValue.equals(value); //string

                }
            default:
                return false;
        }
    }

    /**
     * Method joinRelations that join two relations
     * @param : selectCondition : the condition of the join
     * @param : selectedRecordsFirstRelation : the selected records of the first relation
     * @param : selectedRecordsSecondRelation : the selected records of the second relation
     * @return ArrayList<ArrayList<Record>> : the joined records
     */
    public static ArrayList<ArrayList<Record>> joinRelations(SelectCondition selectCondition,ArrayList<Record> selectedRecordsFirstRelation, ArrayList<Record> selectedRecordsSecondRelation){
        
        String columnNameFirstRelation=selectCondition.getFirstColumnName(); //get the column name of the condition joint
        String columnNameSecondRelarion=selectCondition.getSecondColumnName(); //get the column name of the condition joint

        String operator=selectCondition.getOperator();  //get the oparator of the condition joint
        String firstRelationName=selectCondition.getFirstRelationName(); //get the name of the first relation
        String secondRelationName=selectCondition.getSecondRelationName(); //get the name of the second relation

        TableInfo firstTableInfo= DatabaseInfo.getInstance().GetTableInfo(firstRelationName);
        TableInfo secondTableInfo=DatabaseInfo.getInstance().GetTableInfo(secondRelationName); //get the table info of the second relation

        int columnIndexFirstRelation=selectCondition.getColumnIndex(columnNameFirstRelation,firstTableInfo); //get the index of the column in the first relation
        int columnIndexSecondRelation=selectCondition.getColumnIndex(columnNameSecondRelarion,secondTableInfo); //get the index of the column in the second relation

        ColumnType columnType=selectCondition.getColumnType(columnNameFirstRelation,firstTableInfo);

       
        return joinRecords(selectedRecordsFirstRelation,selectedRecordsSecondRelation,columnIndexFirstRelation,columnIndexSecondRelation,operator,columnType);
    }

    /**
     * Method joinRecords that join two records
     * @param : selectedRecordsFirstRelation : the selected records of the first relation
     * @param : selectedRecordsSecondRelation : the selected records of the second relation
     * @param : columnIndexFirstRelation : the index of the column in the first relation
     * @param : columnIndexSecondRelation : the index of the column in the second relation
     * @param : operator : the operator of the condition joint
     * @return ArrayList<ArrayList<Record>> : the joined records
     */
    public static ArrayList<ArrayList<Record>> joinRecords(ArrayList<Record> selectedRecordsFirstRelation, ArrayList<Record> selectedRecordsSecondRelation, int columnIndexFirstRelation, int columnIndexSecondRelation, String operator, ColumnType columnType) {
       
        ArrayList<ArrayList<Record>> joinedRecords=new ArrayList<ArrayList<Record>>();

        for(int i=0;i<selectedRecordsFirstRelation.size();i++){
            Record recordFirstRelation=selectedRecordsFirstRelation.get(i); //select a record from the first relation
           

            
            String columnValueFirstRelation=recordFirstRelation.getRecValues().get(columnIndexFirstRelation); //get the value of the column in the first relation

            for(int j=0;j<selectedRecordsSecondRelation.size();j++){
                 ArrayList<Record> newRecord=new ArrayList<Record>(); //it a that containe [record1,record2]
                newRecord.add(recordFirstRelation);

                Record recordSecondRelation=selectedRecordsSecondRelation.get(j); //Select a record from the second relation
                String columnValueSecondRelation=recordSecondRelation.getRecValues().get(columnIndexSecondRelation); //get the value of the column in the second relation


                if(columnType==ColumnType.INT || columnType==ColumnType.FLOAT){
                    columnValueFirstRelation=columnValueFirstRelation.replace(",", ".");
                    columnValueSecondRelation=columnValueSecondRelation.replace(",", ".");
                }

                switch(operator){

                    case "=":

                    if(columnType==ColumnType.INT || columnType==ColumnType.FLOAT){
                         if(Float.parseFloat(columnValueFirstRelation)==(Float.parseFloat(columnValueSecondRelation))){

                            newRecord.add(recordSecondRelation);
                            joinedRecords.add(newRecord);
                         }
                         
                    }

                    else{
                        if(columnValueFirstRelation.equals(columnValueSecondRelation)){
                            newRecord.add(recordSecondRelation);
                            joinedRecords.add(newRecord);
                        }
                    }

                    
                        break;
                    case "<":
                        if(columnType==ColumnType.INT || columnType==ColumnType.FLOAT){
                         if(Float.parseFloat(columnValueFirstRelation)<(Float.parseFloat(columnValueSecondRelation))){

                            newRecord.add(recordSecondRelation);
                            joinedRecords.add(newRecord);
                         }
                         
                    }

                    else{
                        if(columnValueFirstRelation.compareTo(columnValueSecondRelation)<0){
                            newRecord.add(recordSecondRelation);
                            joinedRecords.add(newRecord);
                        }
                    }

                        break;
                    case ">":
                        if(columnType==ColumnType.INT || columnType==ColumnType.FLOAT){
                         if(Float.parseFloat(columnValueFirstRelation)>(Float.parseFloat(columnValueSecondRelation))){

                            newRecord.add(recordSecondRelation);
                            joinedRecords.add(newRecord);
                         }
                         
                    }

                    else{
                        if(columnValueFirstRelation.compareTo(columnValueSecondRelation)>0){
                            newRecord.add(recordSecondRelation);
                            joinedRecords.add(newRecord);
                        }
                    }

                        break;
                    case "<=":
                        if(columnType==ColumnType.INT || columnType==ColumnType.FLOAT){
                         if(Float.parseFloat(columnValueFirstRelation)<=(Float.parseFloat(columnValueSecondRelation))){

                            newRecord.add(recordSecondRelation);
                            joinedRecords.add(newRecord);
                         }
                         
                    }

                    else{
                        if(columnValueFirstRelation.compareTo(columnValueSecondRelation)<=0){
                            newRecord.add(recordSecondRelation);
                            joinedRecords.add(newRecord);
                        }
                    }

                        break;
                    case ">=":
                         if(columnType==ColumnType.INT || columnType==ColumnType.FLOAT){
                         if(Float.parseFloat(columnValueFirstRelation)>=(Float.parseFloat(columnValueSecondRelation))){

                            newRecord.add(recordSecondRelation);
                            joinedRecords.add(newRecord);
                         }
                         
                    }

                    else{
                        if(columnValueFirstRelation.compareTo(columnValueSecondRelation)>=0){
                            newRecord.add(recordSecondRelation);
                            joinedRecords.add(newRecord);
                        }
                    }

                        break;
                    case "<>":
                        if(columnType==ColumnType.INT || columnType==ColumnType.FLOAT){
                         if(Float.parseFloat(columnValueFirstRelation)!=(Float.parseFloat(columnValueSecondRelation))){

                            newRecord.add(recordSecondRelation);
                            joinedRecords.add(newRecord);
                         }
                         
                    }

                    else{
                        if(!columnValueFirstRelation.equals(columnValueSecondRelation)){
                            newRecord.add(recordSecondRelation);
                            joinedRecords.add(newRecord);
                        }
                    }

                        break;
                    default:
                        break;
                }

                
            }
        }

        return joinedRecords;
    }

    /**
     * Method getColumnIndex that returns the index of the column
     * @param : columnName : the name of the column
     * @param : tableInfo : the table info of the relation
     * @return int : the index of the column
     */
    private int getColumnIndex(String columnName, TableInfo tableInfo) {

        int columnIndex = -1;
        List<ColInfo> tableCols = tableInfo.getTableCols();

        for (int i = 0; i < tableCols.size(); i++) {
            if (tableCols.get(i).getNameCol().equals(columnName)) {
                columnIndex = i;
                break;
            }
        }
        return columnIndex;
    }

    /**
     * Method getColumnType that returns the type of the column
     * @param : columnName : the name of the column
     * @param : tableInfo : the table info of the relation
     * @return ColumnType : the type of the column
     */
    private ColumnType getColumnType(String columnName, TableInfo tableInfo) {

        int columnIndex = -1;
        List<ColInfo> tableCols = tableInfo.getTableCols();

        for (int i = 0; i < tableCols.size(); i++) {
            if (tableCols.get(i).getNameCol().equals(columnName)) {
                columnIndex = i;
                break;
            }
            
        }

        
        return tableCols.get(columnIndex).getTypeCol();
    }

    /**
     * Method toString that returns the condition
     * @param : nothing
     * @return String : the condition
     */
    @Override
    public String toString() {
        return firstRelationName + " " +firstColumnName+" "+secondRelationName+" "+secondColumnName+" "+ operator + " " + value;
    }

}

