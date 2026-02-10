// executor service allows us to reuse threads and not init our own threads
// we do not have to worry about the lifecycle of the threads, 
// we just have to submit tasks to the executor service and it will take care of the rest

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.time.*;

class RaceCourse {
    public int courseLength;
    public static AtomicInteger ranking = new AtomicInteger(0);

    public void setCourseLength(int courseLength) {
        this.courseLength = courseLength;
    }

    public int getCourseLength() {
        return this.courseLength;
    }

    public static int updateRanking() {
        return RaceCourse.ranking.incrementAndGet();
    }
}

class RaceBike implements Runnable {
    public String bikerName;
    protected int currentLocation;
    protected Instant startTime;
    protected Instant endTime;
    protected AtomicInteger ranking;
    protected int courseLength;
    protected int laneNumber;
    private CyclicBarrier startBarrier;

    public RaceBike(String bikerName, int courseLength, int laneNumber, CyclicBarrier startBarrier) {
        this.bikerName = bikerName;
        this.currentLocation = 0;
        this.startTime = Instant.now();
        this.endTime = Instant.now();
        this.courseLength = courseLength;
        this.ranking = new AtomicInteger(0);
        this.laneNumber = laneNumber;
        this.startBarrier = startBarrier;
    }

    Random rand = new Random();

    public void run() {
        System.out.println("Racer " + this.bikerName + " is ready.");
        
        try {
            // wait for all racers
            startBarrier.await();
        } catch (Exception e) {
            System.out.println(e);
        }

        this.startTime = Instant.now();

        while (this.currentLocation < this.courseLength) {
            try {
                // a random number between 100-1000 (0.1s to 1s)
                Thread.sleep((rand.nextInt(100) * 10));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // we take a random amount of time to cover a fixed 100m
            this.currentLocation += 100;
            Race.refreshRequest();
        }
        this.ranking.set(RaceCourse.updateRanking());
        this.endTime = Instant.now();
    }
}

class Race implements Runnable {
    RaceBike[] racers;
    RaceCourse course;
    protected int laneNumber;
    protected int numberOfRacers;
    public static volatile boolean boardRefreshRequested;
    private CyclicBarrier startBarrier;
    private ExecutorService racerExecutor;
    private ExecutorService displayExecutor;
    public boolean startRace = false;

    public Race() {
        this.racers = new RaceBike[100];
        this.course = new RaceCourse();
        this.laneNumber = 0;
        this.numberOfRacers = 0;
        this.boardRefreshRequested = false;
    }

    public void setNumberofRacers(int numberOfRacers) {
        this.numberOfRacers = numberOfRacers;
        System.out.println("Number of racers set to: " + this.numberOfRacers);
    }

    public void initializeRace() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Kindly enter the length of the course: ");
        int courseLength = Integer.parseInt(sc.nextLine());
        this.course.setCourseLength(courseLength);
        System.out.println("Kindly enter the number of bikers. (Please do not enter more than 10)");
        int numberOfRacers = Integer.parseInt(sc.nextLine());
        setNumberofRacers(numberOfRacers);
        // basically all racers have to get to the cyclic barrier 
        // 1 for the display thread, once they're all ready, print the barrier action, which is basically the ready flag.
        this.startBarrier = new CyclicBarrier(this.numberOfRacers + 1, () -> System.out.println("All Racers ready!"));
        this.racerExecutor = Executors.newFixedThreadPool(this.numberOfRacers);
    }

    public void initializeRacers() throws InterruptedException {   
        Scanner sc = new Scanner(System.in);
        for (int i = 0; i < this.numberOfRacers; i++) {
            // allow the previous biker to initialize
            Thread.sleep(100);
            System.out.println("Enter biker name:");
            String name = sc.nextLine();
            RaceBike r = new RaceBike(name, this.course.courseLength, ++this.laneNumber, this.startBarrier);
            racers[i] = r;
            racerExecutor.submit(r);
        }
    }

    public void raceFormalities() throws InterruptedException {
        System.out.println("Kindly press the enter key to start the game.");
        Scanner sc = new Scanner(System.in);
        sc.nextLine();
        for (int i = 0; i < 3; i++) {
            System.out.print((i+1) + "... ");
            Thread.sleep(1000);
        }
        System.out.println("Race starts!");
        for (int i = 0; i < this.numberOfRacers; i++) {
            System.out.println();
        }
        sc.close();
    }

    public void startRace() {
        try {
            startBarrier.await();
        } catch (Exception e) {
            System.out.println(e);
        }

        racerExecutor.shutdown();
        try {
            if (!racerExecutor.awaitTermination(5, TimeUnit.MINUTES)) {
                racerExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            racerExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        System.out.println("Race complete!");
    }

    public void refreshDisplay() {
        // move the cursor to the left
        System.out.print("\033[" + this.numberOfRacers + "A");
        for (int i = 0; i < this.numberOfRacers; i++) {
            System.out.print("\033[K");
            System.out.print("Biker " + racers[i].bikerName + ": ");
            for (int j = 0; j < racers[i].currentLocation / 100; j++) {
                System.out.print(".");
            }
            System.out.println();
        }
    }

    public static void refreshRequest() {
        Race.boardRefreshRequested = true;
    }

    public void displayRanking() {
        RaceBike temp;
        System.out.println("Final rankings: ");        
        for (int i = 0; i < this.numberOfRacers; i++) {
            for (int j = i; j < this.numberOfRacers; j++) {
                if (racers[i].ranking.get() > racers[j].ranking.get()) {
                    temp = racers[i];
                    racers[i] = racers[j];
                    racers[j] = temp;
                }
            }
        }
        for (int i = 0; i < this.numberOfRacers; i++) {
            System.out.println("Biker " + racers[i].bikerName + " is at " + racers[i].ranking.get() + " position; Start time: " + racers[i].startTime + " End time: " + racers[i].endTime +"; Time taken for lap: " + Duration.between(racers[i].startTime, racers[i].endTime));
        }
    }

    public void startDisplayThread() {
        this.displayExecutor = Executors.newSingleThreadExecutor();
        this.displayExecutor.submit(this);
    }

    public void stopDisplayThread() {
        this.displayExecutor.shutdownNow();
        try {
            if (!this.displayExecutor.awaitTermination(2, TimeUnit.SECONDS)) {
                System.out.println("Display thread did not terminate gracefully");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            if (boardRefreshRequested) {
                refreshDisplay();
                boardRefreshRequested = false;
            }
            try {
                Thread.sleep(100);
            } 
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

public class BikeRacingExecutorService {
    public static void main(String[] args) throws InterruptedException{
        Race race = new Race();

        race.initializeRace();
        race.initializeRacers();
        race.raceFormalities();       

        race.startDisplayThread();
        race.startRace();
        race.stopDisplayThread();

        race.displayRanking();
    }
}
