package org.hyperledger.fabric.sdk.aberic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.hyperledger.fabric.sdk.Enrollment;

/**
 * 联盟存储配置对象
 * 
 * @author 杨毅
 *
 * @date 2017年9月7日 - 下午4:36:19
 * @email abericyang@gmail.com
 */
class FabricStore {

	private String file;
	/** 用户信息集合 */
	private final Map<String, FabricUser> members = new HashMap<>();

	public FabricStore(File file) {
		this.file = file.getAbsolutePath();
	}

	/**
	 * 设置与名称相关的值
	 *
	 * @param name
	 *            名称
	 * @param value
	 *            相关值
	 */
	public void setValue(String name, String value) {
		Properties properties = loadProperties();
		try (OutputStream output = new FileOutputStream(file)) {
			properties.setProperty(name, value);
			properties.store(output, "");
			output.close();
		} catch (IOException e) {
			System.out.println(String.format("Could not save the keyvalue store, reason:%s", e.getMessage()));
		}
	}

	/**
	 * 获取与名称相关的值
	 *
	 * @param 名称
	 * @return 相关值
	 */
	public String getValue(String name) {
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
			input.close();
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
	 * @param 名称
	 * @param 组织
	 * 
	 * @return 用户
	 */
	public FabricUser getMember(String name, String org) {
		// 尝试从缓存中获取User状�??
		FabricUser fabricUser = members.get(FabricUser.toKeyValStoreName(name, org));
		if (null != fabricUser) {
			return fabricUser;
		}
		// 创建User，并尝试从键值存储中恢复它的状�??(如果找到的话)�?
		fabricUser = new FabricUser(name, org, this);
		return fabricUser;
	}

	/**
	 * 用给定的名称获取用户
	 * 
	 * @param name
	 *            名称
	 * @param org
	 *            组织
	 * @param mspId
	 *            会员id
	 * @param privateKeyFile
	 * @param certificateFile
	 * 
	 * @return user 用户
	 * 
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchProviderException
	 * @throws InvalidKeySpecException
	 */
	public FabricUser getMember(String name, String org, String mspId, File privateKeyFile, File certificateFile)
			throws IOException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException {
		try {
			// 尝试从缓存中获取User状�??
			FabricUser fabricUser = members.get(FabricUser.toKeyValStoreName(name, org));
			if (null != fabricUser) {
				System.out.println("尝试从缓存中获取User状�?? User = " + fabricUser);
				return fabricUser;
			}
			// 创建User，并尝试从键值存储中恢复它的状�??(如果找到的话)�?
			fabricUser = new FabricUser(name, org, this);
			fabricUser.setMspId(mspId);
			String certificate = new String(IOUtils.toByteArray(new FileInputStream(certificateFile)), "UTF-8");
			PrivateKey privateKey = getPrivateKeyFromBytes(IOUtils.toByteArray(new FileInputStream(privateKeyFile)));
			fabricUser.setEnrollment(new StoreEnrollement(privateKey, certificate));
			fabricUser.saveState();
			return fabricUser;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw e;
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
			throw e;
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
			throw e;
		} catch (ClassCastException e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 通过字节数组信息获取私钥
	 * 
	 * @param data
	 *            字节数组
	 * 
	 * @return 私钥
	 * 
	 * @throws IOException
	 * @throws NoSuchProviderException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	private PrivateKey getPrivateKeyFromBytes(byte[] data) throws IOException, NoSuchProviderException, NoSuchAlgorithmException, InvalidKeySpecException {
		final Reader pemReader = new StringReader(new String(data));
		final PrivateKeyInfo pemPair;
		try (PEMParser pemParser = new PEMParser(pemReader)) {
			pemPair = (PrivateKeyInfo) pemParser.readObject();
		}
		PrivateKey privateKey = new JcaPEMKeyConverter().setProvider(BouncyCastleProvider.PROVIDER_NAME).getPrivateKey(pemPair);
		return privateKey;
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
	 *
	 */
	static final class StoreEnrollement implements Enrollment, Serializable {

		private static final long serialVersionUID = 6965341351799577442L;

		/** 私钥 */
		private final PrivateKey privateKey;
		/** 授权证书 */
		private final String certificate;

		StoreEnrollement(PrivateKey privateKey, String certificate) {
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
