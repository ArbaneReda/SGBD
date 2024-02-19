package CoucheOperateursRelationnels;

import java.util.ArrayList;
import java.util.List;
import CoucheAcces_Fichier.RecordId;

public class BPlusInternalNode implements BPlusNode {
    private List<String> keys; // Ajout de la liste des clés
    private List<BPlusNode> children; // Ajout de la liste des enfants
    private IndexBPlusTree bPlusTree; // Ajout de la référence à l'arbre

    public BPlusInternalNode(IndexBPlusTree bPlusTree) {
        this.keys = new ArrayList<>();
        this.children = new ArrayList<>();
        this.bPlusTree = bPlusTree;
    }

    // Additional method to handle insertion of the internal node
    @Override
    public BPlusNode insert(String key, RecordId recordId, int order) {
        // Find the child to insert into
        int index = findChildIndex(key);
        BPlusNode child = children.get(index);

        // Insert into the child
        BPlusNode newChild = child.insert(key, recordId, order);

        // If the child split, handle the split
        if (newChild != child) {
            keys.add(index, newChild.getFirstKey());
            children.add(index + 1, newChild);
            // Check if the node itself needs to split
            if (keys.size() > order) {
                return split();
            }
        }
        return this;
    }

    // Additional method to handle deletion of the internal node
    @Override
    public BPlusNode delete(String key, int order) {
        // Find the child to delete from
        int index = findChildIndex(key);
        BPlusNode child = children.get(index);

        // Delete from the child
        BPlusNode newChild = child.delete(key, order);

        // If the child merged, handle the merge
        if (newChild != child) {
            keys.remove(index);
            children.remove(index + 1);
            // Check if the node itself needs to merge
            if (keys.size() < order / 2) {
                return merge();
            }
        }
        return this;
    }

    // Additional method to handle searching of the internal node
    @Override
    public List<RecordId> search(String key) {
        int index = findChildIndex(key);
        return children.get(index).search(key);
    }

    private int findChildIndex(String key) {
        int low = 0;
        int high = keys.size() - 1;

        while (low <= high) {
            int mid = (low + high) / 2;
            String currentKey = keys.get(mid);
            int comparison = key.compareTo(currentKey);

            if (comparison == 0) {
                // Key found, return the index
                return mid;
            } else if (comparison < 0) {
                // If the search key is less than the current key, search in the left half
                high = mid - 1;
            } else {
                // If the search key is greater than the current key, search in the right half
                low = mid + 1;
            }
        }

        // If the key is not found, return the index where it should be inserted
        return low;
    }

    // Additional method to get the first key of the node
    public String getFirstKey() {
        return keys.get(0);
    }

    // Additional method to handle splitting of the internal node
    private BPlusInternalNode split() {
        int mid = keys.size() / 2;
        BPlusInternalNode newNode = new BPlusInternalNode(this.bPlusTree); // Pass the reference

        // Move half of the keys and children to the new node
        newNode.keys.addAll(keys.subList(mid, keys.size()));
        newNode.children.addAll(children.subList(mid + 1, children.size()));

        keys.subList(mid, keys.size()).clear();
        children.subList(mid + 1, children.size()).clear();

        return newNode;
    }

    // Additional method to handle merging of the internal node
    private BPlusInternalNode merge() {
        if (this == bPlusTree.getRoot()) {
            return this; // Don't merge the root
        }

        int mid = keys.size() / 2;
        BPlusInternalNode sibling = (BPlusInternalNode) children.get(mid);

        // Move the separator key to the left sibling
        sibling.keys.add(keys.get(mid));
        sibling.keys.addAll(keys.subList(mid + 1, keys.size()));
        sibling.children.addAll(children.subList(mid + 1, children.size()));

        keys.subList(mid, keys.size()).clear();
        children.subList(mid + 1, children.size()).clear();

        return sibling;
    }

    // Additional method to get the keys of the node
    public List<BPlusNode> getChildren() {
        return children;
    }
}