/*
 *  GoldFish.java
 *
 * Copyright (c) 2000-2012, The University of Sheffield.
 *
 * This file is part of GATE (see http://gate.ac.uk/), and is free
 * software, licenced under the GNU Library General Public License,
 * Version 3, 29 June 2007.
 *
 * A copy of this licence is included in the distribution in the file
 * licence.html, and is also available at http://gate.ac.uk/gate/licence.html.
 *
 *  Ricardo KAKA, 7/3/2014
 *
 * For details on the configuration options, see the user guide:
 * http://gate.ac.uk/cgi-bin/userguide/sec:creole-model:config
 */

package sheffield.creole.goldfish;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import gate.*;
import gate.creole.*;
import gate.util.*;


/** 
 * This class is the implementation of the resource GOLDFISH.
 */

public class GoldFish  extends AbstractLanguageAnalyser
  implements ProcessingResource {
	
	 // F:\\Gate\\gate-7.1-build4485-ALL\\
	 String path = "C:\\data\\";
	 int classwords[] = new int[5];
	 int totalvocab;
	 String FinalString[] = new String[5];
	 HashMap<String,Integer> positive = new HashMap();
	 HashMap<String,Integer> negative = new HashMap();
	 ArrayList tobeVerb = new ArrayList();
	 ArrayList<Integer> rating = new ArrayList<Integer>();
	        
	public void execute() throws ExecutionException{

		// Remove the indexes. 
/*
	  System.out.println("Tweets 2" + this.document.getAnnotations().get("Tweet"));
	  AnnotationSet annotSet = this.document.getAnnotations().get("Lookup");
	  Iterator i = annotSet.iterator();
	  while(i.hasNext()){
		Annotation annot = (Annotation)  i.next();
		if(annot.getFeatures().get("majorType").equals("jobtitle")){
			System.out.println("Special Annotation" + annot);
		} 
		
		
	  }
	  */
	try {
		Reading();
		TokenCounting();
		AnnotationSet annotationSet = this.document.getAnnotations().get("Tweet");
		
		Iterator iterator = annotationSet.iterator();
		while(iterator.hasNext()){
			Annotation annot = (Annotation)  iterator.next();
			
		  
			String txtData =(String) annot.getFeatures().get("Tweet");
			txtData = Normalization(txtData);
			if(txtData.equals("")){
				
			}else{
				
			    int rate = calculatefinalProbability(txtData);
			    ///------------------ see  Remove the below line
			    annot.getFeatures().put("rate", rate);
				System.out.println(txtData+" Probability = "+rate);
				//System.out.println("tinga");
				rating.add(rate);
			}
			
		}
		
	//	System.out.println(" langoor ");
				int  sum= 0 ;
		        int counting = 0; 
				
        		for(int index= 0 ;index < rating.size() ; index++){
        				sum = sum + rating.get(index);
        		}
  
    
//                      System.out.println("Rating ArrayList" + ratings.toString());
      
		      float avg = ((float)sum / rating.size()) ;
		 
		      // Adjust the value here a little bit
		      // Here i am counting 4 and 5. 
		      
		      int goodRating = 0;
		      int badRating = 0;
		      
		      Iterator ite = rating.iterator();
		      while(ite.hasNext()){
		           int rate =  (Integer)ite.next() ;
		           if(rate >3){
		               goodRating ++;
		           }else if(rate <=2){
		               badRating ++;
		           }
		      }
		      
		    
		      
		      
		      
		      if(rating.size() >= 25 && rating.size() <=35){
		    	  if((goodRating - badRating) >=15){
		    		  avg = avg + 1;
		    	  }
		    		  
		    	  
		      }else if(rating.size()>35 && rating.size()<=50){
		    	  if((goodRating - badRating) >=30){
		    		  avg = avg + 1;
		    	  }
		    	  
		      }else if(rating.size()>50){
		    	  if((goodRating - badRating) >=35){
		    		  avg = avg + 1;
		    	  }
		      }
		      
		      if(avg>=5)
		    	  avg=5 ;
		      
		      System.out.println("------------------------------------------");
		      System.out.println("                                          ");
		      System.out.println("                                          ");
		      System.out.println("Average of Rating of Movie is  = " + avg);
		      System.out.println("                                          ");
		      System.out.println("                                          ");
		      System.out.println("------------------------------------------");
		      
		     // this.document.getFeatures().put("Tweet1", avg);
		  
		      // --------- uncomment below three lines
		      /*
		      FeatureMap  fmap = this.document.getFeatures();
		      fmap.put("Tweet1", avg);
		      this.document.getAnnotations().add( this.document.getAnnotations().firstNode(), this.document.getAnnotations().lastNode(),"Tweet1", fmap);
		       */
		   //   FeatureMap map =(FeatureMap) this.document.getFeatures().get("Tweet1");
		     //  map.put("Tweet1", avg);
		      //this.document.getAnnotations().add(annotationSet2.firstNode(), annotationSet2.lastNode(), "Tweet1", map);
		
	//	String txtData =Normalization("This is bad movie");
		
		
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  
	  
	}
	
	public String Normalization(String text){
		StringTokenizer str = new StringTokenizer(text," ");
        String temp = "" ;
        boolean process = false;
        while(str.hasMoreTokens()){
            text = str.nextToken();
            if(positive.get(text)!= null || negative.get(text) != null){
                process = true;
            }
            if(text.startsWith("http://") || text.startsWith("@")){
                text ="";
            }else{
                text = text.replaceAll("[^A-Za-z0-9 ]","");
            }
            temp = temp + " " + text ;
            
        }
        
        if(process)
        	return temp;
        else
        	return "";
        
        
		
	}
	
	
	
	
	
	public void Reading() throws Exception{
		FileReader fr = new FileReader(new File(path+"positive-words.txt"));
        BufferedReader br = new BufferedReader(fr);
        String newLine= "";
        while((newLine= br.readLine())!= null){
            
            StringTokenizer stt = new StringTokenizer(newLine," ");
            String tokens = (String)stt.nextToken();
            int rate =Integer.parseInt(stt.nextToken());
            positive.put(tokens, rate);
            
        }
        FileReader fri = new FileReader(new File(path+"negative-words.txt"));
        BufferedReader bri = new BufferedReader(fri);
         while((newLine= bri.readLine())!= null){
            negative.put(newLine,2);
        }
         
         FileReader frii = new FileReader(new File(path+"tobeverb.txt"));
         BufferedReader brii = new BufferedReader(frii);
         while((newLine= brii.readLine())!= null){
             tobeVerb.add(newLine);
         }
		
	}
	
    
   public void TokenCounting() throws FileNotFoundException, IOException{
        File a[] = new File[]{new File(path+"class 1.txt"),new File(path+"class 2.txt"),new File(path+"class 3.txt"),new File(path+"class 4.txt"),new File(path+"class 5.txt")};
        for(int i = 0 ; i < a.length ; i++){
        FileReader fr = new FileReader(a[i]);
        BufferedReader br = new BufferedReader(fr);
        ArrayList uniqueness = new ArrayList();
        FinalString[i] = "" ;
        String string2; 
        String tempstring="";
        int uniquecount = 0;
        while((string2 = br.readLine())!= null){
             FinalString[i]  =  FinalString[i] + string2;
             StringTokenizer str = new StringTokenizer(FinalString[i]," ");
             while(str.hasMoreTokens()){
             tempstring = str.nextToken();
             if(!uniqueness.contains(tempstring)){
                 uniqueness.add(string2);
                 uniquecount++ ;
             }
            }
        }
         classwords[i] = uniquecount;
         System.out.println("unique count"+ uniquecount);
         totalvocab = totalvocab + uniquecount ;
       }System.out.println("total voacb"+totalvocab);
        
   }
	
   public int calculatefinalProbability(String s){
       System.out.println(classwords[0]);
       double maxprobability = 0  ;
       int estimatedrate = 0 ;
       float finalestimate = 0;
        ArrayList str  =new ArrayList();
        int tokencount = 0;
        String previousWord = "" ;
        StringTokenizer strr = new StringTokenizer(s," ");
        while(strr.hasMoreTokens()){
            String process = strr.nextToken();
            str.add(process);
       if(positive.containsKey(process)){
                estimatedrate = positive.get(process) + estimatedrate ;
                tokencount++ ;
                
                if(tobeVerb.contains(previousWord)){
                    System.out.println("The sentence is negative");
                    return 1;
                }
               //  System.out.println("Here");
            }if(negative.containsKey(process)){
                estimatedrate = negative.get(process) + estimatedrate ;
                tokencount++ ;
            }
            
            previousWord = process;
        }
        
        finalestimate = (float)estimatedrate / tokencount ;
        int result = 0;
        
       for(int i = 0 ; i < 5 ; i++){
         double probability = 0   ;  
         for(int j=0 ; j <str.size() ; j++){  
           
            int count = 0 ;
            int startIndex = FinalString[i].indexOf((String)str.get(j));
            while (startIndex != -1) {
                count++;
                startIndex = FinalString[i].indexOf((String)str.get(j), 
                                  startIndex +str.get(j).toString().length());
            }
                probability =probability + Math.log((double)(count+1)/(double)(classwords[i]+totalvocab));
           }
         
           if(i == 0){
             maxprobability = probability;
            }
            if(probability > maxprobability){
                maxprobability = probability ;
              result = i ;
         }
       }
       
        if(finalestimate >= 3 && (result+1) <=2 ){
           if(finalestimate > 3){
               result = 3;
           }
       }
   //   System.out.println("Rating is :"+(result+1));
       
        return result+1 ;
   }
   
   
   
	
	


} // class GoldFish
