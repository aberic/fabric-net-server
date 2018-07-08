package cn.aberic.fabric.utils.encryption;

import cn.aberic.fabric.bean.Key;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import java.io.*;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Objects;

/**
 * 描述：
 *
 * @author : Aberic 【2018/5/17 15:51】
 */
@Slf4j
public class Utils {

    private static Utils instance = null;

    public static Utils obtain() {
        if (null == instance) {
            synchronized (Utils.class) {
                if (null == instance) {
                    instance = new Utils();
                }
            }
        }
        return instance;
    }

    private Utils() {}

    /**
     * 在指定路径的目录下随机创建以".keystore"为后缀的公私钥。
     * 最终公私钥存储在path目录下并分别命名为keyName_privateKey.keystore及keyName_publicKey.keystore
     *
     * @param path    指定路径的目录
     * @param keyName 公私钥前置名称
     */
    public void createRSAKeyPair(String path, String keyName) {
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGenerator;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return;
        }
        // 初始化密钥对生成器，密钥大小为96-1024位，使用SecureRandom生成强随机数
        keyPairGenerator.initialize(1024, new SecureRandom());
        KeyPair keyPair = keyPairGenerator.generateKeyPair(); // 生成一个密钥对，保存在keyPair中
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic(); // 得到公钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate(); // 得到私钥
        String publicKeyStr = Base64.encodeBase64String(publicKey.getEncoded()); // 得到公钥字符串
        String privateKeyStr = Base64.encodeBase64String(privateKey.getEncoded()); // 得到私钥字符串
        log.debug("publicKeyStr = " + publicKeyStr);
        log.debug("privateKeyStr = " + privateKeyStr);
        try {
            File pubFile = new File(String.format("%s/%s_publicRSA.key", path, keyName));
            File priFile = new File(String.format("%s/%s_privateRSA.key", path, keyName));
            if (!pubFile.getParentFile().exists()) {
                pubFile.getParentFile().mkdirs();
            }
            if (!pubFile.exists()) {
                pubFile.createNewFile();
            }
            if (!priFile.getParentFile().exists()) {
                priFile.getParentFile().mkdirs();
            }
            if (!priFile.exists()) {
                priFile.createNewFile();
            }
            // 将密钥对写入到文件
            FileWriter pubfw = new FileWriter(String.format("%s/%s_publicRSA.key", path, keyName));
            FileWriter prifw = new FileWriter(String.format("%s/%s_privateRSA.key", path, keyName));
            BufferedWriter pubbw = new BufferedWriter(pubfw);
            BufferedWriter pribw = new BufferedWriter(prifw);
            pubbw.write(publicKeyStr);
            pribw.write(privateKeyStr);
            pubbw.flush();
            pubbw.close();
            pubfw.close();
            pribw.flush();
            pribw.close();
            prifw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 在指定路径的目录下随机创建以".keystore"为后缀的公私钥。
     * 最终公私钥存储在path目录下并分别命名为keyName_private.key及keyName_public.key
     *
     * @param path    指定路径的目录
     * @param keyName 公私钥前置名称
     */
    public void createECCDSAKeyPair(String path, String keyName) {
        KeyPairGenerator keyPairGenerator;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance("EC");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return;
        }
        // 初始化密钥对生成器，密钥大小为96-1024位
        keyPairGenerator.initialize(256);
        KeyPair keyPair = keyPairGenerator.generateKeyPair(); // 生成一个密钥对，保存在keyPair中
        ECPublicKey publicKey = (ECPublicKey) keyPair.getPublic(); // 得到公钥
        ECPrivateKey privateKey = (ECPrivateKey) keyPair.getPrivate(); // 得到私钥
        String publicKeyStr = Base64.encodeBase64String(publicKey.getEncoded()); // 得到公钥字符串
        String privateKeyStr = Base64.encodeBase64String(privateKey.getEncoded()); // 得到私钥字符串
        log.debug("publicKeyStr = " + publicKeyStr);
        log.debug("privateKeyStr = " + privateKeyStr);
        try {
            // 将密钥对写入到文件
            FileWriter pubfw = new FileWriter(String.format("%s/%s_publicECDSA.key", path, keyName));
            FileWriter prifw = new FileWriter(String.format("%s/%s_privateECDSA.key", path, keyName));
            BufferedWriter pubbw = new BufferedWriter(pubfw);
            BufferedWriter pribw = new BufferedWriter(prifw);
            pubbw.write(publicKeyStr);
            pribw.write(privateKeyStr);
            pubbw.flush();
            pubbw.close();
            pubfw.close();
            pribw.flush();
            pribw.close();
            prifw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 在指定路径的目录下随机创建以".keystore"为后缀的公私钥。
     * 最终公私钥存储在path目录下并分别命名为keyName_private.key及keyName_public.key
     */
    public Key createECCDSAKeyPair() {
        KeyPairGenerator keyPairGenerator;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance("EC");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        // 初始化密钥对生成器，密钥大小为96-1024位
        keyPairGenerator.initialize(256);
        KeyPair keyPair = keyPairGenerator.generateKeyPair(); // 生成一个密钥对，保存在keyPair中
        ECPublicKey publicKey = (ECPublicKey) keyPair.getPublic(); // 得到公钥
        ECPrivateKey privateKey = (ECPrivateKey) keyPair.getPrivate(); // 得到私钥
        String publicKeyStr = Base64.encodeBase64String(publicKey.getEncoded()); // 得到公钥字符串
        String privateKeyStr = Base64.encodeBase64String(privateKey.getEncoded()); // 得到私钥字符串
        log.debug("publicKeyStr = " + publicKeyStr);
        log.debug("privateKeyStr = " + privateKeyStr);
        return new Key(privateKeyStr, publicKeyStr);
    }

    /**
     * 获取key路径
     *
     * @return /WEB-INF/classes/fabric/${resName}/
     */
    public String getKeyPath(String resName) {
        log.debug("getResource = " + Utils.class.getClassLoader().getResource(resName));
        String directories = Objects.requireNonNull(Utils.class.getClassLoader().getResource(resName)).getFile();
        File directory = new File(directories);
        log.debug("directories = " + directories);
        return directory.getPath();
    }

    /**
     * 根据path获取指定file中的字符串内容
     *
     * @param filePath 指定路径的文件
     *
     * @return 文件中字符串内容
     */
    public String getStringByFile(String filePath) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String readLine;
            StringBuilder sb = new StringBuilder();
            while ((readLine = br.readLine()) != null) {
                sb.append(readLine);
            }
            br.close();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("IOException：公钥数据流读取错误");
        }
    }

}
