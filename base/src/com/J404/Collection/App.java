import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class App {
    private static HashMap<String, People> hashMapTest = new HashMap<String, People>();
    private static ConcurrentHashMap<String, People> concurrentHashMapTest = new ConcurrentHashMap<String, People>();

    private static class ThreadOne extends Thread {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            System.out.println("ThreadOne run");
            // hashMapTest.forEach((key, value) -> {
            //     System.out.println(value);
            //     try {
            //         Thread.sleep(10);
            //     } catch (InterruptedException e) {
            //         // TODO Auto-generated catch block
            //         e.printStackTrace();
            //     }
            // });
            Iterator<Entry<String, People>> iterator = concurrentHashMapTest.entrySet().iterator();
            while (iterator.hasNext()) {
                Entry<String, People> entry = iterator.next();
                System.out.println(entry.getValue());
            }
        }
    }

    private static class ThreadTwo extends Thread {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            System.out.println("ThreadTwo run");
            int i = 1;
            while (i <= concurrentHashMapTest.size()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (i == 3) {
                    concurrentHashMapTest.remove("people" + i);
                }
                i++;
            }
        }
    }

    public static void main(String[] args) throws Exception {
        People people1 = new People("people1");
        People people2 = new People("people22");
        People people3 = new People("people333");
        People people4 = new People("people4444");
        People people5 = new People("people55555");
        People people6 = new People("people666666");
        // -----------------------------------------------------
        // TreeSet sortable non-repeating
        TreeSet<People> treeSet = new TreeSet<People>();
        treeSet.add(people1);
        treeSet.add(people2);
        treeSet.add(people3);
        treeSet.add(people4);
        treeSet.add(people5);
        treeSet.add(people6);
        System.out.println("treeSet: " + treeSet);
        System.out.println("-----------------------------------------------------");
        // ArrayList non-sortable repeating
        ArrayList<People> arrayList = new ArrayList<People>();
        arrayList.add(people1);
        arrayList.add(people3);
        arrayList.add(people1);
        arrayList.add(people4);
        arrayList.add(3, people6);
        System.out.println("arrayList: " + arrayList);
        System.out.println("-----------------------------------------------------");
        // HashMap
        HashMap<String, People> hashMap = new HashMap<String, People>();
        hashMap.put("people1", people1);
        hashMap.put("people2", people2);
        hashMap.put("people3", people3);
        hashMap.put("people4", people4);
        System.out.println("hashMap: " + hashMap);
        System.out.println("-----------------------------------------------------");
        // ConcurrentHashMap
        ConcurrentHashMap<String, People> concurrentHashMap = new ConcurrentHashMap<String, People>();
        concurrentHashMap.put("people1", people1);
        concurrentHashMap.put("people2", people2);
        concurrentHashMap.put("people3", people3);
        concurrentHashMap.put("people4", people4);
        System.out.println("concurrentHashMap: " + concurrentHashMap);
        System.out.println("-----------------------------------------------------");
        // Test HashMap and ConcurrentHashMap
        concurrentHashMapTest.put("people1", people1);
        concurrentHashMapTest.put("people2", people2);
        concurrentHashMapTest.put("people3", people3);
        concurrentHashMapTest.put("people4", people4);
        ThreadOne threadOne = new ThreadOne();
        ThreadTwo threadTwo = new ThreadTwo();
        try {
            // threadOne.start();
            // threadTwo.start();
        } catch (Exception e) {
            //TODO: handle exception
            System.out.println(e.getMessage());
        }
        System.out.println("-----------------------------------------------------");
        // LinkedHashMap
        LinkedHashMap<String, People> linkedHashMap = new LinkedHashMap<String, People>();
        linkedHashMap.put("people1", people1);
        linkedHashMap.put("people2", people2);
        linkedHashMap.put("people3", people3);
        linkedHashMap.put("people4", people4);
        linkedHashMap.forEach((key, value) -> {
            System.out.println(key + ": " + value);
        });
        System.out.println("-----------------------------------------------------");
        LRUCache<String, People> lruCache = new LRUCache<String, People>();
        lruCache.put("people1", people1);
        lruCache.put("people2", people2);
        lruCache.put("people3", people3);
        lruCache.get("people1");
        lruCache.put("people4", people4);
        lruCache.forEach((key, value) -> {
            System.out.println(key + ": " + value);
        });
        
    }
}

class People implements Comparable<People> {
    private String name;

    public People() {
    }

    public People(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        People other = (People) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "People [name=" + name + "]";
    }

    @Override
    public int compareTo(People o) {
        // TODO Auto-generated method stub
        if (this.name.length() > o.getName().length()) {
            return 1;
        } else if (this.name.length() < o.getName().length()) {
            return -1;
        } else {
            return 0;
        }
    }
}

class LRUCache<K, V> extends LinkedHashMap<K, V> {

    /**
     *
     */
    private final static long serialVersionUID = 1L;
    private final static int MAX_SIZE = 3;
    LRUCache() {
        super(MAX_SIZE, 0.75f, true);
    }

    @Override
    protected boolean removeEldestEntry(java.util.Map.Entry<K, V> eldest) {
        // TODO Auto-generated method stub
        return size() > MAX_SIZE;
    }
}