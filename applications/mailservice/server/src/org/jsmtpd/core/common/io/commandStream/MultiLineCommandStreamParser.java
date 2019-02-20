package org.jsmtpd.core.common.io.commandStream;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import org.jsmtpd.core.common.io.BareLFException;
import org.jsmtpd.core.common.io.BufferedWireReader;
import org.jsmtpd.core.common.io.InputSizeToBig;
import org.jsmtpd.tools.ByteArrayTool;


public class MultiLineCommandStreamParser {
    private List<byte[]> partBuffer = new LinkedList<byte[]>();
    private BufferedWireReader rd;
    private boolean rejectBareLF;
    
    public MultiLineCommandStreamParser(InputStream is, int maxMessageSize, boolean rejectBareLF) {
        this.rejectBareLF = rejectBareLF;
        rd= new BufferedWireReader (512,ByteArrayTool.LF,is,maxMessageSize);
    }

    public String readLine() throws InputSizeToBig, IOException, BareLFException {
        byte[] data = getData();
        
        if (data.length<3)
            return "";
        if ((data[data.length-2]!=ByteArrayTool.CR[0]) && rejectBareLF)
            throw new BareLFException();
        
        String res = new String (data);
        res=res.replaceAll("\r","");
        res=res.replaceAll("\n","");
        return res;
    }
    
    private byte[] getData () throws InputSizeToBig, IOException, BareLFException{
        byte[] data;
        if (partBuffer.size()==0)
            fillBuffer();
        if (partBuffer.size()==0)
            throw new IOException();
        data = partBuffer.get(0);
        partBuffer.remove(0);
        return data;
    }
    
    private void fillBuffer () throws InputSizeToBig, IOException, BareLFException{
        byte[] data = rd.readBlock();
        if (data==null)
            throw new IOException("Null stream");
        
        List<byte[]> tmp = ByteArrayTool.split(data,ByteArrayTool.LF[0]);
        for (int i=0;i<tmp.size();i++) {
            partBuffer.add(tmp.get(i));
        }
    }
}
