package cn.aberic.fabric.runner;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 作者：Aberic on 2018/6/15 23:48
 * 邮箱：abericyang@gmail.com
 */
@Component
public class FabricRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
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
