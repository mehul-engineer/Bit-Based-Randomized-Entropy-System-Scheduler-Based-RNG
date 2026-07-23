<div align="center">

# BBRES-RNG v1.2.0

**Bit-Based Randomized Entropy System - Scheduler-Based RNG**

*A Java random-number generator that harvests real-time entropy from OS thread-scheduling chaos.*

[![Java](https://img.shields.io/badge/Language-Java-orange?style=flat-square&logo=openjdk&logoColor=white)](#requirements)
[![JDK 8+](https://img.shields.io/badge/JDK-8%2B-green?style=flat-square)](#requirements)
[![Validation](https://img.shields.io/badge/Validation-54%2F54%20PASS-brightgreen?style=flat-square)](#validation)
[![Previous](https://img.shields.io/badge/Predecessor-v1-lightgrey?style=flat-square)](https://github.com/mehul-engineer/Bit-Based-Randomized-Entropy-System-Scheduler-Based-RNG)

</div>

---

## Overview

BBRES-RNG treats the unpredictable timing of the OS thread scheduler as an entropy source. Worker threads race to record their arrival order; that order - inherently non-deterministic - is mixed into random bits and assembled into integers.

Threads are grouped into segments of up to 32, each segment independently produces one bit via XOR mixing of the earliest and latest finishers, and segment results are combined. This is `v1.2.0`, a correctness-focused rework of the [original v1 architecture](https://github.com/mehul-engineer/Bit-Based-Randomized-Entropy-System-Scheduler-Based-RNG).

This project is for research, experimentation, and educational study. Passing statistical tests indicates good observed output quality - it does not by itself certify cryptographic security.

## What changed since v1

v1.2.0 is not a rename - it fixes real concurrency bugs in how entropy was isolated between thread segments.

| Area | v1 | v1.2.0 |
|---|---|---|
| Readiness signaling | `getBit(id)` scanned the **entire global** flag array - threads in one segment could write into another segment's slots | `getBit(id, parId)` is scoped to the caller's own `localStart..localEnd` range |
| Bit mixing (first/last 20%) | Computed from **global** `n` and global array indices, misaligned with a segment's actual local slice | Computed from the segment's **local** size and local indices only |
| Flag array sizing | Sized once from `RNG.n` at class-load - could throw `ArrayIndexOutOfBoundsException` if a later call used a larger `n` | Fixed-size buffer covering the full supported `n` range (≤1000) |
| Shuffle helper (G2 mode) | Separate, simpler, less-scrutinized implementation | Rebuilt on the same corrected segment-isolated architecture |
| Max segments | Capped at 64 (unreachable given `n ≤ 1000`) | Capped at 32, matching what's actually reachable |
| Naming | `randomBitGenRNG`, `modRandomBitGenRNG`, `randomBitGeneratorModifiedRoot` | `workerProcess`, `level2ProcessInitializer`, `level1ProcessInitializer` |

The net effect: segments no longer read entropy bits that were never written by their own threads, which was the mechanism most likely to quietly degrade output quality as `n` grew past a single 32-thread segment.

## Validation

Independently tested against **54 statistical tests** - NIST SP 800-22 (core + extended: Binary Matrix Rank, Template Matching, Random Excursions, Linear Complexity), uniformity, spectral/FFT, entropy, adversarial ML (LogReg, GBT, MLP), cryptographic wrapper, and integer-level distribution/sequence tests.

| Metric | Result |
|---|---|
| Tests passed | **54 / 54** |
| Sample size | 31,377,615 bits · 100,000 integers |
| Shannon entropy (8-bit) | 7.999952 / 8.0 |
| SP 800-90B min-entropy | 0.4573 bits/bit *(bound by LRS estimator - same bound seen on a `numpy.random` control, i.e. expected estimator behavior, not a defect)* |
| ML next-bit prediction accuracy | ~50% across LogReg / GBT / MLP |

Full report: [`docs/Reports`](docs/Reports).

> **Status:** pre-release. Sampling for this report is complete; supporting documentation is still being finalized.

## Architecture

```
Stage 1 - Thread Spawning:      up to 32 worker threads race per segment, up to 32 segments
Stage 2 - Entropy Collection:   arrival order recorded into a segment-local flag array
Stage 3 - Bitwise Mixing:       first/last ~20% of local finishers XORed, then xorshift-mixed → 1 bit
```

Two generation techniques, selectable via `randTech`:
* **G1 (default)** - segments threads in natural ID order
* **G2** - Fisher–Yates shuffle of thread IDs before segmenting, for an extra layer of unpredictability

## Usage

```java
RNG rng = new RNG();

rng.bbresRNG();                    // default range [0, 10]
rng.bbresRNG(100L);                // [0, 100]
rng.bbresRNG(50L, 150L);           // [50, 150]
rng.bbresRNG(0L, 999L, 71);        // custom sample size (n)
rng.bbresRNG(0L, 999L, 71, 2);     // G2 technique
```

## Project Structure

```text
src/
├── Main.java
├── bbresRNG/
│   ├── RNG.java                     - public API
│   ├── level1ProcessInitializer.java - segmentation & orchestration (G1/G2)
│   ├── level2ProcessInitializer.java - per-segment bit mixing
│   └── workerProcess.java            - entropy-harvesting worker thread
└── helperForFisherYatesBasedOnBBRESrNG/
    └── ...                          - shuffle RNG for G2, same corrected architecture
docs/    - architecture notes & validation reports
LICENSE
README.md
```

## Requirements

Java 8+. No external dependencies.

## Related

* [BBRES-RNG (v1, original)](https://github.com/mehul-engineer/Bit-Based-Randomized-Entropy-System-Scheduler-Based-RNG) - prior architecture and validation report
