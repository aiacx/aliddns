package cc.lqt;

import cc.lqt.model.Item;
import cc.lqt.utils.DDNSUtil;
import cc.lqt.utils.IPUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class ItemThread implements Runnable{
    public int index;
    public Item item;

    @Override
    public void run() {
        log.info("DDNS["+index+"]["+item.getIpType()+"]"+"[TTL "+item.getTTL()+"][interval "+item.getInterval()+"]["+item.getRRKeyWord()+"."+item.getDomainName()+"]");
        String remoteIp="",localIp="",type="";
        while (true){
            try {
                if("ipv4".equals(item.getIpType())){
                    type = "A";
                    remoteIp = DDNSUtil.getRecordValue(item.getDomainName(),item.getRRKeyWord(),type);
                    localIp = IPUtil.getIpv4();
                }
                if("ipv6".equals(item.getIpType())){
                    type = "AAAA";
                    remoteIp = DDNSUtil.getRecordValue(item.getDomainName(),item.getRRKeyWord(),type);
                    localIp = IPUtil.getIpv6();
                }
                if(remoteIp != null && localIp !=null && !remoteIp.equals(localIp)){
                    //记录值和本机ip不同
                    log.warn(item.getRRKeyWord()+"."+item.getDomainName()+" IP发生变化，更新为："+localIp);
                    DDNSUtil.updateRecordValue(item.getDomainName(),item.getRRKeyWord(),type,item.getRRKeyWord(),type,localIp,item.getTTL());
                }else {
                    log.info(item.getRRKeyWord()+"."+item.getDomainName()+" IP："+ localIp);
                }
                Thread.sleep(item.getInterval()*1000);
            } catch (Exception e) {
                log.warn("线程["+index+"]"+e.getMessage());
            }
        }
    }


}
