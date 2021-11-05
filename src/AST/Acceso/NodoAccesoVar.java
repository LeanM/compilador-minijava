package AST.Acceso;

import AST.Sentencia.NodoSentencia;
import AST.Sentencia.NodoVarLocal;
import AnalizadorLexico.Token;
import AnalizadorSemantico.*;

import java.util.LinkedList;

public class NodoAccesoVar extends NodoPrimario{

    //private Token token_variable;
    private EntradaUnidad metodo_origen;

    public NodoAccesoVar(Token token_variable, String key_clase, EntradaUnidad metodo_origen){
        super(token_variable,key_clase);
        this.metodo_origen = metodo_origen;
        //this.token_variable = token_variable;
    }

    @Override
    public void esta_bien_definido() {
        //Verificar semantica de acceso variables

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
                        throw new ExcepcionTipo("La variable a la que se quiere acceder es un atributo pero no al alcance de la llamada, y no es variable local, ni argumento de la unidad.");
                }
                else
                    throw new ExcepcionTipo("La variable a la que se quiere acceder no es ni variable local, ni argumento de la unidad, ni atributo visible de la clase.");
            }
        return toReturn;
    }
}
