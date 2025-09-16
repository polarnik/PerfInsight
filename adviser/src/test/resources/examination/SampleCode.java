public class SampleCode {
    /**
     * A sample function that performs some operations.
     * This function has potential performance issues.
     */
    public void processData(int[] data) {
        // Inefficient loop implementation
        for (int i = 0; i < data.length; i++) {
            // Creating objects inside a loop
            Integer value = new Integer(data[i]);

            // Expensive computation without caching
            int result = calculateExpensiveResult(value);

            // Process the result
            System.out.println("Result: " + result);
        }
    }

    /**
     * An expensive calculation that could benefit from memoization.
     */
    private int calculateExpensiveResult(Integer value) {
        // Simulate an expensive computation
        try {
            Thread.sleep(10); // Simulate work
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return value * value;
    }
}
