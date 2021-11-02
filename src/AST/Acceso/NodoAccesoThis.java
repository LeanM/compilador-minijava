package AST.Acceso;

import AnalizadorLexico.Token;

public class NodoAccesoThis extends NodoPrimario {

    private Token token_this;

    public NodoAccesoThis(Token token_this){
        super();
        this.token_this = token_this;
    }

    @Override
    public void esta_bien_definido() {

    }
}
