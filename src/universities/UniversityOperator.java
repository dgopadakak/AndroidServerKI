package universities;

import java.util.ArrayList;
import java.util.Objects;

public class UniversityOperator
{
    private final int id = 1;
    private ArrayList<University> universities = new ArrayList<>();

    public void addFaculty(String universityName, Faculty faculty)
    {
        boolean isNewGroupNeeded = true;
        for (University university : universities)
        {
            if (Objects.equals(university.name, universityName))
            {
                isNewGroupNeeded = false;
                university.listOfFaculties.add(faculty);
                break;
            }
        }
        if (isNewGroupNeeded)
        {
            ArrayList<Faculty> tempArrayList = new ArrayList<>();
            tempArrayList.add(faculty);
            universities.add(new University(universityName, tempArrayList));
        }
    }

    public void delFaculty(int airlineId, int ticketId)
    {
        universities.get(airlineId).listOfFaculties.remove(ticketId);
    }

    public void editFaculty(int airlineId, int ticketId, Faculty newFaculty)
    {
        universities.get(airlineId).listOfFaculties.set(ticketId, newFaculty);
    }

    public ArrayList<University> getUniversities()
    {
        return universities;
    }

    public void setUniversities(ArrayList<University> universities)
    {
        this.universities = universities;
    }
}
