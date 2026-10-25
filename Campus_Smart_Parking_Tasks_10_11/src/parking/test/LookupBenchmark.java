package parking.test;
import parking.model.*;
import java.time.LocalDate;
import java.util.*;
/** Lookup-only microbenchmark; not a guarantee of real-world latency. */
public final class LookupBenchmark {
    private static volatile long sink;
    private static long bench(int kind, ParkingTicket[] array, ArrayList<ParkingTicket> list,
                              HashMap<String,ParkingTicket> map, String[] keys, int operations) {
        long acc = 0;
        long start = System.nanoTime();
        for (int j=0; j<operations; j++) {
            String key=keys[(j*73)%keys.length];
            ParkingTicket value=null;
            if (kind==0) {
                for (ParkingTicket t:array) if (t!=null && t.getVehicle().getRegistrationNumber().equals(key)) {value=t;break;}
            } else if (kind==1) {
                for (ParkingTicket t:list) if (t.getVehicle().getRegistrationNumber().equals(key)) {value=t;break;}
            } else value=map.get(key);
            if(value==null) throw new AssertionError("Missing key");
            acc += value.getSlotNumber();
        }
        sink=acc;
        return System.nanoTime()-start;
    }
    public static void main(String[] args) {
        int operations=30000;
        System.out.println("Warm-up: one short pass per structure; latency in nanoseconds per lookup");
        for(int n:new int[]{100,1000,5000}) {
            ParkingTicket[] arr=new ParkingTicket[n];
            ArrayList<ParkingTicket> list=new ArrayList<>(n);
            HashMap<String,ParkingTicket> map=new HashMap<>();
            String[] keys=new String[n];
            for(int i=0;i<n;i++) {
                String reg=String.format("TN%08d",i);
                keys[i]=reg;
                ParkingTicket ticket=new ParkingTicket(new Car(reg),1,i+1,0,LocalDate.of(2026,10,25),null);
                arr[i]=ticket;list.add(ticket);map.put(reg,ticket);
            }
            for(int k=0;k<3;k++) bench(k,arr,list,map,keys,1000);
            double[] ns=new double[3];
            for(int k=0;k<3;k++) ns[k]=bench(k,arr,list,map,keys,operations)/(double)operations;
            System.out.printf(Locale.ROOT,"n=%5d  array=%10.1f  ArrayList=%10.1f  HashMap=%10.1f%n",n,ns[0],ns[1],ns[2]);
        }
        if(sink==0)throw new AssertionError();
    }
}
