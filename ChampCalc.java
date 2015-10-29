/*
Author: TG
LoL champion game calc
*/

import java.io.*;
import java.lang.*;
import java.util.*;

public class ChampCalc {
   
   static String dir = "C:\\Riot Games\\League of Legends\\Logs\\Game - R3d Logs\\";
   static TreeMap<String, Integer> champSet = new TreeMap<String, Integer>();
   
   public static void main(String[] args){
      String sumName = getName();
      int count = fileCount(dir, sumName);
      System.out.println("number of files: "+ count);
      printOutput(champSet);
   }
   static public String getName(){
      Scanner input = new Scanner(System.in);
      System.out.println("please input Summoner name: ");
      String sumName = input.nextLine();  
      System.out.println(sumName);
      System.out.println("name length: " + sumName.length());
      return sumName;
      
   }

   static public int fileCount(String dir, String sumName){
      int count = 0; 
      File champs = new File(dir);
      String[] list = champs.list();
      for(String string : list){
         count += 1;
         try{
            Scanner reader = null;
            reader = new Scanner(new File(dir+string)); 
            while(reader.hasNextLine()){
               String curr = reader.nextLine();
               String champName = "";
               if(curr.contains("created")&& curr.contains("Hero") && curr.contains("for") && curr.contains(sumName)){
                  int start = 62;
                  int end = 62 + (curr.length() - 85); 
                  champName = curr.substring(start, end); 
                  if(champSet.containsKey(champName)){
                     int playcount = champSet.get(champName);
                     champSet.put(champName,playcount+1);
                  }
                  else{
                     champSet.put(champName, 1);
                  }
                  System.out.println("found one game where "+ sumName +" played "+ champName);
               }
            }
               
               
         }
         catch(IOException a){
            System.out.println("IOException, system exiting");
            break;
         }
         
      }
      
      return count;  
   } 
   static public void printOutput(TreeMap<String,Integer> champSet){
      for(Map.Entry<String,Integer> entry : champSet.entrySet()){
         String key = entry.getKey();
         Integer played = entry.getValue();
         System.out.println(key+" played "+played+" time(s)");
         
      }
      
      
   }











}