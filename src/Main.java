public class Main {
    public static void main(String[] args) {
        bbresRNG.RNG rng = new bbresRNG.RNG();
        System.out.println("Random number between 0 and 10: " + rng.bbresRNG());
        System.out.println("Random number between 5 and 15: " + rng.bbresRNG(5, 15));
        System.out.println("Random number between 1 and 100 with n=50: " + rng.bbresRNG(1, 100, 50));
        System.out.println("Random number between 1 and 100 with invalid n=1: " + rng.bbresRNG(1, 100, 1));
        System.out.println("Random number smaller than 10000: " + rng.bbresRNG(10000));
        System.out.println("Random number smaller than 10000 using 2nd method with n = 35: " + rng.bbresRNG(0,10000,35,2));
    }
}
