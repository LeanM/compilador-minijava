package Traductor;

public class Label_String_Generador {

    private int index;

    public static Label_String_Generador instance = null;

    public static Label_String_Generador getInstance() {
        if(instance == null)
            instance = new Label_String_Generador();

        return instance;
    }

    private Label_String_Generador(){
        index = 0;
    }

    public String get_label() {
        String toReturn = "lString_"+index;
        index++;
        return toReturn;
    }
}
