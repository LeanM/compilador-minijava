package AST.Acceso;

import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;
import AnalizadorSemantico.*;

import java.util.LinkedList;

public class NodoAccesoMetodo extends NodoAccesoUnidad{

    public NodoAccesoMetodo(Token nombre, LinkedList<NodoExpresion> args, String key_clase){
        super(nombre,args,key_clase);
    }

    public void m1(NodoAccesoUnidad au){}
    /*
    @Override
    public void esta_bien_definido() throws ExcepcionTipo, ExcepcionSemantica {
        boolean iguales = false;
        EntradaClase clase = TablaSimbolos.getInstance().get_entrada_clase(key_clase);
        LinkedList<EntradaMetodo> lista_metodos = clase.get_tabla_metodos().get(token_acceso.get_lexema());

        for (EntradaMetodo em : lista_metodos){
            LinkedList<EntradaParametro> args_metodo = em.get_lista_argumentos();
            if(argumentos.size() == args_metodo.size()) {
                iguales = true;
                for (int i = 0; i < argumentos.size(); i++) {
                    if(!this.argumentos.get(i).get_tipo_expresion().es_de_tipo(args_metodo.get(i).get_tipo()))
                        iguales = false;
                }
                if(iguales)
                    break;
            }
        }

        if(!iguales)
            throw new ExcepcionTipo("La llamda a metodo no es correctamente tipada para ninguno de los metodos de la clase.");
    }
    */
    @Override
    public void esta_bien_definido() throws ExcepcionTipo, ExcepcionSemantica {
        boolean iguales = false;
        EntradaClase clase = TablaSimbolos.getInstance().get_entrada_clase(key_clase);
        LinkedList<EntradaMetodo> lista_metodos = clase.get_tabla_metodos().get(token_acceso.get_lexema());

        for (EntradaMetodo em : lista_metodos){
            LinkedList<EntradaParametro> args_metodo = em.get_lista_argumentos();
            iguales = this.conforma_argumentos(args_metodo);
            if(iguales)
                break;
        }

        if(!iguales)
            throw new ExcepcionTipo("La llamada a metodo no es correctamente tipada para ninguno de los metodos de la clase.");
    }

    @Override
    public Tipo get_tipo_acceso() throws ExcepcionTipo, ExcepcionSemantica {
        LinkedList<EntradaMetodo> metodos = TablaSimbolos.getInstance().get_entrada_clase(key_clase).get_tabla_metodos().get(token_acceso.get_lexema());
        LinkedList<EntradaParametro> args_metodo;
        Tipo toReturn = new TipoReferencia(null);
        boolean conforma = false;
        for(EntradaMetodo m : metodos){
            args_metodo = m.get_lista_argumentos();
            conforma = this.conforma_argumentos(args_metodo);
            if(conforma) {
                toReturn = m.get_tipo();
                break;
            }
        }

        if(!conforma)
            throw new ExcepcionTipo("No hay ningun metodo en la clase que conforme con el metodo llamado.");

        return toReturn;
    }
}
