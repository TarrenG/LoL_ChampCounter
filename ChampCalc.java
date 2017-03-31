/*
<<<<<<< HEAD
Author: Tarren Grimsley
LoL champion game calc
*/

import java.io.*;
import java.lang.*;
import java.util.*;

public class ChampCalc {
   static String dir = "game_logs/";//"C:\\Riot Games\\League of Legends\\Logs\\Game - R3d Logs";
   static TreeMap<String, Integer> champSet = new TreeMap<String, Integer>();
   static TreeMap<String, Integer> winRate = new TreeMap<String, Integer>();
   public static final String NEW_LINE = System.getProperty("line.separator");

   public static void main(String[] args){
      String sumName = getName();

      switch( sumName ){
        case ".exit":
          System.exit(1);
          break;
        case ".help":
          System.out.println("type .exit to close \ntype .changedir to switch folder location \ntype .curdir to see current directory for logs");
          main(args);
          break;
        case ".changedir":
          Scanner directory_input = new Scanner(System.in);
          System.out.println("type in new location of directory (ex C:\\Riot Games\\ League of Legends\\Logs\\Game - R3d Logs)");
          dir = directory_input.nextLine();
          main(args);
          break;
        case ".curdir":
          System.out.println("current directory for Logs is "+dir);
          main(args);
          break;
      }

      System.out.println("Working...");
      int count = fileCount(dir, sumName);
      printOutput(champSet);
      System.out.println("number of files: "+ count);
      champSet.clear();
      winRate.clear();
      main(args);
   }

   static public String getName(){
      Scanner input = new Scanner(System.in);
      System.out.print("please input Summoner name (type .help for options): ");
      String sumName = input.nextLine();
      //System.out.println("name length: " + sumName.length());
      return sumName;

   }

   static public int fileCount(String dir, String sumName){
      int count = 0;
      File champs = new File(dir);
      String[] list = champs.list();
      for(String string : list){
         count += 1;
         try {
            Scanner reader = null;
            boolean alreadyFound = false;
            reader = new Scanner(new File(dir+string)); // "C:\\Riot Games\\League of Legends\\Logs\\Game - R3d Logs\\"
            boolean foundSum = false;
            while(reader.hasNextLine() && !alreadyFound){

              String curr = reader.nextLine();
              String champName = "";

               if(curr.contains("Hero") && curr.toLowerCase().contains("for "+sumName.toLowerCase()) && (curr.length() - sumName.length()) == curr.toLowerCase().indexOf(sumName.toLowerCase()) ){
                  foundSum = true;
                  //int start = 62;
                  int start = curr.indexOf("Hero ")+5;
                  /* cut out relevant champ name, 13 is 'Hero created ' length */
                  int end = curr.length() - (13+sumName.length());
                  champName = curr.substring(start, end);

                  if (champName.contains(")")) {
                    int len = champName.length() - 3;
	                  champName = champName.substring(0,len);
                  }

                  /* updating TreeMap */
                  if(champSet.containsKey(champName)){
                     int playcount = champSet.get(champName);
                     champSet.put(champName,playcount+1);
                  }
                  else{
                     champSet.put(champName, 1);
                  }
                //  System.out.println("found one game where "+ sumName +" played "+ champName);
               }
               /* keep track of game outcomes */
               if (curr.contains("exit_code") && foundSum  ){
                 String outcome = curr.split("exit_code\":\"")[1];
                 outcome = outcome.substring(0, outcome.length() - 2);
                 if (winRate.containsKey(outcome)){
                   int cnt = winRate.get(outcome);
                   winRate.put(outcome, cnt+1);
                 }
                 else{
                    winRate.put(outcome, 1);
                 }
                 /* short circuit rest of file since we dont care about it */
                 alreadyFound = true;
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
     int max = 0;
     int total = 0;
     String fav = "none";
      for(Map.Entry<String,Integer> entry : champSet.entrySet()){
         String key = entry.getKey();
         Integer played = entry.getValue();
         if (played > max){
           max = played;
           fav = key;
         }
	 total += played;
         System.out.println(key+" played "+played+" time(s)");
      }
      System.out.println("Favorite champion is "+fav+", played "+max+" times.");
      System.out.println("Total games played: "+total);
      System.out.println("Different champions played: "+champSet.size());
      int wins = 0;
      int losses = 0;
      int abandons = 0;
      int none = 0;
      for (Map.Entry<String, Integer> entry : winRate.entrySet()) {
        String key = entry.getKey();
        switch (key) {

          case "EXITCODE_WIN":
            wins = entry.getValue();
            break;

          case "EXITCODE_LOSE":
            losses = entry.getValue();
            break;

          case "EXITCODE_ABANDON":
            abandons = entry.getValue();
            break;

          case "EXITCODE_NONE":
            none = entry.getValue();
            break;
        }

      }
      System.out.println("Game Outcomes (not accurate for games older than 2013~): Wins "+ wins+" losses "+losses+" abandons "+abandons+" none "+none);
      System.out.printf("Winrate: %.2f%% \n",(float)wins/(float)(losses+wins)*100);
   }


}
