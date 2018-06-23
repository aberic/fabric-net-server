#!/usr/bin/env bash
echo
echo " ____    _____      _      ____    _____  "
echo "/ ___|  |_   _|    / \    |  _ \  |_   _| "
echo "\___ \    | |     / _ \   | |_) |   | |   "
echo " ___) |   | |    / ___ \  |  _ <    | |   "
echo "|____/    |_|   /_/   \_\ |_| \_\   |_|   "
echo

## 启动SDK
startService () {
    ## 启动spring-boot服务
    nohup java -jar /home/fabric-service-1.0-RC1.jar > /home/fabric-service-1.0-RC1.nohup 2>&1 &

    tail -f logs /home/fabric-service-1.0-RC1.nohup
}

startService