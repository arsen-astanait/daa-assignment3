# makes plots from results/tables/*.csv
# run: python3 scripts/plot_results.py
import csv
import os

import matplotlib

matplotlib.use("Agg")
import matplotlib.pyplot as plt

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
TABLES = os.path.join(ROOT, "results", "tables")
PLOTS = os.path.join(ROOT, "results", "plots")

SURFACE = "#fcfcfb"
TEXT = "#29261b"
MUTED = "#6b6a5e"
GRID = "#e6e5df"

# (algorithm, graph type), label, color, marker
SERIES = [
    (("Kruskal", "undirected connected"), "Kruskal", "#2a78d6", "o"),
    (("Prim", "undirected connected"), "Prim", "#eb6834", "s"),
    (("Dijkstra", "undirected connected"), "Dijkstra", "#1baf7a", "^"),
    (("Tarjan SCC", "directed random"), "Tarjan SCC", "#eda100", "D"),
    (("Topological sort", "DAG"), "Topological sort (DAG)", "#e87ba4", "v"),
]


def read(name):
    with open(os.path.join(TABLES, name), newline="", encoding="utf-8") as f:
        return list(csv.DictReader(f))


def style(ax, title, xlabel, ylabel):
    ax.set_facecolor(SURFACE)
    ax.set_title(title, loc="left", color=TEXT, fontsize=12, fontweight="bold", pad=12)
    ax.set_xlabel(xlabel, color=MUTED)
    ax.set_ylabel(ylabel, color=MUTED)
    ax.grid(True, which="major", color=GRID, linewidth=0.8)
    ax.tick_params(colors=MUTED, labelsize=9)
    for side in ("top", "right"):
        ax.spines[side].set_visible(False)
    for side in ("left", "bottom"):
        ax.spines[side].set_color(GRID)


def new_figure():
    fig, ax = plt.subplots(figsize=(8, 5), dpi=130)
    fig.patch.set_facecolor(SURFACE)
    return fig, ax


def line_plot(rows, xkey, title, xlabel, filename, value="avg_ms", ylabel="Average time (ms, log scale)"):
    fig, ax = new_figure()
    for (algo, gtype), label, color, marker in SERIES:
        pts = sorted((int(r[xkey]), float(r[value])) for r in rows
                     if r["algorithm"] == algo and r["graph_type"] == gtype)
        if not pts:
            continue
        xs, ys = zip(*pts)
        ax.plot(xs, ys, color=color, linewidth=2, marker=marker, markersize=7,
                markeredgecolor=SURFACE, markeredgewidth=1.5, label=label)
    ax.set_xscale("log")
    ax.set_yscale("log")
    style(ax, title, xlabel, ylabel)
    ax.legend(frameon=False, fontsize=9, labelcolor=TEXT, loc="upper left")
    ax.margins(x=0.12)
    fig.tight_layout()
    fig.savefig(os.path.join(PLOTS, filename), facecolor=SURFACE)
    plt.close(fig)


def kruskal_vs_prim(rows):
    fig, axes = plt.subplots(1, 2, figsize=(10, 4.4), dpi=130)
    fig.patch.set_facecolor(SURFACE)
    for ax, key, title, ylabel in (
            (axes[0], "avg_ms", "Kruskal vs Prim: time", "Average time (ms, log)"),
            (axes[1], "total_ops", "Kruskal vs Prim: operations", "Counted operations (log)")):
        for (algo, gtype), label, color, marker in SERIES[:2]:
            pts = sorted((int(r["E"]), float(r[key])) for r in rows
                         if r["algorithm"] == algo and r["graph_type"] == gtype)
            xs, ys = zip(*pts)
            ax.plot(xs, ys, color=color, linewidth=2, marker=marker, markersize=7,
                    markeredgecolor=SURFACE, markeredgewidth=1.5, label=label)
        ax.set_xscale("log")
        ax.set_yscale("log")
        style(ax, title, "Edges E (V = E / 5, log)", ylabel)
        ax.legend(frameon=False, fontsize=9, labelcolor=TEXT)
    fig.tight_layout()
    fig.savefig(os.path.join(PLOTS, "kruskal_vs_prim.png"), facecolor=SURFACE)
    plt.close(fig)


def dsu_plot(rows):
    variants = [("none", "#2a78d6", "o"), ("path compression", "#eb6834", "s"),
                ("union by rank", "#1baf7a", "^"), ("both", "#eda100", "D")]
    fig, ax = new_figure()
    for variant, color, marker in variants:
        pts = sorted((int(r["n"]), int(r["parent_steps"]) / int(r["find_calls"])) for r in rows
                     if r["workload"] == "random unions + finds" and r["variant"] == variant)
        xs, ys = zip(*pts)
        ax.plot(xs, ys, color=color, linewidth=2, marker=marker, markersize=7,
                markeredgecolor=SURFACE, markeredgewidth=1.5, label=variant)
        ax.annotate(f"{ys[-1]:.2f}", (xs[-1], ys[-1]), xytext=(6, 0), textcoords="offset points",
                    va="center", fontsize=8, color=TEXT)
    ax.set_xscale("log")
    ax.set_yscale("log")
    style(ax, "DSU: parent pointers followed per find (random workload)",
          "Elements n (log)", "Average steps per find (log)")
    ax.legend(title="Optimisations", frameon=False, fontsize=9, labelcolor=TEXT, title_fontsize=9)
    ax.margins(x=0.12)
    fig.tight_layout()
    fig.savefig(os.path.join(PLOTS, "dsu_optimisations.png"), facecolor=SURFACE)
    plt.close(fig)


def main():
    os.makedirs(PLOTS, exist_ok=True)
    required = read("benchmark.csv")
    by_v = read("scaling_vertices.csv")
    by_e = read("scaling_edges.csv")
    line_plot(required, "V", "Required sizes: time vs number of vertices",
              "Vertices V (Small 10 / Medium 100 / Large 1000, log)", "required_time_vs_vertices.png")
    line_plot(required, "E", "Required sizes: time vs number of edges",
              "Edges E (Small 30 / Medium 500 / Large 5000, log)", "required_time_vs_edges.png")
    line_plot(by_v, "V", "Time vs number of vertices (E = 5V)", "Vertices V (log)", "time_vs_vertices.png")
    line_plot(by_e, "E", "Time vs number of edges (V = 10,000)", "Edges E (log)", "time_vs_edges.png")
    kruskal_vs_prim(by_v)
    dsu_plot(read("dsu.csv"))
    print("Plots written to", PLOTS)


if __name__ == "__main__":
    main()
