package CoucheOperateursRelationnels;

import java.util.ArrayList;
import java.util.List;

import CoucheAcces_Fichier.RecordId;

public class BPlusLeafNode implements BPlusNode {
    private List<IndexEntry> entries; // Ajout de la liste des entrées (clé, recordId)

    public BPlusLeafNode() {
        this.entries = new ArrayList<>();
    }

    // Method to get the first key of the leaf
    @Override
    public String getFirstKey() {
        // Logique pour obtenir la première clé de la feuille
        if (!entries.isEmpty()) {
            return entries.get(0).getKey();
        }
        return null; // Gérer le cas où la feuille est vide
    }

    // Additional method to handle insertion of the leaf node
    @Override
    public BPlusNode insert(String key, RecordId recordId, int order) {
        // Logique d'insertion pour les feuilles
        IndexEntry newEntry = new IndexEntry(key, recordId);

        // Trouver l'index où insérer la nouvelle entrée tout en maintenant la liste
        // triée
        int index = findInsertIndex(newEntry);

        entries.add(index, newEntry);

        // Vérifier si le nœud lui-même doit être divisé
        if (entries.size() > order) {
            return splitLeaf();
        }

        return this;
    }

    // Additional method to handle deletion of the leaf node
    @Override
    public BPlusNode delete(String key, int order) {
        // Logique de suppression pour les feuilles
        int index = findIndexByKey(key);

        if (index != -1) {
            entries.remove(index);

            // Vérifier si le nœud lui-même doit être fusionné
            if (entries.size() < order / 2) {
                return mergeLeaf();
            }
        }

        return this;
    }

    // Additional method to handle search of the leaf node
    @Override
    public List<RecordId> search(String key) {
        int index = findIndexByKey(key);

        if (index != -1) {
            List<RecordId> recordIds = new ArrayList<>();
            recordIds.add(entries.get(index).getRecordId());
            return recordIds;
        }

        return new ArrayList<>(); // Retourne une liste vide si aucune correspondance n'est trouvée
    }

    // private method to find the index of the child to insert into
    // this method uses binary search to find the index
    private int findIndexByKey(String key) {
        int low = 0;
        int high = entries.size() - 1;

        while (low <= high) {
            int mid = (low + high) / 2;
            String currentKey = entries.get(mid).getKey();
            int comparison = key.compareTo(currentKey);

            if (comparison == 0) {
                // Clé trouvée, retourner l'index
                return mid;
            } else if (comparison < 0) {
                // Si la clé de recherche est inférieure à la clé actuelle, rechercher dans la
                // moitié gauche
                high = mid - 1;
            } else {
                // Si la clé de recherche est supérieure à la clé actuelle, rechercher dans la
                // moitié droite
                low = mid + 1;
            }
        }

        // Si la clé n'est pas trouvée, retourner -1
        return -1;
    }

    // private method to find the index to insert the new entry
    private int findInsertIndex(IndexEntry newEntry) {
        String newKey = newEntry.getKey();

        int low = 0;
        int high = entries.size() - 1;

        while (low <= high) {
            int mid = (low + high) / 2;
            String currentKey = entries.get(mid).getKey();
            int comparison = newKey.compareTo(currentKey);

            if (comparison == 0) {
                // Clé déjà présente, insérer après la clé existante
                return mid + 1;
            } else if (comparison < 0) {
                // Si la nouvelle clé est inférieure à la clé actuelle, insérer dans la moitié
                // gauche
                high = mid - 1;
            } else {
                // Si la nouvelle clé est supérieure à la clé actuelle, insérer dans la moitié
                // droite
                low = mid + 1;
            }
        }

        // Insérer à la fin si la clé est plus grande que toutes les clés existantes
        return low;
    }

    // private method to split the leaf node in case of overflow
    private BPlusNode splitLeaf() {
        int mid = entries.size() / 2;
        BPlusLeafNode newLeaf = new BPlusLeafNode();

        // Déplacer la moitié des entrées vers le nouveau nœud
        newLeaf.entries.addAll(entries.subList(mid, entries.size()));
        entries.subList(mid, entries.size()).clear();

        return newLeaf;
    }

    // private method to merge the leaf node in case of underflow
    private BPlusNode mergeLeaf() {
        // La fusion des feuilles peut être gérée au niveau supérieur
        return this;
    }

    // Additional method to get the keys of the node
    public List<String> getKeys() {
        List<String> keys = new ArrayList<>();
        for (IndexEntry entry : entries) {
            keys.add(entry.getKey());
        }
        return keys;
    }
}