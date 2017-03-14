package com.exalttech.trex.ui.controllers.dashboard.tabs.streams;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import com.exalttech.trex.ui.models.stats.flow.StatsFlowStream;
import com.exalttech.trex.ui.views.statistics.StatsLoader;
import com.exalttech.trex.ui.views.statistics.cells.CellType;
import com.exalttech.trex.ui.views.statistics.cells.HeaderCell;
import com.exalttech.trex.ui.views.statistics.cells.StatisticLabelCell;
import com.exalttech.trex.util.ArrayHistory;
import com.exalttech.trex.util.Initialization;
import com.exalttech.trex.util.Util;


public class DashboardTabStreams extends AnchorPane {
    @FXML
    private GridPane table;

    public DashboardTabStreams() {
        Initialization.initializeFXML(this, "/fxml/Dashboard/tabs/streams/DashboardTabStreams.fxml");
    }

    public void update(Set<Integer> visiblePorts, Set<String> visibleStreams, int streamsCount) {
        StatsLoader statsLoader = StatsLoader.getInstance();
        Map<String, ArrayHistory<StatsFlowStream>> streams = statsLoader.getFlowStatsHistoryMap();

        int firstColumnWidth = 120;
        int secondHeaderWidth = 150;

        table.getChildren().clear();

        table.add(new HeaderCell(firstColumnWidth, "PG ID"), 0, 0);
        table.add(new StatisticLabelCell("Tx pps", firstColumnWidth, false, CellType.DEFAULT_CELL, false), 0, 1);
        table.add(new StatisticLabelCell("Tx Bps L2", firstColumnWidth, true, CellType.DEFAULT_CELL, false), 0, 2);
        table.add(new StatisticLabelCell("Tx Bps L1", firstColumnWidth, false, CellType.DEFAULT_CELL, false), 0, 3);
        table.add(new StatisticLabelCell("Rx pps", firstColumnWidth, true, CellType.DEFAULT_CELL, false), 0, 4);
        table.add(new StatisticLabelCell("Rx bps", firstColumnWidth, false, CellType.DEFAULT_CELL, false), 0, 5);
        table.add(new StatisticLabelCell("Tx pkts", firstColumnWidth, true, CellType.DEFAULT_CELL, false), 0, 6);
        table.add(new StatisticLabelCell("Rx pkts", firstColumnWidth, false, CellType.DEFAULT_CELL, false), 0, 7);
        table.add(new StatisticLabelCell("Tx bytes", firstColumnWidth, true, CellType.DEFAULT_CELL, false), 0, 8);
        table.add(new StatisticLabelCell("Rx bytes", firstColumnWidth, false, CellType.DEFAULT_CELL, false), 0, 9);

        AtomicInteger rowIndex = new AtomicInteger(1);

        streams.forEach((String stream, ArrayHistory<StatsFlowStream> history) -> {
            if (rowIndex.get() > streamsCount || (visibleStreams != null && !visibleStreams.contains(stream)) || history.isEmpty()) {
                return;
            }

            StatsFlowStream last = history.last();
            table.add(new HeaderCell(secondHeaderWidth, stream), rowIndex.get(), 0);
            table.add(new StatisticLabelCell(Util.getFormatted(String.valueOf(round(last.calcTotalTxPps(visiblePorts))), true, "pkt/s"), secondHeaderWidth, false, CellType.DEFAULT_CELL, true), rowIndex.get(), 1);
            table.add(new StatisticLabelCell(Util.getFormatted(String.valueOf(round(last.calcTotalTxBpsL2(visiblePorts))), true, "B/s"), secondHeaderWidth, true, CellType.DEFAULT_CELL, true), rowIndex.get(), 2);
            table.add(new StatisticLabelCell(Util.getFormatted(String.valueOf(round(last.calcTotalTxBpsL1(visiblePorts))), true, "B/s"), secondHeaderWidth, false, CellType.DEFAULT_CELL, true), rowIndex.get(), 3);
            table.add(new StatisticLabelCell(Util.getFormatted(String.valueOf(round(last.calcTotalRxPps(visiblePorts))), true, "pkt/s"), secondHeaderWidth, true, CellType.DEFAULT_CELL, true), rowIndex.get(), 4);
            table.add(new StatisticLabelCell(Util.getFormatted(String.valueOf(round(last.calcTotalRxBps(visiblePorts))), true, "B/s"), secondHeaderWidth, false, CellType.DEFAULT_CELL, true), rowIndex.get(), 5);
            table.add(new StatisticLabelCell(Util.getFormatted(String.valueOf(last.calcTotalTxPkts(visiblePorts)), true, "pkts"), secondHeaderWidth, true, CellType.DEFAULT_CELL, true), rowIndex.get(), 6);
            table.add(new StatisticLabelCell(Util.getFormatted(String.valueOf(last.calcTotalRxPkts(visiblePorts)), true, "pkts"), secondHeaderWidth, false, CellType.DEFAULT_CELL, true), rowIndex.get(), 7);
            table.add(new StatisticLabelCell(Util.getFormatted(String.valueOf(last.calcTotalTxBytes(visiblePorts)), true, "B"), secondHeaderWidth, true, CellType.DEFAULT_CELL, true), rowIndex.get(), 8);
            table.add(new StatisticLabelCell(Util.getFormatted(String.valueOf(last.calcTotalRxBytes(visiblePorts)), true, "B"), secondHeaderWidth, false, CellType.DEFAULT_CELL, true), rowIndex.get(), 9);

            rowIndex.addAndGet(1);
        });
    }

    static double round(double value) {
        return ((int)(value*100))/100.0;
    }
}