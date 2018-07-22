package org.hyperledger.fabric.sdk.aberic.bean;

/**
 * Fabric创建的chaincode信息，涵盖所属channel等信息
 * 
 * @author 杨毅
 *
 * @date 2017年10月18日 - 下午2:07:42
 * @email abericyang@gmail.com
 */
public class Chaincode {

	/** 当前将要访问的智能合约所属频道名称 */
	private String channelName; // ffetest
	/** 智能合约名称 */
	private String chaincodeName; // ffetestcc
	/** 智能合约安装路径 */
	private String chaincodePath; // github.com/hyperledger/fabric/xxx/chaincode/go/example/test
	/** 智能合约版本号 */
	private String chaincodeVersion; // 1.0
	/** 临时变量来控制等待部署和调用的时间，以完成在发出之前的事件。当SDK能够接收来自于此的事件时，这个问题就会被删除 */
	private int transactionWaitTime = 100000;
	/** 临时变量来控制等待部署和调用的时间，以完成在发出之前的事件。当SDK能够接收来自于此的事件时，这个问题就会被删除 */
	private int deployWatiTime = 120000;

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getChaincodeName() {
		return chaincodeName;
	}

	public void setChaincodeName(String chaincodeName) {
		this.chaincodeName = chaincodeName;
	}

	public String getChaincodePath() {
		return chaincodePath;
	}

	public void setChaincodePath(String chaincodePath) {
		this.chaincodePath = chaincodePath;
	}

	public String getChaincodeVersion() {
		return chaincodeVersion;
	}

	public void setChaincodeVersion(String chaincodeVersion) {
		this.chaincodeVersion = chaincodeVersion;
	}

	public int getTransactionWaitTime() {
		return transactionWaitTime;
	}

	public void setTransactionWaitTime(int invokeWatiTime) {
		this.transactionWaitTime = invokeWatiTime;
	}

	public int getDeployWaitTime() {
		return deployWatiTime;
	}

	public void setDeployWaitTime(int deployWaitTime) {
		this.deployWatiTime = deployWaitTime;
	}

}
