package com.company;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Chromosome implements Comparable {
	List<Integer> genome;
	double[][] travelPrices;
	int startingCity;
	int numberOfCities = 0;
	double fitness;

	public Chromosome(int numberOfCities, double[][] travelPrices2, int startingCity) {
		this.travelPrices = travelPrices2;
		this.startingCity = startingCity;
		this.numberOfCities = numberOfCities;
		genome = randomSalesman();
		fitness = this.calculateFitness();
	}

	public Chromosome(List<Integer> permutationOfCities, int numberOfCities, double[][] travelPrices,
					  int startingCity) {
		genome = permutationOfCities;
		this.travelPrices = travelPrices;
		this.startingCity = startingCity;
		this.numberOfCities = numberOfCities;
		fitness = this.calculateFitness();
	}

	public double calculateFitness() {
		double fitness = 0;
		int currentCity = startingCity;
		for (int gene : genome) {
			fitness += travelPrices[currentCity][gene];
			currentCity = gene;
		}

		fitness += travelPrices[genome.get(numberOfCities - 2)][startingCity];
		DecimalFormat df = new DecimalFormat("#.##");
		fitness = Double.parseDouble(df.format(fitness));
		return fitness;
	}

	private List<Integer> randomSalesman() {
		List<Integer> result = new ArrayList<Integer>();
		for (int i = 0; i < numberOfCities; i++) {
			if (i != startingCity)
				result.add(i);
		}
		Collections.shuffle(result);
		return result;
	}

	public List<Integer> getGenome() {
		return genome;
	}

	public int getStartingCity() {
		return startingCity;
	}

	public double getFitness() {
		return fitness;
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Path: ");
		sb.append(startingCity);
		for (int gene : genome) {
			sb.append(" ");
			sb.append(gene);
		}
		sb.append(" ");
		sb.append(startingCity);
		sb.append("\nLength: ");
		sb.append(this.fitness);
		return sb.toString();
	}

	@Override
	public int compareTo(Object o) {
		Chromosome genome = (Chromosome) o;
		if (this.fitness > genome.getFitness())
			return 1;
		else if (this.fitness < genome.getFitness())
			return -1;
		else
			return 0;
	}
}
