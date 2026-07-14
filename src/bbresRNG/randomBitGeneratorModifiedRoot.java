package bbresRNG;
public class randomBitGeneratorModifiedRoot {
    protected static long nos;

    protected static String generateRandBitG2(){
      int n = RNG.n;
      nos = 0;
      System.out.println("Randomly Shuffling IDs:");
        
        int arr[] = new int[n];
        for(int i = 0; i < n; i++){
            arr[i] = i;
        }
        int c = 0;
        int pc = (int)((n-1)/10);
        pc = pc == 0?1:pc;
        long i;
        for(i = n - 1; i > 0; i--){
            //////Progress Bar
            if(c%pc == 0){
                System.out.print("▒ ");   
            }
            c++;
            /////Progress Bar
            bbresv1.RNGv1 rngv1 = new bbresv1.RNGv1();
            long j = rngv1.bbresRNGv1(0,i);

            int temp = arr[(int)i];
            arr[(int)i] = arr[(int)j];
            arr[(int)j] = temp;
            
        }
        System.out.println("\nSegregating Threads");
        long nearest = 32;
        for(i = 1;i*32<=n+32;i++){
            if(Math.abs(i*32-n) <= Math.abs(nearest-n)){
                nearest = i*32;
            }
        }
        long noOfThreads = nearest/32;
        if(noOfThreads > 64){
            noOfThreads = 64;
        }
        System.out.println("Segmentation Block Size Finalized...");
        int id = 0;
        int temp = n;
        int curr = 0;
        modRandomBitGenRNG tr[] = new modRandomBitGenRNG[(int)noOfThreads];
        for(i = 0;i < noOfThreads;i++){
            if(temp>=32 && i!=noOfThreads-1){
                tr[(int)i] = new modRandomBitGenRNG(id++,arr, curr,curr+31);
                curr += 32;
                temp -= 32;
            }
            else{
                                
                tr[(int)i] = new modRandomBitGenRNG(id++,arr, curr,curr+temp-1);
                curr += temp;
                temp = 0;
            }
        }
        id-=1;

        for(i = 0; i < noOfThreads; i++){
            tr[(int)i].start();
        }
        for(i = 0; i < noOfThreads; i++){
            try{
                tr[(int)i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
        }
    for(i = 0; i < noOfThreads; i++){
       nos^=modRandomBitGenRNG.bitSelected[(int)i];
    }
    return String.valueOf(nos&1);
    }
   


    protected static String generateRandBitG1(){
      int n = RNG.n;
      nos = 0;
        
        int arr[] = new int[n];

        for(int i = 0; i < n; i++){
            arr[i] = i;
        }
        int nearest = 32;
        for(int i = 1;i*32<=n+32;i++){
            if(Math.abs(i*32-n) <= Math.abs(nearest-n)){
                nearest = i*32;
            }
        }
        int noOfThreads = nearest/32;
        if(noOfThreads > 64){
            noOfThreads = 64;
        }
        int id = 0;
        int temp = n;
        int curr = 0;
        modRandomBitGenRNG tr[] = new modRandomBitGenRNG[noOfThreads];
        for(int i = 0;i < noOfThreads;i++){
            if(temp>=32 && i!=noOfThreads-1){
                tr[i] = new modRandomBitGenRNG(id++,arr, curr,curr+31);
                curr += 32;
                temp -= 32;
            }
            else{
                                
                tr[i] = new modRandomBitGenRNG(id++,arr, curr,curr+temp-1);
                curr += temp;
                temp = 0;
            }
        }
        id-=1;

        for(int i = 0; i < noOfThreads; i++){
            tr[i].start();
        }
        for(int i = 0; i < noOfThreads; i++){
            try{
                tr[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
        }
    for(int i = 0; i < noOfThreads; i++){
       nos^=modRandomBitGenRNG.bitSelected[i];
    }
    return String.valueOf(nos&1);
    }

    }

