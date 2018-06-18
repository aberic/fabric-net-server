package cn.aberic.fabric.base;

/**
 * 业务层基类
 *
 * @author 杨毅 【2017年10月24日 - 20:58:52】
 */
public interface DTOService<B> {

    /**
     * 新增对象
     *
     * @return 请求返回
     */
    String add(B b);

    /**
     * 根据id获取对象列表
     *
     * @return 对象列表
     */
    String list(int id);

    /**
     * 获取对象列表
     *
     * @return 对象列表
     */
    String listAll();

    /**
     * 更新对象
     *
     * @return 请求返回
     */
    String update(B b);

    /**
     * 根据id获取对象
     *
     * @param id id
     *
     * @return 请求返回
     */
    String get(int id);

}
