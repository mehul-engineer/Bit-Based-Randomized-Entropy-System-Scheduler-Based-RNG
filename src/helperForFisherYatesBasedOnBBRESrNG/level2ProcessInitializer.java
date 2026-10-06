package helperForFisherYatesBasedOnBBRESrNG;
public class level2ProcessInitializer extends Thread{
    protected volatile int[] ids;
    protected volatile int localStart;
    protected volatile int localEnd;
    protected volatile int id;
    protected volatile static int[] bitSelected = new int[32];

    public level2ProcessInitializer(int id, int[] ids, int start, int end){
        this.ids = ids;
        this.localStart = start;
        this.localEnd = end;
        this.id = id;
    }

    public void run(){
        workerProcess[] arr = new workerProcess[localEnd - localStart + 1];
        
        for(int i = localStart; i <= localEnd; i++){
            workerProcess.flag[i] = -1;
        }
    
        for(int i = 0,j = localStart; i <localEnd - localStart + 1; i++,j++){
            arr[i] = new workerProcess(ids[j],id);
        }

        for(int i = 0; i < localEnd - localStart + 1; i++){
            arr[i].start();
        }
        workerProcess.conc.set(id, 1);
        for(int i = 0; i < localEnd - localStart + 1; i++){
            try{
                arr[i].join();
            }
            catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        int xor = 0;
        int cut = (int) Math.ceil((localEnd - localStart + 1) * 0.2);
        for(int i = localStart; i < localStart+cut; i++){
            xor = xor ^ workerProcess.flag[i];
        }
        for(int i = localEnd-cut+1; i <= localEnd; i++){
            xor = xor ^ workerProcess.flag[i];
        }
        xor ^= xor << 10;
        xor ^= xor >>> 23;
        xor ^= xor << 7;
        bitSelected[id] = xor;
        for(int i = localStart; i <= localEnd; i++){
            workerProcess.flag[i] = -1;
        }
        workerProcess.conc.set(id, 0);
    }
    public static void main(String[] args){
        int[] arr = new int[RNG.n];
        level2ProcessInitializer prnt = new level2ProcessInitializer(0, arr, 0, RNG.n - 1);
        prnt.start();
        try{
            prnt.join();
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }System.out.println("Bit Selected: " + bitSelected[0]);
    }
}
