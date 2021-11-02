package AST.Expresion;

import AnalizadorLexico.Token;

public abstract class NodoOperando_Literal extends NodoOperando {

    private Token literal;

    public NodoOperando_Literal(Token literal){
        super();
        this.literal = literal;
    }
}
