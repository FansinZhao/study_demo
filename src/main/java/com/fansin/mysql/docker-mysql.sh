#!/bin/bash
# Created by zhaofeng on 17-5-11.
#
#### 
#启动三个实例
docker run --name mysql1 -e  MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=test -d mysql
docker run --name mysql2 -e  MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=test -d mysql
docker run --name mysql3 -e  MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=test -d mysql
#查看ip地址
docker ps |grep mysql[0-9]|awk '{system("docker inspect --format={{.NetworkSettings.IPAddress}} "$NF"")}'