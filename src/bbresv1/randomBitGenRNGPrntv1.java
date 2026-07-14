package bbresv1;
public class randomBitGenRNGPrntv1 extends Thread{
    protected volatile static String bitSelected;
    public void run(){
        int max = RNGv1.n;
        randomBitGenRNGv1 arr[] = new randomBitGenRNGv1[max];
        
        for(int i = 0; i < randomBitGenRNGv1.flag.length; i++){
            randomBitGenRNGv1.flag[i] = -1;
        }
    
        for(int i = 0; i < max; i++){
            arr[i] = new randomBitGenRNGv1(i);
        }
        for(int i = 0; i < max; i++){
            arr[i].start();
        }
        randomBitGenRNGv1.conc = 1;
        for(int i = 0; i < max; i++){
            try{
                arr[i].join();
            }
            catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        int xor = 0;
        int cut = (int) Math.ceil(RNGv1.n * 0.2);
        for(int i = 0; i < cut; i++){
            xor = xor ^ randomBitGenRNGv1.flag[i];
        }
        cut = randomBitGenRNGv1.flag.length - cut;
        for(int i = cut; i < randomBitGenRNGv1.flag.length; i++){
            xor = xor ^ randomBitGenRNGv1.flag[i];
        }
        xor ^= xor << 10;
        xor ^= xor >>> 23;
        xor ^= xor << 7;
        bitSelected = String.valueOf(xor&1);
        for(int i = 0; i < randomBitGenRNGv1.flag.length; i++){
            randomBitGenRNGv1.flag[i] = -1;
        }
        randomBitGenRNGv1.conc = 0;
    }
    public static void main(String[] args){
        randomBitGenRNGPrntv1 prnt = new randomBitGenRNGPrntv1();
        prnt.start();
        try{
            prnt.join();
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }System.out.println("Bit Selected: " + bitSelected);
    }
}
