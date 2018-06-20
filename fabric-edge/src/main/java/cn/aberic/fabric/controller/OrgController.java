package cn.aberic.fabric.controller;

import cn.aberic.fabric.thrift.MultiServiceProvider;
import cn.aberic.thrift.org.OrgInfo;
import org.apache.thrift.TException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 描述：
 *
 * @author : Aberic 【2018/6/4 15:01】
 */
@CrossOrigin
@RestController
@RequestMapping("org")
public class OrgController {

    @Resource
    private MultiServiceProvider multiSService;

    @PostMapping(value = "add")
    public String add(@RequestBody OrgInfo org) {
        try {
            if (multiSService.getOrgService().add(org) > 0) {
                return "success";
            }
        } catch (TException e) {
            e.printStackTrace();
        }
        return "fail";
    }

    @PostMapping(value = "update")
    public String update(@RequestBody OrgInfo org) {
        try {
            if (multiSService.getOrgService().update(org) > 0) {
                return "success";
            }
        } catch (TException e) {
            e.printStackTrace();
        }
        return "fail";
    }

    @GetMapping(value = "list/{id}")
    public List<OrgInfo> list(@PathVariable("id") int id) {
        try {
            return multiSService.getOrgService().listById(id);
        } catch (TException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping(value = "listAll")
    public List<OrgInfo> listAll() {
        try {
            return multiSService.getOrgService().listAll();
        } catch (TException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping(value = "get/{id}")
    public OrgInfo get(@PathVariable("id") int id) {
        try {
            return multiSService.getOrgService().get(id);
        } catch (TException e) {
            e.printStackTrace();
        }
        return null;
    }

}
