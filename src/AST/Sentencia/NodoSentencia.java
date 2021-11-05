package AST.Sentencia;

import AnalizadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemantica;
import AnalizadorSemantico.ExcepcionTipo;

public abstract class NodoSentencia {

    //protected Token token_sentencia;

    public NodoSentencia(){

    }
/*
    public Token getToken(){
        return token_sentencia;
    }
*/
    public abstract void esta_bien_definido() throws ExcepcionTipo, ExcepcionSemantica;
    public abstract void mostar_sentencia();
}
