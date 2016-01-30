package com.dc.esb.servicegov.export;

/**
 * Created by vincentfxz on 15/11/24.
 */
public abstract class IExportableNode {

    public abstract String getId() ;

    public abstract void setId(String id);

    public abstract String getStructName();

    public abstract void setStructName(String structName);

    public abstract String getStructAlias();

    public abstract void setStructAlias(String structAlias) ;

    public abstract String getMetadataId();

    public abstract void setMetadataId(String metadataId);

    public abstract int getSeq();

    public abstract void setSeq(int seq);

    public abstract String getType();

    public abstract void setType(String type);

    public abstract String getLength();

    public abstract void setLength(String length);

    public abstract String getRequired();

    public abstract void setRequired(String required);

    public abstract String getParentId();

    public abstract void setParentId(String id);

}
