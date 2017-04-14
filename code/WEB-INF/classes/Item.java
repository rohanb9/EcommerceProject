
import java.io.Serializable;

public class Item implements Serializable{

	private String name;
    private int id;
    private String salary;
     
    public String toString(){
        return name +"=="+id+"=="+salary;
    }
 
    public String getName() {
        return name;
    }
 
    public void setName(String name) {
        this.name = name;
    }
 
    public int getId() {
        return id;
    }
 
    public void setId(int id) {
        this.id = id;
    }
 
    public String getSalary() {
        return salary;
    }
 
    public void setSalary(String salary) {
        this.salary = salary;
    }
}
