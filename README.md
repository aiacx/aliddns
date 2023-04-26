# AliDDNS

### 一个基于阿里云SDK动态解析域名的工具，支持多个ipv4和ipv6同时解析
#### 1.启动
##### Linux/Mac OS
````
java -jar aliddns.jar
````
##### Windows
###### 1.前台启动
###### CMD中直接java -jar会假死，需要关闭快速编辑模式和插入模式，推荐使用run.bat
````
run.bat
````
###### 2.后台启动(推荐)
###### 使用nssm.exe注册服务
####
````
1.双击运行 install_service.bat
2.浏览Application Path，选择run.bat
3.输入服务名，点击确定
4.net start 服务名
````
#### 2.配置文件
##### 在config.xml启动后自动生成，需修改后再次启动
##### 关于ip接口，可暂时访问http://pkpig.com:8443/api/ip/client
````
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<config>
    <!--获取外网ipv4的接口，需要接口直接以文本形式返回客户端ip-->
    <ipv4Api>http://lqt.cc:8445/api/ip/client</ipv4Api>
    <aliyun>
        <!--阿里云节点地域-->
        <regionId>cn-chengdu</regionId>
        <!--阿里云assessKeyId-->
        <assessKeyId>你的assessKeyId</assessKeyId>
        <!--阿里云secret-->
        <secret>你的secret</secret>
    </aliyun>
    <items>
        <!--域名动态解析的配置项-->
        <item>
            <!--监测的时间间隔 单位：秒-->
            <interval>60</interval>
            <!--IP类型 ipv4或ipv6，如果是ipv4则会调用ipv4Api接口获得公网ipv4-->
            <ipType>ipv6</ipType>
            <!--顶级域名-->
            <domainName>lqt.cc</domainName>
            <!--主机记录-->
            <RRKeyWord>abc</RRKeyWord>
            <!--TTL-->
            <TTL>600</TTL>
        </item>
    </items>
</config>
````

#### 3.日志文件
##### 同目录log文件夹
