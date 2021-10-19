// Prueba encabezados de metodos redefinicion valida
class A {
    dynamic void m3(A p1, B p2)
    {}  
}
class B extends A {
    dynamic void m3(A p1, B p2)
    {}  
}

class Init{
    static void main()
    { }
}




