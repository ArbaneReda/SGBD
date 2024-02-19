package CoucheOperateursRelationnels;

import CoucheAcces_Fichier.RecordId;

/*
 * IndexEntry class that represents an entry in the index (key, recordId)
 */
public class IndexEntry {
    private String key; // Add the key of the entry
    private RecordId recordId; // Add the recordId of the entry

    public IndexEntry(String key, RecordId recordId) {
        this.key = key;
        this.recordId = recordId;
    }

    // Method to get the key of the entry
    public String getKey() {
        return key;
    }

    // Method to get the recordId of the entry
    public RecordId getRecordId() {
        return recordId;
    }
}