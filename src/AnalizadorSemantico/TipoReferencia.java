package AnalizadorSemantico;

import AnalizadorLexico.Token;

import java.util.LinkedList;

public class TipoReferencia extends Tipo{

    protected EntradaClase clase_tipo;
    /*
    public TipoReferencia(Token token_tipo, EntradaClase clase) {
        super(token_tipo);
        this.clase_tipo = clase;
    }
     */

    public TipoReferencia(Token token_tipo){
        super(token_tipo);
    }

    public boolean esPrimitivo() {
        return false;
    }

    @Override
    public boolean es_de_tipo(Tipo tipo) throws ExcepcionSemantica {
        boolean toReturn = false;

        if(tipo.esPrimitivo() && !tipo.getNombre().equals("String"))
            toReturn = false;
        else {
            if (this.getNombre().equals("null") || tipo.getNombre().equals("null"))
                toReturn = true;
            else {
                if (!tipo.getNombre().equals(this.getNombre())) {
                    LinkedList<String> lista_ancestros = new LinkedList<String>();
                    TablaSimbolos.getInstance().get_entrada_clase(token_tipo.get_lexema()).get_lista_ancestros(lista_ancestros); //Puede andar mal por q no se si se puede ejecutar antes de haber verificado que la clase estaba declarada
                    if (lista_ancestros.contains(tipo.getNombre())) {
                        toReturn = true;
                    }
                } else toReturn = true;
            }
        }

        return toReturn;
    }

}
