package AST.Acceso;

import AnalizadorLexico.Token;

public class NodoAccesoEstatico extends NodoPrimario{

    private Token token_clase;

    public NodoAccesoEstatico(Token clase){
        super();
        this.token_clase = clase;
    }

    @Override
    public void esta_bien_definido() {

    }
}
