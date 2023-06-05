class UseThreadLocal {
    public static ThreadLocal<Integer> makeThreadLocal(int counter) {
        // write your code here

        return  ThreadLocal.withInitial(()-> counter +1);
    }
}

