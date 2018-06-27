package cn.aberic.fabric.service.impl;

import cn.aberic.fabric.base.BaseService;
import cn.aberic.fabric.dao.Chaincode;
import cn.aberic.fabric.dao.mapper.*;
import cn.aberic.fabric.sdk.FabricManager;
import cn.aberic.fabric.service.ChaincodeService;
import cn.aberic.fabric.utils.DateUtil;
import cn.aberic.fabric.utils.FabricHelper;
import cn.aberic.fabric.utils.FileUtil;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

@Service("chaincodeService")
public class ChaincodeServiceImpl implements ChaincodeService, BaseService {

    @Resource
    private OrgMapper orgMapper;
    @Resource
    private OrdererMapper ordererMapper;
    @Resource
    private PeerMapper peerMapper;
    @Resource
    private ChannelMapper channelMapper;
    @Resource
    private ChaincodeMapper chaincodeMapper;
    @Resource
    private Environment env;

    @Override
    public int add(Chaincode chaincode) {
        if (StringUtils.isEmpty(chaincode.getName()) ||
                StringUtils.isEmpty(chaincode.getPath()) ||
                StringUtils.isEmpty(chaincode.getVersion()) ||
                chaincode.getProposalWaitTime() == 0 ||
                chaincode.getInvokeWaitTime() == 0) {
            return 0;
        }
        chaincode.setDate(DateUtil.getCurrent("yyyy年MM月dd日"));
        return chaincodeMapper.add(chaincode);
    }

    @Override
    public String install(Chaincode chaincode, ByteBuffer sourceBuff, String sourceFileName) {
//        if (null == sourceBuff) {
//            return responseFail("install error, source mush be uploaded");
//        }
//        String chaincodeSource = String.format("%s/%s/%s/%s/%s/chaincode", env.getProperty("config.dir"),
//                chaincode.getLeagueName(),
//                chaincode.getOrgName(),
//                chaincode.getPeerName(),
//                chaincode.getChannelName());
//        String chaincodePath = sourceFileName.split("\\.")[0];
//        chaincode.setSource(chaincodeSource);
//        chaincode.setPath(chaincodePath);
//        chaincode.setPolicy(chaincodeSource + File.separator + chaincodePath + File.separator + "policy.yaml");
//        chaincode.setDate(DateUtil.getCurrent("yyyy年MM月dd日"));
//        FileUtil.save(sourceBuff, sourceFileName, chaincodeSource);
//        chaincodeMapper.add(chaincode);
//        chaincode.setId(chaincodeMapper.getByName(chaincode.getName()).getId());
//        return chainCode(chaincode.getId(), orgMapper, channelMapper, chaincodeMapper, ordererMapper, peerMapper, ChainCodeIntent.INSTALL, new String[]{});
        return "";
    }

    @Override
    public String instantiate(Chaincode chaincodeInfo, List<String> strArray) {
        int size = strArray.size();
        String[] args = new String[size];
        for (int i = 0; i < size; i++) {
            args[i] = strArray.get(i);
        }
        return chainCode(chaincodeInfo.getId(), orgMapper, channelMapper, chaincodeMapper, ordererMapper, peerMapper, ChainCodeIntent.INSTANTIATE, args);
    }

    @Override
    public int update(Chaincode chaincodeInfo) {
        FabricHelper.obtain().removeManager(chaincodeInfo.getId());
        return chaincodeMapper.update(chaincodeInfo);
    }

    @Override
    public List<Chaincode> listAll() {
        return chaincodeMapper.listAll();
    }

    @Override
    public List<Chaincode> listById(int id) {
        return chaincodeMapper.list(id);
    }

    @Override
    public Chaincode get(int id) {
        return chaincodeMapper.get(id);
    }

    @Override
    public int countById(int id) {
        return chaincodeMapper.count(id);
    }

    @Override
    public int count() {
        return chaincodeMapper.countAll();
    }

    enum ChainCodeIntent {
        INSTALL, INSTANTIATE
    }

    private String chainCode(int chaincodeId, OrgMapper orgMapper, ChannelMapper channelMapper, ChaincodeMapper chainCodeMapper,
                             OrdererMapper ordererMapper, PeerMapper peerMapper, ChainCodeIntent intent, String[] args) {
        Map<String, String> resultMap = null;
        try {
            FabricManager manager = FabricHelper.obtain().get(orgMapper, channelMapper, chainCodeMapper, ordererMapper, peerMapper,
                    chaincodeId);
            switch (intent) {
                case INSTALL:
                    resultMap = manager.install();
                    break;
                case INSTANTIATE:
                    resultMap = manager.instantiate(args);
                    break;
            }
            if (resultMap.get("code").equals("error")) {
                return responseFail(resultMap.get("data"));
            } else {
                return responseSuccess(resultMap.get("data"), resultMap.get("txid"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return responseFail(String.format("Request failed： %s", e.getMessage()));
        }
    }
}
