package cc.lqt.utils;

import cc.lqt.model.Config;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class ConfigUtil {
    private static final XmlMapper xmlMapper = new XmlMapper();
    private static final String configStr = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
            "<config>\n" +
            "    <!--获取外网ipv4的接口，需要接口直接以文本形式返回客户端ip-->\n" +
            "    <ipv4Api>http://lqt.cc:8443/api/ip/client</ipv4Api>\n" +
            "    <!--多ipv6地址时是否自动重启网卡刷新地址-->\n" +
            "    <isRestartNetwork>true</isRestartNetwork>\n" +
            "    <aliyun>\n" +
            "        <!--阿里云节点地域-->\n" +
            "        <regionId>cn-chengdu</regionId>\n" +
            "        <!--阿里云assessKeyId-->\n" +
            "        <assessKeyId>你的assessKeyId</assessKeyId>\n" +
            "        <!--阿里云secret-->\n" +
            "        <secret>你的secret</secret>\n" +
            "    </aliyun>\n" +
            "    <items>\n" +
            "        <!--域名动态解析的配置项-->\n" +
            "        <item>\n" +
            "            <!--监测的时间间隔 单位：秒-->\n" +
            "            <interval>60</interval>\n" +
            "            <!--IP类型 ipv4或ipv6，如果是ipv4则会调用ipv4Api接口获得公网ipv4-->\n" +
            "            <ipType>ipv4</ipType>\n" +
            "            <!--顶级域名-->\n" +
            "            <domainName>lqt.cc</domainName>\n" +
            "            <!--主机记录-->\n" +
            "            <RRKeyWord>ipv4</RRKeyWord>\n" +
            "            <!--TTL-->\n" +
            "            <TTL>600</TTL>\n" +
            "        </item>\n" +
            "        <!--域名动态解析的配置项-->\n" +
            "        <item>\n" +
            "            <!--监测的时间间隔 单位：秒-->\n" +
            "            <interval>60</interval>\n" +
            "            <!--IP类型 ipv4或ipv6，如果是ipv4则会调用ipv4Api接口获得公网ipv4-->\n" +
            "            <ipType>ipv6</ipType>\n" +
            "            <!--顶级域名-->\n" +
            "            <domainName>lqt.cc</domainName>\n" +
            "            <!--主机记录-->\n" +
            "            <RRKeyWord>ipv6</RRKeyWord>\n" +
            "            <!--TTL-->\n" +
            "            <TTL>600</TTL>\n" +
            "        </item>\n" +
            "    </items>\n" +
            "</config>";
    public static Config init() throws IOException {
        String xmlStr = "";
        String fileName = "config.xml";
        File file = new File(fileName);
        log.info("正在读取配置文件config.xml...");
        if(!file.exists()){
            // 文件不存在，则创建config.xml
            Path path = Paths.get(fileName);
            //使用newBufferedWriter创建文件并写文件
            //使用try-with-resource方法关闭流，不用手动关闭
            log.error("未找到config.xml，重新创建...");
            try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
                writer.write(configStr);
            }
            return null;
        }
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line;
        while((line = br.readLine()) != null){
            //process the line
            xmlStr += line;
            //System.out.println(line);
        }
        Config config = xmlMapper.readValue(xmlStr,Config.class);


        return config;
    }
}
