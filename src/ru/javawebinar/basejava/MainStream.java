package ru.javawebinar.basejava;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.partitioningBy;
import static java.util.stream.Collectors.toList;

public class MainStream {

    public static void main(String[] args) {
        Random random = new Random();
        int[] ints = new int[random.nextInt(3, 7)];

        IntStream.range(0, ints.length).forEach(i -> ints[i] = random.nextInt(1, 9));

        System.out.println("original array: " + Arrays.toString(ints));

        System.out.println("minValue result: " + minValue(ints));

        List<Integer> integerList = Arrays.stream(ints).boxed().collect(Collectors.toCollection(ArrayList::new));

        System.out.println("oddOrEven result: " + oddOrEven(integerList));
    }

    private static int minValue(int[] values) {
        Stream<Integer> integerStream = Arrays.stream(values).boxed();
        final int[] result = {0};
        integerStream.distinct().sorted().forEach(i -> result[0] = i + result[0] * 10);
        return result[0];
    }

    private static List<Integer> oddOrEven(List<Integer> integers) {
        Map<Boolean, List<Integer>> map = integers
                .stream()
                .collect(partitioningBy(x -> x % 2 == 0, toList()));
        return map.get(map.get(false).size() % 2 != 0);
    }
}
