package AnalizadorSemantico;

import AST.Sentencia.NodoSentencia;
import AnalizadorLexico.Token;
import java.util.HashMap;


public class EntradaMetodo extends EntradaUnidad {

    private String alcance_metodo;
    private String clase_base;
    private boolean fue_traducido;
    private int offset;

    public EntradaMetodo(Token token_metodo, String alcance_metodo, Tipo tipo_metodo) {
        super(tipo_metodo,token_metodo);
        this.alcance_metodo = alcance_metodo;
        this.clase_base = "";
        this.fue_traducido = false;
        this.offset = 0;
    }

    public String get_alcance() {return alcance_metodo;}
    public void set_sentencias_static(){
        for(NodoSentencia ns : bloque_principal.get_lista_sentencias())
            ns.en_metodo_static();
    }

    public void esta_bien_declarado() throws ExcepcionSemantica {
        if(!tipo_unidad.esPrimitivo())
            if(!TablaSimbolos.getInstance().clase_esta_declarada(tipo_unidad.getNombre()))
                throw new ExcepcionSemantica(tipo_unidad.get_token_tipo(),"Error Semantico en linea "+token_unidad.get_nro_linea() +": El tipo de retorno del metodo "+token_unidad.get_lexema()+" es la clase "+tipo_unidad.getNombre()+" que no esta declarada.");

        for (EntradaParametro ea : lista_argumentos)
            ea.esta_bien_declarado();

    }

    public boolean es_estatico(){
        return alcance_metodo.equals("static");
    }

    /* *--En la comparacion entre metodos para verificar redefinicion debe recibir el mensaje el metodo de la clase hija--*
     *
     * Verifica que el metodo que recibe el mensaje tenga los argumentos exactamente iguales (mismo orden y exactamente mismo tipo)
     * que los argumentos del metodo parametrizado.
     * Tambien que el metodo que recibe el mensaje retorne un objeto del mismo tipo (no exactamente el mismo) que el metodo parametrizado.
     * Y por ultimo que el metodo que recibe el mensaje tenga exactamente el mismo alcance que el metodo parametrizado.
     */
    public boolean metodos_iguales(EntradaMetodo metodo_a_comparar) throws ExcepcionSemantica {
        boolean toReturn = false;
        if (this.mismos_argumentos(metodo_a_comparar.get_lista_argumentos()) && this.tipo_unidad.es_de_tipo(metodo_a_comparar.get_tipo()) && metodo_a_comparar.get_alcance().equals(this.alcance_metodo)){
            //Redefinicion correcta
            toReturn = true;
        }

        return toReturn;
    }

    public boolean fue_traducido() {
        return fue_traducido;
    }

    public void set_traducido() {
        this.fue_traducido = true;
    }

    public void set_offset (int offset) {
        this.offset = offset;
    }

    public int get_offset() {
        return offset;
    }

    public void set_clase_base(String nombre_clase){
        this.clase_base = nombre_clase;
    }

    public String get_clase_base() {
        return clase_base;
    }

    public String get_etiqueta() {
        return "l"+this.getNombre()+"_"+offset+"_"+clase_base;
    }
}
