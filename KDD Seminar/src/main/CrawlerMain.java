package main;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;

public class CrawlerMain {
	
	public static ArrayList<RFDEntry> entries = new ArrayList<RFDEntry>();

	public static void main(String[] args) {


		storeTxtToList();
		
		
		

	}
	
	public static String readFile(String path, Charset encoding) 
			  throws IOException 
	{
	  byte[] encoded = Files.readAllBytes(Paths.get(path));
	  return new String(encoded, encoding);
	}
	
	public static void storeTxtToList() {
		
		String path = "C:\\Users\\Jan-Peter Schmidt\\Dropbox\\Privat\\Uni\\Master\\3. Semester\\Data-mining and knowledge-discovery\\";
		String fileName = "lwapis_v1.txt";
		
		try {
			String content = readFile(path + fileName, Charset.forName("UTF-8"));
			String[] lines = content.split("\\r\\n|\\n|\\r");
			
			System.out.println("Found " + lines.length + " lines.");
			
			int counter = 0;
			
			for(String line:lines) {
				
				int arrayCounter = 0;
				int beginIndex = 0;				
				
				String[] parts = new String[4];
				
				if(line.contains("\"")) {
					
					//split complex triples
					boolean splitabble = true;
					for(int i=0; i<line.length();i++) {
						
						if(line.charAt(i)=='"' && line.charAt(i-1)!='\\') {
							
							//Toogle SplitNot
							splitabble = !splitabble;
							
						}
						
						if(line.charAt(i)==' ' && splitabble) {
							
							parts[arrayCounter++] = line.substring(beginIndex, i);
							beginIndex = i+1;
						}
						
					}

				} else {
					parts = line.split(" ");
				}
				
				entries.add(new RFDEntry(parts[0],parts[1],parts[2]));
				
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
	}

}