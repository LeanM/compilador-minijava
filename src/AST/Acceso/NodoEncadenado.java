package AST.Acceso;

import AnalizadorLexico.Token;

public abstract class NodoEncadenado {

    private Token token_nombre;

    public NodoEncadenado(Token nombre){
        this.token_nombre = nombre;
    }

    public abstract void esta_bien_definido();
}
