package AST.Acceso;

import AnalizadorLexico.Token;

public class NodoVarEncadenada extends NodoEncadenado{

    public NodoVarEncadenada(Token nombre){
        super(nombre);
    }

    @Override
    public void esta_bien_definido() {

    }
}
