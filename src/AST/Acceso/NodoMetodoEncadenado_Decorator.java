package AST.Acceso;

import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;
import AnalizadorSemantico.*;

import java.io.IOException;
import java.util.LinkedList;

public class NodoMetodoEncadenado_Decorator extends NodoEncadenado_Decorator{

    protected LinkedList<NodoExpresion> argumentos;

    public NodoMetodoEncadenado_Decorator(Token token_metodo_encadenado,LinkedList<NodoExpresion> args, NodoPrimario_Component primario) {
        super(token_metodo_encadenado, primario);
        this.argumentos = args;
    }

    @Override
    public void esta_bien_definido() throws ExcepcionTipo, ExcepcionSemantica {
        primario_decorator.esta_bien_definido();
        Tipo tipo_izq = primario_decorator.get_tipo_acceso();

        if(tipo_izq.esPrimitivo())
            throw new ExcepcionTipo(token_acceso,"El tipo de retorno del encadenado a izquierda no puede ser un tipo primitivo ni void");

        EntradaMetodo metodo_conforma = TablaSimbolos.getInstance().conforma_metodo(token_acceso,argumentos,tipo_izq.getNombre());
        if(metodo_conforma == null)
            throw new ExcepcionTipo(token_acceso,"La llamada a metodo "+token_acceso.get_lexema()+" no conforma con ningun metodos de la clase del encadenado de la izquierda ( "+tipo_izq.getNombre()+" )");
    }

    @Override
    public Tipo get_tipo_acceso() throws ExcepcionTipo, ExcepcionSemantica {
        Tipo toReturn;
        Tipo tipo_izq = primario_decorator.get_tipo_acceso();
        EntradaMetodo metodo_conforma = TablaSimbolos.getInstance().conforma_metodo(token_acceso,argumentos,tipo_izq.getNombre());
        if(metodo_conforma == null)
            throw new ExcepcionTipo(token_acceso,"La llamada a metodo "+token_acceso.get_lexema()+" no conforma con ningun metodos de la clase del encadenado de la izquierda ( "+tipo_izq.getNombre()+" )");
        else
            toReturn = metodo_conforma.get_tipo();

        return toReturn;
    }

    @Override
    public void generar_codigo() throws IOException {

    }

}
