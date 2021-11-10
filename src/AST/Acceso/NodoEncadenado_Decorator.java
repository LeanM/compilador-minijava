package AST.Acceso;

import AnalizadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemantica;
import AnalizadorSemantico.ExcepcionTipo;
import AnalizadorSemantico.Tipo;

public abstract class NodoEncadenado_Decorator extends NodoPrimario_Component {

    protected NodoPrimario_Component primario_decorator;

    public NodoEncadenado_Decorator(Token token_primario, NodoPrimario_Component primario) {
        super(token_primario);
        primario_decorator = primario;
    }

    public NodoPrimario_Concreto obtener_primario_concreto() {
        return primario_decorator.obtener_primario_concreto();
    }

    public void chequeo_acceso_estatico() throws ExcepcionTipo, ExcepcionSemantica {
        this.obtener_primario_concreto().chequeo_acceso_estatico();
    }

}
