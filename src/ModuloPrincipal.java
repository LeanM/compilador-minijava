import AST.NodoBloque;
import AnalizadorLexico.Analizador_Lexico;
import AnalizadorLexico.GestorArchivo;
import AnalizadorLexico.Token;
import AnalizadorSemantico.*;
import AnalizadorSintactico.Analizador_Sintactico;
import Traductor.Traductor;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.LinkedList;

public class ModuloPrincipal {
    private static GestorArchivo gestorArchivo;
    private static Analizador_Lexico analizador_lexico;
    private static Analizador_Sintactico analizador_sintactico;

    public static void main (String [] args){
        TablaSimbolos.getInstance().inicializar_tabla_simbolos();
        //String programa = "pruebaTRADUCTOR_vars_locales.txt";
        String programa = "prueba_simple.txt";
        try {
            //programa = args[0];
        }
        catch (ArrayIndexOutOfBoundsException e){ e.printStackTrace();}

        File codigo = new File(programa);

        try {
            gestorArchivo = new GestorArchivo(codigo);
            analizador_lexico = new Analizador_Lexico(gestorArchivo);
            analizador_sintactico = new Analizador_Sintactico(analizador_lexico);
            analizador_sintactico.inicial();
            TablaSimbolos.getInstance().chequeo_semantico();
            TablaSimbolos.getInstance().chequeo_sentencias();
            if(!TablaSimbolos.getInstance().huboErrores())
                Traductor.getInstance().traducir();

            mostrarClases(); //fines de prueba
            //mostrarAST();
            if (!analizador_lexico.hubo_errores() && !analizador_sintactico.hubo_errores() && !TablaSimbolos.getInstance().huboErrores() && !Traductor.getInstance().hubo_errores()) {
                System.out.println("Compilacion Exitosa");
                System.out.println();
                System.out.println("[SinErrores]");
            }
            TablaSimbolos.getInstance().vaciarTablaSimbolos(); //Para los testers
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void mostrarToken(Token tok){
        System.out.println("(" + tok.get_id_token() + "," + tok.get_lexema() + "," + tok.get_nro_linea() + ")");
    }

    private static void mostrarClases(){
        EntradaClase entradaClase;
        LinkedList<EntradaMetodo> lista_metodos_actuales;
        EntradaAtributo atributo_actual;
        EntradaConstructor constructor_actual;
        Enumeration<EntradaClase> enum_clases = TablaSimbolos.getInstance().get_tabla_clases().elements();
        while (enum_clases.hasMoreElements()) {
            entradaClase = enum_clases.nextElement();
            System.out.println("Clase : "+entradaClase.get_token_clase().get_lexema());
            System.out.println("Atributos : ");
            Enumeration<EntradaAtributo> enum_atributos = entradaClase.get_tabla_atributos().elements();
            while (enum_atributos.hasMoreElements()) {
                atributo_actual = enum_atributos.nextElement();
                System.out.println(atributo_actual.getTipo().getNombre()+" "+atributo_actual.get_token_atributo().get_lexema());
                System.out.println("Offset de atributo : "+atributo_actual.get_offset());
            }
            System.out.println("Constructores : ");
            LinkedList<EntradaConstructor> lista_constructores = entradaClase.get_lista_constructores();
            for(EntradaConstructor ec : lista_constructores) {
                System.out.println(ec.get_token_unidad().get_lexema()+" ("+ec.get_lista_argumentos().toString() +")");
                for(EntradaParametro ep : ec.get_lista_argumentos())
                    System.out.println("Parametro const : "+ep.getNombre()+" "+ep.get_offset());
                ec.get_bloque_principal().mostrar_offset_var_locales();
            }
            System.out.println("Metodos : ");
            Enumeration<LinkedList<EntradaMetodo>> enum_metodos = entradaClase.get_tabla_metodos().elements();
            while (enum_metodos.hasMoreElements()) {
                lista_metodos_actuales = enum_metodos.nextElement();
                for(EntradaMetodo em : lista_metodos_actuales) {
                    System.out.println(em.get_token_unidad().get_lexema() + " (" + em.get_lista_argumentos().toString() + ")");
                    System.out.println("Offset de metodo : "+em.get_offset());
                    for(EntradaParametro ep : em.get_lista_argumentos())
                        System.out.println("Parametro met : "+ep.getNombre()+" "+ep.get_offset());
                    em.get_bloque_principal().mostrar_offset_var_locales();
                }
            }
            System.out.println("--------------------------");
        }
    }

    private static void mostrarAST() {
        EntradaClase entradaClase;
        Enumeration<EntradaClase> enum_clases = TablaSimbolos.getInstance().get_tabla_clases().elements();

        while (enum_clases.hasMoreElements()) {
            entradaClase = enum_clases.nextElement();
            System.out.println("CLASE ::::: "+entradaClase.getNombre());
            Enumeration<LinkedList<EntradaMetodo>> enum_metodos = entradaClase.get_tabla_metodos().elements();
            while (enum_metodos.hasMoreElements()) {
                for (EntradaMetodo metodo : enum_metodos.nextElement()) {
                    System.out.println("METODO :::::: " + metodo.getNombre());
                    NodoBloque bloque_metodo = metodo.get_bloque_principal();
                    bloque_metodo.mostar_sentencia();
                }
            }

        }
    }
}
