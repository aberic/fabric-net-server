package cn.aberic.fabric.runner;

import cn.aberic.fabric.dao.User;
import cn.aberic.fabric.dao.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 作者：Aberic on 2018/6/22 21:36
 * 邮箱：abericyang@gmail.com
 */
@Component
public class FabricEdgeRunner implements ApplicationRunner {

    @Resource
    private UserMapper userMapper;

    @Value("${username}")
    private String username;
    @Value("${password}")
    private String password;

    @Override
    public void run(ApplicationArguments args) {
        addUser();
        System.out.println();
        System.out.println(" _____   _   _   ____    ");
        System.out.println("| ____| | \\ | | |  _ \\   ");
        System.out.println("|  _|   |  \\| | | | | |  ");
        System.out.println("| |___  | |\\  | | |_| |  ");
        System.out.println("|_____| |_| \\_| |____/   ");
        System.out.println();
        System.out.println("===================== please make your fabric net work ===================== ");
        System.out.println();
        System.out.println("=============================== read logs ============================== ");
    }

    private void addUser() {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
//        user.setUsername(System.getenv("USERNAME"));
//        user.setPassword(System.getenv("PASSWORD"));
        userMapper.add(user);
    }
}
