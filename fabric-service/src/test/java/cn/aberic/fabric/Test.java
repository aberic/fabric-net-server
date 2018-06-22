package cn.aberic.fabric;

import cn.aberic.fabric.utils.SystemUtil;

import java.io.IOException;

public class Test {

    public static void main(String[] args) throws IOException {
        SystemUtil.initSigar();
        try {
            // System信息，从jvm获取
            SystemUtil.property();
            System.out.println("----------------------------------");
            // cpu信息
            SystemUtil.cpu();
            System.out.println("----------------------------------");
            // 内存信息
            SystemUtil.memory();
            System.out.println("----------------------------------");
            // 操作系统信息
            SystemUtil.os();
            System.out.println("----------------------------------");
            // 用户信息
            SystemUtil.who();
            System.out.println("----------------------------------");
            // 文件系统信息
            SystemUtil.file();
            System.out.println("----------------------------------");
            // 网络信息
            SystemUtil.net();
            System.out.println("----------------------------------");
            // 以太网信息
            SystemUtil.ethernet();
            System.out.println("----------------------------------");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

}
