package com.company;

public class ProgressBar {

    private final int segments;

    /**
     * Instantiates a new {@code ProgressBar} with the given amount of
     * segments, in other words the width in characters.
     *
     * @param segments The amount of {@code segments}.
     * @author Stephan de Jongh
     */
    public ProgressBar(int segments) {
        this.segments = segments;
    }

    /**
     * Updates the {@code ProgressBar} with the current progress, based on
     * the {@code progress} made towards the goal of {@code total}.
     *
     * @param progress {@code long} indicating the amount of progress made.
     * @param total    {@code long} indicating total progress to be made.
     * @author Stephan de Jongh
     */
    public void update(long progress, long total) {
        if (progress < 0 || total < 1 || progress > total) {
            throw new IllegalArgumentException("Progress invalid.");
        }

        double fraction = (double) progress / total;
        double percentage = fraction * 100;
        int completed = (int) (fraction * segments);
        int remainder = segments - completed;

        System.out.print(draw(percentage, completed, remainder));
    }

    /**
     * Redraws the {@code ProgressBar} amount of completed {@code segments}
     * in relation to total amount of {@code segments}.
     *
     * @param percentage Percentage to be displayed next to the {@code ProgressBar}.
     * @param completed  Amount of {@code segments} of the total completed thus far.
     * @param remainder  Amount of {@code segments} still remaining till completion.
     * @author Stephan de Jongh
     */
    private String draw(double percentage, int completed, int remainder) {
        StringBuilder sb = new StringBuilder("\rPROGRESS [");
        for (int i = 0; i < completed; i++) {
            sb.append('☻');
        }
        for (int i = 0; i < remainder; i++) {
            sb.append('☺');
        }
        sb.append(String.format("] %.1f%% ", percentage));
        if (completed == segments) {
            sb.append("DONE\n");
        }
        return sb.toString();
    }
}
