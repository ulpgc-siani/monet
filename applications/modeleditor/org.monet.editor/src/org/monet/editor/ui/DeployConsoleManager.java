package org.monet.editor.ui;

import java.io.OutputStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.monet.editor.library.StreamHelper;

public class DeployConsoleManager {
	private MessageConsole console;
	
	private DeployConsoleManager(String deployName) {
		console = new MessageConsole(String.format("Deploying %s...", deployName), null);
	    Font font = new Font(Display.getCurrent(), "Courier New", 10, SWT.NORMAL);
	    console.setFont(font);
		console.activate();
		ConsolePlugin.getDefault().getConsoleManager().addConsoles(new IConsole[]{ console });
	}
	
	public static DeployConsoleManager buildNew(String deployName) {
		return new DeployConsoleManager(deployName);
	}
	
	public void println(String message, Object ... args) {
		this.internalPrint(false, true, message, args);
	}
	
	public void print(String message) {
	  this.internalPrint(false, false, message);
	}
	
	public void error(String message, Object ... args) {
	  this.internalPrint(true, true, message, args);
	}
	
	private void internalPrint(final boolean isError, final boolean isLine, final String message, final Object ... args) {
	  Display.getDefault().asyncExec(new Runnable() {
      public void run() {
        MessageConsoleStream stream = console.newMessageStream();
        if(isError)
          stream.setColor(new Color(Display.getDefault(), 255, 0, 0));
        
        String finalMessage = args != null && args.length > 0 ? String.format(message, args) : message;
        if(isLine)
          stream.println(finalMessage);
        else
          stream.print(finalMessage);
        StreamHelper.close(stream);
      }
	  });
	}
	
	public OutputStream getOutputStream() {
	  return this.console.newMessageStream();
	}

  public void close() {
//    this.console.matcherFinished();
    this.console.partitionerFinished();
    //this.console.destroy();
  }
	
}
