class HashFunctions {

    private final int a;
    private final int b;
    private final int m1;
    private static final int p = 15486907;



    public HashFunctions(String a,String b,String m1){
        this.a= Integer.parseInt(a);
        this.b=Integer.parseInt(b);
        try{
            this.m1=Integer.parseInt(m1);
        }
        catch(Exception e){
            throw new IllegalArgumentException("the input is not number");
        }


    }

    public long activateFunction(long k)
    {
        return (((a*k+b)% p) %m1);

    }
}
