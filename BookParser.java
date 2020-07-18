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
    public BookParser() throws FileNotFoundException{}

    public PriorityQueue<String []> fillQ(HashMap<String,Integer> map, String type){ // fills the queue according to the map of prepopulated values
        PriorityQueue<String[]> freqQ;
        if(type.equals("max")){
            freqQ = new PriorityQueue<String[]>(new maxHeap());;
        }else{
            freqQ = new PriorityQueue<String[]>(new minHeap());;
        }
        
        for(String x : map.keySet()){
            String val = map.get(x) + "";
            String [] temp = new String []{x, val};
            freqQ.add(temp);
        }
        return freqQ;
    }

    public static HashMap<String, HashMap<String,Integer>> parseWords(File f, Scanner sc){
        HashMap<String,HashMap<String,Integer>> map = new HashMap<String,Integer>();
        int currentChapter = 0;
        while(sc.hasNextLine()){
            String [] curr = sc.nextLine().split(" ");
            //curr = curr.toLowerCase();
            //curr = curr.replaceAll("\\p{Punct}", "");
            if(curr[0].equals("Chapter")){
                HashMap<String,Integer> temp = new HashMap<String,Integer>();
                map.put(curr[0] + curr[1],temp);
            }
            else{
                HashMap<String,Integer> currMap = map.get()
            }
            //if(map.containsKey(curr) && !curr.isBlank()){
            //    map.put(curr, map.get(curr) + 1);
            //}else if(!map.containsKey(curr) && !curr.isBlank() ){
            //    map.put(curr, 1);
            //}
        }
        return map;
    }
    public int getTotalNumberOfWords(){
        int sum = 0;
        for(String x : map.keySet()){
            sum += map.get(x);
        }
        return sum;
    }

    public int getTotalUniqueWords(){
        return map.size();
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


    public int get20MostInterestingFrequentWords(){
        return -1;
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

    public int getFrequencyOfWord(){
        return -1;
    }

    public int getChapterQuoteAppears(){
        return -1;
    }

    public int generateSentence(){
        return -1;
    }
    public static void main (String [] args) throws FileNotFoundException {
        BookParser b = new BookParser();
        System.out.println(b.getTotalUniqueWords());
        System.out.println(b.getTotalNumberOfWords());
        System.out.println(Arrays.deepToString(b.get20MostFrequentWords()));
        System.out.println();
        System.out.println(Arrays.deepToString(b.get20LeastFrequentWords()));
    }
}