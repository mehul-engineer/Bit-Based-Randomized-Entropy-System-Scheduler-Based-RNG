package bbresv1;
public class RNGv1 {
    private static long min;
    private static long max;
    protected static int n;
    public long bbresRNGv1(){
        RNGv1.min = 0;
        RNGv1.max = 10;
        RNGv1.n = 71;
        return generateRandomv1(RNGv1.min, RNGv1.max);
    }
    public long bbresRNGv1(long min,long max)
    {
        RNGv1.min = min;
        RNGv1.max = max;
        RNGv1.n = 71;

        return generateRandomv1(RNGv1.min, RNGv1.max);
    }

    public long bbresRNGv1(long min,long max,int n)
    {
        RNGv1.min = min;
        RNGv1.max = max;
        if(n <= 2 || n > 1000){
             RNGv1.n = 71;
             System.out.println("Invalid n value. Setting n to default value of 71.");
        }
        else{
        RNGv1.n = n;}
        return generateRandomv1(RNGv1.min, RNGv1.max);
    }

    protected static String getBinaryv1(long x){
        String binary = "";
        if(x == 0){
            binary = "0";
        }
        else{
        while(x>0){
            binary = String.valueOf((long)x%2) + binary;
            x = x/2;
        }
        }
        return binary;
    }

    public static long getIntegerv1(String x){
        long value = 0;
        long length = x.length();
        for(int i = 0; i <= length-1; i++){
            if(x.charAt(0) == '1'){
                value += Math.pow(2,length-1-i);
            }
            x = x.substring(1);
        }
        return value;
    }
    private static String getRandomBinaryv1(){
        String bits = "";
        long maxBinaryLength = RNGv1.getBinaryv1(max).length();
        for(int i = 0;i< maxBinaryLength;i++){
        Thread t = new randomBitGenRNGPrntv1();
        t.start();
        try{
            t.join();    
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
        bits += randomBitGenRNGPrntv1.bitSelected;
        }
        return bits;
    }

    private static long generateRandomv1(long min,long max)
    {
        long randomNo = getIntegerv1(getRandomBinaryv1());
        randomNo = min + (randomNo % (max - min + 1));
        return randomNo;
    }

    public static void main(String[] args){
        RNGv1 rng = new RNGv1();
        for(int i = 0; i < 10; i++){
        System.out.print( rng.bbresRNGv1(0,10000)+",");
        }
  }
}