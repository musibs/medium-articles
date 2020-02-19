package io.tbc.spring.ws.exception.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * @author somnath.musib
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArticleFault", propOrder = {"code", "description"})
public class ArticleFault {

    private String code;
    private String description;

    public ArticleFault() {}

    public ArticleFault(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
