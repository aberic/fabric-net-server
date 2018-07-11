#!/usr/bin/env bash
echo
echo " ____    _____      _      ____    _____  "
echo "/ ___|  |_   _|    / \    |  _ \  |_   _| "
echo "\___ \    | |     / _ \   | |_) |   | |   "
echo " ___) |   | |    / ___ \  |  _ <    | |   "
echo "|____/    |_|   /_/   \_\ |_| \_\   |_|   "
echo

## 启动SDK
startEdge () {
    ## 启动spring-boot服务
    nohup java -jar /home/fabric-edge-1.0-RC5.jar > /home/fabric-edge-1.0-RC5.nohup 2>&1 &

    tail -f logs /home/fabric-edge-1.0-RC5.nohup
}

startEdge