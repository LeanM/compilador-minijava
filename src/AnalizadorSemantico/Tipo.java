package AnalizadorSemantico;

import AnalizadorLexico.Token;

import java.util.LinkedList;

public abstract class Tipo {

    protected Token token_tipo;

    public Tipo (Token token_tipo){
        this.token_tipo = token_tipo;
    }

    public String getNombre(){
        return token_tipo.get_lexema();
    }
    public abstract boolean esPrimitivo();
    public abstract boolean es_de_tipo(Tipo tipo);
    public boolean mismo_tipo_exacto(Tipo tipo_a_comparar){
        return tipo_a_comparar.getNombre().equals(this.getNombre());
    }
}