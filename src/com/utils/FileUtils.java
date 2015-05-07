package com.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtils
{
	private static final String LINE_SEPORATOR = System.getProperty("line.separator");
	
	public static void writeFile(String fileName, String Content)
	{
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName)))
		{
			bw.write(Content);
		}
		catch (FileNotFoundException e)
		{
			System.out.println("Файл не найден.");
		}
		catch (IOException e)
		{
			System.out.println("Произошла ошибка I/O");
		}
	}
	
	public static String readFile(String fileName)
	{
		StringBuilder sb = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new FileReader(fileName)))
		{
			String s = null;
			boolean firstLine = true;
	        while ((s = br.readLine()) != null)
	        {
	        	if (!firstLine)
	        		sb.append(LINE_SEPORATOR);
	        	else
	        		firstLine = false;
	        	sb.append(s);
	        }
		}
		catch (FileNotFoundException e)
		{
			System.out.println("Файл не найден.");
		}
		catch (IOException e)
		{
			System.out.println("Произошла ошибка I/O");
		}
		
        return sb.toString();
	}
}
