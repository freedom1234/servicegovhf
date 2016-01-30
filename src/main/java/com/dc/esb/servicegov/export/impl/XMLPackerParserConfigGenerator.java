package com.dc.esb.servicegov.export.impl;

import com.dc.esb.servicegov.export.IPackerParserConfigGenerator;
import com.dc.esb.servicegov.export.common.AbstractPackerParserConfigGenerator;
import org.springframework.stereotype.Component;

/**
 * Created by vincentfxz on 15/11/24.
 */
@Component
public class XMLPackerParserConfigGenerator extends AbstractPackerParserConfigGenerator implements IPackerParserConfigGenerator {

    private static final String MSG_TYPE = "xml";

    @Override
    public String getMsgType() {
        return MSG_TYPE;
    }
}
