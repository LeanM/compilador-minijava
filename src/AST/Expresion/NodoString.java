package AST.Expresion;

import AnalizadorLexico.Token;

public class NodoString extends NodoOperando_Literal {
    public NodoString(Token literal) {
        super(literal);
    }

    public void esta_bien_definido(){
        //Verificar para el literal especifico
    }
}