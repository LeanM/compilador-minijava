package AnalizadorSemantico;

import AnalizadorLexico.Token;

public class EntradaParametro {

    private Token token_parametro;
    private Tipo tipo_parametro;
    private int offset;

    public EntradaParametro(Token token_parametro, Tipo tipo_parametro){
        this.token_parametro = token_parametro;
        this.tipo_parametro = tipo_parametro;
        this.offset = 0;
    }

    public String getNombre(){
        return token_parametro.get_lexema();
    }
    public Token get_token_parametro(){ return token_parametro;}
    public Tipo get_tipo(){return tipo_parametro;}

    /*  Verifica que ambos parametros, tanto el que recibio el mensaje como
     *  el parametro parametrizado sean de el mismo tipo exacto.
     */
    public boolean son_iguales(EntradaParametro param_a_comparar) {
        return this.tipo_parametro.mismo_tipo_exacto(param_a_comparar.get_tipo());
    }

    public void esta_bien_declarado() throws ExcepcionSemantica {
        if(!tipo_parametro.esPrimitivo()){
            if(!TablaSimbolos.getInstance().clase_esta_declarada(tipo_parametro.getNombre()))
                throw new ExcepcionSemantica(tipo_parametro.get_token_tipo(),"Error Semantico en linea "+token_parametro.get_nro_linea() +": El tipo del parametro "+token_parametro.get_lexema()+" es la clase "+tipo_parametro.getNombre()+" que no esta declarada.");
        }
    }

    public int get_offset() { return offset; }
    public void set_offset(int offset) { this.offset = offset; }
}