package CoucheAcces_Fichier;

import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * Class Record that represents a record
 */
public class Record {
    private TableInfo tabInfo;
    private ArrayList<String> recValues;
    private boolean deletedRecord=false;
    private RecordId recordId;

    /**
     * ----------------   Constructor   ----------------
     * @param : tableInfo : the tableInfo of the record
     */
    public Record(TableInfo tableInfo) {
        this.tabInfo = tableInfo;
        this.recValues = new ArrayList<>();
    }

    // ----------------   Methods   ----------------

    /**
     * Method setRecValues which is called to set the recValues of a record
     * @param : recVals : the list of values of the record
     * @return void : nothing
     */
    public void setRecValues(ArrayList<String> recVals) {
        recValues = recVals;
    }

    /**
     * Method setAsDeleted which is called to set a record as deleted, it means that the record is not in the database anymore
     * @param : nothing
     * @return void : nothing
     */
    public void setAsDeleted(){
        this.deletedRecord=true;
    }

    /**
     * Method isDeleted which is called to know if a record is deleted or not
     * @param : nothing
     * @return boolean : true if the record is deleted, false otherwise
     */
    public boolean isDeleted(){
        return this.deletedRecord;
    }

    /**
     * Method getRecValues which is called to retrieve the recValues of a record
     * @param : nothing
     * @return ArrayList<String> : the list of values of the record
     */
    public ArrayList<String> getRecValues() {
        return recValues;
    }

    /**
     * Method writeToBuffer which is called to write the record to a buffer, it's used to write the record to a page
     * @param : buff : the buffer to write to
     * @param : pos : the position in the buffer
     * @return int : the total size of the record
     */
    public int writeToBuffer(ByteBuffer buff, int pos) {
        try {
            buff.position(pos);
            int totalSize = 0;
            for (int i = 0; i < tabInfo.getNumberCols(); i++) {
                String value = recValues.get(i);

                switch (tabInfo.getTableCols().get(i).getTypeCol()) {
                    case INT:
                        int intValue = Integer.parseInt(value);
                        buff.putInt(intValue);
                        totalSize += 4; // 4 bytes for an int
                        break;
                    case FLOAT:
                        // Store the FLOAT value as binary (no conversion, we've made it this way to avoid on certain problems)
                        float floatValue = Float.parseFloat(value);
                        buff.putFloat(floatValue);
                        totalSize += 4;  // 4 bytes for a float
                        break;
                    case STRING:
                        // Write a fixed-length STRING
                        String stringValue = value;
                        int stringLength = tabInfo.getTableCols().get(i).getLengthString();
                        byte[] stringBytes = new byte[stringLength];
                        byte[] valueBytes = stringValue.getBytes();

                        // Copy the needed part of the value string into the STRING column
                        System.arraycopy(valueBytes, 0, stringBytes, 0, Math.min(valueBytes.length, stringLength));
                        
                        buff.put(stringBytes);
                        totalSize += stringLength;
                        break;
                    case VARSTRING:
                        // Store the VARSTRING value as binary with its length
                        int varStringLength = value.length();
                        buff.putInt(varStringLength);
                        byte[] varStringBytes = value.getBytes();
                        buff.put(varStringBytes);
                        totalSize += 4 + varStringLength;
                        break;
                    default:
                        System.out.println("Column type not supported");
                        break;
                }
            }
            return totalSize;
        } catch (Exception e) {
            System.err.println("Error in writeToBuffer: " + e.getMessage());
            return -1; // Or another error value as needed
        }
    }

    /**
     * Method sizeRecord which is called to get the size of a record
     * @param : nothing
     * @return int : the size of the record
     */
    public int sizeRecord(){
         int totalSize = 0;
            for (int i = 0; i < tabInfo.getNumberCols(); i++) {
                
                String value = recValues.get(i);

                switch (tabInfo.getTableCols().get(i).getTypeCol()) {
                    case INT:
                        totalSize += 4;
                        break;
                    case FLOAT:

                        totalSize += 4;
                        break;
                    case STRING:                        
                        int stringLength = tabInfo.getTableCols().get(i).getLengthString();
                        totalSize += stringLength;
                        break;
                    case VARSTRING:
                        int varStringLength = value.length();
                        totalSize += 4 + varStringLength;
                        break;
                    default:
                        System.out.println("Column type not supported");
                        break;
                }
            }
            return totalSize;
    }

    /**
     * Method readFromBuffer which is called to read a record from a buffer, it's used to read a record from a page
     * @param : buff : the buffer to read from
     * @param : pos : the position in the buffer
     * @return int : the total size of the record
     */
    public int readFromBuffer(ByteBuffer buff, int pos) {
        try {
            recValues.clear();
            buff.position(pos);
            int totalSize = 0;

            for (int i = 0; i < tabInfo.getNumberCols(); i++) {
                switch (tabInfo.getTableCols().get(i).getTypeCol()) {
                    case INT:
                        int intValue = buff.getInt(); 
                        recValues.add(Integer.toString(intValue));
                        totalSize += 4;
                        break;
                    case FLOAT:
                        float floatValue = buff.getFloat();
                        recValues.add(Float.toString(floatValue));
                        totalSize += 4;
                        break;
                    case STRING:
                        int stringLength = tabInfo.getTableCols().get(i).getLengthString();
                        byte[] stringBytes = new byte[stringLength];
                        buff.get(stringBytes);
                        String stringValue = new String(stringBytes).trim(); // remove spaces or extra-padding
                        recValues.add(stringValue);
                        totalSize += stringLength;
                        break;
                    case VARSTRING:
                        int varStringLength = buff.getInt();
                        byte[] varStringBytes = new byte[varStringLength];
                        buff.get(varStringBytes);
                        String varStringValue = new String(varStringBytes);
                        recValues.add(varStringValue);
                        totalSize += 4 + varStringLength;
                        break;
                    default:
                        System.out.println("Column type not supported");
                        break;
                }
            }
            return totalSize;
        } catch (Exception e) {
            System.err.println("Error in readFromBuffer: " + e.getMessage());
            return -1; // Or another error value as needed
        }
    }

    /**
     * Method getTabInfo which is called to get the tableInfo of a record
     * @param : nothing
     * @return TableInfo : the tableInfo of the record
     */
    public TableInfo getTableInfo(){
        return this.tabInfo;
    }

    /**
     * Method displayRecord which is called to display the values of a record, it's used for testing
     * @param : nothing
     * @return void : nothing
     */
    public void displayRecord(){
        for(int i=0;i<recValues.size();i++){
            System.out.print(recValues.get(i));
        }
        System.out.println("\n");
    }

    /**
     * Method compare which is called to compare two records
     * @param : record : the record to compare with
     * @return boolean : true if the records are the same, false otherwise
     */
    public boolean compare(Record record){

        if(record.getRecValues().isEmpty()|| this.recValues.isEmpty()){
            return false;
        }
        
        for(int i=0;i<recValues.size();i++){
            if(!recValues.get(i).equals(record.getRecValues().get(i))){
                
                return false;
            }
        }
        return true;
    }

    /*
     * Method to get a record value to a specific column
     * 
     * @param colIndex : the index of the column
     */
    public String getRecValue(int colIndex) {
        return recValues.get(colIndex);
    }

    /*
     * Method to get the RecordId of a record
     */
    public RecordId getRecordId() {
        return recordId;
    }

}
