package com.dc.esb.servicegov.service.impl;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.*;
import junit.framework.TestCase;

import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-5-28
 * Time: 上午9:11
 */
public class PdfGeneratorTest extends TestCase {
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void test() {
        String filename = "D:/helloworld.pdf";
        // step 1
        Document document = new Document();
        try {
            // step 2
            PdfWriter.getInstance(document, new FileOutputStream(filename));
            // step 3
            document.open();
            // step 4
            document.add(new Paragraph("Hello World!"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // step 5
            document.close();

        }
    }

    public void testChinese() {
        Document document = new Document();
        String filename = "D:/helloworld_Chinese.pdf";
        try {
            PdfWriter.getInstance(document, new FileOutputStream(filename));
            document.open();
            BaseFont bfChinese = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", false);
            Font fontChinese = new Font(bfChinese, 12, Font.NORMAL, Color.BLACK);
            Paragraph pf = new Paragraph("1.1.1 念奴娇", fontChinese);
            pf.add(new Paragraph("大江东去，浪淘尽，千古风流人物。故垒西边，人道是：三国周郎赤壁。乱石崩云，惊涛裂岸，卷起千堆雪", fontChinese));
            document.add(pf);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    public void testDirect() {
        String filename = "D:/helloworld_direct.pdf";
        Document document = new Document();
        // step 2
        try {
            PdfWriter writer = null;
            writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
            // step 3
            document.open();
            // step 4
            PdfContentByte canvas = writer.getDirectContentUnder();
            writer.setCompressionLevel(0);
            canvas.saveState();                               // q
            canvas.beginText();                               // BT
            canvas.moveText(36, 788);                         // 36 788 Td
            canvas.setFontAndSize(BaseFont.createFont(), 12); // /F1 12 Tf
            canvas.showText("Hello World");                   // (Hello World)Tj
            canvas.endText();                                 // ET
            canvas.restoreState();                            // Q
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            document.close();
        }
    }

    public void testChunk() {
        String filename = "D:/helloworld_chunks.pdf";
        Document document = new Document();
        String niannuj = "大江东去，浪淘尽，千古风流人物，故垒西边，人道是：三国周郎赤壁，乱石崩云，惊涛裂岸，卷起千堆雪，江山如画，一时多少豪杰";
        String[] niannujs = niannuj.split("，");
        try {
            PdfWriter.getInstance(document, new FileOutputStream(filename))
                    .setInitialLeading(16);
            document.open();
            BaseFont bfChinese = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", false);
            Font fontChineseBlack = new Font(bfChinese, 12, Font.NORMAL, Color.BLACK);
            Font fontChinese = new Font(bfChinese, 12, Font.NORMAL, Color.white);
            for (String s : niannujs) {
                // add a country to the document as a Chunk
                document.add(new Chunk(s, fontChineseBlack));
                document.add(new Chunk(" "));
                Chunk id = new Chunk(s, fontChinese);
                // with a background color
                id.setBackground(Color.BLACK, 1f, 0.5f, 1f, 1.5f);
                // and a text rise
                id.setTextRise(6);
                document.add(id);
                document.add(Chunk.NEWLINE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    public void testTable() {
        String filename = "D:/helloworld_table.pdf";
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(filename))
                    .setInitialLeading(16);
            document.open();
            BaseFont bfChinese = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", false);
            Font fontChineseBlack = new Font(bfChinese, 8, Font.NORMAL, Color.BLACK);
            Font fontChinese = new Font(bfChinese, 8, Font.NORMAL, Color.white);

            PdfPTable table = new PdfPTable(4);
            PdfPCell cell;
            cell = new PdfPCell(new Phrase("服务",fontChineseBlack));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setColspan(4);
            table.addCell(cell);

            PdfPCell headerENcell = new PdfPCell(new Phrase("英文名称", fontChineseBlack));
            headerENcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(headerENcell);
            PdfPCell headerCNcell = new PdfPCell(new Phrase("中文名称", fontChineseBlack));
            headerCNcell.setHorizontalAlignment(Element.ALIGN_CENTER);

            table.addCell(headerENcell);
            PdfPCell headerTypeENcell = new PdfPCell(new Phrase("类型", fontChineseBlack));
            headerTypeENcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(headerTypeENcell);
            PdfPCell headerRemarkcell = new PdfPCell(new Phrase("备注", fontChineseBlack));
            headerRemarkcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(headerRemarkcell);

            PdfPCell headerDirectcell = new PdfPCell(new Phrase("输入", fontChineseBlack));
            headerDirectcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerDirectcell.setColspan(4);
            table.addCell(headerDirectcell);

            PdfPCell headerDirectcell2 = new PdfPCell(new Phrase("输出", fontChineseBlack));
            headerDirectcell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerDirectcell2.setColspan(4);
            table.addCell(headerDirectcell2);

            document.add(table);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    public void testCreatePdf() {
        try{
            BaseFont bfChinese = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", false);
            Font fontChineseBlack = new Font(bfChinese, 8, Font.NORMAL, Color.BLACK);
            Font fontChinese = new Font(bfChinese, 8, Font.NORMAL, Color.white);
            String filename = "D:/helloworld_table2.pdf";
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filename));
            // step 3
            document.open();
            // step 4
            PdfPTable table = new PdfPTable(4);
            PdfPCell cell;
            cell = new PdfPCell(new Phrase("服务",fontChineseBlack));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(4);
            table.addCell(cell);

            PdfPCell cell1;
            cell1 = new PdfPCell(new Phrase("英文名称",fontChineseBlack));
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell1.setColspan(1);
            table.addCell(cell1);


            PdfPCell headerENcell = new PdfPCell(new Phrase("英文名称", fontChineseBlack));
            headerENcell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
            headerENcell.setColspan(1);
            table.addCell(headerENcell);

            PdfPCell headerCNcell = new PdfPCell(new Phrase("中文名称", fontChineseBlack));
            headerCNcell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(headerCNcell);

            PdfPCell headerTypeENcell = new PdfPCell(new Phrase("类型", fontChineseBlack));
            headerTypeENcell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(headerTypeENcell);

            PdfPCell headerRemarkcell = new PdfPCell(new Phrase("备注", fontChineseBlack));
            headerRemarkcell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(headerTypeENcell);

//            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
//            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

            document.add(table);

            // step 5
            document.close();
            // Close the database connection

        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
