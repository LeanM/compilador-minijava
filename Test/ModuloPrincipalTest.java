import AnalizadorSemantico.TablaSimbolos;
import kotlin.test.AssertionsKt;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

public class ModuloPrincipalTest {

    private static String directorio_archivos = "Test/archivos-test-etapa3/";
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private String errorCode;

    @Before
    public  void setUpClass() {
        System.setOut(new PrintStream(outContent));
    }

    @After
    public  void tearDownClass() {
        System.setOut(originalOut);
    }

    @Test
    public void main() {
        assertEquals("asd","asd");
        for(int i=0;i<=4;i++){
            //pruebas_etapa_3(i);
        }
        sin_errores();

        //pruebas_etapa_4();
    }

    //------------------------------------------ETAPA 3-----------------------------------------------------------------------------//
    void pruebas_etapa_3(int tipo_prueba){
        switch (tipo_prueba) {
            case 1 : clase_duplicada();
            case 2 : atributos_duplicados();
            case 3 : herencia_circular();
            case 4 : super_no_declarado();
            case 0 : sin_errores();
        }
    }

    void sin_errores(){
        //Prueba dos clases con el mismo nombre
        errorCode = "[SinErrores]";
        String [] args = {directorio_archivos+"sin-errores.txt"};
        ModuloPrincipal.main(args);
        assertThat("No se encontro el codigo: " + errorCode,  outContent.toString(), CoreMatchers.containsString(errorCode));
    }

    void clase_duplicada () {
        //Prueba dos clases con el mismo nombre
        errorCode = "ya esta declarada";
        String [] args = {directorio_archivos+"clases-repetidas.txt"};
        ModuloPrincipal.main(args);
        assertThat("No se encontro el codigo: " + errorCode,  outContent.toString(), CoreMatchers.containsString(errorCode));
    }

    void atributos_duplicados() {
        //Prueba una clase con dos atributos con el mismo nombre
        errorCode = "atributo declarado";
        String [] args = {directorio_archivos+"atr-repetidos.txt"};
        ModuloPrincipal.main(args);
        assertThat("No se encontro el codigo: " + errorCode,  outContent.toString(), CoreMatchers.containsString(errorCode));
    }

    void herencia_circular() {
        //Prueba una clase con dos atributos con el mismo nombre
        errorCode = "herencia circular";
        String [] args = {directorio_archivos+"herencia-circular.txt"};
        ModuloPrincipal.main(args);
        assertThat("No se encontro el codigo: " + errorCode,  outContent.toString(), CoreMatchers.containsString(errorCode));
    }

    void super_no_declarado(){
        //Prueba una clase con dos atributos con el mismo nombre
        errorCode = "no esta declarada";
        String [] args = {directorio_archivos+"super-nulo.txt"};
        ModuloPrincipal.main(args);
        assertThat("No se encontro el codigo: " + errorCode,  outContent.toString(), CoreMatchers.containsString(errorCode));
    }

//--------------------------------------------------------------------------------------------------------------------------//
}