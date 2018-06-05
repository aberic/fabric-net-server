package cn.aberic.simple.module.mapper;

import cn.aberic.simple.module.dto.PeerDTO;
import org.apache.ibatis.annotations.Param;

/**
 * 描述：
 *
 * @author : Aberic 【2018/6/5 17:54】
 */
public interface PeerDao {

    void addPeer(@Param("p") PeerDTO peer);

}
