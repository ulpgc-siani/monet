package org.monet.editor.core.parsers;

import java.util.StringTokenizer;

public abstract class CodeParser {
	private boolean stop;

	public CodeParser(String code) {
		execute(code);		
	}

	private void execute(String code) {
		StringTokenizer tokenizer = new StringTokenizer(code);
		this.stop = false;
		start();
		while (tokenizer.hasMoreTokens() && !stop) {
			String token = tokenizer.nextToken();
			if (token.equals("{")) {
				push();
				continue;
			} else if (token.equals("}")) {
				pop();
				continue;
			}
			analyze(token);
		}
	}
	
	protected abstract void start();
	protected void stop() {
		this.stop = true;
	}
	protected abstract void push();
	protected abstract void pop();
	protected abstract void analyze(String token);
}
