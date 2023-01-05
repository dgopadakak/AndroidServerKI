package companies;

import java.util.ArrayList;
import java.util.Objects;

public class AirlineOperator
{
    private final int id = 1;
    private ArrayList<Airline> airlines = new ArrayList<>();

    public void addPlane(String airlineName, Plane plane)
    {
        boolean isNewGroupNeeded = true;
        for (Airline airline : airlines)
        {
            if (Objects.equals(airline.name, airlineName))
            {
                isNewGroupNeeded = false;
                airline.listOfPlanes.add(plane);
                break;
            }
        }
        if (isNewGroupNeeded)
        {
            ArrayList<Plane> tempArrayList = new ArrayList<>();
            tempArrayList.add(plane);
            airlines.add(new Airline(airlineName, tempArrayList));
        }
    }

    public void delPlane(int airlineId, int ticketId)
    {
        airlines.get(airlineId).listOfPlanes.remove(ticketId);
    }

    public void editPlane(int airlineId, int ticketId, Plane newPlane)
    {
        airlines.get(airlineId).listOfPlanes.set(ticketId, newPlane);
    }

    public ArrayList<Airline> getAirlines()
    {
        return airlines;
    }

    public void setAirlines(ArrayList<Airline> airlines)
    {
        this.airlines = airlines;
    }
}
