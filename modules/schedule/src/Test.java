import java.util.Date;

public class Test {
    public static void main(String[] args){

        Date datea = Tool.StrToDate("2018-03-14 14:00");
        Date dateb = Tool.StrToDate("2018-03-14 14:00");

        if(datea.equals(dateb)==true){
            System.out.println("相等");
        }
    }
}
