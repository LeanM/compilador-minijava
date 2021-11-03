package AST.Acceso;

import AnalizadorLexico.Token;

public abstract class NodoAcceso {

    protected Token token_acceso;

    public NodoAcceso(Token token){
        this.token_acceso = token;
    }

    public Token getToken() { return token_acceso; }
    public abstract void esta_bien_definido();
    public abstract void mostrar_acceso();

}
