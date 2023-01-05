package companies;

public class Plane
{
    String model;
    String color;
    int num;
    String factory;
    String productionDate;
    int seats;
    int isCargo;
    String comment;

    public Plane(String model, String color, int num, String factory, String productionDate, int seats, int isCargo, String comment)
    {
        this.model = model;
        this.color = color;
        this.num = num;
        this.factory = factory;
        this.productionDate = productionDate;
        this.seats = seats;
        this.isCargo = isCargo;
        this.comment = comment;
    }
}
