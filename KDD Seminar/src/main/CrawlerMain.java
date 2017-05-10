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
	public static ArrayList<String> predicateList = new ArrayList<String>();

	public static void main(String[] args) {


		storeTxtToList();
		
		System.out.println("***** STORED TO LIST ******");
		
		Iterator<RFDEntry> iterator = entries.iterator();
		
		while(iterator.hasNext()) {
			
			RFDEntry entry = iterator.next();
			
			//System.out.println("Check " + entry.getPredicate());
			
			if(!predicateList.contains(entry)) {
				
				predicateList.add(entry.getPredicate());
				
			} else {
				
				System.out.println("Duplicate: " + entry.getPredicate());
				
			}
			
		}
		
		//Predicate ausgeben
		Iterator<String> iteratorPredicate = predicateList.iterator();
		
		while(iteratorPredicate.hasNext()) {
			
			System.out.println(iteratorPredicate.next());
			
		}
		
		

	}
	
	public static String readFile(String path, Charset encoding) 
			  throws IOException 
	{
	  byte[] encoded = Files.readAllBytes(Paths.get(path));
	  return new String(encoded, encoding);
	}
	
	public static void storeTxtToList() {
		
		String fileName = "lwapis_v1.txt";
		
		try {
			String content = readFile(fileName, Charset.forName("UTF-8"));
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