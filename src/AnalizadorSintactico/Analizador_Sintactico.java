package AnalizadorSintactico;

import AnalizadorLexico.Analizador_Lexico;
import AnalizadorLexico.ExcepcionLexica;
import AnalizadorLexico.Token;
import AnalizadorSemantico.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

public class Analizador_Sintactico {

    private Analizador_Lexico analizador_lexico;
    private Token token_actual;
    private boolean huboErrores;
    private TablaSimbolos TS = TablaSimbolos.getInstance();

    public Analizador_Sintactico(Analizador_Lexico analizador_lexico) {
        this.huboErrores = false;
        this.analizador_lexico = analizador_lexico;
        this.token_actual = this.analizador_lexico.proximo_token();
    }

    public void inicial() {
        try {
            lista_clases();
            match("$");
        }
        catch (ExcepcionSintactica | ExcepcionSemantica e) {
            e.printStackTrace();
            huboErrores = true;
        }
    }

    private void lista_clases() throws ExcepcionSintactica, ExcepcionSemantica {
        clase();
        otraListaClases_Vacio();
    }

    private void clase() throws ExcepcionSintactica, ExcepcionSemantica {
        match("pr_class");
        EntradaClase entrada_clase = new EntradaClase_Standar(token_actual);
        TS.setClaseActual(entrada_clase);
        match("idClase");
        //LinkedList<Token> lista_genericidad = genericidad_Vacio();
        //TS.getClaseActual().setListaGenericas(lista_genericidad);
        Token clase_super = herencia();
        TS.set_padre_clase_actual(clase_super);
        match("llaveAbre");
        lista_miembros();
        match("llaveCierra");
        TS.agregarClase(entrada_clase.getNombre(),entrada_clase);
    }

    private LinkedList<Token> genericidad_Vacio() throws ExcepcionSintactica {
        LinkedList<Token> lista_genericas = new LinkedList<Token>();
        if(token_actual.get_id_token().equals("op<")) {
            match("op<");
            lista_genericas.add(token_actual);
            match("idClase");
            otraGenerica_Vacio(lista_genericas);
            match("op>");
        }
        else {
            //nada por que -> e
        }

        return lista_genericas;
    }

    private void otraGenerica_Vacio(LinkedList<Token> lista_genericas) throws ExcepcionSintactica {
        if(token_actual.get_id_token().equals("pun,")) {
            match("pun,");
            lista_genericas.add(token_actual);
            match("idClase");
            otraGenerica_Vacio(lista_genericas);
        }
        else {
            //nada por que -> e
        }
    }

    private void otraListaClases_Vacio() throws ExcepcionSintactica, ExcepcionSemantica {
        //Primeros clase
        if (token_actual.get_id_token().equals("pr_class")) {
            lista_clases();
        }
        else {
            //nada por que <otraListaClases_Vacio> -> e
        }
    }

    private Token herencia() throws ExcepcionSintactica {
        Token toReturn;
        if (token_actual.get_id_token().equals("pr_extends")) {
            match("pr_extends");
            toReturn = token_actual;
            match("idClase");
        }
        else {
            toReturn = new Token("idClase","Object",token_actual.get_nro_linea());
        }
        return toReturn;
    }

    private void lista_miembros() throws ExcepcionSintactica, ExcepcionSemantica {

        //Primeros de <miembro> <lista_miembros> = primeros de <miembro>
        if (Arrays.asList("pr_public","pr_private","idClase","pr_static","pr_dynamic").contains(token_actual.get_id_token())) {
            miembro();
            lista_miembros();
        }
        else {
            //nada por que <lista_miembros> -> e
        }
    }

    private void miembro() throws ExcepcionSintactica, ExcepcionSemantica {
        if (token_actual.get_id_token().equals("pr_public") || token_actual.get_id_token().equals("pr_private"))
            atributo();
        else
        if(token_actual.get_id_token().equals("idClase"))
            constructor();
        else
        if(token_actual.get_id_token().equals("pr_static") || token_actual.get_id_token().equals("pr_dynamic"))
            metodo();

        else {
            //preguntar por que teoricamente nunca entraria aca por que si o si es una de esas palabras
            throw new ExcepcionSintactica(token_actual, "public,private,idClase,static,dynamic");
        }
    }

    private void atributo() throws ExcepcionSintactica, ExcepcionSemantica {
        String visibilidad = visibilidad();
        String estatico = estatico_Vacio();
        Tipo tipo_atributo = tipo();
        listaDecAtrs(visibilidad,estatico,tipo_atributo);
        match("pun;");
    }


    private String visibilidad() throws ExcepcionSintactica {
        String toReturn;
        if(token_actual.get_id_token().equals("pr_public")) {
            toReturn = token_actual.get_lexema();
            match("pr_public");
        }
        else
        if(token_actual.get_id_token().equals("pr_private")) {
            toReturn = token_actual.get_lexema();
            match("pr_private");
        }
        else throw new ExcepcionSintactica(token_actual,"pr_private o pr_public");

        return toReturn;
    }

    private String estatico_Vacio() throws ExcepcionSintactica {
        String toReturn="";
        if (token_actual.get_id_token().equals("pr_static")) {
            toReturn = token_actual.get_lexema();
            match("pr_static");
        }
        else {
            //nada por que -> e
        }

        return toReturn;
    }

    private Tipo tipo() throws ExcepcionSintactica {
        Tipo toReturn;
        if (token_actual.get_id_token().equals("idClase")) {
            toReturn = new TipoReferencia(token_actual);
            match("idClase");
            //genericidad_Vacio();
        }
        else
            //Primeros de <tipoPrimitivo>
            toReturn = tipoPrimitivo();

        return toReturn;
    }

    private Tipo tipoPrimitivo() throws ExcepcionSintactica {
        Tipo toReturn;
        if(token_actual.get_id_token().equals("pr_char")) {
            toReturn = new TipoPrimitivo(token_actual);
            match("pr_char");
        }
        else
        if(token_actual.get_id_token().equals("pr_int")) {
            toReturn = new TipoPrimitivo(token_actual);
            match("pr_int");
        }
        else
        if(token_actual.get_id_token().equals("pr_String")) {
            toReturn = new TipoPrimitivo(token_actual);
            match("pr_String");
        }
        else
        if(token_actual.get_id_token().equals("pr_boolean")) {
            toReturn = new TipoPrimitivo(token_actual);
            match("pr_boolean");
        }

        else {
            throw new ExcepcionSintactica(token_actual,"idClase o Palabras reservadas : int, String, char o boolean");
        }

        return toReturn;

    }

    private void listaDecAtrs(String visibilidad, String estatico, Tipo tipo) throws ExcepcionSintactica, ExcepcionSemantica {
        if(token_actual.get_id_token().equals("idMetVar")) {
            EntradaAtributo atr = new EntradaAtributo(token_actual,visibilidad,tipo,estatico);
            TS.getClaseActual().setAtributo(atr.getNombre(),atr);
            match("idMetVar");
            otroAtrs_Vacio(visibilidad,estatico,tipo);
        }
        else throw new ExcepcionSintactica(token_actual,"idMetVar");
    }

    private void otroAtrs_Vacio(String visibilidad, String estatico, Tipo tipo) throws ExcepcionSintactica, ExcepcionSemantica {
        if (token_actual.get_id_token().equals("pun,")) {
            match("pun,");
            listaDecAtrs(visibilidad,estatico,tipo);
        }
        else {
            //nada por que otroAtrs_Vacio -> e
        }
    }

    private void metodo() throws ExcepcionSintactica, ExcepcionSemantica {
        String forma = formaMetodo();
        Tipo tipo_metodo = tipoMetodo();
        EntradaMetodo metodo = new EntradaMetodo(token_actual,forma,tipo_metodo);
        TS.setUnidadActual(metodo);
        match("idMetVar");
        argsFormales();
        bloque();
        TS.getClaseActual().setMetodo(metodo.getNombre(),metodo);
    }

    private String formaMetodo() throws ExcepcionSintactica {
        String toReturn;
        if (token_actual.get_id_token().equals("pr_dynamic")) {
            toReturn = token_actual.get_lexema();
            match("pr_dynamic");
        }
        else
        if(token_actual.get_id_token().equals("pr_static")) {
            toReturn = token_actual.get_lexema();
            match("pr_static");
        }
        else {
            throw new ExcepcionSintactica(token_actual,"pr_static,pr_dynamic");
        }

        return toReturn;
    }

    private Tipo tipoMetodo() throws ExcepcionSintactica {
        Tipo tipo_metodo;
        //Primeros tipo
        if (Arrays.asList("idClase","pr_char","pr_boolean","pr_int","pr_String").contains(token_actual.get_id_token()))
            tipo_metodo = tipo();
        else
        if(token_actual.get_id_token().equals("pr_void")) {
            tipo_metodo = new TipoPrimitivo(token_actual);
            match("pr_void");
        }
        else {
            throw new ExcepcionSintactica(token_actual,"idClase,pr_char,pr_boolean,pr_String,pr_int,pr_void");
        }

        return tipo_metodo;
    }

    private void constructor() throws ExcepcionSintactica, ExcepcionSemantica {
        EntradaConstructor cons = new EntradaConstructor(token_actual);
        TS.setUnidadActual(cons);
        match("idClase");
        argsFormales();
        bloque();
        TS.getClaseActual().setConstructor(cons.getNombre(),cons);
    }

    private void argsFormales() throws ExcepcionSintactica, ExcepcionSemantica {
        match("parAbre");
        listaArgsFormales_Vacio();
        match("parCierra");
    }

    private void listaArgsFormales_Vacio() throws ExcepcionSintactica, ExcepcionSemantica {
        //Primeros listaArgsFormales
        if (Arrays.asList("pr_char","pr_int","pr_String","pr_boolean","idClase").contains(token_actual.get_id_token())){
            listaArgsFormales();
        }
        else {
            //nada por que listaArgsFormales_Vacio -> e
        }
    }

    private void listaArgsFormales() throws ExcepcionSintactica, ExcepcionSemantica {
        argFormal();
        otroArgFormal_Vacio();
    }

    private void argFormal() throws ExcepcionSintactica, ExcepcionSemantica {
        Tipo tipo_arg = tipo();
        EntradaParametro arg = new EntradaParametro(token_actual,tipo_arg);
        match("idMetVar");
        TS.getUnidadActual().setArgumento(arg.getNombre(),arg);
    }

    private void otroArgFormal_Vacio() throws ExcepcionSintactica, ExcepcionSemantica {
        if(token_actual.get_id_token().equals("pun,")) {
            match("pun,");
            listaArgsFormales();
        }
        else {
            //nada por que -> e
        }

    }

    private void bloque() throws ExcepcionSintactica {
        match("llaveAbre");
        listaSentencias();
        match("llaveCierra");
    }

    private void listaSentencias() throws ExcepcionSintactica {

        //Primeros sentencia
        if (Arrays.asList("idClase","pr_boolean","pr_char","pr_int","pr_String","pr_this","idMetVar","pr_new","parAbre","pun;","pr_if","pr_for","pr_return","llaveAbre").contains(token_actual.get_id_token())){
            sentencia();
            listaSentencias();
        }
        else {
            //nada por que listaSentencias -> e
        }
    }

    private void sentencia() throws ExcepcionSintactica {

        if (token_actual.get_id_token().equals("pun;"))
            match("pun;");
        else
        if (token_actual.get_id_token().equals("idClase")){
            Tipo tipo = new TipoReferencia(token_actual);
            match("idClase");
            genericidad_Vacio();    //cambiar
            accesoEstatico_VarLocal(tipo);
            match("pun;");
        }
        else
            //Primeros Asignacion_Llamada = Primeros Acceso
            if (Arrays.asList("parAbre" , "idMetVar", "pr_this", "pr_new").contains(token_actual.get_id_token())) {
                acceso_sin_Estatico();
                asignacion_llamada();
                match("pun;");
            } else
                //Primeros varLocal
                if (Arrays.asList("pr_boolean", "pr_int", "pr_char", "pr_String").contains(token_actual.get_id_token())) {
                    varLocal_sin_TipoClase();
                    match("pun;");
                } else
                    //Primeros return
                    if (token_actual.get_id_token().equals("pr_return")) {
                        nt_return();
                        match("pun;");
                    } else
                        //Primeros if
                        if (token_actual.get_id_token().equals("pr_if"))
                            nt_if();
                        else
                            //Primeros for
                            if (token_actual.get_id_token().equals("pr_for"))
                                nt_for();
                            else
                                //Primeros bloque
                                if (token_actual.get_id_token().equals("llaveAbre"))
                                    bloque();
                                else {
                                    throw new ExcepcionSintactica(token_actual,"; , { , pr_for , pr_if , pr_return , pr_boolean , pr_int , pr_char , pr_String , idClase , parAbre , idMetVar , pr_this o pr_new");
                                }
    }

    private void accesoEstatico_VarLocal(Tipo tipo) throws ExcepcionSintactica {
        //Primero accesoEstatico_Continuacion
        if (token_actual.get_id_token().equals("pun."))
            accesoEstatico_Continuacion();
        else
            //Es declaracion de var local
            if (token_actual.get_id_token().equals("idMetVar")) {
                varLocal_Continuacion(tipo);
            }
            else
                throw new ExcepcionSintactica(token_actual,". o idMetVar");
    }


    private void accesoEstatico_Continuacion() throws ExcepcionSintactica {
        match("pun.");
        match("idMetVar");
        metodo_var();
        estatico_encadenado_asignacion();
    }

    private void estatico_encadenado_asignacion() throws ExcepcionSintactica {

        if(token_actual.get_id_token().equals("asig=")){
            match("asig=");
            expresion();
        }
        else
            //Primeros <accesoEstatico_continuacion>
            if(token_actual.get_id_token().equals("pun."))
                accesoEstatico_Continuacion();
            else {
                //nada por que -> e
            }
    }

    private void varLocal_Continuacion(Tipo tipo) throws ExcepcionSintactica {
        if (token_actual.get_id_token().equals("idMetVar")) {
            //EntradaVariable var = new EntradaVariable(token_actual,tipo); NO DE ESTA ETAPA
            match("idMetVar");
            varLocal_Expresion();
            //TS.getUnidadActual().setVariable(var.getNombre(),var);
        }
        else throw new ExcepcionSintactica(token_actual,"idMetVar");
    }

    private void acceso_sin_Estatico() throws ExcepcionSintactica {
        if(token_actual.get_id_token().equals("parAbre")) {
            match("parAbre");
            casting_ExpresionParentizada();
        }
        else {
            primario_sin_expParentizada_sinEstatico();
            encadenado();
        }
    }

    private void primario_sin_expParentizada_sinEstatico() throws ExcepcionSintactica {

        if (token_actual.get_id_token().equals("idMetVar")){
            match("idMetVar");
            accesoIdMetVar();
        }
        else
            //Primero AccesoThis
            if (token_actual.get_id_token().equals("pr_this"))
                accesoThis();
            else
                //Primero accesoConstructor
                if (token_actual.get_id_token().equals("pr_new"))
                    accesoConstructor();
                else {
                    throw new ExcepcionSintactica(token_actual,"id metodo/variable , this , new , ( o Id de clase");
                }
    }

    private void varLocal_sin_TipoClase() throws ExcepcionSintactica {
        Tipo tipo = tipoPrimitivo();
        //EntradaVariable var = new EntradaVariable(token_actual,tipo); NO DE ESTA ETAPA
        match("idMetVar");
        varLocal_Expresion();
        //TS.getMetodoActual().setVariable(var.getNombre(),var);
    }

    private void acceso() throws ExcepcionSintactica {
        if(token_actual.get_id_token().equals("parAbre")) {
            match("parAbre");
            casting_ExpresionParentizada();
        }
        else {
            primario_sin_expParentizada();
            encadenado();
        }
    }

    private void casting_ExpresionParentizada() throws ExcepcionSintactica {

        if (token_actual.get_id_token().equals("idClase")) {
            match("idClase");
            match("parCierra");
            primario();
            encadenado();
        }
        else {
            expresion();
            match("parCierra");
            encadenado();
        }
    }

    private void primario() throws ExcepcionSintactica {

        if (token_actual.get_id_token().equals("idMetVar")){
            match("idMetVar");
            accesoIdMetVar();
        }
        else
            //Primero AccesoThis
            if (token_actual.get_id_token().equals("pr_this"))
                accesoThis();
            else
                //Primero accesoConstructor
                if (token_actual.get_id_token().equals("pr_new"))
                    accesoConstructor();
                else
                    //Primeros expresionParentizada
                    if (token_actual.get_id_token().equals("parAbre"))
                        expresionParentizada();
                    else
                        //Primero accesoEstatico
                        if (token_actual.get_id_token().equals("idClase"))
                            accesoEstatico();
                        else{
                            throw new ExcepcionSintactica(token_actual,"id metodo/variable , this , new , ( o Id de clase");
                        }
    }

    private void primario_sin_expParentizada() throws ExcepcionSintactica {
        if (token_actual.get_id_token().equals("idMetVar")){
            match("idMetVar");
            accesoIdMetVar();
        }
        else
            //Primero AccesoThis
            if (token_actual.get_id_token().equals("pr_this"))
                accesoThis();
            else
                //Primero accesoConstructor
                if (token_actual.get_id_token().equals("pr_new"))
                    accesoConstructor();
                else
                    //Primero accesoEstatico
                    if (token_actual.get_id_token().equals("idClase"))
                        accesoEstatico();
                    else{
                        throw new ExcepcionSintactica(token_actual,"id metodo/variable , this , new , ( o Id de clase");
                    }
    }

    private void accesoEstatico() throws ExcepcionSintactica {
        match("idClase");
        match("pun.");
        match("idMetVar");
        metodo_var();
        estatico_encadenado_asignacion();
    }

    private void accesoIdMetVar() throws ExcepcionSintactica {
        //Primeros argsActuales
        if (token_actual.get_id_token().equals("parAbre"))
            argsActuales();
        else {
            //nada por que accesoIdMetVar -> e
        }
    }

    private void accesoThis() throws ExcepcionSintactica {
        match("pr_this");
    }

    private void accesoConstructor() throws ExcepcionSintactica {
        match("pr_new");
        match("idClase");
        //genericidad_Vacio();
        argsActuales();
    }

    private void argsActuales() throws ExcepcionSintactica {
        match("parAbre");
        listaExps_Vacio();
        match("parCierra");
    }

    private void listaExps_Vacio() throws ExcepcionSintactica {
        //Primeros listaExps = Primeros Expresion
        if (Arrays.asList("op+","op-","op!","pr_null","pr_true","pr_false","Entero","Char","String","idMetVar","pr_this","pr_new","parAbre").contains(token_actual.get_id_token()))
            lista_Exps();
        else {
            //nada por que listaExps_Vacio -> e
        }
    }

    private void lista_Exps() throws ExcepcionSintactica {
        expresion();
        otraExpresion_Vacio();
    }

    private void otraExpresion_Vacio() throws ExcepcionSintactica {
        if(token_actual.get_id_token().equals("pun,")) {
            match("pun,");
            lista_Exps();
        }
        else {
            //nada por que otraExpresion_Vacio -> e
        }
    }

    private void expresion() throws ExcepcionSintactica {
        expresionUnaria();
        expresionRecDerecha_Vacio();
    }

    private void expresionRecDerecha_Vacio() throws ExcepcionSintactica {
        //Primeros operadorBinario
        if (Arrays.asList("op||","op&&","op==","op!=","op<","op>","op<=","op>=","op+","op-","op*","op/","op%").contains(token_actual.get_id_token())){
            operadorBinario();
            expresionUnaria();
            expresionRecDerecha_Vacio();
        }
        else {
            //nada por que -> e
        }
    }

    private void operadorBinario() throws ExcepcionSintactica {
        switch (token_actual.get_id_token()) {
            case "op||": match("op||");
                break;
            case "op&&": match("op&&");
                break;
            case "op==": match("op==");
                break;
            case "op!=": match("op!=");
                break;
            case "op<": match("op<");
                break;
            case "op<=": match("op<=");
                break;
            case "op>": match("op>");
                break;
            case "op>=": match("op>=");
                break;
            case "op+": match("op+");
                break;
            case "op-": match("op-");
                break;
            case "op*": match("op*");
                break;
            case "op/": match("op/");
                break;
            case "op%": match("op%");
                break;
        }
    }

    private void expresionUnaria() throws ExcepcionSintactica {
        //Primeros operador unario
        if (Arrays.asList("op+","op-","op!").contains(token_actual.get_id_token())){
            operadorUnario();
            operando();
        }
        else
            //Primeros operando
            if(Arrays.asList("idClase","pr_null","pr_true","pr_false","Entero","Char","String","parAbre","idMetVar","pr_this","pr_new").contains(token_actual.get_id_token())){
                operando();
            }
            else {
                throw new ExcepcionSintactica(token_actual,"op+,op-,op!,pr_null,pr_true,pr_false,Entero,Char,String,parAbre,idMetVar,pr_this,pr_new");
            }
    }

    private void operadorUnario() throws ExcepcionSintactica {
        if (token_actual.get_id_token().equals("op+"))
            match("op+");
        else
        if (token_actual.get_id_token().equals("op-"))
            match("op-");
        else
        if (token_actual.get_id_token().equals("op!"))
            match("op!");
        else {
            //throw error? nunca entraria aca
        }
    }

    private void operando() throws ExcepcionSintactica {
        //Primeros literal
        if (Arrays.asList("pr_null","pr_true","pr_false","Entero","Char","String").contains(token_actual.get_id_token()))
            literal();
        else
            //Primeros Acceso
            if (Arrays.asList("parAbre","idMetVar","pr_this","pr_new","idClase").contains(token_actual.get_id_token()))
                acceso();
            else {
                throw new ExcepcionSintactica(token_actual,"Entero, Char, String, (, idMetVar, idClase o Palabras reservadas : null, true , false, this, new");
            }
    }

    private void literal() throws ExcepcionSintactica {
        if (token_actual.get_id_token().equals("pr_null"))
            match("pr_null");
        else
        if (token_actual.get_id_token().equals("pr_true"))
            match("pr_true");
        else
        if (token_actual.get_id_token().equals("pr_false"))
            match("pr_false");
        else
        if (token_actual.get_id_token().equals("Entero")) {
            match("Entero");
        }
        else
        if (token_actual.get_id_token().equals("Char"))
            match("Char");
        else
        if (token_actual.get_id_token().equals("String"))
            match("String");
        else {
            // throw?
        }

    }

    private void expresionParentizada() throws ExcepcionSintactica {
        match("parAbre");
        expresion();
        match("parCierra");
    }

    private void varLocal() throws ExcepcionSintactica {
        Tipo tipoVariable = tipo();
        //EntradaVariable var = new EntradaVariable(token_actual,tipoVariable); NO DE ESTA ETAPA
        match("idMetVar");
        varLocal_Expresion();
        //TS.getMetodoActual().setVariable(var.getNombre(),var);
    }

    private void varLocal_Expresion() throws ExcepcionSintactica {
        if (token_actual.get_id_token().equals("asig=")) {
            match("asig=");
            expresion();
        }
        else {
            //nada por que varLocal_Expresion -> e
        }
    }

    private void encadenado() throws ExcepcionSintactica {
        //Primeros varOMetodoEncadenado
        if (token_actual.get_id_token().equals("pun.")) {
            varOMetodoEncadenado();
            encadenado();
        }
        else {
            //nada por que -> e
        }
    }

    private void varOMetodoEncadenado() throws ExcepcionSintactica {
        match("pun.");
        match("idMetVar");
        metodo_var();
    }

    private void metodo_var() throws ExcepcionSintactica {
        //Primeros argActuales
        if (token_actual.get_id_token().equals("parAbre"))
            argsActuales();
        else{
            //nada por que -> e
        }
    }

    private void asignacion_llamada() throws ExcepcionSintactica {
        //Primeros tipoDeAsignacion
        if (Arrays.asList("asig=","asig++","asig--").contains(token_actual.get_id_token()))
            tipoDeAsignacion();
        else {
            //nada por que -> e
        }
    }

    private void nt_return() throws ExcepcionSintactica {
        match("pr_return");
        expresion_Vacio();
    }

    private void expresion_Vacio() throws ExcepcionSintactica {
        //Primeros expresion
        if (Arrays.asList("op+","op-","op!","pr_null","pr_true","pr_false","Entero","Char","String","parAbre","idMetVar","pr_this","pr_new").contains(token_actual.get_id_token()))
            expresion();
        else {
            //nada por que -> e
        }
    }

    private void nt_if() throws ExcepcionSintactica {
        match("pr_if");
        match("parAbre");
        expresion();
        match("parCierra");
        sentencia();
        nt_if_else();
    }

    private void nt_if_else() throws ExcepcionSintactica {
        if (token_actual.get_id_token().equals("pr_else")){
            match("pr_else");
            sentencia();
        }
        else {
            //nada -> e
        }
    }

    private void nt_for() throws ExcepcionSintactica {
        match("pr_for");
        match("parAbre");
        varLocal();
        match("pun;");
        expresion();
        match("pun;");
        asignacion();
        match("parCierra");
        sentencia();
    }

    private void asignacion() throws ExcepcionSintactica {
        acceso();
        tipoDeAsignacion();
    }

    private void tipoDeAsignacion() throws ExcepcionSintactica {
        if (token_actual.get_id_token().equals("asig=")) {
            match("asig=");
            expresion();
        }
        else
        if(token_actual.get_id_token().equals("asig++"))
            match("asig++");
        else
        if(token_actual.get_id_token().equals("asig--"))
            match("asig--");
        else {
            throw new ExcepcionSintactica(token_actual,"++, -- o =");
        }
    }


    private void match(String id_token) throws ExcepcionSintactica {
        if (id_token == "$")
            id_token = "EOF";
        if (id_token.equals(token_actual.get_id_token())){
            this.token_actual = analizador_lexico.proximo_token();
        }
        else {
            throw new ExcepcionSintactica(token_actual,id_token);
        }

    }

    public boolean hubo_errores(){
        return huboErrores;
    }

}
