package cc.lqt.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import java.util.List;


@Data
public class Config {
    @JacksonXmlProperty(localName = "ipv4Api")
    public String ipv4Api;
    @JacksonXmlProperty(localName = "aliyun")
    public Aliyun aliyun;
    @JacksonXmlElementWrapper(localName = "items")
    @JacksonXmlProperty(localName = "item")
    public List<Item> items;
}
