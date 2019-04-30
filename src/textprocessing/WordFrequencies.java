package textprocessing;
import java.util.Map;
import java.util.HashMap;

public class WordFrequencies {
    private Map<String, Integer> frequencies;
    
    public WordFrequencies() {
        frequencies = new HashMap<>();
    }
    
    public synchronized void addFrequencies(Map<String,Integer> f){
        for(Map.Entry<String, Integer> entry : f.entrySet()) {
            String word = entry.getKey();
            Integer freq = entry.getValue();
            Integer thisFreq = frequencies.get(word);
            frequencies.put(word, (thisFreq == null) ? freq : thisFreq + freq);
        }
    }
    
    public Map<String,Integer> getFrequencies(){
        return frequencies;
    }
}
