package AST.Acceso;

public abstract class NodoPrimario extends NodoAcceso{

    private NodoEncadenado encadenado;

    public NodoPrimario(){
        super();
    }

    public void setEncadenado(NodoEncadenado encadenado){
        this.encadenado = encadenado;
    }
}
