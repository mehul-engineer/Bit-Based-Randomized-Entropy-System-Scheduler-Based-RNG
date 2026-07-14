package bbresRNG;
public class modRandomBitGenRNG extends Thread{
    protected volatile int ids[] = new int[RNG.n];
    protected volatile int localStart;
    protected volatile int localEnd;
    protected volatile int id;
    protected volatile static int bitSelected[] = new int[64];
    public modRandomBitGenRNG(int id, int ids[], int start, int end){
        this.ids = ids;
        this.localStart = start;
        this.localEnd = end;
        this.id = id;
    }

    public void run(){
        randomBitGenRNG arr[] = new randomBitGenRNG[localEnd - localStart + 1];
        
        for(int i = localStart; i <= localEnd; i++){
            randomBitGenRNG.flag[i] = -1;
        }
    
        for(int i = 0,j = localStart; i <localEnd - localStart + 1; i++,j++){
            arr[i] = new randomBitGenRNG(ids[j],id);
        }

        for(int i = 0; i < localEnd - localStart + 1; i++){
            arr[i].start();
        }
        randomBitGenRNG.conc.set(id, 1);
        for(int i = 0; i < localEnd - localStart + 1; i++){
            try{
                arr[i].join();
            }
            catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        int xor = 0;
        int cut = (int) Math.ceil(RNG.n * 0.2);
        for(int i = 0; i < cut; i++){
            xor = xor ^ randomBitGenRNG.flag[i];
        }
        cut = randomBitGenRNG.flag.length - cut;
        for(int i = cut; i < randomBitGenRNG.flag.length; i++){
            xor = xor ^ randomBitGenRNG.flag[i];
        }
        xor ^= xor << 10;
        xor ^= xor >>> 23;
        xor ^= xor << 7;
        bitSelected[id] = xor;
        for(int i = 0; i < randomBitGenRNG.flag.length; i++){
            randomBitGenRNG.flag[i] = -1;
        }
        randomBitGenRNG.conc.set(id, 0);
    }
    public static void main(String[] args){
        int arr[] = new int[RNG.n];
        modRandomBitGenRNG prnt = new modRandomBitGenRNG(0, arr, 0, RNG.n - 1);
        prnt.start();
        try{
            prnt.join();
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }System.out.println("Bit Selected: " + bitSelected[0]);
    }
}
