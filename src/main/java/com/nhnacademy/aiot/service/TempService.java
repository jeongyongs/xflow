package com.nhnacademy.aiot.service;

import com.nhnacademy.aiot.node.FunctionNode;
import com.nhnacademy.aiot.node.HttpNode;
import com.nhnacademy.aiot.node.ResponseNode;
import com.nhnacademy.aiot.pipe.Pipe;

public class TempService implements Service {
    private static final String HOSTNAME = "ems.nhnacademy.com";
    private static final int PORT = 1880;
    private static final String PATH = "/ep/temperature/24e124126c457594?count=1&st=1696772438&et="
            + (int) (System.currentTimeMillis() / 1000 - 30);
    private static final String RESPONSE = "response";
    private static final String TIME = "time";
    private static final String HUMIDITY = "humidity";
    private static final String CRLF = "\r\n";

    @Override
    public void execute(Pipe inPipe) {
        Pipe functionInpipe = new Pipe();
        Pipe functionNodeOutPipe = new Pipe();

        HttpNode httpNode = new HttpNode(HOSTNAME, PORT, PATH);
        httpNode.connectIn(0, inPipe);
        httpNode.connectOut(0, functionInpipe);

        FunctionNode functionNode = new FunctionNode(message -> {
            StringBuilder sb = new StringBuilder();
            int length = message.getString(RESPONSE).length();

            sb.append("HTTP/1.1 200 OK\r\n");
            sb.append("Content-Type: text/html; charset=utf-8\r\n");
            sb.append("Content-Length:" + length);
            sb.append(CRLF);
            sb.append("dateTime:" + message.getString(TIME).getBytes());
            sb.append(HUMIDITY + message.getString(HUMIDITY).getBytes());

            message.put(RESPONSE, sb);
            return message;
        });

        /*
         * {
         * "dateTime":"2023-10-05 15:41:13",
         * "humidity":39
         * } ==> 이렇게 들어오는 데이터를 가공처리 해서,
         * 
         * => 바디에 이렇게 넣어줘야 한다.
         * {
         * "dateTime":"2023-10-05 15:41:13",
         * "humidity":70
         * }
         */

        functionNode.connectIn(0, functionInpipe);
        functionNode.connectOut(0, functionNodeOutPipe);

        ResponseNode responseNode = new ResponseNode();

        responseNode.connectIn(0, functionNodeOutPipe);

        httpNode.start();
        functionNode.start();
        responseNode.start();
    }

}