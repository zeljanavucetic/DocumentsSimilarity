/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.similaritydoc;

import com.textrazor.AnalysisException;
import com.textrazor.NetworkException;

import java.util.*;
import java.io.*;



/**
 *
 * @author DM
 */
public class Main {
     
    public static void main(String[] args) throws IOException, NetworkException, NetworkException, AnalysisException{
        
        String text1 = "Barclays misled shareholders and the public about one of the biggest investments in the bank's history, a BBC Panorama investigation has found.\n" +
"The bank announced in 2008 that Manchester City owner Sheikh Mansour had agreed to invest more than £3bn.\n" +
"But the BBC found that the money, which helped Barclays avoid a bailout by British taxpayers, actually came from the Abu Dhabi government.\n" +
"Barclays said the mistake in its accounts was \"a drafting error\".\n" +
"Unlike RBS and Lloyds TSB, Barclays narrowly avoided having to request a government bailout late in 2008 after it was rescued by £7bn worth of new investment, most of which came from the gulf states of Qatar and Abu Dhabi.\n" +
"Half of the cash was supposed to be coming from Sheikh Mansour.";
        String text2 = "US President Donald Trump has met Pope Francis at the Vatican for a short private audience on the third leg of his overseas trip.\n" +
"He arrived for the meeting along with his wife Melania, daughter Ivanka and son-in-law Jared Kushner.\n" +
"The meeting was keenly awaited as the two men have already clashed at a distance on issues including migration and climate change.\n" +
"Mr Trump is now due to meet Italy's president and prime minister.\n" +
"He will then fly to Brussels for a Nato summit.\n" +
"He earlier vowed to help Israelis and Palestinians achieve peace, as he ended the Middle East leg of his tour.";
        String text3 = "The bank said it subsequently provided \"appropriate disclosure\" in three prospectuses that were issued the following day.\n" +
"But the disclosure was buried deep in the small print and said that Sheikh Mansour \"has arranged for his investment…to be funded by an Abu Dhabi governmental investment vehicle, which will become the indirect shareholder\".\n" +
"Barclays still used the phrase \"his investment\", even though it knew Sheikh Mansour was not actually investing in the bank at the time.\n" +
"The bank continued to mislead shareholders in its annual reports of 2008 and 2009, both of which identified Sheikh Mansour as the investor\n" +
"Barclays said the mistake in its accounts was \"simply a drafting error\" and that the information provided in the prospectuses was \"entirely appropriate in all the circumstances\".\n" +
"The bank also said: \"The shareholders meeting had already taken place and there was therefore no need to issue press releases or additional formal communications to shareholders/other market participants.\"";

         String API_KEY = "";
        
        Properties prop = new Properties();
	InputStream input = null;

        
	try {

		input = new FileInputStream("src/main/resources/config.properties");

		// load a properties file
		prop.load(input);
                
                    API_KEY =  prop.getProperty("API_KEY");  
        

	} catch (IOException ex) {
		ex.printStackTrace();
	} 

        
    List<String> documents = new ArrayList<String>();
    documents.add(text1);
    documents.add(text2);
    documents.add(text3);

 
       
        List <SimilarDocument> similarDocs = new ArrayList<SimilarDocument>();
        
        similarDocs = Functions.getFinalSimilarity(documents, API_KEY);
        
        System.out.println("Done");
   
        
    } 
}
