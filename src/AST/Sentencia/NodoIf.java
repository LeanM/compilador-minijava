package AST.Sentencia;

import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemantica;
import AnalizadorSemantico.ExcepcionTipo;

public class NodoIf extends NodoSentencia {

    protected NodoExpresion condicion;
    protected NodoSentencia cuerpo_then;

    public NodoIf(NodoExpresion condicion, NodoSentencia then){
        super();
        this.condicion = condicion;
        this.cuerpo_then = then;
    }

    @Override
    public void esta_bien_definido() throws ExcepcionTipo, ExcepcionSemantica {
        condicion.esta_bien_definido();

        if(!condicion.get_tipo_expresion().getNombre().equals("boolean"))
            throw new ExcepcionTipo(condicion.getToken(),"Error de tipo : La expresion condicion en un if debe ser de tipo boolean");

        cuerpo_then.esta_bien_definido();
    }

    @Override
    public void mostar_sentencia() {
        System.out.println("-----------------------------------------------------------");
        System.out.println("Nodo If ");
        condicion.mostrar_expresion();
        cuerpo_then.mostar_sentencia();
        System.out.println("-----------------------------------------------------------");
    }

    @Override
    public void generar_codigo() {

    }
}
