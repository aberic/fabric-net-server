package org.hyperledger.fabric.sdk.aberic;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;

import org.hyperledger.fabric.sdk.aberic.bean.Chaincode;
import org.hyperledger.fabric.sdk.aberic.bean.Orderers;
import org.hyperledger.fabric.sdk.aberic.bean.Peers;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.TransactionException;

public class Fabric {

	private String username = "";
	private Peers peers;
	private Orderers orderers;
	private String channleArtifactsPath = "";
	private String cryptoConfigPath = "";
	private Chaincode chaincode;
	private boolean openCATLS;
	private boolean registerEvent = false;
	private FabricConfig fabricConfig;

	public Fabric() {
	}

	public Fabric setUser(String username) {
		this.username = username;
		return this;
	}

	public Fabric setChannleArtifactsPath(String channleArtifactsPath) {
		this.channleArtifactsPath = channleArtifactsPath;
		return this;
	}

	public Fabric setCryptoConfigPath(String cryptoConfigPath) {
		this.cryptoConfigPath = cryptoConfigPath;
		return this;
	}

	public Fabric setOrderers(String ordererDomainName) {
		orderers = new Orderers();
		orderers.setOrdererDomainName(ordererDomainName);
		return this;
	}

	public Fabric addOrderer(String name, String location) {
		orderers.addOrderer(name, location);
		return this;
	}

	public Fabric setPeers(String orgName, String orgMSPID, String orgDomainName) {
		peers = new Peers();
		peers.setOrgName(orgName);
		peers.setOrgMSPID(orgMSPID);
		peers.setOrgDomainName(orgDomainName);
		return this;
	}

	public Fabric addPeer(String peerName, String peerEventHubName, String peerLocation, String peerEventHubLocation,
			String caLocation) {
		peers.addPeer(peerName, peerEventHubName, peerLocation, peerEventHubLocation, caLocation);
		return this;
	}

	/**
	 * 获取智能合约
	 * 
	 * @param channelName
	 *            频道名称
	 * @param chaincodeName
	 *            智能合约名称
	 * @param chaincodePath
	 *            智能合约路径
	 * @param chaincodeVersion
	 *            智能合约版本
	 * @return Fabric
	 */
	public Fabric setChaincode(String channelName, String chaincodeName, String chaincodePath,
			String chaincodeVersion) {
		chaincode = new Chaincode();
		chaincode.setChannelName(channelName);
		chaincode.setChaincodeName(chaincodeName);
		chaincode.setChaincodePath(chaincodePath);
		chaincode.setChaincodeVersion(chaincodeVersion);
		return this;
	}

	/**
	 * 设置是否开启CATLS
	 * 
	 * @param openCATLS
	 *            是否
	 */
	public Fabric openTLS(boolean openCATLS) {
		this.openCATLS = openCATLS;
		return this;
	}

	/**
	 * 设置是否监听事件
	 * 
	 * @param registerEvent
	 *            是否
	 */
	public Fabric setRegisterEvent(boolean registerEvent) {
		this.registerEvent = registerEvent;
		return this;
	}

	/**
	 * 
	 * @param transactionWaitTime
	 *            临时变量来控制等待部署和调用的时间，以完成在发出之前的事件。当SDK能够接收来自于此的事件时，这个问题就会被删除
	 * @param deployWaitTime
	 *            临时变量来控制等待部署和调用的时间，以完成在发出之前的事件。当SDK能够接收来自于此的事件时，这个问题就会被删除
	 * @return Fabric
	 */
	public Fabric setWaitTime(int transactionWaitTime, int deployWaitTime) {
		chaincode.setTransactionWaitTime(transactionWaitTime);
		chaincode.setDeployWaitTime(deployWaitTime);
		return this;
	}

	/**
	 * 根据节点作用类型获取节点服务器配置
	 * 
	 * @param type
	 *            服务器作用类型（1、执行；2、查询）
	 * @return 节点服务器配置
	 */
	private FabricConfig getFabricConfig() {
		if (peers == null || peers.get().size() == 0) {
			throw new RuntimeException("peers is null or peers size is 0");
		}
		if (orderers == null || orderers.get().size() == 0) {
			throw new RuntimeException("orderers is null or orderers size is 0");
		}
		if (chaincode == null) {
			throw new RuntimeException("chaincode must be instantiated");
		}
		if (channleArtifactsPath.equals("")) {
			throw new RuntimeException("channleArtifactsPath must be set");
		}
		if (cryptoConfigPath.equals("")) {
			throw new RuntimeException("cryptoConfigPath must be set");
		}
		fabricConfig = new FabricConfig();
		fabricConfig.setOrderers(orderers);
		fabricConfig.setPeers(peers);
		fabricConfig.setChaincode(chaincode);
		fabricConfig.openCATLS(openCATLS);
		fabricConfig.setRegisterEvent(registerEvent);
		fabricConfig.setChannelArtifactsPath(channleArtifactsPath);
		fabricConfig.setCryptoConfigPath(cryptoConfigPath);
		return fabricConfig;
	}

	public ChaincodeManager getChaincodeManager()
			throws CryptoException, InvalidArgumentException, NoSuchAlgorithmException, NoSuchProviderException,
			InvalidKeySpecException, TransactionException, IOException {
		return new ChaincodeManager(!username.equals("") ? username : "Admin", getFabricConfig());
	}

}
