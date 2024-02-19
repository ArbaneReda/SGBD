package CoucheAcces_Fichier.Classes_Testing;

import CoucheAcces_Fichier.*;

/**
 * Class TestColInfo that tests the ColInfo class
 */
public class TestColInfo {
    /**
     * ----------------   Main   ----------------
     * @param : args : arguments
     */
    public static void main(String[] args) {
        ColInfo colInfo1 = new ColInfo("ColumnName1", ColumnType.INT, 0);
        System.out.println("Column Name 1: " + colInfo1.getNameCol());
        System.out.println("Column Type 1: " + colInfo1.getTypeCol());
        System.out.println("Column Length 1: " + colInfo1.getLengthString());

        ColInfo colInfo2 = new ColInfo("ColumnName2", ColumnType.FLOAT, 0);
        System.out.println("Column Name 2: " + colInfo2.getNameCol());
        System.out.println("Column Type 2: " + colInfo2.getTypeCol());
        System.out.println("Column Length 2: " + colInfo2.getLengthString());

        ColInfo colInfo3 = new ColInfo("ColumnName3", ColumnType.STRING, 10);
        System.out.println("Column Name 3: " + colInfo3.getNameCol());
        System.out.println("Column Type 3: " + colInfo3.getTypeCol());
        System.out.println("Column Length 3: " + colInfo3.getLengthString());

        ColInfo colInfo4 = new ColInfo("ColumnName4", ColumnType.VARSTRING, 20);
        System.out.println("Column Name 4: " + colInfo4.getNameCol());
        System.out.println("Column Type 4: " + colInfo4.getTypeCol());
        System.out.println("Column Length 4: " + colInfo4.getLengthString());

    }
}

