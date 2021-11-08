package AST.Acceso;

import AnalizadorLexico.Token;
import AnalizadorSemantico.*;

public class NodoAccesoEstatico extends NodoPrimario_Concreto{

    protected Tipo tipo_estatico;
    public NodoAccesoEstatico(Tipo tipo_estatico, String key_clase){
        super(tipo_estatico.get_token_tipo(),key_clase);
        this.tipo_estatico = tipo_estatico;
    }

    @Override
    public void esta_bien_definido() throws ExcepcionSemantica {
        //Verificar semanticas de acceso estatico
        if(!TablaSimbolos.getInstance().clase_esta_declarada(token_acceso.get_lexema()))
            throw new ExcepcionSemantica(token_acceso,"La clase "+token_acceso.get_lexema()+" no esta declarada.");
    }

    @Override
    public Tipo get_tipo_acceso() throws ExcepcionTipo, ExcepcionSemantica {
        return tipo_estatico;
    }

    @Override
    public void chequeo_acceso_estatico() throws ExcepcionSemantica, ExcepcionTipo {
        //nada
    }


}
