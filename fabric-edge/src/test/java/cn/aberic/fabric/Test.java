package cn.aberic.fabric;

import cn.aberic.fabric.utils.encryption.Utils;
import cn.aberic.fabric.utils.encryption.ecc.ECDSAEncrypt;
import cn.aberic.fabric.utils.encryption.ecc.ECDSASignature;
import cn.aberic.fabric.utils.encryption.rsa.RSAEncrypt;
import cn.aberic.fabric.utils.encryption.rsa.RSASignature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

/**
 * `
 * <p>
 * 描述：
 *
 * @author : Aberic 【2018/5/16 14:38】
 */
@Slf4j
public class Test {

    @Value("${key.dir}")
    private static String keyPath;

    public static void main(String[] args) {
        testCrypt();
    }

    private static void testCrypt(){
        String filePath = "/Users/Aberic/Documents/tmp/key";
        Utils.obtain().createRSAKeyPair(filePath, "Test");
        String pubFilePath = String.format("%s/%s_publicRSA.key", filePath, "Test");
        String priFilePath = String.format("%s/%s_privateRSA.key", filePath, "Test");
        log.debug("--------------RSA公钥加密私钥解密过程-------------------");
        String info = "RSA公钥加密私钥解密过程";
        //公钥加密
        String enStr = RSAEncrypt.obtain().encryptPubFile(pubFilePath, info);
        //私钥解密
        String deStr = RSAEncrypt.obtain().decryptPriFile(priFilePath, enStr);
        log.debug("原文：" + info);
        log.debug("加密：" + enStr);
        log.debug("解密：" + deStr);
        log.info("=====================================================================================");
        log.debug("--------------RSA私钥加密公钥解密过程-------------------");
        info = "RSA私钥加密公钥解密过程";
        //私钥加密
        enStr = RSAEncrypt.obtain().encryptPriFile(priFilePath, info);
        //公钥解密
        deStr = RSAEncrypt.obtain().decryptPubFile(pubFilePath, enStr);
        log.debug("原文：" + info);
        log.debug("加密：" + enStr);
        log.debug("解密：" + deStr);
        log.info("=====================================================================================");
        log.debug("--------------RSA私钥签名过程-------------------");
        info = "这是用于RSA签名的原始数据";
        String signStr = RSASignature.obtain().signByFile(info, priFilePath);
        log.debug("签名原串：" + info);
        log.debug("签名串：" + signStr);
        log.info("=====================================================================================");
        log.debug("--------------RSA公钥校验签名-------------------");
        log.debug("校验结果 = " + RSASignature.obtain().verifyByFile(info, signStr, pubFilePath));





        log.info("=====================================================================================");
        log.debug("=====================================================================================");
        log.info("=====================================================================================");
        Utils.obtain().createECCDSAKeyPair(filePath, "Test");
        pubFilePath = String.format("%s/%s_publicECDSA.key", filePath, "Test");
        priFilePath = String.format("%s/%s_privateECDSA.key", filePath, "Test");
        log.debug("--------------ECDSA公钥加密私钥解密过程-------------------");
        info = "ECDSA公钥加密私钥解密过程";
        //公钥加密
        enStr = ECDSAEncrypt.obtain().encryptByFile(pubFilePath, info);
        //私钥解密
        deStr = ECDSAEncrypt.obtain().decryptPriFile(priFilePath, enStr);
        log.debug("原文：" + info);
        log.debug("加密：" + enStr);
        log.debug("解密：" + deStr);
        log.info("=====================================================================================");
        log.debug("--------------ECDSA私钥签名过程-------------------");
        info = "这是用于ECDSA签名的原始数据";
        signStr = ECDSASignature.obtain().signByFile(info, priFilePath);
        log.debug("签名原串：" + info);
        log.debug("签名串：" + signStr);
        log.info("=====================================================================================");
        log.debug("--------------ECDSA公钥校验签名-------------------");
        log.debug("校验结果 = " + ECDSASignature.obtain().verifyByFile(info, signStr, pubFilePath));
    }

}
