package cn.aberic.fabric.service;

import cn.aberic.fabric.base.BaseService;
import cn.aberic.fabric.mapper.*;
import cn.aberic.fabric.sdk.FabricManager;
import cn.aberic.fabric.utils.FabricHelper;
import cn.aberic.fabric.utils.FileUtil;
import cn.aberic.thrift.chaincode.ChaincodeInfo;
import cn.aberic.thrift.chaincode.ChaincodeService;
import cn.aberic.thrift.utils.DateUtil;
import org.apache.thrift.TException;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

@Service("chaincodeService")
public class ChaincodeServiceImpl implements ChaincodeService.Iface, BaseService {

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
    public int add(ChaincodeInfo chaincodeInfo) throws TException {
        if (StringUtils.isEmpty(chaincodeInfo.getName()) ||
                StringUtils.isEmpty(chaincodeInfo.getPath()) ||
                StringUtils.isEmpty(chaincodeInfo.getVersion()) ||
                chaincodeInfo.getProposalWaitTime() == 0 ||
                chaincodeInfo.getInvokeWaitTime() == 0) {
            return 0;
        }
        chaincodeInfo.setDate(DateUtil.getCurrent("yyyy年MM月dd日"));
        return chaincodeMapper.add(chaincodeInfo);
    }

    @Override
    public String install(ChaincodeInfo chaincodeInfo, ByteBuffer sourceBuff, String sourceFileName) throws TException {
        if (null == sourceBuff) {
            return responseFail("install error, source mush be uploaded");
        }
        String chaincodeSource = String.format("%s/%s/%s/%s/%s/chaincode", env.getProperty("config.dir"),
                chaincodeInfo.getLeagueName(),
                chaincodeInfo.getOrgName(),
                chaincodeInfo.getPeerName(),
                chaincodeInfo.getChannelName());
        String chaincodePath = sourceFileName.split("\\.")[0];
        chaincodeInfo.setSource(chaincodeSource);
        chaincodeInfo.setPath(chaincodePath);
        chaincodeInfo.setPolicy(chaincodeSource + File.separator + chaincodePath + File.separator + "policy.yaml");
        chaincodeInfo.setDate(DateUtil.getCurrent("yyyy年MM月dd日"));
        FileUtil.save(sourceBuff, sourceFileName, chaincodeSource);
        chaincodeMapper.add(chaincodeInfo);
        chaincodeInfo.setId(chaincodeMapper.getByName(chaincodeInfo.getName()).getId());
        return chainCode(chaincodeInfo.getId(), orgMapper, channelMapper, chaincodeMapper, ordererMapper, peerMapper, ChainCodeIntent.INSTALL, new String[]{});
    }

    @Override
    public String instantiate(ChaincodeInfo chaincodeInfo, List<String> strArray) throws TException {
        int size = strArray.size();
        String[] args = new String[size];
        for (int i = 0; i < size; i++) {
            args[i] = strArray.get(i);
        }
        return chainCode(chaincodeInfo.getId(), orgMapper, channelMapper, chaincodeMapper, ordererMapper, peerMapper, ChainCodeIntent.INSTANTIATE, args);
    }

    @Override
    public int update(ChaincodeInfo chaincodeInfo) throws TException {
        FabricHelper.obtain().removeManager(chaincodeInfo.getId());
        return chaincodeMapper.update(chaincodeInfo);
    }

    @Override
    public List<ChaincodeInfo> listAll() throws TException {
        return chaincodeMapper.listAll();
    }

    @Override
    public List<ChaincodeInfo> listById(int id) throws TException {
        return chaincodeMapper.list(id);
    }

    @Override
    public ChaincodeInfo get(int id) throws TException {
        return chaincodeMapper.get(id);
    }

    @Override
    public int countById(int id) throws TException {
        return chaincodeMapper.count(id);
    }

    @Override
    public int count() throws TException {
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
