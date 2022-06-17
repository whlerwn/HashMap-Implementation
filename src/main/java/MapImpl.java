import java.util.AbstractSet;
import java.util.Map;

/**
 * This object is provided in a key-value pair.
 * <p> A map cannot contain duplicate keys - each key must be
 * equal to at most one value. The key should be immutable objects.
 *
 * @param <K> the type of keys maintained by this interface
 * @param <V> the type of values maintained by this interface
 *
 * @author Valeria Bozhko
 * @see HashMapImpl
 */
public interface MapImpl<K, V> {
    int size();
    boolean put(K key, V value);
    boolean remove(K key);
    V get(K key);
    boolean containsKey(K key);
}
