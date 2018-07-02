package cn.aberic.fabric.runner;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 作者：Aberic on 2018/6/22 21:36
 * 邮箱：abericyang@gmail.com
 */
@Component
public class FabricEdgeRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) {
        System.out.println();
        System.out.println(" _____   _   _   ____    ");
        System.out.println("| ____| | \\ | | |  _ \\   ");
        System.out.println("|  _|   |  \\| | | | | |  ");
        System.out.println("| |___  | |\\  | | |_| |  ");
        System.out.println("|_____| |_| \\_| |____/   ");
        System.out.println();
        System.out.println("===================== please make your fabric net work ===================== ");
        System.out.println();
        System.out.println("=============================== read sdk logs ============================== ");
    }
}
