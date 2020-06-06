package github.com.suitelhy.dingding.app.infrastructure.util;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * <tt>RegexUtil</tt> 的 <code>Pattern</code> 缓存池
 *
 * @Description 由于是项目级别的工具类, 使用必定非常频繁, 故采用单例
 *-> (枚举实现单例便于业务拓展) 来缓存并限制最大容量, 提高性能、精简资源.
 *-> 该缓存仅提供读取和插入 <code>Pattern</code> 的操作.
 * @Reference
 *-> <a href="https://examples.javacodegeeks.com/core-java/util/regex/patternsyntaxexception/java-util-regex-patternsyntaxexception-example/">
 *->     java.util.regex.PatternSyntaxException Example | Examples Java Code Geeks - 2019</a>
 *-> , <a href="https://github.com/CyC2018/CS-Notes/blob/master/notes/Java%20%E5%B9%B6%E5%8F%91.md#%E4%B9%9D%E7%BA%BF%E7%A8%8B%E4%B8%8D%E5%AE%89%E5%85%A8%E7%A4%BA%E4%BE%8B">
 *->     CS-Notes/Java 并发.md at master · CyC2018/CS-Notes</a>
 *-> , <a href="https://www.jianshu.com/p/d9977a048dab">线程不安全的SimpleDateFormat - 简书</a>
 *-> , <a href="https://my.oschina.net/githubhty/blog/658633">
 *->     java并发问题，java并发容器解决全局变量的并发问题 - 帅的不像男的的个人空间 - OSCHINA</a>
 *-> , <a href="https://github.com/CyC2018/CS-Notes/blob/master/notes/Java%20%E5%B9%B6%E5%8F%91.md#%E5%8D%81java-%E5%86%85%E5%AD%98%E6%A8%A1%E5%9E%8B">
 *->     CS-Notes/Java 并发.md at master · CyC2018/CS-Notes</a>
 *
 */
enum RegexUtilCache {
    SINGLETON;

    public static final int INITIAL_CAPACITY = 10;

    public static final int MAX_CAPACITY = INITIAL_CAPACITY + 512;

    public static Pattern pattern(@NotNull String regex) {
        Pattern result = null;
        try {
            result = Pattern.compile(regex);
        } catch (PatternSyntaxException e) {
            e.printStackTrace();
        } finally {
            return result;
        }
    }

    private final Map<String, Pattern> REGEX_PATTERN_MAP;

    RegexUtilCache() {
        this.REGEX_PATTERN_MAP = new HashMap<>(INITIAL_CAPACITY);
    }

    public boolean add(@NotNull String regex) {
        Pattern pattern;
        if (null != regex
                && REGEX_PATTERN_MAP.size() != MAX_CAPACITY
                && null != (pattern = pattern(regex))) {
            //--- 并发环境下保证一致性
            synchronized (REGEX_PATTERN_MAP) {
                REGEX_PATTERN_MAP.put(regex, pattern);
            }
            return true;
        }
        return false;
    }

    /**
     * 从缓存池中获取 <code>Pattern</code> 实例, 若不存在则创建并添加至缓存池
     * @param regex
     * @return 可为 null, 此时 <param>regex</param> 不符合正则表达式的模式.
     */
    public Pattern create(@NotNull String regex) {
        return null != get(regex)
                ? get(regex)
                : (add(regex) ? get(regex) : pattern(regex));
    }

    public Pattern get(@NotNull String regex) {
        return (null != regex && REGEX_PATTERN_MAP.size() != MAX_CAPACITY)
                ? REGEX_PATTERN_MAP.get(regex)
                : null;
    }

}

/**
 * Regex 工具类
 *
 * @Description 使用缓存池提高性能、精简资源占用.
 * @Reference <a href="https://docs.oracle.com/javase/7/docs/api/java/util/regex/Pattern.html">
 *->     Pattern (Java Platform SE 7 )</a>
 */
public final class RegexUtil {

    //===== constructor =====//
    private RegexUtil() {}

    //===== static domain =====//
    /*// 共用的全局变量应该进行基于并发操作环境的设计
    private static final Map<String, Pattern> REGEX_PATTERN_MAP = new HashMap<>(10);*/

    /**
     * 尝试获取 <param>regex</param> 对应的正则表达式 <code>Pattern</code> 实例
     * @param regex
     * @return
     */
    public static Pattern getPattern(@NotNull String regex) {
        return RegexUtilCache.SINGLETON.create(regex);
    }

    /**
     * 尝试根据 <param>regex</param> 对应的正则表达式匹配整个 <param>input</param>
     * @param regex
     * @param input
     * @return 可为 null, 此时 <param>regex</param> 不符合正则表达式的模式.
     */
    public static Boolean matchers(@NotNull String regex, @NotNull String input) {
        Pattern pattern;
        return null != (pattern = RegexUtilCache.SINGLETON.create(regex))
                ? pattern.matcher(input).matches()
                : null;
    }

}
