package AST.Acceso;

import AnalizadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemantica;
import AnalizadorSemantico.ExcepcionTipo;
import AnalizadorSemantico.Tipo;

public class NodoAccesoEstatico extends NodoPrimario{

    //private Token token_clase;

    public NodoAccesoEstatico(Token clase, String key_clase){
        super(clase,key_clase);
        //this.token_clase = clase;
    }

    @Override
    public void esta_bien_definido() {
        //Verificar semanticas de acceso estatico
    }

    @Override
    public Tipo get_tipo_acceso() throws ExcepcionTipo, ExcepcionSemantica {
        return null;
    }


}
