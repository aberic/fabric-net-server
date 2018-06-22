package cn.aberic.fabric.runner;

import cn.aberic.fabric.utils.SystemUtil;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class SystemRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        SystemUtil.initSigar();
    }
}
