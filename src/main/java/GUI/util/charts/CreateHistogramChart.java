package GUI.util.charts;

import GUI.util.ValidateExtension;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.statistics.HistogramDataset;

/**
 *
 */
public class CreateHistogramChart implements IHistogramDataset, ICreateChart {
    private HistogramDataset dataset;
    double[] data;

    public CreateHistogramChart(double[] data) {
        this.data = data;
        createDataset();
    }
    //delete that. It has to be read from Database
    /*
    double[] values = { 95, 49, 14, 59, 50, 66, 47, 40, 1, 67,
            12, 58, 28, 63, 14, 9, 31, 17, 94, 71,
            49, 64, 73, 97, 15, 63, 10, 12, 31, 62,
            93, 49, 74, 90, 59, 14, 15, 88, 26, 57,
            77, 44, 58, 91, 10, 67, 57, 19, 88, 84
    };
     */

    @Override
    public JFreeChart createChart() {
        JFreeChart histogram = ChartFactory.createHistogram("JFreeChart Histogram",
                "y values", "x values", dataset);
        return histogram;
    }

    /**
     * this method has to be called first
     * if not dataset will be null
     */
    private void createDataset() {
        HistogramDataset dataset = new HistogramDataset();
        dataset.addSeries("key", data, 20);
        this.dataset = dataset;
    }
}
