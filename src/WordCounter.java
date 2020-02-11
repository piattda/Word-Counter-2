import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import components.map.Map1L;
import components.queue.Queue;
import components.queue.Queue1L;
import components.set.Set;
import components.set.Set1L;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

/**
 * A program to read in a file and print out a formatted HTML file of the 
 * occurances of each word in the file.
 * 
 * 
 * @author David Piatt
 * 
 */
public final class WordCounter {

    /**
     * 
     * Comparitor class to compare the queue to alphabetize it. 
     *
     *
     */
    
    public static class PairComparitor implements Comparator<Map.Entry<String,Integer>>{
        
        
        @Override
        public int compare(Map.Entry<String, Integer> arg0, Map.Entry<String, Integer> arg1) {
            return arg0.getKey().compareToIgnoreCase(arg1.getKey());
        }
        
    }
    
    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private WordCounter() {
    }


    /**
     * Make a map of the input file to the occurances of each individual word. 
     * 
     * @param filename
     *  file name for the input file
     * 
     * @param input
     *  A SimpleReader to read in the file
     * 
     * 
     * 
     * 
     * 
     * @ensures [map contains words and occurances]
     */
    
    public static Map<String, Integer> getCountMap(BufferedReader input) {
        
        //All of the terms are mapped with their occurances using nextWordOrSeparator. 
        
        int index = 0;
        
        //decalre a map. 
        Map<String, Integer> countMap = new HashMap<String,Integer>();
        
        String separators = " ,.?/!-\t&:;";
        
        
        String s = "";
        String r = "";
        try {
            
            r = input.readLine();
            
            while(r != null){
                
                s += r;
                r = input.readLine();
                
            }
        } catch (IOException e) {
            System.out.println("Error reading in getCountMap");
        }
        
       //go through the string that is the whole document. 
        //if the term is not in the map add it and set occurance to one
        //else increment occurance of term
        while(index < s.length()-1){
            String nextWord = nextWordOrSeparator(s,index);
            index += nextWord.length();
            if(!countMap.containsKey(nextWord) && !separators.contains(nextWord.substring(0,1))){
                countMap.put(nextWord, 1);
            }else if(!separators.contains(nextWord.substring(0,1))){
                countMap.replace(nextWord, countMap.get(nextWord) + 1);
            }
        }
        
        return countMap;

    }
    
    

    


    /**
     * Returns the first "word" (maximal length string of characters not in
     * {@code separators}) or "separator string" (maximal length string of
     * characters in {@code separators}) in the given {@code text} starting at
     * the given {@code position}.
     * 
     *
     * @param text
     *            the {@code String} from which to get the word or separator
     *            string
     * @param position
     *            the starting index
     * @param separators
     *            the {@code Set} of separator characters
     * @return the first word or separator string found in {@code text} starting
     *         at index {@code position}
     * @requires 0 <= position < |text|
     * @ensures <pre>
     * nextWordOrSeparator =
     *   text[position, position + |nextWordOrSeparator|)  and
     * if entries(text[position, position + 1)) intersection separators = {}
     * then
     *   entries(nextWordOrSeparator) intersection separators = {}  and
     *   (position + |nextWordOrSeparator| = |text|  or
     *    entries(text[position, position + |nextWordOrSeparator| + 1))
     *      intersection separators /= {})
     * else
     *   entries(nextWordOrSeparator) is subset of separators  and
     *   (position + |nextWordOrSeparator| = |text|  or
     *    entries(text[position, position + |nextWordOrSeparator| + 1))
     *      is not subset of separators)
     * </pre>
     */

    private static String nextWordOrSeparator(String text, int position) {
        
        
        
        final String separatorStr = " ,.?/!-\t";

        //set up separator set
        Set<Character> separators = new Set1L<>();
        
        separators.add(' ');
        separators.add(',');
        separators.add('.');
        separators.add('?');
        separators.add('/');
        separators.add('!');
        separators.add('-');
        separators.add('\t');
        separators.add('&');
        separators.add(':');
        separators.add(';');
        
        
        
        //return the next word or separator with toReturn;
        
        
        //checking the characters to see if they are separators, updating the position of  
        // next word or separator. 
        
        String toReturn = "";
        int index = position;
        char tmp = text.charAt(index);

        if(separators.contains(tmp) && index < text.length()){
            while(separators.contains(tmp) && index < text.length()){
                index++;
                if(index < text.length()){
                    tmp = text.charAt(index);
                    
                }
            }
        } else {
            while(!separators.contains(tmp)&& index < text.length()){
                index ++;
                if(index < text.length()){
                    tmp = text.charAt(index);

                }
            }
        }
        
        
        return text.substring(position, index);
    }


    /**
     * 
     * outputHTML will build the html webpage for the occurances of the words.
     * 
     * @param queue
     *  a queue of map objects
     * @param out
     *  out writer to print out the file information. 
     * @param fileName
     *  the file name to print to the screen.
     */

    public static void outputHTML(Queue<Map.Entry<String, Integer>> queue, SimpleWriter out, String fileName){
        //SimpleWriter out = new SimpleWriter1L(fileName+".html");

        //build html document. Iterate through queue and print out keys and values. 
        
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Words counted from: " + fileName + "</title>");

        out.println("<body>");
        out.println("<h2> Words counted from: " + fileName + "</h2>");
        out.println("<hr>");
        out.println("<table border = \"1\"");
        out.println("<tbody>");
        for(Map.Entry<String,Integer> x : queue){
            out.println("<tr>");
            out.println("<td>" + x.getKey().toString() + "</td>");
            out.println("<td>" + x.getValue().toString() + "</td>");
            out.println("</tr>");

        }
        out.println("</tbody>");
        out.println("</table>");
        out.println("<body>");
        out.println("</html>");
    }

    /**
     * getSortedQueue will sort the queue of maps by alphabetical order. It uses the comparator class
     * @param map
     *  the map of terms and their occurances.
     * @return
     *  Queue that is of sorted maps. 
     */
    
    public static Queue<Map.Entry<String,Integer>> getSortedQueue(Map<String,Integer> map){
       Queue<Map.Entry<String, Integer>> pairs = new Queue1L<>();
       
       //build the queue from the map
       
       for(Map.Entry<String, Integer> entry : map.entrySet()){
           pairs.enqueue(entry);
       }
          pairs.sort(new PairComparitor());
          return pairs;

    }
    
    /**
     * Main method.
     *  To read in a file and output a formatted HTML page of words and occurances. 
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
        SimpleReader in = new SimpleReader1L();
        SimpleWriter out = new SimpleWriter1L();

        out.println("Welcome to David Piatt's word counter!");
        
        out.println("");
        out.println("");
        
        //Get in file
        out.println("Please enter in a file name to count the words: ");
        String fileName = in.nextLine();
        BufferedReader newIn;
        try{
            newIn = new BufferedReader(new FileReader(fileName));
        }catch(IOException e){
            System.out.println("Error reading input file");
            return;
        }
        
        
        //get printing location
        out.println("Please enter an output file name: ");
        String outputFileName = in.nextLine();
        
        //create writer from new file
        SimpleWriter newOut = new SimpleWriter1L(outputFileName);
        
        //make reader to get the content in the file.
        //SimpleReader newIn = new SimpleReader1L(fileName);
        
        //make a map with the counted terms.
        Map<String, Integer> countMap = getCountMap(newIn);
        
        //now we make a queue of maps. then for the pair we can just call the key and value. We'll
        //sort the queue so everything is nice and alphabetical. God forbid there be no order.
        Queue<Map.Entry<String, Integer>> pairs = getSortedQueue(countMap);
        
                
        
        outputHTML(pairs, newOut, fileName);
        
        try {
            newIn.close();
        } catch (IOException e) {
            System.out.println("error closing newIn");
        }
        
        newOut.close();
        in.close();
        out.close();
    }
    
    
    

}
