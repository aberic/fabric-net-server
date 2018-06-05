package cn.aberic.simple.module.manager;

import cn.aberic.simple.base.BaseManager;
import org.hyperledger.fabric.sdk.aberic.FabricManager;
import org.hyperledger.fabric.sdk.aberic.OrgManager;

/**
 * 描述：
 *
 * @author : Aberic 【2018/6/4 10:46】
 */
public class SimpleManager extends BaseManager {

    private static SimpleManager instance;

    private FabricManager fabricManager;

    public static SimpleManager obtain() throws Exception {
        if (null == instance) {
            synchronized (SimpleManager.class) {
                if (null == instance) {
                    instance = new SimpleManager();
                }
            }
        }
        return instance;
    }

    private SimpleManager() throws Exception {
        fabricManager = obtainFabricManager();
    }

    public FabricManager getFabricManager() {
        return fabricManager;
    }

    private FabricManager obtainFabricManager() throws Exception {
        OrgManager orgManager = new OrgManager();
        orgManager
                .init("Org1", true, false)
                .setUser("Admin", getCryptoConfigPath("aberic"), getChannleArtifactsPath("aberic"))
//                .setUser("haha", "mAtBqOymDtBI", "org1.department1", new HashSet<>(Arrays.asList("hf.Revoker", "hf.GenCRL", "admin")), getCryptoConfigPath("aberic"))
                .setCA("ca", "http://118.89.243.236:7054")
                .setPeers("Org1MSP", "org1.example.com")
                .addPeer("peer0.org1.example.com", "peer0.org1.example.com", "grpc://118.89.243.236:7051", "grpc://118.89.243.236:7053", true)
                .setOrderers("example.com")
                .addOrderer("orderer.example.com", "grpc://118.89.243.236:7050")
                .setChannel("mychannel")
                .setChainCode("test2cc", "/code", "chaincode/chaincode_example02", "1.2", 90000, 120)
                .setBlockListener(map -> {
                    logger.debug(map.get("code"));
                    logger.debug(map.get("data"));
                })
                .add();
        return orgManager.use("Org1");
    }

}
