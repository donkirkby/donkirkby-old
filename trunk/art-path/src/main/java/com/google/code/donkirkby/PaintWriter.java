package com.google.code.donkirkby;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.TranscodingHints;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

public class PaintWriter {
	private SVGGraphics2D svgGenerator;
	
	public Graphics2D createGraphicsContext(int width, int height)
	{
		// Get a DOMImplementation.
		DOMImplementation domImpl = GenericDOMImplementation
				.getDOMImplementation();

		// Create an instance of org.w3c.dom.Document.
		String svgNS = "http://www.w3.org/2000/svg";
		Document document = domImpl.createDocument(svgNS, "svg", null);

		// Create an instance of the SVG Generator.
		svgGenerator = new SVGGraphics2D(document);
		svgGenerator.setSVGCanvasSize(new Dimension(width, height));

		return svgGenerator;
	}
	
	public void write(String svgFileName, String pngFileName)
	{
		try {
			// Finally, write out SVG to a file.
			boolean useCSS = true; // we want to use CSS style attributes
			Writer out = 
				new OutputStreamWriter(new FileOutputStream(svgFileName),"UTF-8");
			svgGenerator.stream(out, useCSS);
			
			convert(svgFileName, pngFileName);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void convert(String svgFileName, String pngFileName)
	{
		Transcoder transcoder = new PNGTranscoder();
        Map<TranscodingHints.Key, Float> hints = 
        	new HashMap<TranscodingHints.Key, Float>();
        hints.put(ImageTranscoder.KEY_MAX_HEIGHT, new Float(750));
        hints.put(ImageTranscoder.KEY_MAX_WIDTH, new Float(750));
        transcoder.setTranscodingHints(hints);
		
        try {
	        TranscoderInput input = 
	        	new TranscoderInput(new File(svgFileName).toURI().toString());
			TranscoderOutput output =
				new TranscoderOutput(new FileOutputStream(pngFileName));
		
			transcoder.transcode(input, output);
		} catch (Exception e) {
			throw new RuntimeException("Transcoding failed.", e);
		}
	}

	public int getHeight() {
		return svgGenerator.getSVGCanvasSize().height;
	}

	public void setHeight(int height) {
		svgGenerator.getSVGCanvasSize().height = height;
	}

	public int getWidth() {
		return svgGenerator.getSVGCanvasSize().width;
	}

	public void setWidth(int width) {
		this.svgGenerator.getSVGCanvasSize().width = width;
	}
}
