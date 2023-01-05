package universities;

public class Faculty
{
    String name;
    String directions;
    int num;
    String email;
    String dateOfFoundation;
    int students;
    int isHaveDistanceLearning;
    String comment;

    public Faculty(String name, String directions, int num, String email, String dateOfFoundation, int students,
                   int isHaveDistanceLearning, String comment)
    {
        this.name = name;
        this.directions = directions;
        this.num = num;
        this.email = email;
        this.dateOfFoundation = dateOfFoundation;
        this.students = students;
        this.isHaveDistanceLearning = isHaveDistanceLearning;
        this.comment = comment;
    }
}
