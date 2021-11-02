package AST.Expresion;

import AnalizadorLexico.Token;

public class NodoBoolean extends NodoOperando_Literal {
    public NodoBoolean(Token literal) {
        super(literal);
    }

    public void esta_bien_definido(){
        //Verificar para el literal especifico
    }
}