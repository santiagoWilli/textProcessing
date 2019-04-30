package textprocessing;
import java.util.*;
import java.io.*;
        
public class Main{
    final static int MAX_CHARS = 100 * 1024;

    public static void main(String[] args) throws InterruptedException {
        FileNames fileNames = new FileNames();
        FileContents fileContents = new FileContents(30, MAX_CHARS);
        WordFrequencies wordFrequencies = new WordFrequencies();
        
        FileReader fr1 = new FileReader(fileNames, fileContents);
        FileReader fr2 = new FileReader(fileNames, fileContents);
        FileProcessor fp1 = new FileProcessor(fileContents, wordFrequencies);
        FileProcessor fp2 = new FileProcessor(fileContents, wordFrequencies);
        FileProcessor fp3 = new FileProcessor(fileContents, wordFrequencies);
        
        fr1.start();
        fr2.start();
        fp1.start();
        fp2.start();
        fp3.start();
        
        Tools.fileLocator(fileNames, "datos");
        fileNames.noMoreNames();
        
        fr1.join();
        fr2.join();
        fp1.join();
        fp2.join();
        fp3.join();
        
        List<String> words = Tools.wordSelector(wordFrequencies.getFrequencies());
        
        for(String word : words) {
            System.out.println(word);
        }
        System.out.println("");
        
        Test.check(wordFrequencies, "datos");
    }
}

class Tools {
    public static void getDirNames(List<String> list, String path) {
        File dir = new File(path);
        if(!dir.exists()) {
            return;
        }
        if(dir.isDirectory()) {
            String[] dirList = dir.list();
            for(String name : dirList) {
                if(name.equals(".") || name.equals("..")) {
                    continue;
                }
                getDirNames(list, dir + File.separator + name);
            }
        } else {
            list.add(dir.getAbsolutePath());
        }
    }
    
    public static void fileLocator(FileNames fn, String dirname){
        List<String> names = new ArrayList<>();
        getDirNames(names, dirname);
        for(String name : names) {
            fn.addName(name);
        }
    }
    
    public static class Order implements Comparator< Map.Entry<String,Integer> > {
        @Override
        public int compare(Map.Entry<String,Integer> o1, Map.Entry<String,Integer> o2) {
            if ( o1.getValue().compareTo(o2.getValue()) == 0 ) {
                return o1.getKey().compareTo(o2.getKey());
            }
            return o2.getValue().compareTo(o1.getValue());
        }
    }
    
    public static List<String> wordSelector(Map<String,Integer> wf){
        Set<Map.Entry<String,Integer>> set = wf.entrySet();
        Set<Map.Entry<String,Integer>> orderedSet = new TreeSet<>(new Order());
        orderedSet.addAll(set);
        List<String> list = new LinkedList<>();
        int i = 0;
        for (Map.Entry<String,Integer> pair : orderedSet){
            list.add(pair.getValue() + " " + pair.getKey() );
            if (++i >= 10 ) break;
        }
        return list;
    }

    public static  String getContents(String fileName){
        StringBuilder text = new StringBuilder();
            try {
                FileInputStream fis = new FileInputStream(fileName);
                BufferedReader br = new BufferedReader(new InputStreamReader(fis, "ISO8859-1"));
                    String line;
                    while((line = br.readLine()) != null) {
                        text.append(line);
                        text.append("\n");
                    }
            } catch (IOException e) {
                return "Error: " + e.getMessage();
            }
        return text.toString();
    }
}
