package AST.Expresion;

import AnalizadorLexico.Token;

public abstract class NodoExpresion {

    protected Token token_expresion;

    public NodoExpresion(Token token){
        this.token_expresion = token;
    }

    public Token getToken(){ return token_expresion; }
    public abstract void esta_bien_definido();

    public abstract void mostrar_expresion();
}
