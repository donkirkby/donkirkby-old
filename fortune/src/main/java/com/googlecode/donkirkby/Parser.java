package com.googlecode.donkirkby;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class Parser
{

	public List<String> parse(String input)
	{
		try
		{
			ArrayList<String> messages = new ArrayList<String>();
			StringWriter currentMessage = new StringWriter();
			PrintWriter printer = new PrintWriter(currentMessage);
			BufferedReader reader = new BufferedReader(new StringReader(input));
			try
			{
				String line;
				do
				{
					line = reader.readLine();
					if (line == null || line.trim().equals("%"))
					{
						if (currentMessage.getBuffer().length() > 0)
						{
							messages.add(currentMessage.toString());
						}
						currentMessage.getBuffer().setLength(0);
					} else
					{
						if (currentMessage.getBuffer().length() > 0)
						{
							printer.println();
						}
						printer.print(line);
					}
				}while (line != null);
			}
			finally
			{
				reader.close();
			}
			
			return messages;
		} catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}
}
