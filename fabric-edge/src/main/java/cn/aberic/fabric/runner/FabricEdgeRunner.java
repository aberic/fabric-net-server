package cn.aberic.fabric.runner;

import cn.aberic.fabric.thrift.MultiServiceProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    private Logger logger = LogManager.getLogger(FabricEdgeRunner.class);

    @Resource
    private MultiServiceProvider multiService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            multiService.getSystemService().init();
        } catch (Exception e) {
            logger.debug("edge start first!");
        }
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
