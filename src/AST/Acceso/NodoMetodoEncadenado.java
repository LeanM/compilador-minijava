package AST.Acceso;

import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;
import AnalizadorSemantico.*;

import java.util.LinkedList;

public class NodoMetodoEncadenado extends NodoEncadenado{

    private LinkedList<NodoExpresion> argumentos;

    public NodoMetodoEncadenado(Token nombre,LinkedList<NodoExpresion> args, NodoPrimario nodoPrimario){
        super(nombre,nodoPrimario);
        argumentos = args;
    }

    public void setArgumentos(LinkedList<NodoExpresion> args){
        this.argumentos = args;
    }

    @Override
    public Tipo obtener_tipo() throws ExcepcionTipo, ExcepcionSemantica {
        Tipo toReturn;
        EntradaMetodo em;
        if(nodo_primario.get_pos_en_encadenados(this) == 0){
            em = TablaSimbolos.getInstance().conforma_metodo(token_nombre,argumentos, nodo_primario.get_tipo_acceso().getNombre());
            toReturn = em.get_tipo();
        }
        else {
            NodoEncadenado encadenado_izq = nodo_primario.get_encadenado_izq(this);
            em = TablaSimbolos.getInstance().conforma_metodo(token_nombre,argumentos, encadenado_izq.obtener_tipo().getNombre());
            toReturn = em.get_tipo();
        }

        return toReturn;
    }

    @Override
    public void esta_bien_definido() throws ExcepcionTipo, ExcepcionSemantica {
        int pos_en_encadenados = nodo_primario.get_pos_en_encadenados(this);
        EntradaClase entradaClase;
        LinkedList<EntradaMetodo> lista_metodos;
        boolean iguales = false;
        if(pos_en_encadenados == 0){
            entradaClase = TablaSimbolos.getInstance().get_tabla_clases().get(nodo_primario.key_clase);
            lista_metodos = entradaClase.get_tabla_metodos().get(token_nombre.get_lexema());
            Tipo tipo_metodo = new TipoReferencia(null); //Inicializacion null
            for (EntradaMetodo em : lista_metodos){
                LinkedList<EntradaParametro> args_metodo = em.get_lista_argumentos();
                iguales = this.conforma_argumentos(args_metodo);
                if(iguales) {
                    tipo_metodo = em.get_tipo();
                    break;
                }
            }
            if(!iguales)
                throw new ExcepcionTipo("La llamada a metodo no es correctamente tipada para ninguno de los metodos de la clase.");

            EntradaClase clase_retorno_encadenado_izq = TablaSimbolos.getInstance().get_entrada_clase(tipo_metodo.getNombre());

        }
    }

    public void esta_bien_definidoa() throws ExcepcionTipo, ExcepcionSemantica {
        int pos_en_encadenados = nodo_primario.get_pos_en_encadenados(this);
        EntradaMetodo metodo_conforma;
        NodoEncadenado encadenado_izq;
        Tipo tipo_metodo_var_izq;

        if(pos_en_encadenados > 0){
            encadenado_izq = nodo_primario.get_encadenado_izq(this);
            tipo_metodo_var_izq = encadenado_izq.obtener_tipo();
            metodo_conforma = TablaSimbolos.getInstance().conforma_metodo(token_nombre,argumentos,tipo_metodo_var_izq.getNombre());
            if(metodo_conforma == null)
                throw new ExcepcionTipo("La llamada a metodo no conforma con ningun metodos de la clase.");
        }
        else {
            //pos_en_encadenados == 0
            tipo_metodo_var_izq = nodo_primario.get_tipo_acceso();
            metodo_conforma = TablaSimbolos.getInstance().conforma_metodo(token_nombre,argumentos,tipo_metodo_var_izq.getNombre());
            if(metodo_conforma == null)
                throw new ExcepcionTipo("La llamada a metodo no conforma con ningun metodos de la clase.");
        }
    }

    public boolean conforma_argumentos(LinkedList<EntradaParametro> argumentos_formales) throws ExcepcionTipo, ExcepcionSemantica {
        boolean iguales = true;

        if(argumentos.size() == argumentos_formales.size()) {
            iguales = true;
            for (int i = 0; i < argumentos.size() && iguales; i++) {
                if(!this.argumentos.get(i).get_tipo_expresion().es_de_tipo(argumentos_formales.get(i).get_tipo()))
                    iguales = false;
            }
        }
        else
            iguales = false;

        return iguales;
    }
}
