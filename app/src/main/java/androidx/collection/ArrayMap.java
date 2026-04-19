package androidx.collection;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class ArrayMap<K, V> extends SimpleArrayMap<K, V> implements Map<K, V> {
    MapCollections<K, V> mCollections;

    public ArrayMap() {
    }

    public ArrayMap(int i) {
        super(i);
    }

    public ArrayMap(SimpleArrayMap simpleArrayMap) {
        super(simpleArrayMap);
    }

    private MapCollections<K, V> getCollection() {
        if (this.mCollections == null) {
            this.mCollections = new MapCollections<K, V>() {
                @Override
                protected int colGetSize() {
                    return ArrayMap.this.mSize;
                }

                @Override
                protected Object colGetEntry(int i, int i2) {
                    return ArrayMap.this.mArray[(i << 1) + i2];
                }

                @Override
                protected int colIndexOfKey(Object obj) {
                    return ArrayMap.this.indexOfKey(obj);
                }

                @Override
                protected int colIndexOfValue(Object obj) {
                    return ArrayMap.this.indexOfValue(obj);
                }

                @Override
                protected Map<K, V> colGetMap() {
                    return ArrayMap.this;
                }

                @Override
                protected void colPut(K k, V v) {
                    ArrayMap.this.put(k, v);
                }

                @Override
                protected V colSetValue(int i, V v) {
                    return ArrayMap.this.setValueAt(i, v);
                }

                @Override
                protected void colRemoveAt(int i) {
                    ArrayMap.this.removeAt(i);
                }

                @Override
                protected void colClear() {
                    ArrayMap.this.clear();
                }
            };
        }
        return this.mCollections;
    }

    public boolean containsAll(Collection<?> collection) {
        return MapCollections.containsAllHelper(this, collection);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        ensureCapacity(this.mSize + map.size());
        for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    public boolean removeAll(Collection<?> collection) {
        return MapCollections.removeAllHelper(this, collection);
    }

    public boolean retainAll(Collection<?> collection) {
        return MapCollections.retainAllHelper(this, collection);
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return getCollection().getEntrySet();
    }

    @Override
    public Set<K> keySet() {
        return getCollection().getKeySet();
    }

    @Override
    public Collection<V> values() {
        return getCollection().getValues();
    }
}
