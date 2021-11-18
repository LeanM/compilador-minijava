package AST.Acceso;
import AST.NodoBloque;
import AST.Sentencia.*;
import AnalizadorLexico.Token;
import AnalizadorSemantico.*;

import java.io.IOException;

public class NodoAccesoVar extends NodoPrimario_Concreto{

    private EntradaUnidad metodo_origen;
    private NodoBloque bloque_acceso_var;
    private Var acceso_tipo_variable;

    public NodoAccesoVar(Token token_variable, String key_clase, EntradaUnidad metodo_origen, NodoBloque bloque_acceso_var){
        super(token_variable,key_clase);
        this.metodo_origen = metodo_origen;
        this.bloque_acceso_var = bloque_acceso_var;
        acceso_tipo_variable = null;
    }

    @Override
    public void esta_bien_definido() throws ExcepcionTipo, ExcepcionSemantica {
        //Verificar semantica de acceso variables
        if (!acceso_var_local_bien_definido()) {
            //No esta en los parametros del metodo ni definida como variable local en el metodo
            if (!TablaSimbolos.getInstance().get_tabla_clases().get(key_clase).get_tabla_atributos().containsKey(token_acceso.get_lexema()))
                //No esta en los atributos
                throw new ExcepcionTipo(token_acceso, "La variable a la que se quiere acceder no esta declarada como variable local en el alcance, ni es argumento de la unidad, ni atributo visible de la clase.");
            else
                acceso_tipo_variable = new Var_Instancia(TablaSimbolos.getInstance().get_tabla_clases().get(key_clase).get_tabla_atributos().get(token_acceso.get_lexema()),this);
        }
    }

    @Override
    public Tipo get_tipo_acceso() throws ExcepcionTipo, ExcepcionSemantica {
        Tipo toReturn;
        //Verificar si es parametro o variable local ya declarada
        if (metodo_origen.get_tabla_argumentos().containsKey(token_acceso.get_lexema())){
            //Es un argumento
            toReturn = metodo_origen.get_tabla_argumentos().get(token_acceso.get_lexema()).get_tipo();
        }
        else {
            toReturn = metodo_origen.obtener_tipo_variable(token_acceso, bloque_acceso_var);
            if (toReturn == null) {
                EntradaAtributo entradaAtributo;
                if (TablaSimbolos.getInstance().get_tabla_clases().get(key_clase).get_tabla_atributos().containsKey(token_acceso.get_lexema())) {
                    entradaAtributo = TablaSimbolos.getInstance().get_tabla_clases().get(key_clase).get_tabla_atributos().get(token_acceso.get_lexema());
                    toReturn = entradaAtributo.getTipo();
                } else
                    throw new ExcepcionTipo(token_acceso, "La variable a la que se quiere acceder no es ni variable local, ni argumento de la unidad, ni atributo visible de la clase.");
            }
        }
        return toReturn;
    }

    public boolean acceso_var_local_bien_definido() throws ExcepcionSemantica {
        boolean toReturn = true;
        boolean esta_declarada = false;
        NodoBloque bloque_padre;

        if(!metodo_origen.get_tabla_argumentos().containsKey(token_acceso.get_lexema())) {
            if (bloque_acceso_var == metodo_origen.get_bloque_principal()) {
                if (bloque_acceso_var.get_tabla_var_locales().containsKey(token_acceso.get_lexema())) {
                    if (metodo_origen.get_bloque_principal().get_tabla_var_locales().get(token_acceso.get_lexema()).get_token().get_nro_linea() > token_acceso.get_nro_linea())
                        //Si esta declarada la variable local en el bloque despues del acceso [error]
                        toReturn = false;
                        else
                            acceso_tipo_variable = new Var_Local(bloque_acceso_var.get_tabla_var_locales().get(token_acceso.get_lexema()),this);
                } else
                    toReturn = false;
            } else
                //Si son bloques distintos
                if (bloque_acceso_var.get_tabla_var_locales().containsKey(token_acceso.get_lexema())) {
                    if (bloque_acceso_var.get_tabla_var_locales().get(token_acceso.get_lexema()).get_token().get_nro_linea() > token_acceso.get_nro_linea())
                        toReturn = false;
                    else
                        acceso_tipo_variable = new Var_Local(bloque_acceso_var.get_tabla_var_locales().get(token_acceso.get_lexema()),this);
                        //es_var_local = bloque_acceso_var.get_tabla_var_locales().get(token_acceso.get_lexema());
                } else {
                    bloque_padre = bloque_acceso_var.get_bloque_padre();
                    while (bloque_padre != metodo_origen.get_bloque_principal() && !esta_declarada) {
                        if (bloque_padre.get_tabla_var_locales().containsKey(token_acceso.get_lexema()) && bloque_padre.get_tabla_var_locales().get(token_acceso.get_lexema()).get_token().get_nro_linea() < token_acceso.get_nro_linea()) {
                            //La var local esta definida en un bloque padre [correcto]
                            esta_declarada = true;
                            //es_var_local = bloque_padre.get_tabla_var_locales().get(token_acceso.get_lexema());
                            acceso_tipo_variable = new Var_Local(bloque_padre.get_tabla_var_locales().get(token_acceso.get_lexema()),this);
                        } else bloque_padre = bloque_padre.get_bloque_padre();
                    }
                    if(!esta_declarada && metodo_origen.get_bloque_principal().get_tabla_var_locales().containsKey(token_acceso.get_lexema()) && metodo_origen.get_bloque_principal().get_tabla_var_locales().get(token_acceso.get_lexema()).get_token().get_nro_linea() < token_acceso.get_nro_linea())
                        esta_declarada = true;

                    if (!esta_declarada)
                        toReturn = false;
                }
        }
        else
            acceso_tipo_variable = new Var_parametro(metodo_origen.get_tabla_argumentos().get(token_acceso.get_lexema()),this);
            //es_parametro = true;

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

    @Override
    public void generar_codigo() throws IOException, ExcepcionTipo, ExcepcionSemantica {
        if (acceso_tipo_variable != null)
            acceso_tipo_variable.generar_codigo();
    }

    public boolean puede_ser_asignado(){
        return true;
    }

    public Var get_acceso_tipo_var(){
        return acceso_tipo_variable;
    }
    /*
    public EntradaAtributo es_variable_instancia() throws ExcepcionSemantica {
        EntradaAtributo toReturn = null;
        if(es_var_instancia)
            toReturn = TablaSimbolos.getInstance().conforma_atributo(token_acceso,key_clase);

        return toReturn;
    }

    public EntradaParametro es_parametro() {
        EntradaParametro toReturn = null;
        if(es_parametro)
            toReturn = metodo_origen.get_tabla_argumentos().get(token_acceso.get_lexema());

        return toReturn;
    }

    public NodoVarLocal es_var_local() {
        return es_var_local;
    }

     */
}