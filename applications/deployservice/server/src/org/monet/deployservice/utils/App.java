package org.monet.deployservice.utils;

public class App {
  public static String getStackTrace() {
    String result = "";
    StackTraceElement[] stack = Thread.currentThread().getStackTrace();
    for(int pos=stack.length - 1; pos > 1; pos--){
      StackTraceElement elem = stack[pos];
      String name = elem.getClassName().substring(elem.getClassName().lastIndexOf(".") + 1 );
//      System.out.print(name + "." + elem.getMethodName() + ":" + elem.getLineNumber());
      result += name + "." + elem.getMethodName() + ":" + elem.getLineNumber() + "\n";
//      if(pos > 2) System.out.print("->");
      if(pos > 2) result += "->";
    }
    return result;
  }
}
