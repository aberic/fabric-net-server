package org.hyperledger.fabric.sdk.aberic;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.hyperledger.fabric.sdk.Peer;
import org.hyperledger.fabric.sdk.User;
import org.hyperledger.fabric.sdk.aberic.bean.Orderers;
import org.hyperledger.fabric.sdk.aberic.bean.Peers;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 联盟组织对象
 * 
 * @author 杨毅
 *
 * @date 2017年9月7日 - 下午4:35:40
 * @email abericyang@gmail.com
 */
class FabricOrg {

	private static Logger log = LoggerFactory.getLogger(FabricOrg.class);

	/** 名称 */
	private String name;
	/** 会员id */
	private String mspid;
	/** ca 客户端 */
	private HFCAClient caClient;

	/** 用户集合 */
	Map<String, User> userMap = new HashMap<>();
	/** 本地节点集合 */
	Map<String, String> peerLocations = new HashMap<>();
	/** 本地排序服务集合 */
	Map<String, String> ordererLocations = new HashMap<>();
	/** 本地事件集合 */
	Map<String, String> eventHubLocations = new HashMap<>();
	/** 节点集合 */
	Set<Peer> peers = new HashSet<>();
	/** 联盟管理员用户 */
	private FabricUser admin;
	/** 本地 ca */
	private String caLocation;
	/** ca 配置 */
	private Properties caProperties = null;

	/** 联盟单节点管理员用户 */
	private FabricUser peerAdmin;

	/** 域名名称 */
	private String domainName;

	public FabricOrg(String username, Peers peers, Orderers orderers, FabricStore fabricStore, String cryptoConfigPath, boolean openCATLS)
			throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException, IOException {
		this.name = peers.getOrgName();
		this.mspid = peers.getOrgMSPID();
		for (int i = 0; i < peers.get().size(); i++) {
			addPeerLocation(peers.get().get(i).getPeerName(), peers.get().get(i).getPeerLocation());
			addEventHubLocation(peers.get().get(i).getPeerEventHubName(), peers.get().get(i).getPeerEventHubLocation());
			setCALocation(peers.get().get(i).getCaLocation());
			if (openCATLS) {
				File caCert = Paths.get(cryptoConfigPath, "/peerOrganizations/", peers.getOrgDomainName(), String.format("/ca/ca.%s-cert.pem", peers.getOrgDomainName())).toFile();
				if (!caCert.exists() || !caCert.isFile()) {
					throw new RuntimeException("TEST is missing cert file " + caCert.getAbsolutePath());
				}
				Properties properties = new Properties();
				properties.setProperty("pemFile", caCert.getAbsolutePath());

				// properties.setProperty("allowAllHostNames", "true"); // 仅用于测试环境
				setCAProperties(properties);
			}
		}
		for (int i = 0; i < orderers.get().size(); i++) {
			addOrdererLocation(orderers.get().get(i).getOrdererName(), orderers.get().get(i).getOrdererLocation());
		}
		setDomainName(peers.getOrgDomainName()); // domainName=tk.anti-moth.com

		// Set up HFCA for Org1
		setCAClient(HFCAClient.createNewInstance(getCALocation(), getCAProperties()));

		// setAdmin(fabricStore.getMember("admin", peers.getOrgName())); // 设置该组织的管理员

		File skFile = Paths.get(cryptoConfigPath, "/peerOrganizations/", peers.getOrgDomainName(), String.format("/users/%s@%s/msp/keystore", username, peers.getOrgDomainName())).toFile();
		File certificateFile = Paths.get(cryptoConfigPath, "/peerOrganizations/", peers.getOrgDomainName(),
				String.format("/users/%s@%s/msp/signcerts/%s@%s-cert.pem", username, peers.getOrgDomainName(), username, peers.getOrgDomainName())).toFile();
		log.debug("skFile = " + skFile.getAbsolutePath());
		log.debug("certificateFile = " + certificateFile.getAbsolutePath());
		setPeerAdmin(fabricStore.getMember(peers.getOrgName() + username, peers.getOrgName(), peers.getOrgMSPID(), findFileSk(skFile), certificateFile)); // 一个特殊的用户，可以创建通道，连接对等点，并安装链码
	}

	public String getName() {
		return name;
	}

	/**
	 * 获取联盟管理员用户
	 * 
	 * @return 联盟管理员用户
	 */
	public FabricUser getAdmin() {
		return admin;
	}

	/**
	 * 设置联盟管理员用户
	 * 
	 * @param admin
	 *            联盟管理员用户
	 */
	public void setAdmin(FabricUser admin) {
		this.admin = admin;
	}

	/**
	 * 获取会员id
	 * 
	 * @return 会员id
	 */
	public String getMSPID() {
		return mspid;
	}

	/**
	 * 设置本地ca
	 * 
	 * @param caLocation
	 *            本地ca
	 */
	public void setCALocation(String caLocation) {
		this.caLocation = caLocation;
	}

	/**
	 * 获取本地ca
	 * 
	 * @return 本地ca
	 */
	public String getCALocation() {
		return this.caLocation;
	}

	/**
	 * 添加本地节点
	 * 
	 * @param name
	 *            节点key
	 * @param location
	 *            节点
	 */
	public void addPeerLocation(String name, String location) {
		peerLocations.put(name, location);
	}

	/**
	 * 添加本地组织
	 * 
	 * @param name
	 *            组织key
	 * @param location
	 *            组织
	 */
	public void addOrdererLocation(String name, String location) {
		ordererLocations.put(name, location);
	}

	/**
	 * 添加本地事件
	 * 
	 * @param name
	 *            事件key
	 * @param location
	 *            事件
	 */
	public void addEventHubLocation(String name, String location) {
		eventHubLocations.put(name, location);
	}

	/**
	 * 获取本地节点
	 * 
	 * @param name
	 *            节点key
	 * @return 节点
	 */
	public String getPeerLocation(String name) {
		return peerLocations.get(name);
	}

	/**
	 * 获取本地组织
	 * 
	 * @param name
	 *            组织key
	 * @return 组织
	 */
	public String getOrdererLocation(String name) {
		return ordererLocations.get(name);
	}

	/**
	 * 获取本地事件
	 * 
	 * @param name
	 *            事件key
	 * @return 事件
	 */
	public String getEventHubLocation(String name) {
		return eventHubLocations.get(name);
	}

	/**
	 * 获取一个不可修改的本地节点key集合
	 * 
	 * @return 节点key集合
	 */
	public Set<String> getPeerNames() {
		return Collections.unmodifiableSet(peerLocations.keySet());
	}

	/**
	 * 获取一个不可修改的本地节点集合
	 * 
	 * @return 节点集合
	 */
	public Set<Peer> getPeers() {
		return Collections.unmodifiableSet(peers);
	}

	/**
	 * 获取一个不可修改的本地组织key集合
	 * 
	 * @return 组织key集合
	 */
	public Set<String> getOrdererNames() {
		return Collections.unmodifiableSet(ordererLocations.keySet());
	}

	/**
	 * 获取一个不可修改的本地组织集合
	 * 
	 * @return 组织集合
	 */
	public Collection<String> getOrdererLocations() {
		return Collections.unmodifiableCollection(ordererLocations.values());
	}

	/**
	 * 获取一个不可修改的本地事件key集合
	 * 
	 * @return 事件key集合
	 */
	public Set<String> getEventHubNames() {
		return Collections.unmodifiableSet(eventHubLocations.keySet());
	}

	/**
	 * 获取一个不可修改的本地事件集合
	 * 
	 * @return 事件集合
	 */
	public Collection<String> getEventHubLocations() {
		return Collections.unmodifiableCollection(eventHubLocations.values());
	}

	/**
	 * 设置 ca 客户端
	 * 
	 * @param caClient
	 *            ca 客户端
	 */
	public void setCAClient(HFCAClient caClient) {
		this.caClient = caClient;
	}

	/**
	 * 获取 ca 客户端
	 * 
	 * @return ca 客户端
	 */
	public HFCAClient getCAClient() {
		return caClient;
	}

	/**
	 * 向用户集合中添加用户
	 * 
	 * @param user
	 *            用户
	 */
	public void addUser(FabricUser user) {
		userMap.put(user.getName(), user);
	}

	/**
	 * 从用户集合根据名称获取用户
	 * 
	 * @param name
	 *            名称
	 * @return 用户
	 */
	public User getUser(String name) {
		return userMap.get(name);
	}

	/**
	 * 向节点集合中添加节点
	 * 
	 * @param peer
	 *            节点
	 */
	public void addPeer(Peer peer) {
		peers.add(peer);
	}

	/**
	 * 设置 ca 配置
	 * 
	 * @param caProperties
	 *            ca 配置
	 */
	public void setCAProperties(Properties caProperties) {
		this.caProperties = caProperties;
	}

	/**
	 * 获取 ca 配置
	 * 
	 * @return ca 配置
	 */
	public Properties getCAProperties() {
		return caProperties;
	}

	/**
	 * 设置联盟单节点管理员用户
	 * 
	 * @param peerAdmin
	 *            联盟单节点管理员用户
	 */
	public void setPeerAdmin(FabricUser peerAdmin) {
		this.peerAdmin = peerAdmin;
	}

	/**
	 * 获取联盟单节点管理员用户
	 * 
	 * @return 联盟单节点管理员用户
	 */
	public FabricUser getPeerAdmin() {
		return peerAdmin;
	}

	/**
	 * 设置域名名称
	 * 
	 * @param doainName
	 *            域名名称
	 */
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	/**
	 * 获取域名名称
	 * 
	 * @return 域名名称
	 */
	public String getDomainName() {
		return domainName;
	}

	/**
	 * 从指定路径中获取后缀为 _sk 的文件，且该路径下有且仅有该文件
	 * 
	 * @param directorys
	 *            指定路径
	 * @return File
	 */
	private File findFileSk(File directory) {
		File[] matches = directory.listFiles((dir, name) -> name.endsWith("_sk"));
		if (null == matches) {
			throw new RuntimeException(String.format("Matches returned null does %s directory exist?", directory.getAbsoluteFile().getName()));
		}
		if (matches.length != 1) {
			throw new RuntimeException(String.format("Expected in %s only 1 sk file but found %d", directory.getAbsoluteFile().getName(), matches.length));
		}
		return matches[0];
	}

}
