package AST.Acceso;

import AnalizadorLexico.Token;
import AnalizadorSemantico.*;

public class NodoCasting extends NodoAcceso{

    private NodoPrimario primario;
    //private Token token_clase_casteo;

    public NodoCasting(NodoPrimario primario, Token clase, String key_clase){
        super(clase,key_clase);
        this.primario = primario;
        //this.token_clase_casteo = clase;
    }

    public void esta_bien_definido() throws ExcepcionTipo, ExcepcionSemantica {
        if(TablaSimbolos.getInstance().get_tabla_clases().containsKey(key_clase)){
            primario.esta_bien_definido();
            this.get_tipo_acceso().es_de_tipo(primario.get_tipo_acceso());
        }

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
