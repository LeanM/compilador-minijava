package AnalizadorSemantico;

import AnalizadorLexico.Token;
import java.util.Hashtable;

public class EntradaConstructor extends EntradaUnidad {

    private Token token_constructor;
    //private Hashtable<String,EntradaVariable> tabla_variables;

    public EntradaConstructor(Token token_constructor,Tipo tipo_constructor) {
        super(tipo_constructor);
        this.token_constructor = token_constructor;
        //tabla_variables = new Hashtable<String,EntradaVariable>();
    }

    public void setArgumento(String nombre_argumento, EntradaParametro argumento) throws ExcepcionSemantica{
        if(!tabla_argumentos.containsKey(nombre_argumento)) {
            tabla_argumentos.put(nombre_argumento, argumento);
            lista_argumentos.add(argumento);
        }
        else throw new ExcepcionSemantica(argumento.get_token_parametro(),"Error Semantico en linea "+argumento.get_token_parametro().get_nro_linea() +": Ya hay un parametro declarado con el nombre "+nombre_argumento);
    }
    /*  No en esta etapa (variables locales)
    public void setVariable(String nombre_variable, EntradaVariable variable) {
        tabla_variables.put(nombre_variable,variable);
    }
    */
    public String getNombre(){
        return token_constructor.get_lexema();
    }
    public Token get_token_constructor(){return token_constructor;}

    public void esta_bien_declarado() throws ExcepcionSemantica {
        for (EntradaParametro ea : lista_argumentos)
            ea.esta_bien_declarado();
    }
}
