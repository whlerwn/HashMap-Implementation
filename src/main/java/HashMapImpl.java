import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.hash;

/**
 * The implementation is provided in the form of hash tables.
 *
 * <p>The key in the HashMapImpl is unique. Keys {@code null} and
 * values {@code null} allowed.
 *
 * <p>Values are accessed by key, but not vice versa - you cannot
 * get the key by value, because the values are not unique {@code get}.
 *
 * <p>This implementation assumes that the hash function correctly
 * allocates items to bins {@code put}.
 *
 * <p>This implementation allows you to check if a certain object is
 * contained in the list {@code containsKey}.
 *
 * @param <K> the type of keys maintained by this class
 * @param <V> the type of values maintained by this class
 *
 * @author Valeria Bozhko
 * @see MapImpl
 */

public class HashMapImpl<K, V> implements MapImpl<K, V> {

    private Node<K, V>[] hashTable;
    private int size = 0;
    private float loadfactor;

    public HashMapImpl() {
        hashTable = new Node[16];
        loadfactor = hashTable.length * 0.75f;
    }

    private int hash(final K key) {
        int hash = 31;
        hash = hash * 17 + key.hashCode();
        return hash % hashTable.length;
    }


    /**
     *
     * @return the number of objects in the map
     */
    @Override
    public int size() {
        return size;
    }

    /**
     *
     * @param key the key
     * @param value the value
     * @return true (if the object is added to the map)/false (if adding is not possible)
     */
    @Override
    public boolean put(K key, V value) {
        if (size + 1 >= loadfactor) {
            loadfactor *= 2;
            arrayDoubling();
        }

        Node<K, V> newNode = new Node<>(key, value);
        int index = hash(key);

        if (hashTable[index] == null) {
            return simpleAdd(index, newNode);
        }

        List<Node<K,V>> nodeList = hashTable[index].getNodes();

        for (Node<K,V> node : nodeList) {
            if (keyExistValueNew(node, newNode, value) ||
                    collisionProcessing(node, newNode, nodeList)) {
                return true;
            }
        }

        return false;
    }

    private void arrayDoubling() {
        Node<K,V>[] oldHashTable = hashTable;
        hashTable = new Node[oldHashTable.length * 2];
        size = 0;
        for (Node<K,V> node : oldHashTable) {
            if (node != null) {
                for (Node<K, V> n : node.getNodes()) {
                    put(n.key, n.value);
                }
            }
        }
    }

    private boolean collisionProcessing(final Node<K,V> nodeFromList,
                                        final Node<K,V> newNode,
                                        final List<Node<K,V>> nodes) {
        if (newNode.hashCode() == nodeFromList.hashCode() &&
        !Objects.equals(newNode.key, nodeFromList.key) &&
        !Objects.equals(newNode.value, nodeFromList.value)) {
            nodes.add(newNode);
            size++;
            return true;
        }
        return false;
    }

    private boolean keyExistValueNew(final Node<K, V> nodeFromList,
                                     final Node<K, V> newNode,
                                     final V value) {
        if (newNode.getKey().equals(nodeFromList.getKey()) &&
        !(newNode.getValue().equals(nodeFromList.getValue()))) {
            nodeFromList.setValue(value);
            return true;
        }
        return false;
    }

    private boolean simpleAdd(int index, Node<K,V> newNode) {
        hashTable[index] = new Node<>(null, null);
        hashTable[index].getNodes().add(newNode);
        size++;
        return true;
    }

    /**
     *
     * @param key the key
     * @return true (if the object is deleted from the map)/false (if the object is not found)
     */
    @Override
    public boolean remove(K key) {
        int index = hash(key);

        if (hashTable[index] == null) {
            return false;
        }

        if (hashTable[index].getNodes().size() == 1) {
            hashTable[index] = null;
            return true;
        }

        List<Node<K,V>> nodeList = hashTable[index].getNodes();
        for (Node<K,V> node : nodeList) {
            if (key.equals(node.getKey())) {
                nodeList.remove(node);
                return true;
            }
        }

        return false;
    }

    /**
     *
     * @param key the key
     * @return the value of the object found by the key
     */
    @Override
    public V get(K key) {
        Node<K, V> node = getNode(key);
        return (node == null) ? null : node.getValue();
    }

    /**
     *
     * @param key the key
     * @return true (if an object with that key exists)/false (if an object with that key is not found)
     */
    @Override
    public boolean containsKey(K key) {
        Node<K, V> node = getNode(key);
        return node != null;
    }

    private Node<K, V> getNode(K key) {
        int index = hash(key);
        if (index < hashTable.length && hashTable[index] != null) {
            List<Node<K, V>> list = hashTable[index].getNodes();
            for (Node<K, V> node : list) {
                if (key.equals(node.getKey())) {
                    return node;
                }
            }
        }

        return null;
    }


    /**
     * Node is a hash table cell.
     *
     * @param <K> the key
     * @param <V> the value
     */
    private class Node<K, V> {
        private List<Node<K,V>> nodes;
        private int hash;
        private K key;
        private V value;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
            nodes = new LinkedList<Node<K, V>>();
        }

        private List<Node<K,V>> getNodes() {
            return nodes;
        }

        private int hash() {
            return hashCode() % hashTable.length;
        }

        private K getKey() {
            return key;
        }

        private V getValue() {
            return value;
        }

        private void setValue(V value) {
            this.value = value;
        }

        @Override
        public int hashCode() {
            hash = 31;
            hash = hash * 17 + key.hashCode();
            hash = hash * 17 + value.hashCode();
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || obj.getClass() != this.getClass()) {
                return false;
            }
            Node<K, V> node = (Node) obj;
            return (key.equals(node.getKey()) &&
                    value.equals(node.getValue()) ||
                    hash == node.hashCode());
        }


    }
}
