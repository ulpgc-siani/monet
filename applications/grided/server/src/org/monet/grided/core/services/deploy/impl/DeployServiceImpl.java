package org.monet.grided.core.services.deploy.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import org.monet.api.deploy.setupservice.DeploySetupApi;
import org.monet.api.deploy.setupservice.impl.DeploySetupApiMockImpl;
import org.monet.api.deploy.setupservice.impl.model.ServerState;
import org.monet.api.deploy.setupservice.impl.model.ServerState.Memory;
import org.monet.api.deploy.setupservice.impl.model.ServerState.Performance;
import org.monet.api.deploy.setupservice.impl.model.ServerState.Swap;
import org.monet.api.deploy.setupservice.impl.model.ServerState.Task;
import org.monet.api.deploy.setupservice.impl.model.SpaceManifest;
import org.monet.api.deploy.setupservice.impl.model.Ticket;
import org.monet.grided.core.model.Space;
import org.monet.grided.core.services.deploy.DeployService;

import com.google.inject.Inject;

public class DeployServiceImpl implements DeployService {
               
    private String certificateFilename;
    private String certificatePassword;
    
    @Inject
    public DeployServiceImpl(String certificateFilename, String certificatePassword) {                
        this.certificateFilename = certificateFilename;
        this.certificatePassword = certificatePassword;       
    }
    
    @Override
    public Ticket createSpace(String serverLocation, Space space) {
        DeploySetupApi api = this.getDeployApi(serverLocation);        
        String name = space.getName();
        String label = space.getData().getLabel();
        
        SpaceManifest manifest = new SpaceManifest();        
        manifest.setBaseUrl(space.getData().getUrl());
        manifest.setFederationName(space.getFederation().getName());
        manifest.setRequireDatawareHouse(space.getData().getDatawarehouse());
        
        return api.createSpace(name, label, manifest);
/*        
        String ip   = space.getFederation().getServer().getIp();
        String port = "4323";
        String cmd = "<command id=\"create_space\"><parameter id=\"name\">a</parameter><parameter id=\"label\">a</parameter><parameter id=\"url\">http://localhost:8091/a</parameter><parameter id=\"federation-name\">federationservice_user</parameter></command>";
        
        InetAddress destAddr;
        try {
            destAddr = InetAddress.getByName(ip);
            int destPort = Integer.parseInt(port);

            Socket sock = new Socket(destAddr, destPort);

            String SendMessage = cmd;

            System.out.println("Sending message (" + SendMessage.length() + " bytes): ");
            System.out.println(SendMessage);
            try {
                PrintWriter out = new PrintWriter(sock.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));

                out.println(SendMessage);

                String receiveMessage = in.readLine();

                System.out.println("Received message:");
                System.out.println(receiveMessage);
                
                sock.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (UnknownHostException e1) {
          System.out.println("Should raise a UnknownHostException");
        } // Destination address
        catch (IOException e) {
          System.out.println("Should raise a IOException");
        }
        
        return new Ticket();
*/        
    }
    
    @Override
    public Ticket removeSpace(String serverLocation, Space space) {
        
        String ip   = space.getFederation().getServer().getIp();
        String port = "4323";
        String cmd = "<command id=\"remove_space\"><parameter id=\"name\">a</parameter><parameter id=\"url\">http://localhost:8091/a</parameter></command>";
        
        InetAddress destAddr;
        try {
            destAddr = InetAddress.getByName(ip);
            int destPort = Integer.parseInt(port);

            Socket sock = new Socket(destAddr, destPort);

            String SendMessage = cmd;

            System.out.println("Sending message (" + SendMessage.length() + " bytes): ");
            System.out.println(SendMessage);
            try {
                PrintWriter out = new PrintWriter(sock.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));

                out.println(SendMessage);

                String receiveMessage = in.readLine();

                System.out.println("Received message:");
                System.out.println(receiveMessage);
                
                sock.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (UnknownHostException e1) {
          System.out.println("Should raise a UnknownHostException");
        } // Destination address
        catch (IOException e) {
          System.out.println("Should raise a IOException");
        }
        
        return new Ticket();
    }    
    
    
//    //private DeployServiceDriver driver;
//    
//    @Inject
//    public DeployServiceMockImpl(/*DeployServiceDriver driver*/) {
//        //this.driver = driver;
//    }
//    
    @Override
    public ServerState getServerState(String serverIP) {
        String log = "Información de log.....";
        ServerState state = new ServerState();
        
        Performance performance = new Performance("10.0", "99%", "102");
        Task task = new Task("10", "9", "1", "0", "0");
        Memory memory = new Memory("100", "2", "20");
        Swap swap = new Swap("100", "2", "20");
        
        state.setPerformance(performance);
        state.setTask(task);
        state.setMemory(memory);
        state.setSwap(swap);
        state.setLog(log);
        
        return state;
    }
        
    private DeploySetupApi getDeployApi(String url) {
        return new DeploySetupApiMockImpl(url, this.certificateFilename, this.certificatePassword);        
    }
}