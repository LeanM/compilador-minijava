package AST.Acceso;

import AnalizadorLexico.Token;

public class NodoCasting {

    private NodoPrimario primario;
    private Token token_clase_casteo;

    public NodoCasting(NodoPrimario primario, Token clase){
        this.primario = primario;
        this.token_clase_casteo = clase;
    }

    public void esta_bien_definido() {

    }
}
