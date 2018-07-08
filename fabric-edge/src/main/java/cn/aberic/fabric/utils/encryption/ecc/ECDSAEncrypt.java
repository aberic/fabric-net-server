package cn.aberic.fabric.utils.encryption.ecc;

import cn.aberic.fabric.utils.encryption.Utils;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NullCipher;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.*;

/**
 * 描述：ECDSA（椭圆加密算法）加解密工具类
 *
 * @author : Aberic 【2018/5/17 14:57】
 */
public class ECDSAEncrypt {

    private static ECDSAEncrypt instance = null;

    public static ECDSAEncrypt obtain() {
        if (null == instance) {
            synchronized (ECDSAEncrypt.class) {
                if (null == instance) {
                    instance = new ECDSAEncrypt();
                }
            }
        }
        return instance;
    }

    private ECDSAEncrypt() {}

    /**
     * 通过公钥字符串获取公钥
     *
     * @param keyStr 公钥字符串信息
     * @return 公钥
     */
    private ECPublicKey getPublicKeyByStr(String keyStr) {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.decodeBase64(keyStr));
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("EC");
            return (ECPublicKey) keyFactory.generatePublic(keySpec);
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
    private ECPrivateKey getPrivateKeyByStr(String keyStr) {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(keyStr));
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("EC");
            return (ECPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException("算法异常错误");
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
            throw new RuntimeException("无效PKCS8EncodedKeySpec异常错误");
        }
    }

    /**
     * 使用私钥文件来执行对info内容的解密操作
     *
     * @param filePath 私钥文件所在路径（如：xx/xx/privateECDSA.key）
     * @param info     待解密内容
     * @return 解密后字符串
     */
    public String decryptPriFile(String filePath, String info) {
        return decryptPriStr(Utils.obtain().getStringByFile(filePath), info);
    }

    /**
     * 使用私钥字符串信息来执行对info内容的解密操作
     *
     * @param keyStr 私钥字符串信息
     * @param info   待解密内容
     * @return 解密后字符串
     */
    public String decryptPriStr(String keyStr, String info) {
        byte[] infoBytes = Base64.decodeBase64(info);
        if (keyStr.isEmpty()) {
            throw new RuntimeException("加密公钥不能为空");
        }
        ECPrivateKey privateKey = getPrivateKeyByStr(keyStr);
        try {
            ECPrivateKeySpec privateKeySpec = new ECPrivateKeySpec(privateKey.getS(), privateKey.getParams());
            Cipher cipher = new NullCipher();
            cipher.init(Cipher.DECRYPT_MODE, privateKey, privateKeySpec.getParams());
            return new String(cipher.doFinal(infoBytes));
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            throw new RuntimeException("加密公钥非法,请检查");
        } catch (BadPaddingException e) {
            e.printStackTrace();
            throw new RuntimeException("内容数据已损坏");
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
            throw new RuntimeException("内容长度非法");
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
            throw new RuntimeException("无效的算法参数异常");
        }
    }

    /**
     * 使用公钥文件来执行对info内容的加解密操作
     *
     * @param filePath 公钥文件所在路径（如：xx/xx/publicECDSA.key）
     * @param info     待加解密内容
     * @return 加解密后字符串
     */
    public String encryptByFile(String filePath, String info) {
        return encryptByStr(Utils.obtain().getStringByFile(filePath), info);
    }

    /**
     * 使用公钥字符串信息来执行对info内容的加密操作
     *
     * @param keyStr 公钥字符串信息
     * @param info   待加密内容
     * @return 加密后字符串
     */
    public String encryptByStr(String keyStr, String info) {
        byte[] infoBytes = info.getBytes();
        if (keyStr.isEmpty()) {
            throw new RuntimeException("加密公钥不能为空");
        }
        ECPublicKey publicKey = getPublicKeyByStr(keyStr);
        try {
            ECPublicKeySpec publicKeySpec = new ECPublicKeySpec(publicKey.getW(), publicKey.getParams());
            Cipher cipher = new NullCipher();
            cipher.init(Cipher.ENCRYPT_MODE, publicKey, publicKeySpec.getParams());
            return Base64.encodeBase64String(cipher.doFinal(infoBytes));
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            throw new RuntimeException("加密公钥非法,请检查");
        } catch (BadPaddingException e) {
            e.printStackTrace();
            throw new RuntimeException("内容数据已损坏");
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
            throw new RuntimeException("内容长度非法");
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
            throw new RuntimeException("无效的算法参数异常");
        }
    }

}
