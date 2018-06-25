package cn.aberic.fabric.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.thrift.TException;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class FileUtil {

    private static Logger log = LogManager.getLogger(FileUtil.class);

    private static final int BUFFER_SIZE = 2 * 1024;

    /**
     * 解压指定zip文件
     *
     * @param unZipFile 压缩文件的路径
     * @param destFile  解压到的目录
     */
    private static void unZip(String unZipFile, String destFile) {
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
            log.error("Error", e);
        }
    }

    private static void unZipAndSave(String unZipFile, String destFile) throws IOException {
        File dest = new File(unZipFile);
        unZip(unZipFile, destFile);
        dest.delete();
    }

    /**
     * 通过递归得到某一路径下所有的目录及其文件并删除所有文件
     *
     * @param filePath 文件夹路径
     */
    public static void deleteFiles(String filePath) {
        File root = new File(filePath);
        if (!root.exists()) {
            return;
        }
        File[] files = root.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                deleteFiles(file.getAbsolutePath());// 递归调用
                file.delete();
                log.debug(String.format("显示%s下所有子目录及其文件", file.getAbsolutePath()));
            } else {
                file.delete();
                log.debug(String.format("显示%s下所有子目录%s====文件名：%s", filePath, file.getAbsolutePath(), file.getName()));
            }
        }
    }

    public static void save(ByteBuffer buff, String fileName, String path, boolean samePath) throws TException {
        InputStream is = new ByteArrayInputStream(buff.array());
        OutputStream os = null;
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        try {
            String unZipFile = path + File.separator + fileName;
            File zipFile = new File(path + File.separator + fileName);
            if (zipFile.getParentFile().exists() && samePath) {
                zipFile.getParentFile().delete();
            }
            if (samePath) {
                zipFile.getParentFile().mkdirs();
            }
            os = new FileOutputStream(zipFile);
            while ((bytesRead = is.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            FileUtil.unZipAndSave(unZipFile, path);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                if (null != os) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
