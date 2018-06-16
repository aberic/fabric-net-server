#!/usr/bin/env bash
echo
echo " ____    _____      _      ____    _____  "
echo "/ ___|  |_   _|    / \    |  _ \  |_   _| "
echo "\___ \    | |     / _ \   | |_) |   | |   "
echo " ___) |   | |    / ___ \  |  _ <    | |   "
echo "|____/    |_|   /_/   \_\ |_| \_\   |_|   "
echo

## 启动SDK
startSDK () {
    ## 启动spring-boot服务
    nohup java -jar /home/fabric-1.0-beta3.jar > /home/fabric-1.0-beta3.nohup 2>&1 &

    tail -f logs /home/fabric-1.0-beta3.nohup
}

startSDK