package github.com.suitelhy.dingding.core.infrastructure.domain.util;

import java.util.*;

/**
 * @Description 键只能使用数组, 且不允许 {@link null} 和 空数组.
 *
 * @param <K>
 * @param <V>
 */
public class ContainArrayHashMap<K, V>
        extends HashMap<K[], V> {

    //===== Constructor =====//

    public ContainArrayHashMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public ContainArrayHashMap(int initialCapacity) {
        super(initialCapacity);
    }

    public ContainArrayHashMap() {
        super();
    }

    public ContainArrayHashMap(Map<? extends K[], ? extends V> m) {
        super(m);
    }

    //==========//

//    @Override
//    public V get(Object key) {
//        if (null == super.get(key)
//                && null != key
//                && key.getClass().isArray()) {
//            for (Map.Entry<K[], V> entry : this.entrySet()) {
//                if (ObjectUtils.nullSafeEquals(entry.getKey(), key)) {
//                    return super.get(entry.getKey());
//                }
//            }
//
//            return null;
//        }
//        return super.get(key);
//    }
//
//    @Override
//    public V getOrDefault(Object key, V defaultValue) {
//        if (defaultValue == super.getOrDefault(key, defaultValue)
//                && null != key
//                && key.getClass().isArray()) {
//            for (Map.Entry<K[], V> entry : this.entrySet()) {
//                if (ObjectUtils.nullSafeEquals(entry.getKey(), key)) {
//                    return super.getOrDefault(entry.getKey(), defaultValue);
//                }
//            }
//
//            return defaultValue;
//        }
//        return super.getOrDefault(key, defaultValue);
//    }
//
//    @Override
//    public boolean containsKey(Object key) {
//        if (!super.containsKey(key)
//                && null != key
//                && key.getClass().isArray()) {
//            for (K[] eachKey : this.keySet()) {
//                if (ObjectUtils.nullSafeEquals(eachKey, key)) {
//                    return super.containsKey(eachKey);
//                }
//            }
//
//            return false;
//        }
//        return super.containsKey(key);
//    }
//
//    @Override
//    public V put(K[] key, V value) {
//        if (null == super.put(key, value)
//                && null != key
//                && key.getClass().isArray()) {
//            for (K[] eachKey : this.keySet()) {
//                if (eachKey.length > 0
//                        && ObjectUtils.nullSafeEquals(eachKey, key)) {
//                    return super.put(eachKey, value);
//                }
//            }
//
//            return null;
//        }
//        return super.put(key, value);
//    }
//
//    @Override
//    public V remove(Object key) {
//        if (null == super.remove(key)
//                && null != key
//                && key.getClass().isArray()) {
//            for (K[] eachKey : this.keySet()) {
//                if (ObjectUtils.nullSafeEquals(eachKey, key)) {
//                    return super.remove(eachKey);
//                }
//            }
//
//            return null;
//        }
//        return super.remove(key);
//    }
//
//    @Override
//    public boolean remove(Object key, Object value) {
//        if (!super.containsKey(key)
//                && null != key
//                && key.getClass().isArray()) {
//            for (Map.Entry<K[], V> entry : this.entrySet()) {
//                if (ObjectUtils.nullSafeEquals(entry.getKey(), key)) {
//                    return super.remove(entry.getKey(), entry.getValue());
//                }
//            }
//
//            return false;
//        }
//        return super.remove(key, value);
//    }
//
//    @Override
//    public boolean replace(K[] key, V oldValue, V newValue) {
//        if (!super.replace(key, oldValue, newValue)
//                && null != key
//                && key.getClass().isArray()) {
//            for (K[] eachKey : this.keySet()) {
//                if (ObjectUtils.nullSafeEquals(eachKey, key)) {
//                    return super.replace(eachKey, oldValue, newValue);
//                }
//            }
//
//            return false;
//        }
//        return super.replace(key, oldValue, newValue);
//    }
//
//    @Override
//    public V replace(K[] key, V value) {
//        if (null == super.replace(key, value)
//                && null != key
//                && key.getClass().isArray()) {
//            for (K[] eachKey : this.keySet()) {
//                if (ObjectUtils.nullSafeEquals(eachKey, key)) {
//                    return super.replace(eachKey, value);
//                }
//            }
//
//            return null;
//        }
//        return super.replace(key, value);
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (!super.equals(o) && o instanceof Map) {
//            try {
//                return this.equals((Map<?, ?>) o);
//            } catch (Exception e) {
//                return false;
//            }
//        }
//        return false;
//    }
//
//    /**
//     * 自定义比较方法
//     *
//     * @param m
//     * @param <_KEY>
//     * @param <_VALUE>
//     * @return
//     */
//    <_KEY, _VALUE> boolean equals(@NotNull Map<_KEY, _VALUE> m) {
//        if (null == m) return false;
//
//        if (!this.isEmpty() && !m.isEmpty()) {
//            boolean exist = false;
//
//            for (Map.Entry<_KEY, _VALUE> each : m.entrySet()) {
//                final _KEY eachKey;
//                final _VALUE eachValue;
//
//                if (null == (eachKey = each.getKey())
//                        || !eachKey.getClass().isArray()) {
//                    return false;
//                }
//
//                eachValue = each.getValue();
//
//                for (Map.Entry<K[], V> temp : this.entrySet()) {
//                    final K[] tempKey;
//                    final V tempValue;
//
//                    if (null == (tempKey = temp.getKey()) || tempKey.length == 0) {
//                        return false;
//                    }
//
//                    if (/*Arrays.deepEquals((Object[]) eachKey, tempKey)*/ObjectUtils.nullSafeEquals(eachKey, tempKey)) {
//                        //--- key 相等
//                        tempValue = temp.getValue();
//
//                        if (/*ObjectUtils.nullSafeEquals(eachValue, tempValue)*/eachValue == tempValue
//                                || (null != eachValue
//                                    && eachValue.equals(tempValue))
//                                    && tempValue.equals(eachValue)) {
//                            //--- value 相等
//                            exist = true;
//                            break;
//                        }
//                    }
//                }
//
//                if (!exist) {
//                    return false;
//                }
//                exist = false;
//            }
//
//            for (Map.Entry<K[], V> each : this.entrySet()) {
//                final K[] eachKey;
//                final V eachValue;
//
//                if (null == (eachKey = each.getKey()) || eachKey.length == 0) {
//                    return false;
//                }
//
//                eachValue = each.getValue();
//
//                for (Map.Entry<_KEY, _VALUE> temp : m.entrySet()) {
//                    final _KEY tempKey;
//                    final _VALUE tempValue;
//
//                    if (null == (tempKey = temp.getKey())
//                            || !tempKey.getClass().isArray()) {
//                        return false;
//                    }
//
//                    if (ObjectUtils.nullSafeEquals(eachKey, tempKey)) {
//                        //--- key 相等
//                        tempValue = temp.getValue();
//
//                        if (ObjectUtils.nullSafeEquals(eachValue, tempValue)) {
//                            //--- value 相等
//                            exist = true;
//                            break;
//                        }
//                    }
//                }
//
//                if (!exist) {
//                    return false;
//                }
//                exist = false;
//            }
//
//            return true;
//        }
//        return this.isEmpty() && m.isEmpty();
//    }

}
