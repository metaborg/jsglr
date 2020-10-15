import csv
import sys
from os import makedirs, path, scandir, remove

import matplotlib.pyplot as plt
import pdftools

if len(sys.argv) < 3:
    print(f"Usage: {sys.argv[0]} DIR REPORTS_DIR", file=sys.stderr)
    sys.exit(2)

[_, DIR, REPORTS_DIR] = sys.argv

COLORS = {
    "Batch": "rs",
    "Incremental": "g^",
    "IncrementalNoCache": "bv",
}


def read_csv(p):
    with open(p) as csv_file:
        lines = list(csv.reader(csv_file, delimiter=','))
        header = lines[0]
        return [{h: v for h, v in zip(header, line)} for line in lines[1:]]


def base_plot(plot_size, xlabel, ylabel, zlabel="", subplot_kwargs=None):
    fig = plt.figure(figsize=plot_size[:2])
    ax = fig.add_subplot(**(subplot_kwargs if subplot_kwargs is not None else {}))

    ax.margins(*(0.1 / x for x in plot_size))
    ax.set_xlabel(xlabel)
    ax.set_ylabel(ylabel)
    if zlabel:
        ax.set_zlabel(zlabel)

    return fig, ax


def plot_times(rows, parser_types):
    n = len(rows)
    plot_size = (8 if n < 50 else 12 if n < 100 else 18 if n < 200 else 24, 6)
    fig, ax1 = base_plot(plot_size, "Version number", "Parse time (ms)")

    # Plot lines on ax2 below those on ax1 (https://stackoverflow.com/a/57307539)
    ax1.set_zorder(1)  # default z order is 0 for ax1 and ax2
    ax1.patch.set_visible(False)  # prevents ax1 from hiding ax2

    ax2 = ax1.twinx()  # instantiate a second axis that shares the same x-axis
    ax2.margins(*(0.1 / x for x in plot_size))
    ax2.set_ylabel("Change size (bytes)", color="y")
    ax2.tick_params(labelcolor="y")

    x, y = zip(*((int(row["i"]), int(row["Removed"]) + int(row["Added"])) for row in rows))
    ax2.plot(x, y, "yo", label="Change size", markersize=3)

    for column in parser_types:
        x, y, yerr = zip(*((int(row["i"]), float(row[column]), float(row[column + " Error (99.9%)"] or 0))
                           for row in rows if row[column]))
        ax1.errorbar(x, y, yerr, fmt=COLORS[column], label=column)

    # Combine legends for both axes (https://stackoverflow.com/a/10129461)
    lines1, labels1 = ax1.get_legend_handles_labels()
    lines2, labels2 = ax2.get_legend_handles_labels()
    ax2.legend(lines1 + lines2, labels1 + labels2, loc=2)

    fig.tight_layout()
    return fig


def plot_times_vs_changes(rows, unit, *changes):
    fig, ax = base_plot((8, 6), f"Change size ({unit})", "Parse time (ms)")

    x, y = zip(*((sum(int(row[change]) for change in changes), float(row["Incremental"]))
                 for row in rows if row["Incremental"]))
    ax.plot(x, y, COLORS["Incremental"], label="Incremental", markersize=3)

    fig.tight_layout()
    return fig


def plot_times_vs_changes_3D(rows):
    fig, ax = base_plot((6, 6, 6), "Change size (bytes)", "Change size (chunks)", "Parse time (ms)",
                        {"projection": "3d"})

    x, y, z = zip(*((int(row["Added"]) + int(row["Removed"]), int(row["Changes"]), float(row["Incremental"]))
                    for row in rows if row["Incremental"]))
    ax.plot(x, y, z, COLORS["Incremental"], label="Incremental", markersize=3)

    ax.view_init(elev=30, azim=-45)
    return fig


print("Creating plots for incremental...")
for language in scandir(path.join(DIR, "results", "incremental")):
    print(f" {language.name}")
    for result_file in scandir(language.path):
        csv_basename = ".".join(result_file.name.split(".")[:-1])
        print(f"  {csv_basename}")

        result_data = read_csv(result_file.path)
        initial_size = int(result_data[0]["Added"])
        result_data = [row for row in result_data if int(row["Removed"]) + int(row["Added"]) < initial_size]
        result_data_except_first = result_data[1:]

        report_path = path.join(REPORTS_DIR, "incremental", language.name, csv_basename)
        makedirs(report_path, exist_ok=True)

        reports = [
            (plot_times(result_data, ["Batch", "Incremental", "IncrementalNoCache"]), "report"),
            (plot_times(result_data_except_first, ["Incremental"]), "report-except-first"),
            (plot_times_vs_changes(result_data_except_first, "bytes", "Added", "Removed"), "report-time-vs-bytes"),
            (plot_times_vs_changes(result_data_except_first, "chunks", "Changes"), "report-time-vs-changes"),
            (plot_times_vs_changes_3D(result_data_except_first), "report-time-vs-changes-3D"),
        ]

        for fig, name in reports:
            fig.savefig(path.join(report_path, name + ".pdf"))
            fig.savefig(path.join(report_path, name + ".svg"))

        plt.close("all")

        merged_path = path.join(report_path, "merged.pdf")
        if path.exists(merged_path):
            remove(merged_path)
        pdftools.pdf_merge([path.join(report_path, name + ".pdf") for _, name in reports], merged_path)
