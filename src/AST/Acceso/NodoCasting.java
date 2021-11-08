package AST.Acceso;

import AnalizadorLexico.Token;
import AnalizadorSemantico.*;

public class NodoCasting extends NodoAcceso{

    protected NodoPrimario_Component primario;

    public NodoCasting(NodoPrimario_Component primario, Token clase){
        super(clase);
        this.primario = primario;
    }

    public void esta_bien_definido() throws ExcepcionTipo, ExcepcionSemantica {
        if(TablaSimbolos.getInstance().get_tabla_clases().containsKey(token_acceso.get_lexema())){
            primario.esta_bien_definido();
            Tipo tipo_primario = primario.get_tipo_acceso();
            Tipo tipo_acceso = this.get_tipo_acceso();
            if(!tipo_acceso.es_de_tipo(tipo_primario) && !tipo_primario.es_de_tipo(tipo_acceso))
                throw new ExcepcionTipo(token_acceso,"No es posible hacer casting de "+tipo_primario.getNombre()+" a el tipo "+token_acceso.get_lexema()+" ya que no son subtipos.");

        }
        else throw new ExcepcionSemantica(token_acceso,"No se puede hacer casting a una clase que no esta definida.");

    }

    public Tipo get_tipo_acceso() {
        return new TipoReferencia(token_acceso);
    }

    public boolean puede_ser_asignado(){
        return primario.puede_ser_asignado();
    }

    @Override
    public void chequeo_acceso_estatico() throws ExcepcionTipo, ExcepcionSemantica {
        primario.chequeo_acceso_estatico();
    }

    public NodoPrimario_Concreto obtener_primario_concreto(){
        return primario.obtener_primario_concreto();
    }

    @Override
    public void mostrar_acceso() {
        System.out.println("Nodo Acceso Casting");
        System.out.println(token_acceso.get_lexema());
        primario.mostrar_acceso();
    }

}
