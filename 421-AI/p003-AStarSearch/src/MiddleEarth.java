/* Alex Pendell
 * CIS 421 - Artificial Intelligence
 * Assignment 3: A*Search -- Travelin' In Middle-earth
 * October 17th, 2019
 *
 * The MiddleEarth class is used as our 'graph'. This is the set
 * of all cities used in the problem. 
 */
 
import java.util.*;
import java.io.*;
 
public class MiddleEarth {
    
    // We store our cities in an array.
    // ME == Middle Earth... For those that didn't know.
    City[] ME;
    
    /* This is the City class (analagous to the node in a graph)
     * The primary function of this class is to give us a location to store data
     * pertaining to cities (nodes). We will need to store:
     *  - the name of the city,
     *  - the distance to home (Iron Hills)
     *  - and the set of roads leading out of the city 
     */
    
    /* This is where the work is done in creating the graph that will act as
     * our Middle-Earth.
     * parameters:
     *  File file: This is the file that our source information will be drawn
          from. *NOTE* I created this file. It is in super specific format to
          simplify the creation process. There is more information about the 
          structure of the data file in the readme.
     * postconditions:
     *  Middle-Earth will have been created! (We created the graph!)
     */
    public MiddleEarth(File file)throws FileNotFoundException{

        // We store our locations in an array of linked lists.
        // We use 25 because that's the number of locations in ME we have.
        ME = new City[25];
        
        Scanner sc = new Scanner(file) ;
        String line;
        line = sc.nextLine();
        // This loop will initialize our cities with (mostly) empty values.
        // When a city is created in this loop, the only thing it's given
        // is a name and a distance to home value. We fill in the information
        // about the roads in a later while loop.
        int index = 0;

        for(int i = 0; i < 25; i++) {
            Scanner sc2 = new Scanner(line);
            String cityName = sc2.next();
            int distToHome = Integer.parseInt(sc2.next());
            
            ME[i] = new City(cityName.toUpperCase(), distToHome);
            line = sc.nextLine();
        }
        
        // Advance the scanner off the "END_INIT" line.
        line = sc.nextLine();

        // We COULD hard-code this like the for-loop above, but this allows us
        // to add roads in the future should we want to.
        while (!line.equals("EOF")){
            Scanner sc2 = new Scanner(line);
            //String city1 = sc2.next();
            //String city2 = sc2.next();

            // The two cities we are connecting a road between.
            int city1pos = getCityPos(sc2.next().toUpperCase());
            int city2pos = getCityPos(sc2.next().toUpperCase());
            
            int dist = Integer.parseInt(sc2.next()); // Distance
            int qual = Integer.parseInt(sc2.next()); // Quality
            int risk = Integer.parseInt(sc2.next()); // Risk Level
            int wint = Integer.parseInt(sc2.next()); // Risk Level in winter

            // We've created the city, and we can reference it by name, we now
            // need to construct the necessary roads via linked lists within
            // the City objects.

            // This will add the necessary road between the two cities.
            ME[city1pos].addRoad(city2pos, dist, qual, risk, wint);
            // Since roads are bidirectional, we need to add the necessary road
            // going the other way as well
            // ME[city2pos].addRoad(city1pos, dist, qual, risk, wint);
            
            line = sc.nextLine();
        }
    }

    /* I need a way to find a city's index only given a name. This will
     * iterate through the array of locations and find the city name and
     * return it's index. This is public for now, because the Dwarves should
     * be able to know what index they're on (which location they're at).
     * parameters:
     *   String cityName: The name of the city that's searched for.
     * returns: An int that represents the index within ME that corresponds
     *   to our city.
     */
    public int getCityPos(String cityName) {
        for(int i = 0; i < 25; i++){
            if(ME[i].toString().equals(cityName))
                return i;
        }
        // The error is over 9000!!
        // We shouldn't ever get here because we never have to search for
        // a city that doesn't exist.
        return 9001;
    }

    public void printMap(){         /* MMMMM PROGRESS FEEELLS SOOOO GOOOOOOODDD */
        for(int i = 0; i < 25; i++){
            System.out.println(ME[i].toString() + ". D to IRONHILLS: " + ME[i].distanceToIronHills);
            Iterator<Road> it = ME[i].roads.iterator();
            while(it.hasNext()){
                Road road = it.next();
                System.out.println("-->" + ME[road.destination].toString() +
                                   ". D to IRONHILLS: " + ME[road.destination].distanceToIronHills);
                System.out.println("----> Dist: " + road.distance + " Q: " +
                                   road.quality + " risk: " + road.risk);
            }
            System.out.println();
        }
    }

     
     
    public class City {

        // These are used for the AStar algorithm to help isolate and print the path taken
        City parent = null;
        City next = null;

        int distanceFromStart;
        
        String name;
        int distanceToIronHills;
        LinkedList<Road> roads;
        int f;
        
        /* Constructor */
        private City(String name, int distToIronHills) {
            this.name = name;
            this.distanceToIronHills = distToIronHills;
            roads = new LinkedList<Road>();
        
        }
        
        /* A function to return the name of the city.
         */
        public String toString(){
            return this.name;
        }

        /* This function will add a new road leading out of the city with the
         * desired parameters.
         * parameters:
         *  City dest: the destination city this road leads to.
         *  int qual: the quality of the road
         *  int rsk: the risk involved in taking the road
         *  int wnt: the risk involved with taking this road during winter.
         * postconditions:
         *  the created road gets added to the roads set.
         */
        private void addRoad(int dest, int dist, int qual, int rsk, int wnt){
            this.roads.add(new Road(dest, dist, qual, rsk, wnt));
        }        
    }
    
    /* The road class is analogous to edges on a graph.
     * For the purposes of this assignment, each road must have the
     * following features:
     *  source City,
     *  destination City,
     *  distance,
     *  quality rating,
     *  risk rating,
     *  and a risk rating for during winter.
     */
    public class Road {
        
        int destination;
        
        int distance;
        int quality;
        int risk;
        int winterRisk;

        
        /* Constructor */
        // I'm not convinced we need to track the source. Because each city will have
        // it's own set of roads leading out of it, therefore the city will be it's own
        // source...
        private Road(/*City src,*/ int dest, int dist, int qual, int rsk, int wnt){
            //this.source = src;
            this.destination = dest;
            this.distance = dist;
            this.quality = qual;
            this.risk = rsk;
            this.winterRisk = wnt;
          
        }
    }        
}
 
