package AST.Acceso;

import AnalizadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemantica;
import AnalizadorSemantico.ExcepcionTipo;

public abstract class NodoPrimario_Concreto extends NodoPrimario_Component {

    protected String key_clase;

    public NodoPrimario_Concreto(Token token_primario, String key_clase) {
        super(token_primario);
        this.key_clase = key_clase;
    }

    @Override
    public void esta_bien_definido() throws ExcepcionTipo, ExcepcionSemantica {
        //Comportamiento general (no hacer nada)
    }

    public NodoPrimario_Concreto obtener_primario_concreto() {
        return this;
    }

}
