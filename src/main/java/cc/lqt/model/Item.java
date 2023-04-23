package cc.lqt.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

@Data
public class Item {
    @JacksonXmlProperty(localName = "interval")
    private Long interval;
    @JacksonXmlProperty(localName = "ipType")
    private String ipType;
    @JacksonXmlProperty(localName = "domainName")
    private String domainName;
    @JacksonXmlProperty(localName = "RRKeyWord")
    private String RRKeyWord;
    @JacksonXmlProperty(localName = "TTL")
    private Long TTL;
}
