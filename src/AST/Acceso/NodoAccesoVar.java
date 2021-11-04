package AST.Acceso;

import AnalizadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemantica;
import AnalizadorSemantico.ExcepcionTipo;
import AnalizadorSemantico.Tipo;

public class NodoAccesoVar extends NodoPrimario{

    //private Token token_variable;

    public NodoAccesoVar(Token token_variable, String key_clase){
        super(token_variable,key_clase);
        //this.token_variable = token_variable;
    }

    @Override
    public void esta_bien_definido() {
        //Verificar semantica de acceso variables
    }

    @Override
    public Tipo get_tipo_acceso() throws ExcepcionTipo, ExcepcionSemantica {
        return null;
    }
}
