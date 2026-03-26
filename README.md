<div align="center">

# BBRES-RNG

**Bit-Based Randomized Entropy System — Scheduler-Based RNG**

*a multithreaded random number generator that harvests real-time entropy from OS thread-scheduling chaos*

<br/>

<img src="https://img.shields.io/badge/language-Java-orange?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java">
<img src="https://img.shields.io/badge/JDK-8%2B-green?style=for-the-badge" alt="JDK 8+">
<img src="https://img.shields.io/badge/Statistical_Tests-30%20/%2030-brightgreen?style=for-the-badge" alt="30/30">
<img src="https://img.shields.io/badge/build-passing-brightgreen?style=for-the-badge" alt="Build: Passing">
<img src="https://img.shields.io/badge/license-Custom%20Restrictive-red?style=for-the-badge" alt="License">

<br/><br/>

**`30/30 Statistical Tests Passed`** · **`Outperforms Java Math.random()`** · **`Matches Java SecureRandom`**

</div>

<br/>

---

<br/>

## `$ cat README`

> No mathematical seed. No deterministic shortcut.
>
> **BBRES-RNG** takes a fundamentally different approach to generating random numbers. Instead of relying on standard library algorithms or fixed mathematical seeds, it deliberately creates controlled concurrency and treats the **unpredictable timing behaviour of the OS thread scheduler** as its entropy source.
>
> Every number it produces is shaped by the live, non-deterministic state of the host machine — making each output practically unrepeatable.

<br/>

## `$ diff traditional_prng bbres_rng`

| Traditional PRNGs | BBRES-RNG |
|:--|:--|
| Seed-based (deterministic from the same seed) | Entropy-based (non-deterministic by design) |
| Mathematical formulas (LCG, Mersenne Twister, etc.) | Harvests timing noise from OS thread scheduling |
| Reproducible given the same initial state | Practically unreproducible — tied to real-time system state |
| Single-threaded execution | Multi-threaded concurrency architecture |

<br/>

## `$ cat pipeline.txt`

```
                         BBRES-RNG Pipeline

  +----------+   +----------+   +----------+
  | Thread 1 |   | Thread 2 |   | Thread N |   <- Spawn
  +----+-----+   +----+-----+   +----+-----+
       |              |              |
       v              v              v
  +----------------------------------------------+
  |      OS Thread Scheduler (Entropy Source)     |
  +----------------------+-----------------------+
                         |
                         v
  +----------------------------------------------+
  |      Timing Data Collection & Capture         |
  +----------------------+-----------------------+
                         |
                         v
  +----------------------------------------------+
  |      Bitwise Mixing (XOR + Bit Shifts)        |
  +----------------------+-----------------------+
                         |
                         v
                  [ Random Output ]
```

**Stage 1 — Thread Spawning & Controlled Race Conditions**
Multiple worker threads launch simultaneously. The OS scheduler decides their execution order — inherently unpredictable, varying by microseconds based on CPU state, system load, and kernel-level scheduling.

**Stage 2 — Timing-Based Entropy Collection**
Each worker thread captures fine-grained timing data during execution. Microsecond-level variations between thread timings become raw entropy input.

**Stage 3 — Bitwise Mixing & Aggregation**
Collected timing results are processed through XOR sums and bit shifts, producing a single random bit per cycle. Bits are assembled to construct the final random number.

<br/>

## `$ ./validate --all --compare`

> Validated against a comprehensive **30-test battery** spanning NIST SP 800-22, extended statistical tests, spectral analysis, adversarial ML attacks, cryptographic wrapper validation, and integer-level distribution tests. Benchmarked head-to-head against Java's two standard RNG implementations.

```
Test Parameters
---------------
Bit Sample Size       2,000,000 bits
Integer Sample Size   100,000 integers [0..999]
Total Tests per RNG   30
Significance (a)      0.01
```

### `$ cat results/scorecard.txt`

| RNG System | Tests Passed | Verdict | Failed Tests |
|:--|:--|:--|:--|
| **BBRES-RNG** | **30 / 30** | ARCHITECTURE ACCEPTED | None |
| Java `SecureRandom` | **30 / 30** | ARCHITECTURE ACCEPTED | None |
| Java `Math.random()` | **28 / 30** | REVIEW NEEDED | Maurer's, Gap |

> **BBRES-RNG matches cryptographic-grade `SecureRandom` with a perfect score, and outperforms `Math.random()` which failed 2 tests.**

<br/>

### `$ cat results/nist_core.log`

NIST SP 800-22 Core Tests — **9/9**

| Test | p-value | Result |
|:--|:--|:--|
| Frequency (Monobit) | 0.094324 | PASS |
| Block Frequency (M=128) | 0.923799 | PASS |
| Runs Test | 0.323306 | PASS |
| Longest Run of Ones | 0.469160 | PASS |
| Cumulative Sums (Forward) | 0.178039 | PASS |
| Cumulative Sums (Reverse) | 0.163651 | PASS |
| Approximate Entropy (m=2) | 0.053664 | PASS |
| Serial (m=2) - d1 | 0.151995 | PASS |
| Serial (m=2) - d2 | 0.324972 | PASS |

### `$ cat results/extended_uniformity.log`

Extended NIST & Uniformity Tests — **6/6**

| Test | p-value | Result |
|:--|:--|:--|
| Maurer's Universal Statistical | 0.981707 | PASS |
| Poker Test (m=4) | 0.479188 | PASS |
| Random Excursion Variant | 0.500000 | PASS |
| Byte-Level Chi-Square | 0.468109 | PASS |
| Nibble-Level Chi-Square | 0.479188 | PASS |
| 2-Bit Pair Distribution | 0.386897 | PASS |

### `$ cat results/spectral_adversarial.log`

Spectral, Adversarial & Cryptographic Tests — **5/5**

| Test | p-value | Result |
|:--|:--|:--|
| DFT (Periodicity) | 0.227460 | PASS |
| Frequency Prediction (w=8) | 0.884165 | PASS |
| ML Attack (LogReg, w=16) | 0.579260 | PASS |
| Pattern Repetition (w=53) | 1.000000 | PASS |
| SHA-256 CSPRNG Wrapper | 1.000000 | PASS |

### `$ cat results/integer_distribution.log`

Integer Distribution & Sequence Tests — **10/10**

| Test | p-value | Result |
|:--|:--|:--|
| Chi-Square Uniformity | 0.475159 | PASS |
| Mean (Z-test) | 0.398873 | PASS |
| Variance (Chi-sq) | 0.842551 | PASS |
| Binned Goodness-of-Fit | 0.756309 | PASS |
| Birthday Spacing | 1.000000 | PASS |
| Coupon Collector | 0.500000 | PASS |
| Runs Up/Down | 0.994013 | PASS |
| Lag-1 Autocorrelation | 0.418097 | PASS |
| Gap Test (KS) | 0.332555 | PASS |
| Permutation (t=5) | 0.703417 | PASS |

<br/>

## `$ cat results/entropy_quality.csv`

| Metric | BBRES-RNG | SecureRandom | Math.random() | Theoretical Max |
|:--|:--|:--|:--|:--|
| Shannon Entropy (8-bit) | **7.999260** | 7.999311 | 7.999233 | 8.000000 |
| Min-Entropy (8-bit) | **7.883082** | 7.828281 | 7.879001 | 8.000000 |
| Transition Rate | **0.500348** | 0.499482 | 0.500284 | 0.500000 |
| Bit Balance (1s : 0s) | 50.06 : 49.94 | 49.97 : 50.03 | 50.02 : 49.98 | 50 : 50 |

> **99.991% of maximum Shannon entropy.** Highest min-entropy (7.883) among all three RNGs — superior worst-case unpredictability.

### `$ cat results/autocorrelation.csv`

| Lag | BBRES-RNG | SecureRandom | Math.random() |
|:--|:--|:--|:--|
| 1 | -0.0007 | +0.0010 | -0.0006 |
| 2 | -0.0009 | -0.0005 | -0.0002 |
| 8 | -0.0000 | -0.0011 | -0.0002 |
| 32 | -0.0010 | +0.0002 | +0.0001 |
| 256 | -0.0003 | -0.0002 | +0.0007 |

Near-zero autocorrelation at all lags — no serial dependence in output streams.

<br/>

## `$ diff math_random_failures bbres_rng_passes`

> Where `Math.random()` failed — and BBRES-RNG didn't.

| Test | Math.random() | BBRES-RNG | Why It Matters |
|:--|:--|:--|:--|
| Maurer's Universal Statistical | FAIL p=0.0083 | PASS p=0.9817 | Detects compressibility — subtle LCG patterns in output |
| Gap Test (KS) | FAIL p=0.0005 | PASS p=0.3326 | Non-uniform gap distribution between repeated values |

<br/>

## `$ tree src/`

```
Bit-Based-Randomized-Entropy-System-Scheduler-Based-RNG/
├── src/
│   ├── Main.java                              # Entry point & usage examples
│   └── bbresRNG/
│       ├── RNG.java                           # Primary API — [min, max] range
│       ├── randomBitGeneratorModifiedRoot.java # Core bit generator — thread orchestration
│       ├── modRandomBitGenRNG.java            # Modified worker thread implementation
│       └── RandomBitGenRNG.java               # Base worker thread — entropy harvesting
├── docs/                                      # Validation data & reports
├── LICENSE                                    # Custom Restrictive License
└── README.md
```

### `$ grep -r "class" src/ --summary`

```
RNG.java                           Public API. Accepts (min, max), concurrency n, technique randTech.
randomBitGeneratorModifiedRoot.java Orchestrates single random bit generation. Two methods: G1 and G2.
modRandomBitGenRNG.java            Worker thread variant — collects timing data in parallel.
RandomBitGenRNG.java               Base worker thread — launched for entropy harvesting.
```

<br/>

## `$ cat INSTALL.md`

### Prerequisites

`JDK 8+` · Terminal or any Java IDE

### Build & Run

```bash
# Clone
git clone https://github.com/singhmehul7783/Bit-Based-Randomized-Entropy-System-Scheduler-Based-RNG.git
cd Bit-Based-Randomized-Entropy-System-Scheduler-Based-RNG

# Compile
javac src/Main.java

# Run
java -cp src Main
```

<br/>

## `$ cat examples/usage.java`

```java
import bbresRNG.RNG;

public class Main {
    public static void main(String[] args) {

        RNG rng = new RNG();

        // Basic: random number between 0 and 10 (default settings)
        System.out.println(rng.bbresRNG());

        // Custom range: random number between 5 and 15
        System.out.println(rng.bbresRNG(5, 15));

        // Custom concurrency: range [1, 100] with n=50 worker threads
        System.out.println(rng.bbresRNG(1, 100, 50));

        // Alternate generation method: randTech=2, n=35, range [0, 10000]
        System.out.println(rng.bbresRNG(0, 10000, 35, 2));
    }
}
```

### `$ man bbresRNG`

| Method | Description |
|:--|:--|
| `bbresRNG()` | Random number in `[0, 10]` with default concurrency and technique |
| `bbresRNG(min, max)` | Random number in `[min, max]` |
| `bbresRNG(min, max, n)` | Random number in `[min, max]` with `n` concurrent worker threads |
| `bbresRNG(min, max, n, randTech)` | Full control — range, concurrency level, and generation technique (`1` or `2`) |

**Parameters:**
- **`n`** (int) — Number of worker threads to spawn. Higher values increase entropy but also increase computation time.
- **`randTech`** (int) — Selects between two bit-generation algorithms: `1` for `generateRandBitG1`, `2` for `generateRandBitG2`.

<br/>

## `$ cat PHILOSOPHY.md`

> Concurrency is inherently non-deterministic.
>
> The OS thread scheduler makes decisions based on CPU load, interrupt timing, memory pressure, and hundreds of other factors that are impossible to predict or reproduce. By deliberately creating race conditions and measuring their outcomes with microsecond precision, BBRES-RNG converts system-level chaos into usable randomness.
>
> Conceptually similar to hardware RNGs that harvest physical noise — except here, the "physical noise" is the operating system itself.

<br/>

## `$ ls docs/`

Full validation reports with charts, visual analysis, and detailed methodology:

```
docs/
├── bbres-rng_combined_report.pdf            # BBRES-RNG      30/30
├── Java-SecureRandom_combined_report.pdf    # SecureRandom   30/30
└── Java-Math.random()_combined_report.pdf   # Math.random()  28/30
```

<br/>

## `$ cat LICENSE --summary`

```
PERMITTED    Academic research, education, personal study — with proper attribution
PROHIBITED   Commercial use, modification, distribution, sublicensing, derivative works
             (without written permission)
CONTACT      github.com/singhmehul7783
```

See [LICENSE](LICENSE) for full terms.

<br/>

## `$ cat CONTRIBUTING.md`

> Source-available for educational purposes. To contribute, open an issue first to discuss proposed changes. All contributions are subject to the project's custom license terms.

<br/>

---

<div align="center">

<br/>

```
Built with Java and a healthy respect for chaos.
Developed by Mehul Singh
Validated against 30 statistical tests — 2M+ bits & 100k integers.
```

<sub>[mehul.engineer](https://mehul.engineer) · [hello@mehul.engineer](mailto:hello@mehul.engineer)</sub>

</div>
