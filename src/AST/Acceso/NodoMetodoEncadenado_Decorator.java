package AST.Acceso;

import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;
import AnalizadorSemantico.*;
import java.util.LinkedList;

public class NodoMetodoEncadenado_Decorator extends NodoEncadenado_Decorator{

    protected LinkedList<NodoExpresion> argumentos;

    public NodoMetodoEncadenado_Decorator(Token token_metodo_encadenado,LinkedList<NodoExpresion> args, NodoPrimario_Component primario) {
        super(token_metodo_encadenado, primario);
        this.argumentos = args;
    }

    @Override
    public void esta_bien_definido() throws ExcepcionTipo, ExcepcionSemantica {
        Tipo tipo_izq = primario_decorator.get_tipo_acceso();
        EntradaMetodo metodo_conforma = TablaSimbolos.getInstance().conforma_metodo(token_acceso,argumentos,tipo_izq.getNombre());
        if(metodo_conforma == null)
            throw new ExcepcionTipo(token_acceso,"La llamada a metodo no conforma con ningun metodos de la clase.");
    }

    @Override
    public Tipo get_tipo_acceso() throws ExcepcionTipo, ExcepcionSemantica {
        Tipo toReturn;
        Tipo tipo_izq = primario_decorator.get_tipo_acceso();
        EntradaMetodo metodo_conforma = TablaSimbolos.getInstance().conforma_metodo(token_acceso,argumentos,tipo_izq.getNombre());
        if(metodo_conforma == null)
            throw new ExcepcionTipo(token_acceso,"La llamada a metodo no conforma con ningun metodos de la clase.");
        else
            toReturn = metodo_conforma.get_tipo();

        return toReturn;
    }

}
