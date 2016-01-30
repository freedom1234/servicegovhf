package com.dc.esb.servicegov.util;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-5-28
 * Time: 下午3:55
 */
public class PdfUtils {

    private static final Log log = LogFactory.getLog(PdfUtils.class);
    public static Font ST_SONG_FONT = null;
    public static Font ST_SONG_SMALL_FONT = null;
    public static Font ST_SONG_SMALL_BOLD_FONT = null;
    public static Font ST_SONG_MIDDLE_FONT = null;
    public static Font ST_SONG_MIDDLE_BOLD_FONT = null;
    public static Font ST_SONG_BIG_FONT = null;
    public static Font ST_SONG_BIG_BOLD_FONT = null;
    public static Font TABLE_NORMAL_FONT = null;
    public static Font TABLE_BOLD_FONT = null;
    public static Font NORMAL_SMALL_FONT = null;
    public static Font NORMAL_SMALL_BOLD_FONT = null;
    public static Font NORMAL_MIDDLE_FONT = null;
    public static Font NORMAL_MIDDLE_BOLD_FONT = null;
    public static Font NORMAL_BIG_FONT = null;
    public static Font NORMAL_BIG_BOLD_FONT = null;

    static {
        BaseFont bfChinese = null;
        String font = "STSongStd-Light";
        String code = "UniGB-UCS2-H";
        try {
            bfChinese = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", false);
            Font fontChineseBlack = new Font(bfChinese, 12, Font.NORMAL, Color.BLACK);
            ST_SONG_FONT = new Font(bfChinese, 12, Font.NORMAL, Color.BLACK);
            ST_SONG_SMALL_FONT = new Font(bfChinese, 8, Font.NORMAL, Color.BLACK);
            ST_SONG_SMALL_BOLD_FONT = new Font(bfChinese, 8, Font.BOLD, Color.BLACK);
            ST_SONG_MIDDLE_BOLD_FONT = new Font(bfChinese, 10, Font.BOLD, Color.BLACK);
            ST_SONG_MIDDLE_FONT = new Font(bfChinese, 10, Font.NORMAL, Color.BLACK);
            ST_SONG_BIG_FONT = new Font(bfChinese, 12, Font.NORMAL, Color.BLACK);
            ST_SONG_BIG_BOLD_FONT = new Font(bfChinese, 12, Font.BOLD, Color.BLACK);
            TABLE_NORMAL_FONT = new Font(Font.TIMES_ROMAN, 8);
            TABLE_BOLD_FONT = new Font(Font.TIMES_ROMAN, 8, Font.BOLD, Color.BLACK);
            NORMAL_SMALL_FONT = new Font(Font.TIMES_ROMAN, 8, Font.NORMAL, Color.BLACK);
            NORMAL_SMALL_BOLD_FONT = new Font(Font.TIMES_ROMAN, 8, Font.BOLD, Color.BLACK);
            NORMAL_MIDDLE_FONT = new Font(Font.TIMES_ROMAN, 10, Font.NORMAL, Color.BLACK);
            NORMAL_MIDDLE_BOLD_FONT = new Font(Font.TIMES_ROMAN, 10, Font.BOLD, Color.BLACK);
            NORMAL_BIG_FONT = new Font(Font.TIMES_ROMAN, 12, Font.NORMAL, Color.BLACK);
            NORMAL_BIG_BOLD_FONT = new Font(Font.TIMES_ROMAN, 12, Font.BOLD, Color.BLACK);
        } catch (Exception e) {
            String errorMsg = "获取中文字体[" + font + ":" + code + "]失败！";
            log.error(errorMsg);
        }
    }

    public static void renderChinese(String content, Document doc) throws Exception {
        Paragraph paragraph = new Paragraph(content, ST_SONG_FONT);
        doc.add(paragraph);
    }

    public static void renderLatin(String content, Document doc) throws Exception {
        Paragraph paragraph = new Paragraph(content);
        doc.add(paragraph);
    }

    public static void renderChineseInBlock(String content, Paragraph paragraph) {
        Phrase phrase = new Phrase(content, ST_SONG_FONT);
        paragraph.add(phrase);
    }

    public static void renderLatinInBlock(String content, Paragraph paragraph) {
        Phrase phrase = new Phrase(content);
        paragraph.add(phrase);
    }
	public static void renderLine(String content, Document document) throws DocumentException {
		Phrase opDescPhrase = new Phrase();
		opDescPhrase.add(content);
		document.add(opDescPhrase);
	}
    public static void renderInLine(String content, Phrase phrase, Font font) {
        Phrase phraseToAppend = new Phrase(content, font);
        phrase.add(phraseToAppend);
    }

    public static void renderLatinTableHeader(String content, PdfPCell cell) {
        renderTableHeader(content, cell, TABLE_NORMAL_FONT);
    }

    public static void renderChineseTableHeader(String content, PdfPCell cell) {
        renderTableHeader(content, cell, ST_SONG_SMALL_BOLD_FONT);
    }

    public static void renderTableHeader(String content, PdfPCell cell, Font font) {
        Phrase phrase = null;
        if (null != font) {
            phrase = new Phrase(content, font);
        } else {
            phrase = new Phrase(content);
        }
        cell.setPhrase(phrase);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(Color.LIGHT_GRAY);

    }

    public static void renderTableHeader(Phrase phrase, PdfPCell cell) {
        cell.setPhrase(phrase);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(Color.LIGHT_GRAY);
    }

    public static void renderChineseTableData(String content, PdfPCell cell) {
        renderTableData(content, cell, ST_SONG_SMALL_FONT);
    }

    public static void renderLatinTableData(String content, PdfPCell cell) {
        renderTableData(content, cell, TABLE_NORMAL_FONT);
    }

    public static void renderTableData(String content, PdfPCell cell, Font font) {
        Phrase phrase = null;
        if (null != font) {
            phrase = new Phrase(content, font);
        } else {
            phrase = new Phrase(content);
        }
        cell.setPhrase(phrase);
    }

	public static File mergePdfFile(String[] param) {
		// String[] param = {
		// "D:\\pdf\\201412351-142918浦发银行服务手册-0100.pdf" ,
		// "D:\\pdf\\201412351-142918浦发银行服务手册-0200.pdf",
		// "D:\\pdf\\201412351-142918浦发银行服务手册-0300.pdf",
		// "D:\\pdf\\201412351-142918浦发银行服务手册-0400.pdf",
		// "D:\\pdf\\201412351-142918浦发银行服务手册-0500.pdf",
		// "D:\\pdf\\201412351-142918浦发银行服务手册-0600.pdf",
		// "D:\\pdf\\201412351-142918浦发银行服务手册-0700.pdf",
		// "D:\\pdf\\201412351-142918浦发银行服务手册-0800.pdf",
		// "D:\\pdf\\201412351-142918浦发银行服务手册-0900.pdf",
		// "D:\\pdf\\201412351-142918浦发银行服务手册-1000.pdf",
		// "D:\\pdf\\201412351-142918浦发银行服务手册-1100.pdf",
		// "D:\\pdf\\201412351-142918浦发银行服务手册-1200.pdf",
		// "D:\\pdf\\201412351-142918浦发银行服务手册-1300.pdf",
		// "D:\\pdf\\201412351-142918浦发银行服务手册-all.pdf",};
		File pdfAllFile = null;
		if (param.length < 2)
			System.err.println("arguments: file1 [file2 ...] destfile");
		else
			try {
				int i = 0;
				ArrayList localArrayList = new ArrayList();
				int j = 0;
				String str = param[(param.length - 1)];
				Document localDocument = null;
				PdfCopy localPdfCopy = null;
				while (j < param.length - 1) {
					PdfReader localPdfReader = new PdfReader(param[j]);
					localPdfReader.consolidateNamedDestinations();
					int k = localPdfReader.getNumberOfPages();
					List localList = SimpleBookmark.getBookmark(localPdfReader);
					if (localList != null) {
						if (i != 0)
							SimpleBookmark.shiftPageNumbers(localList, i, null);
						localArrayList.addAll(localList);
					}
					i += k;
					if (j == 0) {
						localDocument = new Document(
								localPdfReader.getPageSizeWithRotation(1));
						localPdfCopy = new PdfCopy(localDocument,
								new FileOutputStream(str));
						localDocument.open();
					}
					int m = 0;
					while (m < k) {
						m++;
						PdfImportedPage localPdfImportedPage = localPdfCopy
								.getImportedPage(localPdfReader, m);
						localPdfCopy.addPage(localPdfImportedPage);
					}
					localPdfCopy.freeReader(localPdfReader);
					j++;
				}
				if (!localArrayList.isEmpty())
					localPdfCopy.setOutlines(localArrayList);
				localDocument.close();
			} catch (Exception localException) {
				localException.printStackTrace();
			}
			pdfAllFile = new File(param[(param.length - 1)]);
			if(pdfAllFile.exists()){
				return pdfAllFile; 
			}else{
				return null;
			}
	}
    public static void main(String[] args) throws Exception {
    	String[] param = {
    			"D:\\pdf\\201412351-142918浦发银行服务手册-0100.pdf" ,
    			"D:\\pdf\\201412351-142918浦发银行服务手册-0200.pdf",
    			"D:\\pdf\\201412351-142918浦发银行服务手册-0300.pdf",
    			"D:\\pdf\\201412351-142918浦发银行服务手册-0400.pdf",
    			"D:\\pdf\\201412351-142918浦发银行服务手册-0500.pdf",
    			"D:\\pdf\\201412351-142918浦发银行服务手册-0600.pdf",
    			"D:\\pdf\\201412351-142918浦发银行服务手册-0700.pdf",
    			"D:\\pdf\\201412351-142918浦发银行服务手册-0800.pdf",
    			"D:\\pdf\\201412351-142918浦发银行服务手册-0900.pdf",
    			"D:\\pdf\\201412351-142918浦发银行服务手册-1000.pdf",
    			"D:\\pdf\\201412351-142918浦发银行服务手册-1100.pdf",
    			"D:\\pdf\\201412351-142918浦发银行服务手册-1200.pdf",
    			"D:\\pdf\\201412351-142918浦发银行服务手册-1300.pdf",
    			"D:\\pdf\\201412351-142918浦发银行服务手册-all.pdf",};
    	 if (param.length < 2)
    	      System.err.println("arguments: file1 [file2 ...] destfile");
    	    else
    	      try
    	      {
    	        int i = 0;
    	        ArrayList localArrayList = new ArrayList();
    	        int j = 0;
    	        String str = param[(param.length - 1)];
    	        Document localDocument = null;
    	        PdfCopy localPdfCopy = null;
    	        while (j < param.length - 1)
    	        {
    	          PdfReader localPdfReader = new PdfReader(param[j]);
    	          localPdfReader.consolidateNamedDestinations();
    	          int k = localPdfReader.getNumberOfPages();
    	          List localList = SimpleBookmark.getBookmark(localPdfReader);
    	          if (localList != null)
    	          {
    	            if (i != 0)
    	              SimpleBookmark.shiftPageNumbers(localList, i, null);
    	            localArrayList.addAll(localList);
    	          }
    	          i += k;
    	          System.out.println("There are " + k + " pages in " + param[j]);
    	          if (j == 0)
    	          {
    	            localDocument = new Document(localPdfReader.getPageSizeWithRotation(1));
    	            localPdfCopy = new PdfCopy(localDocument, new FileOutputStream(str));
    	            localDocument.open();
    	          }
    	          int m = 0;
    	          while (m < k)
    	          {
    	            m++;
    	            PdfImportedPage localPdfImportedPage = localPdfCopy.getImportedPage(localPdfReader, m);
    	            localPdfCopy.addPage(localPdfImportedPage);
    	            System.out.println("Processed page " + m);
    	          }
    	          localPdfCopy.freeReader(localPdfReader);
    	          j++;
    	        }
    	        if (!localArrayList.isEmpty())
    	          localPdfCopy.setOutlines(localArrayList);
    	        localDocument.close();
    	      }
    	      catch (Exception localException)
    	      {
    	        localException.printStackTrace();
    	      }
	}
}
