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
//        orgManager
//                .init("Org1")
//                .setUser("Admin", getCryptoConfigPath("aberic"), getChannleArtifactsPath("aberic"))
////                .setUser("haha", "mAtBqOymDtBI", "org1.department1", new HashSet<>(Arrays.asList("hf.Revoker", "hf.GenCRL", "admin")), getCryptoConfigPath("aberic"))
//                .setCALocation("http://123.207.138.131:7054")
//                .setPeers("Org1MSP", "org1.example.com")
//                .addPeer("peer0.org1.example.com", "peer0.org1.example.com", "grpc://123.207.138.131:7051", "grpc://123.207.138.131:7053", false)
//                .setOrderers("example.com")
//                .addOrderer("orderer.example.com", "grpc://123.207.138.131:7050")
//                .setChainCode("mychannel", "mycc", "github.com/hyperledger/fabric/aberic/chaincode/go/chaincode_example02", "1.3")
//                .setBlockListener(null)
//                .openTLS(false)
//                .openCATLS(false)
//                .setWaitTime(100000, 120000)-
//                .add();
//        orgManager
//                .init("Org1")
//                .setUser("Admin", getCryptoConfigPath("aberic"), getChannleArtifactsPath("aberic"))
////                .setUser("haha", "mAtBqOymDtBI", "org1.department1", new HashSet<>(Arrays.asList("hf.Revoker", "hf.GenCRL", "admin")), getCryptoConfigPath("aberic"))
//                .setCALocation("http://118.89.243.236:7054")
//                .setPeers("Org1MSP", "org1.example.com")
//                .addPeer("peer0.org1.example.com", "peer0.org1.example.com", "grpc://118.89.243.236:7051", "grpc://118.89.243.236:7053", true)
//                .setOrderers("example.com")
//                .addOrderer("orderer.example.com", "grpc://118.89.243.236:7050")
//                .setChannel("mychannel", 100000, 120000)
//                .setChainCode("mycc", "/opt/gopath", "github.com/hyperledger/fabric/aberic/chaincode/go/chaincode_example02", "1.4")
//                .openTLS(true)
//                .openCATLS(false)
//                .setBlockListener(event -> {
//                    LogUtil.debug("========================Event事件监听开始========================");
//                    try {
//                        LogUtil.debug("event.getChannelId() = " + event.getChannelId());
//                        LogUtil.debug("event.getEvent().getChaincodeEvent().getPayload().toStringUtf8() = " + event.getEvent().getChaincodeEvent().getPayload().toStringUtf8());
//                        LogUtil.debug("event.getBlock().getData().getDataList().size() = " + event.getBlock().getData().getDataList().size());
//                        ByteString byteString = event.getBlock().getData().getData(0);
//                        String result = byteString.toStringUtf8();
//                        LogUtil.debug("byteString.toStringUtf8() = " + result);
//
//                        String r1[] = result.split("END CERTIFICATE");
//                        String rr = r1[2];
//                        LogUtil.debug("rr = " + rr);
//                    } catch (InvalidProtocolBufferException e) {
//                        // TODO
//                        e.printStackTrace();
//                    }
//                    LogUtil.debug("========================Event事件监听结束========================");
//                })
//                .add();
        orgManager
                .init("Org1")
                .setUser("Admin", getCryptoConfigPath("aberic"), getChannleArtifactsPath("aberic"))
//                .setUser("haha", "mAtBqOymDtBI", "org1.department1", new HashSet<>(Arrays.asList("hf.Revoker", "hf.GenCRL", "admin")), getCryptoConfigPath("aberic"))
                .setCA("ca", "http://118.89.243.236:7054")
                .setPeers("Org1MSP", "org1.example.com")
                .addPeer("peer0.org1.example.com", "peer0.org1.example.com", "grpc://118.89.243.236:7051", "grpc://118.89.243.236:7053", true)
                .setOrderers("example.com")
                .addOrderer("orderer.example.com", "grpc://118.89.243.236:7050")
                .setChannel("mychannel")
                .setChainCode("test2cc", "/code", "chaincode/chaincode_example02", "1.2", 90000, 120)
                .openTLS(true)
                .openCATLS(false)
                .setBlockListener(map -> {
                    logger.debug(map.get("code"));
                    logger.debug(map.get("data"));
                })
                .add();
        logger.debug("openCATLS = true");
        return orgManager.use("Org1");
    }

}
