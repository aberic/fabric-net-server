package cn.aberic.fabric.service;

import cn.aberic.fabric.dao.Org;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 作者：Aberic on 2018/6/27 22:07
 * 邮箱：abericyang@gmail.com
 */
public interface OrgService {

    int add(Org org, MultipartFile file);

    int update(Org org, MultipartFile file);

    List<Org> listAll();

    List<Org> listById(int id);

    Org get(int id);

    int countById(int id);

    int count();
}
