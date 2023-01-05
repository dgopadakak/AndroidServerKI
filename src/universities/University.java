package universities;

import java.util.ArrayList;

public class University
{
    String name;
    ArrayList<Faculty> listOfFaculties;

    public University(String name, ArrayList<Faculty> listOfFaculties)
    {
        this.name = name;
        this.listOfFaculties = listOfFaculties;
    }
}
