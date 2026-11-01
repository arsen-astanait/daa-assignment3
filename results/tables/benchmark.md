| Algorithm | Graph | Size | V | E | Avg time (µs) | Std (µs) | Total ops | Operation counts | Result |
|---|---|---|---:|---:|---:|---:|---:|---|---|
| Kruskal | undirected connected | Small | 10 | 30 | 6.10 | 0.82 | 147 | sortComparisons=111, edgesExamined=9, findCalls=18, unionCalls=9, parentSteps=2 | weight=82, connected=true |
| Prim | undirected connected | Small | 10 | 30 | 7.05 | 0.19 | 322 | heapComparisons=202, edgesExamined=60, heapInserts=30, heapPolls=30, staleEdges=21 | weight=82, connected=true |
| Tarjan SCC | directed random | Small | 10 | 30 | 2.50 | 0.11 | 40 | verticesVisited=10, edgesExamined=30 | SCCs=1, largest=10 |
| Tarjan SCC | DAG | Small | 10 | 30 | 4.03 | 1.09 | 40 | verticesVisited=10, edgesExamined=30 | SCCs=10, largest=1 |
| Topological sort | DAG | Small | 10 | 30 | 0.41 | 0.03 | 70 | vertexRemovals=10, edgesExamined=60 | DAG, order of 10 vertices |
| Topological sort | directed random | Small | 10 | 30 | 0.41 | 0.04 | 37 | vertexRemovals=0, edgesExamined=37 | cycle of length 7 |
| Dijkstra | undirected connected | Small | 10 | 30 | 8.17 | 4.47 | 170 | heapComparisons=76, edgesExamined=60, relaxations=16, heapPushes=17, heapPolls=17, stalePolls=7 | reachable=10/10 |
| Dijkstra | directed random | Small | 10 | 30 | 0.92 | 0.03 | 98 | heapComparisons=42, edgesExamined=30, relaxations=12, heapPushes=13, heapPolls=13, stalePolls=3 | reachable=10/10 |
| Kruskal | undirected connected | Medium | 100 | 500 | 29.33 | 4.05 | 4,704 | sortComparisons=3836, edgesExamined=217, findCalls=434, unionCalls=217, parentSteps=351 | weight=1189, connected=true |
| Prim | undirected connected | Medium | 100 | 500 | 38.12 | 0.33 | 9,678 | heapComparisons=7678, edgesExamined=1000, heapInserts=500, heapPolls=500, staleEdges=401 | weight=1189, connected=true |
| Tarjan SCC | directed random | Medium | 100 | 500 | 8.49 | 0.16 | 600 | verticesVisited=100, edgesExamined=500 | SCCs=2, largest=99 |
| Tarjan SCC | DAG | Medium | 100 | 500 | 10.42 | 0.57 | 600 | verticesVisited=100, edgesExamined=500 | SCCs=100, largest=1 |
| Topological sort | DAG | Medium | 100 | 500 | 3.63 | 0.13 | 1,100 | vertexRemovals=100, edgesExamined=1000 | DAG, order of 100 vertices |
| Topological sort | directed random | Medium | 100 | 500 | 1.26 | 0.06 | 508 | vertexRemovals=0, edgesExamined=508 | cycle of length 8 |
| Dijkstra | undirected connected | Medium | 100 | 500 | 18.69 | 3.08 | 3,791 | heapComparisons=2385, edgesExamined=1000, relaxations=202, heapPushes=203, heapPolls=203, stalePolls=103 | reachable=100/100 |
| Dijkstra | directed random | Medium | 100 | 500 | 12.37 | 3.18 | 2,140 | heapComparisons=1362, edgesExamined=500, relaxations=138, heapPushes=139, heapPolls=139, stalePolls=39 | reachable=100/100 |
| Kruskal | undirected connected | Large | 1,000 | 5,000 | 623.97 | 7.30 | 64,357 | sortComparisons=50585, edgesExamined=3443, findCalls=6886, unionCalls=3443, parentSteps=6391 | weight=11846, connected=true |
| Prim | undirected connected | Large | 1,000 | 5,000 | 1125.24 | 58.82 | 131,020 | heapComparisons=111020, edgesExamined=10000, heapInserts=5000, heapPolls=5000, staleEdges=4001 | weight=11846, connected=true |
| Tarjan SCC | directed random | Large | 1,000 | 5,000 | 141.99 | 1.26 | 6,000 | verticesVisited=1000, edgesExamined=5000 | SCCs=14, largest=987 |
| Tarjan SCC | DAG | Large | 1,000 | 5,000 | 71.79 | 1.40 | 6,000 | verticesVisited=1000, edgesExamined=5000 | SCCs=1000, largest=1 |
| Topological sort | DAG | Large | 1,000 | 5,000 | 55.92 | 15.71 | 11,000 | vertexRemovals=1000, edgesExamined=10000 | DAG, order of 1000 vertices |
| Topological sort | directed random | Large | 1,000 | 5,000 | 10.53 | 0.30 | 5,067 | vertexRemovals=7, edgesExamined=5060 | cycle of length 20 |
| Dijkstra | undirected connected | Large | 1,000 | 5,000 | 473.61 | 5.04 | 49,251 | heapComparisons=35395, edgesExamined=10000, relaxations=1927, heapPushes=1928, heapPolls=1928, stalePolls=928 | reachable=1000/1000 |
| Dijkstra | directed random | Large | 1,000 | 5,000 | 288.91 | 27.25 | 32,097 | heapComparisons=24240, edgesExamined=4967, relaxations=1444, heapPushes=1445, heapPolls=1445, stalePolls=452 | reachable=993/1000 |
