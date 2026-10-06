package bbresRNG;
public class level1ProcessInitializer {
    protected static long nos;

    protected static String generateRandBitG2(){
      int n = RNG.n;
      nos = 0;
      System.out.println("Randomly Shuffling IDs:");
        
        int[] arr = new int[n];
        for(int i = 0; i < n; i++){
            arr[i] = i;
        }
        int c = 0;
        int pc = ((n-1)/10);
        pc = pc == 0?1:pc;
        long i;
        for(i = n - 1; i > 0; i--){
            //////Progress Bar
            if(c%pc == 0){
                System.out.print("▒ ");   
            }
            c++;
            /////Progress Bar
            helperForFisherYatesBasedOnBBRESrNG.RNG rngv1 = new  helperForFisherYatesBasedOnBBRESrNG.RNG();
            long j = rngv1.bbresRNG(0,i);
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
        if(noOfThreads > 32){
            noOfThreads = 32;
        }
        System.out.println("Segmentation Block Size Finalized...");
        int id = 0;
        int temp = n;
        int curr = 0;
        level2ProcessInitializer[] tr = new level2ProcessInitializer[(int)noOfThreads];
        for(i = 0;i < noOfThreads;i++){
            if(temp>=32 && i!=noOfThreads-1){
                tr[(int)i] = new level2ProcessInitializer(id++,arr, curr,curr+31);
                workerProcess.localStart[(int)i] = curr;
                workerProcess.localEnd[(int)i] = curr+31;
                curr += 32;
                temp -= 32;
            }
            else{
                tr[(int)i] = new level2ProcessInitializer(id++,arr, curr,curr+temp-1);
                workerProcess.localStart[(int)i] = curr;
                workerProcess.localEnd[(int)i] = curr+temp-1;
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
       nos^= level2ProcessInitializer.bitSelected[(int)i];
    }
        return String.valueOf(nos&1);
    }

    protected static String generateRandBitG1(){
      int n = RNG.n;
      nos = 0;
        
        int[] arr = new int[n];

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
        if(noOfThreads > 32){
            noOfThreads = 32;
        }
        int id = 0;
        int temp = n;
        int curr = 0;
        level2ProcessInitializer[] tr = new level2ProcessInitializer[noOfThreads];
        for(int i = 0;i < noOfThreads;i++){
            if(temp>=32 && i!=noOfThreads-1){
                tr[i] = new level2ProcessInitializer(id++,arr, curr,curr+31);
                workerProcess.localStart[(int)i] = curr;
                workerProcess.localEnd[(int)i] = curr+31;
                curr += 32;
                temp -= 32;
            }
            else{
                                
                tr[i] = new level2ProcessInitializer(id++,arr, curr,curr+temp-1);
                workerProcess.localStart[(int)i] = curr;
                workerProcess.localEnd[(int)i] = curr+temp-1;
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
        nos = level2ProcessInitializer.bitSelected[0];
    for(int i = 1; i < noOfThreads; i++){
       nos^= level2ProcessInitializer.bitSelected[i];
    }
    return String.valueOf(nos&1);
    }

}

