#!/usr/bin/env bash
echo
echo " ____    _____      _      ____    _____  "
echo "/ ___|  |_   _|    / \    |  _ \  |_   _| "
echo "\___ \    | |     / _ \   | |_) |   | |   "
echo " ___) |   | |    / ___ \  |  _ <    | |   "
echo "|____/    |_|   /_/   \_\ |_| \_\   |_|   "
echo

initUrl=http://localhost:8080/sdk/init

## 启动SDK
startSDK () {
    ## 启动spring-boot服务
    nohup java -jar /home/simple-1.0-beta2.jar > /home/simple-1.0-beta2.nohup 2>&1 &

    # 日志总行数
    line=0
    # 当前读取行数
    index=0
    today=`date +%Y-%m-%d`
    hour=`date +%H`
    while [ -f /home/simple-1.0-beta2.nohup ]
    do
        line=`awk '{print NR}' /home/simple-1.0-beta2.nohup|tail -n1`
        result=`grep "$today $hour" /home/simple-1.0-beta2.nohup | grep "Started"`
        if [[ "$result" != "" ]]
        then
            head -n -0 /home/simple-1.0-beta2.nohup |tail -n +${index}
            break
        elif [[ ${line} -gt ${index} ]]
        then head -n -0 /home/simple-1.0-beta2.nohup |tail -n +${index}
        index=${line}
        else
            sleep 0.1s
        fi
    done
    echo "spring-boot Started..........."
}

# 初始化环境变量数据
init () {
    result=`curl ${initUrl}`
    echo "result = "${result}
	if [ ${result} -eq 0 ]; then
	    echo "===================== org add success ===================== "
	    echo
	else
	    echo "===================== org add failed ===================== "
	    echo
	    exit 1
	fi
}

echo "start sdk..."
startSDK

echo "sdk init..."
init

echo
echo "===================== All GOOD, start sdk completed ===================== "
echo

echo
echo " _____   _   _   ____    "
echo "| ____| | \ | | |  _ \   "
echo "|  _|   |  \| | | | | |  "
echo "| |___  | |\  | | |_| |  "
echo "|_____| |_| \_| |____/   "
echo

echo
echo "===================== read sdk logs ===================== "
echo

tail -f logs /home/simple-1.0-beta2.nohup