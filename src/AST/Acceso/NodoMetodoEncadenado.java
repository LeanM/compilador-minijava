package AST.Acceso;

import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;
import AnalizadorSemantico.*;

import java.util.LinkedList;

public class NodoMetodoEncadenado extends NodoEncadenado{

    private LinkedList<NodoExpresion> argumentos;

    public NodoMetodoEncadenado(Token nombre,LinkedList<NodoExpresion> args, NodoPrimario nodoPrimario){
        super(nombre,nodoPrimario);
        argumentos = args;
    }

    @Override
    public Tipo obtener_tipo() throws ExcepcionTipo, ExcepcionSemantica {
        Tipo toReturn;
        EntradaMetodo em;
        if(nodo_primario.get_pos_en_encadenados(this) == 0){
            em = TablaSimbolos.getInstance().conforma_metodo(token_nombre,argumentos, nodo_primario.get_tipo_acceso().getNombre());
            if(em == null)
                throw new ExcepcionTipo("La llamada a metodo no conforma con ningun metodos de la clase.");
            else
                toReturn = em.get_tipo();
        }
        else {
            NodoEncadenado encadenado_izq = nodo_primario.get_encadenado_izq(this);
            em = TablaSimbolos.getInstance().conforma_metodo(token_nombre,argumentos, encadenado_izq.obtener_tipo().getNombre());
            if(em == null)
                throw new ExcepcionTipo("La llamada a metodo no conforma con ningun metodos de la clase.");
            else
                toReturn = em.get_tipo();
        }

        return toReturn;
    }


    public void esta_bien_definido() throws ExcepcionTipo, ExcepcionSemantica {
        int pos_en_encadenados = nodo_primario.get_pos_en_encadenados(this);
        EntradaMetodo metodo_conforma;
        NodoEncadenado encadenado_izq;
        Tipo tipo_metodo_var_izq;

        if(pos_en_encadenados > 0){
            encadenado_izq = nodo_primario.get_encadenado_izq(this);
            tipo_metodo_var_izq = encadenado_izq.obtener_tipo();
            metodo_conforma = TablaSimbolos.getInstance().conforma_metodo(token_nombre,argumentos,tipo_metodo_var_izq.getNombre());
            if(metodo_conforma == null)
                throw new ExcepcionTipo("La llamada a metodo no conforma con ningun metodos de la clase.");
        }
        else {
            //pos_en_encadenados == 0
            tipo_metodo_var_izq = nodo_primario.get_tipo_acceso();
            metodo_conforma = TablaSimbolos.getInstance().conforma_metodo(token_nombre,argumentos,tipo_metodo_var_izq.getNombre());
            if(metodo_conforma == null)
                throw new ExcepcionTipo("La llamada a metodo no conforma con ningun metodos de la clase.");
        }
    }
}
