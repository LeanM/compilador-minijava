package AST.Sentencia;

import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;

public class NodoReturn extends NodoSentencia {

    private NodoExpresion expresion;
    private Token token_return;

    public NodoReturn(Token token_return){
        super();this.token_return = token_return;
    }

    public NodoReturn(Token token_return, NodoExpresion expresion){
        super();
        this.token_return = token_return;
        this.expresion = expresion;
    }

    @Override
    public void esta_bien_definido() {
        //expresion puede ser null
    }
}
