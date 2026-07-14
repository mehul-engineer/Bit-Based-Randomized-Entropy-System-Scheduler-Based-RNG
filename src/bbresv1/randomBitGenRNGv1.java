package bbresv1;
public class randomBitGenRNGv1 extends Thread {
    private int id;
    protected static volatile int conc = 0;
    protected static volatile int flag[] = new int[RNGv1.n];
    public randomBitGenRNGv1(int idl){
        id = idl;
    }
    private synchronized static void getBitv1(int id){
        for(int i = 0; i < flag.length; i++){
            if(flag[i] == -1){
                flag[i] = id;
                break;
            }
        }
    }
    public void run(){
        while(conc == 0){
            Thread.onSpinWait();
        }
            getBitv1(id);
        }
    
    
}
