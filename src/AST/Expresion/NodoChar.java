package AST.Expresion;

import AnalizadorLexico.Token;

public class NodoChar extends NodoOperando_Literal {
    public NodoChar(Token literal) {
        super(literal);
    }

    public void esta_bien_definido(){
        //Verificar para el literal especifico
    }
}
