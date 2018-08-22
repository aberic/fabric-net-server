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
import cn.aberic.fabric.dao.entity.Chaincode;
import cn.aberic.fabric.service.ChaincodeService;
import cn.aberic.fabric.service.StateService;
import cn.aberic.fabric.service.TraceService;
import cn.aberic.fabric.utils.SpringUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Objects;

import static cn.aberic.fabric.bean.Api.Intent.INSTANTIATE;
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
    private ChaincodeService chaincodeService;
    @Resource
    private StateService stateService;
    @Resource
    private TraceService traceService;

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
                chaincodeService.update(chaincode);
                break;
            case "install":
                chaincode = chaincodeService.resetChaincode(chaincode);
                chaincodeService.install(chaincode, sourceFile, api, init);
                break;
            case "upgrade":
                Chaincode chaincode1 = chaincodeService.get(id);
                chaincode1 = chaincodeService.resetChaincode(chaincode1);
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
        String url = String.format("http://localhost:port/%s", Objects.requireNonNull(intent).getApiUrl());
        modelAndView.addObject("url", url);
        switch (intent) {
            case INVOKE:
                State state = chaincodeService.getState(id, api);
                result = stateService.invoke(state);
                modelAndView.addObject("jsonStr", chaincodeService.formatState(state));
                modelAndView.addObject("method", "POST");
                break;
            case QUERY:
                state = chaincodeService.getState(id, api);
                result = stateService.query(state);
                modelAndView.addObject("jsonStr", chaincodeService.formatState(state));
                modelAndView.addObject("method", "POST");
                break;
            case INFO:
                String cc = chaincodeService.get(id).getCc();
                result = traceService.queryBlockChainInfo(cc, api.getKey(), chaincodeService.getCAByFlag(api.getFlag()));
                modelAndView.addObject("jsonStr", "");
                modelAndView.addObject("method", "GET");
                modelAndView.addObject("url", String.format("%s/%s/%s", url, cc, api.getKey()));
                break;
            case HASH:
                Trace trace = chaincodeService.getTrace(api);
                result = traceService.queryBlockByHash(trace);
                modelAndView.addObject("jsonStr", chaincodeService.formatTrace(trace));
                modelAndView.addObject("method", "POST");
                break;
            case NUMBER:
                trace = chaincodeService.getTrace(api);
                result = traceService.queryBlockByNumber(trace);
                modelAndView.addObject("jsonStr", chaincodeService.formatTrace(trace));
                modelAndView.addObject("method", "POST");
                break;
            case TXID:
                trace = chaincodeService.getTrace(api);
                result = traceService.queryBlockByTransactionID(trace);
                modelAndView.addObject("jsonStr", chaincodeService.formatTrace(trace));
                modelAndView.addObject("method", "POST");
                break;
        }
        modelAndView.addObject("result", result);
        modelAndView.addObject("api", api);
        return modelAndView;
    }

    @PostMapping(value = "instantiate")
    public ModelAndView instantiate(@ModelAttribute Api api, @RequestParam("chaincodeId") int chaincodeId) {
        chaincodeService.instantiate(chaincodeService.getInstantiateChaincode(api, chaincodeId), Arrays.asList(api.getExec().split(",")));
        return new ModelAndView(new RedirectView("list"));
    }

    @GetMapping(value = "add")
    public ModelAndView add() {
        ModelAndView modelAndView = new ModelAndView("chaincodeSubmit");
        modelAndView.addObject("intentLittle", SpringUtil.get("enter"));
        modelAndView.addObject("submit", SpringUtil.get("submit"));
        modelAndView.addObject("intent", "add");
        modelAndView.addObject("chaincode", new Chaincode());
        modelAndView.addObject("channels", chaincodeService.getChannelFullList());
        modelAndView.addObject("init", false);
        return modelAndView;
    }

    @GetMapping(value = "edit")
    public ModelAndView edit(@RequestParam("id") int chaincodeId) {
        ModelAndView modelAndView = new ModelAndView("chaincodeSubmit");
        modelAndView.addObject("intentLittle", SpringUtil.get("edit"));
        modelAndView.addObject("submit", SpringUtil.get("modify"));
        modelAndView.addObject("intent", "edit");
        modelAndView.addObject("init", false);

        Chaincode chaincode = chaincodeService.getEditChaincode(chaincodeId);
        modelAndView.addObject("chaincode", chaincode);
        modelAndView.addObject("channels", chaincodeService.getEditChannels(chaincode));
        return modelAndView;
    }

    @GetMapping(value = "instantiate")
    public ModelAndView instantiate(@RequestParam("id") int chaincodeId) {
        ModelAndView modelAndView = new ModelAndView("chaincodeInstantiate");
        modelAndView.addObject("intentLittle", SpringUtil.get("instantiate"));
        modelAndView.addObject("submit", SpringUtil.get("submit"));
        modelAndView.addObject("chaincodeId", chaincodeId);
        modelAndView.addObject("cas", chaincodeService.getCAs(chaincodeId));
        modelAndView.addObject("api", new Api("实例化智能合约", INSTANTIATE.getIndex()));
        return modelAndView;
    }

    @GetMapping(value = "install")
    public ModelAndView install() {
        ModelAndView modelAndView = new ModelAndView("chaincodeInstall");
        modelAndView.addObject("intentLittle", SpringUtil.get("install"));
        modelAndView.addObject("submit", SpringUtil.get("submit"));
        modelAndView.addObject("intent", "install");
        modelAndView.addObject("chaincode", new Chaincode());
        modelAndView.addObject("cas", chaincodeService.getAllCAs());
        modelAndView.addObject("channels", chaincodeService.getChannelFullList());
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
        modelAndView.addObject("cas", chaincodeService.getCAs(chaincodeId));
        modelAndView.addObject("api", new Api("升级智能合约", UPGRADE.getIndex()));
        return modelAndView;
    }

    @GetMapping(value = "verify")
    public ModelAndView verify(@RequestParam("id") int chaincodeId) {
        ModelAndView modelAndView = new ModelAndView("chaincodeVerify");
        modelAndView.addObject("intentLittle", SpringUtil.get("verify"));
        modelAndView.addObject("submit", SpringUtil.get("submit"));
        modelAndView.addObject("chaincodeId", chaincodeId);
        modelAndView.addObject("apis", chaincodeService.getApis());
        modelAndView.addObject("cas", chaincodeService.getCAs(chaincodeId));
        modelAndView.addObject("apiIntent", new Api());
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
        modelAndView.addObject("chaincodes", chaincodeService.getChaincodes());
        return modelAndView;
    }

}
