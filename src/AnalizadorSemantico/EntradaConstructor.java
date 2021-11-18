package AnalizadorSemantico;

import AnalizadorLexico.Token;
import java.util.Hashtable;

public class EntradaConstructor extends EntradaUnidad {

    public EntradaConstructor(Token token_constructor,Tipo tipo_constructor) {
        super(tipo_constructor,token_constructor);
    }

    /*  No en esta etapa (variables locales)
    public void setVariable(String nombre_variable, EntradaVariable variable) {
        tabla_variables.put(nombre_variable,variable);
    }
    */

    public void esta_bien_declarado() throws ExcepcionSemantica {
        for (EntradaParametro ea : lista_argumentos)
            ea.esta_bien_declarado();
    }

    public String get_etiqueta() {
        String toReturn = "lCtor"+this.getNombre();
        for(EntradaParametro ep : lista_argumentos){
            toReturn = toReturn + "_" + ep.get_tipo().getNombre();
        }
        return toReturn;
    }
}
