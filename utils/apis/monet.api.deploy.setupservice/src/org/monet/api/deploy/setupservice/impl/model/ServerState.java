package org.monet.api.deploy.setupservice.impl.model;

import java.io.IOException;
import java.io.StringReader;

import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class ServerState {
   
   private String log;
   private Performance performance;
   private Memory memory;
   private Task task;
   private Swap swap;
      
   public ServerState() {
       this.log = "";
       this.performance = new Performance();
       this.task = new Task();
       this.memory = new Memory();
       this.swap = new Swap();
   }
   
   public ServerState(String log, Performance performance, Task task, Memory memory, Swap swap) {
       this.log = log;
       this.performance = performance;
       this.task = task;
       this.memory = memory;
       this.swap = swap;
   }
   
   public String getLog() {
       return this.log;
   }
   
   public void setLog(String log) {
       this.log = log;
   }
   
   public Performance getPerformance() {
       return this.performance;
   }
   
   public void setPerformance(Performance performance) {
       this.performance = performance;
   }
   
   public Task getTask() {
       return this.task;
   }
   
   public void setTask(Task task) {
       this.task = task;
   }
   
   public Memory getMemory() {
       return this.memory;
   }
   
   public void setMemory(Memory memory) {
       this.memory = memory;
   }
   
   public Swap getSwap() {
       return this.swap;
   }
   
   public void setSwap(Swap swap) {
       this.swap = swap;
   }
         
   public void deserializeFromXML(String content) throws JDOMException, IOException {
       SAXBuilder builder = new SAXBuilder();
       StringReader reader;
       org.jdom.Document document;
       Element node;

       if (content.isEmpty()) return;
       
       while (!content.substring(content.length() - 1).equals(">"))
         content = content.substring(0, content.length() - 1);

       reader = new StringReader(content);
       document = builder.build(reader);
       node = document.getRootElement();
       
       this.deserializeFromXML(node);
     }
   
   private void deserializeFromXML(Element status) {
       this.performance  = this.deserializePerformance(status.getChild("performance"));
       this.task         = this.deserializeTask(status.getChild("tasks"));
       this.memory       = this.deserializeMemory(status.getChild("memory"));
       this.swap         = this.deserializeSwap(status.getChild("swap"));
       this.log          = this.deserializeLog(status.getChild("log"));
   }
   

   private Performance deserializePerformance(Element element) {       
       if (element == null) return new Performance(); 
       return new Performance(element.getChild("load").getValue(), element.getChild("cpu").getValue(), element.getChild("users").getValue());
   }
   

   private String deserializeLog(Element element) {       
       if (element == null) return "";
       return element.getValue();
   }

   private Memory deserializeMemory(Element element) {
       if (element == null) return new Memory();
       return new Memory(element.getChild("total").getValue(), element.getChild("used").getValue(), element.getChild("available").getValue());
   }

   private Task deserializeTask(Element element) {
       if (element == null) return new Task();
       return new Task(element.getChild("total").getValue(), element.getChild("execution").getValue(), 
                       element.getChild("sleeped").getValue() , element.getChild("stopped").getValue(), element.getChild("zoombies").getValue());
   }
   
   private Swap deserializeSwap(Element element) {
       if (element == null) return new Swap();
       return new Swap(element.getChild("total").getValue(), element.getChild("used").getValue(), element.getChild("available").getValue());
   }

       
   
   public static class Performance  {       
       private final String load;
       private final String cpu;
       private final String users;
       
       public Performance() {
           this.load = "";
           this.cpu = "";
           this.users = "";
       }
       
       public Performance(String load, String cpu, String users) {
           this.load = load;
           this.cpu = cpu;
           this.users = users;
       }
       
       public String getLoad() { return this.load; }
       public String getCPU() { return this.cpu; }
       public String getUsers() { return this.users; } 
   }
   
   public static class Task  {       
       private final String total;
       private final String execution;
       private final String sleeped;
       private final String stopped;
       private final String zoombies;
              
       public Task() {
           this.total = "";
           this.execution = "";
           this.sleeped = "";
           this.stopped = "";
           this.zoombies = "";
       }       
       
       public Task(String total, String execution, String sleeped, String stopped, String zoombies) {
           this.total = total;
           this.execution = execution;
           this.sleeped = sleeped;
           this.stopped = stopped;
           this.zoombies = zoombies;
       }
       
       public String getTotal()     { return this.total; }
       public String getExecution() { return this.execution; }
       public String getSleeped()   { return this.sleeped; }
       public String getStopped()   { return this.stopped; }
       public String getZoombies()  { return this.zoombies; }
   }
   
   public static class Memory {       
       private final String total;
       private final String used;
       private final String available;
       
       public Memory() {
           this.total = "";
           this.used = "";
           this.available = "";
       }

       public Memory(String total, String used, String available) {
           this.total = total;
           this.used = used;
           this.available = available;
       }
       
       public String getTotal() { return this.total; }
       public String getUsed()  { return this.used; }
       public String getAvailable() { return this.available; }   
   }
   
   public static class Swap {       
       private final String total;
       private final String used;
       private final String available;
       
       public Swap() {
           this.total = "";
           this.used = "";
           this.available = "";
       }

       public Swap(String total, String used, String available) {
           this.total = total;
           this.used = used;
           this.available = available;
       }
       
       public String getTotal() { return this.total; }
       public String getUsed()  { return this.used; }
       public String getAvailable() { return this.available; }   
   }
}

