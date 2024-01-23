package cc.lqt;

import cc.lqt.model.Config;
import cc.lqt.model.Item;
import cc.lqt.utils.ConfigUtil;
import cc.lqt.utils.DDNSUtil;
import cc.lqt.utils.IPUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;


@Slf4j
public class Main{

    public static Config config;

    static {
        //初始化config
        try {
            config = ConfigUtil.init();
        } catch (IOException e) {
            log.error("读取配置文件时出错 ->" +e.getMessage());
        }
    }
    public static void main(String[] args) {
        if (config == null){
            log.error("请在同目录config.xml配置参数，再运行本程序！");
            return;
        }
        log.info("欢迎使用本工具 Ver 1.0");
        log.info("官网：https://lqt.cc");
        log.info("GitHub：https://github.com/LiaoQingTing");
        log.info("共计["+config.items.size()+"]个解析项目");
        DDNSUtil.init(config.aliyun);
        IPUtil.init(config.ipv4Api,true);
        for(int i=0;i<config.items.size();i++){
            Item item = config.items.get(i);
            Thread t = new Thread(new ItemThread(i,item));
            t.start();
        }
    }

}