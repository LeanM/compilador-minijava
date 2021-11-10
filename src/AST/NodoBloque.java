package AST;

import AST.Sentencia.NodoSentencia;
import AST.Sentencia.NodoVarLocal;
import AnalizadorLexico.Token;
import AnalizadorSemantico.*;

import java.util.Hashtable;
import java.util.LinkedList;

public class NodoBloque extends NodoSentencia {

    private LinkedList<NodoSentencia> lista_sentencias;
    private Hashtable<String,NodoVarLocal> tabla_var_locales;
    private EntradaUnidad unidad_de_bloque;
    private NodoBloque bloque_padre;

    public NodoBloque(EntradaUnidad unidad){
        super();
        lista_sentencias = new LinkedList<NodoSentencia>();
        tabla_var_locales = new Hashtable<String,NodoVarLocal>();
        unidad_de_bloque = unidad;
        //bloque_padre = new NodoBloque(unidad);
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
    }

    public Hashtable<String, NodoVarLocal> get_tabla_var_locales() {
        return tabla_var_locales;
    }

    public void set_bloque_padre(NodoBloque padre) {
        this.bloque_padre = padre;
    }

    public NodoBloque get_bloque_padre() { return bloque_padre; }

    @Override
    public void mostar_sentencia() {
        System.out.println("NodoBloque : ");
        for(NodoSentencia ns : lista_sentencias)
            ns.mostar_sentencia();
    }
}
