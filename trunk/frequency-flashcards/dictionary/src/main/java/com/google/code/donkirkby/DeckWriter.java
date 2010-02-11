package com.google.code.donkirkby;

import java.io.PrintWriter;
import java.io.Writer;

public class DeckWriter {
	private PrintWriter printer;
	private Writer writer;
	private int cardNumber;
	
	public void writeHeader()
	{
		printer.println("<?xml version='1.0' encoding='UTF-8'?>");
		printer.println("<mnemosyne core_version='1'>");

	}
	
	public void writeCategory(String categoryName)
	{
		printer.println("<category active='1'>");
		printer.println("<name>" + escape(categoryName) + "</name>");
		printer.println("</category>");
	}
	
	public void writeCard(String categoryName, String question, String answer)
	{
		printer.println("<item id='_" + cardNumber++ + "'>");
		printer.println("<cat>" + escape(categoryName) + "</cat>");
		printer.println("<Q>" + escape(question) + "</Q>");
		printer.println("<A>" + escape(answer) + "</A>");
		printer.println("</item>");
	}

	private String escape(String text) {
		return text
			.replaceAll("&", "&amp;")
			.replaceAll("<", "&lt;")
			.replaceAll(">", "&gt;");
	}
	
	public void writeFooter()
	{
		printer.println("</mnemosyne>");
	}
	
	public Writer getWriter() {
		return writer;
	}
	public void setWriter(Writer writer) {
		this.writer = writer;
		this.printer = new PrintWriter(writer);
	}
}
