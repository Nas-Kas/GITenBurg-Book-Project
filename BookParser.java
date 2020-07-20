import java.util.*;
import java.io.*;

class minHeap implements Comparator<String []>{
    public int compare(String [] a, String [] b){
        int aVal = Integer.parseInt(a[1]);
        int bVal = Integer.parseInt(b[1]);
        if(aVal > bVal){
            return 1;
        }
        if(aVal < bVal){
            return  -1;
        }
        return 0;
    }
}

class maxHeap implements Comparator<String []>{
    public int compare(String [] a, String [] b){
        int aVal = Integer.parseInt(a[1]);
        int bVal = Integer.parseInt(b[1]);
        if(aVal > bVal){
            return -1;
        }
        if(aVal < bVal){
            return 1;
        }
        return 0;
    }
}

public class BookParser{
    File file = new File("pride.txt");
    Scanner sc = new Scanner(file);
    HashMap<String, HashMap<String,Integer>> map = parseWords(file, sc); // key will be chapter value will be a map key = word, value = frequency of word
    HashSet<String> commonWords = new HashSet<String>();
    String chunk = "";
    public BookParser() throws FileNotFoundException{
        File f = new File("frequentwords.txt");
        //File f2 = new File("pride.txt");
        Scanner s = new Scanner(f);
        int count = 0;
        while(s.hasNext() && count < 100){
            String next = s.next();
            commonWords.add(next);
            count++;
        }
        s.close();
        //s = new Scanner(f2);
        //while(s.hasNext()){
        //    String res = s.next();
        //    chunk += res;
        //}
        //s.close();
    }

    public void fillCommonwords(){

    }
    public PriorityQueue<String []> fillQ(HashMap<String,HashMap<String,Integer>> map, String type){ // fills the queue according to the map of prepopulated values
        PriorityQueue<String[]> freqQ;
        if(type.equals("max")){
            freqQ = new PriorityQueue<String[]>(new maxHeap());;
        }else{
            freqQ = new PriorityQueue<String[]>(new minHeap());;
        }
        HashMap<String,Integer> mergedMap = new HashMap<String,Integer>();
        for(String x : map.keySet()){
            HashMap<String,Integer> curr = map.get(x);
            for(String y : curr.keySet()){
                if(mergedMap.containsKey(y)){
                    mergedMap.put(y, curr.get(y) + mergedMap.get(y));
                }else{
                    mergedMap.put(y,curr.get(y));
                }
            }
        }
        for(String x : mergedMap.keySet()){
            String [] temp = new String [] {x,mergedMap.get(x) + ""};
            freqQ.add(temp);
        }
        return freqQ;
    }

    public static HashMap<String, HashMap<String,Integer>> parseWords(File f, Scanner sc){
        HashMap<String,HashMap<String,Integer>> map = new HashMap<String,HashMap<String,Integer>>();
        String currentChapter = "0";
        while(sc.hasNextLine()){
            String [] curr = sc.nextLine().split(" ");
            if(curr[0].equals("Chapter")){
                currentChapter = curr[1];
                map.put(curr[1], new HashMap<String,Integer>());
            }
            else if(curr.length > 0 && curr != null){
                for(int i = 0; i < curr.length; i++){
                    String currString = curr[i];
                    currString = currString.toLowerCase();
                    currString = currString.replaceAll("\\p{Punct}", "");
                    
                    if(!currString.isBlank() && map.get(currentChapter) != null && map.get(currentChapter).containsKey(currString)){
                        HashMap<String,Integer> temp = map.get(currentChapter);
                        //System.out.println("increment");
                        temp.put(currString, temp.get(currString) + 1);
                        map.put(currentChapter, temp);
                    }else if(currString != null && !currString.isBlank()){
                        HashMap<String,Integer> temp = map.get(currentChapter);
                        temp.put(currString,1);
                        map.put(currentChapter,temp);
                    }
                }
              }
            }
        return map;
    }

    public int getTotalNumberOfWords(){
        int sum = 0;
        for(String x : map.keySet()){
            HashMap<String,Integer> curr = map.get(x);
            for(String y : curr.keySet()){
                sum += curr.get(y);
            }
        }
        return sum;
    }

    public int getTotalUniqueWords(){
        HashSet<String> uniques = new HashSet<String>();
        for(String x: map.keySet()){
            HashMap<String,Integer> curr = map.get(x);
            for(String y: curr.keySet()){
                uniques.add(y);
            }
        }
        return uniques.size();
    }

    public String [] [] get20MostFrequentWords(){
        String [] [] res = new String [20] [2];
        PriorityQueue<String[]> freqQ = fillQ(map,"max");
        for(int i = 0; i < 20; i++){
            String [] temp = freqQ.poll();
            res[i][0] = temp[0];
            res[i][1] = temp[1];
        }
        return res;
    }


    public String [] [] get20MostInterestingFrequentWords(){
        String [] [] res = new String [20] [2];
        PriorityQueue<String []> freqQ = fillQ(map,"max");
        int count = 0;
        while(count < 20){
            String [] curr = freqQ.poll();
            if(!commonWords.contains(curr[0])){
                String [] temp = new String [] {curr[0],curr[1]};
                res[count] = temp;
                count++;
            }
        }
        return res;
    }

    public String [] [] get20LeastFrequentWords(){
        String [] [] res = new String [20] [2];
        PriorityQueue<String[]> freqQ = fillQ(map,"min");
        for(int i = 0; i < 20; i++){
            String [] temp = freqQ.poll();
            res[i][0] = temp[0];
            res[i][1] = temp[1];
        }
        return res;
    }

    public int [] getFrequencyOfWord(String word){
        int [] res = new int [61];
        int count = 0;
        for(String x : map.keySet()){
            HashMap<String,Integer> curr = map.get(x);
            res[count] = curr.get(word);
            count++;
        }
        return res;
    }

    public String getChapterQuoteAppears(String quote) throws FileNotFoundException{ // ridiculously bad runtime
        File f2 = new File("pride.txt");
        Scanner s = new Scanner(f2);
        while(s.hasNext()){
            String res = s.next();
            chunk += res;
        }
        s.close();

        String [] chunkArr = chunk.split("Chapter");
        quote = quote.replaceAll(" ", "");
        String chapter = "";
        for(int i = 0; i < chunkArr.length; i++){
            if(chunkArr[i].contains(quote)){
                chapter = chunkArr[i].substring(0,2);
                if(Character.isDigit(chapter.charAt(1))){
                    return "Chapter " + chapter;
                }else{
                    return "Chapter " + chapter.substring(0,1);
                }
            }
        }
        return "String does not exist";
    }
/*
    public int generateSentence(){
        return -1;
    }
    */
    public static void main (String [] args) throws FileNotFoundException {
        BookParser b = new BookParser();
        //System.out.print(b.chunk);
        //System.out.println(b.getTotalUniqueWords());
        //System.out.println(b.getTotalNumberOfWords());
        //System.out.println(Arrays.deepToString(b.get20MostFrequentWords()));
        //System.out.println(Arrays.deepToString(b.get20MostInterestingFrequentWords()));
        //System.out.println(Arrays.toString(b.getFrequencyOfWord("the")));
        //System.out.println(Arrays.deepToString(b.get20LeastFrequentWords()));
        //System.out.println(b.getChapterQuoteAppears("But to live in ignorance on such a point was impossible; or at least it was impossible not to try for information."));
    }
}