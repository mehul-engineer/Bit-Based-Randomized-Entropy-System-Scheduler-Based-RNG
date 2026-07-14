<div align="center">

# BBRES-RNG

**Bit-Based Randomized Entropy System — Scheduler-Based RNG**

*An experimental Java random-number generator that harvests real-time entropy from OS thread-scheduling chaos.*

[![Java](https://img.shields.io/badge/Language-Java-orange?style=for-the-badge&logo=openjdk&logoColor=white)](#requirements)
[![JDK 8+](https://img.shields.io/badge/JDK-8%2B-green?style=for-the-badge)](#requirements)
[![Reference](https://img.shields.io/badge/Research%20Reference-SYS--ENG%2F001-blue?style=for-the-badge)](https://qquantt.com/article.html?id=sys-eng%2F001)

</div>

## Overview

BBRES-RNG is a multithreaded random number generator that deliberately creates controlled concurrency and treats the unpredictable timing behavior of the OS thread scheduler as its entropy source. Every number it produces is shaped by the live, non-deterministic state of the host machine, making each output practically unrepeatable. 

This departs from traditional PRNG designs in several ways: rather than relying on deterministic formulas, fixed mathematical seeds, and single-threaded execution, BBRES-RNG is entropy-based, inherently non-deterministic, and built on a multi-threaded concurrency architecture.

This project is intended for research, experimentation, and educational study. Statistical test results can help assess observed output quality for a given sample; they do not, by themselves, establish cryptographic security, entropy strength, or suitability for security-sensitive use.

## Architecture

The generation pipeline proceeds through three stages:

1. **Stage 1 - Thread Spawning & Controlled Race Conditions:** Multiple worker threads (`randomBitGenRNG`) are launched simultaneously. The OS scheduler decides their execution order, which varies unpredictably by microseconds depending on CPU state, system load, and kernel-level scheduling.
2. **Stage 2 - Timing-Based Entropy Collection:** Each worker thread races to record its ID into a shared flag array. The arrival order becomes the raw entropy signal, while a separate `AtomicIntegerArray` coordinates readiness across thread groups without needing a global lock.
3. **Stage 3 - Bitwise Mixing & Aggregation:** Arrival-order values from the first and last ~20% of finishers are combined using XOR operations. These are passed through a small xorshift-style mixing step to produce one random bit per cycle. These bits are then assembled by `RNG.java` into arbitrary-range integers.

The orchestration layer (`randomBitGeneratorModifiedRoot.java`) segments requested concurrency into blocks of up to 32 threads, capped at a maximum of 64 segments. It exposes two generation techniques:
* **G1 (Default):** Segments threads in their natural ID order.
* **G2:** Performs a Fisher-Yates-style shuffle of thread IDs (driven by a nested BBRES-RNG v1 call) before segmenting, adding an extra layer of unpredictability to the scheduling races.

## Project Structure

```text
src/
├── Main.java (entry point and usage examples)
└── bbresRNG/
    ├── RNG.java (public API)
    ├── randomBitGeneratorModifiedRoot.java (core bit generator and thread orchestration, G1/G2)
    ├── modRandomBitGenRNG.java (worker-thread variant collecting timing data in parallel)
    └── randomBitGenRNG.java (base worker thread launched for entropy harvesting)
docs/ (contains architecture documentation and full validation reports)
LICENSE
README.md