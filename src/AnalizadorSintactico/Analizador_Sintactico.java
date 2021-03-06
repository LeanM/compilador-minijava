package AnalizadorSintactico;

import AST.Acceso.*;
import AST.Expresion.*;
import AST.NodoBloque;
import AST.Sentencia.*;
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
        NodoBloque bloque_metodo;
        String forma = formaMetodo();
        Tipo tipo_metodo = tipoMetodo();
        EntradaMetodo metodo = new EntradaMetodo(token_actual,forma,tipo_metodo);
        TS.setUnidadActual(metodo);
        match("idMetVar");
        argsFormales();

        bloque_metodo = new NodoBloque(TS.getUnidadActual());
        TS.getUnidadActual().set_bloque_principal(bloque_metodo); //Preguntar
        TS.getUnidadActual().set_bloque_actual(bloque_metodo);
        bloque(bloque_metodo);

        //Si el metodo es static (dudoso)
        if(forma.equals("static"))
            ((EntradaMetodo)TS.getUnidadActual()).set_sentencias_static();

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
        NodoBloque bloque_constructor;
        EntradaConstructor cons = new EntradaConstructor(token_actual,new TipoReferencia(new Token("idClase",TS.getClaseActual().getNombre(),token_actual.get_nro_linea())));
        TS.setUnidadActual(cons);
        match("idClase");
        argsFormales();

        bloque_constructor = new NodoBloque(TS.getUnidadActual());
        TS.getUnidadActual().set_bloque_principal(bloque_constructor); //Preguntar
        TS.getUnidadActual().set_bloque_actual(bloque_constructor);
        bloque(bloque_constructor);

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

    private NodoBloque bloque(NodoBloque bloque_a_completar) throws ExcepcionSintactica, ExcepcionSemantica {
        NodoBloque bloque_backup;
        match("llaveAbre");

        if(bloque_a_completar != TS.getUnidadActual().get_bloque_principal())
            //El bloque no es el bloque principal (no es caso base) debo asignarle como hijo del bloque actual, el bloque a completar
            TS.getUnidadActual().get_bloque_actual().set_bloque_hijo(bloque_a_completar);

        //Seteo bloque padre a bloque a completar (si es el bloque principal del metodo o constructor se le asigna a si mismo como nodo padre)
        bloque_a_completar.set_bloque_padre(TS.getUnidadActual().get_bloque_actual());

        bloque_backup = TS.getUnidadActual().get_bloque_actual();
        TS.getUnidadActual().set_bloque_actual(bloque_a_completar);
        listaSentencias(bloque_a_completar);
        TS.getUnidadActual().set_bloque_actual(bloque_backup);

        match("llaveCierra");

        return bloque_a_completar;
    }

    private void listaSentencias(NodoBloque bloque_a_completar) throws ExcepcionSintactica, ExcepcionSemantica {
        //Primeros sentencia
        if (Arrays.asList("idClase","pr_boolean","pr_char","pr_int","pr_String","pr_this","idMetVar","pr_new","parAbre","pun;","pr_if","pr_for","pr_return","llaveAbre").contains(token_actual.get_id_token())){
            bloque_a_completar.setSentencia(sentencia());
            listaSentencias(bloque_a_completar);
        }
        else {
            //nada por que listaSentencias -> e
        }
    }

    private NodoSentencia sentencia() throws ExcepcionSintactica, ExcepcionSemantica {
        NodoSentencia toReturn = new NodoSentenciaVacia();

        if (token_actual.get_id_token().equals("pun;"))
            match("pun;");
        else
        if (token_actual.get_id_token().equals("idClase")){
            Tipo tipo = new TipoReferencia(token_actual);
            match("idClase");
            //genericidad_Vacio(); no va mas
            toReturn = accesoEstatico_VarLocal(tipo);
            match("pun;");
        }
        else
            //Primeros Asignacion_Llamada = Primeros Acceso
            if (Arrays.asList("parAbre" , "idMetVar", "pr_this", "pr_new").contains(token_actual.get_id_token())) {
                NodoAcceso nodoAcceso = acceso_sin_Estatico();
                toReturn = asignacion_llamada(nodoAcceso);
                match("pun;");
            } else
                //Primeros varLocal
                if (Arrays.asList("pr_boolean", "pr_int", "pr_char", "pr_String").contains(token_actual.get_id_token())) {
                    toReturn = varLocal_sin_TipoClase();
                    match("pun;");
                } else
                    //Primeros return
                    if (token_actual.get_id_token().equals("pr_return")) {
                        toReturn = nt_return();
                        match("pun;");
                    } else
                        //Primeros if
                        if (token_actual.get_id_token().equals("pr_if"))
                            toReturn = nt_if();
                        else
                            //Primeros for
                            if (token_actual.get_id_token().equals("pr_for"))
                                toReturn = nt_for();
                            else
                                //Primeros bloque
                                if (token_actual.get_id_token().equals("llaveAbre"))
                                    toReturn = bloque(new NodoBloque(TS.getUnidadActual()));
                                else {
                                    throw new ExcepcionSintactica(token_actual,"; , { , pr_for , pr_if , pr_return , pr_boolean , pr_int , pr_char , pr_String , idClase , parAbre , idMetVar , pr_this o pr_new");
                                }
        return toReturn;
    }

    private NodoSentencia accesoEstatico_VarLocal(Tipo tipo) throws ExcepcionSintactica, ExcepcionSemantica {
        NodoSentencia toReturn;
        NodoPrimario_Component primario_component;
        NodoPrimario_Component primario_encadenado;
        //Primero accesoEstatico_Continuacion
        if (token_actual.get_id_token().equals("pun.")) {
            NodoAccesoEstatico nodoAccesoEstatico = new NodoAccesoEstatico(tipo,TS.getClaseActual().getNombre());

            match("pun.");
            Token idMetVar = token_actual;
            match("idMetVar");
            primario_encadenado = metodo_var(idMetVar,nodoAccesoEstatico);
            nodoAccesoEstatico.set_encadenado_estatico(primario_encadenado);

            primario_component = accesoEstatico_Continuacion(primario_encadenado);
            toReturn = asignacion_llamada(primario_component);
        }
        else
            //Es declaracion de var local
            if (token_actual.get_id_token().equals("idMetVar")) {
                toReturn = varLocal_Continuacion(tipo);
            }
            else
                throw new ExcepcionSintactica(token_actual,". o idMetVar");

        return toReturn;
    }


    private NodoPrimario_Component accesoEstatico_Continuacion(NodoPrimario_Component nodo_acceso_estatico) throws ExcepcionSintactica {
        NodoPrimario_Component toReturn = nodo_acceso_estatico;
        NodoPrimario_Component primario_encadenado;
        if(token_actual.get_id_token().equals("pun.")) {
            match("pun.");
            Token idMetVar = token_actual;
            match("idMetVar");
            primario_encadenado = metodo_var(idMetVar,nodo_acceso_estatico);
            toReturn = accesoEstatico_Continuacion(primario_encadenado);
        }
        else {
            //nada
        }

        return toReturn;
    }

    private NodoVarLocal varLocal_Continuacion(Tipo tipo) throws ExcepcionSintactica, ExcepcionSemantica {
        NodoVarLocal toReturn;
        if (token_actual.get_id_token().equals("idMetVar")) {
            Token nombre_Var = token_actual;
            match("idMetVar");
            toReturn = varLocal_Expresion(tipo,nombre_Var);
        }
        else throw new ExcepcionSintactica(token_actual,"idMetVar");

        return toReturn;
    }

    private NodoAcceso acceso_sin_Estatico() throws ExcepcionSintactica {
        NodoAcceso toReturn;
        if(token_actual.get_id_token().equals("parAbre")) {
            match("parAbre");
            toReturn = casting_ExpresionParentizada();
        }
        else {
            NodoPrimario_Component nodoPrimario = primario_sin_expParentizada_sinEstatico();
            toReturn = encadenado(nodoPrimario);
        }
        return toReturn;
    }

    private NodoPrimario_Component primario_sin_expParentizada_sinEstatico() throws ExcepcionSintactica {
        NodoPrimario_Component toReturn;
        if (token_actual.get_id_token().equals("idMetVar")){
            Token nombre_met_var = token_actual;
            match("idMetVar");
            toReturn = accesoIdMetVar(nombre_met_var);
        }
        else
            //Primero AccesoThis
            if (token_actual.get_id_token().equals("pr_this"))
                toReturn = accesoThis();
            else
                //Primero accesoConstructor
                if (token_actual.get_id_token().equals("pr_new"))
                    toReturn = accesoConstructor();
                else {
                    throw new ExcepcionSintactica(token_actual,"id metodo/variable , this , new , ( o Id de clase");
                }
        return toReturn;
    }

    private NodoVarLocal varLocal_sin_TipoClase() throws ExcepcionSintactica, ExcepcionSemantica {
        Tipo tipo = tipoPrimitivo();
        Token nombre_var = token_actual;
        match("idMetVar");
        return varLocal_Expresion(tipo,nombre_var);
    }

    private NodoAcceso acceso() throws ExcepcionSintactica {
        NodoAcceso toReturn;
        if(token_actual.get_id_token().equals("parAbre")) {
            match("parAbre");
            toReturn = casting_ExpresionParentizada();
        }
        else {
            NodoPrimario_Component nodoPrimario = primario_sin_expParentizada();
            toReturn = encadenado(nodoPrimario);
        }

        return toReturn;
    }

    private NodoAcceso casting_ExpresionParentizada() throws ExcepcionSintactica {
        NodoAcceso toReturn;
        NodoPrimario_Component primario_encadenado;

        if (token_actual.get_id_token().equals("idClase")) {
            toReturn = casting_estatico();
            /*
            //Casting
            Token clase = token_actual;
            match("idClase");
            match("parCierra");
            NodoPrimario_Component nodoPrimario = primario();
            primario_encadenado = encadenado(nodoPrimario);
            toReturn = new NodoCasting(primario_encadenado, clase);

             */

        }
        else {
            NodoExpParentizada nodoExpParentizada = new NodoExpParentizada(expresion(),TS.getClaseActual().getNombre());
            match("parCierra");
            toReturn = encadenado(nodoExpParentizada);
        }
        return toReturn;
    }

    private NodoAcceso casting_estatico() throws ExcepcionSintactica {
        NodoAcceso toReturn;
        NodoPrimario_Component primario_encadenado;
        Token clase = token_actual;
        match("idClase");
        if(token_actual.get_id_token().equals("pun.")) {
            NodoAccesoEstatico nodoAccesoEstatico = new NodoAccesoEstatico(new TipoReferencia(clase),TS.getClaseActual().getNombre());
            match("pun.");
            Token idMetVar = token_actual;
            match("idMetVar");
            primario_encadenado = metodo_var(idMetVar,nodoAccesoEstatico);
            nodoAccesoEstatico.set_encadenado_estatico(primario_encadenado);
            toReturn = accesoEstatico_Continuacion(primario_encadenado);
            match("parCierra");
        }
        else {
            match("parCierra");
            NodoPrimario_Component nodoPrimario = primario();
            primario_encadenado = encadenado(nodoPrimario);
            toReturn = new NodoCasting(primario_encadenado, clase);
        }

        return toReturn;
    }

    private NodoPrimario_Component primario() throws ExcepcionSintactica {
        NodoPrimario_Component toReturn;
        if (token_actual.get_id_token().equals("idMetVar")){
            Token nombre_met_var = token_actual;
            match("idMetVar");
            toReturn = accesoIdMetVar(nombre_met_var);
        }
        else
            //Primero AccesoThis
            if (token_actual.get_id_token().equals("pr_this")) {
                toReturn = accesoThis();
            }
            else
                //Primero accesoConstructor
                if (token_actual.get_id_token().equals("pr_new")) {
                    toReturn = accesoConstructor();
                }
                else
                    //Primeros expresionParentizada
                    if (token_actual.get_id_token().equals("parAbre"))
                        toReturn = expresionParentizada();
                    else
                        //Primero accesoEstatico
                        if (token_actual.get_id_token().equals("idClase"))
                            toReturn = accesoEstatico();
                        else{
                            throw new ExcepcionSintactica(token_actual,"id metodo/variable , this , new , ( o Id de clase");
                        }
        return toReturn;
    }

    private NodoPrimario_Component primario_sin_expParentizada() throws ExcepcionSintactica {
        NodoPrimario_Component toReturn;
        if (token_actual.get_id_token().equals("idMetVar")){
            Token nombre_met_var = token_actual;
            match("idMetVar");
            toReturn = accesoIdMetVar(nombre_met_var);
        }
        else
            //Primero AccesoThis
            if (token_actual.get_id_token().equals("pr_this"))
                toReturn = accesoThis();
            else
                //Primero accesoConstructor
                if (token_actual.get_id_token().equals("pr_new"))
                    toReturn = accesoConstructor();
                else
                    //Primero accesoEstatico
                    if (token_actual.get_id_token().equals("idClase"))
                        toReturn = accesoEstatico();
                    else{
                        throw new ExcepcionSintactica(token_actual,"id metodo/variable , this , new , ( o Id de clase");
                    }
        return toReturn;
    }

    private NodoPrimario_Component accesoEstatico() throws ExcepcionSintactica {
        NodoPrimario_Component toReturn;
        NodoAccesoEstatico accesoEstatico = new NodoAccesoEstatico(new TipoReferencia(token_actual),TS.getClaseActual().getNombre());
        NodoPrimario_Component primario_encadenado;
        match("idClase");
        match("pun.");
        Token nombre_met_var = token_actual;
        match("idMetVar");
        primario_encadenado = metodo_var(nombre_met_var,accesoEstatico);
        accesoEstatico.set_encadenado_estatico(primario_encadenado);

        toReturn = accesoEstatico_Continuacion(primario_encadenado);

        return toReturn;
    }

    private NodoPrimario_Component accesoIdMetVar(Token nombre_met_var) throws ExcepcionSintactica {
        NodoPrimario_Component toReturn;
        //Primeros argsActuales
        if (token_actual.get_id_token().equals("parAbre"))
            toReturn = new NodoAccesoMetodo(nombre_met_var,argsActuales(),TS.getClaseActual().getNombre());
        else {
            toReturn = new NodoAccesoVar(nombre_met_var,TS.getClaseActual().getNombre(),TS.getUnidadActual(),TS.getUnidadActual().get_bloque_actual());
        }
        return toReturn;
    }

    private NodoAccesoThis accesoThis() throws ExcepcionSintactica {
        NodoAccesoThis toReturn = new NodoAccesoThis(token_actual,new TipoReferencia(TS.getClaseActual().get_token_clase()));
        match("pr_this");

        return toReturn;
    }

    private NodoAccesoConstructor accesoConstructor() throws ExcepcionSintactica {
        NodoAccesoConstructor toReturn;
        match("pr_new");
        Token token_constructor = token_actual;
        match("idClase");
        toReturn = new NodoAccesoConstructor(token_constructor,argsActuales(),TS.getClaseActual().getNombre());
        //genericidad_Vacio();

        return toReturn;
    }

    private LinkedList<NodoExpresion> argsActuales() throws ExcepcionSintactica {
        LinkedList<NodoExpresion> toReturn = new LinkedList<NodoExpresion>();
        match("parAbre");
        listaExps_Vacio(toReturn);
        match("parCierra");

        return toReturn;
    }

    private void listaExps_Vacio(LinkedList<NodoExpresion> args) throws ExcepcionSintactica {
        //Primeros listaExps = Primeros Expresion
        if (Arrays.asList("op+","op-","op!","pr_null","pr_true","pr_false","Entero","Char","String","idMetVar","pr_this","pr_new","parAbre").contains(token_actual.get_id_token())) {
            lista_Exps(args);
        }
        else {
            //nada por que listaExps_Vacio -> e
        }
    }

    private void lista_Exps(LinkedList<NodoExpresion> args) throws ExcepcionSintactica {
        args.add(expresion());
        otraExpresion_Vacio(args);
    }

    private void otraExpresion_Vacio(LinkedList<NodoExpresion> args) throws ExcepcionSintactica {
        if(token_actual.get_id_token().equals("pun,")) {
            match("pun,");
            lista_Exps(args);
        }
        else {
            //nada por que otraExpresion_Vacio -> e
        }
    }

    private NodoExpresion expresion() throws ExcepcionSintactica {
        NodoExpresion toReturn;
        toReturn = expresionUnaria();
        toReturn = expresionRecDerecha_Vacio(toReturn);

        return toReturn;
    }

    private NodoExpresion expresionRecDerecha_Vacio(NodoExpresion nodo) throws ExcepcionSintactica {
        NodoExpresion toReturn = nodo;
        //Primeros operadorBinario
        if (Arrays.asList("op||","op&&","op==","op!=","op<","op>","op<=","op>=","op+","op-","op*","op/","op%").contains(token_actual.get_id_token())){
            Token operador_binario = token_actual;
            operadorBinario();
            toReturn = new NodoExpresionBinaria(nodo,operador_binario,expresionUnaria());
            toReturn = expresionRecDerecha_Vacio(toReturn);
        }
        else {
            //nada por que -> e
        }
        return toReturn;
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

    private NodoExpresion expresionUnaria() throws ExcepcionSintactica {
        NodoExpresion toReturn;
        //Primeros operador unario
        if (Arrays.asList("op+","op-","op!").contains(token_actual.get_id_token())){
            Token operador = operadorUnario();
            NodoOperando operando = operando();
            toReturn = new NodoExpresionUnaria(operador,operando);
        }
        else
            //Primeros operando
            if(Arrays.asList("idClase","pr_null","pr_true","pr_false","Entero","Char","String","parAbre","idMetVar","pr_this","pr_new").contains(token_actual.get_id_token())){
                toReturn = operando();
            }
            else {
                throw new ExcepcionSintactica(token_actual,"op+,op-,op!,pr_null,pr_true,pr_false,Entero,Char,String,parAbre,idMetVar,pr_this,pr_new");
            }
        return toReturn;
    }

    private Token operadorUnario() throws ExcepcionSintactica {
        Token toReturn = token_actual;
        if (token_actual.get_id_token().equals("op+"))
            match("op+");
        else
        if (token_actual.get_id_token().equals("op-"))
            match("op-");
        else match("op!");

        return toReturn;
    }

    private NodoOperando operando() throws ExcepcionSintactica {
        NodoOperando toReturn;
        //Primeros literal
        if (Arrays.asList("pr_null","pr_true","pr_false","Entero","Char","String").contains(token_actual.get_id_token()))
            toReturn = literal();
        else
            //Primeros Acceso
            if (Arrays.asList("parAbre","idMetVar","pr_this","pr_new","idClase").contains(token_actual.get_id_token()))
                toReturn = new NodoOperando_Acceso(acceso());
            else {
                throw new ExcepcionSintactica(token_actual,"Entero, Char, String, (, idMetVar, idClase o Palabras reservadas : null, true , false, this, new");
            }
        return toReturn;
    }

    private NodoOperando_Literal literal() throws ExcepcionSintactica {
        NodoOperando_Literal nodo = new NodoNull(token_actual);
        if (token_actual.get_id_token().equals("pr_null"))
            match("pr_null");
        else
        if (token_actual.get_id_token().equals("pr_true")) {
            nodo = new NodoBoolean(token_actual);
            match("pr_true");
        }
        else
        if (token_actual.get_id_token().equals("pr_false")) {
            nodo = new NodoBoolean(token_actual);
            match("pr_false");
        }
        else
        if (token_actual.get_id_token().equals("Entero")) {
            nodo = new NodoEntero(token_actual);
            match("Entero");
        }
        else
        if (token_actual.get_id_token().equals("Char")) {
            nodo = new NodoChar(token_actual);
            match("Char");
        }
        else
        if (token_actual.get_id_token().equals("String")) {
            nodo = new NodoString(token_actual);
            match("String");
        }

        return nodo;
    }

    private NodoExpParentizada expresionParentizada() throws ExcepcionSintactica {
        NodoExpParentizada toReturn;
        match("parAbre");
        NodoExpresion expresion = expresion();
        toReturn = new NodoExpParentizada(expresion,TS.getClaseActual().getNombre());
        match("parCierra");

        return toReturn;
    }

    private NodoVarLocal varLocal() throws ExcepcionSintactica, ExcepcionSemantica {
        NodoVarLocal toReturn;
        Tipo tipoVariable = tipo();
        Token nombre_var = token_actual;
        match("idMetVar");
        toReturn = varLocal_Expresion(tipoVariable,nombre_var);

        return toReturn;
    }

    private NodoVarLocal varLocal_Expresion(Tipo tipo, Token nombre) throws ExcepcionSintactica, ExcepcionSemantica {
        NodoVarLocal toReturn;
        if (token_actual.get_id_token().equals("asig=")) {
            match("asig=");
            toReturn = new NodoVarLocal_Asignacion(tipo,nombre,TS.getUnidadActual(),TS.getUnidadActual().get_bloque_actual(), expresion());
        }
        else {
            //nada por que varLocal_Expresion -> e
            toReturn = new NodoVarLocal(tipo,nombre,TS.getUnidadActual(),TS.getUnidadActual().get_bloque_actual());
        }
        TS.getUnidadActual().get_bloque_actual().set_var_local(nombre.get_lexema(),toReturn);

        return toReturn;
    }

    private NodoPrimario_Component encadenado(NodoPrimario_Component primario) throws ExcepcionSintactica {
        NodoPrimario_Component toReturn = primario;
        NodoPrimario_Component primario_encadenado;
        //Primeros varOMetodoEncadenado
        if (token_actual.get_id_token().equals("pun.")) {
            primario_encadenado = varOMetodoEncadenado(primario);
            toReturn = encadenado(primario_encadenado);
        }
        else {
            //nada por que -> e
        }

        return toReturn;
    }

    private NodoPrimario_Component varOMetodoEncadenado(NodoPrimario_Component primario) throws ExcepcionSintactica {
        match("pun.");
        Token nombre = token_actual;
        match("idMetVar");
        return metodo_var(nombre,primario);
    }

    private NodoPrimario_Component metodo_var(Token nombre_met_var, NodoPrimario_Component primario) throws ExcepcionSintactica {
        NodoEncadenado_Decorator toReturn;
        //Primeros argActuales
        if (token_actual.get_id_token().equals("parAbre")) {
            toReturn = new NodoMetodoEncadenado_Decorator(nombre_met_var,argsActuales(),primario);
        }
        else{
            //es var encadenada
            toReturn = new NodoVarEncadenada_Decorator(nombre_met_var,primario);
        }
        return toReturn;
    }

    private NodoSentencia asignacion_llamada(NodoAcceso nodoAcceso) throws ExcepcionSintactica {
        NodoSentencia toReturn;
        //Primeros tipoDeAsignacion
        if (Arrays.asList("asig=","asig++","asig--").contains(token_actual.get_id_token())) {
            toReturn = tipoDeAsignacion(nodoAcceso);
        }
        else {
            //nada por que -> e
            toReturn = new NodoLlamada(nodoAcceso);
        }
        return toReturn;
    }

    private NodoReturn nt_return() throws ExcepcionSintactica {
        NodoReturn toReturn;
        Token token_return = token_actual;
        match("pr_return");
        NodoExpresion nodoExpresion = expresion_Vacio();
        if (nodoExpresion != null)
            toReturn = new NodoReturn_Expresion(token_return,nodoExpresion, TS.getUnidadActual().get_bloque_actual());
        else toReturn = new NodoReturn(token_return, TS.getUnidadActual().get_bloque_actual());

        return toReturn;
    }

    private NodoExpresion expresion_Vacio() throws ExcepcionSintactica {
        NodoExpresion toReturn = null;
        //Primeros expresion
        if (Arrays.asList("op+","op-","op!","pr_null","pr_true","pr_false","Entero","Char","String","parAbre","idMetVar","pr_this","pr_new").contains(token_actual.get_id_token()))
            toReturn = expresion();
        else {
            //nada por que -> e
        }
        return toReturn;
    }

    private NodoIf nt_if() throws ExcepcionSintactica, ExcepcionSemantica {
        NodoIf toReturn;
        match("pr_if");
        match("parAbre");
        NodoExpresion condicion = expresion();
        match("parCierra");
        NodoSentencia cuerpo_then = sentencia();
        toReturn = nt_if_else(condicion,cuerpo_then);

        return toReturn;
    }

    private NodoIf nt_if_else(NodoExpresion condicion, NodoSentencia cuerpo_then) throws ExcepcionSintactica, ExcepcionSemantica {
        NodoIf toReturn;
        if (token_actual.get_id_token().equals("pr_else")){
            match("pr_else");
            NodoSentencia cuerpo_else = sentencia();
            toReturn = new NodoIf_else(condicion,cuerpo_then,cuerpo_else);
        }
        else {
            //nada -> e
            toReturn = new NodoIf(condicion,cuerpo_then);
        }

        return toReturn;
    }

    private NodoFor nt_for() throws ExcepcionSintactica, ExcepcionSemantica {
        match("pr_for");
        match("parAbre");
        NodoVarLocal var = varLocal();
        match("pun;");
        NodoExpresion condicion = expresion();
        match("pun;");
        NodoAsignacion asignacion = asignacion();
        match("parCierra");
        NodoSentencia cuerpo_for = sentencia();

        return new NodoFor(var,condicion,asignacion,cuerpo_for);
    }

    private NodoAsignacion asignacion() throws ExcepcionSintactica {
        NodoAcceso nodoAcceso = acceso();

        return tipoDeAsignacion(nodoAcceso);
    }

    private NodoAsignacion tipoDeAsignacion(NodoAcceso nodo_acceso) throws ExcepcionSintactica {
        NodoAsignacion toReturn;
        if (token_actual.get_id_token().equals("asig=")) {
            Token tipo_asignacion = token_actual;
            match("asig=");
            toReturn = new NodoAsignacion_Standar(nodo_acceso,tipo_asignacion,expresion());
        }
        else
        if(token_actual.get_id_token().equals("asig++")) {
            Token tipo_asignacion = token_actual;
            match("asig++");
            toReturn = new NodoAsignacion_Inc(nodo_acceso,tipo_asignacion);
        }
        else
        if(token_actual.get_id_token().equals("asig--")) {
            Token tipo_asignacion = token_actual;
            match("asig--");
            toReturn = new NodoAsignacion_Dec(nodo_acceso,tipo_asignacion);
        }
        else {
            throw new ExcepcionSintactica(token_actual,"++, -- o =");
        }

        return toReturn;
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
