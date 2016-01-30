package com.dc.esb.servicegov.export.impl;

import com.dc.esb.servicegov.export.util.XMLUtil;

/**
 * Created by kongxfa on 2015/12/23.
 */
public class Testxml {
    public static void main(String[]args){
        String a="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<service package_type=\"xml\" store-mode=\"UTF-8\">\n" +
                "    \n" +
                "    <MessageTypeID metadataid=\"MsgType\" type=\"n4\" />\n" +
                "<bitmap metadataid=\"PrimAcctNo\" type=\"b128\" />\n" +
                "<f2 metadataid=\"PrimAcctNo\" type=\"n..19(LLVAR)\" />\n" +
                "<f3 metadataid=\"TranPcsCd\" type=\"n6\" />\n" +
                "<f4 metadataid=\"TranAmt\" type=\"n12\" />\n" +
                "<f5 metadataid=\"ClrgAmt\" type=\"n12\" />\n" +
                "<f6 metadataid=\"CrdhldAtmDbtAmt\" type=\"n12\" />\n" +
                "<f7 metadataid=\"TranStmTmStmp\" type=\"n10(MMDDhhmmss)\" />\n" +
//                "<9 metadataid=\"ClrCnvrRt\" type=\"n8\" />\n" +
                "<f10 metadataid=\"CrdhldAtmDbtCnvrRt\" type=\"n8\" />\n" +
                "<f11 metadataid=\"StmTranAudtNo\" type=\"n6\" />\n" +
                "<f12 metadataid=\"TranLclTm\" type=\"n6(hhmmss)\" />\n" +
                "<f13 metadataid=\"TranLclDt\" type=\"n4(MMDD)\" />\n" +
                "<f14 metadataid=\"CrdExpDt\" type=\"n4(YYMM)\" />\n" +
                "<f15 metadataid=\"ClrgDt\" type=\"n4(MMDD)\" />\n" +
                "<f16 metadataid=\"ExgDt\" type=\"n4(MMDD)\" />\n" +
                "<f18 metadataid=\"MrchTp\" type=\"n4\" />\n" +
                "<f19 metadataid=\"AcptInstCtyCd\" type=\"n3\" />\n" +
                "<f25 metadataid=\"POSCdtnCd\" type=\"n2\" />\n" +
                "<f28 metadataid=\"MngtFee\" type=\"x+n8\" />\n" +
                "<f32 metadataid=\"AgncInstIdCd\" type=\"n..11(LLVAR)\" />\n" +
                "<f33 metadataid=\"SndInstIdCd\" type=\"n..11(LLVAR)\" />\n" +
                "<f37 metadataid=\"SrchRefrNo\" type=\"an12\" />\n" +
                "<f38 metadataid=\"AhrIdentRspCd\" type=\"an6\" />\n" +
                "<f39 metadataid=\"RspCd\" type=\"an2\" />\n" +
                "<f41 metadataid=\"CrdAcptTmlIdCd\" type=\"ans8\" />\n" +
                "<f42 metadataid=\"CrdAcptIdCd\" type=\"ans15\" />\n" +
                "<f49 metadataid=\"TranCcy\" type=\"an3\" />\n" +
                "<f50 metadataid=\"ClgCcyCd\" type=\"an3\" />\n" +
                "<f51 metadataid=\"CrdhldCcyCd\" type=\"an3\" />\n" +
                "<f54 metadataid=\"ActBal\" type=\"an...040(LLLVAR)\" />\n" +
                "<f57 metadataid=\"IssurAdlData\" type=\"ans...100(LLLVAR)\" />\n" +
                "<f60 metadataid=\"MsgRsnCd\" type=\"n4\" />\n" +
                "<f60.2.1 metadataid=\"AcctType\" type=\"ans1\" />\n" +
                "<f60.2.2 metadataid=\"TmnlEntrCapb\" type=\"n1\" />\n" +
                "<f60.2.3 metadataid=\"ICCrdCdtnCd\" type=\"n1\" />\n" +
                "<f60.2.4 metadataid=\"RsrvFld\" type=\"n1\" />\n" +
                "<f60.2.5 metadataid=\"TmnlType\" type=\"n2\" />\n" +
                "<f60.2.6 metadataid=\"OTECPNetFlg\" type=\"n1\" />\n" +
                "<f60.2.7 metadataid=\"TCCrdVerfRlbltFlg\" type=\"n1\" />\n" +
                "<f60.2.8 metadataid=\"ElctrCmrcFlg\" type=\"n2\" />\n" +
                "<f60.2.9 metadataid=\"IntrctvMd\" type=\"n1\" />\n" +
                "<f60.3.1 metadataid=\"SpclChrgTp\" type=\"ans2\" />\n" +
                "<f60.3.2 metadataid=\"SpclChrgLvl\" type=\"ans1\" />\n" +
                "\n" +
                "</service>";
        String b= XMLUtil.formatXml(a);
        System.out.println(b);
    }
}
