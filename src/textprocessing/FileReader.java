package textprocessing;

/**
 * Hilo que se encarga iterativamente de obtener un nombre de fichero
 * de un FileNames, leer su contenido y almacenarlo en un FileContents.
 * Termina si el FileNames le devuelve null.
 */
public class FileReader extends Thread {
    private FileNames fn;
    private FileContents fc;
    
    public FileReader(FileNames fn, FileContents fc){
        this.fn = fn;
        this.fc = fc;
    }
    
    public void run() {
        try {
            fc.registerWriter();
            while(true) {
                String name = fn.getName();
                if(name == null) {
                    fc.unregisterWriter();
                    break;
                }
                fc.addContents(Tools.getContents(name));
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
