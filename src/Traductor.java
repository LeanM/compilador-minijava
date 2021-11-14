import AnalizadorSemantico.*;

import java.util.Enumeration;
import java.util.LinkedList;

public class Traductor {

    private boolean hubo_errores;

    private static Traductor instance = null;

    private Traductor(){
        hubo_errores = false;
    }

    public static Traductor getInstance() {
        if(instance == null)
            instance = new Traductor();

        return  instance;
    }

    public void consolidar_offsets_clases() {
        int next_offset = 1, next_offset_atr = 1;
        EntradaClase object = TablaSimbolos.getInstance().get_entrada_clase("Object");
        //Pongo offset al metodo debugPrint de Object (el unico metodo de la clase)
        object.get_tabla_metodos().get("debugPrint").getFirst().set_offset(next_offset++);
        System.out.println("OFFSET DEBUGPRINT : "+object.get_tabla_metodos().get("debugPrint").getFirst().get_offset());
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
                    else
                        metodo_hijo.set_offset(offset_base++);
                }
                else {
                    //Si el padre no contiene un metodo con el mismo nombre
                    metodo_hijo.set_offset(offset_base++);
                }
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

    public void gen(String instruccion) {}

    public boolean hubo_errores() {
        return hubo_errores;
    }
}
