package cc.lqt.utils;

import cc.lqt.model.Aliyun;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.alidns.model.v20150109.DescribeDomainRecordsRequest;
import com.aliyuncs.alidns.model.v20150109.DescribeDomainRecordsResponse;
import com.aliyuncs.alidns.model.v20150109.UpdateDomainRecordRequest;
import com.aliyuncs.alidns.model.v20150109.UpdateDomainRecordResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class DDNSUtil {
    private static IAcsClient client;

    private static DescribeDomainRecordsResponse describeDomainRecords(DescribeDomainRecordsRequest request) {
        /**
         * 获取主域名的所有解析记录列表
         */
        try {
            // 调用SDK发送请求
            return client.getAcsResponse(request);
        } catch (ClientException e) {
            //e.printStackTrace();
            log.info(e.getMessage());
            // 发生调用错误，抛出运行时异常
            //throw new RuntimeException();
            return null;
        }
    }

    private static UpdateDomainRecordResponse updateDomainRecord(UpdateDomainRecordRequest request) {
        try {
            // 调用SDK发送请求
            return client.getAcsResponse(request);
        } catch (ClientException e) {
            //e.printStackTrace();
            // 发生调用错误，抛出运行时异常
            //throw new RuntimeException();
            log.info(e.getMessage());
            return null;
        }
    }

    private static DescribeDomainRecordsResponse getDescribeDomainRecordsRequest(String domainName, String RRKeyWord, String type) {
        // 查询指定二级域名的最新解析记录
        DescribeDomainRecordsRequest describeDomainRecordsRequest = new DescribeDomainRecordsRequest();
        // 主域名
        describeDomainRecordsRequest.setDomainName(domainName);
        // 主机记录
        describeDomainRecordsRequest.setRRKeyWord(RRKeyWord);
        // 解析记录类型
        describeDomainRecordsRequest.setType(type);
        DescribeDomainRecordsResponse describeDomainRecordsResponse = describeDomainRecords(describeDomainRecordsRequest);
        return describeDomainRecordsResponse;
    }
    public static void init(String regionId,String assessKeyId,String secret){
        // 设置鉴权参数，初始化客户端
        DefaultProfile profile = DefaultProfile.getProfile(
                regionId,// 地域ID
                assessKeyId,// 您的AccessKey ID
                secret);// 您的AccessKey Secret
        client = new DefaultAcsClient(profile);
    }

    public static void init(Aliyun aliyun){
        // 设置鉴权参数，初始化客户端
        DefaultProfile profile = DefaultProfile.getProfile(
                aliyun.regionId,// 地域ID
                aliyun.assessKeyId,// 您的AccessKey ID
                aliyun.secret);// 您的AccessKey Secret
        client = new DefaultAcsClient(profile);
    }

    /**
     * @param domainName 顶级域名    lqt.cc
     * @param RRKeyWord  二级       home
     * @param type       解析类型    A
     */
    public static String getRecordValue(String domainName, String RRKeyWord, String type) {
        // 获取对应的记录值
        DescribeDomainRecordsResponse describeDomainRecordsResponse = getDescribeDomainRecordsRequest(domainName, RRKeyWord, type);
        String recordValue ="";
        try{
            recordValue = describeDomainRecordsResponse.getDomainRecords().get(0).getValue();
        }catch (IndexOutOfBoundsException e){
            log.warn(RRKeyWord+"."+domainName +" " + type +" 未找到，请先到阿里云添加此记录值！");
        }
        return recordValue;
    }

    /**
     * @param domainName   顶级域名    lqt.cc
     * @param RRKeyWord    二级       home
     * @param type         解析类型    A
     * @param newRRKeyWord 新二级     home
     * @param newType      新解析类型  A
     * @param newValue     新解析值    8.8.8.8
     * @param TTL          TTL       600
     */
    public static int updateRecordValue(String domainName, String RRKeyWord, String type, String newRRKeyWord, String newType, String newValue,Long TTL) {
        DescribeDomainRecordsResponse describeDomainRecordsResponse = getDescribeDomainRecordsRequest(domainName, RRKeyWord, type);
        //log_print("describeDomainRecords",describeDomainRecordsResponse);
        List<DescribeDomainRecordsResponse.Record> domainRecords = describeDomainRecordsResponse.getDomainRecords();
        // 最新的一条解析记录
        if (domainRecords.size() != 0) {
            DescribeDomainRecordsResponse.Record record = domainRecords.get(0);
            // 记录ID
            String recordId = record.getRecordId();
            // 记录值
            String recordsValue = record.getValue();
            if (!newValue.equals(recordsValue)) {
                // 修改解析记录
                UpdateDomainRecordRequest updateDomainRecordRequest = new UpdateDomainRecordRequest();
                // 主机记录
                updateDomainRecordRequest.setRR(newRRKeyWord);
                // 记录ID
                updateDomainRecordRequest.setRecordId(recordId);
                // 将主机记录值改为当前主机IP
                updateDomainRecordRequest.setValue(newValue);
                // 解析记录类型
                updateDomainRecordRequest.setType(newType);
                updateDomainRecordRequest.setTTL(TTL);
                UpdateDomainRecordResponse updateDomainRecordResponse = updateDomainRecord(updateDomainRecordRequest);
                //log_print("updateDomainRecord",updateDomainRecordResponse);
                return 0;
            }
        }
        return 1;
    }

}