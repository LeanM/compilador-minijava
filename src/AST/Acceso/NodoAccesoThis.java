package AST.Acceso;

import AnalizadorLexico.Token;
import AnalizadorSemantico.*;

public class NodoAccesoThis extends NodoPrimario {

    //private Token token_this;

    public NodoAccesoThis(Token token_this, String key_clase){
        super(token_this,key_clase);
        //this.token_this = token_this;
    }

    @Override
    public void esta_bien_definido() {

    }

    @Override
    public Tipo get_tipo_acceso() throws ExcepcionTipo, ExcepcionSemantica {
        return new TipoReferencia(token_acceso);
    }
}
