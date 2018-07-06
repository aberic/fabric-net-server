/*
 * Copyright (c) 2018. Aberic - aberic@qq.com - All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.aberic.fabric.controller;

import cn.aberic.fabric.bean.Api;
import cn.aberic.fabric.bean.State;
import cn.aberic.fabric.bean.Trace;
import cn.aberic.fabric.dao.*;
import cn.aberic.fabric.service.*;
import cn.aberic.fabric.utils.SpringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static cn.aberic.fabric.bean.Api.Intent.INSTANTIATE;
import static cn.aberic.fabric.bean.Api.Intent.INVOKE;
import static cn.aberic.fabric.bean.Api.Intent.UPGRADE;

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
                               @ModelAttribute Api api,
                               @RequestParam("init") boolean init,
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
                chaincode = resetChaincode(chaincode);
                chaincodeService.install(chaincode, sourceFile, api, init);
                break;
            case "upgrade":
                Chaincode chaincode1 = chaincodeService.get(id);
                chaincode1 = resetChaincode(chaincode1);
                chaincode1.setVersion(chaincode.getVersion());
                chaincodeService.upgrade(chaincode1, sourceFile, api);
                break;
        }
        return new ModelAndView(new RedirectView("list"));
    }

    @PostMapping(value = "verify")
    public ModelAndView verify(@ModelAttribute Api api, @RequestParam("chaincodeId") int id) {
        ModelAndView modelAndView = new ModelAndView("chaincodeResult");
        Api.Intent intent = Api.Intent.get(api.getIndex());
        String result = "";
        String url = String.format("http://localhost:%s/%s", env.getProperty("server.port"), Objects.requireNonNull(intent).getApiUrl());
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

        chaincodeService.instantiate(chaincode, Arrays.asList(api.getExec().split(",")));
        return new ModelAndView(new RedirectView("list"));
    }

    @GetMapping(value = "add")
    public ModelAndView add() {
        ModelAndView modelAndView = new ModelAndView("chaincodeSubmit");
        modelAndView.addObject("intentLittle", SpringUtil.get("enter"));
        modelAndView.addObject("submit", SpringUtil.get("submit"));
        modelAndView.addObject("intent", "add");
        modelAndView.addObject("chaincode", new Chaincode());
        modelAndView.addObject("channels", getChannelFullList());
        modelAndView.addObject("init", false);
        return modelAndView;
    }

    @GetMapping(value = "edit")
    public ModelAndView edit(@RequestParam("id") int id) {
        ModelAndView modelAndView = new ModelAndView("chaincodeSubmit");
        modelAndView.addObject("intentLittle", SpringUtil.get("edit"));
        modelAndView.addObject("submit", SpringUtil.get("modify"));
        modelAndView.addObject("intent", "edit");
        modelAndView.addObject("init", false);
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
    public ModelAndView instantiate(@RequestParam("id") int chaincodeId) {
        ModelAndView modelAndView = new ModelAndView("chaincodeInstantiate");
        modelAndView.addObject("intentLittle", SpringUtil.get("instantiate"));
        modelAndView.addObject("submit", SpringUtil.get("submit"));
        modelAndView.addObject("chaincodeId", chaincodeId);

        Api apiInstantiate = new Api("实例化智能合约", INSTANTIATE.getIndex());

        modelAndView.addObject("api", apiInstantiate);
        return modelAndView;
    }

    @GetMapping(value = "install")
    public ModelAndView install() {
        ModelAndView modelAndView = new ModelAndView("chaincodeInstall");
        modelAndView.addObject("intentLittle", SpringUtil.get("install"));
        modelAndView.addObject("submit", SpringUtil.get("submit"));
        modelAndView.addObject("intent", "install");
        modelAndView.addObject("chaincode", new Chaincode());
        modelAndView.addObject("channels", getChannelFullList());
        modelAndView.addObject("init", false);

        Api apiInstantiate = new Api("实例化智能合约", INSTANTIATE.getIndex());

        modelAndView.addObject("api", apiInstantiate);
        return modelAndView;
    }

    @GetMapping(value = "upgrade")
    public ModelAndView upgrade(@RequestParam("id") int chaincodeId) {
        ModelAndView modelAndView = new ModelAndView("chaincodeUpgrade");
        modelAndView.addObject("intentLittle", SpringUtil.get("upgrade"));
        modelAndView.addObject("submit", SpringUtil.get("submit"));
        modelAndView.addObject("intent", "upgrade");
        modelAndView.addObject("init", true);
        modelAndView.addObject("chaincode", chaincodeService.get(chaincodeId));

        Api apiInstantiate = new Api("升级智能合约", UPGRADE.getIndex());

        modelAndView.addObject("api", apiInstantiate);
        return modelAndView;
    }

    @GetMapping(value = "verify")
    public ModelAndView verify(@RequestParam("id") int chaincodeId) {
        ModelAndView modelAndView = new ModelAndView("chaincodeVerify");
        modelAndView.addObject("intentLittle", SpringUtil.get("verify"));
        modelAndView.addObject("submit", SpringUtil.get("submit"));
        modelAndView.addObject("chaincodeId", chaincodeId);

        List<Api> apis = new ArrayList<>();
        Api apiInvoke = new Api(SpringUtil.get("chaincode_invoke"), INVOKE.getIndex());
        Api apiQuery = new Api(SpringUtil.get("chaincode_query"), Api.Intent.QUERY.getIndex());
        Api api = new Api(SpringUtil.get("chaincode_block_info"), Api.Intent.INFO.getIndex());
        Api apiHash = new Api(SpringUtil.get("chaincode_block_get_by_hash"), Api.Intent.HASH.getIndex());
        Api apiTxid = new Api(SpringUtil.get("chaincode_block_get_by_txid"), Api.Intent.TXID.getIndex());
        Api apiNumber = new Api(SpringUtil.get("chaincode_block_get_by_height"), Api.Intent.NUMBER.getIndex());
        apis.add(apiInvoke);
        apis.add(apiQuery);
        apis.add(api);
        apis.add(apiHash);
        apis.add(apiTxid);
        apis.add(apiNumber);

        Api apiIntent = new Api();

        modelAndView.addObject("apis", apis);
        modelAndView.addObject("apiIntent", apiIntent);
        return modelAndView;
    }

    @GetMapping(value = "delete")
    public ModelAndView delete(@RequestParam("id") int id) {
        chaincodeService.delete(id);
        return new ModelAndView(new RedirectView("list"));
    }

    @GetMapping(value = "list")
    public ModelAndView list() {
        ModelAndView modelAndView = new ModelAndView("chaincodes");
        List<Chaincode> chaincodes = chaincodeService.listAll();
        for (Chaincode chaincode : chaincodes) {
            chaincode.setChannelName(channelService.get(chaincode.getChannelId()).getName());
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
        state.setStrArray(Arrays.asList(api.getExec().trim().split(",")));
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
        trace.setTrace(api.getExec().trim());
        return trace;
    }

    private String formatTrace(Trace trace) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", trace.getId());
        jsonObject.put("trace", trace.getTrace());
        return jsonObject.toJSONString();
    }

    private Chaincode resetChaincode(Chaincode chaincode) {
        Channel channel = channelService.get(chaincode.getChannelId());
        Peer peer = peerService.get(channel.getPeerId());
        Org org = orgService.get(peer.getId());
        League league = leagueService.get(org.getLeagueId());
        chaincode.setLeagueName(league.getName());
        chaincode.setOrgName(org.getName());
        chaincode.setPeerName(peer.getName());
        chaincode.setChannelName(channel.getName());
        return chaincode;
    }

}
