package GestionEspaceDisque_et_Buffer;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Class DiskManager that represents the disk manager
 */
public class DiskManager {

    private static DiskManager diskManager = new DiskManager();

    public Map<Integer, ArrayList<PageID>> filePageId = new HashMap<>(); // {file:[page]}
    private ArrayList<PageID> desalocatedPage = new ArrayList<PageID>(); // for desallocated page
    private PageID currentAllocatedPageId;

    /**
     * ---------------- Constructor ----------------
     * 
     * @param : nothing
     */
    private DiskManager() {
        createFiles();
    }

    // ---------------- Methods ----------------

    /**
     * Method getDiskManager that returns the disk manager
     * @param : nothing
     * @return DiskManager : the disk manager
     */
    public static DiskManager getDiskManager() {
        return diskManager;
    }

    public void createFiles(){
        for (int i = 0; i < DBParams.DMFileCount; i++) { // get the available files in the db
            String filePatheName = DBParams.DBPath + "/F" + i + ".data"; // assume that the db should always have
                                                                         // DMFileCount

            File file = new File(filePatheName);
            if (!file.exists()) {
                
                DBParams.createFile();

            }
            filePageId.put(i, new ArrayList<PageID>());
            
        }
    }

    /**
     * Method AllocPage that allocates a page
     * @param : nothing
     * @return PageID : the id of the allocated page
     */
    public PageID AllocPage() {

        int currentSmallestFileId;
        int currentSmallestPageId;
        PageID currentAllocatedPage = new PageID();

        if (!desalocatedPage.isEmpty()) { // if a page was desalocated we use it
            currentAllocatedPage = desalocatedPage.get(desalocatedPage.size() - 1);
            desalocatedPage.remove(desalocatedPage.size() - 1);
            currentSmallestFileId = currentAllocatedPage.getFileIdx();
            currentSmallestPageId = currentAllocatedPage.getPageIdx();

        } else { // if there isn't a disalocated page we use a page from a file with the least
                 // allocated page

            PageID currentSmallestPage = getSmallestPage();
            currentSmallestFileId = currentSmallestPage.getFileIdx();
            currentSmallestPageId = currentSmallestPage.getPageIdx();
        }
        currentAllocatedPage.setFileIdx(currentSmallestFileId);
        currentAllocatedPage.setPageIdx(currentSmallestPageId);
        updateFilePageId(currentAllocatedPage); // update the map filePageId by adding currentAllocatedPage to the list
                                                // associated to its page
        //System.out.println("allocated page : " + currentAllocatedPage.toString());
        this.currentAllocatedPageId = currentAllocatedPage;
        return currentAllocatedPage;
    }

    /**
     * Method updateFilePageId that updates the map filePageId
     * @param : currentAllocatedPage : the page that we'll add to the map
     * @return void : nothing
     */
    private void updateFilePageId(PageID currentAllocatedPage) {
        ArrayList<PageID> filePages = new ArrayList<PageID>();
        ArrayList<PageID> filePages1 = filePageId.getOrDefault(currentAllocatedPage.getFileIdx(), new ArrayList<>());
        for (int i = 0; i < filePages1.size(); i++) {
            PageID page = new PageID();
            PageID page1 = filePages1.get(i);
            page.setFileIdx(page1.getFileIdx());
            page.setPageIdx(page1.getPageIdx());
            filePages.add(page);
        }
        filePages.add(currentAllocatedPage);
        filePageId.put(currentAllocatedPage.getFileIdx(), filePages);
    }

    /**
     * Method getSmallestPage that returns the last page of the file with the
     * smallest allocated page
     * @param : nothing
     * @return PageID : the last page of the file with the smallest allocated page
     */
    private PageID getSmallestPage() {
        int smallestFileSize = Integer.MAX_VALUE;
        int fileIdxWithSmallestSize = -1;

        for (int fileIdx : filePageId.keySet()) { // get the file with the less number of pages
            int fileSize = filePageId.get(fileIdx).size();
            if (fileSize < smallestFileSize) {
                smallestFileSize = fileSize;
                fileIdxWithSmallestSize = fileIdx;
            }
        }
        return new PageID(fileIdxWithSmallestSize, smallestFileSize);
    }

    /**
     * Method DeallocPage that desallocates a page
     * @param : pageId : the id of the page that we'll desallocate
     * @return void : nothing
     */
    public void DeallocPage(PageID pageId) {

        int fileIdx = pageId.getFileIdx();
        int pageIdx = pageId.getPageIdx();
        // if (filePageId.containsKey(fileIdx)) { // "To check if the file corresponding
        // to the page exists
        // in the map
        ArrayList<PageID> currentFilePagesTab = filePageId.get(fileIdx); // "A temporary array that will
                                                                         // update the map at the end
        // if (pageIdx >= 0 && pageIdx < currentFilePagesTab.size()) { // verify that
        // the page exists in the file
        // 'better with the try catch)

        desalocatedPage.add(pageId); // adding the page that we'll desalocate to the array of desallocated pages

        currentFilePagesTab.remove(pageIdx);

        filePageId.put(fileIdx, currentFilePagesTab); // update the map with the new array but without
                                                      // the desallocated page

        //System.out.println("desalocated page : " + pageId.toString());

        // } else {
        // System.out.println("this page was not allocated");
        // }
        // } else {
        // System.out.println("this page doesn't belong to any file of the database");
        // }
    }

    /**
     * Method WritePage that writes a page
     * @param : pageId : the id of the page that we'll write
     * @param : buff : the byte buffer that we'll write
     * @return void : nothing
     */
    public void WritePage(PageID pageId, ByteBuffer buff) {

        int fileIdx = pageId.getFileIdx();
        int pageIdx = pageId.getPageIdx();

        try {

            String filePath = DBParams.DBPath + "/F" + fileIdx + ".data";
            RandomAccessFile file = new RandomAccessFile(filePath, "rw");
            FileChannel fileChannel = file.getChannel();

            int seekPosition = pageIdx * DBParams.SGBDPageSize;

            file.seek(seekPosition);
            buff.position(0);

            fileChannel.write(buff);

            file.close();

            buff.clear();
            //System.out.println("the message has been written to the page : " + pageId.toString());
        } catch (IOException e) {
            System.err.println(e);
        }

    }

    /**
     * Method getPageSize that returns the size of a page
     * @param : filePath : the path of the file
     * @param : pageIdx : the index of the page
     * @return int : the size of the page
     */
    public int getPageSize(String filePath, int pageIdx) {

        ByteBuffer buffRead = ByteBuffer.allocate(DBParams.SGBDPageSize);
        int seekPosition = pageIdx * DBParams.SGBDPageSize;
        try {
            RandomAccessFile file = new RandomAccessFile(filePath, "rw");
            FileChannel fileChannel = file.getChannel();
            fileChannel.position(seekPosition);

            int size = 0;
            int bytesRead = fileChannel.read(buffRead);
            if (bytesRead != -1) {
                buffRead.flip(); // Prepare the buffer for reading
                while (buffRead.hasRemaining()) { // iterate over the caracters of the page

                    byte currentByte = buffRead.get();
                    if (currentByte != '\0') {
                        size++;
                    }
                }
                file.close();
                return size;
            }
            file.close();

        } catch (IOException e) {
            System.err.println("can't access to the file");
        }

        return 0;
    }

    /**
     * Method ReadPage that reads a page
     * @param : pageId : the id of the page that we'll read
     * @param : buff : the byte buffer that we'll read
     * @return ByteBuffer : the byte buffer that we'll read
     */
    public static ByteBuffer ReadPage(PageID pageId, ByteBuffer buff) {
        if (pageId.isValid()) {
            buff.position(0);

            int fileIdx = pageId.getFileIdx();
            int pageIdx = pageId.getPageIdx();

            int seekPosition = pageIdx * DBParams.SGBDPageSize;
            String filePath = DBParams.DBPath + "/F" + fileIdx + ".data";
            try {

                RandomAccessFile file = new RandomAccessFile(filePath, "rw");
                FileChannel fileChannel = file.getChannel();
                fileChannel.position(seekPosition);
                fileChannel.read(buff);
                file.close();

            }

            catch (IOException e) {
                System.err.println("can't access to the file");
            }
        }

        return buff;
    }

    /**
     * Method readContentOfBuffer that reads the content of a buffer
     * @param : bf : the byte buffer that we'll read
     * @return StringBuilder : the content of the buffer
     */
    public static StringBuilder readContentOfBuffer(ByteBuffer bf) {
        bf.position(0);
        StringBuilder message = new StringBuilder();
        byte currentByte;
        while (bf.hasRemaining()) {
            currentByte = bf.get();

            if (currentByte != '\0') {

                char currentChar = (char) currentByte;
                message.append(currentChar);

            }
        }
        bf.position(0);
        if (message.length() == 0) {
            return null;
        }

        return message;
    }

    /**
     * Method getByteBufferSize that returns the size of a buffer
     * @param : bf : the byte buffer that we'll read
     * @return int : the size of the buffer
     */
    public int getByteBufferSize(ByteBuffer bf) {
        bf.position(0);
        int size = 0;
        byte currentByte;
        while (bf.hasRemaining()) {
            currentByte = bf.get();

            if (currentByte != '\0') {
                size++;
            }
        }
        return size;
    }

    /**
     * Method getCurrentCountAllocPages that returns the number of allocated pages
     * @param : nothing
     * @return int : the number of allocated pages
     */
    public int GetCurrentCountAllocPages() {
        int currentAllocatedPages = 0;
        for (ArrayList<PageID> pages : filePageId.values()) { // for each array in map.values
            currentAllocatedPages += pages.size();
        }
        return currentAllocatedPages;
    }

    /**
     * Method getCurrentAllocatedPage that returns the current allocated page
     * @param : nothing
     * @return PageID : the current allocated page
     */
    public PageID getCurrentAllocatedPage() {
        return this.currentAllocatedPageId;
    }

    /*
     * Method disalocatAllPages that desallocates all pages
     * @param : nothing
     * @return void : nothing
     */
    public void disalocatAllPages() {
        for (int file : filePageId.keySet()) {
            ArrayList<PageID> listPages = filePageId.get(file);

            for (int i = 0; i < listPages.size(); i++) {
                PageID page = listPages.get(i);
                DeallocPage(page);
            }
        }
    }

    /**
     * Method getEmptyPage that returns an empty page
     * @param : nothing
     * @return PageID : an empty page
     */
    public PageID getEmptyPage() {
        for (int i = 0; i < DBParams.nbPageFile * DBParams.DMFileCount; i++) {
            PageID page = AllocPage();
            ByteBuffer buff = ByteBuffer.allocate(DBParams.SGBDPageSize);
            ReadPage(page, buff);
            StringBuilder content = readContentOfBuffer(buff);
            if (content == null) {
                return page;
            }
        }
        return null;
    }

    /**
     * Method toString that returns the information of the disk manager
     * @param : nothing
     * @return String : the information of the disk manager
     */
    @Override
    public String toString() {
        String filePageIdInfo = "filePageId : \n";
        for (int fileIdx : filePageId.keySet()) {
            filePageIdInfo += " file : " + fileIdx + "-->" + "[";
            for (PageID page : filePageId.get(fileIdx)) {
                filePageIdInfo += page.toString();
            }
            filePageIdInfo += "] \n";
        }
        filePageIdInfo += " disalocated Pages : " + "\n [";
        for (PageID page : desalocatedPage) {
            filePageIdInfo += page.toString();
        }
        return filePageIdInfo + "]";
    }

    /**
     * Method deleteAllDBFiles that deletes all files of the database
     * @param : nothing
     * @return void : nothing
     */
    public void deleteAllDBFiles() {

        File directory = new File(DBParams.DBPath);

        // Check if the directory exists
        if (directory.exists() && directory.isDirectory()) {
            // List all files in the directory
            File[] files = directory.listFiles();

            // Iterate through the files and delete them
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        // Delete the file
                        if (file.delete()) {
                            System.out.println("Deleted file: " + file.getAbsolutePath());
                        } else {
                            System.err.println("Failed to delete: " + file.getAbsolutePath());
                        }
                    }
                }
            }
        }
    }

    /**
     * Method resetDiskManager that resets the disk manager
     * @param : nothing
     * @return void : nothing
     */
    public void resetDiskManager() {
        filePageId = new HashMap<>(); // {file:[page]}
        desalocatedPage = new ArrayList<PageID>(); // for desallocated page
        currentAllocatedPageId = null;
        
    }

}
