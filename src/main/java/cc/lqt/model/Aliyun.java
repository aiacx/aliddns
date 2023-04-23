package cc.lqt.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

@Data
public class Aliyun {
    @JacksonXmlProperty(localName = "regionId")
    public String regionId;
    @JacksonXmlProperty(localName = "assessKeyId")
    public String assessKeyId;
    @JacksonXmlProperty(localName = "secret")
    public String secret;

}
