package AST.Acceso;

import AST.Expresion.NodoExpresion;
import AST.Expresion.NodoOperando_Acceso;
import AST.Expresion.NodoOperando_Literal;
import AnalizadorLexico.Token;
import AnalizadorSemantico.*;
import Traductor.Traductor;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

public class NodoMetodoEncadenado_Decorator extends NodoEncadenado_Decorator{

    protected LinkedList<NodoExpresion> argumentos;
    protected EntradaMetodo metodo_conformado;
    protected Tipo tipo_encadenado_izq;

    public NodoMetodoEncadenado_Decorator(Token token_metodo_encadenado,LinkedList<NodoExpresion> args, NodoPrimario_Component primario) {
        super(token_metodo_encadenado, primario);
        this.argumentos = args;
        this.metodo_conformado = null;
        this.tipo_encadenado_izq = null;
    }

    @Override
    public void esta_bien_definido() throws ExcepcionTipo, ExcepcionSemantica {
        primario_decorator.esta_bien_definido();
        tipo_encadenado_izq = primario_decorator.get_tipo_acceso();

        if(tipo_encadenado_izq.esPrimitivo())
            throw new ExcepcionTipo(token_acceso,"El tipo de retorno del encadenado a izquierda no puede ser un tipo primitivo ni void");

        metodo_conformado = TablaSimbolos.getInstance().conforma_metodo(token_acceso,argumentos,tipo_encadenado_izq.getNombre());
        if(metodo_conformado == null)
            throw new ExcepcionTipo(token_acceso,"La llamada a metodo "+token_acceso.get_lexema()+" no conforma con ningun metodos de la clase del encadenado de la izquierda ( "+tipo_encadenado_izq.getNombre()+" )");
    }

    @Override
    public Tipo get_tipo_acceso() throws ExcepcionTipo, ExcepcionSemantica {
        Tipo toReturn;
        if(metodo_conformado != null)
            throw new ExcepcionTipo(token_acceso,"La llamada a metodo "+token_acceso.get_lexema()+" no conforma con ningun metodos de la clase del encadenado de la izquierda ( "+tipo_encadenado_izq.getNombre()+" )");
        else
            toReturn = metodo_conformado.get_tipo();

        return toReturn;
    }

    @Override
    public void generar_codigo() throws IOException, ExcepcionTipo, ExcepcionSemantica {
        //Si a la izquierda hay un metodo encadenado o un acceso a metodo, debo tomar el retorno para el this
        //Si a la izquierda hay una var encadenada, debo tomar el offset en la clase de esa var ya que es de instancia
        //Si a la izquierda hay un acceso a var, lo hace la variable
        primario_decorator.generar_codigo();
        //Generar codigo parametros
        if(metodo_conformado != null) {

            if (Arrays.asList("debugPrint", "read", "printI", "printB", "printC", "printS", "println", "printIln", "printBln", "printCln", "printSln").contains(metodo_conformado.getNombre())) {
                switch (metodo_conformado.getNombre()) {
                    //Ejecuto el Iprint
                    case "debugPrint" :
                    case "printI" : {
                        argumentos.get(0).generar_codigo();
                        Traductor.getInstance().gen("IPRINT");
                        break;
                    }
                    case "printC" : {
                        argumentos.get(0).generar_codigo();
                        Traductor.getInstance().gen("CPRINT");
                        break;
                    }
                    case "printB" : {
                        argumentos.get(0).generar_codigo();
                        Traductor.getInstance().gen("BPRINT");
                    }
                    case "printS" : {
                        argumentos.get(0).generar_codigo();
                        Traductor.getInstance().gen("SPRINT");
                        break;
                    }
                    case "read" : {
                        Traductor.getInstance().gen("READ");
                        break;
                    }
                    case "println" : {
                        Traductor.getInstance().gen("PRNLN");
                        break;
                    }
                    case "printBln" : {
                        argumentos.get(0).generar_codigo();
                        Traductor.getInstance().gen("BPRINT");
                        Traductor.getInstance().gen("PRNLN");
                        break;
                    }
                    case "printIln" : {
                        argumentos.get(0).generar_codigo();
                        Traductor.getInstance().gen("IPRINT");
                        Traductor.getInstance().gen("PRNLN");
                        break;
                    }
                    case "printCln" : {
                        argumentos.get(0).generar_codigo();
                        Traductor.getInstance().gen("CPRINT");
                        Traductor.getInstance().gen("PRNLN");
                        break;
                    }
                    case "printSln" : {
                        argumentos.get(0).generar_codigo();
                        Traductor.getInstance().gen("SPRINT");
                        Traductor.getInstance().gen("PRNLN");
                        break;
                    }

                }
            }

            else {
                LinkedList<EntradaParametro> argumentos_formales = metodo_conformado.get_lista_argumentos();

                //El CIR de la variable o retorno de metodo encadenado a izquierda al ejecutar
                //primario_decorator.generar_codigo(); ya queda en el tope de la pila

                if (!metodo_conformado.no_retorna()) {
                    //Si la unidad retorna un valor
                    Traductor.getInstance().gen("RMEM 1");
                    if (!metodo_conformado.es_estatico())
                        //Hago un swap para ir bajando el this si es dinamico
                        Traductor.getInstance().gen("SWAP");
                }

                for (int i = 0; i < argumentos_formales.size(); i++) {
                    //Esto dejaria el resultado de la expresion en la pila
                    argumentos.get(i).generar_codigo();
                    //Pongo el comentario del nombre del parametro (No se si no tengo q hacer un .STACK para q aparezca en la pila)
                    Traductor.getInstance().gen_comment_stack(argumentos_formales.get(i).getNombre());
                    if (!metodo_conformado.es_estatico())
                        //Hago un swap para ir bajando el this, asi este queda por debajo de los parametros
                        Traductor.getInstance().gen("SWAP");
                }

                if (!metodo_conformado.es_estatico()) {
                    //Si el metodo es dinamico
                    //Ahora que tenemos los parametros y el this, podemos hacer la llamada al metodo (hay que buscarlo en la VT del this)
                    //Por como funciona el LOADREF hay que hacer DUP para no perder la referencia al CIR (this)
                    Traductor.getInstance().gen("DUP");
                    //Hago el LOADREF para obtener la VT en el CIR (el offset de la VT es siempre 0)
                    Traductor.getInstance().gen("LOADREF 0");
                    //Obtengo de la VT el label del metodo conformado con el offset de ese metodo
                    Traductor.getInstance().gen("LOADREF " + metodo_conformado.get_offset());
                } else
                    //Si el metodo es estatico
                    Traductor.getInstance().gen("PUSH " + metodo_conformado.get_etiqueta());

                //Hago la llamada
                Traductor.getInstance().gen("CALL");
            }
        }
    }

}
