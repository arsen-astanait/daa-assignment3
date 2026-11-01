| Algorithm | Graph | Size | V | E | Avg time (µs) | Std (µs) | Total ops | Operation counts | Result |
|---|---|---|---:|---:|---:|---:|---:|---|---|
| Kruskal | undirected connected | E=10000 | 10,000 | 10,000 | 1859.97 | 99.67 | 143,086 | sortComparisons=103086, edgesExamined=10000, findCalls=20000, unionCalls=10000, parentSteps=12618 | weight=504558, connected=true |
| Prim | undirected connected | E=10000 | 10,000 | 10,000 | 2006.28 | 51.51 | 236,507 | heapComparisons=196507, edgesExamined=20000, heapInserts=10000, heapPolls=10000, staleEdges=1 | weight=504558, connected=true |
| Tarjan SCC | directed random | E=10000 | 10,000 | 10,000 | 1063.45 | 166.03 | 20,000 | verticesVisited=10000, edgesExamined=10000 | SCCs=9912, largest=43 |
| Tarjan SCC | DAG | E=10000 | 10,000 | 10,000 | 851.66 | 58.53 | 20,000 | verticesVisited=10000, edgesExamined=10000 | SCCs=10000, largest=1 |
| Topological sort | DAG | E=10000 | 10,000 | 10,000 | 486.59 | 45.39 | 30,000 | vertexRemovals=10000, edgesExamined=20000 | DAG, order of 10000 vertices |
| Topological sort | directed random | E=10000 | 10,000 | 10,000 | 462.99 | 6.77 | 28,094 | vertexRemovals=8809, edgesExamined=19285 | cycle of length 12 |
| Dijkstra | undirected connected | E=10000 | 10,000 | 10,000 | 2429.71 | 139.77 | 217,683 | heapComparisons=177681, edgesExamined=20000, relaxations=10000, heapPushes=10001, heapPolls=10001, stalePolls=1 | reachable=10000/10000 |
| Dijkstra | directed random | E=10000 | 10,000 | 10,000 | 42.84 | 6.66 | 15 | heapComparisons=1, edgesExamined=4, relaxations=4, heapPushes=5, heapPolls=5, stalePolls=0 | reachable=5/10000 |
| Kruskal | undirected connected | E=20000 | 10,000 | 20,000 | 3982.89 | 797.05 | 288,681 | sortComparisons=209101, edgesExamined=19895, findCalls=39790, unionCalls=19895, parentSteps=34988 | weight=289093, connected=true |
| Prim | undirected connected | E=20000 | 10,000 | 20,000 | 11586.22 | 1382.90 | 619,315 | heapComparisons=539315, edgesExamined=40000, heapInserts=20000, heapPolls=20000, staleEdges=10001 | weight=289093, connected=true |
| Tarjan SCC | directed random | E=20000 | 10,000 | 20,000 | 2722.58 | 874.01 | 30,000 | verticesVisited=10000, edgesExamined=20000 | SCCs=3554, largest=6447 |
| Tarjan SCC | DAG | E=20000 | 10,000 | 20,000 | 1155.72 | 38.82 | 30,000 | verticesVisited=10000, edgesExamined=20000 | SCCs=10000, largest=1 |
| Topological sort | DAG | E=20000 | 10,000 | 20,000 | 702.97 | 49.11 | 50,000 | vertexRemovals=10000, edgesExamined=40000 | DAG, order of 10000 vertices |
| Topological sort | directed random | E=20000 | 10,000 | 20,000 | 145.03 | 1.96 | 25,915 | vertexRemovals=1993, edgesExamined=23922 | cycle of length 2 |
| Dijkstra | undirected connected | E=20000 | 10,000 | 20,000 | 5015.98 | 343.88 | 361,629 | heapComparisons=295473, edgesExamined=40000, relaxations=13077, heapPushes=13078, heapPolls=13078, stalePolls=3078 | reachable=10000/10000 |
| Dijkstra | directed random | E=20000 | 10,000 | 20,000 | 47.05 | 2.86 | 2 | heapComparisons=0, edgesExamined=0, relaxations=0, heapPushes=1, heapPolls=1, stalePolls=0 | reachable=1/10000 |
| Kruskal | undirected connected | E=50000 | 10,000 | 50,000 | 8235.54 | 138.68 | 695,155 | sortComparisons=528915, edgesExamined=41560, findCalls=83120, unionCalls=41560, parentSteps=78322 | weight=124402, connected=true |
| Prim | undirected connected | E=50000 | 10,000 | 50,000 | 19204.76 | 507.41 | 1,660,384 | heapComparisons=1460384, edgesExamined=100000, heapInserts=50000, heapPolls=50000, staleEdges=40001 | weight=124402, connected=true |
| Tarjan SCC | directed random | E=50000 | 10,000 | 50,000 | 4108.54 | 823.39 | 60,000 | verticesVisited=10000, edgesExamined=50000 | SCCs=125, largest=9876 |
| Tarjan SCC | DAG | E=50000 | 10,000 | 50,000 | 1788.00 | 102.64 | 60,000 | verticesVisited=10000, edgesExamined=50000 | SCCs=10000, largest=1 |
| Topological sort | DAG | E=50000 | 10,000 | 50,000 | 1058.00 | 34.77 | 110,000 | vertexRemovals=10000, edgesExamined=100000 | DAG, order of 10000 vertices |
| Topological sort | directed random | E=50000 | 10,000 | 50,000 | 136.85 | 8.79 | 50,550 | vertexRemovals=67, edgesExamined=50483 | cycle of length 91 |
| Dijkstra | undirected connected | E=50000 | 10,000 | 50,000 | 9008.01 | 735.15 | 626,647 | heapComparisons=487609, edgesExamined=100000, relaxations=19518, heapPushes=19519, heapPolls=19519, stalePolls=9519 | reachable=10000/10000 |
| Dijkstra | directed random | E=50000 | 10,000 | 50,000 | 6161.87 | 458.46 | 411,494 | heapComparisons=333214, edgesExamined=49634, relaxations=14322, heapPushes=14323, heapPolls=14323, stalePolls=4390 | reachable=9933/10000 |
| Kruskal | undirected connected | E=100000 | 10,000 | 100,000 | 15917.10 | 331.05 | 1,287,027 | sortComparisons=1060419, edgesExamined=56652, findCalls=113304, unionCalls=56652, parentSteps=108624 | weight=65078, connected=true |
| Prim | undirected connected | E=100000 | 10,000 | 100,000 | 46485.19 | 2881.21 | 3,488,214 | heapComparisons=3088214, edgesExamined=200000, heapInserts=100000, heapPolls=100000, staleEdges=90001 | weight=65078, connected=true |
| Tarjan SCC | directed random | E=100000 | 10,000 | 100,000 | 7136.33 | 2572.90 | 110,000 | verticesVisited=10000, edgesExamined=100000 | SCCs=2, largest=9999 |
| Tarjan SCC | DAG | E=100000 | 10,000 | 100,000 | 2484.16 | 66.70 | 110,000 | verticesVisited=10000, edgesExamined=100000 | SCCs=10000, largest=1 |
| Topological sort | DAG | E=100000 | 10,000 | 100,000 | 1644.41 | 35.36 | 210,000 | vertexRemovals=10000, edgesExamined=200000 | DAG, order of 10000 vertices |
| Topological sort | directed random | E=100000 | 10,000 | 100,000 | 199.18 | 7.28 | 100,097 | vertexRemovals=1, edgesExamined=100096 | cycle of length 62 |
| Dijkstra | undirected connected | E=100000 | 10,000 | 100,000 | 21269.18 | 3403.98 | 911,482 | heapComparisons=660796, edgesExamined=200000, relaxations=25342, heapPushes=25343, heapPolls=25343, stalePolls=15343 | reachable=10000/10000 |
| Dijkstra | directed random | E=100000 | 10,000 | 100,000 | 9613.50 | 783.40 | 625,997 | heapComparisons=486993, edgesExamined=99994, relaxations=19504, heapPushes=19505, heapPolls=19505, stalePolls=9506 | reachable=9999/10000 |
| Kruskal | undirected connected | E=200000 | 10,000 | 200,000 | 36045.98 | 1047.38 | 2,310,329 | sortComparisons=2124697, edgesExamined=46408, findCalls=92816, unionCalls=46408, parentSteps=88034 | weight=34819, connected=true |
| Prim | undirected connected | E=200000 | 10,000 | 200,000 | 181068.44 | 44815.64 | 7,325,254 | heapComparisons=6525254, edgesExamined=400000, heapInserts=200000, heapPolls=200000, staleEdges=190001 | weight=34819, connected=true |
| Tarjan SCC | directed random | E=200000 | 10,000 | 200,000 | 10425.66 | 518.62 | 210,000 | verticesVisited=10000, edgesExamined=200000 | SCCs=1, largest=10000 |
| Tarjan SCC | DAG | E=200000 | 10,000 | 200,000 | 9622.03 | 1248.81 | 210,000 | verticesVisited=10000, edgesExamined=200000 | SCCs=10000, largest=1 |
| Topological sort | DAG | E=200000 | 10,000 | 200,000 | 5094.90 | 629.32 | 410,000 | vertexRemovals=10000, edgesExamined=400000 | DAG, order of 10000 vertices |
| Topological sort | directed random | E=200000 | 10,000 | 200,000 | 360.75 | 10.69 | 200,205 | vertexRemovals=0, edgesExamined=200205 | cycle of length 49 |
| Dijkstra | undirected connected | E=200000 | 10,000 | 200,000 | 37408.13 | 6606.46 | 1,310,176 | heapComparisons=847310, edgesExamined=400000, relaxations=31432, heapPushes=31433, heapPolls=31433, stalePolls=21433 | reachable=10000/10000 |
| Dijkstra | directed random | E=200000 | 10,000 | 200,000 | 18093.87 | 1668.12 | 913,082 | heapComparisons=662352, edgesExamined=200000, relaxations=25364, heapPushes=25365, heapPolls=25365, stalePolls=15365 | reachable=10000/10000 |
| Kruskal | undirected connected | E=500000 | 10,000 | 500,000 | 86861.17 | 4733.91 | 5,483,942 | sortComparisons=5291650, edgesExamined=48073, findCalls=96146, unionCalls=48073, parentSteps=91209 | weight=17515, connected=true |
| Prim | undirected connected | E=500000 | 10,000 | 500,000 | 587803.17 | 26149.21 | 19,510,888 | heapComparisons=17510888, edgesExamined=1000000, heapInserts=500000, heapPolls=500000, staleEdges=490001 | weight=17515, connected=true |
| Tarjan SCC | directed random | E=500000 | 10,000 | 500,000 | 40403.82 | 6845.95 | 510,000 | verticesVisited=10000, edgesExamined=500000 | SCCs=1, largest=10000 |
| Tarjan SCC | DAG | E=500000 | 10,000 | 500,000 | 25205.98 | 803.98 | 510,000 | verticesVisited=10000, edgesExamined=500000 | SCCs=10000, largest=1 |
| Topological sort | DAG | E=500000 | 10,000 | 500,000 | 24695.49 | 5413.11 | 1,010,000 | vertexRemovals=10000, edgesExamined=1000000 | DAG, order of 10000 vertices |
| Topological sort | directed random | E=500000 | 10,000 | 500,000 | 1389.27 | 86.51 | 500,160 | vertexRemovals=0, edgesExamined=500160 | cycle of length 136 |
| Dijkstra | undirected connected | E=500000 | 10,000 | 500,000 | 78892.18 | 13618.33 | 2,115,602 | heapComparisons=1039826, edgesExamined=1000000, relaxations=37887, heapPushes=37888, heapPolls=37888, stalePolls=27888 | reachable=10000/10000 |
| Dijkstra | directed random | E=500000 | 10,000 | 500,000 | 40782.39 | 2967.60 | 1,457,221 | heapComparisons=891417, edgesExamined=500000, relaxations=32901, heapPushes=32902, heapPolls=32902, stalePolls=22902 | reachable=10000/10000 |
