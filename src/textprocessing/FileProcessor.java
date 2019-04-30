package textprocessing;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class FileProcessor extends Thread {
    private FileContents fc;
    private WordFrequencies wf;
    
    public FileProcessor(FileContents fc, WordFrequencies wf){
        this.fc = fc;
        this.wf = wf;
    }
    
    public void run() {
        try {
            while(true) {
                String content = fc.getContents();
                if(content == null) {
                    break;
                }
                Map<String, Integer> frequencies = processData(content);
                wf.addFrequencies(frequencies);
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    private Map<String, Integer> processData(String data) {
        Map<String, Integer> frequencies = new HashMap<>();
        Pattern p = Pattern.compile("[a-zA-Z_0-9áéíóúüÁÉÍÓÚÜ]+");
        Matcher m = p.matcher(data);
        List<String> words = new ArrayList<>();
        while(m.find()) {
            words.add(m.group());
        }
        for(String word : words) {
            Integer freq = frequencies.get(word);
            frequencies.put(word, (freq == null) ? 1 : freq + 1);
        }
        return frequencies;
    }
}
