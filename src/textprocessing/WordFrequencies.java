package textprocessing;
import java.util.Map;
import java.util.HashMap;

/**
 * Un objeto WordFrequencies almacenará las palabras encontradas y sus correspondientes
 * frecuencias acumuladas correspondientes al conjunto de los ficheros ya procesados.
 */
public class WordFrequencies {
    private Map<String, Integer> frequencies;
    
    public WordFrequencies() {
        frequencies = new HashMap<>();
    }

    /**
     * Añade a la instancia WordFrequencies los pares palabras/frecuencias del mapa dado.
     *
     * @param f el mapa de pares palabra/frequencia a añadir
     */
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
