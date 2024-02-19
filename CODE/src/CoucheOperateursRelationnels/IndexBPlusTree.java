package CoucheOperateursRelationnels;


import java.util.ArrayList;
import java.util.List;
import CoucheAcces_Fichier.RecordId;

/*
*  B+ tree: dynamique, s’adapte bien aux insertions
*  et suppressions → le type d’index le plus utilisé !
*  pas comme ISAM qui est statique
*/

public class IndexBPlusTree {
    
    private BPlusNode root; // Add the root of the B+ tree
    private int order; // Add the order of the B+ tree (maximum number of children per node)

    // Constructor for the IndexBPlusTree class
    public IndexBPlusTree(int order) {
        this.order = order;
        this.root = new BPlusLeafNode();
    }

    // Method to get the root of the B+ tree
    public BPlusNode getRoot() {
        return root;
    }

    // Method to insert a new entry in the B+ tree
    public void insert(String key, RecordId recordId) {
        root = root.insert(key, recordId, order);
    }

    // Method to delete an entry from the B+ tree
    public void delete(String key) {
        root = root.delete(key, order);
    }

    // Method to search for entries in the B+ tree
    public List<RecordId> search(String key) {
        return root.search(key);
    }

    // Method to get a list of all the keys in the B+ tree
    public List<String> getKeys() {
    List<String> keys = new ArrayList<>();
    collectKeys(root, keys);
    return keys;
}

// Method to collect all the keys in the B+ tree recursively as long as the node is not a leaf node
private void collectKeys(BPlusNode node, List<String> keys) {
    if (node instanceof BPlusLeafNode) {
        BPlusLeafNode leafNode = (BPlusLeafNode) node;
        for (String key : leafNode.getKeys()) {
            keys.add(key);
        }
    } else if (node instanceof BPlusInternalNode) {
        BPlusInternalNode internalNode = (BPlusInternalNode) node;
        for (BPlusNode child : internalNode.getChildren()) {
            collectKeys(child, keys);
        }
    }
}
}