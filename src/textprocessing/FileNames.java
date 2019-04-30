package textprocessing;
import java.util.Queue;
import java.util.LinkedList;

public class FileNames {
    private Queue<String> queue;
    private boolean noMoreNames = false;
    
    public FileNames() {
        queue = new LinkedList<>();
    }
    
    public synchronized void addName(String fileName) {
        queue.add(fileName);
        notifyAll();
    }
    
    public synchronized String getName() {

        while(queue.isEmpty()) {
            if(noMoreNames) {
                return null;
            }
            try {
                wait(10);
            } catch(InterruptedException e) {}
        }
        String name = queue.remove();
        notifyAll();
        return name;
    }
    
    public void noMoreNames() {
        noMoreNames = true;
    }
}