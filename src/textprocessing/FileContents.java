package textprocessing;
import java.util.Queue;
import java.util.LinkedList;

public class FileContents {
    private Queue<String> queue;
    private int maxFiles;
    private int maxChars;
    private int registerCount = 0;
    private boolean theresBeenActivity = false;
    private boolean closed = false;
    
    public FileContents(int maxFiles, int maxChars) {
        this.maxFiles = maxFiles;
        this.maxChars = maxChars;
        queue = new LinkedList<>();
    }
    
    public synchronized void registerWriter() {
        if(!closed) {
            registerCount++;
            theresBeenActivity = true;
        }
    }
    
    public synchronized void unregisterWriter() {
        registerCount--;
        if(theresBeenActivity && registerCount == 0) {
            closed = true;
        }
    }
    
    public synchronized void addContents(String contents) {
        while(queue.size() >= maxFiles) {
            try {
                wait();
            } catch(InterruptedException e) {}
        }
        if(contents.length() <= maxChars) {
            queue.add(contents);
        }
        notifyAll();
    }
    
    public synchronized String getContents() {
        if(closed) {
            return null;
        }
        while(queue.isEmpty()) {
            if(closed) {
                return null;
            }
            try {
                wait(10);
            } catch(InterruptedException e) {}
        }
        String contents = queue.remove();
        notifyAll();
        return contents;
    }
}
