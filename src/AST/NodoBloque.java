package AST;

import AST.Sentencia.NodoSentencia;
import AST.Sentencia.NodoVarLocal;
import AnalizadorLexico.Token;
import AnalizadorSemantico.*;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;

public class NodoBloque extends NodoSentencia {

    private LinkedList<NodoSentencia> lista_sentencias;
    private Hashtable<String,NodoVarLocal> tabla_var_locales;
    private EntradaUnidad unidad_de_bloque;
    private NodoBloque bloque_padre;
    private LinkedList<NodoBloque> lista_bloques_hijos;

    public NodoBloque(EntradaUnidad unidad){
        super();
        lista_sentencias = new LinkedList<NodoSentencia>();
        tabla_var_locales = new Hashtable<String,NodoVarLocal>();
        unidad_de_bloque = unidad;
        lista_bloques_hijos = new LinkedList<NodoBloque>();
    }

    public void setSentencia(NodoSentencia sentencia_nueva) {
        lista_sentencias.addLast(sentencia_nueva);
    }
    public LinkedList<NodoSentencia> get_lista_sentencias(){ return lista_sentencias; }

    public void esta_bien_definido() throws ExcepcionTipo, ExcepcionSemantica {
        //Verificar toda la lista de sentencias
        for(NodoSentencia ns : lista_sentencias)
            ns.esta_bien_definido();
    }

    public void set_var_local(String nombre_var, NodoVarLocal nodo) throws ExcepcionSemantica {
        if(tabla_var_locales.containsKey(nombre_var))
            throw new ExcepcionSemantica(nodo.get_token(),"Ya hay una variable con el mismo nombre definida en el alcance");
        tabla_var_locales.put(nombre_var,nodo);
        unidad_de_bloque.set_var_local(nombre_var,nodo);
    }

    public Hashtable<String, NodoVarLocal> get_tabla_var_locales() {
        return tabla_var_locales;
    }

    public void set_bloque_padre(NodoBloque padre) {
        this.bloque_padre = padre;
    }

    public NodoBloque get_bloque_padre() { return bloque_padre; }

    public void set_bloque_hijo(NodoBloque bloque_hijo){
        lista_bloques_hijos.addLast(bloque_hijo);
    }

    @Override
    public void mostar_sentencia() {
        for(NodoSentencia ns : lista_sentencias)
            ns.mostar_sentencia();
    }

    public void generar_codigo() throws ExcepcionTipo, ExcepcionSemantica, IOException {
        //Debo reservar espacio para las variables locales de este bloque, al salir del bloque
        //debo liberar el espacio

        Traductor.getInstance().gen("RMEM "+tabla_var_locales.size());

        for(NodoSentencia ns : lista_sentencias)
            ns.generar_codigo();

        Traductor.getInstance().gen("FMEM "+tabla_var_locales.size());
    }

    public int set_offset_var_locales(int offset_base) {
        Enumeration<NodoVarLocal> enum_var_locales = tabla_var_locales.elements();
        NodoVarLocal var_local;
        while (enum_var_locales.hasMoreElements()){
            var_local = enum_var_locales.nextElement();
            var_local.set_offset(offset_base--);
        }

        for(NodoBloque bloque_hijo : lista_bloques_hijos)
            offset_base = bloque_hijo.set_offset_var_locales(offset_base);

        return offset_base;
    }

    public void mostrar_offset_var_locales() {
        Enumeration<NodoVarLocal> enum_locales = tabla_var_locales.elements();
        NodoVarLocal var;
        while(enum_locales.hasMoreElements()){
            var = enum_locales.nextElement();
            System.out.println("Offset var local : "+var.get_token().get_lexema()+" es : "+var.get_offset());
        }

        for(NodoBloque hijo : lista_bloques_hijos){
            hijo.mostrar_offset_var_locales();
        }
    }
}
