package cn.aberic.fabric.utils.encryption.rsa;

import cn.aberic.fabric.utils.encryption.Utils;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * 描述：RSA加解密工具类
 *
 * @author : Aberic 【2018/5/16 18:06】
 */
public class RSAEncrypt {

    private static RSAEncrypt instance = null;

    public static RSAEncrypt obtain() {
        if (null == instance) {
            synchronized (RSAEncrypt.class) {
                if (null == instance) {
                    instance = new RSAEncrypt();
                }
            }
        }
        return instance;
    }

    private RSAEncrypt() {}

    /**
     * 通过公钥字符串获取公钥
     *
     * @param keyStr 公钥字符串信息
     * @return 公钥
     */
    private RSAPublicKey getPublicKeyByStr(String keyStr) {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.decodeBase64(keyStr));
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException("算法异常错误");
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
            throw new RuntimeException("无效X509EncodedKeySpec异常错误");
        }
    }

    /**
     * 通过私钥字符串获取公钥
     *
     * @param keyStr 私钥字符串信息
     * @return 私钥
     */
    private RSAPrivateKey getPrivateKeyByStr(String keyStr) {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(keyStr));
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException("算法异常错误");
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
            throw new RuntimeException("无效PKCS8EncodedKeySpec异常错误");
        }
    }

    /**
     * 使用公钥文件来执行对info内容的加密操作
     *
     * @param filePath 公钥文件所在路径（如：xx/xx/publicKey.keystore）
     * @param info     待加密内容
     * @return 加密后字符串
     */
    public String encryptPubFile(String filePath, String info) {
        return encryptByFile(filePath, info, 0, true);
    }

    /**
     * 使用公钥字符串信息来执行对info内容的加密操作
     *
     * @param keyStr 公钥字符串信息
     * @param info   待加密内容
     * @return 加密后字符串
     */
    public String encryptPubStr(String keyStr, String info) {
        return cryptByStr(keyStr, info, 0, true);
    }

    /**
     * 使用私钥文件来执行对info内容的加密操作
     *
     * @param filePath 私钥文件所在路径（如：xx/xx/privateKey.keystore）
     * @param info     待加密内容
     * @return 加密后字符串
     */
    public String encryptPriFile(String filePath, String info) {
        return encryptByFile(filePath, info, 1, true);
    }

    /**
     * 使用私钥字符串信息来执行对info内容的加密操作
     *
     * @param keyStr 私钥字符串信息
     * @param info   待加密内容
     * @return 加密后字符串
     */
    public String encryptPriStr(String keyStr, String info) {
        return cryptByStr(keyStr, info, 1, true);
    }

    /**
     * 使用公钥文件来执行对info内容的解密操作
     *
     * @param filePath 公钥文件所在路径（如：xx/xx/publicKey.keystore）
     * @param info     待解密内容
     * @return 解密后字符串
     */
    public String decryptPubFile(String filePath, String info) {
        return encryptByFile(filePath, info, 0, false);
    }

    /**
     * 使用公钥字符串信息来执行对info内容的解密操作
     *
     * @param keyStr 公钥字符串信息
     * @param info   待解密内容
     * @return 解密后字符串
     */
    public String decryptPubStr(String keyStr, String info) {
        return cryptByStr(keyStr, info, 0, false);
    }

    /**
     * 使用私钥文件来执行对info内容的解密操作
     *
     * @param filePath 私钥文件所在路径（如：xx/xx/privateKey.keystore）
     * @param info     待解密内容
     * @return 解密后字符串
     */
    public String decryptPriFile(String filePath, String info) {
        return encryptByFile(filePath, info, 1, false);
    }

    /**
     * 使用私钥字符串信息来执行对info内容的解密操作
     *
     * @param keyStr 私钥字符串信息
     * @param info   待解密内容
     * @return 解密后字符串
     */
    public String decryptPriStr(String keyStr, String info) {
        return cryptByStr(keyStr, info, 1, false);
    }

    /**
     * 使用公/私钥文件来执行对info内容的加解密操作
     *
     * @param filePath 公钥文件所在路径（如：xx/xx/publicKey.keystore或xx/xx/privateKey.keystore）
     * @param info     待加解密内容
     * @param type     type=0表示获取公钥，type=1表示获取私钥
     * @param encrypt  是否加密操作
     * @return 加解密后字符串
     */
    private String encryptByFile(String filePath, String info, int type, boolean encrypt) {
        return cryptByStr(Utils.obtain().getStringByFile(filePath), info, type, encrypt);
    }

    /**
     * 使用公/私钥字符串信息来执行对info内容的加解密操作
     *
     * @param keyStr  公/私钥字符串信息
     * @param info    待加解密内容
     * @param type    type=0表示获取公钥，type=1表示获取私钥
     * @param encrypt 是否加密操作
     * @return 加解密后字符串
     */
    private String cryptByStr(String keyStr, String info, int type, boolean encrypt) {
        byte[] infoBytes = encrypt ? info.getBytes() : Base64.decodeBase64(info);
        if (keyStr.isEmpty()) {
            throw new RuntimeException("加密公/私钥不能为空");
        }
        try {
            Cipher cipher = Cipher.getInstance("RSA"); // 使用RSA加解密
            cipher.init(encrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, type == 0 ? getPublicKeyByStr(keyStr) : getPrivateKeyByStr(keyStr));
            byte[] resultBytes = cipher.doFinal(infoBytes);
            return encrypt ? Base64.encodeBase64String(resultBytes) : new String(resultBytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException("算法异常错误");
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            throw new RuntimeException("无此加密算法");
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            throw new RuntimeException("加密公钥非法,请检查");
        } catch (BadPaddingException e) {
            e.printStackTrace();
            throw new RuntimeException("内容数据已损坏");
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
            throw new RuntimeException("内容长度非法");
        }
    }

}
