#!/bin/sh

cls=$1
flag=0
clsDwd=cn.itcast.logistics.offline.dwd.${cls}DWD
clsDws=cn.itcast.logistics.offline.dws.${cls}DWS
baseDir=/export/services/logistics/lib

# 获取运行主题名称, 用于组装类名称
if [[ $cls = "Customer" || $cls = "ExpressBill" || $cls = "DotTransportTool" || $cls = "WarehouseTransportTool" || $cls = "Warehouse" || $cls = "Waybill" ]]; then
        echo -e "\e[32m==== MainClass is: "$clsDwd" and "$clsDws"\e[0m"
        flag=1
else
        echo -e "\e[31mUsage : \n\tExpressBill\n\tCustomer\n\tDotTransportTool\n\tWareHouseTransportTool\n\tWareHouse\n\tWaybill\e[0m"
fi

# 当即将运行应用时, 需要设置参数
if [[ $flag = 1 ]]; then
        echo -e "\e[32m==== builder spark commands ====\e[0m"
        # 构建DWD宽表应用运行命令
        cmd1="spark-submit --packages org.apache.kudu:kudu-spark2_2.11:1.9.0-cdh6.2.1 --class ${clsDwd} --master yarn --deploy-mode cluster --driver-memory 512m --executor-cores 1 --executor-memory 512m --queue default --verbose ${baseDir}/logistics-etl.jar"
        # 构建DWS指标计算应用运行命令
        cmd2="spark-submit --packages org.apache.kudu:kudu-spark2_2.11:1.9.0-cdh6.2.1 --class ${clsDws} --master yarn --deploy-mode cluster --driver-memory 512m --executor-cores 1 --executor-memory 512m --queue default --verbose ${baseDir}/logistics-etl.jar"
        echo -e "\e[32m==== CMD1 is: $cmd1 ====\e[0m"
        echo -e "\e[32m==== CMD2 is: $cmd2 ====\e[0m"
fi

# 运行命令, 执行Spark应用
if [[ $flag = 1 && `ls -A $baseDir|wc -w` = 1 ]]; then
        echo -e "\e[32m==== start execute ${clsDwd} ====\e[0m"
        sh $cmd1
        echo -e "\e[32m==== start execute ${clsDws} ====\e[0m"
        sh $cmd2
else
        echo -e "\e[31m==== The jar package in $baseDir directory does not exist! ====\e[0m"
        echo -e "\e[31m==== Plase upload logistics-common.jar,logistics-etl.jar,logistics-generate.jar ====\e[0m"
fi
