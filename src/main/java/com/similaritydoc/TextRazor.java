/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.similaritydoc;

import com.textrazor.AnalysisException;
import com.textrazor.NetworkException;
import com.textrazor.annotations.AnalyzedText;
import com.textrazor.annotations.Entity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author DM
 */
public class TextRazor {
    
          public static  List<String> analyseText(String API_KEY, String text) throws NetworkException, AnalysisException{
            
            List<String> dictionary = new ArrayList<String>();
            
            com.textrazor.TextRazor client = new com.textrazor.TextRazor(API_KEY);
		
		//client.addExtractor("words");
		client.addExtractor("entities");
		client.addExtractor("topics");
		 
		client.setClassifiers(Arrays.asList("textrazor_newscodes"));
		
                AnalyzedText response = client.analyze(text);
		
		System.out.println(response.isOk());
		
		for (Entity entity : response.getResponse().getEntities()) {
			System.out.println("Entity:" + entity.getEntityId() + " " + entity.getMatchedText());
		}
		
		for (Entity entity : response.getResponse().getEntities()) {
                        dictionary.add(entity.getEntityId());
		}
//                for (String topic : dictionary) {
//			System.out.println("Topic:" + topic);
//                        
//		}
                
        return dictionary;
        }
    
}
