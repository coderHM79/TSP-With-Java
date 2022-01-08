package com.company;

import java.awt.geom.Point2D;
import java.text.DecimalFormat;

public class Main {

	public static void main(String[] args) {

		int numberOfCities = 8;

		int[] X = new int[] { 10, 15, 12, 18, 8, 9, 11, 5 };
		int[] Y = new int[] { 5, 20, 14, 6, 18, 13, 24, 19 };

		double[][] travelPrices = new double[numberOfCities][numberOfCities];
		for (int i = 0; i < numberOfCities; i++) {
			for (int j = 0; j <= i; j++) {
				if (i == j)
					travelPrices[i][j] = 0.00;
				else {
					DecimalFormat df = new DecimalFormat("#.##");
					travelPrices[i][j] = Double.parseDouble(df.format(Point2D.distance(X[i], Y[i], X[j], Y[j])));
					travelPrices[j][i] = travelPrices[i][j];
				}
			}
		}

		PrintDistanceOfCities(travelPrices, numberOfCities);

		SaleMan geneticAlgorithm = new SaleMan(numberOfCities, travelPrices, 0);
		Chromosome result = geneticAlgorithm.optimize();
		System.out.println("--------------\n final: \n ");
		System.out.println(result);

	}

	public static void PrintDistanceOfCities(double[][] travelPrices, int numberOfCities) {
		for (int i = 0; i < numberOfCities; i++) {
			for (int j = 0; j < numberOfCities; j++) {
				System.out.print(travelPrices[i][j] + "  ");
				if (travelPrices[i][j] / 10 == 0)
					System.out.print("  ");
				else
					System.out.print(' ');
			}
			System.out.print("\n\n");
		}
	}

}
