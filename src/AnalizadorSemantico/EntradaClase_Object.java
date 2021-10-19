package AnalizadorSemantico;

import AnalizadorLexico.Token;

import java.util.LinkedList;

public class EntradaClase_Object extends EntradaClase{

    public EntradaClase_Object(Token token_clase) {
        super(token_clase);

    }

    @Override
    public void herenciaCircular() {
    }

    @Override
    public void setClaseSuper(Token claseSuper) {

    }


    @Override
    public Token getClaseSuper() {
        return this.token_clase;
    }

    @Override
    public void esta_bien_declarada() throws ExcepcionSemantica {
        //nada
    }

    @Override
    protected void get_lista_ancestros(LinkedList<String> jerarquia_ancestros) {
        //nada
    }
}
