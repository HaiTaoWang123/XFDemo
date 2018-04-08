package com.haitaow.baseutils.algorithm;

/**
 * @Class:
 * @Description:
 * @Author: haitaow(haitaow@hpe.com)
 * @Date: 4/8/2018-11:25 AM.
 * @Version 1.0
 */

public class SortAlgorithm {
    /**
     * 选择排序
     *
     * @param isAccending 是否升序
     * @param doubles     待排序数组
     */
    private static void selectSort(double[] doubles, boolean isAccending) {
        if (isAccending) {
            for (int i = 0; i < doubles.length; i++) {
                for (int j = i + 1; j < doubles.length; j++) {
                    double temp = doubles[j];
                    if (doubles[i] > doubles[j]) {
                        doubles[j] = doubles[i];
                        doubles[i] = temp;
                    }
                }
            }
        } else {
            for (int i = 0; i < doubles.length; i++) {
                for (int j = i + 1; j < doubles.length; j++) {
                    double temp = doubles[j];
                    if (doubles[i] < doubles[j]) {
                        doubles[j] = doubles[i];
                        doubles[i] = temp;
                    }
                }
            }
        }
    }

    /**
     * 冒泡排序
     *
     * @param isAccending 是否升序
     * @param doubles     待排序数组
     */
    private static void bubbleSort(double[] doubles, boolean isAccending) {
        double temp;
        if (isAccending) {
            for (int i = 0; i < doubles.length - 1; i++) {
                for (int j = 0; j < doubles.length - 1 - i; j++) {
                    temp = doubles[j];
                    if (doubles[j] > doubles[j + 1]) {
                        doubles[j] = doubles[j + 1];
                        doubles[j + 1] = temp;
                    }
                }
            }
        } else {
            for (int i = 0; i < doubles.length - 1; i++) {
                for (int j = 0; j < doubles.length - 1 - i; j++) {
                    temp = doubles[j];
                    if (doubles[j] < doubles[j + 1]) {
                        doubles[j] = doubles[j + 1];
                        doubles[j + 1] = temp;
                    }
                }
            }
        }
    }
}
