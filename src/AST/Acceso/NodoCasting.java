package AST.Acceso;

import AnalizadorLexico.Token;

public class NodoCasting extends NodoAcceso{

    private NodoPrimario primario;
    //private Token token_clase_casteo;

    public NodoCasting(NodoPrimario primario, Token clase){
        super(clase);
        this.primario = primario;
        //this.token_clase_casteo = clase;
    }

    public void esta_bien_definido() {

    }

    @Override
    public void mostrar_acceso() {
        System.out.println("Nodo Acceso Casting");
        System.out.println(token_acceso.get_lexema());
        primario.mostrar_acceso();
    }
}
