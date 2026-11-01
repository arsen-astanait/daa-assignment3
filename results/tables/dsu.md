| Workload | n | Optimisations | find calls | parent steps | steps per find | Avg time (ms) |
|---|---:|---|---:|---:|---:|---:|
| chain unions + n finds | 1,000 | none | 2,998 | 499,500 | 166.61 | 1.4534 |
| chain unions + n finds | 1,000 | path compression | 2,998 | 1,997 | 0.67 | 0.0182 |
| chain unions + n finds | 1,000 | union by rank | 2,998 | 1,997 | 0.67 | 0.0083 |
| chain unions + n finds | 1,000 | both | 2,998 | 1,997 | 0.67 | 0.0131 |
| chain unions + n finds | 5,000 | none | 14,998 | 12,497,500 | 833.28 | 28.7613 |
| chain unions + n finds | 5,000 | path compression | 14,998 | 9,997 | 0.67 | 0.0515 |
| chain unions + n finds | 5,000 | union by rank | 14,998 | 9,997 | 0.67 | 0.0407 |
| chain unions + n finds | 5,000 | both | 14,998 | 9,997 | 0.67 | 0.0436 |
| chain unions + n finds | 20,000 | none | 59,998 | 199,990,000 | 3333.28 | 465.3163 |
| chain unions + n finds | 20,000 | path compression | 59,998 | 39,997 | 0.67 | 0.2162 |
| chain unions + n finds | 20,000 | union by rank | 59,998 | 39,997 | 0.67 | 0.1526 |
| chain unions + n finds | 20,000 | both | 59,998 | 39,997 | 0.67 | 0.1563 |
| random unions + finds | 10,000 | none | 30,000 | 3,400,942 | 113.36 | 9.3263 |
| random unions + finds | 10,000 | path compression | 30,000 | 43,459 | 1.45 | 0.7704 |
| random unions + finds | 10,000 | union by rank | 30,000 | 27,437 | 0.91 | 0.5555 |
| random unions + finds | 10,000 | both | 30,000 | 19,304 | 0.64 | 0.5252 |
| random unions + finds | 50,000 | none | 150,000 | 82,915,046 | 552.77 | 430.2282 |
| random unions + finds | 50,000 | path compression | 150,000 | 245,809 | 1.64 | 3.9817 |
| random unions + finds | 50,000 | union by rank | 150,000 | 139,753 | 0.93 | 4.2413 |
| random unions + finds | 50,000 | both | 150,000 | 96,752 | 0.65 | 3.0248 |
| random unions + finds | 100,000 | none | 300,000 | 332,163,830 | 1107.21 | 2308.8321 |
| random unions + finds | 100,000 | path compression | 300,000 | 513,146 | 1.71 | 8.9373 |
| random unions + finds | 100,000 | union by rank | 300,000 | 284,254 | 0.95 | 10.6489 |
| random unions + finds | 100,000 | both | 300,000 | 193,937 | 0.65 | 7.7235 |
| Kruskal (V=1000, E=5000) | 1,000 | none | 7,062 | 611,842 | 86.64 | 2.3335 |
| Kruskal (V=1000, E=5000) | 1,000 | path compression | 7,062 | 13,001 | 1.84 | 0.6983 |
| Kruskal (V=1000, E=5000) | 1,000 | union by rank | 7,062 | 11,682 | 1.65 | 0.7730 |
| Kruskal (V=1000, E=5000) | 1,000 | both | 7,062 | 6,508 | 0.92 | 0.6740 |
| Kruskal (V=10000, E=50000) | 10,000 | none | 97,984 | 87,465,669 | 892.65 | 249.8588 |
| Kruskal (V=10000, E=50000) | 10,000 | path compression | 97,984 | 204,145 | 2.08 | 16.9909 |
| Kruskal (V=10000, E=50000) | 10,000 | union by rank | 97,984 | 180,568 | 1.84 | 16.2558 |
| Kruskal (V=10000, E=50000) | 10,000 | both | 97,984 | 93,357 | 0.95 | 12.7401 |
