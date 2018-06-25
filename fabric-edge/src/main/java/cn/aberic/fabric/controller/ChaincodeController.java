package cn.aberic.fabric.controller;

import cn.aberic.fabric.bean.Api;
import cn.aberic.fabric.thrift.MultiServiceProvider;
import cn.aberic.thrift.chaincode.ChaincodeInfo;
import cn.aberic.thrift.channel.ChannelInfo;
import cn.aberic.thrift.league.LeagueInfo;
import cn.aberic.thrift.org.OrgInfo;
import cn.aberic.thrift.peer.PeerInfo;
import cn.aberic.thrift.state.StateInfo;
import cn.aberic.thrift.trace.TraceInfo;
import org.apache.thrift.TException;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static cn.aberic.fabric.bean.Api.Intent.INSTANTIATE;
import static cn.aberic.fabric.bean.Api.Intent.INVOKE;

/**
 * 描述：
 *
 * @author : Aberic 【2018/6/4 15:01】
 */
@CrossOrigin
@RestController
@RequestMapping("chaincode")
public class ChaincodeController {

    @Resource
    private MultiServiceProvider multiService;
    @Resource
    private Environment env;

    @PostMapping(value = "submit")
    public ModelAndView submit(@ModelAttribute ChaincodeInfo chaincode,
                               @RequestParam("intent") String intent,
                               @RequestParam(value = "sourceFile", required = false) MultipartFile sourceFile,
                               @RequestParam(value = "policyFile", required = false) MultipartFile policyFile,
                               @RequestParam("id") int id) {
        try {
            switch (intent) {
                case "add":
                    multiService.getChaincodeService().add(chaincode);
                    break;
                case "edit":
                    chaincode.setId(id);
                    multiService.getChaincodeService().update(chaincode);
                    break;
                case "install":
                    ChannelInfo channel = multiService.getChannelService().get(chaincode.getChannelId());
                    PeerInfo peer = multiService.getPeerService().get(channel.getPeerId());
                    OrgInfo org = multiService.getOrgService().get(peer.getId());
                    LeagueInfo league = multiService.getLeagueService().get(org.getLeagueId());
                    chaincode.setLeagueName(league.getName());
                    chaincode.setOrgName(org.getName());
                    chaincode.setPeerName(peer.getName());
                    chaincode.setChannelName(channel.getName());
                    try {
                        multiService.getChaincodeService().install(chaincode,
                                ByteBuffer.wrap(sourceFile.getBytes()),
                                ByteBuffer.wrap(policyFile.getBytes()),
                                sourceFile.getOriginalFilename());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        } catch (TException e) {
            e.printStackTrace();
        }
        return list();
    }

    @GetMapping(value = "add")
    public ModelAndView add() {
        ModelAndView modelAndView = new ModelAndView("chaincodeSubmit");
        modelAndView.addObject("intentLarge", "新建合约");
        modelAndView.addObject("intentLittle", "新建");
        modelAndView.addObject("submit", "新增");
        modelAndView.addObject("intent", "add");
        ChaincodeInfo chaincode = new ChaincodeInfo();
        List<ChannelInfo> channels;
        try {
            channels = multiService.getChannelFullList();
        } catch (TException e) {
            channels = new ArrayList<>();
            e.printStackTrace();
        }
        modelAndView.addObject("chaincode", chaincode);
        modelAndView.addObject("channels", channels);
        return modelAndView;
    }

    @GetMapping(value = "install")
    public ModelAndView install() {
        ModelAndView modelAndView = new ModelAndView("chaincodeInstall");
        modelAndView.addObject("intentLarge", "安装合约");
        modelAndView.addObject("intentLittle", "安装");
        modelAndView.addObject("submit", "安装");
        modelAndView.addObject("intent", "install");
        ChaincodeInfo chaincode = new ChaincodeInfo();
        List<ChannelInfo> channels;
        try {
            channels = multiService.getChannelFullList();
        } catch (TException e) {
            channels = new ArrayList<>();
            e.printStackTrace();
        }
        modelAndView.addObject("chaincode", chaincode);
        modelAndView.addObject("channels", channels);
        return modelAndView;
    }

    @PostMapping(value = "instantiate")
    public ModelAndView instantiate(@ModelAttribute Api api, @RequestParam("chaincodeId") int id) {
        try {
            ChaincodeInfo chaincode = multiService.getChaincodeService().get(id);
            ChannelInfo channel = multiService.getChannelService().get(chaincode.getChannelId());
            PeerInfo peer = multiService.getPeerService().get(channel.getPeerId());
            OrgInfo org = multiService.getOrgService().get(peer.getId());
            LeagueInfo league = multiService.getLeagueService().get(org.getLeagueId());
            chaincode.setLeagueName(league.getName());
            chaincode.setOrgName(org.getName());
            chaincode.setPeerName(peer.getName());
            chaincode.setChannelName(channel.getName());
            multiService.getChaincodeService().instantiate(chaincode, Arrays.asList(api.exec.split(",")));
        } catch (TException e) {
            e.printStackTrace();
        }
        return list();
    }

    @GetMapping(value = "edit")
    public ModelAndView edit(@RequestParam("id") int id) {
        ModelAndView modelAndView = new ModelAndView("chaincodeSubmit");
        modelAndView.addObject("intentLarge", "编辑合约");
        modelAndView.addObject("intentLittle", "编辑");
        modelAndView.addObject("submit", "修改");
        modelAndView.addObject("intent", "edit");
        ChaincodeInfo chaincode;
        List<ChannelInfo> channels;
        try {
            chaincode = multiService.getChaincodeService().get(id);
            PeerInfo peer = multiService.getPeerService().get(multiService.getChannelService().get(chaincode.getChannelId()).getPeerId());
            OrgInfo org = multiService.getOrgService().get(peer.getId());
            LeagueInfo league = multiService.getLeagueService().get(org.getLeagueId());
            chaincode.setPeerName(peer.getName());
            chaincode.setOrgName(org.getName());
            chaincode.setLeagueName(league.getName());
            channels = multiService.getChannelService().listById(peer.getId());
            for (ChannelInfo channel : channels) {
                channel.setPeerName(peer.getName());
                channel.setOrgName(org.getName());
                channel.setLeagueName(league.getName());
            }
        } catch (TException e) {
            chaincode = new ChaincodeInfo();
            channels = new ArrayList<>();
            e.printStackTrace();
        }
        modelAndView.addObject("chaincode", chaincode);
        modelAndView.addObject("channels", channels);
        return modelAndView;
    }

    @GetMapping(value = "instantiate")
    public ModelAndView instantiate(@RequestParam("chaincodeId") int chaincodeId) {
        ModelAndView modelAndView = new ModelAndView("chaincodeInstantiate");
        modelAndView.addObject("intentLarge", "实例化合约");
        modelAndView.addObject("intentLittle", "实例化");
        modelAndView.addObject("submit", "实例化");
        modelAndView.addObject("chaincodeId", chaincodeId);

        Api apiInstantiate = new Api("实例化智能合约", INSTANTIATE.getIndex());

        modelAndView.addObject("api", apiInstantiate);
        return modelAndView;
    }

    @GetMapping(value = "verify")
    public ModelAndView verify(@RequestParam("chaincodeId") int chaincodeId) {
        ModelAndView modelAndView = new ModelAndView("chaincodeVerify");
        modelAndView.addObject("intentLarge", "验证合约");
        modelAndView.addObject("intentLittle", "验证");
        modelAndView.addObject("submit", "验证");
        modelAndView.addObject("chaincodeId", chaincodeId);

        List<Api> apis = new ArrayList<>();
        Api apiInvoke = new Api("执行智能合约", INVOKE.getIndex());
        Api apiQuery = new Api("查询智能合约", Api.Intent.QUERY.getIndex());
        Api apiInfo = new Api("查询当前链信息", Api.Intent.INFO.getIndex());
        Api apiHash = new Api("根据交易hash查询区块", Api.Intent.HASH.getIndex());
        Api apiNumber = new Api("根据交易区块高度查询区块", Api.Intent.NUMBER.getIndex());
        Api apiTxid = new Api("根据交易ID查询区块", Api.Intent.TXID.getIndex());
        apis.add(apiInvoke);
        apis.add(apiQuery);
        apis.add(apiInfo);
        apis.add(apiHash);
        apis.add(apiNumber);
        apis.add(apiTxid);

        Api apiIntent = new Api();

        modelAndView.addObject("apis", apis);
        modelAndView.addObject("apiIntent", apiIntent);
        return modelAndView;
    }

    @PostMapping(value = "verify")
    public ModelAndView verify(@ModelAttribute Api api, @RequestParam("chaincodeId") int id) {
        ModelAndView modelAndView = new ModelAndView("chaincodeResult");
        Api.Intent intent = Api.Intent.get(api.index);
        String result = null;
        String url = String.format("http://localhost:%s/%s", env.getProperty("server.port"), intent.getApiUrl());
        try {
            switch (intent) {
                case INVOKE:
                    StateInfo state = multiService.getState(id, api);
                    result = multiService.getStateService().invoke(state);
                    modelAndView.addObject("jsonStr", multiService.formatState(state));
                    modelAndView.addObject("method", "POST");
                    break;
                case QUERY:
                    state = multiService.getState(id, api);
                    result = multiService.getStateService().query(state);
                    modelAndView.addObject("jsonStr", multiService.formatState(state));
                    modelAndView.addObject("method", "POST");
                    break;
                case INFO:
                    result = multiService.getTraceService().queryBlockChainInfo(id);
                    modelAndView.addObject("jsonStr", "");
                    modelAndView.addObject("method", "GET");
                    break;
                case HASH:
                    TraceInfo trace = multiService.getTrace(id, api);
                    result = multiService.getTraceService().queryBlockByHash(trace);
                    modelAndView.addObject("jsonStr", multiService.formatTrace(trace));
                    modelAndView.addObject("method", "POST");
                    break;
                case NUMBER:
                    trace = multiService.getTrace(id, api);
                    result = multiService.getTraceService().queryBlockByNumber(trace);
                    modelAndView.addObject("jsonStr", multiService.formatTrace(trace));
                    modelAndView.addObject("method", "POST");
                    break;
                case TXID:
                    trace = multiService.getTrace(id, api);
                    result = multiService.getTraceService().queryBlockByTransactionID(trace);
                    modelAndView.addObject("jsonStr", multiService.formatTrace(trace));
                    modelAndView.addObject("method", "POST");
                    break;
            }
        } catch (TException e) {
            result = String.format("error:%s", e.getMessage());
            e.printStackTrace();
        }
        modelAndView.addObject("result", result);
        modelAndView.addObject("api", api);
        modelAndView.addObject("url", url);
        return modelAndView;
    }

    @GetMapping(value = "list")
    public ModelAndView list() {
        ModelAndView modelAndView = new ModelAndView("chaincodes");
        try {
            List<ChaincodeInfo> chaincodes = multiService.getChaincodeService().listAll();
            for (ChaincodeInfo chaincode : chaincodes) {
                chaincode.setChannelName(multiService.getPeerService().get(chaincode.getChannelId()).getName());
            }
            modelAndView.addObject("chaincodes", chaincodes);
        } catch (TException e) {
            modelAndView.addObject("chaincodes", new ArrayList<>());
            e.printStackTrace();
        }
        return modelAndView;
    }

}
