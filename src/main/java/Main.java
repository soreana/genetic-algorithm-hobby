import java.util.*;
import java.util.function.Function;
import java.util.stream.IntStream;

public class Main {
    private static final int MAX_POOL_SIZE = 10;
    private static Set<Chromosome> pool = new HashSet<>();
    private static Random random = new Random();

    private static void initializePool(Function<Integer, Chromosome> generator) {
        Random random = new Random();
        for (int i = 0; i < MAX_POOL_SIZE; i++)
            pool.add(generator.apply(random.nextInt(2048)));
    }

    private static void showPool(Set<Chromosome> pool) {
        pool.forEach(System.out::println);
        int sum = pool.stream().mapToInt(Chromosome::fitness).sum();
        int avg = sum / MAX_POOL_SIZE;
        OptionalInt max = pool.stream().mapToInt(Chromosome::fitness).max();
        OptionalInt min = pool.stream().mapToInt(Chromosome::fitness).min();
        System.out.println(String.format("Sum: %d, Avg: %d, Max: %d, Min: %d",sum,avg,max.orElse(-1),min.orElse(-1)));
    }

    private static Chromosome getCandidate() {
        double temp = random.nextDouble();
        for (Chromosome candidate : pool) {
            temp -= candidate.getRoletProbability();
            if (temp < 0)
                return candidate;
        }
        return null;
    }

    private static Chromosome getCandidateExcept(Chromosome chromosome) {
        Chromosome temp;
        do {
            temp = getCandidate();
        } while (Objects.equals(temp, chromosome));

        return temp;
    }

    private static Chromosome mating(Chromosome mother, Chromosome father) {
        int split = random.nextInt(Chromosome.CHROMOSOME_LENGTH);
        String sperm = father.getBinaryString().substring(0, split);
        String ovum = mother.getBinaryString().substring(split);
        String diploid = sperm + ovum;

        // mutation or not
        if(random.nextInt(10)>7) {
            int mutation_position = random.nextInt(Chromosome.CHROMOSOME_LENGTH);
            int mutation_number = random.nextInt(2);

            if (mutation_position == 0)
                diploid = String.valueOf(mutation_number) + diploid.substring(mutation_position + 1);
            else
                diploid = diploid.substring(0, mutation_position - 1) + String.valueOf(mutation_number)
                        + diploid.substring(mutation_position + 1);
        }

        return new Chromosome(Integer.parseInt(diploid, 2));
    }

    private static void updateRoletProbabilities(Set<Chromosome> pool){
        int sum = pool.stream().mapToInt(Chromosome::fitness).sum();
        pool.forEach(e -> e.updateRoletProbability(sum));
    }


    public static void main(String[] args) {
        initializePool(Chromosome::new);


        for(int i=0 ; i<20 ; i++) {
            System.out.println("========= " + i + "th pool is ==========");
            updateRoletProbabilities(pool);
            showPool(pool);
            System.out.println();
            Set<Chromosome> matePool = new HashSet<>();

            while (matePool.size() < MAX_POOL_SIZE){
                Chromosome mother = getCandidate();
                Chromosome father = getCandidateExcept(mother);
                Chromosome child = mating(Objects.requireNonNull(mother), father);
                matePool.add(child);
            }

            pool = matePool;
        }

        System.out.println(
                IntStream.rangeClosed(0,2048)
                        .mapToObj(Chromosome::new)
                        .mapToInt(Chromosome::fitness)
                        .max());
    }
}
