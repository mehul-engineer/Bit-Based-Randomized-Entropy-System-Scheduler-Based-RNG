package helperForFisherYatesBasedOnBBRESrNG;
import java.util.concurrent.atomic.AtomicIntegerArray;
public class workerProcess extends Thread {
    private int id;
    protected static volatile AtomicIntegerArray conc = new AtomicIntegerArray(32);
    protected static volatile int[] flag = new int[1000];
    protected volatile int parId;
    protected static int[] localStart = new int[32];
    protected static int[] localEnd = new int[32];

    public workerProcess(int idl, int parId) {
        id = idl;
        this.parId = parId;
    }
    private synchronized static void getBit(int id,int parId){
        for(int i = localStart[parId]; i <= localEnd[parId]; i++){
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
            getBit(id,parId);
        }
}
