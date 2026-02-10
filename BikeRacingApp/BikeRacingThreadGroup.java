
import java.util.*;
import java.time.*;

class RaceCourse {
    public int courseLength;
    public static int ranking = 0;

    public void setCourseLength(int courseLength) {
        this.courseLength = courseLength;
    }

    public int getCourseLength() {
        return this.courseLength;
    }

    public static int updateRanking() {
        RaceCourse.ranking += 1;
        return RaceCourse.ranking;
    }
}

class RaceBike extends Thread {
    public String bikerName;
    protected int currentLocation;
    protected Instant startTime;
    protected Instant endTime;
    protected int ranking;
    protected int courseLength;
    protected int laneNumber;
    public boolean ready;

    public RaceBike(ThreadGroup group, String bikerName, int courseLength, int laneNumber) {
        // super to get ThreadGroup methods, to be able to run group.suspend()
        super(group, bikerName);
        this.bikerName = bikerName;
        this.currentLocation = 0;
        this.startTime = Instant.now();
        this.endTime = Instant.now();
        this.courseLength = courseLength;
        this.ranking = 0;
        this.laneNumber = laneNumber;
        this.ready = false;
    }

    Random rand = new Random();

    public void run() {
        System.out.println("Racer " + this.bikerName + " is ready.");
        this.suspend();
        this.ready = true;
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
        synchronized (RaceCourse.class) {
            this.ranking = RaceCourse.updateRanking();
        }
        this.endTime = Instant.now();
    }
}

class Race implements Runnable {
    RaceBike[] racers;
    RaceCourse course;
    protected int laneNumber;
    protected int numberOfRacers;
    public static volatile boolean boardRefreshRequested;
    ThreadGroup group;
    public boolean startRace = false;

    public Race() {
        this.racers = new RaceBike[100];
        this.course = new RaceCourse();
        this.laneNumber = 0;
        this.numberOfRacers = numberOfRacers;
        this.boardRefreshRequested = false;
        this.group = new ThreadGroup("racers");
    }

    public void setNumberofRacers(int numberOfRacers) {
        this.numberOfRacers = numberOfRacers;
    }

    public void initializeRace() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Kindly enter the length of the course: ");
        int courseLength = Integer.parseInt(sc.nextLine());
        this.course.setCourseLength(courseLength);
        System.out.println("Kindly enter the number of bikers.");
        int numberOfRacers = Integer.parseInt(sc.nextLine());
        setNumberofRacers(numberOfRacers);
    }

    public void initializeRacers() throws InterruptedException {
        Scanner sc = new Scanner(System.in);        
        for (int i = 0; i < this.numberOfRacers; i++) {
            // allow the previous biker to initialize
            Thread.sleep(100);
            System.out.println("Enter biker name:");
            String name = sc.nextLine();
            RaceBike r = new RaceBike(this.group, name, this.course.courseLength, ++this.laneNumber);
            r.start();
            racers[i] = r;
        }
    }

    public void raceFormalities() throws InterruptedException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Kindly press the enter key to start the game.");
        sc.nextLine();
        for (int i = 0; i < 3; i++) {
            System.out.print((i+1) + "... ");
            Thread.sleep(1000);
        }
        System.out.println("Race starts!");
        for (int i = 0; i < this.numberOfRacers; i++) {
            System.out.println();
        }
    }

    public void startRace() {
        // for (RaceBike r : racers) {
        //     try {
        //         r.startTime = System.nanoTime();
        //         r.start();
        //     } catch (Exception e) {
        //         e.printStackTrace();
        //     }
        // }

        group.resume();

        for (int i = 0; i < this.numberOfRacers; i++) {
            try {
                racers[i].join();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println("Race complete!");

    }

    public void run() {
        while (true) {
            if (boardRefreshRequested) {
                synchronized (Race.class) {
                    refreshDisplay();
                    boardRefreshRequested = false;
                }
            }
            try {
                Thread.sleep(100);
            } 
            catch (Exception e) {
                System.out.println(e);
            }
        }
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
                if (racers[i].ranking > racers[j].ranking) {
                    temp = racers[i];
                    racers[i] = racers[j];
                    racers[j] = temp;
                }
            }
        }
        for (int i = 0; i < this.numberOfRacers; i++) {
            System.out.println("Biker " + racers[i].bikerName + " is at " + racers[i].ranking + " position; Start time: " + racers[i].startTime + " End time: " + racers[i].endTime +"; Time taken for lap: " + Duration.between(racers[i].startTime, racers[i].endTime));
        }
    }
}

public class BikeRacingThreadGroup {
    public static void main(String[] args) throws InterruptedException{
        Scanner sc = new Scanner(System.in);

        Race race = new Race();
        Thread displayThread = new Thread(race);

        race.initializeRace();
        race.initializeRacers();
        race.raceFormalities();       

        displayThread.start();
        race.startRace();

        displayThread.interrupt();
        race.displayRanking();
    }
}
