package CoucheOperateursRelationnels;

import CoucheAcces_Fichier.DatabaseManager;

import java.io.BufferedReader;
import java.io.FileReader;

public class ParserBDD {

    /**
     * Method readScript which is called to read a script file and execute the commands in it
     * @param : fileName : the name of the script file
     * @return void : nothing
     */
    public static void readScript(String fileName){


        try(BufferedReader br = new BufferedReader(new FileReader(fileName))){  // Ouvre le fichier et retourne une si ça rate

            System.out.println("Début de la lecture du fichier : " + fileName);

            String ligne = null;

            while ((ligne = br.readLine()) !=null){          // Tant qu'il y a des lignes à lire, on les lit

                if (ligne.equalsIgnoreCase("EXIT")) {
                    break;
                }

                DatabaseManager.Init();
                DatabaseManager.ProcessCommand(ligne);       // Execute la commande

                if(!ligne.startsWith("RESETDB") && !ligne.startsWith("SELECT")){
                    DatabaseManager.Finish();
                }
            }

        }catch (Exception e) {

            System.err.println(e);
        }

        System.out.println("Fin script");
    }
}
