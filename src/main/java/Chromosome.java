public class Chromosome {
    private Double value;
    private Double roletProbability;
    public static final int CHROMOSOME_LENGTH = 11;
    private String binaryString = null;

    Chromosome(int value) {
        this.value = (double) value;

        StringBuilder temp = new StringBuilder(Integer.toString(value,2));
        int left_zero_length = CHROMOSOME_LENGTH - temp.length();
        for (int i =0 ; i< left_zero_length ; i++){
            temp.insert(0,"0");
        }

        binaryString = temp.toString();
    }

    public String getBinaryString() {
        return binaryString;
    }

    public Double getValue() {
        return  value;
    }

    public Double getRoletProbability() {
        return roletProbability;
    }

    public int fitness(){
        return (int) (1000 * Math.sin(value/500)+ 600 * Math.sin(value/200) + 300 * Math.sin(value/300)) + 1291;
    }

    void updateRoletProbability(int sum){
        roletProbability = (double)fitness()/sum;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Chromosome && ((Chromosome) o).value.equals(this.value);
    }

    @Override
    public String toString(){
        return String.format("Value: %4.0f, Fitness: %4d, probability: %f, expected: %.2f, %s", value, fitness(),
                roletProbability,10*roletProbability, binaryString);
    }
}
