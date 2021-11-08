package AST.Acceso;

import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;
import AnalizadorSemantico.*;

import java.util.LinkedList;

public class NodoAccesoMetodo extends NodoAccesoUnidad{

    public NodoAccesoMetodo(Token nombre, LinkedList<NodoExpresion> args, String key_clase){
        super(nombre,args,key_clase);
    }

    @Override
    public void esta_bien_definido() throws ExcepcionTipo, ExcepcionSemantica {
        if(!TablaSimbolos.getInstance().get_tabla_clases().get(key_clase).get_tabla_metodos().containsKey(token_acceso.get_lexema()))
            throw new ExcepcionSemantica(token_acceso,"El metodo llamado no es visible en el contexto de la clase "+key_clase);

        EntradaMetodo metodo_en_clase = TablaSimbolos.getInstance().conforma_metodo(token_acceso,argumentos,key_clase);
        if (metodo_en_clase == null)
            throw new ExcepcionTipo(token_acceso,"La llamada a metodo no conforma con ningun metodos de la clase.");

    }

    @Override
    public Tipo get_tipo_acceso() throws ExcepcionTipo, ExcepcionSemantica {
        EntradaMetodo metodo_en_clase = TablaSimbolos.getInstance().conforma_metodo(token_acceso,argumentos,key_clase);
        if (metodo_en_clase == null)
            throw new ExcepcionTipo(token_acceso,"La llamada a metodo no conforma con ningun metodos de la clase.");
        else return metodo_en_clase.get_tipo();

    }

    @Override
    public void chequeo_acceso_estatico() throws ExcepcionSemantica, ExcepcionTipo {
        EntradaMetodo metodo_en_clase = TablaSimbolos.getInstance().conforma_metodo(token_acceso,argumentos,key_clase);
        if (metodo_en_clase == null)
            throw new ExcepcionTipo(token_acceso,"La llamada a metodo no conforma con ningun metodos de la clase.");
        else
            if(!metodo_en_clase.es_estatico())
                throw new ExcepcionSemantica(token_acceso,"No se puede acceder al metodo dinamico "+metodo_en_clase.getNombre()+" desde un contexto estatico");
    }
}
