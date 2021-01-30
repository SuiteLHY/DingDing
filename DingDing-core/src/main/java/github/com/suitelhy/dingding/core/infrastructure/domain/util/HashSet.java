package github.com.suitelhy.dingding.core.infrastructure.domain.util;

import org.springframework.util.ObjectUtils;

import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * @param <E>
 * @Description 封装类型.
 * @see java.util.HashSet
 */
public class HashSet<E>
        extends java.util.HashSet<E> {

    //===== Constructor =====//

    public HashSet() {
        super();
    }

    public HashSet(Collection<? extends E> c) {
        super(c);
    }

    /**
     * (Constructor)
     *
     * @param initialCapacity
     * @throws IllegalArgumentException – if the initial capacity is less than zero
     */
    public HashSet(int initialCapacity) {
        super(initialCapacity);
    }

    public HashSet(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    //==========//

    @Override
    public boolean equals(@NotNull Object o) {
        if (null == o) return false;

        if (o instanceof HashSet) {
            //--- (防止无限递归调用)
            return this.equals((HashSet<?>) o);
        }

        if (o instanceof Collection && !super.equals(o)) {
            try {
                return this.equals((Collection<?>) o) && o.equals(this);
            } catch (Exception e) {
                return false;
            }
        }
        return super.equals(o) && o.equals(this);
    }

    /**
     * 等效比较
     *
     * @param c
     * @param <T>
     * @return
     */
    <T> boolean equals(@NotNull Collection<T> c) {
        if (null == c) return false;

        if (!this.isEmpty() && !c.isEmpty()) {
            if (this.size() != c.size()) return false;

            boolean exist = false;

            for (T each : c) {
                if (null == each) {
                    return false;
                }

                for (E temp : this) {
                    if (null == temp) return false;

                    if (ObjectUtils.nullSafeEquals(each, temp)
                            && ObjectUtils.nullSafeEquals(temp, each)) {
                        exist = true;
                        break;
                    }
                }

                if (!exist) {
                    return false;
                }
                exist = false;
            }

            for (E each : this) {
                if (null == each) return false;

                for (T temp : c) {
                    if (null == temp) return false;

                    if (ObjectUtils.nullSafeEquals(each, temp)
                            && ObjectUtils.nullSafeEquals(temp, each)) {
                        exist = true;
                        break;
                    }
                }

                if (!exist) {
                    return false;
                }
                exist = false;
            }

            return true;
        }
        return this.isEmpty() && c.isEmpty();
    }

    /**
     * @return hashCode
     * @Description 根据 {@link this#equals(Object)} 进行对应的适配设计实现.
     * @see super#hashCode()
     */
    @Override
    public int hashCode() {
        int h = 0;
        Iterator<E> i = iterator();
        while (i.hasNext()) {
            E obj = i.next();
            if (obj != null) {
                /*h += obj.hashCode();*/
                h += ObjectUtils.nullSafeHashCode(obj);
            }
        }
        return h;
    }

    @Override
    public boolean contains(Object o) {
        if (null != o) {
            for (E each : this) {
                if (ObjectUtils.nullSafeEquals(each, o)
                        && ObjectUtils.nullSafeEquals(o, each)) {
                    return true;
                }
            }
        }
        return super.contains(o);
    }

    @Override
    public boolean add(E obj) {
        if (null != obj) {
            return !this.contains(obj) && super.add(obj);
        }
        return false;
    }

    @Override
    public boolean remove(Object o) {
        if (null != o) {
            for (E each : this) {
                if (ObjectUtils.nullSafeEquals(each, o)
                        && ObjectUtils.nullSafeEquals(o, each)) {
                    return super.remove(each);
                }
            }
        }
        return super.remove(o);
    }

    // (测试)
    public static void main(String[] args) {
        Set<String[]>/*ContainArrayHashSet<String>*/ urlInfoSet1 = new ContainArrayHashSet<>(1);

        urlInfoSet1.add(new String[]{"1", "1.0"});
        urlInfoSet1.add(new String[]{"2", "2.0"});
        urlInfoSet1.add(new String[]{"3", "3.0"});

        //=====
        Set<String[]>/*ContainArrayHashSet<String>*/ urlInfoSet2 = new ContainArrayHashSet<>(1);

        urlInfoSet2.add(new String[]{"2", "2.0"});
        urlInfoSet2.add(new String[]{"1", "1.0"});
        urlInfoSet2.add(new String[]{"3", "3.0"});

        //=====
        Set<String[]> urlInfoSet3 = new java.util.HashSet<>(1);

        urlInfoSet3.add(new String[]{"3", "3.0"});
        urlInfoSet3.add(new String[]{"2", "2.0"});
        urlInfoSet3.add(new String[]{"1", "1.0"});

        //=====
        List<String[]> urlInfoList1 = new ArrayList<>(1);
        urlInfoList1.add(new String[]{"1", "1.0"});
        urlInfoList1.add(new String[]{"2", "2.0"});
        urlInfoList1.add(new String[]{"3", "3.0"});

        //=====
        List<Object[]> urlInfoObjectList1 = new ArrayList<>(1);
        urlInfoObjectList1.add(new Object[]{"1", "1.0"});
        urlInfoObjectList1.add(new Object[]{"2", "2.0"});
        urlInfoObjectList1.add(new Object[]{"3", "3.0"});

        //=====
        List<Object[]> urlInfoObjectList2 = new ArrayList<>(1);
        urlInfoObjectList2.add(new Object[]{"1", "1.0"});
        urlInfoObjectList2.add(new Object[]{"2", "2.0"});
        urlInfoObjectList2.add(new Object[]{3, "3.0"});

        //===== equals(Object) =====//
        DemoUtils.show("//===== equals(Object) =====//");

        DemoUtils.show("===== urlInfoSet1.equals(urlInfoSet2) -> "
                + urlInfoSet1.equals(urlInfoSet2)
                + " =====");
        DemoUtils.show("===== urlInfoSet1.equals(urlInfoSet3) -> "
                + urlInfoSet1.equals(urlInfoSet3)
                + " =====");
        DemoUtils.show("===== urlInfoSet3.equals(urlInfoSet1) -> "
                + urlInfoSet3.equals(urlInfoSet1)
                + " =====");

        DemoUtils.show(null);
        DemoUtils.show("===== urlInfoSet1.equals(urlInfoList1) -> "
                + urlInfoSet1.equals(urlInfoList1)
                + " =====");
        DemoUtils.show("===== urlInfoList1.equals(urlInfoSet1) -> "
                + urlInfoList1.equals(urlInfoSet1)
                + " =====");
        DemoUtils.show("===== urlInfoSet1.equals(urlInfoObjectList1) -> "
                + urlInfoSet1.equals(urlInfoObjectList1)
                + " =====");
        DemoUtils.show("===== urlInfoObjectList1.equals(urlInfoSet1) -> "
                + urlInfoObjectList1.equals(urlInfoSet1)
                + " =====");
        DemoUtils.show("===== urlInfoSet1.equals(urlInfoObjectList2) -> "
                + urlInfoSet1.equals(urlInfoObjectList2)
                + " =====");
        DemoUtils.show("===== urlInfoObjectList2.equals(urlInfoSet1) -> "
                + urlInfoObjectList2.equals(urlInfoSet1)
                + " =====");

        //===== hashCode() =====//
        DemoUtils.show(null);
        DemoUtils.show("//===== hashCode() =====//");

        DemoUtils.show("===== urlInfoSet1.hashCode() -> "
                + urlInfoSet1.hashCode()
                + " =====");
        DemoUtils.show("===== urlInfoSet2.hashCode() -> "
                + urlInfoSet2.hashCode()
                + " =====");
        DemoUtils.show("===== urlInfoSet3.hashCode() -> "
                + urlInfoSet3.hashCode()
                + " =====");

        DemoUtils.show("===== urlInfoSet1.hashCode() == urlInfoSet2.hashCode() -> "
                + (urlInfoSet1.hashCode() == urlInfoSet2.hashCode())
                + " =====");
        DemoUtils.show("===== urlInfoSet1.hashCode() == urlInfoSet3.hashCode() -> "
                + (urlInfoSet1.hashCode() == urlInfoSet3.hashCode())
                + " =====");

        //===== Put into java.util.HashSet =====//
        Set<Object> set = new java.util.HashSet<>(3);

        set.add(urlInfoSet1);
        set.add(urlInfoSet2);
        set.add(urlInfoSet3);

        DemoUtils.show(null);
        DemoUtils.show("//===== Put into java.util.HashSet =====//");

        DemoUtils.show("===== set -> " + set);
        DemoUtils.show("===== set.size() -> " + set.size());
        DemoUtils.show("===== set.contains(urlInfoSet1) -> " + set.contains(urlInfoSet1));
        DemoUtils.show("===== set.contains(urlInfoSet2) -> " + set.contains(urlInfoSet2));
        DemoUtils.show("===== set.contains(urlInfoSet3) -> " + set.contains(urlInfoSet3));

        //===== Put into HashSet =====//
        HashSet<Object> hashSet = new HashSet<>(3);

        hashSet.add(urlInfoSet1);
        hashSet.add(urlInfoSet2);
        hashSet.add(urlInfoSet3);

        DemoUtils.show(null);
        DemoUtils.show("//===== Put into HashSet =====//");

        DemoUtils.show("===== hashSet -> " + hashSet);
        DemoUtils.show("===== hashSet.size() -> " + hashSet.size());
        DemoUtils.show("===== hashSet.contains(urlInfoSet1) -> " + hashSet.contains(urlInfoSet1));
        DemoUtils.show("===== hashSet.contains(urlInfoSet2) -> " + hashSet.contains(urlInfoSet2));
        DemoUtils.show("===== hashSet.contains(urlInfoSet3) -> " + hashSet.contains(urlInfoSet3));

        //===== Contain element =====//
        DemoUtils.show(null);
        DemoUtils.show("//===== Contain element =====//");

        DemoUtils.show("===== urlInfoSet1.contains(new String[] {\"1\", \"1.0\"}) -> "
                + urlInfoSet1.contains(new String[]{"1", "1.0"})
                + " =====");
        DemoUtils.show("===== urlInfoSet2.contains(new String[] {\"1\", \"1.0\"}) -> "
                + urlInfoSet2.contains(new String[]{"1", "1.0"})
                + " =====");
        DemoUtils.show("===== urlInfoSet3.contains(new String[] {\"1\", \"1.0\"}) -> "
                + urlInfoSet3.contains(new String[]{"1", "1.0"})
                + " =====");

        DemoUtils.show(null);
        DemoUtils.show("===== urlInfoSet1.contains(new String[] {\"1.0\", \"1\"}) -> "
                + urlInfoSet1.contains(new String[]{"1.0", "1"})
                + " =====");
        DemoUtils.show("===== urlInfoSet2.contains(new String[] {\"1.0\", \"1\"}) -> "
                + urlInfoSet2.contains(new String[]{"1.0", "1"})
                + " =====");
        DemoUtils.show("===== urlInfoSet3.contains(new String[] {\"1.0\", \"1\"}) -> "
                + urlInfoSet3.contains(new String[]{"1.0", "1"})
                + " =====");

        //===== Add element =====//
        DemoUtils.show(null);
        DemoUtils.show("//===== Add element =====//");

        DemoUtils.show("===== (Before add element) urlInfoSet1.size() -> "
                + urlInfoSet1.size()
                + " =====");
        DemoUtils.show("===== (Before add element) urlInfoSet2.size() -> "
                + urlInfoSet2.size()
                + " =====");
        DemoUtils.show("===== (Before add element) urlInfoSet3.size() -> "
                + urlInfoSet3.size()
                + " =====");

        urlInfoSet1.add(new String[]{"1", "1.0"});
        urlInfoSet2.add(new String[]{"1", "1.0"});
        urlInfoSet3.add(new String[]{"1", "1.0"});
        DemoUtils.show(null);
        DemoUtils.show("===== (After add <code>new String[] {\"1\", \"1.0\"}</code>) urlInfoSet1.size() -> "
                + urlInfoSet1.size()
                + " =====");
        DemoUtils.show("===== (After add <code>new String[] {\"1\", \"1.0\"}</code>) urlInfoSet2.size() -> "
                + urlInfoSet2.size()
                + " =====");
        DemoUtils.show("===== (After add <code>new String[] {\"1\", \"1.0\"}</code>) urlInfoSet3.size() -> "
                + urlInfoSet3.size()
                + " =====");

        urlInfoSet1.add(new String[]{"1.0", "1"});
        urlInfoSet2.add(new String[]{"1.0", "1"});
        urlInfoSet3.add(new String[]{"1.0", "1"});
        DemoUtils.show(null);
        DemoUtils.show("===== (After add <code>new String[] {\"1.0\", \"1\"}</code>) urlInfoSet1.size() -> "
                + urlInfoSet1.size()
                + " =====");
        DemoUtils.show("===== (After add <code>new String[] {\"1.0\", \"1\"}</code>) urlInfoSet2.size() -> "
                + urlInfoSet2.size()
                + " =====");
        DemoUtils.show("===== (After add <code>new String[] {\"1.0\", \"1\"}</code>) urlInfoSet3.size() -> "
                + urlInfoSet3.size()
                + " =====");

        //===== Remove element =====//
        DemoUtils.show(null);
        DemoUtils.show("//===== Remove element =====//");

        DemoUtils.show("===== (Before remove element) urlInfoSet1.size() -> "
                + urlInfoSet1.size()
                + " =====");
        DemoUtils.show("===== (Before remove element) urlInfoSet2.size() -> "
                + urlInfoSet2.size()
                + " =====");
        DemoUtils.show("===== (Before remove element) urlInfoSet3.size() -> "
                + urlInfoSet3.size()
                + " =====");

        urlInfoSet1.remove(new String[]{"1", "1.0"});
        urlInfoSet2.remove(new String[]{"1", "1.0"});
        urlInfoSet3.remove(new String[]{"1", "1.0"});
        DemoUtils.show(null);
        DemoUtils.show("===== (After remove <code>new String[] {\"1\", \"1.0\"}</code>) urlInfoSet1.size() -> "
                + urlInfoSet1.size()
                + " =====");
        DemoUtils.show("===== (After remove <code>new String[] {\"1\", \"1.0\"}</code>) urlInfoSet2.size() -> "
                + urlInfoSet2.size()
                + " =====");
        DemoUtils.show("===== (After remove <code>new String[] {\"1\", \"1.0\"}</code>) urlInfoSet3.size() -> "
                + urlInfoSet3.size()
                + " =====");

        urlInfoSet1.remove(new String[]{"1.0", "1"});
        urlInfoSet2.remove(new String[]{"1.0", "1"});
        urlInfoSet3.remove(new String[]{"1.0", "1"});
        DemoUtils.show(null);
        DemoUtils.show("===== (After remove <code>new String[] {\"1.0\", \"1\"}</code>) urlInfoSet1.size() -> "
                + urlInfoSet1.size()
                + " =====");
        DemoUtils.show("===== (After remove <code>new String[] {\"1.0\", \"1\"}</code>) urlInfoSet2.size() -> "
                + urlInfoSet2.size()
                + " =====");
        DemoUtils.show("===== (After remove <code>new String[] {\"1.0\", \"1\"}</code>) urlInfoSet3.size() -> "
                + urlInfoSet3.size()
                + " =====");
    }

}
