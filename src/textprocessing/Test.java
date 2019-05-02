package textprocessing;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
    public static void check(WordFrequencies wf, String path) {
        if(wordFreqWordCount(wf.getFrequencies()) == filesWordCount(path)) {
            System.out.println("El procesador de texto funciona correctamente"
                    + "\n - Número total de palabras del WordFrequencies: " + filesWordCount(path)
                    + "\n - Número total de palabras que no superan el máximo en los ficheros: " + filesWordCount(path));
        } else {
            System.out.println("La cantidad de palabras que maneja el procesador de texto " 
                    + wordFreqWordCount(wf.getFrequencies()) + " y la cantidad de palabras "
                    + "que no superan el máximo de caracteres en los ficheros " 
                    + filesWordCount(path) + " no es el mismo");
        }
    }
    
    private static int filesWordCount(String path) {
        List<String> dirNames = new ArrayList<>();
        Tools.getDirNames(dirNames, path);
        List<String> contents = new ArrayList<>();
        for(String name : dirNames) {
            String dirContent = Tools.getContents(name);
            contents.add(dirContent);
        }
        return processData(contents);
    }
    
    private static int processData(List<String> data) {
        int count = 0;
        Pattern p = Pattern.compile("[a-zA-Z_0-9áéíóúüÁÉÍÓÚÜ]+");
        for(String dirData : data) {           
            Matcher m = p.matcher(dirData);
            while(m.find()) {
                count++;
            }
        }
        return count;
    }
    
    private static int wordFreqWordCount(Map<String,Integer> wf){
        int count = 0;
        for(Map.Entry<String, Integer> entry : wf.entrySet()) {
            Integer value = entry.getValue();
            count += value;
        }
        return count;
    }
}
