package bbresRNG;
import java.util.concurrent.atomic.AtomicIntegerArray;
public class randomBitGenRNG extends Thread {
    private int id;
    protected static volatile AtomicIntegerArray conc = new AtomicIntegerArray(64);
    protected static volatile int flag[] = new int[RNG.n];
    protected volatile int parId;
     protected volatile static int nos;
    public randomBitGenRNG(int idl,int parId){
        id = idl;
        this.parId = parId;
    }
    private synchronized static void getBit(int id){
        for(int i = 0; i < flag.length; i++){
            if(flag[i] == -1){
                flag[i] = id;
                break;
            }
        }
    }
    public void run(){
        while(conc.get(parId) == 0){
            Thread.onSpinWait();
        }
            getBit(id);
        }
}
