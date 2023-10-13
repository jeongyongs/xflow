package com.nhnacademy.aiot.node;

import java.util.Arrays;

import com.nhnacademy.aiot.message.Message;
import com.nhnacademy.aiot.pipe.Pipe;

public class StdOutNode extends Node implements Outputable {
    Pipe[] pipes;
    
    /**
     * @param pipeLength StdOutNode의 pipe에 연결된 Node들의 갯수
     */
    public StdOutNode(int pipeLength) {
        pipes = new Pipe[pipeLength];
        Arrays.stream(pipes).forEach(pipe -> new Pipe());
    }

    @Override
    public void add(int inputPort, Message message) throws InterruptedException {
        pipes[inputPort].add(message);
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            // pipe에서 messageg 가져와서 sysout으로 출력하기
            for (Pipe pipe : pipes) {
                try {
                    System.out.print(pipe.get().toString());
                } catch (InterruptedException e) {
                    //
                }
            }
        }
    }
    
}
