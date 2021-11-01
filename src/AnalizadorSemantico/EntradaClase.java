package AnalizadorSemantico;

import AnalizadorLexico.Token;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;

public abstract class EntradaClase {

    protected Token token_clase;
    protected LinkedList<EntradaConstructor> lista_constructores;
    protected Hashtable<String,EntradaAtributo> tabla_atributos; //Agregar los atributos ocultos por el logro, hay que almacenarlos, los de toda la jerarquia (con alguna distincion)
    protected LinkedList<EntradaAtributo> lista_atributos_ocultos; //En la primer posicion estan los atributos ocultos del padre, en la segunda del abuelo, y asi sucesivamente
    protected LinkedList<String> nombre_clase_atributos_ocultos;
    protected Hashtable<String,LinkedList<EntradaMetodo>> tabla_metodos;
    protected boolean consolido_metodos, consolido_atributos;

    public EntradaClase (Token clase) {
        token_clase = clase;
        lista_constructores = new LinkedList<EntradaConstructor>();
        tabla_atributos = new Hashtable<String,EntradaAtributo>();
        tabla_metodos = new Hashtable<String,LinkedList<EntradaMetodo>>();
        lista_atributos_ocultos = new LinkedList<EntradaAtributo> ();
        nombre_clase_atributos_ocultos = new LinkedList<String>();
        consolido_metodos = consolido_atributos = false;
    }

    public String getNombre(){
        return token_clase.get_lexema();
    }
    public Token get_token_clase(){return token_clase;}

    public void setConstructor(String nombreConstructor, EntradaConstructor constructor) throws ExcepcionSemantica {
        //Tengo q hacer lo de mas de un constructor
        boolean mismos_argumentos = false;
        if (!nombreConstructor.equals(this.getNombre()))
            throw new ExcepcionSemantica(constructor.get_token_constructor(),"Error semantico en linea "+constructor.get_token_constructor().get_nro_linea()+" : El constructor no posee el mismo nombre de la clase a la que pertenece.");
        if(!lista_constructores.isEmpty()) {
            for (int i = 0; i < lista_constructores.size() && !mismos_argumentos; i++) {
                mismos_argumentos = lista_constructores.get(i).mismos_argumentos(constructor.get_lista_argumentos());
            }
            if(mismos_argumentos)
                throw new ExcepcionSemantica(constructor.get_token_constructor(), "Error semantico en linea " + constructor.get_token_constructor().get_nro_linea() + " : El constructor posee los mismos parametros que un constructor ya definido.");
        }
        if(!mismos_argumentos)
            lista_constructores.add(constructor);

    }
    public void setAtributo(String nombreAtributo, EntradaAtributo atributo) throws ExcepcionSemantica {
        if(!tabla_atributos.containsKey(nombreAtributo))
            tabla_atributos.put(nombreAtributo,atributo);
        else throw new ExcepcionSemantica(atributo.get_token_atributo(),"Error Semantico en linea "+atributo.get_token_atributo().get_nro_linea() +": Ya hay un atributo declarado con el nombre "+nombreAtributo);
    }
    public void setAtributoOculto(String nombre_clase, EntradaAtributo ea){
        lista_atributos_ocultos.add(ea);
        nombre_clase_atributos_ocultos.add(nombre_clase);
    }

    public EntradaAtributo getAtributoOculto(String nombre_atributo, String nombre_clase){
        EntradaAtributo toReturn = null;
        for(int i = 0 ; i < lista_atributos_ocultos.size(); i++){
            if(lista_atributos_ocultos.get(i).getNombre().equals(nombre_atributo))
                if(nombre_clase_atributos_ocultos.get(i).equals(nombre_clase))
                    toReturn = lista_atributos_ocultos.get(i);
        }
        return toReturn;
    }

    public void setMetodo(String nombreMetodo, EntradaMetodo metodo) throws ExcepcionSemantica {
        if(!tabla_metodos.containsKey(nombreMetodo))
            insertarMetodoNuevo(nombreMetodo,metodo);
        else {
            //verificar si tiene parametros distintos
            LinkedList<EntradaMetodo> metodos = tabla_metodos.get(nombreMetodo);
            for(EntradaMetodo em : metodos){
                if(em.mismos_argumentos(metodo.get_lista_argumentos()))
                    throw new ExcepcionSemantica(metodo.get_token_metodo(),"Error Semantico en linea "+metodo.get_token_metodo().get_nro_linea() +": Ya hay un metodo declarado con el nombre "+nombreMetodo+" que posee los mismos argumentos.");
            }
            //Si no hay ningun metodo con los mismo argumentos
            metodos.add(metodo);
        }
    }

    private void insertarMetodoNuevo(String nombre, EntradaMetodo entradaMetodo) {
        LinkedList<EntradaMetodo> lista = new LinkedList<EntradaMetodo>();
        lista.add(entradaMetodo);
        tabla_metodos.put(nombre,lista);
    }

    public Hashtable<String,LinkedList<EntradaMetodo>> get_tabla_metodos(){
        return tabla_metodos;
    }
    public Hashtable<String,EntradaAtributo> get_tabla_atributos() { return tabla_atributos; }
    public LinkedList<EntradaConstructor> get_lista_constructores() { return lista_constructores; }
    public void metodos_consolidados(){
        consolido_metodos = true;
    }
    public void atributos_consolidados(){
        consolido_atributos = true;
    }
    public boolean get_consolido_metodos() { return consolido_metodos;}
    public boolean get_consolido_atributos(){return consolido_atributos;}

    public abstract void herenciaCircular() throws ExcepcionSemantica;
    public abstract void setClaseSuper(Token claseSuper);
    public abstract Token getClaseSuper();
    public abstract void esta_bien_declarada() throws ExcepcionSemantica;
    protected abstract void get_lista_ancestros(LinkedList<String> jerarquia_ancestros) throws ExcepcionSemantica;
}
