package com.nhnacademy.aiot.node;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.nhnacademy.aiot.message.Message;

public class StdInNode extends Node implements Inputable {
    Map<Integer, Target> targets; 
    Message message;
    
    public StdInNode() {
        targets = new HashMap<>();
    }
    
    /** 
     * scanner를 message로 형변환합니다.
     * 
     * @param scanner 입력을 받습니다.
     */
    public void scanToMessage(Scanner scanner) {
        message = new Message(10000);
        message.append("value", scanner.nextLine());
    }
    
    /**
     * StdInNode의 port와 target을 연결합니다.
     * 
     * @param outputPort StdInNode의 port로, 맵의 Integer로 들어갑니다.
     * @param target Node의 종류를 말하며, Map의 Target으로 들어갑니다.
     */
    @Override
    public void connect(int outputPort, Target target) {
        targets.put(outputPort,target);
    }
    
    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);

        scanToMessage(scanner);
       
        for (Target target : targets.values()) {
            try{
                target.add(message);
            } catch (InterruptedException e) {
                System.out.println("노드로 message 못 보냄\n" +e);
            }
        }
    }

}
