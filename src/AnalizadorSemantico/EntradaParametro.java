package AnalizadorSemantico;

import AnalizadorLexico.Token;

public class EntradaParametro {

    private Token token_parametro;
    private Tipo tipo_parametro;

    public EntradaParametro(Token token_parametro, Tipo tipo_parametro){
        this.token_parametro = token_parametro;
        this.tipo_parametro = tipo_parametro;
    }

    public String getNombre(){
        return token_parametro.get_lexema();
    }
    public Token get_token_parametro(){ return token_parametro;}
    public Tipo get_tipo(){return tipo_parametro;};
    public boolean son_iguales(EntradaParametro param_a_comparar) {
        return this.tipo_parametro.es_de_tipo(param_a_comparar.get_tipo());
    }

    public void esta_bien_declarado() throws ExcepcionSemantica {
        if(!tipo_parametro.esPrimitivo()){
            if(!TablaSimbolos.getInstance().clase_esta_declarada(tipo_parametro.getNombre()))
                throw new ExcepcionSemantica(token_parametro,"Error Semantico en linea "+token_parametro.get_nro_linea() +": El tipo del parametro "+token_parametro.get_lexema()+" es una clase que no esta declarada.");
        }
    }
}