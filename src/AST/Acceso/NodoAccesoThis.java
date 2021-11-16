package AST.Acceso;

import AnalizadorLexico.Token;
import AnalizadorSemantico.*;

public class NodoAccesoThis extends NodoPrimario_Concreto {

    protected Tipo tipo_this;

    public NodoAccesoThis(Token token_this, Tipo tipo_clase_actual){
        super(token_this,tipo_clase_actual.getNombre());
        this.tipo_this = tipo_clase_actual;
    }

    @Override
    public Tipo get_tipo_acceso() {
        return tipo_this;
    }

    @Override
    public void chequeo_acceso_estatico() throws ExcepcionSemantica, ExcepcionTipo {
        throw new ExcepcionSemantica(token_acceso,"No se puede referenciar a this desde un contexto estatico");
    }

    @Override
    public void generar_codigo() {

    }
}
