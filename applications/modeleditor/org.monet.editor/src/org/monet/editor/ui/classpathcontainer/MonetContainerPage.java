package org.monet.editor.ui.classpathcontainer;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.ui.wizards.IClasspathContainerPage;
import org.eclipse.jdt.ui.wizards.IClasspathContainerPageExtension;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.monet.editor.core.classpath.MonetContainer;
import org.eclipse.swt.widgets.Label;

/**
 * This classpath container page colects the directory and the file extensions for a new 
 * or existing SimpleDirContainer.  
 * 
 * @author Aaron J Tarter
 */
public class MonetContainerPage extends WizardPage 
               implements IClasspathContainerPage, IClasspathContainerPageExtension {

    private Composite composite;

    /**
     * Default Constructor - sets title, page name, description
     */
    public MonetContainerPage() {
        super("", "", null);
        setTitle("Monet Libraries");
        setDescription("This read-only container manages Monet project dependencies.");
        setPageComplete(true);
    }
    
    
    /* (non-Javadoc)
     * @see org.eclipse.jdt.ui.wizards.IClasspathContainerPageExtension#initialize(org.eclipse.jdt.core.IJavaProject, org.eclipse.jdt.core.IClasspathEntry[])
     */
    public void initialize(IJavaProject project, IClasspathEntry[] currentEntries) {
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    public void createControl(Composite parent) {
        composite = new Composite(parent, SWT.NULL);
        composite.setLayout(new GridLayout());
        composite.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_FILL
                | GridData.HORIZONTAL_ALIGN_FILL));
        composite.setFont(parent.getFont());
        
        setControl(composite);
        
        Label lblThisLibraryProvides = new Label(composite, SWT.NONE);
        lblThisLibraryProvides.setText("This library provides all required dependencies for Monet projects.");
    }
        
    /* (non-Javadoc)
     * @see org.eclipse.jdt.ui.wizards.IClasspathContainerPage#finish()
     */
    public boolean finish() {       
        return true;        
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.ui.wizards.IClasspathContainerPage#getSelection()
     */
    public IClasspathEntry getSelection() {
        IPath containerPath = MonetContainer.ID;
        return JavaCore.newContainerEntry(containerPath);
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.ui.wizards.IClasspathContainerPage#setSelection(org.eclipse.jdt.core.IClasspathEntry)
     */
    public void setSelection(IClasspathEntry containerEntry) {
    }    
}
