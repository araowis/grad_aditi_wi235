# Bike Racing:

## Differences between BikeRacing and BikeRacingDynamicRacers:

- Fixed vs. Dynamic racer count:

BikeRacing: Hardcoded for exactly 10 racers (racers = new RaceBike[10])
BikeRacingDynamicRacers: Supports dynamic number of racers (array size 100, actual count from user input)

- Input handling:

BikeRacing: Only asks for course length
BikeRacingDynamicRacers: Asks for both course length AND number of bikers

- Countdown timer:

BikeRacing: No countdown, just prints "Race starts!"
BikeRacingDynamicRacers: Has a 3-second countdown (1... 2... 3... Race starts!)

- Race bike sleep duration:

BikeRacing: rand.nextInt(100) * 100 (0.1s to 10s)
BikeRacingDynamicRacers: rand.nextInt(100) * 10 (0.1s to 1s) — faster

- Loop iterations:

BikeRacing: All loops hardcoded to 10 (initializeRacers, startRace, displayRanking loops)
BikeRacingDynamicRacers: All loops use this.numberOfRacers variable