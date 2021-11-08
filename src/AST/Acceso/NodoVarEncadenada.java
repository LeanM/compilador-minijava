package AST.Acceso;

import AnalizadorLexico.Token;
import AnalizadorSemantico.*;
/*
public class NodoVarEncadenada extends NodoEncadenado{

    public NodoVarEncadenada(Token nombre, NodoPrimario nodoPrimario){
        super(nombre,nodoPrimario);
    }

    @Override
    public Tipo obtener_tipo_encadenado() throws ExcepcionTipo, ExcepcionSemantica {
        Tipo toReturn;
        if(nodo_primario.get_pos_en_encadenados(this) == 0){
            toReturn = nodo_primario.get_tipo_acceso();
        }
        else {
            toReturn = nodo_primario.get_encadenado_izq(this).obtener_tipo_encadenado();
        }
        return toReturn;
    }

    @Override
    public void esta_bien_definido() throws ExcepcionTipo, ExcepcionSemantica {
        Tipo tipo_encadenado_izq;
        EntradaAtributo entradaAtributo;
        if(nodo_primario.get_pos_en_encadenados(this) == 0){
            entradaAtributo = TablaSimbolos.getInstance().conforma_atributo(token_nombre,nodo_primario.key_clase);
        }
        else {
            tipo_encadenado_izq = nodo_primario.get_encadenado_izq(this).obtener_tipo_encadenado();
            entradaAtributo = TablaSimbolos.getInstance().conforma_atributo(token_nombre,tipo_encadenado_izq.getNombre());
        }

        if(entradaAtributo == null)
            throw new ExcepcionTipo("El atributo encadenado no es un atributo de la clase del encadenado de la izquierda o no esta al alcance");
    }
}
*/