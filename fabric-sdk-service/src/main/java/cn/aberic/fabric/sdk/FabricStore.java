/*
 * Copyright (c) 2018. Aberic - All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.aberic.fabric.sdk;

import org.apache.commons.io.IOUtils;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.hyperledger.fabric.sdk.Enrollment;

import java.io.*;
import java.security.PrivateKey;
import java.security.Security;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 联盟存储配置对象
 *
 * @author 杨毅【2017/9/7 16:36】
 */
class FabricStore {

    private String file;
    /**
     * 用户信息集合
     */
    private final Map<String, IntermediateUser> members = new HashMap<>();

    FabricStore(File file) {
        this.file = file.getAbsolutePath();
    }

    /**
     * 设置与名称相关的值
     *
     * @param name  名称
     * @param value 相关值
     */
    void setValue(String name, String value) {
        Properties properties = loadProperties();
        try (OutputStream output = new FileOutputStream(file)) {
            properties.setProperty(name, value);
            properties.store(output, "");
        } catch (IOException e) {
            System.out.println(String.format("Could not save the keyvalue store, reason:%s", e.getMessage()));
        }
    }

    /**
     * 获取与名称相关的值
     *
     * @param name 名称
     * @return 相关值
     */
    String getValue(String name) {
        Properties properties = loadProperties();
        return properties.getProperty(name);
    }

    /**
     * 加载配置文件
     *
     * @return 配置文件对象
     */
    private Properties loadProperties() {
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream(file)) {
            properties.load(input);
        } catch (FileNotFoundException e) {
            System.out.println(String.format("Could not find the file \"%s\"", file));
        } catch (IOException e) {
            System.out.println(String.format("Could not load keyvalue store from file \"%s\", reason:%s", file, e.getMessage()));
        }
        return properties;
    }

    /**
     * 用给定的名称获取用户
     *
     * @param name    用户名称（User1）
     * @param orgName 组织名称（Org1）
     * @return 用户
     */
    IntermediateUser getMember(String name, String orgName) {
        // 尝试从缓存中获取User状态
        IntermediateUser user = members.get(IntermediateUser.getKeyForFabricStoreName(name, orgName));
        if (null != user) {
            return user;
        }
        // 创建User，并尝试从键值存储中恢复它的状态(如果找到的话)
        user = new IntermediateUser(name, orgName, this);
        members.put(IntermediateUser.getKeyForFabricStoreName(name, orgName), user);
        return user;
    }

    /**
     * 用给定的名称获取用户
     *
     * @param name            用户名称（User1）
     * @param org             组织名称（Org1）
     * @param mspId           会员id
     * @param privateKeyFile  私钥
     * @param certificateFile 证书
     * @return user 用户
     */
    IntermediateUser getMember(String name, String org, String mspId, File privateKeyFile, File certificateFile) throws IOException {
        // 尝试从缓存中获取User状态
        IntermediateUser user = members.get(IntermediateUser.getKeyForFabricStoreName(name, org));
        if (null != user) {
            System.out.println("尝试从缓存中获取User状态 User = " + user);
            return user;
        }
        // 创建User，并尝试从键值存储中恢复它的状态(如果找到的话)
        user = new IntermediateUser(name, org, this);
        user.setMspId(mspId);
        String certificate = new String(IOUtils.toByteArray(new FileInputStream(certificateFile)), "UTF-8");
        PrivateKey privateKey = getPrivateKeyFromBytes(IOUtils.toByteArray(new FileInputStream(privateKeyFile)));
        user.setEnrollment(new StoreEnrollment(privateKey, certificate));
        user.saveState();
        members.put(IntermediateUser.getKeyForFabricStoreName(name, org), user);
        return user;
    }

    /**
     * 通过字节数组信息获取私钥
     *
     * @param data 字节数组
     * @return 私钥
     */
    private PrivateKey getPrivateKeyFromBytes(byte[] data) throws IOException {
        final Reader pemReader = new StringReader(new String(data));
        final PrivateKeyInfo pemPair;
        try (PEMParser pemParser = new PEMParser(pemReader)) {
            pemPair = (PrivateKeyInfo) pemParser.readObject();
        }
//        PrivateKey privateKey = new JcaPEMKeyConverter().setProvider(BouncyCastleProvider.PROVIDER_NAME).getPrivateKey(pemPair);
//        return privateKey;
        return new JcaPEMKeyConverter().setProvider(BouncyCastleProvider.PROVIDER_NAME).getPrivateKey(pemPair);
    }

    static {
        try {
            Security.addProvider(new BouncyCastleProvider());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 自定义注册登记操作类
     *
     * @author yangyi47
     */
    static final class StoreEnrollment implements Enrollment, Serializable {

        private static final long serialVersionUID = 6965341351799577442L;

        /** 私钥 */
        private final PrivateKey privateKey;
        /** 授权证书 */
        private final String certificate;

        StoreEnrollment(PrivateKey privateKey, String certificate) {
            this.certificate = certificate;
            this.privateKey = privateKey;
        }

        @Override
        public PrivateKey getKey() {
            return privateKey;
        }

        @Override
        public String getCert() {
            return certificate;
        }
    }

}
