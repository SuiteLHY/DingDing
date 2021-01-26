package github.com.suitelhy.dingding.core.infrastructure.web.util;

import java.io.File;

public class CommonValidate {

    /**
     * 补全路径尾部
     *
     * @param path 路径
     * @return <code>path</code>
     */
    public String checkPathEnd(String path) {
        return path.endsWith("\\") ? path : path + "\\";
    }

    /**
     * 补全字符串后缀斜杠
     *
     * @param str 字符串
     *
     * @return
     */
    public String checkStringEnd(String str) {
        return str.endsWith("/") ? str : (str + "/");
    }

    /**
     * 判断文件是否存在
     *
     * @param path     路径
     * @param fileName 文件名
     *
     * @return true|false
     */
    public boolean validateFileExist(String path, String fileName) {
        return new File(checkPathEnd(path) + fileName).exists();
    }

    /**
     * 判断文件是否存在
     *
     * @param path  路径 + 文件名
     *
     * @return true | false
     */
    public boolean validateFileExist(String path) {
        return new File(path).exists();
    }

    /**
     * 判断文件是否存在, 在指定文件路径下的所有层级中查找
     *
     * @param path     文件路径
     * @param fileName 文件名
     * @param mark     标记
     *
     * @return 可用的文件名
     */
    public String validateRepeatFilename(String path
            , String fileName
            , String mark) {
        if (fileName.contains(".")) {//--- 如果文件有后缀名
            while (validateFileExist(path, fileName)) {
                String name = fileName.substring(0, fileName.lastIndexOf("."));
                String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
                fileName = name + mark + "." + prefix;
                validateRepeatFilename(path, fileName, mark);
            }
        } else {//--- 如果没有后缀名
            StringBuilder fileNameSB = new StringBuilder(fileName);
            while (validateFileExist(path, fileName)) {
                fileName += mark;
                validateRepeatFilename(path, fileName, mark);
            }
        }
        return fileName;
    }

}
