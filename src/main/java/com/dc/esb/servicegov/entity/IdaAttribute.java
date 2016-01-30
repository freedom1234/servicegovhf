package com.dc.esb.servicegov.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "ida_attribute")
public class IdaAttribute implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "type")
    private String type;//固定值0:，表达式:1
    @Column(name = "value")
    private String value;
    @Column(name = "ida_id")
    private String idaId;

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

    public String getIdaId() {
        return idaId;
    }

    public void setIdaId(String idaId) {
        this.idaId = idaId;
    }
}
