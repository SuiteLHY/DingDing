package github.com.suitelhy.dingding.core.infrastructure.domain.util;

import org.springframework.lang.Nullable;
import org.springframework.util.ObjectUtils;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Map;

public class HashMap<K, V>
        extends java.util.HashMap<K, V> {

    //===== Constructor =====//

    public HashMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public HashMap(int initialCapacity) {
        super(initialCapacity);
    }

    public HashMap() {
        super();
    }

    public HashMap(Map<? extends K, ? extends V> m) {
        super(m);
    }

    //==========//

    @Override
    @Nullable
    public V get(Object key) {
        if (null != key && this.containsKey(key)) {
            for (Map.Entry<K, V> entry : this.entrySet()) {
                if (ObjectUtils.nullSafeEquals(entry.getKey(), key)
                        && ObjectUtils.nullSafeEquals(key, entry.getKey())) {
                    return entry.getValue();
                }
            }
        }
        return null;
    }

    @Override
    @Nullable
    public V getOrDefault(Object key, V defaultValue) {
        if (null != key && this.containsKey(key)) {
            for (Map.Entry<K, V> entry : this.entrySet()) {
                if (ObjectUtils.nullSafeEquals(entry.getKey(), key)
                        && ObjectUtils.nullSafeEquals(key, entry.getKey())) {
                    return entry.getValue();
                }
            }
        }
        return defaultValue;
    }

    @Override
    public boolean containsKey(@NotNull Object key) {
        if (null != key) {
            for (K eachKey : this.keySet()) {
                if (ObjectUtils.nullSafeEquals(eachKey, key)
                        && ObjectUtils.nullSafeEquals(key, eachKey)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean containsValue(@Nullable Object value) {
        for (V eachValue : this.values()) {
            if (ObjectUtils.nullSafeEquals(eachValue, value)
                    && ObjectUtils.nullSafeEquals(value, eachValue)) {
                return true;
            }
        }
        return false;
    }

    @Override
    @Nullable
    public V put(@NotNull K key, V value) {
        if (null != key) {
            for (K eachKey : this.keySet()) {
                if (ObjectUtils.nullSafeEquals(eachKey, key)
                        && ObjectUtils.nullSafeEquals(key, eachKey)) {
                    return super.put(eachKey, value);
                }
            }

            return super.put(key, value);
        }
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        if (null == m) return;

        /*boolean exist = false;*/

        for (Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
            /*for (K eachKey : this.keySet()) {
                if (ObjectUtils.nullSafeEquals(entry.getKey(), eachKey)
                        && ObjectUtils.nullSafeEquals(eachKey, entry.getKey())) {
                    put(eachKey, entry.getValue());

                    exist = true;
                    break;
                }
            }

            if (exist) {
                exist = false;
                continue;
            }*/
            this.put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public V remove(@NotNull Object key) {
        if (null != key) {
            for (K eachKey : this.keySet()) {
                if (ObjectUtils.nullSafeEquals(eachKey, key)
                        && ObjectUtils.nullSafeEquals(key, eachKey)) {
                    return super.remove(eachKey);
                }
            }
        }
        return null;
    }

    @Override
    public boolean remove(@NotNull Object key, Object value) {
        if (null != key) {
            for (Map.Entry<K, V> entry : this.entrySet()) {
                if (ObjectUtils.nullSafeEquals(entry.getKey(), key)
                        && ObjectUtils.nullSafeEquals(key, entry.getKey())
                        && ObjectUtils.nullSafeEquals(entry.getValue(), value)
                        && ObjectUtils.nullSafeEquals(value, entry.getValue())) {
                    return super.remove(entry.getKey(), entry.getValue());
                }
            }
        }
        return false;
    }

    @Override
    public boolean replace(@NotNull K key, V oldValue, V newValue) {
        if (null != key) {
            for (Map.Entry<K, V> entry : this.entrySet()) {
                if (ObjectUtils.nullSafeEquals(entry.getKey(), key)
                        && ObjectUtils.nullSafeEquals(key, entry.getKey())
                        && ObjectUtils.nullSafeEquals(entry.getValue(), oldValue)
                        && ObjectUtils.nullSafeEquals(oldValue, entry.getValue())) {
                    return super.replace(entry.getKey(), entry.getValue(), newValue);
                }
            }
        }
        return false;
    }

    @Override
    public V replace(@NotNull K key, V value) {
        if (null != key) {
            for (K eachKey : this.keySet()) {
                if (ObjectUtils.nullSafeEquals(eachKey, key)
                        && ObjectUtils.nullSafeEquals(key, eachKey)) {
                    return super.replace(eachKey, value);
                }
            }
        }
        return null;
    }

    @Override
    public boolean equals(@NotNull Object o) {
        if (super.equals(o)) return true;

        if (o instanceof Map) {
            try {
                return this.equals((Map<?, ?>) o);
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    /**
     * 自定义比较方法
     *
     * @param m
     * @param <_KEY>
     * @param <_VALUE>
     * @return
     */
    <_KEY, _VALUE> boolean equals(@NotNull Map<_KEY, _VALUE> m) {
        if (null == m) return false;

        if (!this.isEmpty() && !m.isEmpty()) {
            if (this.size() != m.size()) return false;

            boolean exist = false;

            for (Map.Entry<_KEY, _VALUE> each : m.entrySet()) {
                final _KEY eachKey = each.getKey();
                final _VALUE eachValue;

                if (null == eachKey) return false;

                eachValue = each.getValue();

                for (Map.Entry<K, V> temp : this.entrySet()) {
                    final K tempKey = temp.getKey();
                    final V tempValue;

                    if (null == tempKey) return false;

                    if (/*Arrays.deepEquals((Object[]) eachKey, tempKey)*/ObjectUtils.nullSafeEquals(eachKey, tempKey)
                            && ObjectUtils.nullSafeEquals(tempKey, eachKey)) {
                        //--- key 相等
                        tempValue = temp.getValue();

                        if (ObjectUtils.nullSafeEquals(eachValue, tempValue)
                                && ObjectUtils.nullSafeEquals(tempValue, eachValue)) {
                            //--- value 相等
                            exist = true;
                            break;
                        }
                    }
                }

                if (!exist) {
                    return false;
                }
                exist = false;
            }

            for (Map.Entry<K, V> each : this.entrySet()) {
                final K eachKey = each.getKey();
                final V eachValue;

                if (null == eachKey) return false;

                eachValue = each.getValue();

                for (Map.Entry<_KEY, _VALUE> temp : m.entrySet()) {
                    final _KEY tempKey = temp.getKey();
                    final _VALUE tempValue;

                    if (null == tempKey) return false;

                    if (ObjectUtils.nullSafeEquals(eachKey, tempKey)
                            && ObjectUtils.nullSafeEquals(tempKey, eachKey)) {
                        //--- key 相等
                        tempValue = temp.getValue();

                        if (ObjectUtils.nullSafeEquals(eachValue, tempValue)
                                && ObjectUtils.nullSafeEquals(tempValue, eachValue)) {
                            //--- value 相等
                            exist = true;
                            break;
                        }
                    }
                }

                if (!exist) {
                    return false;
                }
                exist = false;
            }

            return true;
        }
        return this.isEmpty() && m.isEmpty();
    }

    /**
     * @see super#hashCode()
     */
    @Override
    public int hashCode() {
        int h = 0;
        for (Entry<K, V> kvEntry : entrySet()) {
            /*h += ObjectUtils.nullSafeHashCode(kvEntry);*/
            h += ObjectUtils.nullSafeHashCode(kvEntry.getKey());
            h += ObjectUtils.nullSafeHashCode(kvEntry.getValue());
        }
        return h;
    }

    // (测试)
    public static void main(String[] args) {
        HashMap<String[], Object> urlInfoMap1 = new HashMap<>(1);

        urlInfoMap1.put(new String[] {"1", "1.0"}
                , Arrays.asList(1, 1.0));
        urlInfoMap1.put(new String[] {"2", "2.0"}
                , Arrays.asList(2, 2.0));
        urlInfoMap1.put(new String[] {"3", "3.0"}
                , Arrays.asList(3, 3.0));

        //=====
        HashMap<String[], Object> urlInfoMap2 = new HashMap<>(1);

        urlInfoMap2.put(new String[] {"2", "2.0"}
                , Arrays.asList(2, 2.0));
        urlInfoMap2.put(new String[] {"1", "1.0"}
                , Arrays.asList(1, 1.0));
        urlInfoMap2.put(new String[] {"3", "3.0"}
                , Arrays.asList(3, 3.0));

        //=====
        Map<String[], Object> urlInfoMap3 = new java.util.HashMap<>(1);

        urlInfoMap3.put(new String[] {"3", "3.0"}
                , Arrays.asList(3, 3.0));
        urlInfoMap3.put(new String[] {"2", "2.0"}
                , Arrays.asList(2, 2.0));
        urlInfoMap3.put(new String[] {"1", "1.0"}
                , Arrays.asList(1, 1.0));

        //=====
        Map<String[], Object> urlInfoMap4 = new java.util.LinkedHashMap<>(1);

        urlInfoMap4.put(new String[] {"1", "1.0"}
                , Arrays.asList(1, 1.0));
        urlInfoMap4.put(new String[] {"2", "2.0"}
                , Arrays.asList(2, 2.0));
        urlInfoMap4.put(new String[] {"3", "3.0"}
                , Arrays.asList(3, 3.0));

        //=====
        Map<Object[], Object> urlInfoObjectMap1 = new java.util.HashMap<>(1);

        urlInfoObjectMap1.put(new Object[] {"1", "1.0"}
                , Arrays.asList(1, 1.0));
        urlInfoObjectMap1.put(new Object[] {"2", "2.0"}
                , Arrays.asList(2, 2.0));
        urlInfoObjectMap1.put(new Object[] {"3", "3.0"}
                , Arrays.asList(3, 3.0));

        //=====
        Map<Object[], Object> urlInfoObjectMap2 = new java.util.HashMap<>(1);

        urlInfoObjectMap2.put(new Object[] {"1", "1.0"}
                , Arrays.asList(1, 1.0));
        urlInfoObjectMap2.put(new Object[] {"2", "2.0"}
                , Arrays.asList(2, 2.0));
        urlInfoObjectMap2.put(new Object[] {3, "3.0"}
                , Arrays.asList(3, 3.0));

        //===== equals(Object) =====//
        DemoUtils.show("//===== equals(Object) =====//");

        DemoUtils.show("===== urlInfoMap1.equals(urlInfoMap2) -> "
                + urlInfoMap1.equals(urlInfoMap2)
                + " =====");
        DemoUtils.show("===== urlInfoMap1.equals(urlInfoMap3) -> "
                + urlInfoMap1.equals(urlInfoMap3)
                + " =====");
        DemoUtils.show("===== urlInfoMap3.equals(urlInfoMap1) -> "
                + urlInfoMap3.equals(urlInfoMap1)
                + " =====");

        DemoUtils.show(null);
        DemoUtils.show("===== urlInfoMap1.equals(urlInfoMap4) -> "
                + urlInfoMap1.equals(urlInfoMap4)
                + " =====");
        DemoUtils.show("===== urlInfoMap4.equals(urlInfoMap1) -> "
                + urlInfoMap4.equals(urlInfoMap1)
                + " =====");
        DemoUtils.show("===== urlInfoMap1.equals(urlInfoObjectMap1) -> "
                + urlInfoMap1.equals(urlInfoObjectMap1)
                + " =====");
        DemoUtils.show("===== urlInfoObjectMap1.equals(urlInfoMap1) -> "
                + urlInfoObjectMap1.equals(urlInfoMap1)
                + " =====");
        DemoUtils.show("===== urlInfoMap1.equals(urlInfoObjectMap2) -> "
                + urlInfoMap1.equals(urlInfoObjectMap2)
                + " =====");
        DemoUtils.show("===== urlInfoObjectMap2.equals(urlInfoMap1) -> "
                + urlInfoObjectMap2.equals(urlInfoMap1)
                + " =====");
        DemoUtils.show("===== urlInfoObjectMap1.equals(urlInfoObjectMap2) -> "
                + urlInfoObjectMap1.equals(urlInfoObjectMap2)
                + " =====");
        DemoUtils.show("===== urlInfoObjectMap2.equals(urlInfoObjectMap1) -> "
                + urlInfoObjectMap2.equals(urlInfoObjectMap1)
                + " =====");

        //===== hashCode() =====//
        DemoUtils.show(null);
        DemoUtils.show("//===== hashCode() =====//");

        DemoUtils.show("===== urlInfoMap1.hashCode() -> "
                + urlInfoMap1.hashCode()
                + " =====");
        DemoUtils.show("===== urlInfoMap2.hashCode() -> "
                + urlInfoMap2.hashCode()
                + " =====");
        DemoUtils.show("===== urlInfoMap3.hashCode() -> "
                + urlInfoMap3.hashCode()
                + " =====");

        DemoUtils.show("===== urlInfoMap1.hashCode() == urlInfoMap2.hashCode() -> "
                + (urlInfoMap1.hashCode() == urlInfoMap2.hashCode())
                + " =====");
        DemoUtils.show("===== urlInfoMap1.hashCode() == urlInfoMap3.hashCode() -> "
                + (urlInfoMap1.hashCode() == urlInfoMap3.hashCode())
                + " =====");

        //===== Put into java.util.HashSet =====//
        Map<Object, Object> map = new java.util.HashMap<>(3);

        map.putAll(urlInfoMap1);
        map.putAll(urlInfoMap2);
        map.putAll(urlInfoMap3);

        DemoUtils.show(null);
        DemoUtils.show("//===== Put into java.util.HashSet =====//");

        DemoUtils.show("===== map -> " + map);
        DemoUtils.show("===== map.size() -> " + map.size());
        /*DemoUtils.show("===== map.contains(urlInfoSet1) -> " + map.contains(urlInfoMap1));
        DemoUtils.show("===== map.contains(urlInfoSet2) -> " + map.contains(urlInfoMap2));
        DemoUtils.show("===== map.contains(urlInfoSet3) -> " + map.contains(urlInfoMap3));*/

        //===== Put into HashSet =====//
        Map<Object, Object> hashMap = new HashMap<>(3);

        hashMap.putAll(urlInfoMap1);
        hashMap.putAll(urlInfoMap2);
        hashMap.putAll(urlInfoMap3);

        DemoUtils.show(null);
        DemoUtils.show("//===== Put into HashMap =====//");

        DemoUtils.show("===== hashMap -> " + hashMap);
        DemoUtils.show("===== hashMap.size() -> " + hashMap.size());
        /*DemoUtils.show("===== hashMap.contains(urlInfoMap1) -> " + hashMap.contains(urlInfoMap1));
        DemoUtils.show("===== hashMap.contains(urlInfoMap2) -> " + hashMap.contains(urlInfoMap2));
        DemoUtils.show("===== hashMap.contains(urlInfoMap3) -> " + hashMap.contains(urlInfoMap3));*/

        //===== Contain element =====//
        DemoUtils.show(null);
        DemoUtils.show("//===== Contains key =====//");

        DemoUtils.show("===== urlInfoMap1.containsKey(new String[] {\"1\", \"1.0\"}) -> "
                + urlInfoMap1.containsKey(new String[] {"1", "1.0"})
                + " =====");
        DemoUtils.show("===== urlInfoMap2.containsKey(new String[] {\"1\", \"1.0\"}) -> "
                + urlInfoMap2.containsKey(new String[] {"1", "1.0"})
                + " =====");
        DemoUtils.show("===== urlInfoMap3.containsKey(new String[] {\"1\", \"1.0\"}) -> "
                + urlInfoMap3.containsKey(new String[] {"1", "1.0"})
                + " =====");

        DemoUtils.show(null);
        DemoUtils.show("===== urlInfoMap1.containsKey(new String[] {\"1.0\", \"1\"}) -> "
                + urlInfoMap1.containsKey(new String[] {"1.0", "1"})
                + " =====");
        DemoUtils.show("===== urlInfoMap2.containsKey(new String[] {\"1.0\", \"1\"}) -> "
                + urlInfoMap2.containsKey(new String[] {"1.0", "1"})
                + " =====");
        DemoUtils.show("===== urlInfoMap3.containsKey(new String[] {\"1.0\", \"1\"}) -> "
                + urlInfoMap3.containsKey(new String[] {"1.0", "1"})
                + " =====");

        DemoUtils.show(null);
        DemoUtils.show("//===== Contains value =====//");

        DemoUtils.show("===== urlInfoMap1.containsValue(Arrays.asList(1, 1.0)) -> "
                + urlInfoMap1.containsValue(Arrays.asList(1, 1.0))
                + " =====");
        DemoUtils.show("===== urlInfoMap2.containsValue(Arrays.asList(1, 1.0)) -> "
                + urlInfoMap2.containsValue(Arrays.asList(1, 1.0))
                + " =====");
        DemoUtils.show("===== urlInfoMap3.containsValue(Arrays.asList(1, 1.0)) -> "
                + urlInfoMap3.containsValue(Arrays.asList(1, 1.0))
                + " =====");

        DemoUtils.show(null);
        DemoUtils.show("===== urlInfoMap1.containsValue(Arrays.asList(1.0, 1)) -> "
                + urlInfoMap1.containsValue(Arrays.asList(1.0, 1))
                + " =====");
        DemoUtils.show("===== urlInfoMap2.containsValue(Arrays.asList(1.0, 1)) -> "
                + urlInfoMap2.containsValue(Arrays.asList(1.0, 1))
                + " =====");
        DemoUtils.show("===== urlInfoMap3.containsValue(Arrays.asList(1.0, 1)) -> "
                + urlInfoMap3.containsValue(Arrays.asList(1.0, 1))
                + " =====");

        //===== Put into element =====//
        DemoUtils.show(null);
        DemoUtils.show("//===== Put into element =====//");

        DemoUtils.show("===== (Before put element) urlInfoMap1.size() -> "
                + urlInfoMap1.size()
                + " =====");
        DemoUtils.show("===== (Before put element) urlInfoMap2.size() -> "
                + urlInfoMap2.size()
                + " =====");
        DemoUtils.show("===== (Before put element) urlInfoMap3.size() -> "
                + urlInfoMap3.size()
                + " =====");

        urlInfoMap1.put(new String[] {"1", "1.0"}, Arrays.asList(1, 2, 3));
        urlInfoMap2.put(new String[] {"1", "1.0"}, Arrays.asList(1, 2, 3));
        urlInfoMap3.put(new String[] {"1", "1.0"}, Arrays.asList(1, 2, 3));
        DemoUtils.show(null);
        DemoUtils.show("===== (After put <code>new String[] {\"1\", \"1.0\"}, Arrays.asList(1, 2, 3)</code>) urlInfoMap1.size() -> "
                + urlInfoMap1.size()
                + " =====");
        DemoUtils.show("===== (After put <code>new String[] {\"1\", \"1.0\"}, Arrays.asList(1, 2, 3)</code>) urlInfoMap2.size() -> "
                + urlInfoMap2.size()
                + " =====");
        DemoUtils.show("===== (After put <code>new String[] {\"1\", \"1.0\"}, Arrays.asList(1, 2, 3)</code>) urlInfoMap3.size() -> "
                + urlInfoMap3.size()
                + " =====");

        urlInfoMap1.put(new String[] {"1.0", "1"}, Arrays.asList(1, 2, 3));
        urlInfoMap2.put(new String[] {"1.0", "1"}, Arrays.asList(1, 2, 3));
        urlInfoMap3.put(new String[] {"1.0", "1"}, Arrays.asList(1, 2, 3));
        DemoUtils.show(null);
        DemoUtils.show("===== (After put <code>new String[] {\"1.0\", \"1\"}, Arrays.asList(1, 2, 3)</code>) urlInfoMap1.size() -> "
                + urlInfoMap1.size()
                + " =====");
        DemoUtils.show("===== (After put <code>new String[] {\"1.0\", \"1\"}, Arrays.asList(1, 2, 3)</code>) urlInfoMap2.size() -> "
                + urlInfoMap2.size()
                + " =====");
        DemoUtils.show("===== (After put <code>new String[] {\"1.0\", \"1\"}, Arrays.asList(1, 2, 3)</code>) urlInfoMap3.size() -> "
                + urlInfoMap3.size()
                + " =====");

        //===== Remove element =====//
        DemoUtils.show(null);
        DemoUtils.show("//===== Remove element =====//");

        DemoUtils.show("===== (Before remove element) urlInfoSet1.size() -> "
                + urlInfoMap1.size()
                + " =====");
        DemoUtils.show("===== (Before remove element) urlInfoSet2.size() -> "
                + urlInfoMap2.size()
                + " =====");
        DemoUtils.show("===== (Before remove element) urlInfoSet3.size() -> "
                + urlInfoMap3.size()
                + " =====");

        urlInfoMap1.remove(new String[] {"1", "1.0"});
        urlInfoMap2.remove(new String[] {"1", "1.0"});
        urlInfoMap3.remove(new String[] {"1", "1.0"});
        DemoUtils.show(null);
        DemoUtils.show("===== (After remove <code>new String[] {\"1\", \"1.0\"}</code>) urlInfoSet1.size() -> "
                + urlInfoMap1.size()
                + " =====");
        DemoUtils.show("===== (After remove <code>new String[] {\"1\", \"1.0\"}</code>) urlInfoSet2.size() -> "
                + urlInfoMap2.size()
                + " =====");
        DemoUtils.show("===== (After remove <code>new String[] {\"1\", \"1.0\"}</code>) urlInfoSet3.size() -> "
                + urlInfoMap3.size()
                + " =====");

        urlInfoMap1.remove(new String[] {"1.0", "1"});
        urlInfoMap2.remove(new String[] {"1.0", "1"});
        urlInfoMap3.remove(new String[] {"1.0", "1"});
        DemoUtils.show(null);
        DemoUtils.show("===== (After remove <code>new String[] {\"1.0\", \"1\"}</code>) urlInfoSet1.size() -> "
                + urlInfoMap1.size()
                + " =====");
        DemoUtils.show("===== (After remove <code>new String[] {\"1.0\", \"1\"}</code>) urlInfoSet2.size() -> "
                + urlInfoMap2.size()
                + " =====");
        DemoUtils.show("===== (After remove <code>new String[] {\"1.0\", \"1\"}</code>) urlInfoSet3.size() -> "
                + urlInfoMap3.size()
                + " =====");
    }

}
