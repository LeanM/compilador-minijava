package AST.Sentencia;

import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemantica;
import AnalizadorSemantico.ExcepcionTipo;
import AnalizadorSemantico.Tipo;

public class NodoVarLocal_Asignacion extends NodoVarLocal{

    private NodoExpresion expresion_de_asignacion;

    public NodoVarLocal_Asignacion(Tipo tipo, Token variable,NodoExpresion exp) {
        super(tipo, variable);
        this.expresion_de_asignacion = exp;
    }

    @Override
    public void esta_bien_definido() throws ExcepcionTipo, ExcepcionSemantica {
        super.esta_bien_definido();
        expresion_de_asignacion.esta_bien_definido();
        if(!expresion_de_asignacion.get_tipo_expresion().es_de_tipo(tipo))
            throw new ExcepcionTipo(expresion_de_asignacion.getToken(),"El lado derecho de la asignacion no conforma al tipo del lado izquierdo");

    }

    @Override
    public void mostar_sentencia() {
        System.out.println("varLocal asignacion : "+variable.get_lexema());
        expresion_de_asignacion.mostrar_expresion();
    }
}
