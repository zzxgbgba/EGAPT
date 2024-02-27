import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.jfree.chart.*;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.Layer;
import org.jfree.ui.LengthAdjustmentType;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.TextAnchor;

public class Chartmaker {
    public static void main(String[] args) {

        ChartFrame cf = new ChartFrame("Test", createChart());
        cf.pack();
        cf.setVisible(true);

    }

    /**
     * 创建数据集合
     *
     * @return dataSet
     */
    public static CategoryDataset createGADataSet() {//普通遗传算法
        // 实例化DefaultCategoryDataset对象
        DefaultCategoryDataset dataSet = new DefaultCategoryDataset();

        //曲线名称
        String GAseries = "Genetic algorithm"; // series指的就是报表里的那条数据线
        String RandomSeries = "Least-loaded scheduling";
        String RotateSeries = "Rotating scheduling";
        String EliteSeries = "EGAPT";
        //因此 对数据线的相关设置就需要联系到serise
        //比如说setSeriesPaint 设置数据线的颜色

        //横轴名称(列名称)
        String[] phyMachineCount = new String[5];
        String[] phyMachineCountValue = {"0", "1.5", "3", "4.5", "6"};
        for (int i = 0; i < 5; i++) {
            phyMachineCount[i] = phyMachineCountValue[i];
        }

        //遗传算法start
        dataSet.addValue(0.0595, GAseries, phyMachineCount[0]);
        dataSet.addValue(0.0758, GAseries, phyMachineCount[1]);
        dataSet.addValue(0.0873, GAseries, phyMachineCount[2]);
        dataSet.addValue(0.1010, GAseries, phyMachineCount[3]);
        dataSet.addValue(0.1159, GAseries, phyMachineCount[4]);
        //遗传算法end

        //最小负载算法start
        dataSet.addValue(0.0712, RandomSeries, phyMachineCount[0]);
        dataSet.addValue(0.0786, RandomSeries, phyMachineCount[1]);
        dataSet.addValue(0.0948, RandomSeries, phyMachineCount[2]);
        dataSet.addValue(0.0929, RandomSeries, phyMachineCount[3]);
        dataSet.addValue(0.0982, RandomSeries, phyMachineCount[4]);
        //最小负载算法end

        //轮转调度算法start
        dataSet.addValue(0.0712, RotateSeries, phyMachineCount[0]);
        dataSet.addValue(0.0846, RotateSeries, phyMachineCount[1]);
        dataSet.addValue(0.0989, RotateSeries, phyMachineCount[2]);
        dataSet.addValue(0.0999, RotateSeries, phyMachineCount[3]);
        dataSet.addValue(0.1184, RotateSeries, phyMachineCount[4]);
        //轮转调度算法end

        //精英遗传算法start
        dataSet.addValue(0.0469, EliteSeries, phyMachineCount[0]);
        dataSet.addValue(0.0534, EliteSeries, phyMachineCount[1]);
        dataSet.addValue(0.0715, EliteSeries, phyMachineCount[2]);
        dataSet.addValue(0.0701, EliteSeries, phyMachineCount[3]);
        dataSet.addValue(0.0770, EliteSeries, phyMachineCount[4]);
        //精英遗传算法end


        return dataSet;
    }

    /**
     * 创建JFreeChart对象
     *
     * @return chart
     */
    public static JFreeChart createChart() {
        StandardChartTheme standardChartTheme = new StandardChartTheme("CN"); //创建主题样式
        standardChartTheme.setExtraLargeFont(new Font("Times New Roman", Font.BOLD, 20)); //设置标题字体
        standardChartTheme.setRegularFont(new Font("Times New Roman", Font.BOLD, 15)); //设置图例的字体
        standardChartTheme.setLargeFont(new Font("Times New Roman", Font.BOLD, 15)); //设置轴向的字体
        ChartFactory.setChartTheme(standardChartTheme);//设置主题样式
        // 通过ChartFactory创建JFreeChart
        JFreeChart chart = ChartFactory.createLineChart(null,// 报表题目，字符串类型
                "Task overlap", // 横轴
                "AVFit(PS,MS,P)", // 纵轴
                createGADataSet(), // 获得数据集
                PlotOrientation.VERTICAL, // 图标方向垂直
                true, // 显示图例
                false, // 不用生成工具
                false // 不用生成URL地址
        );
        //整个大的框架属于chart  可以设置chart的背景颜色


        // 生成图形
        CategoryPlot plot = (CategoryPlot ) chart.getPlot();
        chart.getLegend().setPosition(RectangleEdge.RIGHT);

        // 图像属性部分
        plot.setBackgroundPaint(Color.white);
        plot.setDomainGridlinesVisible(true);  //设置背景网格线是否可见
        plot.setDomainGridlinePaint(Color.BLACK); //设置背景网格线颜色
        plot.setRangeGridlinePaint(Color.GRAY);
        plot.setNoDataMessage("没有数据");//没有数据时显示的文字说明。

        // 数据轴属性部分
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createStandardTickUnits());
        rangeAxis.setAutoRangeIncludesZero(true); //自动生成
        rangeAxis.setUpperMargin(0.10);
        rangeAxis.setLabelAngle(0);
        rangeAxis.setAutoRange(false);


        LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot
                .getRenderer();
        renderer.setShapesVisible(true);
        renderer.setDrawOutlines(true);
        //renderer.setUseFillPaint(true);
        //renderer.setBaseFillPaint(Color.white);
        //折线加粗
        renderer.setSeriesStroke(0, new BasicStroke(3.0F));
        renderer.setSeriesStroke(1, new BasicStroke(3.0F));
        renderer.setSeriesStroke(2, new BasicStroke(3.0F));
        renderer.setSeriesPaint(2, Color.ORANGE);//c橙色
        renderer.setSeriesStroke(3, new BasicStroke(3.0F));
        renderer.setSeriesPaint(3, Color.GREEN);//c橙色
        //折点加粗
        renderer.setSeriesOutlineStroke(0, new BasicStroke(4.0F));
        renderer.setSeriesOutlineStroke(1, new BasicStroke(4.0F));
        renderer.setSeriesOutlineStroke(2, new BasicStroke(4.0F));
        renderer.setSeriesOutlineStroke(3, new BasicStroke(4.0F));
        //折点
        //renderer.setSeriesShape(3, new java.awt.geom.);

        renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(
                ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_LEFT));

        DecimalFormat decimalformat = new DecimalFormat("#.####");// 数据点显示数据值的格式
        renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator("{2}", decimalformat));
        renderer.setBaseItemLabelFont(new Font("Dialog", 1, 10));//设置提示折点数据形状
        renderer.setBaseItemLabelsVisible(true);
        renderer.setBaseShapesFilled(true);

        plot.setRenderer(renderer);


        // 创建文件输出流

        File fos_jpg = new File("E://bloodSugarChart.jpg ");

        // 输出到哪个输出流
        try {
            ChartUtilities.saveChartAsJPEG(fos_jpg, chart, // 统计图表对象
                    600, // 宽
                    340 // 高
            );
        } catch (Exception e) {

        }


        return chart;
    }


}
