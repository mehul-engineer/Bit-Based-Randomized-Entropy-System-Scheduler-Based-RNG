package bbresRNG;
public class RNG{
    protected static long min;
    protected static long max;
    public static int n;
    protected static long randTech;
    public long bbresRNG(){
        RNG.min = 0;
        RNG.max = 10;
        RNG.n = 71;
        RNG.randTech = 1;
        
        return generateRandom(RNG.min, RNG.max);
    }
    public long bbresRNG(long min,long max)
    {
        RNG.min = min;
        RNG.max = max;
        RNG.n = 71;
        RNG.randTech = 1;
        

        return generateRandom(RNG.min, RNG.max);
    }

    public long bbresRNG(long max)
    {
        RNG.min = 0;
        RNG.max = max;
        RNG.n = 71;
        RNG.randTech = 1;


        return generateRandom(RNG.min, RNG.max);
    }

    public long bbresRNG(long min,long max,int n)
    {
        RNG.min = min;
        RNG.max = max;

        if(n <= 2 || n > 1000){
             RNG.n = 71;
             System.out.println("Invalid n value. Setting n to default value of 71.");
        }
        else{
        RNG.n = n;}
        RNG.randTech = 1;
        return generateRandom(RNG.min, RNG.max);
    }

    public long bbresRNG(long min,long max,int n,int randTech)
    {
        RNG.min = min;
        RNG.max = max;

        if(n <= 2 || n > 1000){
            RNG.n = 71;
            System.out.println("Invalid n value. Setting n to default value of 71.");
        }
        else{
            RNG.n = n;}
        if(randTech == 1){RNG.randTech = 1;}
        else if(randTech == 2){RNG.randTech = 2;}
        else{System.out.println("Invalid rand tech. Setting rand tech to 1.");RNG.randTech = 1;}

        return generateRandom(RNG.min, RNG.max);
    }

    protected static String getBinary(long x){
        String binary = "";
        if(x == 0){
            binary = "0";
        }
        else{
        while(x>0){
            binary = String.valueOf((int)x%2) + binary;
            x = x/2;
        }
        }
        return binary;
    }

    public static long getInteger(String x){
        long value = 0;
        int length = x.length();
        for(int i = 0; i <= length-1; i++){
            if(x.charAt(0) == '1'){
                value += (long)Math.pow(2,length-1-i);
            }
            x = x.substring(1);
        }
        return value;
    }
    private static String getRandomBinary(long maX){
        String bits = "";
        int maxBinaryLength = RNG.getBinary(maX).length();
        for(int i = 0;i< maxBinaryLength;i++){
        if(RNG.randTech == 1){bits += randomBitGeneratorModifiedRoot.generateRandBitG1();}
        else{bits += randomBitGeneratorModifiedRoot.generateRandBitG2();}

        }
        return bits;
    }

    private static long generateRandom(long min, long max) {
        long range = max - min;

        if (range < 0) {
            throw new RuntimeException("Invalid range");
        }

       if(range == 0){
           return min;
       }
       long randomNo;
       do{
           randomNo = getInteger(getRandomBinary(range));
           randomNo = randomNo & Long.MAX_VALUE;
       }while(randomNo > range);
       return min+randomNo;
    }

    public static void main(String[] args){
        RNG rng = new RNG();
        for(int i = 0;i < 10;i++) {
            System.out.print(rng.bbresRNG(1L,10000L) + ",");
        }
    }
}