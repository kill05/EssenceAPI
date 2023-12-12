package com.github.kill05.essenceapi.core.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WeightedRandomBag<T> {

    private final List<Entry<T>> entries;
    private int totalWeight;

    public WeightedRandomBag() {
        entries = new ArrayList<>();
        totalWeight = 0;
    }

    public WeightedRandomBag<T> addElement(T element, int weight) {
        totalWeight += weight;
        entries.add(new Entry<>(element, weight));
        return this;
    }

    public T getRandomElement(Random random) {
        if (entries.isEmpty()) throw new IllegalStateException("Random Bag is empty!");

        int randomNumber = random.nextInt(totalWeight);

        for (Entry<T> entry : entries) {
            randomNumber -= entry.weight;
            if (randomNumber < 0) {
                return entry.element;
            }
        }

        // Fallback if for some reason the loop doesn't return an element
        throw new IllegalStateException("Failed to select a random item from a Random Bag.");
    }

    private static class Entry<T> {
        private final T element;
        private final int weight;

        private Entry(T element, int weight) {
            this.element = element;
            this.weight = weight;
        }
    }
}
