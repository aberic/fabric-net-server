package cn.aberic.fabric.sdk;

import java.util.Map;

/**
 * 描述：BlockListener监听返回map集合
 *
 * @author : Aberic 【2018/5/22 18:49】
 */
public interface BlockListener {
    void received(Map<String, String> map);
}
