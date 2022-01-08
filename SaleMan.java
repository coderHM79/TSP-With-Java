package com.company;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class SaleMan {
	private int generationSize;
	private int genomeSize;
	private int numberOfCities;
	private int reproductionSize;
	private int maxIterations;
	private float mutationRate;
	private double[][] travelPrices;
	private int startingCity;

	public SaleMan(int numberOfCities, double[][] travelPrices, int startingCity) {
		this.numberOfCities = numberOfCities;
		this.genomeSize = numberOfCities - 1;
		this.travelPrices = travelPrices;
		this.startingCity = startingCity;

		generationSize = 1000;
		reproductionSize = (int) (0.8 * generationSize);
		maxIterations = 40;
		mutationRate = 0.1f;
	}

	public Chromosome optimize() {
		List<Chromosome> population = initialPopulation();
		population = Sort(population);
		Chromosome globalBestGenome = population.get(0);
		for (int i = 0; i < maxIterations; i++) {
			List<Chromosome> selected = selection(population);
			population = createGeneration(selected);
			population = Sort(population);
			globalBestGenome = population.get(0);
		}
		return globalBestGenome;
	}

	public List<Chromosome> initialPopulation() {
		List<Chromosome> population = new ArrayList<>();
		for (int i = 0; i < generationSize; i++) {
			population.add(new Chromosome(numberOfCities, travelPrices, startingCity));
		}
		return population;
	}

	public List<Chromosome> selection(List<Chromosome> population) {

		List<Chromosome> selected = new ArrayList<>();

		// calculating total Fitness
		double totalFitness = population.stream().map(Chromosome::getFitness).mapToDouble(Double::doubleValue).sum();

		// change to minimum and then normalize
		List<Double> normalizedFitness = new ArrayList<>();
		for (Chromosome person : population)
			normalizedFitness.add((1.0 / person.getFitness()) / totalFitness);

		// become cumulative
		List<Double> Cp = new ArrayList<>();
		double sum = 0;
		for (Double i : normalizedFitness) {
			sum += i;
			Cp.add(sum);
		}

		// send Array to rouletteSelection and get Index of Kromozome in population list
		for (int i = 0; i < reproductionSize; i++) {
			int index = rouletteSelection(Cp);
			selected.add(population.get(index));

		}
		return selected;
	}

	public int rouletteSelection(List<Double> Cp) {

		Random random = new Random();
		double randomValue = random.nextDouble(0.99);
		int index = 0;
		for (double i : Cp) {
			if (randomValue <= i) {
				index = Cp.indexOf(i);
				break;
			}
		}
		return index;

	}

	public List<Chromosome> createGeneration(List<Chromosome> population) {
		List<Chromosome> CrroosOvered = new ArrayList<>();
		int currentGenerationSize = 0;
		while (currentGenerationSize < generationSize) {
			List<Chromosome> parents = pickNRandomElements(population, 2);
			List<Chromosome> children = crossover(parents);

			CrroosOvered.addAll(children);
			currentGenerationSize += 2;
		}

		List<Chromosome> Mutated = new ArrayList<>();
		int i = 0;
		while (i < generationSize) {

			Mutated.add(mutate(CrroosOvered.get(i)));
			i++;
		}
		return Mutated;
	}

	public List<Chromosome> crossover(List<Chromosome> parents) {
		// housekeeping
		Random random = new Random();
		int breakpoint = random.nextInt(genomeSize);
		List<Chromosome> children = new ArrayList<>();

		// copy parental genomes - we copy so we wouldn't modify in case they were
		// chosen to participate in crossover multiple times
		List<Integer> parent1Genome = new ArrayList<>(parents.get(0).getGenome());
		List<Integer> parent2Genome = new ArrayList<>(parents.get(1).getGenome());

		// creating child 1
		for (int i = 0; i < breakpoint; i++) {
			int newVal;
			newVal = parent2Genome.get(i);
			Collections.swap(parent1Genome, parent1Genome.indexOf(newVal), i);
		}
		children.add(new Chromosome(parent1Genome, numberOfCities, travelPrices, startingCity));
		parent1Genome = parents.get(0).getGenome(); // reseting the edited parent

		// creating child 2
		for (int i = breakpoint; i < genomeSize; i++) {
			int newVal = parent1Genome.get(i);
			Collections.swap(parent2Genome, parent2Genome.indexOf(newVal), i);
		}
		children.add(new Chromosome(parent2Genome, numberOfCities, travelPrices, startingCity));

		return children;
	}

	public Chromosome mutate(Chromosome salesman) {
		Random random = new Random();
		float mutate = random.nextFloat();
		if (mutate < mutationRate) {
			List<Integer> genome = salesman.getGenome();
			Collections.swap(genome, random.nextInt(genomeSize), random.nextInt(genomeSize));
			return new Chromosome(genome, numberOfCities, travelPrices, startingCity);
		}
		return salesman;
	}

	public static <E> List<E> pickNRandomElements(List<E> list, int n) {
		Random r = new Random();
		int length = list.size();

		if (length < n)
			return null;

		for (int i = length - 1; i >= length - n; --i) {
			Collections.swap(list, i, r.nextInt(i + 1));
		}
		return list.subList(length - n, length);
	}

	public List<Chromosome> Sort(List<Chromosome> pop) {
		Collections.sort(pop, Comparator.comparing(Chromosome::getFitness));
		return pop;
	}

	public void printGeneration(List<Chromosome> generation) {
		for (Chromosome genome : generation) {
			System.out.println(genome);

		}
	}

}
