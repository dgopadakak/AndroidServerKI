package companies;

import java.util.ArrayList;

public class Airline
{
    String name;
    ArrayList<Plane> listOfPlanes;

    public Airline(String name, ArrayList<Plane> listOfPlanes)
    {
        this.name = name;
        this.listOfPlanes = listOfPlanes;
    }
}
