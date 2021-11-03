package AST.Acceso;

import AnalizadorLexico.Token;

public class NodoAccesoVar extends NodoPrimario{

    //private Token token_variable;

    public NodoAccesoVar(Token token_variable){
        super(token_variable);
        //this.token_variable = token_variable;
    }

    @Override
    public void esta_bien_definido() {

    }
}
