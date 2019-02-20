package org.monet.grided.control.actions.impl;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.monet.grided.control.actions.Action;
import org.monet.grided.core.serializers.json.JSONResponse;
import org.monet.grided.core.serializers.json.impl.JSONErrorResponse;
import org.monet.grided.core.serializers.json.impl.JSONSuccessResponse;


public abstract class BaseAction implements Action {

    public static void sendResponse(HttpServletResponse response, String content) {
        PrintWriter writer;
        try {
            response.setContentType("text/html;charset=UTF-8");			
            writer = response.getWriter();
            writer.println(content);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendResponse(HttpServletResponse response, JSONResponse jsonResponse) {
        PrintWriter writer;
        try {
            String content = jsonResponse.toString();
            response.setContentType("text/html;charset=UTF-8");         
            writer = response.getWriter();
            writer.println(content);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }     


    public static void sendResponse(HttpServletResponse response, JSONObject json) {
        PrintWriter writer;
        try {
            String content = new JSONSuccessResponse(json).toString();
            response.setContentType("text/html;charset=UTF-8");         
            writer = response.getWriter();
            writer.println(content);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }     

    public static void sendResponse(HttpServletResponse response, JSONArray json) {
        PrintWriter writer;
        try {
            String content = new JSONSuccessResponse(json).toString();
            response.setContentType("text/html;charset=UTF-8");         
            writer = response.getWriter();
            writer.println(content);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }    

    public static void sendErrorResponse(HttpServletResponse response, String content) {
        PrintWriter writer;
        try {
            response.setContentType("text/html;charset=UTF-8");         
            writer = response.getWriter();
            writer.println(content);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendErrorResponse(HttpServletResponse response, JSONObject json) {
        PrintWriter writer;
        try {
            String content = new JSONErrorResponse(json).toString();
            response.setContentType("text/html;charset=UTF-8");         
            writer = response.getWriter();
            writer.println(content);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }     

    @Override
    public abstract void execute(HttpServletRequest request, HttpServletResponse response);
}
