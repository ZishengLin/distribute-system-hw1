


import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class app {
    static class Run extends Thread {
        int num;
        String address;
        int successfulRequest;
        ArrayList<Long> ls = new ArrayList<>();
        public Run(int num, String address) {
            this.num = num;
            this.address = address;
            successfulRequest = 0;
        }
        public void run() {
            Client client = ClientBuilder.newClient();
            Invocation.Builder ib  = client.target(address)
                    .request(MediaType.TEXT_PLAIN);
            for (int i = 0; i < num; i++) {
                long startTime=System.currentTimeMillis();
                myGet(ib);
                long endTime = System.currentTimeMillis();
                ls.add(endTime - startTime);
            }
            for (int i = 0; i < num; i++) {
                long startTime=System.currentTimeMillis();
                myPost(ib);
                long endTime = System.currentTimeMillis();
                ls.add(endTime - startTime);
            }
            client.close();
        }

        public void myGet(Invocation.Builder ib) {


           Response response = ib.get();
            int status = response.getStatus();
            if (status == 200) {
                successfulRequest++;
            }
            //System.out.println(status);
            //System.out.println(name);
        }
        public void myPost(Invocation.Builder ib) {
            Response response = ib
                    .post(Entity.entity("\"information\"",
                            MediaType.TEXT_PLAIN));
            int status = response.getStatus();
            if (status == 200) {
                successfulRequest++;
            }
           // System.out.println(status);
            //String name = response.readEntity(String.class);

        }


    }
    public static resultType warmUp(int numOfThread, int numOfEachThread, String address) throws InterruptedException {
        long startTime=System.currentTimeMillis();
        ArrayList<Run> ls = new ArrayList<Run>();
        BlockingQueue<Runnable> bqueue = new ArrayBlockingQueue<Runnable>(100);
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(100, 100, 100, TimeUnit.MILLISECONDS, bqueue);
        System.out.println("Warmup phase  "+ numOfThread*0.1 +" threads");
        for (int i = 0; i < numOfThread * 0.1; i++) {
            Run t = new Run(numOfEachThread, address);
            threadPool.execute(t);
            ls.add(t);
        }
//        for (Run t: ls) {
//            t.start();
//        }
//        for (Run t: ls) {
//            t.join();
//        }
        threadPool.shutdown();
        try {
            threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long endTime=System.currentTimeMillis();
        System.out.println("Warmup phase complete: Time "+ (endTime-startTime)/1000 +" ms");
        long res = 0;
        ArrayList<Long> delay = new ArrayList<>();
        for (Run t: ls) {
            delay.addAll(t.ls);
            res += t.successfulRequest;
        }
        return new resultType(res, (long)(numOfThread*0.1), delay, endTime-startTime);
    }

    public static resultType loading(int numOfThread, int numOfEachThread, String address) throws InterruptedException {
        long startTime=System.currentTimeMillis();
        ArrayList<Run> ls = new ArrayList<Run>();
        BlockingQueue<Runnable> bqueue = new ArrayBlockingQueue<Runnable>(100);
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(100, 100, 100, TimeUnit.MILLISECONDS, bqueue);
        System.out.println("loading phase  "+ numOfThread*0.5 +" threads");
        for (int i = 0; i < numOfThread * 0.5; i++) {
            Run t = new Run(numOfEachThread, address);
            threadPool.execute(t);
            ls.add(t);
        }
//        for (Run t: ls) {
//            t.start();
//        }
//        for (Run t: ls) {
//            t.join();
//        }
        threadPool.shutdown();
        try {
            threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long endTime=System.currentTimeMillis();
        System.out.println("Loading phase complete: Time "+ (endTime-startTime)/1000 +" ms");
        long res = 0;
        ArrayList<Long> delay = new ArrayList<>();
        for (Run t: ls) {
            delay.addAll(t.ls);
            res += t.successfulRequest;
        }
        return new resultType(res, (long)(numOfThread * 0.5)*numOfEachThread, delay, endTime-startTime);

    }

    public static resultType peaking(int numOfThread, int numOfEachThread, String address) throws InterruptedException {
        long startTime=System.currentTimeMillis();
        ArrayList<Run> ls = new ArrayList<Run>();
        BlockingQueue<Runnable> bqueue = new ArrayBlockingQueue<Runnable>(100);
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(100, 100, 100, TimeUnit.MILLISECONDS, bqueue);
        System.out.println("peaking phase  "+ numOfThread +" threads");
        for (int i = 0; i < numOfThread; i++) {
            Run t = new Run(numOfEachThread, address);
            threadPool.execute(t);
            ls.add(t);
        }
//        for (Run t: ls) {
//            t.start();
//        }
//        for (Run t: ls) {
//            t.join();
//        }
        threadPool.shutdown();
        try {
            threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long endTime=System.currentTimeMillis();
        System.out.println("peaking phase complete: Time "+ (endTime-startTime)/1000 +" ms");
        long res = 0;
        ArrayList<Long> delay = new ArrayList<>();
        for (Run t: ls) {
            delay.addAll(t.ls);
            res += t.successfulRequest;
        }
        return new resultType(res, (long)(numOfThread)*numOfEachThread, delay, endTime-startTime);
    }

    public static resultType coolDown(int numOfThread, int numOfEachThread, String address) throws InterruptedException {
        long startTime=System.currentTimeMillis();
        ArrayList<Run> ls = new ArrayList<Run>();
        BlockingQueue<Runnable> bqueue = new ArrayBlockingQueue<Runnable>(100);
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(100, 100, 100, TimeUnit.MILLISECONDS, bqueue);
        System.out.println("cooldown phase  "+ numOfThread*0.25 +" threads");
        for (int i = 0; i < numOfThread * 0.25; i++) {
            Run t = new Run(numOfEachThread, address);
            threadPool.execute(t);
            ls.add(t);
        }
//        for (Run t: ls) {
//            t.start();
//        }
//        for (Run t: ls) {
//            t.join();
//        }
        threadPool.shutdown();
        try {
            threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long endTime=System.currentTimeMillis();
        System.out.println("Cooldown phase complete: Time "+ (endTime-startTime)/1000 +" ms");
        long res = 0;
        ArrayList<Long> delay = new ArrayList<>();
        for (Run t: ls) {
            delay.addAll(t.ls);
            res += t.successfulRequest;
        }
        return new resultType(res, (long)(numOfThread*0.25)*numOfEachThread, delay, endTime-startTime);
    }

    public static void main(String[] args) throws InterruptedException {
        args = new String[] {"1000", "1", "http://ec2-34-209-180-211.us-west-2.compute.amazonaws.com:8080/simple-service-webapp/webapi/myresource", "8080"};
        int numOfThread = Integer.valueOf(args[0]);
        int numOfEachThread = Integer.valueOf(args[1]);
        String address = args[2];
        int port = Integer.valueOf(args[3]);
        System.out.println("Client starting timing: " + new Date().toString());


        long success = 0;
        long total = 0;
        long wall = 0;
        ArrayList<Long> delay = new ArrayList<>();

        resultType first = warmUp(numOfThread, numOfEachThread, address);
        success += first.first;
        total += first.second;
        delay.addAll(first.ls);
        wall += first.time;

        resultType second = loading(numOfThread, numOfEachThread, address);
        success += second.first;
        total += second.second;
        delay.addAll(second.ls);
        wall += second.time;

        resultType third = peaking(numOfThread, numOfEachThread, address);
        success += third.first;
        total += third.second;
        delay.addAll(third.ls);
        wall += third.time;

        resultType four = coolDown(numOfThread, numOfEachThread, address);
        success += four.first;
        total += four.second;
        delay.addAll(four.ls);
        wall += four.time;

        Collections.sort(delay);
        long sum = 0;
        for (long num: delay) {
            sum += num;
        }

        double average = sum*0.1 / delay.size();
        double median = delay.get(delay.size() / 2);
        System.out.println("================================================================");
        System.out.println("Total number of requests sent: "+ (int)(numOfEachThread*numOfThread*1.85));
        System.out.println("Total number of Successful responses: "+ success);
        System.out.println("Test Wall Time: "+wall/1000+" seconds");
        System.out.println("Overall throughput across all phases: " + total*1000*0.1 / wall);
        System.out.println("Mean: " + average);
        System.out.println("Median: " + median);
        System.out.println("99th latency: " + delay.get((int)(delay.size() * 0.99)));
        System.out.println("95th latency: " + delay.get((int)(delay.size() * 0.95)));

    }
}
class resultType {
    long first;
    long second;
    ArrayList<Long> ls;
    long time;
    public resultType(long first, long second, ArrayList<Long> ls, long time) {
        this.first = first;
        this.second = second;
        this.ls = ls;
        this.time = time;
    }
}