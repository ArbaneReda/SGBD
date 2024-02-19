package CoucheOperateursRelationnels;


import CoucheAcces_Fichier.RecordId;
import java.util.List;

/*
 * Interface for the B+ tree node (internal or leaf) to help with polymorphism
 * and to make the code more readable
 */
public interface BPlusNode{
    BPlusNode insert(String key, RecordId recordId, int order);
    BPlusNode delete(String key, int order);
    List<RecordId> search(String key);
    String getFirstKey();
}