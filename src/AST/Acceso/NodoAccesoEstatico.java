package AST.Acceso;

import AnalizadorLexico.Token;

public class NodoAccesoEstatico extends NodoPrimario{

    //private Token token_clase;

    public NodoAccesoEstatico(Token clase){
        super(clase);
        //this.token_clase = clase;
    }

    @Override
    public void esta_bien_definido() {
        //Verificar semanticas de acceso estatico
    }


}
