package AST.Acceso;

import AnalizadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemantica;
import AnalizadorSemantico.ExcepcionTipo;
import AnalizadorSemantico.Tipo;
import AnalizadorSemantico.TipoReferencia;

public class NodoCasting extends NodoAcceso{

    private NodoPrimario primario;
    //private Token token_clase_casteo;

    public NodoCasting(NodoPrimario primario, Token clase, String key_clase){
        super(clase,key_clase);
        this.primario = primario;
        //this.token_clase_casteo = clase;
    }

    public void esta_bien_definido() {

    }

    @Override
    public Tipo get_tipo_acceso() {
        return new TipoReferencia(token_acceso);
    }

    @Override
    public void mostrar_acceso() {
        System.out.println("Nodo Acceso Casting");
        System.out.println(token_acceso.get_lexema());
        primario.mostrar_acceso();
    }
}
