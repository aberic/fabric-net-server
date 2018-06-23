package cn.aberic.fabric.utils;

import cn.aberic.thrift.common.SystemInfo;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hyperic.jni.ArchNotSupportedException;
import org.hyperic.sigar.*;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StreamUtils;

import java.io.*;

public class SystemUtil {

    private static Logger logger = LogManager.getLogger(SystemUtil.class);

    private static boolean isInit = false;

    public static boolean isIsInit() {
        return isInit;
    }

    //初始化sigar的配置文件
    public static void initSigar() throws IOException {
        SigarLoader loader = new SigarLoader(Sigar.class);
        String lib = null;

        try {
            lib = loader.getLibraryName();
            logger.debug("初始化完成");
        } catch (ArchNotSupportedException var7) {
            logger.error(var7.getMessage(), var7);
        }
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource("classpath:/sigar/" + lib);
        if (resource.exists()) {
            InputStream is = resource.getInputStream();
            File tempDir = FileUtils.getTempDirectory();
            BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(new File(tempDir, lib), false));
            StreamUtils.copy(is, os);
            is.close();
            os.close();
            System.setProperty("org.hyperic.sigar.path", tempDir.getCanonicalPath());
        }
        isInit = true;
    }

    private static Double cpu() throws SigarException {
        Sigar sigar = new Sigar();
        CpuInfo infos[] = sigar.getCpuInfoList();
        CpuPerc cpuList[] = sigar.getCpuPercList();
        int length = infos.length;
        Double d = 0d;
        for (int i = 0; i < length; i++) {// 不管是单块CPU还是多CPU都适用
            d += printCpuPerc(cpuList[i]);
        }
        return (d * 100) / length;
    }

    private static Double printCpuPerc(CpuPerc cpu) {
        return cpu.getCombined();
    }

    private static Double swap() throws SigarException {
        Sigar sigar = new Sigar();
        Swap swap = sigar.getSwap();
        double total = swap.getTotal()/1024L;
        double used = swap.getUsed()/1024L;
        logger.debug(String.format("swap totle %s, used %s", total, used));
        return (used / total) * 100;
    }

    private static Double memory() throws SigarException {
        Sigar sigar = new Sigar();
        Mem mem = sigar.getMem();
        double total = mem.getTotal()/1024L;
        double used = mem.getUsed()/1024L;
        logger.debug(String.format("mem total %s, used %s", total, used));
        return (used / total) * 100;
    }

    public static SystemInfo get() throws SigarException {
        SystemInfo systemInfo = new SystemInfo();
        systemInfo.setCpu(cpu());
        systemInfo.setMemory(memory());
        systemInfo.setSwap(swap());
        return systemInfo;
    }

}
