import java.util.*;


public class Main {

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) throws InterruptedException {

        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            Thread thread = new Thread(() -> {
                String route = generateRoute("RLRFR", 100);
                int count = 0;
                for (int j = 0; j < route.length(); j++) {
                    if (route.charAt(j) == 'R') count++;
                }
                synchronized (sizeToFreq) {
                    if (sizeToFreq.containsKey(count)) {
                        sizeToFreq.replace(count, sizeToFreq.get(count) + 1);
                    } else {
                        sizeToFreq.put(count, 1);
                    }
                }
            });
            threads.add(thread);
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }



        System.out.println("Самое частое количество повторений: " + maxFreq(sizeToFreq).getKey()
                + " (встретилось " + maxFreq(sizeToFreq).getValue() + " раз)");
        System.out.println("Другие размеры: " + "\n" + printOtherFreq(sizeToFreq));


    }

    public static Map.Entry<Integer, Integer> maxFreq(Map<Integer, Integer> map) {
        Map.Entry<Integer, Integer> max = map.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElse(null);
        return max;
    }

    public static String printOtherFreq(Map<Integer, Integer> map) {
        map.remove(maxFreq(map).getKey());
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Integer, Integer> m : map.entrySet()) {
            sb.append("- " + m.getKey() + " (" + m.getValue() + ")\n");
        }
        return sb.toString();
    }
}