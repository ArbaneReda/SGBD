package CoucheAcces_Fichier;

import java.io.Serializable;

/**
 * Class ColInfo that represents the information of a column
 */
public class ColInfo implements Serializable{ // we've separated each column's info as advised in the TP
    private String nameCol;
    private ColumnType typeCol;
    private int lengthString;
    
    /**
     * ----------------   Constructor   ----------------
     * 
     * @param : nameCol : the name of the column, typeCol : the type of the column
     * @param : lengthString : the length of the column
     */
    public ColInfo(String nameCol, ColumnType typeCol, int lengthString) {
        this.nameCol = nameCol;
        this.typeCol = typeCol;
        this.lengthString = lengthString;
    }

// ----------------   Methods   ----------------

    /* *
    Method getNameCol that returns the name of a column
    @param : nothing
    @return String : name of the column
    */
    public String getNameCol() {
        return nameCol;
    }

    /**
    Method getTypeCol that returns the type of a column
    @param : nothing
    @return ColumnType : type of the column
    */
    public ColumnType getTypeCol() {
        return typeCol;
    }

    /**
    Method getLengthString that returns the length of a column
    @param : nothing
    @return int : length of the column
    */
    public int getLengthString() {
        return lengthString;
    }

    /**
    Method toString that returns the name, type and length of a column
    @param : nothing
    @return String : name, type and length of the column
    */
    public String toString(){
        return "name : "+nameCol+" type : "+typeCol+ " lengthString : "+lengthString+" ";
    }
}
