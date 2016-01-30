package com.dc.esb.servicegov.export.impl;

import com.dc.esb.servicegov.export.IPackerParserConfigGenerator;
import com.dc.esb.servicegov.export.common.AbstractPackerParserConfigGenerator;

/**
 * Created by vincentfxz on 15/11/26.
 */
public class FixPackerParserConfigGenerator  extends AbstractPackerParserConfigGenerator implements IPackerParserConfigGenerator {

    private static final String MSG_TYPE = "fix";

    @Override
    public String getMsgType() {
        return MSG_TYPE;
    }
}
