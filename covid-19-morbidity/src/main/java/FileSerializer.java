import java.io.*;

public class FileSerializer {

    public static void main(String[] args) {

        String path = "src/resources/testZapisu";
        Person person = new Person("Greg", "Ozim", 20);

        writeObjectToFile(person, path);
        readObject(path);

    }

    public static void readObject(String filePath){
        try{
            FileInputStream fileIn = new FileInputStream(filePath);
            ObjectInputStream objInput = new ObjectInputStream(fileIn);
            Person o = (Person) objInput.readObject();
            System.out.println(o);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static void writeObjectToFile(Object serObj, String filepath) {

        try {

            FileOutputStream fileOut = new FileOutputStream(filepath);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(serObj);
            objectOut.close();
            System.out.println("The Object  was succesfully written to a file");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
