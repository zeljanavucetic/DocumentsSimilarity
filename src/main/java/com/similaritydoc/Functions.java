/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.similaritydoc;

import static com.similaritydoc.TextRazor.analyseText;
import com.textrazor.AnalysisException;
import com.textrazor.NetworkException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

/**
 *
 * @author DM
 */
public class Functions {
    
    public static List<List<String>> getTextRazorEntities (List<String> documents, String  API_KEY) throws NetworkException, AnalysisException {
    
               List<List<String>> dictionaries = new ArrayList<List<String>>();
               
               for (int i = 0; i<documents.size(); i++) {
                dictionaries.add(analyseText (API_KEY, documents.get(i)));
               }
                         
                         return dictionaries;
    } 
    
    public static List<String> createMainDictionary(List<List<String>> dictionaries) {
        
                List<String> mainDictionary = new ArrayList<String>();
                
                //create main dictionary of unique terms using the dictionary of each document
		for (List<String> dic : dictionaries) {
                      for (String entity : dic) {
			
			    if (!mainDictionary.contains(entity)) {
			    	mainDictionary.add(entity);
			    }	
		}
		}
              
		return mainDictionary;
	
	}//createMainDictionary
    
    
    public static RealMatrix createBasicMatrix(List<String> mainDictionary,List<List<String>> dictionaries) {
        
		RealMatrix basicMatrix;
                //create matrix with where number of columns is number if documents and number of rows is number of unique entities in dictionary
		basicMatrix = MatrixUtils.createRealMatrix(mainDictionary.size(), dictionaries.size());
                //Term Frequency in the document
		double tf = 0;
                int numberOfColumn = 0;
                
                //indicate how many times each term appears in each (dictionary of the document)
                for (List<String> dic : dictionaries) {
                    
                    for (int i = 0; i<mainDictionary.size(); i++) {
                        
                        for (String entity : dic) {
			  if (mainDictionary.get(i).equalsIgnoreCase(entity)) {
                              tf++;
                          }	
                        }
                        basicMatrix.setEntry(i, numberOfColumn, tf/dic.size());
                        tf=0;
                    }
                    
                 numberOfColumn++;
                 
                }
                
		return basicMatrix;
	}//createBasicMatrix
    
 
    	public static RealMatrix getTfIdf(RealMatrix matrix) {
        //Term Frequency
        double tf;
        //Inverse Document Frequency
        double idf;
        double ndf;
        int numDocumentsWithTheTerm = 0;
        
        //for each term in the dictionary (row of the matrix) calculate tfidf
        for (int i = 0; i < matrix.getRowDimension(); i++) {
      
        //get number of documents with the term in it (get number of columns of the matrix)
        for (int j = 0; j < matrix.getColumnDimension(); j++) {
            if (matrix.getEntry(i, j) != 0) {
                numDocumentsWithTheTerm++;
            }
        }
        
       //calculate tf-idf
        for (int j = 0; j < matrix.getColumnDimension(); j++) {
                tf = matrix.getEntry(i, j);
                idf = Math.log(matrix.getColumnDimension()/numDocumentsWithTheTerm+1);
                matrix.setEntry(i, j, idf * tf);
            }
        numDocumentsWithTheTerm = 0;
        }

        return matrix;
    }//getTfIdf
    
    //get documents similarity using cosine measure
    public static double getCosineSimilarity(int doc1, int doc2, RealMatrix tfidfMatrix) {
        
//Let d1 = 10 5 3 0 0 0 0 0 0
//Let d2 = 5 0 7 0 0 9 0 0
//
//Cosine Similarity (d1, d2) = dot(d1, d2) / ||d1|| ||d2||
//
//dot(d1, d2) = (10)*(5) + (5)*(0) + (3)*(7) + (0)*(0) + (0)*(0) + (0)*(9) + (0)*(0) + (0)*(0) + (0)*() = 71
//
//||d1|| = sqrt((10)^2 + (5)^2 + (3)^2 + (0)^2 + (0)^2 + (0)^2 + (0)^2 + (0)^2 + (0)^2) = 11.5758369028
//
//||d2|| = sqrt((5)^2 + (0)^2 + (7)^2 + (0)^2 + (0)^2 + (9)^2 + (0)^2 + (0)^2) = 12.449899598
//
//Cosine Similarity (d1, d2) = 71 / (11.5758369028) * (12.449899598)
//                           = 71 / 144.118007202
//                           = 0.492651830109
        
        double similarity = 0;
        double sum = 0;
        double magnitude1 = 0;
        double magnitude2 = 0;

        for (int j = 0; j < tfidfMatrix.getRowDimension(); j++) {
            sum += tfidfMatrix.getEntry(j, doc1) * tfidfMatrix.getEntry(j, doc2);
            magnitude1 += Math.pow(tfidfMatrix.getEntry(j, doc1), 2);
            magnitude2 += Math.pow(tfidfMatrix.getEntry(j, doc1), 2);
        }
        similarity = sum / (Math.sqrt(magnitude1) * Math.sqrt(magnitude2));
        return similarity;
    }
    
   
    public static RealMatrix getTfIdfMatrix (List<String> documents, String  API_KEY) throws NetworkException, AnalysisException {
    
               List<List<String>> dictionaries = getTextRazorEntities(documents, API_KEY);
               List<String> dictionary = new ArrayList<String>();

        dictionary = Functions.createMainDictionary(dictionaries);
        RealMatrix basicMatrix;
        basicMatrix = Functions.createBasicMatrix ( dictionary, dictionaries);
        RealMatrix tfidfMatrix;
        tfidfMatrix = Functions.getTfIdf(basicMatrix);
        
        return tfidfMatrix;
    }
    
    public static List<SimilarDocument> getFinalSimilarity(List<String> documents, String  API_KEY) throws IOException, NetworkException, AnalysisException{
        
         RealMatrix topicMatrix;
         RealMatrix tfIdfMatrix;
         topicMatrix  = TopicModelMallet.getTopicDistribution(documents, 10);
         tfIdfMatrix = getTfIdfMatrix(documents, API_KEY);
       
        List <SimilarDocument> similarDocs = new ArrayList<SimilarDocument>();
        
        for (int i=0; i<topicMatrix.getColumnDimension(); i++){
             SimilarDocument doc = new SimilarDocument();
             doc.similarity = (Functions.getCosineSimilarity(0, i, topicMatrix) + Functions.getCosineSimilarity(0, i, tfIdfMatrix))/2;
             doc.name= "doc" + i;
             similarDocs.add(doc);
        } 
    
        return similarDocs;
    }
    
}
