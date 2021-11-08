package AST.Acceso;

import AST.Sentencia.NodoSentencia;
import AST.Sentencia.NodoVarLocal;
import AnalizadorLexico.Token;
import AnalizadorSemantico.*;

import java.util.LinkedList;

public class NodoAccesoVar extends NodoPrimario_Concreto{

    private EntradaUnidad metodo_origen;

    public NodoAccesoVar(Token token_variable, String key_clase, EntradaUnidad metodo_origen){
        super(token_variable,key_clase);
        this.metodo_origen = metodo_origen;
    }

    @Override
    public void esta_bien_definido() throws ExcepcionTipo {
        //Verificar semantica de acceso variables
        if(!metodo_origen.get_tabla_var_locales().containsKey(token_acceso.get_lexema()) && !metodo_origen.get_tabla_argumentos().containsKey(token_acceso.get_lexema())) {
            //No esta en los parametros del metodo ni definida como variable local en el metodo
            if(TablaSimbolos.getInstance().get_tabla_clases().get(key_clase).get_tabla_atributos().containsKey(token_acceso.get_lexema())){
                EntradaAtributo entradaAtributo = TablaSimbolos.getInstance().get_tabla_clases().get(key_clase).get_tabla_atributos().get(token_acceso.get_lexema());
                if (entradaAtributo.get_visibilidad().equals("private")){
                    //No es una var local ni parametro del metodo al que pertenece, y tampoco es un atributo visible en la clase donde esta declarado el metodo
                    throw new ExcepcionTipo(token_acceso,"La variable a la que se quiere acceder no es ni variable local, ni argumento de la unidad, ni atributo visible de la clase.");
                }

            }
            else    //No esta en los atributos
                throw new ExcepcionTipo(token_acceso,"La variable a la que se quiere acceder no es ni variable local, ni argumento de la unidad, ni atributo de la clase.");
        }
    }

    @Override
    public Tipo get_tipo_acceso() throws ExcepcionTipo, ExcepcionSemantica {
        Tipo toReturn;
        //Verificar si es parametro o variable local ya declarada
        if(metodo_origen.get_tabla_var_locales().containsKey(token_acceso.get_lexema())) {
            //Es una var local
            toReturn = metodo_origen.get_tabla_var_locales().get(token_acceso.get_lexema()).getTipo();
        }
        else
            if (metodo_origen.get_tabla_argumentos().containsKey(token_acceso.get_lexema())){
                //Es un argumento
                toReturn = metodo_origen.get_tabla_argumentos().get(token_acceso.get_lexema()).get_tipo();
            }
            else {
                EntradaAtributo entradaAtributo;
                if(TablaSimbolos.getInstance().get_tabla_clases().get(key_clase).get_tabla_atributos().containsKey(token_acceso.get_lexema())) {
                    entradaAtributo = TablaSimbolos.getInstance().get_tabla_clases().get(key_clase).get_tabla_atributos().get(token_acceso.get_lexema());
                    if (entradaAtributo.get_visibilidad().equals("public"))
                        toReturn = entradaAtributo.getTipo();
                    else
                        throw new ExcepcionTipo(token_acceso,"La variable a la que se quiere acceder es un atributo pero no al alcance de la llamada, y no es variable local, ni argumento de la unidad.");
                }
                else
                    throw new ExcepcionTipo(token_acceso,"La variable a la que se quiere acceder no es ni variable local, ni argumento de la unidad, ni atributo visible de la clase.");
            }
        return toReturn;
    }


    public void chequeo_acceso_estatico() throws ExcepcionSemantica {
        EntradaAtributo entradaAtributo = TablaSimbolos.getInstance().conforma_atributo(token_acceso,key_clase);
        if(entradaAtributo != null)
            if(!entradaAtributo.es_estatico()){
                if(!metodo_origen.get_tabla_argumentos().containsKey(token_acceso.get_lexema()) && !metodo_origen.get_tabla_var_locales().containsKey(token_acceso.get_lexema()))
                    throw new ExcepcionSemantica(token_acceso,"No se puede acceder al atributo dinamico "+entradaAtributo.getNombre()+" desde un contexto estatico");
            }
    }

    public boolean puede_ser_asignado(){
        return true;
    }
}
