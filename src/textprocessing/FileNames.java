package textprocessing;
import java.util.Queue;
import java.util.LinkedList;

/**
 * Un FileNames almacena temporalmente los nombres de ficheros a procesar.
 * El método Tools.fileLocator() se encarga de añadir nombres de ficheros,
 * y los hilos FileReader se encargan de consumirlos.
 */
public class FileNames {
    private Queue<String> queue;
    private boolean noMoreNames = false;
    
    public FileNames() {
        queue = new LinkedList<>();
    }

    /**
     * Almacena un nuevo nombre de fichero en un FileNames.
     *
     * @param fileName nombre de fichero a añadir
     */
    public synchronized void addName(String fileName) {
        if(!noMoreNames){
            queue.add(fileName);
            notifyAll();
        }
    }

    /**
     * Extrae y devuelve del FileNames un nombre de fichero. Si cuando no hay
     * ficheros disponibles, se ha llamado sobre el FileNames a noMoreNames(),
     * entonces devuelve null, lo cual indica que no se van a devolver más nombres
     *
     * @return  el nombre de un fichero. Null si no quedan más y se ha llamado a noMoreNames()
     */
    public synchronized String getName() {
        while(queue.isEmpty()) {
            if(noMoreNames) {
                notifyAll();
                return null;
            }
            try {
                wait();
            } catch(InterruptedException e) {}
        }
        String name = queue.remove();
        notifyAll();
        return name;
    }

    /**
     * Da lugar a que el FileNames no admita más nombres de fichero.
     */
    public void noMoreNames() {
        noMoreNames = true;
    }
}