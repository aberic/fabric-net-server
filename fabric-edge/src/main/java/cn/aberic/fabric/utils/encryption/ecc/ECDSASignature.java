package cn.aberic.fabric.utils.encryption.ecc;

import cn.aberic.fabric.utils.encryption.Utils;
import org.apache.commons.codec.binary.Base64;

import javax.annotation.Nullable;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * 描述：ECDSA（椭圆加密算法）签名验证工具类
 *
 * @author : Aberic 【2018/5/17 14:57】
 */
public class ECDSASignature {
    /** 签名算法 */
    private final String SIGN_ALGORITHMS = "SHA1WithECDSA";

    private static ECDSASignature instance = null;

    public static ECDSASignature obtain() {
        if (null == instance) {
            synchronized (ECDSASignature.class) {
                if (null == instance) {
                    instance = new ECDSASignature();
                }
            }
        }
        return instance;
    }

    private ECDSASignature() {}

    /**
     * ECDSA私钥签名字符串
     *
     * @param info     待签名字符串
     * @param filePath 私钥文件所在路径（如：xx/xx/privateKey.keystore）
     * @return 签名值
     */
    public String signByFile(String info, String filePath) {
        return signByStr(info, Utils.obtain().getStringByFile(filePath));
    }

    /**
     * ECDSA私钥签名字符串
     *
     * @param info     待签名字符串
     * @param filePath 私钥文件所在路径（如：xx/xx/privateKey.keystore）
     * @param encode   编码
     * @return 签名值
     */
    public String signByFile(String info, String filePath, @Nullable String encode) {
        return signByStr(info, Utils.obtain().getStringByFile(filePath), encode);
    }

    /**
     * ECDSA私钥签名字符串
     *
     * @param info          待签名字符串
     * @param privateKeyStr 私钥字符串内容
     * @return 签名值
     */
    public String signByStr(String info, String privateKeyStr) {
        return signByStr(info, privateKeyStr, null);
    }

    /**
     * ECDSA私钥签名字符串
     *
     * @param info          待签名字符串
     * @param privateKeyStr 私钥字符串内容
     * @param encode        编码
     * @return 签名值
     */
    public String signByStr(String info, String privateKeyStr, @Nullable String encode) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("EC");
            PrivateKey privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKeyStr)));
            Signature signature = Signature.getInstance(SIGN_ALGORITHMS);
            signature.initSign(privateKey);
            if (encode == null) {
                signature.update(info.getBytes());
            } else {
                signature.update(info.getBytes(encode));
            }
            byte[] signed = signature.sign();
            return Base64.encodeBase64String(signed);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException | SignatureException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * ECDSA公钥签名校验
     *
     * @param info     待签名字符串
     * @param sign     签名值
     * @param filePath 公钥文件所在路径（如：xx/xx/publicKey.keystore）
     * @return 校验结果
     */
    public boolean verifyByFile(String info, String sign, String filePath) {
        return verifyByStr(info, sign, Utils.obtain().getStringByFile(filePath));
    }

    /**
     * ECDSA公钥签名校验
     *
     * @param info     待签名字符串
     * @param sign     签名值
     * @param filePath 公钥文件所在路径（如：xx/xx/publicKey.keystore）
     * @return 校验结果
     */
    public boolean verifyByFile(String info, String sign, String filePath, @Nullable String encode) {
        return verifyByStr(info, sign, Utils.obtain().getStringByFile(filePath), encode);
    }

    /**
     * ECDSA公钥签名校验
     *
     * @param info         待签名字符串
     * @param sign         签名值
     * @param publicKeyStr 公钥字符串内容
     * @return 校验结果
     */
    public boolean verifyByStr(String info, String sign, String publicKeyStr) {
        return verifyByStr(info, sign, publicKeyStr, null);
    }

    /**
     * ECDSA公钥签名校验
     *
     * @param info         待签名字符串
     * @param sign         签名值
     * @param publicKeyStr 公钥字符串内容
     * @param encode       编码
     * @return 校验结果
     */
    public boolean verifyByStr(String info, String sign, String publicKeyStr, @Nullable String encode) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("EC");
            PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(Base64.decodeBase64(publicKeyStr)));
            Signature signature = Signature.getInstance(SIGN_ALGORITHMS);
            signature.initVerify(publicKey);
            if (encode == null) {
                signature.update(info.getBytes());
            } else {
                signature.update(info.getBytes(encode));
            }
            return signature.verify(Base64.decodeBase64(sign));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException | SignatureException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return false;
    }

}
