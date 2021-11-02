package AST.Expresion;

import AnalizadorLexico.Token;

public class NodoEntero extends NodoOperando_Literal {
    public NodoEntero(Token literal) {
        super(literal);
    }

    public void esta_bien_definido(){
        //Verificar para el literal especifico
    }
}
