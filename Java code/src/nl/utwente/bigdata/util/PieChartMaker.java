package nl.utwente.bigdata.util;

/**
 * Created by JJ on 22-1-2015.
 */

import java.awt.Font;
import java.io.File;
import java.io.IOException;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/**
 * A simple demonstration application showing how to create a pie chart using
 * data from a {@link DefaultPieDataset}.
 */
public class PieChartMaker {
    public static void saveChart(PieDataset dataset, String country) {

        JFreeChart chart = ChartFactory.createPieChart(
                "",  // chart title
                dataset,             // data
                false,               // include legend
                false,
                false
        );

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        plot.setNoDataMessage("No data available");
        plot.setCircular(true);
        plot.setLabelGap(0.01);

        try {
            ChartUtilities.saveChartAsPNG(new File("D:\\Documents\\Utwente\\Managing Big Data\\final\\presentation\\googleRegionMap\\pieCharts\\" + country + ".png"), chart, 200, 200);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
