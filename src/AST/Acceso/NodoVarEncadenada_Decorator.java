package AST.Acceso;

import AST.Sentencia.Var;
import AST.Sentencia.Var_Instancia;
import AnalizadorLexico.Token;
import AnalizadorSemantico.*;

import java.io.IOException;

public class NodoVarEncadenada_Decorator extends NodoEncadenado_Decorator{

    private Var_Instancia acceso_tipo_variable;
    private EntradaAtributo atributo_conformado;

    public NodoVarEncadenada_Decorator(Token token_var_encadenada, NodoPrimario_Component primario) {
        super(token_var_encadenada, primario);
        acceso_tipo_variable = null;
        atributo_conformado = null;
    }

    @Override
    public void esta_bien_definido() throws ExcepcionTipo, ExcepcionSemantica {
        primario_decorator.esta_bien_definido();
        Tipo tipo_izq = primario_decorator.get_tipo_acceso();

        if(tipo_izq.esPrimitivo())
            throw new ExcepcionTipo(token_acceso,"El tipo de retorno del encadenado a izquierda no puede ser un tipo primitivo ni void");

        atributo_conformado = TablaSimbolos.getInstance().conforma_atributo(token_acceso,tipo_izq.getNombre());
        if(atributo_conformado == null)
            throw new ExcepcionTipo(token_acceso,"El atributo encadenado no es un atributo de la clase del encadenado de la izquierda");
        if (!(primario_decorator instanceof NodoAccesoThis) && atributo_conformado.get_visibilidad().equals("private"))
            throw new ExcepcionTipo(token_acceso,"El atributo encadenado no esta al alcance, es privado en la clase encadenada a izquierda");

        acceso_tipo_variable = new Var_Instancia(atributo_conformado,this);
    }

    @Override
    public Tipo get_tipo_acceso() throws ExcepcionTipo, ExcepcionSemantica {
        Tipo tipo_izq;
        tipo_izq = primario_decorator.get_tipo_acceso();
        atributo_conformado = TablaSimbolos.getInstance().conforma_atributo(token_acceso,tipo_izq.getNombre());
        if(atributo_conformado == null)
            throw new ExcepcionTipo(token_acceso,"El atributo encadenado no es un atributo de la clase del encadenado de la izquierda o no esta al alcance");

        return atributo_conformado.getTipo();
    }

    public boolean puede_ser_asignado(){
        return true;
    }
    /*
    public EntradaAtributo es_variable_instancia() throws ExcepcionTipo, ExcepcionSemantica {
        Tipo tipo_izq;
        tipo_izq = primario_decorator.get_tipo_acceso();
        EntradaAtributo atributo_conforma = TablaSimbolos.getInstance().conforma_atributo(token_acceso,tipo_izq.getNombre());
        if(atributo_conforma == null)
            throw new ExcepcionTipo(token_acceso,"El atributo encadenado no es un atributo de la clase del encadenado de la izquierda o no esta al alcance");
        else
            return atributo_conforma;
    }


     */
    @Override
    public void generar_codigo() throws IOException, ExcepcionTipo, ExcepcionSemantica {
        primario_decorator.generar_codigo();
        if(this.es_ultimo_encadenado){
            if(!es_lado_izq)    //Solo verifico esto, por que en una variable encadenada, siempre va a ser una variable de instancia
                Traductor.getInstance().gen("LOADREF "+atributo_conformado.get_offset());
            else {
                Traductor.getInstance().gen("SWAP");
                Traductor.getInstance().gen("STOREREF "+atributo_conformado.get_offset());
            }
        }
        //acceso_tipo_variable.generar_codigo();
    }

    public Var get_acceso_tipo_var(){
        return acceso_tipo_variable;
    }
}
