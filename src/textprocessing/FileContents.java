package textprocessing;
import java.util.Queue;
import java.util.LinkedList;

/**
 * Un FileContents se encarga de almacenar y suministrar ristras, cada una de
 * las cuales contendrá toda la información textual de uno de los ficheros.
 * El número de ristras, así como el tamaño total de estas, estarán limitados
 * en el momento de la construcción del objeto. Los hilos FileReader se
 * encargarán de ir añadiendo los datos al objeto, y los hilos FileProcessor
 * se encargarán de sacarlos. El objeto dispone de un mecanismo para saber
 * cuándo deja de ser funcional consistente en que los hilos FileReader indican
 * cuándo comienzan a producir contenido y cuándo terminan, de tal forma que el
 * objeto dejará de ser funcional cuando no quede ningún FileReader y no haya
 * contenido que devolver.
 */
public class FileContents {
    private Queue<String> queue;
    private int maxFiles;
    private int maxChars;
    private int queueSize;
    private int registerCount = 0;
    private boolean theresBeenActivity = false;
    private boolean closed = false;

    /**
     * Inicializa un objeto FileContents con el número máximo de ficheros que
     * puede almacenar y el tamaño total máximo de las ristras que puede
     * almacenar en un momento dado.
     *
     * @param maxFiles  número máximo de ficheros que puede almacenar
     * @param maxChars  tamaño máximo total de las ristras que puede almacenar en un momento dado
     */
    public FileContents(int maxFiles, int maxChars) {
        this.maxFiles = maxFiles;
        this.maxChars = maxChars;
        queue = new LinkedList<>();
    }

    /**
     * Indica que hay un nuevo FileReader usando el objeto.
     */
    public synchronized void registerWriter() {
        if(!closed) {
            registerCount++;
            theresBeenActivity = true;
        }
    }

    /**
     * Indica que un FileReader ha dejado de usar el objeto.
     * Cuando registerCount pase de 1 a 0 el objeto ya no recibirá más contenido
     */
    public synchronized void unregisterWriter() {
        registerCount--;
        if(theresBeenActivity && registerCount == 0) {
            closed = true;
        }
    }

    /**
     * Añade el contenido de un fichero al FileContents. Si se ha alcanzado el
     * límite de ficheros o fuera a superarse el máximo establecido para el
     * tamaño total de contenidos, el hilo se espera hasta que cambie la
     * situación. El límite de tamaño máximo de contenido no se aplica si no hay
     * contenido almacenado.
     *
     * @param contents
     */
    public synchronized void addContents(String contents) {
        while(queue.size() >= maxFiles || (queue.size() > 0 && queueSize + contents.length() >= maxChars)) {
            try {
                wait();
            } catch(InterruptedException e) {}
        }
        queueSize += contents.length();
        queue.add(contents);
        notifyAll();
    }

    /**
     * Extrae el contenido de un fichero. Si no hay contenido disponible, el
     * hilo solicitante espera a que lo haya, salvo que además el último
     * FileReader se ha desregistrado, en cuyo caso devuelve null.
     *
     * @return  el contenido de un fichero. Null si no queda contenido y el último FileReader se ha desregistrado
     */
    public synchronized String getContents() {
        while(queue.isEmpty()) {
            if(closed) {
                notifyAll();
                return null;
            }
            try {
                wait();
            } catch(InterruptedException e) {}
        }
        String contents = queue.remove();
        notifyAll();
        return contents;
    }
}
