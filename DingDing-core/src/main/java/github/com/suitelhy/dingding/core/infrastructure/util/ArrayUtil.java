package github.com.suitelhy.dingding.core.infrastructure.util;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 数组工具
 *
 * @Description 数组类型数据的工具类.
 */
public class ArrayUtil {

    /**
     * 一维数组
     *
     * @Description 一维数组类型数据的工具类.
     */
    public static class OneDimensionalArrayUtil {

        /**
         * @Design (单例模式 - 登记式)
         */
        private static class Factory {
            private static final OneDimensionalArrayUtil SINGLETON = new OneDimensionalArrayUtil();
        }

        private OneDimensionalArrayUtil() {
        }

        public static @NotNull OneDimensionalArrayUtil getInstance() {
            return OneDimensionalArrayUtil.Factory.SINGLETON;
        }

        /**
         * 判断类型归属
         *
         * @Description {@param targetClass} 是否是 {@param array} 的父类类型或者一致类型.
         *
         * @param targetClass
         * @param array
         * @param <T>
         *
         * @return {@link Boolean#TYPE}
         */
        public <T> boolean belongToArray(@NotNull Class<?> targetClass, @NotNull T array) {
            Class<?> type;
            Class<?> elementType;

            if (null == targetClass) {
                return false;
            }

            if (null != array && (type = array.getClass()).isArray()
                    && null != (elementType = type.getComponentType())) {
                //=== 处理基本类型数组（需要优先于对象数组判断）

                if (targetClass == Integer.TYPE) {
                    return elementType /*instanceof*/ == Integer.TYPE;
                }
                if (targetClass == Short.TYPE) {
                    return elementType == Short.TYPE;
                }
                if (targetClass == Long.TYPE) {
                    return elementType == Long.TYPE;
                }
                if (targetClass == Float.TYPE) {
                    return elementType == Float.TYPE;
                }
                if (targetClass == Double.TYPE) {
                    return elementType == Double.TYPE;
                }
                if (targetClass == Character.TYPE) {
                    return elementType == Character.TYPE;
                }
                if (targetClass == Byte.TYPE) {
                    return elementType == Byte.TYPE;
                }
                if (targetClass == Boolean.TYPE) {
                    return elementType == Boolean.TYPE;
                }

                //=== 处理非基本类型数组

                return targetClass.isAssignableFrom(elementType);
            }
            return false;
        }

        /**
         * 判断类型一致
         *
         * @Description {@param targetClass} 与 {@param array} 类型一致.
         *
         * @param targetClass
         * @param array
         * @param <T>
         *
         * @return {@link Boolean#TYPE}
         */
        public <T> boolean isArray(@NotNull Class<?> targetClass, @NotNull T array) {
            Class<?> type;
            Class<?> elementType;

            if (null == targetClass) {
                return false;
            }

            if (null != array && (type = array.getClass()).isArray()
                    && null != (elementType = type.getComponentType())) {
                return targetClass == elementType;
            }
            return false;
        }

    }

    /**
     * @Design (单例模式 - 登记式)
     */
    private static class Factory {
        private static final ArrayUtil SINGLETON = new ArrayUtil();
    }

    private ArrayUtil() {
    }

    public static @NotNull ArrayUtil getInstance() {
        return ArrayUtil.Factory.SINGLETON;
    }

    /**
     * 数组转集合
     *
     * @Description 支持多维数组.
     *
     * @param array
     * @param <T>
     *
     * @return {@link List}
     */
    private static <T> List<Object> arrayToList(@NotNull T[] array) {
        List<Object> result = null;
        if (null != array) {
            result = new ArrayList<>(0);

            Object toListResult = toList(array);
            if (toListResult instanceof List) {
                result.addAll((List<Object>) toListResult);
            } else {
                result.add(toListResult);
            }
        }
        return result;
    }

    /**
     * 转换为集合
     *
     * @Description 数组或非数组类型的对象转换为集合. 支持多维数组.
     *
     * @param arrayOrObj
     * @param <T>
     *
     * @return {@link Object}
     */
    public static <T> Object toList(@NotNull T arrayOrObj) {
        Class<?> type;
        Class<?> elementType;
        if (null != arrayOrObj && (type = arrayOrObj.getClass()).isArray()
                && null != (elementType = type.getComponentType())) {
            List<Object> result = new ArrayList<>(0);

            if (elementType /*instanceof*/ == Integer.TYPE) {
                for (int each : ((int[]) arrayOrObj)) {
                    result.add(each);
                }
                return result;
            }
            if (elementType == Short.TYPE) {
                for (short each : ((short[]) arrayOrObj)) {
                    result.add(each);
                }
                return result;
            }
            if (elementType == Long.TYPE) {
                for (long each : ((long[]) arrayOrObj)) {
                    result.add(each);
                }
                return result;
            }
            if (elementType == Float.TYPE) {
                for (float each : ((float[]) arrayOrObj)) {
                    result.add(each);
                }
                return result;
            }
            if (elementType == Double.TYPE) {
                for (double each : ((double[]) arrayOrObj)) {
                    result.add(each);
                }
                return result;
            }
            if (elementType == Character.TYPE) {
                for (char each : ((char[]) arrayOrObj)) {
                    result.add(each);
                }
                return result;
            }
            if (elementType == Byte.TYPE) {
                for (byte each : ((byte[]) arrayOrObj)) {
                    result.add(each);
                }
                return result;
            }
            if (elementType == Boolean.TYPE) {
                for (boolean each : ((boolean[]) arrayOrObj)) {
                    result.add(each);
                }
                return result;
            }
            // 处理对象数组
            if (elementType instanceof Object) {
                for (Object each : ((Object[]) arrayOrObj)) {
                    if (null != each && each.getClass().isArray())
                        result.add(toList(each));
                    else
                        result.add(each);
                }
                return result;
            }
        }
        return arrayOrObj;
    }

}
