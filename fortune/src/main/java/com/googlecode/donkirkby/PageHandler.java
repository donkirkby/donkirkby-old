package com.googlecode.donkirkby;

import java.io.File;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.Font;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;

public class PageHandler extends PdfPageEventHelper
{
	private String fileName;
	private BaseFont font;
	
	public PageHandler(String fileName)
	{
		this.fileName = fileName;

	}
	
	@Override
	public void onOpenDocument(PdfWriter writer, Document document)
	{
		try
		{
			font = BaseFont.createFont(
					BaseFont.TIMES_ROMAN, 
					BaseFont.WINANSI, 
					false);
		} catch (Exception e)
		{
			throw new ExceptionConverter(e);
		}
	}	

	@Override
	public void onEndPage(PdfWriter writer, Document document)
	{
        try {
        	if (writer.getPageNumber() == 1)
			{
				return;
			}
        	
        	File file = new File(fileName);
            PdfContentByte cb = writer.getDirectContent();
            cb.saveState();
            // compose the footer
            String text = "Find the rules and more fortune puzzles at " +
            		"http://donkirkby.googlecode.com - This puzzle is " +
            		file.getName();
            float textBase = document.bottom() - 20;
            cb.beginText();
            cb.setFontAndSize(font, 12);
            cb.setTextMatrix(document.left(), textBase);
            cb.showText(text);
            cb.endText();
//            cb.saveState();
            cb.restoreState();
            cb.sanityCheck();
        }
        catch (Exception e) {
            throw new ExceptionConverter(e);
        }
	}
}
