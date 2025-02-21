package utils;

import com.github.javafaker.Faker;
import com.github.javafaker.IdNumber;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FakerUtil {

    public static String generateClassName()
    {
        Faker faker=new Faker();
        return "Name" +faker.regexify("[A-Za-z0-9]");
    }

    //public static String generateClassDate()
   // {
    //    Faker faker=new Faker();
     //   return faker.date().birthday().toString();
   // }

    public static IdNumber generateClassNum()
    {
        Faker faker=new Faker();
        return  faker.idNumber();
    }


}
