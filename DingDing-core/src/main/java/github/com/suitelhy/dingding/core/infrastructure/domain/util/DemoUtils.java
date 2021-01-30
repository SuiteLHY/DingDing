package github.com.suitelhy.dingding.core.infrastructure.domain.util;

public class DemoUtils {

    public static void show(Object arg) {
        if (null != arg) {
            String content = (arg instanceof String) ? (String) arg : arg.toString();
            System.out.println(content);
        } else {
            System.out.println();
        }
    }

}
