package AST.Acceso;

import AnalizadorLexico.Token;
import AnalizadorSemantico.*;

public class NodoVarEncadenada_Decorator extends NodoEncadenado_Decorator{

    public NodoVarEncadenada_Decorator(Token token_var_encadenada, NodoPrimario_Component primario) {
        super(token_var_encadenada, primario);
    }

    @Override
    public void esta_bien_definido() throws ExcepcionTipo, ExcepcionSemantica {
        primario_decorator.esta_bien_definido();
        Tipo tipo_izq = primario_decorator.get_tipo_acceso();
        EntradaAtributo atributo_conforma = TablaSimbolos.getInstance().conforma_atributo(token_acceso,tipo_izq.getNombre());
        if(atributo_conforma == null)
            throw new ExcepcionTipo(token_acceso,"El atributo encadenado no es un atributo de la clase del encadenado de la izquierda o no esta al alcance");
    }

    @Override
    public Tipo get_tipo_acceso() throws ExcepcionTipo, ExcepcionSemantica {
        Tipo tipo_izq;
        tipo_izq = primario_decorator.get_tipo_acceso();
        EntradaAtributo atributo_conforma = TablaSimbolos.getInstance().conforma_atributo(token_acceso,tipo_izq.getNombre());
        if(atributo_conforma == null)
            throw new ExcepcionTipo(token_acceso,"El atributo encadenado no es un atributo de la clase del encadenado de la izquierda o no esta al alcance");

        return atributo_conforma.getTipo();
    }

    public boolean puede_ser_asignado(){
        return true;
    }
}
