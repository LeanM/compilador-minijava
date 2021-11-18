package AST.Acceso;

import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;
import AnalizadorSemantico.*;

import java.io.IOException;
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
            LinkedList<EntradaParametro> argumentos_formales = metodo_conformado.get_lista_argumentos();

            for (int i = 0; i < argumentos_formales.size(); i++) {
                //Esto dejaria el resultado de la expresion en la pila
                argumentos.get(i).generar_codigo();
            }

            //  Pusheo la etiqueta del metodo a invocar y hago call
            Traductor.getInstance().gen("PUSH "+metodo_conformado.get_etiqueta());
            Traductor.getInstance().gen("CALL");
        }
    }

}
