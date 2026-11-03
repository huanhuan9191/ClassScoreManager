package com.score.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 统计分析结果：平均分、最高/最低分、及格率、优秀率、分数段分布、排名
 */
public class StatResult {

    private String className = "";
    private String courseName = "";
    private int total;

    private double avg;
    private double max;
    private double min;

    private int passCount;
    private int excellentCount;

    /** 分数段：0-59, 60-69, 70-79, 80-89, 90-100 */
    private int[] distribution = new int[5];

    /** 成绩明细（按分数降序，即排名） */
    private List<Score> ranking = new ArrayList<Score>();

    public double getPassRate() {
        return total == 0 ? 0 : round(passCount * 100.0 / total);
    }

    public double getExcellentRate() {
        return total == 0 ? 0 : round(excellentCount * 100.0 / total);
    }

    /** 柱状图高度百分比：以最大值为 100% 基准 */
    public int getBarPercent(int index) {
        int maxCount = 0;
        for (int c : distribution) {
            if (c > maxCount) {
                maxCount = c;
            }
        }
        if (maxCount == 0) {
            return 0;
        }
        return distribution[index] * 100 / maxCount;
    }

    /** 五个分数段柱状图的高度百分比（JSP 直接用 ${stat.barPercents}） */
    public int[] getBarPercents() {
        int[] result = new int[5];
        for (int i = 0; i < 5; i++) {
            result[i] = getBarPercent(i);
        }
        return result;
    }

    /** 五个分数段的标签 */
    public String[] getDistLabels() {
        return new String[]{"0-59", "60-69", "70-79", "80-89", "90-100"};
    }

    public static double round(double v) {
        return Math.round(v * 100) / 100.0;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public double getAvg() {
        return avg;
    }

    public void setAvg(double avg) {
        this.avg = round(avg);
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public int getPassCount() {
        return passCount;
    }

    public void setPassCount(int passCount) {
        this.passCount = passCount;
    }

    public int getExcellentCount() {
        return excellentCount;
    }

    public void setExcellentCount(int excellentCount) {
        this.excellentCount = excellentCount;
    }

    public int[] getDistribution() {
        return distribution;
    }

    public void setDistribution(int[] distribution) {
        this.distribution = distribution;
    }

    public List<Score> getRanking() {
        return ranking;
    }

    public void setRanking(List<Score> ranking) {
        this.ranking = ranking;
    }
}
