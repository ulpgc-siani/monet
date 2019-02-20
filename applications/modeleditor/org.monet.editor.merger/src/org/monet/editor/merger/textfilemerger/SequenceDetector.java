package org.monet.editor.merger.textfilemerger;

public class SequenceDetector {

    private int[][] table;

    public SequenceDetector(Object[] original, Object[] proposed) {
        createTable(original, proposed);
    }

    public int compareSequence(int originalIndex, int proposedIndex) {
        if (originalIndex >= (originalSize() - 1))
            return -1;
        if (proposedIndex >= (proposedSize() - 1))
            return 1;

        //return Integer.signum(this.getValue(originalIndex + 1, proposedIndex) - this.getValue(originalIndex, proposedIndex + 1));
        return Integer.signum(this.getColMaxValue(proposedIndex) - this.getRowMaxValue(originalIndex));
    }

    private int originalSize() {
        return this.table.length;
    }

    private int proposedSize() {
        return this.table[0].length;
    }

    private int getValue(int row, int col) {
    	if ((row < 0) || (col < 0)) 
    		return 0;
    	
        return table[row][col];
    }

    private int getRowMaxValue(int row) {
    	return table[row][this.proposedSize() - 1];
    }
    
    private int getColMaxValue(int col) {
    	return table[this.originalSize() - 1][col];
    }
    
    private void createTable(Object[] original, Object[] proposed) {
    	this.table = new int[original.length][proposed.length];
        calculateTable(original, proposed);
    }

    private void calculateTable(Object[] original, Object[] proposed) {
        for (int row = 0; row < original.length; row++) {
            calculateRow(row, original[row], proposed);
        }
    }

    private void calculateRow(int row, Object object, Object[] proposed) {
        for (int col = 0; col < proposed.length; col++) {
            if (object.equals(proposed[col])) {
                table[row][col] = this.getValue(row - 1, col - 1) + 1;
            } else {
                table[row][col] = max(this.getValue(row, col - 1), this.getValue(row - 1, col));
            }
        }
    }

    private int max(int i1, int i2) {
        return (i1 > i2) ? i1 : i2;
    }
}
