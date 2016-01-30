package com.dc.esb.servicegov.export.impl;

import com.dc.esb.servicegov.export.IPackerParserConfigGenerator;
import com.dc.esb.servicegov.export.common.AbstractPackerParserConfigGenerator;

/**
 * Created by kongxfa on 2015/12/25.
 */
public class ECDSPackerParserConfigGenerator2 extends AbstractPackerParserConfigGenerator implements IPackerParserConfigGenerator {

    private static final String MSG_TYPE = "ecds";

    @Override
    public String getMsgType() {
        return MSG_TYPE;
    }
}
