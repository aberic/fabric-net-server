package cn.aberic.fabric.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipUtil {

    private static Logger logger = LogManager.getLogger(ZipUtil.class);

    private static final int BUFFER_SIZE = 2 * 1024;

    /**
     * 解压指定zip文件
     *
     * @param unZipFile 压缩文件的路径
     * @param destFile  解压到的目录
     */
    public static void unZip(String unZipFile, String destFile) {
        FileOutputStream fileOut;
        File file;
        InputStream inputStream;

        try {
            // 生成一个zip的文件
            ZipFile zipFile = new ZipFile(unZipFile);
            Enumeration enumeration = zipFile.entries();
            while (enumeration.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) enumeration.nextElement();
                file = new File(destFile + File.separator + entry.getName());
                if (entry.isDirectory()) {
                    file.mkdirs();
                } else {
                    // 如果指定文件的目录不存在,则创建之.
                    File parent = file.getParentFile();
                    if (!parent.exists()) {
                        parent.mkdirs();
                    }
                    // 获取出该压缩实体的输入流
                    inputStream = zipFile.getInputStream(entry);

                    fileOut = new FileOutputStream(file);
                    int length = 0;
                    // 将实体写到本地文件中去
                    byte[] buffer = new byte[BUFFER_SIZE];
                    while ((length = inputStream.read(buffer)) > 0) {
                        fileOut.write(buffer, 0, length);
                    }
                    fileOut.close();
                    inputStream.close();
                }
            }
            zipFile.close();
        } catch (IOException e) {
            logger.error("Error", e);
        }
    }
}
