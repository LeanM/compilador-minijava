package AnalizadorSemantico;

import AnalizadorLexico.Token;

public class EntradaAtributo {

    private Token token_atributo;
    private Tipo tipo_atributo;
    private String visibilidad_atributo;
    private String alcance;
    private int offset;

    public EntradaAtributo (Token token_atributo, String visibilidad_atributo, Tipo tipo_atributo, String alcance ){
        this.token_atributo = token_atributo;
        this.visibilidad_atributo = visibilidad_atributo;
        this.tipo_atributo = tipo_atributo;
        this.alcance = alcance;
        this.offset = 0;
    }

    public String getNombre(){
        return token_atributo.get_lexema();
    }
    public Tipo getTipo() { return tipo_atributo;}
    public Token get_token_atributo(){return token_atributo;}
    public String get_visibilidad() { return visibilidad_atributo;}
    public boolean es_estatico(){
        boolean toReturn = false;
        if (alcance.equals("static"))
            toReturn = true;

        return toReturn;
    }

    public void esta_bien_declarado() throws ExcepcionSemantica {
        if(!tipo_atributo.esPrimitivo()){
            Token clase_tipo = tipo_atributo.get_token_tipo();
            if(!TablaSimbolos.getInstance().clase_esta_declarada(tipo_atributo.getNombre()))
                throw new ExcepcionSemantica(clase_tipo,"Error Semantico en linea "+token_atributo.get_nro_linea() +": La clase "+tipo_atributo.getNombre()+" que es el tipo del atributo "+token_atributo.get_lexema()+" , no esta declarada.");
        }
    }

    public void set_offset(int offset) {
        this.offset = offset;
    }

    public int get_offset() { return offset; }
}
