package CoucheOperateursRelationnels;

import CoucheAcces_Fichier.DatabaseInfo;
import CoucheAcces_Fichier.FileManager;
import GestionEspaceDisque_et_Buffer.BufferManager;
import GestionEspaceDisque_et_Buffer.DiskManager;

/**
 * Class ResetDBCommand that represents the command to reset the database
 */
public class ResetDBCommand {
    
    @SuppressWarnings("unused") // it's used only in the constructor
    private String userCommand;

    /**
     * ----------------   Constructor   ----------------
     * @param : userCommand : the command to reset the database
     */
    public ResetDBCommand(String userCommand) {
        this.userCommand = userCommand;
    }

    // ----------------   Methods   ----------------

    /**
     * Method Execute which is called to execute the command
     * @param : nothing
     * @return : nothing
     */
    public void Execute(){
        
         // delete all the files in the DB folder
        FileManager.getFileManager().resetFileDB();

        // flush all the frames in the buffer
        BufferManager.getBufferManager().flushAll();
        
        // reset the database info
        DatabaseInfo.getInstance().resetDataBaseInfo();

        // reset the disk manager
        DiskManager.getDiskManager().resetDiskManager();

        
    }
}