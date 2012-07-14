package com.googlecode.donkirkby;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	new App().run();
    }
    
    public void run()
    {
    	try
		{
    		System.out.println("Starting.");
    		HashMap<Integer, Integer> puzzleCounts = 
    			new HashMap<Integer, Integer>();
    		Random random = new Random(1);
			File outputFolder = new File("output");
			if ( ! outputFolder.isDirectory())
			{
				outputFolder.mkdir();
			}
			
			processCategory("/humorists.txt", "Computers", puzzleCounts, random);
			processCategory("/literature.txt", "Literature", puzzleCounts, random);
			processCategory("/love.txt", "Love", puzzleCounts, random);
			processCategory("/pets.txt", "Pets", puzzleCounts, random);
			processCategory("/platitudes.txt", "Platitudes", puzzleCounts, random);
			processCategory("/work.txt", "Work", puzzleCounts, random);
			System.out.println("Success.");
		} catch (IOException e)
		{
			throw new RuntimeException(e);
		} catch (DocumentException e)
		{
			throw new RuntimeException(e);
		}
    }

	private void processCategory(String resourceName, String category,
			HashMap<Integer, Integer> puzzleCounts, Random random)
			throws IOException, FileNotFoundException, DocumentException
	{
		List<String> messages = parseMessages(resourceName);
		for (String message : messages)
		{
			Puzzle puzzle = new Puzzle(message);
			List<String> triples = puzzle.getTriples();
			boolean isUsable = 
				triples != null &&
				triples.size() > 0 &&
				triples.size() < 100;
			if (isUsable)
			{
				Integer puzzleCount = 
					getPuzzleCount(puzzleCounts, triples);
				
				shuffle(triples, random);
				String rootFileName = String.format(
						"output/%02d-%04d", 
						triples.size(),
						puzzleCount);
				String cardFileName = rootFileName + "cards.pdf";
				printCards(
						triples, 
						puzzle.getBlanks(),
						category,
						cardFileName, 
						random);
				String textFileName = rootFileName + "small.csv";
				printTextFile(
						triples, 
						puzzle.getBlanks(), 
						category, 
						textFileName);
				String solutionFileName = rootFileName + "solution.txt";
				printSolutionFile(message, solutionFileName);
			}
		}
	}
	
	private void printTextFile(
			List<String> triples,
			String blanks,
			String category,
			String fileName)
	throws FileNotFoundException
	{
		PrintWriter printer = new PrintWriter(fileName);
		try
		{
			printer.println("From the " + category + " category");
			printer.print(blanks);
			for (int i = 0; i < triples.size(); i++)
			{
				if (i % 5 == 0)
				{
					printer.println();
				}
				printer.print("\t" + triples.get(i));
			}
		}
		finally
		{
			printer.close();
		}
	}

	private void printCards(
			List<String> triples,
			String blanks,
			String category,
			String fileName, 
			Random random)
			throws FileNotFoundException, DocumentException
	{
		Document document = new Document(PageSize.LETTER);
		Font font = new Font(Font.COURIER, 200);
		PdfWriter writer = 
			PdfWriter.getInstance(document, new FileOutputStream(fileName));
		writer.setPageEvent(new PageHandler(fileName));
		try
		{
			document.open();
			Paragraph categoryParagraph = new Paragraph(
					"From the " + category + " category",
					new Font(Font.TIMES_ROMAN, 36));
			categoryParagraph.setAlignment(Paragraph.ALIGN_CENTER);
			document.add(categoryParagraph);
			
			document.add(new Paragraph(blanks, new Font(Font.COURIER, 50)));
			document.newPage();

			for (String triple : triples)
			{
				Paragraph p = new Paragraph(triple, font);
				p.setAlignment(Paragraph.ALIGN_CENTER);
				document.add(p);
				document.newPage();
			}
		}
		finally
		{
			document.close();
		}
	}

	private void shuffle(List<String> triples, Random random)
	{
		// shuffle the cards
		for (int i = 0; i < triples.size() * 8; i++)
		{
			int swapIndex = random.nextInt(triples.size());
			String swap = triples.get(swapIndex);
			triples.set(swapIndex, triples.get(0));
			triples.set(0, swap);
		}
	}
	
	private void printSolutionFile(String content, String fileName)
	throws FileNotFoundException
	{
		PrintWriter printer = new PrintWriter(fileName);
		try
		{
			printer.print(content);
		}
		finally
		{
			printer.close();
		}
	}

	private Integer getPuzzleCount(HashMap<Integer, Integer> puzzleCounts,
			List<String> triples)
	{
		Integer puzzleCount = puzzleCounts.get(triples.size());
		puzzleCount = 
			puzzleCount == null
			? 1
			: puzzleCount + 1;
		puzzleCounts.put(triples.size(), puzzleCount);
		return puzzleCount;
	}

	private List<String> parseMessages(String resourceName) throws IOException
	{
		StringWriter writer = new StringWriter();
		PrintWriter printer = new PrintWriter(writer);
		InputStream stream = getClass().getResourceAsStream(resourceName);
		BufferedReader reader = 
			new BufferedReader(new InputStreamReader(stream));
		try
		{
			String line;
			while ((line = reader.readLine()) != null)
			{
				printer.println(line);
			}
		}
		finally
		{
			reader.close();
		}
		Parser parser = new Parser();
		List<String> messages = parser.parse(writer.toString());
		return messages;
	}
}
