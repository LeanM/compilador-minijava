package Traductor;

public class Index_etiquetas {

    private int index;

    public static Index_etiquetas instance = null;

    private Index_etiquetas() {
        index = 0;
    }

    public static Index_etiquetas getInstance(){
        if(instance == null)
            instance = new Index_etiquetas();

        return instance;
    }

    public int get_index() {
        return index++;
    }

    public void reset_index() {
        this.index = 0;
    }
}
