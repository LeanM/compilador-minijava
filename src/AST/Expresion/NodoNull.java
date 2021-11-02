package AST.Expresion;

import AnalizadorLexico.Token;

public class NodoNull extends NodoOperando_Literal {
    public NodoNull(Token literal) {
        super(literal);
    }

    public void esta_bien_definido(){
        //Verificar para el literal especifico
    }
}
