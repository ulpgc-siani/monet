package org.monet.editor.merger.textfilemerger;

import org.monet.editor.merger.textfilemerger.textfile.TextFile;

public class TextFileMerger {

    private SequenceDetector detector;
    private TextFileIterator original;
    private TextFileIterator proposed;

    public TextFileMerger(TextFile originalTextFile, TextFile proposedTextFile) {
        detector = new SequenceDetector(originalTextFile.toArray(), proposedTextFile.toArray());
        original = new TextFileIterator(originalTextFile);
        proposed = new TextFileIterator(proposedTextFile);
    }

    public TextFile execute() {
        TextFile result = merge();
        include(original, result);
        include(proposed, result);
        return result;
    }
	
    private TextFile merge() {
        TextFile result = new TextFile();
        while (true) {
            if (proposed.isFinished() || original.isFinished()) return result;

            if (matchLines()) {
                addOriginalLine(result);
                proposed.next();
            }
            else if (compareSequence() >= 0)
                addOriginalLine(result);
            else
                addProposedLine(result);
        }
    }	

    private void include(TextFileIterator source, TextFile result) {
        while (!source.isFinished()) {
                result.add(source.line());
                source.next();
        }
    }	
	
    private void addProposedLine(TextFile result) {
        result.add(proposed.line().withComment());
        proposed.next();
    }

    private void addOriginalLine(TextFile result) {
        result.add(original.line());
        original.next();
    }

    private boolean matchLines() {
        return original.line().equals(proposed.line());
    }

    private int compareSequence() {
        return detector.compareSequence(original.getIndex(), proposed.getIndex());
    }

}
