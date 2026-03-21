# Project Overview

This repository contains the source code for the Byte-Based-Randomized-Entropy-System-Scheduler-Based-RNG (BBRES-RNG). It is a custom, multithreaded Pseudo-Random Number Generator (PRNG) designed to serve as a high-quality entropy source.

The project's key feature is its innovative architecture that departs from standard library functions by harvesting randomness directly from the operating system's chaotic Thread Scheduler.


# Unique Architecture: Scheduler-Based Entropy

The BBRES-RNG's core mechanism is its unique approach to entropy collection:
Exploiting Concurrency Noise: The system deliberately creates and manages a controlled race condition by launching numerous worker threads.
Timing as Entropy: The microsecond timing variations of these threads' execution, determined by the unpredictable nature of the OS Scheduler, are treated as the system's entropy source.
Advanced Mixing: The results collected from these concurrent processes are aggregated and thoroughly mixed using a complex sequence of bitwise operations (XOR sums and shifts) to produce a single, seemingly unrepeatable random bit.

This approach ensures that the generated numbers are influenced by the real-time, non-deterministic state of the host machine, rather than a fixed mathematical seed.


# How It Works

The system is structured into several modular Java classes:
```RNG.java```: The primary class for generating random numbers within a specified range (min, max). It manages the overall process and includes constructors for setting the configuration parameter n and the randTech method.
```randomBitGeneratorModifiedRoot.java```: Contains the logic for generating a single random bit (0 or 1). It orchestrates the thread spawning and includes two distinct bit generation methods (generateRandBitG1 and generateRandBitG2).
```modRandomBitGenRNG.java``` & ```RandomBitGenRNG.java```/ Worker Threads: These classes handle the parallel collection of timing data. They are launched simultaneously, and their non-deterministic order of execution is the source of entropy.


# Getting Started

#Prerequisites

Java Development Kit (JDK) 8 or newer
A Java IDE (IntelliJ IDEA, Eclipse, VS Code with Java extensions) or a command-line environment for compilation.

# Building and Running

# Clone the Repository:
```
git clone -------
cd Byte-Based-Randomized-Entropy-System-Scheduler-Based-RNG
```
# Compile the Code:
```
javac src/Main.java
```
# Run the Main Test File:
```
java -cp src Main
```

# Usage Examples

The ```Main.java``` file provides several examples of how to instantiate and use the custom RNG:
Java

```
public class Main {  
    public static void main(String[] args) {  
        bbresRNG.RNG rng = new bbresRNG.RNG();  
        // 1. Random number between 0 and 10 (using defaults)
        System.out.println("Random number between 0 and 10: " + rng.bbresRNG());  
        // 2. Random number between 5 and 15
        System.out.println("Random number between 5 and 15: " + rng.bbresRNG(5, 15));  
        // 3. Random number with custom concurrency parameter n=50
        System.out.println("Random number between 1 and 100 with n=50: " + rng.bbresRNG(1, 100, 50));  
        // 4. Using the 2nd randomization technology (randTech=2)
        System.out.println("Random number smaller than 10000 using 2nd method with n = 35: " + rng.bbresRNG(0,10000,35,2));  
    }  
}
```

Note on Security: While ingeniously designed, this system is a proof-of-concept and has not undergone formal cryptographic auditing. It is not recommended for use in commercial, high-security, or production cryptographic applications.

