package AST.Sentencia;

import AST.Acceso.NodoAcceso;
import AST.Acceso.NodoPrimario_Concreto;
import AnalizadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemantica;
import AnalizadorSemantico.ExcepcionTipo;

public abstract class NodoAsignacion extends NodoSentencia {

    protected NodoAcceso lado_izq;
    protected Token tipo_asignacion;

    public NodoAsignacion (NodoAcceso lado_izq, Token tipo_asignacion){
        super();
        this.lado_izq = lado_izq;
        this.tipo_asignacion = tipo_asignacion;
    }

    public void esta_bien_definido() throws ExcepcionTipo, ExcepcionSemantica {
        lado_izq.esta_bien_definido();

        if(en_metodo_static)
            lado_izq.chequeo_acceso_estatico();
    }
}
