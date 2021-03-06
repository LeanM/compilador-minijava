package Traductor;

import AnalizadorSemantico.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.LinkedList;

public class Traductor {

    private boolean hubo_errores;
    private File codigo_output;
    private BufferedWriter bw;
    private String modo_actual, nombre_salida;

    private static Traductor instance = null;

    private Traductor() {
        nombre_salida = "codigo_output.txt";
        Index_etiquetas.getInstance();
        Label_String_Generador.getInstance();
        hubo_errores = false;
    }

    public void set_nombre_salida(String nombre) {
        this.nombre_salida = nombre;
    }

    public static Traductor getInstance() throws IOException {
        if(instance == null)
            instance = new Traductor();

        return  instance;
    }


    public void traducir() throws IOException {
        try {
            codigo_output = new File(nombre_salida);
            FileWriter fw = new FileWriter(codigo_output);
            bw = new BufferedWriter(fw);
            modo_actual = ".CODE";
            bw.write((char) 9);
            bw.write(".CODE");
            rutina_inicializacion();
            consolidar_offsets_clases();
            generar_clases_general();
            finalizar_output();
        }
        catch (ExcepcionTipo | ExcepcionSemantica e) {
            e.printStackTrace();
        }
    }

    public void consolidar_offsets_clases() {
        int next_offset = 1, next_offset_atr = 1;
        EntradaClase object = TablaSimbolos.getInstance().get_entrada_clase("Object");

        Enumeration<EntradaClase> enum_clases = TablaSimbolos.getInstance().get_tabla_clases().elements();
        EntradaClase clase;
        LinkedList<EntradaClase> hijos_object = new LinkedList<EntradaClase>();
        while(enum_clases.hasMoreElements()){
            clase = enum_clases.nextElement();
            if(clase.getClaseSuper().get_lexema().equals("Object") && !clase.getNombre().equals("Object"))
                hijos_object.add(clase);
        }

        try {
            for (EntradaClase hijo_object : hijos_object) {
                consolidar_offsets_constructores(hijo_object);
                consolidar_offsets_metodos(object, hijo_object, next_offset);
                consolidar_offsets_atributos(object,hijo_object,next_offset_atr);
            }
        }
        catch (ExcepcionSemantica e) {
            e.printStackTrace(); hubo_errores = true;
        }
    }

    public void consolidar_offsets_metodos(EntradaClase padre,EntradaClase hijo, int offset_base) throws ExcepcionSemantica {
        EntradaMetodo metodo_redefinido_heredado;
        Enumeration<LinkedList<EntradaMetodo>> enum_metodos_hijo = hijo.get_tabla_metodos().elements();
        LinkedList<EntradaMetodo> lista_metodos_hijo, lista_metodos_padre;
        while(enum_metodos_hijo.hasMoreElements()){
            lista_metodos_hijo = enum_metodos_hijo.nextElement();
            for(EntradaMetodo metodo_hijo : lista_metodos_hijo){
                if(padre.get_tabla_metodos().containsKey(metodo_hijo.getNombre())){
                    //Si el padre contiene un metodo con el mismo nombre
                    metodo_redefinido_heredado = null;
                    lista_metodos_padre = padre.get_tabla_metodos().get(metodo_hijo.getNombre());
                    for(int i = 0; i < lista_metodos_padre.size() && metodo_redefinido_heredado == null; i++){
                        if(metodo_hijo.metodos_iguales(lista_metodos_padre.get(i)))
                            metodo_redefinido_heredado = lista_metodos_padre.get(i);
                    }
                    if(metodo_redefinido_heredado != null) {
                        metodo_hijo.set_offset(metodo_redefinido_heredado.get_offset());
                    }
                    else {
                        if (!metodo_hijo.es_estatico())
                            metodo_hijo.set_offset(offset_base++);
                    }
                }
                else {
                    //Si el padre no contiene un metodo con el mismo nombre
                    if(!metodo_hijo.es_estatico())
                        metodo_hijo.set_offset(offset_base++);
                }
                consolidar_offsets_varlocales_params(metodo_hijo);
            }
        }

        Enumeration<EntradaClase> enum_clases = TablaSimbolos.getInstance().get_tabla_clases().elements();
        EntradaClase clase;
        LinkedList<EntradaClase> lista_descendientes_hijo = new LinkedList<EntradaClase>();
        while(enum_clases.hasMoreElements()){
            clase = enum_clases.nextElement();
            if(clase.getClaseSuper().get_lexema().equals(hijo.getNombre()))
                lista_descendientes_hijo.add(clase);
        }

        for (EntradaClase descendiente : lista_descendientes_hijo) {
            consolidar_offsets_metodos(hijo, descendiente, offset_base);
        }
    }

    public void consolidar_offsets_atributos(EntradaClase padre,EntradaClase hijo, int offset_base){
        EntradaAtributo atributo_tapado;
        Enumeration<EntradaAtributo> enum_atributos_hijo = hijo.get_tabla_atributos().elements();
        EntradaAtributo atributo_hijo;
        while(enum_atributos_hijo.hasMoreElements()){
            atributo_hijo = enum_atributos_hijo.nextElement();
            if(padre.get_tabla_atributos().containsKey(atributo_hijo.getNombre()))
                //Asumo que sea que lo tapa o que lo haya heredado tienen que tener el mismo offset igualmente
                atributo_hijo.set_offset(padre.get_tabla_atributos().get(atributo_hijo.getNombre()).get_offset());
            else
                atributo_hijo.set_offset(offset_base++);
        }

        Enumeration<EntradaClase> enum_clases = TablaSimbolos.getInstance().get_tabla_clases().elements();
        EntradaClase clase;
        LinkedList<EntradaClase> lista_descendientes_hijo = new LinkedList<EntradaClase>();
        while(enum_clases.hasMoreElements()){
            clase = enum_clases.nextElement();
            if(clase.getClaseSuper().get_lexema().equals(hijo.getNombre()))
                lista_descendientes_hijo.add(clase);
        }

        for (EntradaClase descendiente : lista_descendientes_hijo) {
            consolidar_offsets_atributos(hijo, descendiente, offset_base);
        }

    }

    public void consolidar_offsets_varlocales_params(EntradaUnidad unidad){
        int offset_base = 4 + unidad.get_lista_argumentos().size() - 1;; //Por que en el RA los primeros 3 offset estan reservados
        //Offset inicializado en caso constructor o metodo statico

        if (unidad instanceof EntradaMetodo) {
            if((((EntradaMetodo) unidad).es_estatico()))
                //Si el metodo es estatico (no tiene this)
                offset_base = 3 + unidad.get_lista_argumentos().size() - 1;
        }

        //Asigno los primeros offset a los parametros de la unidad
        for(EntradaParametro ep : unidad.get_lista_argumentos()) {
            ep.set_offset(offset_base--);
        }

        unidad.get_bloque_principal().set_offset_var_locales(0); //Paso como offset base 0
    }

    public void consolidar_offsets_constructores(EntradaClase clase){
        for (EntradaConstructor ec : clase.get_lista_constructores()) {
            consolidar_offsets_varlocales_params(ec);
        }

        Enumeration<EntradaClase> enum_clases = TablaSimbolos.getInstance().get_tabla_clases().elements();
        EntradaClase clase_hija;
        LinkedList<EntradaClase> lista_descendientes_hijo = new LinkedList<EntradaClase>();
        while(enum_clases.hasMoreElements()){
            clase_hija = enum_clases.nextElement();
            if(clase_hija.getClaseSuper().get_lexema().equals(clase.getNombre()))
                lista_descendientes_hijo.add(clase_hija);
        }

        for (EntradaClase descendiente : lista_descendientes_hijo) {
            consolidar_offsets_constructores(descendiente);
        }
    }

    public void generar_clases_general() throws IOException, ExcepcionTipo, ExcepcionSemantica {
        Enumeration<EntradaClase> enum_clases = TablaSimbolos.getInstance().get_tabla_clases().elements();
        LinkedList<EntradaMetodo> etiquetas_metodos;
        EntradaClase clase;
        while (enum_clases.hasMoreElements()){
            clase = enum_clases.nextElement();
            etiquetas_metodos = clase.get_metodos_ordenados_offset();
            generar_clase_especifico(clase,etiquetas_metodos);
        }
    }

    public void generar_clase_especifico(EntradaClase clase, LinkedList<EntradaMetodo> etiquetas_metodos) throws IOException, ExcepcionTipo, ExcepcionSemantica {
        this.set_modo_data();
        String etiquetas_string = "";

        if (!etiquetas_metodos.isEmpty())
            etiquetas_string = etiquetas_metodos.get(0).get_etiqueta();

        for (int i = 1; i < etiquetas_metodos.size(); i++) {
            etiquetas_string = etiquetas_string + "," + etiquetas_metodos.get(i).get_etiqueta();
        }
        if(etiquetas_metodos.isEmpty())
            gen_etiqueta("VT_"+clase.getNombre());
        else {
            gen_etiqueta("VT_" + clase.getNombre());
            gen("DW " + etiquetas_string);
        }

        this.set_modo_code();
        //A partir de aca van los metodos y el codigo de los mismos

        Enumeration<LinkedList<EntradaMetodo>> enum_metodos = clase.get_tabla_metodos().elements();
        LinkedList<EntradaMetodo> lista_metodos;
        while(enum_metodos.hasMoreElements()){
            lista_metodos = enum_metodos.nextElement();
            for(EntradaMetodo em : lista_metodos){
                if(!em.fue_traducido()){
                    //Generar code de cada metodo
                    gen_etiqueta(em.get_etiqueta());
                    em.generar_codigo();
                    em.set_traducido();
                }
            }
        }

        for (EntradaConstructor ec : clase.get_lista_constructores()) {
            //Generar code constructor
            gen_etiqueta(ec.get_etiqueta());
            ec.generar_codigo();
        }

    }

    public void set_modo_data() throws IOException {
        if(!modo_actual.equals(".DATA")) {
            bw.newLine();
            bw.write((char) 9);
            bw.write(".DATA");
            this.modo_actual = ".DATA";
        }
    }

    public void set_modo_code() throws IOException {
        if(!modo_actual.equals(".CODE")) {
            bw.newLine();
            bw.write((char) 9);
            bw.write(".CODE");
            this.modo_actual = ".CODE";
        }
    }

    public void set_modo_stack() throws IOException {
        if(!modo_actual.equals(".STACK")){
            bw.newLine();
            bw.write((char) 9);
            bw.write(".STACK");
            this.modo_actual = ".STACK";
        }
    }

    public void gen(String instruccion) throws IOException {
        bw.newLine();
        bw.write((char) 9);
        bw.write(instruccion);
    }

    public void gen_comment_stack(String comment) throws IOException {
        bw.write((char) 9);
        bw.write(";"+comment);
    }

    public void gen_etiqueta(String etiqueta) throws IOException {
        bw.newLine();
        bw.write(etiqueta+":");
        bw.write((char) 9);
        bw.write("NOP");
    }

    public void gen_basico(String text) throws IOException {
        bw.newLine();
        bw.write(text);
    }

    public File finalizar_output() throws IOException {
        bw.close();
        return codigo_output;
    }

    public void rutina_inicializacion() throws IOException {
        //this.set_modo_code();
        //this.set_modo_heap();
        //Inicializacion del heap
        //No es necesario
        //gen("PUSH lheap");
        //gen("RET 0");

        this.set_modo_code();
        //Inicializacion resto
        gen("PUSH lmain");
        gen("CALL");
        gen("HALT");

        //Inicializacion rutina malloc para el heap
        gen_etiqueta("lmalloc");
        gen("LOADFP");
        gen("LOADSP");
        gen("STOREFP");
        gen("LOADHL");
        gen("DUP");
        gen("PUSH 1");
        gen("ADD");
        gen("STORE 4");
        gen("LOAD 3");
        gen("ADD");
        gen("STOREHL");
        gen("STOREFP");
        gen("RET 1");
    }

    public boolean hubo_errores() {
        return hubo_errores;
    }
}
