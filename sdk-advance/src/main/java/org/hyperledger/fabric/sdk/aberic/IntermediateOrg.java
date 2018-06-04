package org.hyperledger.fabric.sdk.aberic;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.util.Asserts;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.User;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.HFCAInfo;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Paths;
import java.util.*;

/**
 * 描述：中继组织对象
 *
 * @author : Aberic 【2018/5/4 15:32】
 */
class IntermediateOrg {

    private static final Log log = LogFactory.getLog(IntermediateOrg.class);

    /** 执行SDK的Fabric用户名 */
    private String username;
    /** 执行SDK的Fabric用户密码 */
    private String password;
    private String affiliation;
    private Set<String> roles;

    /** orderer 排序服务器所在根域名，如：example.com */
    private String ordererDomainName;
    /** orderer 排序服务器集合 */
    private List<IntermediateOrderer> orderers = new LinkedList<>();

    /** 当前指定的组织名称，如：Org1 */
    private String orgName;
    /** 当前指定的组织名称，如：Org1MSP */
    private String orgMSPID;
    /** 当前指定的组织所在根域名，如：org1.example.com */
    private String orgDomainName;
    /** orderer 排序服务器集合 */
    private List<IntermediatePeer> peers = new LinkedList<>();

    /** 是否开启TLS访问 */
    private boolean openTLS;

    /** 频道对象 */
    private IntermediateChannel channel;

    /** 智能合约对象 */
    private IntermediateChaincodeID chaincode;
    /** 事件监听 */
    private BlockListener blockListener;

    /** channel-artifacts所在路径 */
    private String channelArtifactsPath;
    /** crypto-config所在路径 */
    private String cryptoConfigPath;

    /** 当前指定的组织节点ca名称 */
    private String caName;
    /** 当前指定的组织节点ca访问地址（http://110.131.116.21:7054） */
    private String caLocation;
    /** 是否开启CA TLS访问 */
    private boolean openCATLS;
    private HFCAClient caClient;

    private HFClient client;

    private Map<String, User> userMap = new HashMap<>();

    void init(FabricStore fabricStore) throws Exception {
        Properties properties = null;
        if (openCATLS) {
            File caCert = Paths.get(cryptoConfigPath, "/peerOrganizations/", getOrgDomainName(), String.format("/tlsca/tlsca.%s-cert.pem", getOrgDomainName())).toFile();
            if (!caCert.exists() || !caCert.isFile()) {
                throw new RuntimeException("TEST is missing cert file " + caCert.getAbsolutePath());
            }
            properties = new Properties();
            properties.setProperty("pemFile", caCert.getAbsolutePath());
            // properties.setProperty("allowAllHostNames", "true"); // 仅用于测试环境
        }
        // CA客户端
        if (caName != null && !caName.isEmpty()) {
            caClient = HFCAClient.createNewInstance(caName, getCALocation(), properties);
        } else {
            caClient = HFCAClient.createNewInstance(getCALocation(), properties);
        }
        caClient.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
        HFCAInfo info = caClient.info(); // 检查一下我们是否连接
        Asserts.notNull(info, "caClient.info()");
        String infoName = info.getCAName();
        if (infoName != null && !infoName.isEmpty()) {
            if (!caName.equals(infoName)) {
                throw new RuntimeException("ca name is not equals");
            } else {
                log.debug(String.format("ca info check success with ca name: %s", caName));
            }
        }

        if (password != null && !"".equals(password)) {
            IntermediateUser user = fabricStore.getMember(username, orgName);
//        if (!user.isRegistered()) {  // users need to be registered AND enrolled
//            RegistrationRequest rr = new RegistrationRequest(user.getName(), "org1.department1");
//            user.setEnrollmentSecret(caClient.register(rr, peerAdmin));
//        }
            if (!user.isEnrolled()) {
//            user.setEnrollment(caClient.enroll(user.getName(), user.getEnrollmentSecret()));
                user.setEnrollment(caClient.enroll(username, password));
                user.setAffiliation(affiliation);
                user.setRoles(roles);
                user.setMspId(orgMSPID);
            }
            addUser(user);
        }

//        IntermediateUser admin = fabricStore.getMember("admin", orgName);
//        if (!admin.isEnrolled()) {
//            admin.setEnrollment(caClient.enroll(admin.getName(), "adminpw"));
//            admin.setMspId(orgMSPID);
//        }
//        addUser(admin);
//
//        IntermediateUser user1 = fabricStore.getMember(username, orgName);
//        if (!user1.isRegistered()) {
//            RegistrationRequest rr = new RegistrationRequest(user1.getName(), "org1.department1");
//            user1.setEnrollmentSecret(caClient.register(rr, admin));
//        }
//        if (!user1.isEnrolled()) {
//            user1.setEnrollment(caClient.enroll(user1.getName(), user1.getEnrollmentSecret()));
//            user1.setMspId(orgMSPID);
//        }
//        addUser(user1);

        setPeerAdmin(fabricStore);
    }

    private void setPeerAdmin(FabricStore fabricStore) throws IOException {
        File skFile = Paths.get(cryptoConfigPath, "/peerOrganizations/", orgDomainName, String.format("/users/%s@%s/msp/keystore", "Admin", orgDomainName)).toFile();
        File certificateFile = Paths.get(cryptoConfigPath, "/peerOrganizations/", getOrgDomainName(),
                String.format("/users/%s@%s/msp/signcerts/%s@%s-cert.pem", "Admin", orgDomainName, "Admin", orgDomainName)).toFile();
        log.debug("skFile = " + skFile.getAbsolutePath());
        log.debug("certificateFile = " + certificateFile.getAbsolutePath());
        // 一个特殊的用户，可以创建通道，连接对等点，并安装链码
        addUser(fabricStore.getMember("Admin", orgName, orgMSPID, findFileSk(skFile), certificateFile));
    }

    /**
     * 设置CA默认请求用户名或指定的带密码参数的请求用户名
     *
     * @param username 用户名
     */
    void setUsername(String username) {
        this.username = username;
    }

    /**
     * 设置CA请求用户的密码
     *
     * @param password 密码
     */
    void setPassword(String password) {
        this.password = password;
    }

    /**
     * 设置从属联盟信息并将用户状态更新至存储配置对象
     *
     * @param affiliation 从属联盟
     */
    void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    /**
     * 设置规则信息并将用户状态更新至存储配置对象
     *
     * @param roles 规则
     */
    void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    String getUsername() {
        return username;
    }

    void setCaName(String caName) {
        this.caName = caName;
    }

    String getCALocation() {
        return caLocation;
    }

    void setCALocation(String caLocation) {
        this.caLocation = caLocation;
    }

    String getOrdererDomainName() {
        return ordererDomainName;
    }

    void setOrdererDomainName(String ordererDomainName) {
        this.ordererDomainName = ordererDomainName;
    }

    /** 新增排序服务器 */
    void addOrderer(String name, String location) {
        orderers.add(new IntermediateOrderer(name, location));
    }

    /** 获取排序服务器集合 */
    List<IntermediateOrderer> getOrderers() {
        return orderers;
    }

    void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    /**
     * 设置会员id信息并将用户状态更新至存储配置对象
     *
     * @param orgMSPID 会员id
     */
    void setOrgMSPID(String orgMSPID) {
        this.orgMSPID = orgMSPID;
    }

    String getOrgDomainName() {
        return orgDomainName;
    }

    void setOrgDomainName(String orgDomainName) {
        this.orgDomainName = orgDomainName;
    }

    /** 新增节点服务器 */
    void addPeer(String peerName, String peerEventHubName, String peerLocation, String peerEventHubLocation, boolean isEventListener) {
        peers.add(new IntermediatePeer(peerName, peerEventHubName, peerLocation, peerEventHubLocation, isEventListener));
    }

    /** 获取排序服务器集合 */
    List<IntermediatePeer> getPeers() {
        return peers;
    }

    void setChannel(IntermediateChannel channel) {
        this.channel = channel;
    }

    IntermediateChannel getChannel() {
        return channel;
    }

    void setChainCode(IntermediateChaincodeID chaincode) {
        this.chaincode = chaincode;
    }

    IntermediateChaincodeID getChainCode() {
        return chaincode;
    }

    void setChannelArtifactsPath(String channelArtifactsPath) {
        this.channelArtifactsPath = channelArtifactsPath;
    }

    String getChannelArtifactsPath() {
        return channelArtifactsPath;
    }

    void setCryptoConfigPath(String cryptoConfigPath) {
        this.cryptoConfigPath = cryptoConfigPath;
    }

    String getCryptoConfigPath() {
        return cryptoConfigPath;
    }

    void setBlockListener(BlockListener blockListener) {
        this.blockListener = blockListener;
    }

    BlockListener getBlockListener() {
        return blockListener;
    }

    /**
     * 设置是否开启TLS
     *
     * @param openTLS 是否
     */
    void openTLS(boolean openTLS) {
        this.openTLS = openTLS;
    }

    /**
     * 获取是否开启TLS
     *
     * @return 是否
     */
    boolean openTLS() {
        return openTLS;
    }

    /**
     * 设置是否开启CATLS
     *
     * @param openCATLS 是否
     */
    void openCATLS(boolean openCATLS) {
        this.openCATLS = openCATLS;
    }

    /**
     * 获取是否开启CATLS
     *
     * @return 是否
     */
    boolean openCATLS() {
        return openCATLS;
    }

    private void addUser(IntermediateUser user) {
        userMap.put(user.getName(), user);
    }

    User getUser(String name) {
        return userMap.get(name);
    }

    HFCAClient getCaClient() {
        return caClient;
    }

    void setClient(HFClient client) throws CryptoException, InvalidArgumentException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        this.client = client;
        log.debug("Create instance of HFClient");
        client.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
        log.debug("Set Crypto Suite of HFClient");
    }

    HFClient getClient() {
        return client;
    }

    /**
     * 从指定路径中获取后缀为 _sk 的文件，且该路径下有且仅有该文件
     *
     * @param directory 指定路径
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