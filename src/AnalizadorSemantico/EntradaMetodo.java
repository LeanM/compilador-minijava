package AnalizadorSemantico;

import AnalizadorLexico.Token;
import java.util.HashMap;


public class EntradaMetodo extends EntradaUnidad {

    private Token token_metodo;
    private String alcance_metodo;
    private Tipo tipo_metodo;
    //private HashMap<String,EntradaVariable> tabla_variables;

    public EntradaMetodo(Token token_metodo, String alcance_metodo, Tipo tipo_metodo) {
        super();
        this.token_metodo = token_metodo;
        this.alcance_metodo = alcance_metodo;
        this.tipo_metodo = tipo_metodo;
        //tabla_variables = new HashMap<String,EntradaVariable>();
    }

    public void setArgumento(String nombre_argumento, EntradaParametro argumento) throws ExcepcionSemantica {
        if(!tabla_argumentos.containsKey(nombre_argumento)) {
            tabla_argumentos.put(nombre_argumento, argumento);
            lista_argumentos.add(argumento);
        }
        else throw new ExcepcionSemantica(argumento.get_token_parametro(),"Error Semantico en linea "+argumento.get_token_parametro().get_nro_linea() +": Ya hay un parametro declarado con el nombre "+nombre_argumento);
    }
    /*  No de esta etapa (variables locales)
    public void setVariable(String nombre_variable, EntradaVariable variable) {
        tabla_variables.put(nombre_variable,variable);
    }
    */
    public String getNombre(){
        return token_metodo.get_lexema();
    }
    public Token get_token_metodo(){ return token_metodo;}
    public String get_alcance() {return alcance_metodo;}
    public Tipo get_tipo(){return tipo_metodo;}

    public void esta_bien_declarado() throws ExcepcionSemantica {
        if(!tipo_metodo.esPrimitivo())
            if(!TablaSimbolos.getInstance().clase_esta_declarada(tipo_metodo.getNombre()))
                throw new ExcepcionSemantica(token_metodo,"Error Semantico en linea "+token_metodo.get_nro_linea() +": El tipo de retorno del metodo "+token_metodo.get_lexema()+" es una clase que no esta declarada.");

        for (EntradaParametro ea : lista_argumentos)
            ea.esta_bien_declarado();

    }

    public boolean metodos_iguales(EntradaMetodo metodo_a_comparar) throws ExcepcionSemantica {
        boolean toReturn = false;
        if (metodo_a_comparar.mismos_argumentos(this.lista_argumentos) && metodo_a_comparar.tipo_metodo.mismo_tipo_exacto(this.tipo_metodo) && metodo_a_comparar.get_alcance().equals(this.alcance_metodo)){
            //Redefinicion correcta
            toReturn = true;
        }

        return toReturn;
    }
}