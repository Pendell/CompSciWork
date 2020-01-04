/* Alex Pendell
 * CIS 421 -- Artificial Intelligence
 * Assignment 3: A*Search -- Travelin' In Middle-earth
 * October 17th, 2019
 *
 * This is the AStarSearch algorithm.
 * The algorithm functions very similarly to Djikstra's graph search.
 *
 * We begin by initializing two sets:
 *   1 for open cities -- cities we have available to us.
 *   2 closed cities -- cities we have currently visited.
 *
 * In the beginning, the only city in the closed city is the starting city.
 * The open city set contains all of the cities which the starting city connects to.
 * When considering the next city to travel to, we compare the f values of each city,
 *   and select the city with the lowest f value.
 * The parameters we consider when calculting f values depends on which version of the search
 *   we are running.
 *   0 -> Hueristic values only. f = distance travelled so far + distance to the destination
 *   1 -> 0's f value + road risk + (3*road quality) -> We favor better roads here.
 *   2 -> 0's f value + (3*road risk) + road quality -> We favor safer roads here.
 *
 * Once we have selected f values, the city with the lowest f value gets selected.
 * That city gets removed from the open list, and all of it's connections get added to the open.
 * At this point, we are also tracking which city we used to get to the current city. This way,
 * we can back track from Iron Hills and create our pathTaken variable to give us the needed output.
 */
import java.util.*;
import java.io.*;

public class AStarSearch {

    // The first thing we need to do is create Middle Earth for our
    // Dwarves to explore and run around!
    public static void main(String[] args) throws Exception {
        boolean winter = false;
        Scanner sc = new Scanner(System.in);
        int searchNum = 0;
        while(true){
            System.out.print("Is it winter? Y/N: ");
            String input = sc.next();
            input = input.toUpperCase();
            if(input.equals("Y")){
                winter = true;
                break;
            } else if (input.equals("N")){
                break;
            }
                
        }

        System.out.println("Which version of the search would you like to run? 0/1/2: ");
        System.out.println("0: The default. Uses base heuristics for AStar.");
        System.out.println("1/2: Take other variables into account...");
        System.out.println("\t1: Favors road quality more heavily");
        System.out.println("\t2: Favors safer roads");
        
        System.out.print("Please enter version #: ");           
        while(true){
            String input = sc.next();
            if(input.equals("0")){
                searchNum = Integer.parseInt(input);
                break;
            } else if (input.equals("1")){
                searchNum = Integer.parseInt(input);
                break;
            } else if (input.equals("2")){
                searchNum = Integer.parseInt(input);
                break;
            } else {
                System.out.print("Please enter a 0/1/2:");
            }
        }
        
        System.out.println();
        System.out.println();
        
        File file = new File("MiddleEarth.dat");
        MiddleEarth ME = new MiddleEarth(file);
        LinkedList<MiddleEarth.City> pathTaken = aStarSearch(ME, winter, searchNum);

        System.out.println("The road was treacherous, but we made it home.");
        System.out.println(pathTaken.toString());
        Iterator<MiddleEarth.City> it = pathTaken.iterator();
        while(it.hasNext()){
            MiddleEarth.City city = it.next();
            System.out.println(city.toString() + " f value: " + city.f);
            if(city.toString().equals("IRONHILLS")){
                System.out.println("TOTAL COST: " + city.distanceFromStart);
            }
        }
    }

    /* The first search is supposed to be entirely based on whether
     * or not we get closer to the objective. That is, when we're at
     * any node 'N', we will look at all the edges from N, and then take
     * the edge that gets us the closest to our goal.
     * parameters:
     *   MiddleEarth ME: The MiddleEarth we are traveling over.
     *   boolean winter: whether or not it's currently winter in middle earth
     *   int searchNum: The search we are executing.
     */
    public static LinkedList<MiddleEarth.City> aStarSearch(MiddleEarth ME, boolean winter, int searchNum) {
        
        if(winter){
            System.out.println("The chill of winter rolls through Middle Earth...");
        }

        if(searchNum == 1){
            System.out.println("Our wagons cannot withstand ruinous passages. We will favor exceptional roads.");
        } else if (searchNum == 2) {
            System.out.println("Bandits scour the roads, plundering many a merchant. We will heavily consider our safety...");
        } else {
            System.out.println("All is calm in Middle Earth today. Our expedition will be done by-the-book.");
        }
        System.out.println();
        System.out.println();
        
        // We locate the cities based on their indices within the array.
        int startPos = ME.getCityPos("BLUEMOUNTAINS");
        int goalPos = ME.getCityPos("IRONHILLS");
        
        // openCities is a linked list that will let us keep track of what cities are
        // available to us.
        // AKA the "open" set.
        Set<MiddleEarth.City> openCities = new HashSet<MiddleEarth.City>();
        
        // This is a set of the cities we've already expanded the search upon so we don't
        // expand on them again (get caught in loops).
        // AKA the "closed" set.
        Set<MiddleEarth.City> closedCities = new HashSet<MiddleEarth.City>();

        // This is our F value.
        // 0 - Our cost to get here is zero because this is our starting point.
        // ME.ME[startPos].distanceToIronHills -> How far from our goal we are.
        ME.ME[startPos].f = 0 + ME.ME[startPos].distanceToIronHills;

        // This Linked List will keep track of the path we've taken to get to where we
        // need to go.
        LinkedList<MiddleEarth.City> pathTaken = new LinkedList<MiddleEarth.City>();
        
        // Add our starting city to the closed set.
        closedCities.add(ME.ME[startPos]);
        
        // Add all the cities that are connected to the starting city to the open set.
        // and assign them all fValues.
        Iterator<MiddleEarth.Road> initOpenSet = ME.ME[startPos].roads.iterator();
        while(initOpenSet.hasNext()){
            MiddleEarth.Road road = initOpenSet.next();
            MiddleEarth.City city = ME.ME[road.destination];
            city.distanceFromStart = road.distance;
            city.f = road.distance + city.distanceToIronHills;

            // Search 1 will heavily favor higher quality roads.
            // The roads quality will be weighted by 3 and subtracted from f.
            // Search 2 will square the risk, and incorporate that into traveling decisions.
            if(searchNum == 1) {
                city.f += (2*road.risk - (3*road.quality));

              
            } else if (searchNum == 2){
                city.f += ((road.risk*road.risk) - 2*road.quality);
            } 
            
            
            if (winter){
                city.f += (road.winterRisk*road.winterRisk*road.winterRisk);
            }
            
            city.parent = ME.ME[startPos];
            openCities.add(city);
        }

        int totalCost = 0;
        
        while (!closedCities.contains(ME.ME[goalPos])) {
            
            // This will be the next city we will close.
            MiddleEarth.City bestFound = null;
            
            // Iterate through the cities we have available to us.
            Iterator<MiddleEarth.City> openCityIt = openCities.iterator();

            // This will find the best f value in the openSet.
            while(openCityIt.hasNext()){
                MiddleEarth.City city = openCityIt.next();
                if((bestFound == null || city.f < bestFound.f) && !closedCities.contains(city)){
                    bestFound = city;
                }
            }

            // Add all of the next best cities connections to the open set, and
            // assign them f values.
            Iterator<MiddleEarth.Road> roadIt = bestFound.roads.iterator();
            while(roadIt.hasNext()){
                MiddleEarth.Road road = roadIt.next();
                MiddleEarth.City currCity = ME.ME[road.destination];
                currCity.parent = bestFound;
                currCity.distanceFromStart = currCity.parent.distanceFromStart + road.distance;
                currCity.f = currCity.distanceFromStart + currCity.distanceToIronHills;
                
                if(searchNum == 1) {
                    currCity.f += (road.risk - (3*road.quality));
                
                } else if (searchNum == 2){
                    currCity.f += (road.risk*road.risk) - (2*road.quality);
                }                
                
                if (winter){
                    currCity.f += road.winterRisk*road.winterRisk*road.winterRisk;
                }
                openCities.add(currCity);
            }

            openCities.remove(bestFound);
            closedCities.add(bestFound);
        }

        // Create the pathTaken, so we can return it.
        // We work backwards using the 'parent' field in every city in the closed list.
        MiddleEarth.City currCity = ME.ME[goalPos];
        while(currCity != ME.ME[startPos]){
            currCity.parent.next = currCity;
            currCity = currCity.parent;
        }
        
        while(!currCity.next.toString().equals("IRONHILLS")){
            pathTaken.add(currCity);
            currCity = currCity.next;
        }
        pathTaken.add(currCity.next);
        return pathTaken;
    }
}
