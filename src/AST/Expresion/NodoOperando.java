package AST.Expresion;

import AnalizadorLexico.Token;

public abstract class NodoOperando extends NodoExpresion {

    public NodoOperando(Token operando){
        super(operando);
    }

}
