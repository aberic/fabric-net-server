package org.hyperledger.fabric.sdk.aberic;

/**
 * 描述：中继单节点对象
 *
 * @author : Aberic 【2018/5/4 15:29】
 */
class IntermediatePeer {


    /** 当前指定的组织节点域名 */
    private String peerName; // peer0.org1.example.com
    /** 当前指定的组织节点事件域名 */
    private String peerEventHubName; // peer0.org1.example.com
    /** 当前指定的组织节点访问地址 */
    private String peerLocation; // grpc://110.131.116.21:7051
    /** 当前指定的组织节点事件监听访问地址 */
    private String peerEventHubLocation; // grpc://110.131.116.21:7053
    /** 当前peer是否增加Event事件处理 */
    private boolean addEventHub;

    /**
     * 初始化中继Peer对象
     *
     * @param peerName             当前指定的组织节点域名
     * @param peerEventHubName     当前指定的组织节点事件域名
     * @param peerLocation         当前指定的组织节点访问地址
     * @param peerEventHubLocation 当前指定的组织节点事件监听访问地址
     * @param isEventListener      当前peer是否增加Event事件处理
     */
    IntermediatePeer(String peerName, String peerEventHubName, String peerLocation, String peerEventHubLocation, boolean isEventListener) {
        this.peerName = peerName;
        this.peerEventHubName = peerEventHubName;
        this.peerLocation = peerLocation;
        this.peerEventHubLocation = peerEventHubLocation;
        this.addEventHub = isEventListener;
    }

    /**
     * 设置组织节点访问地址（grpc://110.131.116.21:7051）
     *
     * @param peerLocation 组织节点访问地址
     */
    void setPeerLocation(String peerLocation) {
        this.peerLocation = peerLocation;
    }

    /**
     * 设置组织节点事件监听访问地址（grpc://110.131.116.21:7053）
     *
     * @param peerEventHubLocation 组织节点事件监听访问地址
     */
    void setPeerEventHubLocation(String peerEventHubLocation) {
        this.peerEventHubLocation = peerEventHubLocation;
    }

    /**
     * 获取组织节点域名（peer0.org1.example.com）
     *
     * @return 组织节点域名
     */
    String getPeerName() {
        return peerName;
    }

    /**
     * 获取组织节点事件域名（peer0.org1.example.com）
     *
     * @return 组织节点事件域名（peer0.org1.example.com）
     */
    String getPeerEventHubName() {
        return peerEventHubName;
    }

    /**
     * 获取组织节点访问地址（grpc://110.131.116.21:7051）
     *
     * @return 组织节点访问地址（grpc://110.131.116.21:7051）
     */
    String getPeerLocation() {
        return peerLocation;
    }

    /**
     * 获取组织节点事件监听访问地址（grpc://110.131.116.21:7053）
     *
     * @return 组织节点事件监听访问地址（grpc://110.131.116.21:7053）
     */
    String getPeerEventHubLocation() {
        return peerEventHubLocation;
    }

    /**
     * 当前peer是否增加Event事件处理
     *
     * @return 与否
     */
    boolean isAddEventHub() {
        return addEventHub;
    }

}
