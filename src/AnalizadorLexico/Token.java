package AnalizadorLexico;

public class Token {
    private int nro_linea;
    private String id_token;
    private String lexema;

    public Token(String id_token, String lexema, int nro_linea){
        this.id_token = id_token;
        this.lexema = lexema;
        this.nro_linea = nro_linea;
    }

    public int get_nro_linea(){
        return nro_linea;
    }
    public String get_id_token(){
        return id_token;
    }
    public String get_lexema(){
        return lexema;
    }
}
