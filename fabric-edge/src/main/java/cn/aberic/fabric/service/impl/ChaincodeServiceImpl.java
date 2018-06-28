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
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
        if (verify(chaincode) || null != chaincodeMapper.check(chaincode)) {
            return 0;
        }
        chaincode.setDate(DateUtil.getCurrent("yyyy年MM月dd日"));
        return chaincodeMapper.add(chaincode);
    }

    @Override
    public String install(Chaincode chaincode, MultipartFile file) {
        if (!verify(chaincode) || null == file || null != chaincodeMapper.check(chaincode)) {
            return responseFail("install error, param has none value and source mush be uploaded or had the same chaincode");
        }
        String chaincodeSource = String.format("%s%s%s%s%s%s%s%s%s%schaincode",
                env.getProperty("config.dir"),
                File.separator,
                chaincode.getLeagueName(),
                File.separator,
                chaincode.getOrgName(),
                File.separator,
                chaincode.getPeerName(),
                File.separator,
                chaincode.getChannelName(),
                File.separator);
        String chaincodePath = Objects.requireNonNull(file.getOriginalFilename()).split("\\.")[0];
        String childrenPath = String.format("%s%ssrc%s%s", chaincodeSource, File.separator, File.separator, Objects.requireNonNull(file.getOriginalFilename()).split("\\.")[0]);
        chaincode.setSource(chaincodeSource);
        chaincode.setPath(chaincodePath);
        chaincode.setPolicy(String.format("%s%spolicy.yaml", childrenPath, File.separator));
        chaincode.setDate(DateUtil.getCurrent("yyyy年MM月dd日"));
        try {
            FileUtil.unZipAndSave(file, String.format("%s%ssrc", chaincodeSource, File.separator), childrenPath);
        } catch (IOException e) {
            e.printStackTrace();
            return responseFail("source unzip fail");
        }
        chaincodeMapper.add(chaincode);
        chaincode.setId(chaincodeMapper.check(chaincode).getId());
        return chainCode(chaincode.getId(), orgMapper, channelMapper, chaincodeMapper, ordererMapper, peerMapper, ChainCodeIntent.INSTALL, new String[]{});
    }

    @Override
    public String instantiate(Chaincode chaincode, List<String> strArray) {
        int size = strArray.size();
        String[] args = new String[size];
        for (int i = 0; i < size; i++) {
            args[i] = strArray.get(i);
        }
        return chainCode(chaincode.getId(), orgMapper, channelMapper, chaincodeMapper, ordererMapper, peerMapper, ChainCodeIntent.INSTANTIATE, args);
    }

    @Override
    public String upgrade(Chaincode chaincode, List<String> strArray) {
        int size = strArray.size();
        String[] args = new String[size];
        for (int i = 0; i < size; i++) {
            args[i] = strArray.get(i);
        }
        return chainCode(chaincode.getId(), orgMapper, channelMapper, chaincodeMapper, ordererMapper, peerMapper, ChainCodeIntent.UPGRADE, args);
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
        INSTALL, INSTANTIATE, UPGRADE
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
                case UPGRADE:
                    resultMap = manager.upgrade(args);
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

    private boolean verify(Chaincode chaincode) {
        return StringUtils.isEmpty(chaincode.getName()) ||
                StringUtils.isEmpty(chaincode.getPath()) ||
                StringUtils.isEmpty(chaincode.getVersion()) ||
                chaincode.getProposalWaitTime() == 0 ||
                chaincode.getInvokeWaitTime() == 0;
    }
}
