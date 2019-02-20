package org.monet.editor.ui.framework;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

public class EditorContributionHandler implements IObjectActionDelegate {
	
	private ITextEditor editor;
	protected IDocument document;
	protected EditorSelection selection;
	
	public class EditorSelection {
		private String text;
		private final int offset;
		private int length;

		public EditorSelection(String selectedText, int offset, int length) {
			this.text = selectedText;
			this.offset = offset;
			this.length = length;
		}

		public String getText() {
			return text;
		}

		public int getOffset() {
			return offset;
		}

		public int getLength() {
			return length;
		}

	}

	@Override
	public void run(IAction action) {
		if (editor == null) return;
        IDocumentProvider provider = editor.getDocumentProvider();
        document = provider.getDocument(editor.getEditorInput());
		selection = getSelection(editor);
		execute();
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {

	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart part) {
		if (part instanceof ITextEditor) 
			editor = (ITextEditor) part;
	}

	
	private EditorSelection getSelection(IEditorPart editor) {
		try {
			final ISelection selection = editor.getEditorSite().getSelectionProvider().getSelection();
			final ITextSelection textSelection = (ITextSelection) selection;
			return new EditorSelection(textSelection.getText(), textSelection.getOffset(), textSelection.getLength());
		}
		catch (Exception e) {
			return new EditorSelection("", 0, 0);
		}
	}
	
	
	protected Shell getShell() {
		return editor.getSite().getWorkbenchWindow().getShell();
	}
	
	protected void insert(String text) {
		IDocumentProvider documentProvider = editor.getDocumentProvider();
		IDocument document = documentProvider.getDocument(editor.getEditorInput());

		try {
			document.replace(selection.getOffset(), selection.getLength(), text);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	
	
	protected void execute() {
		
	}
	
	
}
