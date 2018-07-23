package org.hyperledger.fabric.sdk.aberic.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Fabric创建的peer信息，包含有cli、org、ca、couchdb等节点服务器关联启动服务信息集合
 * 
 * @author 杨毅
 *
 * @date 2017年10月18日 - 下午1:49:03
 * @email abericyang@gmail.com
 */
public class Peers {

	/** 当前指定的组织名称 */
	private String orgName; // Org1
	/** 当前指定的组织名称 */
	private String orgMSPID; // Org1MSP
	/** 当前指定的组织所在根域名 */
	private String orgDomainName; // org1.example.com
	/** orderer 排序服务器集合 */
	private List<Peer> peers;

	public Peers() {
		peers = new ArrayList<>();
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getOrgMSPID() {
		return orgMSPID;
	}

	public void setOrgMSPID(String orgMSPID) {
		this.orgMSPID = orgMSPID;
	}

	public String getOrgDomainName() {
		return orgDomainName;
	}

	public void setOrgDomainName(String orgDomainName) {
		this.orgDomainName = orgDomainName;
	}

	/** 新增排序服务器 */
	public void addPeer(String peerName, String peerEventHubName, String peerLocation, String peerEventHubLocation, String caLocation) {
		peers.add(new Peer(peerName, peerEventHubName, peerLocation, peerEventHubLocation, caLocation));
	}

	/** 获取排序服务器集合 */
	public List<Peer> get() {
		return peers;
	}

	/**
	 * 节点服务器对象
	 * 
	 * @author 杨毅
	 *
	 * @date 2017年11月11日 - 下午6:56:14
	 * @email abericyang@gmail.com
	 */
	public class Peer {

		/** 当前指定的组织节点域名 */
		private String peerName; // peer0.org1.example.com
		/** 当前指定的组织节点事件域名 */
		private String peerEventHubName; // peer0.org1.example.com
		/** 当前指定的组织节点访问地址 */
		private String peerLocation; // grpc://110.131.116.21:7051
		/** 当前指定的组织节点事件监听访问地址 */
		private String peerEventHubLocation; // grpc://110.131.116.21:7053
		/** 当前指定的组织节点ca访问地址 */
		private String caLocation; // http://110.131.116.21:7054
		/** 当前peer是否增加Event事件处理 */
		private boolean addEventHub = false;

		public Peer(String peerName, String peerEventHubName, String peerLocation, String peerEventHubLocation, String caLocation) {
			this.peerName = peerName;
			this.peerEventHubName = peerEventHubName;
			this.peerLocation = peerLocation;
			this.peerEventHubLocation = peerEventHubLocation;
			this.caLocation = caLocation;
		}

		public Peer(String peerName, String peerEventHubName, String peerLocation, String peerEventHubLocation, String caLocation, boolean isEventListener) {
			this.peerName = peerName;
			this.peerEventHubName = peerEventHubName;
			this.peerLocation = peerLocation;
			this.peerEventHubLocation = peerEventHubLocation;
			this.caLocation = caLocation;
			this.addEventHub = isEventListener;
		}

		public String getPeerName() {
			return peerName;
		}

		public void setPeerName(String peerName) {
			this.peerName = peerName;
		}

		public String getPeerEventHubName() {
			return peerEventHubName;
		}

		public void setPeerEventHubName(String peerEventHubName) {
			this.peerEventHubName = peerEventHubName;
		}

		public String getPeerLocation() {
			return peerLocation;
		}

		public void setPeerLocation(String peerLocation) {
			this.peerLocation = peerLocation;
		}

		public String getPeerEventHubLocation() {
			return peerEventHubLocation;
		}

		public void setPeerEventHubLocation(String eventHubLocation) {
			this.peerEventHubLocation = eventHubLocation;
		}

		public String getCaLocation() {
			return caLocation;
		}

		public void setCaLocation(String caLocation) {
			this.caLocation = caLocation;
		}

		public boolean isAddEventHub() {
			return addEventHub;
		}

		public void addEventHub(boolean addEventHub) {
			this.addEventHub = addEventHub;
		}

	}

}
