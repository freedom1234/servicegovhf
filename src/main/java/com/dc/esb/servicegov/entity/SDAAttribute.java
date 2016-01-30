package com.dc.esb.servicegov.entity;

import com.dc.esb.servicegov.export.IExportableNode;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "sda_attribute")
public class SDAAttribute implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "type")
    private String type;//固定值0:，表达式:1
    @Column(name = "value")
    private String value;
    @Column(name = "sda_id")
    private String sdaId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSdaId() {
        return sdaId;
    }

    public void setSdaId(String sdaId) {
        this.sdaId = sdaId;
    }
}
