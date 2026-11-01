| Algorithm | Graph | Size | V | E | Avg time (µs) | Std (µs) | Total ops | Operation counts | Result |
|---|---|---|---:|---:|---:|---:|---:|---|---|
| Kruskal | undirected connected | V=1000 | 1,000 | 5,000 | 639.27 | 14.60 | 64,357 | sortComparisons=50585, edgesExamined=3443, findCalls=6886, unionCalls=3443, parentSteps=6391 | weight=11846, connected=true |
| Prim | undirected connected | V=1000 | 1,000 | 5,000 | 1057.74 | 15.44 | 131,020 | heapComparisons=111020, edgesExamined=10000, heapInserts=5000, heapPolls=5000, staleEdges=4001 | weight=11846, connected=true |
| Tarjan SCC | directed random | V=1000 | 1,000 | 5,000 | 142.27 | 0.94 | 6,000 | verticesVisited=1000, edgesExamined=5000 | SCCs=14, largest=987 |
| Tarjan SCC | DAG | V=1000 | 1,000 | 5,000 | 78.89 | 6.34 | 6,000 | verticesVisited=1000, edgesExamined=5000 | SCCs=1000, largest=1 |
| Topological sort | DAG | V=1000 | 1,000 | 5,000 | 58.84 | 11.92 | 11,000 | vertexRemovals=1000, edgesExamined=10000 | DAG, order of 1000 vertices |
| Topological sort | directed random | V=1000 | 1,000 | 5,000 | 10.30 | 0.44 | 5,067 | vertexRemovals=7, edgesExamined=5060 | cycle of length 20 |
| Dijkstra | undirected connected | V=1000 | 1,000 | 5,000 | 496.69 | 15.56 | 49,251 | heapComparisons=35395, edgesExamined=10000, relaxations=1927, heapPushes=1928, heapPolls=1928, stalePolls=928 | reachable=1000/1000 |
| Dijkstra | directed random | V=1000 | 1,000 | 5,000 | 285.77 | 25.95 | 32,097 | heapComparisons=24240, edgesExamined=4967, relaxations=1444, heapPushes=1445, heapPolls=1445, stalePolls=452 | reachable=993/1000 |
| Kruskal | undirected connected | V=2000 | 2,000 | 10,000 | 1333.81 | 22.46 | 132,404 | sortComparisons=103232, edgesExamined=7293, findCalls=14586, unionCalls=7293, parentSteps=13614 | weight=24435, connected=true |
| Prim | undirected connected | V=2000 | 2,000 | 10,000 | 2411.41 | 56.13 | 282,570 | heapComparisons=242570, edgesExamined=20000, heapInserts=10000, heapPolls=10000, staleEdges=8001 | weight=24435, connected=true |
| Tarjan SCC | directed random | V=2000 | 2,000 | 10,000 | 502.83 | 86.53 | 12,000 | verticesVisited=2000, edgesExamined=10000 | SCCs=22, largest=1979 |
| Tarjan SCC | DAG | V=2000 | 2,000 | 10,000 | 234.48 | 46.39 | 12,000 | verticesVisited=2000, edgesExamined=10000 | SCCs=2000, largest=1 |
| Topological sort | DAG | V=2000 | 2,000 | 10,000 | 152.62 | 29.35 | 22,000 | vertexRemovals=2000, edgesExamined=20000 | DAG, order of 2000 vertices |
| Topological sort | directed random | V=2000 | 2,000 | 10,000 | 31.90 | 0.37 | 10,087 | vertexRemovals=10, edgesExamined=10077 | cycle of length 3 |
| Dijkstra | undirected connected | V=2000 | 2,000 | 10,000 | 1202.03 | 10.57 | 106,398 | heapComparisons=78650, edgesExamined=20000, relaxations=3873, heapPushes=3874, heapPolls=3874, stalePolls=1874 | reachable=2000/2000 |
| Dijkstra | directed random | V=2000 | 2,000 | 10,000 | 684.81 | 17.96 | 69,385 | heapComparisons=53686, edgesExamined=9961, relaxations=2868, heapPushes=2869, heapPolls=2869, stalePolls=879 | reachable=1990/2000 |
| Kruskal | undirected connected | V=5000 | 5,000 | 25,000 | 4608.39 | 179.04 | 347,096 | sortComparisons=263160, edgesExamined=20984, findCalls=41968, unionCalls=20984, parentSteps=39544 | weight=62281, connected=true |
| Prim | undirected connected | V=5000 | 5,000 | 25,000 | 7254.61 | 171.37 | 778,620 | heapComparisons=678620, edgesExamined=50000, heapInserts=25000, heapPolls=25000, staleEdges=20001 | weight=62281, connected=true |
| Tarjan SCC | directed random | V=5000 | 5,000 | 25,000 | 1522.32 | 90.10 | 30,000 | verticesVisited=5000, edgesExamined=25000 | SCCs=85, largest=4916 |
| Tarjan SCC | DAG | V=5000 | 5,000 | 25,000 | 727.78 | 14.92 | 30,000 | verticesVisited=5000, edgesExamined=25000 | SCCs=5000, largest=1 |
| Topological sort | DAG | V=5000 | 5,000 | 25,000 | 392.37 | 18.95 | 55,000 | vertexRemovals=5000, edgesExamined=50000 | DAG, order of 5000 vertices |
| Topological sort | directed random | V=5000 | 5,000 | 25,000 | 53.59 | 0.77 | 25,302 | vertexRemovals=37, edgesExamined=25265 | cycle of length 7 |
| Dijkstra | undirected connected | V=5000 | 5,000 | 25,000 | 3605.60 | 235.07 | 292,457 | heapComparisons=223017, edgesExamined=50000, relaxations=9719, heapPushes=9720, heapPolls=9720, stalePolls=4720 | reachable=5000/5000 |
| Dijkstra | directed random | V=5000 | 5,000 | 25,000 | 2497.66 | 356.82 | 190,033 | heapComparisons=150992, edgesExamined=24833, relaxations=7103, heapPushes=7104, heapPolls=7104, stalePolls=2141 | reachable=4963/5000 |
| Kruskal | undirected connected | V=10000 | 10,000 | 50,000 | 8353.32 | 280.84 | 695,155 | sortComparisons=528915, edgesExamined=41560, findCalls=83120, unionCalls=41560, parentSteps=78322 | weight=124402, connected=true |
| Prim | undirected connected | V=10000 | 10,000 | 50,000 | 17926.81 | 425.79 | 1,660,384 | heapComparisons=1460384, edgesExamined=100000, heapInserts=50000, heapPolls=50000, staleEdges=40001 | weight=124402, connected=true |
| Tarjan SCC | directed random | V=10000 | 10,000 | 50,000 | 3313.18 | 68.51 | 60,000 | verticesVisited=10000, edgesExamined=50000 | SCCs=125, largest=9876 |
| Tarjan SCC | DAG | V=10000 | 10,000 | 50,000 | 1681.37 | 24.43 | 60,000 | verticesVisited=10000, edgesExamined=50000 | SCCs=10000, largest=1 |
| Topological sort | DAG | V=10000 | 10,000 | 50,000 | 1052.88 | 24.56 | 110,000 | vertexRemovals=10000, edgesExamined=100000 | DAG, order of 10000 vertices |
| Topological sort | directed random | V=10000 | 10,000 | 50,000 | 142.54 | 15.30 | 50,550 | vertexRemovals=67, edgesExamined=50483 | cycle of length 91 |
| Dijkstra | undirected connected | V=10000 | 10,000 | 50,000 | 8411.99 | 1159.21 | 626,647 | heapComparisons=487609, edgesExamined=100000, relaxations=19518, heapPushes=19519, heapPolls=19519, stalePolls=9519 | reachable=10000/10000 |
| Dijkstra | directed random | V=10000 | 10,000 | 50,000 | 9993.52 | 464.85 | 411,494 | heapComparisons=333214, edgesExamined=49634, relaxations=14322, heapPushes=14323, heapPolls=14323, stalePolls=4390 | reachable=9933/10000 |
| Kruskal | undirected connected | V=20000 | 20,000 | 100,000 | 19211.76 | 663.85 | 1,417,625 | sortComparisons=1059633, edgesExamined=89498, findCalls=178996, unionCalls=89498, parentSteps=169334 | weight=248272, connected=true |
| Prim | undirected connected | V=20000 | 20,000 | 100,000 | 52164.13 | 1562.74 | 3,528,998 | heapComparisons=3128998, edgesExamined=200000, heapInserts=100000, heapPolls=100000, staleEdges=80001 | weight=248272, connected=true |
| Tarjan SCC | directed random | V=20000 | 20,000 | 100,000 | 13054.75 | 831.88 | 120,000 | verticesVisited=20000, edgesExamined=100000 | SCCs=287, largest=19714 |
| Tarjan SCC | DAG | V=20000 | 20,000 | 100,000 | 8878.09 | 420.19 | 120,000 | verticesVisited=20000, edgesExamined=100000 | SCCs=20000, largest=1 |
| Topological sort | DAG | V=20000 | 20,000 | 100,000 | 3278.40 | 332.68 | 220,000 | vertexRemovals=20000, edgesExamined=200000 | DAG, order of 20000 vertices |
| Topological sort | directed random | V=20000 | 20,000 | 100,000 | 313.24 | 10.14 | 101,081 | vertexRemovals=118, edgesExamined=100963 | cycle of length 66 |
| Dijkstra | undirected connected | V=20000 | 20,000 | 100,000 | 32063.05 | 2308.33 | 1,332,352 | heapComparisons=1054172, edgesExamined=200000, relaxations=39089, heapPushes=39090, heapPolls=39090, stalePolls=19090 | reachable=20000/20000 |
| Dijkstra | directed random | V=20000 | 20,000 | 100,000 | 25394.72 | 7102.91 | 877,741 | heapComparisons=721217, edgesExamined=99410, relaxations=28556, heapPushes=28557, heapPolls=28557, stalePolls=8675 | reachable=19882/20000 |
| Kruskal | undirected connected | V=50000 | 50,000 | 250,000 | 61743.73 | 4329.32 | 3,602,768 | sortComparisons=2643024, edgesExamined=239936, findCalls=479872, unionCalls=239936, parentSteps=456193 | weight=626217, connected=true |
| Prim | undirected connected | V=50000 | 50,000 | 250,000 | 229581.92 | 14022.63 | 9,518,894 | heapComparisons=8518894, edgesExamined=500000, heapInserts=250000, heapPolls=250000, staleEdges=200001 | weight=626217, connected=true |
| Tarjan SCC | directed random | V=50000 | 50,000 | 250,000 | 56555.10 | 11120.08 | 300,000 | verticesVisited=50000, edgesExamined=250000 | SCCs=718, largest=49283 |
| Tarjan SCC | DAG | V=50000 | 50,000 | 250,000 | 33109.08 | 1230.13 | 300,000 | verticesVisited=50000, edgesExamined=250000 | SCCs=50000, largest=1 |
| Topological sort | DAG | V=50000 | 50,000 | 250,000 | 22026.49 | 2985.52 | 550,000 | vertexRemovals=50000, edgesExamined=500000 | DAG, order of 50000 vertices |
| Topological sort | directed random | V=50000 | 50,000 | 250,000 | 705.60 | 19.62 | 252,522 | vertexRemovals=356, edgesExamined=252166 | cycle of length 60 |
| Dijkstra | undirected connected | V=50000 | 50,000 | 250,000 | 104044.65 | 6175.09 | 3,586,281 | heapComparisons=2890491, edgesExamined=500000, relaxations=97894, heapPushes=97895, heapPolls=97895, stalePolls=47895 | reachable=50000/50000 |
| Dijkstra | directed random | V=50000 | 50,000 | 250,000 | 74262.77 | 9637.61 | 2,393,413 | heapComparisons=2001590, edgesExamined=248235, relaxations=71793, heapPushes=71794, heapPolls=71794, stalePolls=22150 | reachable=49644/50000 |
| Kruskal | undirected connected | V=100000 | 100,000 | 500,000 | 113381.81 | 1986.17 | 7,232,499 | sortComparisons=5290027, edgesExamined=485618, findCalls=971236, unionCalls=485618, parentSteps=924173 | weight=1254743, connected=true |
| Prim | undirected connected | V=100000 | 100,000 | 500,000 | 535079.50 | 46140.21 | 20,071,450 | heapComparisons=18071450, edgesExamined=1000000, heapInserts=500000, heapPolls=500000, staleEdges=400001 | weight=1254743, connected=true |
| Tarjan SCC | directed random | V=100000 | 100,000 | 500,000 | 119410.81 | 4796.10 | 600,000 | verticesVisited=100000, edgesExamined=500000 | SCCs=1381, largest=98620 |
| Tarjan SCC | DAG | V=100000 | 100,000 | 500,000 | 74744.28 | 7783.16 | 600,000 | verticesVisited=100000, edgesExamined=500000 | SCCs=100000, largest=1 |
| Topological sort | DAG | V=100000 | 100,000 | 500,000 | 53081.33 | 1560.21 | 1,100,000 | vertexRemovals=100000, edgesExamined=1000000 | DAG, order of 100000 vertices |
| Topological sort | directed random | V=100000 | 100,000 | 500,000 | 2921.49 | 62.24 | 504,386 | vertexRemovals=687, edgesExamined=503699 | cycle of length 3 |
| Dijkstra | undirected connected | V=100000 | 100,000 | 500,000 | 254952.51 | 50744.85 | 7,551,720 | heapComparisons=6160586, edgesExamined=1000000, relaxations=195566, heapPushes=195567, heapPolls=195567, stalePolls=95567 | reachable=100000/100000 |
| Dijkstra | directed random | V=100000 | 100,000 | 500,000 | 152774.92 | 19810.77 | 5,057,181 | heapComparisons=4274459, edgesExamined=496568, relaxations=143076, heapPushes=143077, heapPolls=143077, stalePolls=43764 | reachable=99313/100000 |
