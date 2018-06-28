package cn.aberic.fabric.controller;

import cn.aberic.fabric.bean.Api;
import cn.aberic.fabric.bean.State;
import cn.aberic.fabric.bean.Trace;
import cn.aberic.fabric.dao.*;
import cn.aberic.fabric.service.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

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
    private ChannelService channelService;
    @Resource
    private PeerService peerService;
    @Resource
    private OrgService orgService;
    @Resource
    private LeagueService leagueService;
    @Resource
    private ChaincodeService chaincodeService;
    @Resource
    private StateService stateService;
    @Resource
    private TraceService traceService;
    @Resource
    private Environment env;

    @PostMapping(value = "submit")
    public ModelAndView submit(@ModelAttribute Chaincode chaincode,
                               @RequestParam("intent") String intent,
                               @RequestParam(value = "sourceFile", required = false) MultipartFile sourceFile,
                               @RequestParam("id") int id) {
        switch (intent) {
            case "add":
                chaincodeService.add(chaincode);
                break;
            case "edit":
                chaincode.setId(id);
                chaincodeService.update(chaincode);
                break;
            case "install":
                Channel channel = channelService.get(chaincode.getChannelId());
                Peer peer = peerService.get(channel.getPeerId());
                Org org = orgService.get(peer.getId());
                League league = leagueService.get(org.getLeagueId());
                chaincode.setLeagueName(league.getName());
                chaincode.setOrgName(org.getName());
                chaincode.setPeerName(peer.getName());
                chaincode.setChannelName(channel.getName());
                chaincodeService.install(chaincode, sourceFile);
                break;
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
        modelAndView.addObject("chaincode", new Chaincode());
        modelAndView.addObject("channels", getChannelFullList());
        return modelAndView;
    }

    @GetMapping(value = "install")
    public ModelAndView install() {
        ModelAndView modelAndView = new ModelAndView("chaincodeInstall");
        modelAndView.addObject("intentLarge", "安装合约");
        modelAndView.addObject("intentLittle", "安装");
        modelAndView.addObject("submit", "安装");
        modelAndView.addObject("intent", "install");
        modelAndView.addObject("chaincode", new Chaincode());
        modelAndView.addObject("channels", getChannelFullList());
        return modelAndView;
    }

    @PostMapping(value = "instantiate")
    public ModelAndView instantiate(@ModelAttribute Api api, @RequestParam("chaincodeId") int id) {
        Chaincode chaincode = chaincodeService.get(id);
        Channel channel = channelService.get(chaincode.getChannelId());
        Peer peer = peerService.get(channel.getPeerId());
        Org org = orgService.get(peer.getId());
        League league = leagueService.get(org.getLeagueId());
        chaincode.setLeagueName(league.getName());
        chaincode.setOrgName(org.getName());
        chaincode.setPeerName(peer.getName());
        chaincode.setChannelName(channel.getName());
        chaincodeService.instantiate(chaincode, Arrays.asList(api.exec.split(",")));
        return new ModelAndView(new RedirectView("list"));
    }

    @GetMapping(value = "edit")
    public ModelAndView edit(@RequestParam("id") int id) {
        ModelAndView modelAndView = new ModelAndView("chaincodeSubmit");
        modelAndView.addObject("intentLarge", "编辑合约");
        modelAndView.addObject("intentLittle", "编辑");
        modelAndView.addObject("submit", "修改");
        modelAndView.addObject("intent", "edit");
        Chaincode chaincode = chaincodeService.get(id);
        Peer peer = peerService.get(channelService.get(chaincode.getChannelId()).getPeerId());
        Org org = orgService.get(peer.getId());
        League league = leagueService.get(org.getLeagueId());
        chaincode.setPeerName(peer.getName());
        chaincode.setOrgName(org.getName());
        chaincode.setLeagueName(league.getName());
        List<Channel> channels = channelService.listById(peer.getId());
        for (Channel channel : channels) {
            channel.setPeerName(peer.getName());
            channel.setOrgName(org.getName());
            channel.setLeagueName(league.getName());
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
        Api api = new Api("查询当前链信息", Api.Intent.INFO.getIndex());
        Api apiHash = new Api("根据交易hash查询区块", Api.Intent.HASH.getIndex());
        Api apiNumber = new Api("根据交易区块高度查询区块", Api.Intent.NUMBER.getIndex());
        Api apiTxid = new Api("根据交易ID查询区块", Api.Intent.TXID.getIndex());
        apis.add(apiInvoke);
        apis.add(apiQuery);
        apis.add(api);
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
        String result = "";
        String url = String.format("http://localhost:%s/%s", env.getProperty("server.port"), intent.getApiUrl());
        switch (intent) {
            case INVOKE:
                State state = getState(id, api);
                result = stateService.invoke(state);
                modelAndView.addObject("jsonStr", formatState(state));
                modelAndView.addObject("method", "POST");
                break;
            case QUERY:
                state = getState(id, api);
                result = stateService.query(state);
                modelAndView.addObject("jsonStr", formatState(state));
                modelAndView.addObject("method", "POST");
                break;
            case INFO:
                result = traceService.queryBlockChainInfo(id);
                modelAndView.addObject("jsonStr", "");
                modelAndView.addObject("method", "GET");
                break;
            case HASH:
                Trace trace = getTrace(id, api);
                result = traceService.queryBlockByHash(trace);
                modelAndView.addObject("jsonStr", formatTrace(trace));
                modelAndView.addObject("method", "POST");
                break;
            case NUMBER:
                trace = getTrace(id, api);
                result = traceService.queryBlockByNumber(trace);
                modelAndView.addObject("jsonStr", formatTrace(trace));
                modelAndView.addObject("method", "POST");
                break;
            case TXID:
                trace = getTrace(id, api);
                result = traceService.queryBlockByTransactionID(trace);
                modelAndView.addObject("jsonStr", formatTrace(trace));
                modelAndView.addObject("method", "POST");
                break;
        }
        modelAndView.addObject("result", result);
        modelAndView.addObject("api", api);
        modelAndView.addObject("url", url);
        return modelAndView;
    }

    @GetMapping(value = "list")
    public ModelAndView list() {
        ModelAndView modelAndView = new ModelAndView("chaincodes");
        List<Chaincode> chaincodes = chaincodeService.listAll();
        for (Chaincode chaincode : chaincodes) {
            chaincode.setChannelName(peerService.get(chaincode.getChannelId()).getName());
        }
        modelAndView.addObject("chaincodes", chaincodes);
        return modelAndView;
    }

    private List<Channel> getChannelFullList() {
        List<Channel> channels = channelService.listAll();
        for (Channel channel : channels) {
            Peer peer = peerService.get(channel.getPeerId());
            channel.setPeerName(peer.getName());
            Org org = orgService.get(peer.getId());
            channel.setOrgName(org.getName());
            League league = leagueService.get(org.getLeagueId());
            channel.setLeagueName(league.getName());
        }
        return channels;
    }

    private State getState(int id, Api api) {
        State state = new State();
        state.setId(id);
        state.setStrArray(Arrays.asList(api.exec.trim().split(",")));
        return state;
    }

    private String formatState(State state) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", state.getId());
        JSONArray jsonArray = JSONArray.parseArray(JSON.toJSONString(state.getStrArray()));
        jsonObject.put("strArray", jsonArray);
        return jsonObject.toJSONString();
    }

    private Trace getTrace(int id, Api api) {
        Trace trace = new Trace();
        trace.setId(id);
        trace.setTrace(api.exec.trim());
        return trace;
    }

    private String formatTrace(Trace trace) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", trace.getId());
        jsonObject.put("trace", trace.getTrace());
        return jsonObject.toJSONString();
    }

}
