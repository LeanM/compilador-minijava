package AnalizadorSemantico;
import AnalizadorLexico.Token;

import java.util.*;

public class TablaSimbolos {

    private Hashtable<String,EntradaClase> tabla_clases;
    private EntradaClase claseActual;
    private EntradaUnidad unidadActual;
    private boolean huboErrores;

    private static TablaSimbolos instance = null;

    public static TablaSimbolos getInstance() {
        if (instance == null) {
            instance = new TablaSimbolos();
            return instance;
        }
        else return instance;
    }
    private TablaSimbolos(){
        tabla_clases = new Hashtable<String,EntradaClase>();
        inicializar_clase_Object();
        inicializar_clase_System();
        huboErrores = false;
    }

    public void agregarClase(String nombreClase,EntradaClase clase) throws ExcepcionSemantica {
       if(!tabla_clases.containsKey(nombreClase))
            tabla_clases.put(nombreClase,clase);
       else throw new ExcepcionSemantica(clase.get_token_clase(),"Error Semantico en linea "+clase.get_token_clase().get_nro_linea() +": La clase "+nombreClase+" ya esta declarada.");
    }
    public Hashtable<String,EntradaClase> get_tabla_clases(){  return tabla_clases; }

    public void set_padre_clase_actual(Token clase_padre) {
        claseActual.setClaseSuper(clase_padre);
    }
    public void setUnidadActual(EntradaUnidad unidad) {
        this.unidadActual = unidad;
    }
    public EntradaUnidad getUnidadActual(){
        return unidadActual;
    }
    public void setClaseActual(EntradaClase clase) {
        this.claseActual = clase;
    }
    public EntradaClase getClaseActual(){
        return claseActual;
    }

    public boolean clase_esta_declarada(String nombre_clase) {
        boolean toReturn = false;
        if(tabla_clases.containsKey(nombre_clase))
            toReturn = true;
        return toReturn;
    }

    public void chequeo_semantico() throws ExcepcionSemantica {

        //Esta es la segunda pasada del analisis semantico
        //Primero aca debo actualizar los metodos y atributos pertenecientes a cada clase los cuales van a cambiar por la herencia
        //Podria tambien verificar la herencia circular por fuera del "esta_bien_declarada()" aca para ya descartar esa posiblidad
        // y ya actualizar los metodos y atributos.

        try {
            boolean existe_main = false;
            EntradaClase entradaClase;
            Enumeration<EntradaClase> enum_clases = tabla_clases.elements();

            //Verifico para cada clase si esta bien declarada y consolido metodos y atributos
            //Tambien verifico que exista un y solo un main entre todas las clases del sistema
            while (enum_clases.hasMoreElements()) {
                entradaClase = enum_clases.nextElement();
                entradaClase.esta_bien_declarada();
                if(!entradaClase.consolido_metodos())
                    consolidarMetodos(entradaClase);    //Realizo la consolidacion de metodos
                if(!entradaClase.consolido_atributos())
                    consolidar_atributos(entradaClase); //Realizo la consolidacion de atributos

                if (entradaClase.get_tabla_metodos().containsKey("main")) {
                    LinkedList<EntradaMetodo> lista_mains = entradaClase.get_tabla_metodos().get("main");
                    for(EntradaMetodo em : lista_mains){
                        if((em.get_lista_argumentos().size() == 0) && (em.get_alcance().equals("static")) && (em.get_tipo().getNombre().equals("void"))) {
                            if(!existe_main) {
                                existe_main = true;
                                break;
                            }
                            else
                                throw new ExcepcionSemantica(em.get_token_metodo(),"Error Semantico en linea "+em.get_token_metodo().get_nro_linea()+" : No puede haber mas de un main declarado.");
                        }
                    }
                }

            }
            if (!existe_main)
                throw new ExcepcionSemantica(new Token("EOF","",0),"Error Semantico, no existe un metodo main sin argumentos en ninguna clase del sistema.");
        }
        catch(ExcepcionSemantica e) { e.printStackTrace(); huboErrores = true;}
    }

    public EntradaClase get_entrada_clase(String nombre_clase) {
        return tabla_clases.get(nombre_clase);
    }

    private void consolidarMetodos(EntradaClase clase) throws ExcepcionSemantica {
        Hashtable<String,LinkedList<EntradaMetodo>> tabla_metodos;
        LinkedList<EntradaMetodo> lista_metodos_actuales;
        if(!clase.getClaseSuper().get_lexema().equals("Object")){
            consolidarMetodos(tabla_clases.get(clase.getClaseSuper().get_lexema()));
        }
        tabla_metodos = tabla_clases.get(clase.getClaseSuper().get_lexema()).get_tabla_metodos();
        Enumeration<LinkedList<EntradaMetodo>> enum_metodos = tabla_metodos.elements();
        while (enum_metodos.hasMoreElements()) {
            lista_metodos_actuales = enum_metodos.nextElement();
            for(EntradaMetodo em : lista_metodos_actuales)
                consolidarMetodo_a_clase(em,clase);
        }
        clase.metodos_consolidados();
    }

    private void consolidarMetodo_a_clase(EntradaMetodo metodo, EntradaClase clase) throws ExcepcionSemantica {
        boolean hayRedefinicion = false;
        //Para el logro : Si son iguales no lo paso a la clase hija, si son distintos lo paso (ahora las clases pueden tener varios metodos con el mismo nombre)
        if(clase.get_tabla_metodos().containsKey(metodo.getNombre())){
            //Redefinicion
            LinkedList<EntradaMetodo> lista_metodos = clase.get_tabla_metodos().get(metodo.getNombre());
            for (EntradaMetodo em : lista_metodos) {
                if (!em.metodos_iguales(metodo)) {
                    if(em.mismos_argumentos(metodo.get_lista_argumentos()))
                        //Si los metodos no son iguales, tienen el mismo nombre pero distintos parametros y/o tipo
                        throw new ExcepcionSemantica(em.get_token_metodo(), "Error Semantico en linea " + em.get_token_metodo().get_nro_linea() + ": El metodo " + metodo.getNombre() + " tiene el mismo nombre y parametros que uno en la clase super, pero distintos tipos de retorno. No cumple las condiciones de redefinicion.");
                    else {
                        //Tiene distintos argumentos, por lo tanto lo debo pasar a la clase hija

                    }
                }
                else {
                    hayRedefinicion = true;
                    break;
                }
                //Si los metodos son iguales, no hago nada, es decir, no paso el metodo de super a la clase hija por que este esta redefinido.
            }
            if (!hayRedefinicion) //No hay error de metodos en la clase hija con mismos argumentos pero distinto tipo de retorno
                //Si no hubo redefinicion, al no haber errores con metodos en la clase hija se debe agregar
                clase.setMetodo(metodo.getNombre(),metodo);
        }
        else {
            clase.setMetodo(metodo.getNombre(),metodo);
        }
    }

    private void consolidar_atributos(EntradaClase clase) throws ExcepcionSemantica {
        Hashtable<String,EntradaAtributo> tabla_atributos;
        EntradaAtributo atr_actual;
        if(clase.getClaseSuper().get_lexema().equals("Object")){
            // Object no tiene atributos, no hacer nada
        }
        else {
            consolidar_atributos(tabla_clases.get(clase.getClaseSuper().get_lexema()));
            tabla_atributos = tabla_clases.get(clase.getClaseSuper().get_lexema()).get_tabla_atributos();
            Enumeration<EntradaAtributo> enum_atributos = tabla_atributos.elements();
            while (enum_atributos.hasMoreElements()) {
                atr_actual = enum_atributos.nextElement();
                if (atr_actual.get_visibilidad().equals("public"))
                    consolidar_atributo_a_clase(atr_actual, clase);
            }
            clase.atributos_consolidados();
        }
    }

    private void consolidar_atributo_a_clase(EntradaAtributo atr, EntradaClase clase) throws ExcepcionSemantica {
        if(!clase.get_tabla_atributos().containsKey(atr.getNombre()))
            clase.setAtributo(atr.getNombre(),atr);
        else {
            //Agregar atributo a atributos ocultos de la clase
        }
        //else throw new ExcepcionSemantica(clase.get_tabla_atributos().get(atr.getNombre()).get_token_atributo(),"Error Semantico en linea "+clase.get_tabla_atributos().get(atr.getNombre()).get_token_atributo().get_nro_linea() +": El atributo "+atr.getNombre()+" tiene el mismo nombre que uno en la clase super.");
        //no hago nada (logro atributos tapados)
    }

    private void inicializar_clase_Object() {
        EntradaClase entrada_object = new EntradaClase_Object(new Token("idClase","Object",0));
        EntradaMetodo debugPrint = new EntradaMetodo(new Token("idMetVar","debugPrint",0),"static",new TipoPrimitivo(new Token("pr_void","void",0)));
        EntradaParametro i = new EntradaParametro(new Token("idMetVar","i",0),new TipoPrimitivo(new Token("pr_int","int",0)));
        try {
            debugPrint.setArgumento("i", i);
            entrada_object.setMetodo("debugPrint", debugPrint);
            tabla_clases.put(entrada_object.getNombre(), entrada_object);
        }
        catch (ExcepcionSemantica e) { e.printStackTrace(); huboErrores = true;}
    }
    private void inicializar_clase_System(){
        EntradaClase entrada_System = new EntradaClase_Standar(new Token("idClase","System",0));
        entrada_System.setClaseSuper(tabla_clases.get("Object").token_clase);
        try {
            //Metodo int read()
            EntradaMetodo read = new EntradaMetodo(new Token("idMetVar", "read", 0), "static", new TipoPrimitivo(new Token("pr_int", "int", 0)));
            entrada_System.setMetodo(read.getNombre(), read);
            //Metodo void printB(boolean b)
            EntradaMetodo printB = new EntradaMetodo(new Token("idMetVar","printB",0),"static",new TipoPrimitivo(new Token("pr_void","void",0)));
            EntradaParametro b = new EntradaParametro(new Token("idMetVar","b",0),new TipoPrimitivo(new Token("pr_boolean","boolean",0)));
            printB.setArgumento(b.getNombre(),b);
            entrada_System.setMetodo(printB.getNombre(),printB);
            //Metodo void printC (char c)
            EntradaMetodo printC = new EntradaMetodo(new Token("idMetVar","printC",0),"static",new TipoPrimitivo(new Token("pr_void","void",0)));
            EntradaParametro c = new EntradaParametro(new Token("idMetVar","c",0),new TipoPrimitivo(new Token("pr_char","char",0)));
            printC.setArgumento(c.getNombre(),c);
            entrada_System.setMetodo(printC.getNombre(),printC);
            //Metodo void printI (int i)
            EntradaMetodo printI = new EntradaMetodo(new Token("idMetVar","printI",0),"static",new TipoPrimitivo(new Token("pr_void","void",0)));
            EntradaParametro i = new EntradaParametro(new Token("idMetVar","i",0),new TipoPrimitivo(new Token("pr_int","int",0)));
            printI.setArgumento(i.getNombre(),i);
            entrada_System.setMetodo(printI.getNombre(),printI);
            //Metodo void printS (String s)
            EntradaMetodo printS = new EntradaMetodo(new Token("idMetVar","printS",0),"static",new TipoPrimitivo(new Token("pr_void","void",0)));
            EntradaParametro s = new EntradaParametro(new Token("idMetVar","s",0),new TipoPrimitivo(new Token("pr_String","String",0)));
            printS.setArgumento(s.getNombre(),s);
            entrada_System.setMetodo(printS.getNombre(),printS);
            //Metodo println()
            EntradaMetodo println = new EntradaMetodo(new Token("idMetVar","println",0),"static",new TipoPrimitivo(new Token("pr_void","void",0)));
            entrada_System.setMetodo(println.getNombre(),println);
            //Metodo printBln (boolean b)
            EntradaMetodo printBln = new EntradaMetodo(new Token("idMetVar","printBln",0),"static",new TipoPrimitivo(new Token("pr_void","void",0)));
            EntradaParametro b2 = new EntradaParametro(new Token("idMetVar","b",0),new TipoPrimitivo(new Token("pr_boolean","boolean",0)));
            printBln.setArgumento(b2.getNombre(),b2);
            entrada_System.setMetodo(printBln.getNombre(),printBln);
            //Metodo printCln (char c)
            EntradaMetodo printCln = new EntradaMetodo(new Token("idMetVar","printCln",0),"static",new TipoPrimitivo(new Token("pr_void","void",0)));
            EntradaParametro c2 = new EntradaParametro(new Token("idMetVar","c",0),new TipoPrimitivo(new Token("pr_char","char",0)));
            printCln.setArgumento(c2.getNombre(),c2);
            entrada_System.setMetodo(printCln.getNombre(),printCln);
            //Metodo printIln (int i)
            EntradaMetodo printIln = new EntradaMetodo(new Token("idMetVar","printIln",0),"static",new TipoPrimitivo(new Token("pr_void","void",0)));
            EntradaParametro i2 = new EntradaParametro(new Token("idMetVar","i",0),new TipoPrimitivo(new Token("pr_int","int",0)));
            printIln.setArgumento(i2.getNombre(),i2);
            entrada_System.setMetodo(printIln.getNombre(),printIln);
            //Metodo printSln (String s)
            EntradaMetodo printSln = new EntradaMetodo(new Token("idMetVar","printSln",0),"static",new TipoPrimitivo(new Token("pr_void","void",0)));
            EntradaParametro s2 = new EntradaParametro(new Token("idMetVar","s",0),new TipoPrimitivo(new Token("pr_String","String",0)));
            printSln.setArgumento(s2.getNombre(),s2);
            entrada_System.setMetodo(printSln.getNombre(),printSln);

            tabla_clases.put(entrada_System.getNombre(), entrada_System);
        }
        catch (ExcepcionSemantica e) { e.printStackTrace(); huboErrores = true;}
    }

    public boolean huboErrores() {return huboErrores;}

    public void vaciarTablaSimbolos(){
        this.tabla_clases = new Hashtable<String,EntradaClase>();
        inicializar_clase_Object();
        inicializar_clase_System();
    }
}
