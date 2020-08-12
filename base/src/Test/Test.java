public class Test {
    public static void main(String[] args) throws Exception {
        //P p;
        //X x = new X();
        // System.out.println(P.i);
        System.out.println(P.j);

    }

    public static class P {
        final static int i = 8;
        static int j = 9;
        static {
            System.out.println("P");
        }
    }

    public static class X extends P {
        static {
            System.out.println("X");
        }
    }
}
