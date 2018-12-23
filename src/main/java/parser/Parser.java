package parser;

import com.models.DataObject;
import parser.DAO.SimptomsDAO;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class Parser {
    public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException {
        SimptomsDAO simptomsDAO = new SimptomsDAO();
        FileReader input = new FileReader("mainNew.txt");
        BufferedReader bufRead = new BufferedReader(input);
        String line = null;

        while ((line = bufRead.readLine()) != null) {
            String[] array = line.split(",");
            DataObject o = new DataObject(
                    Integer.parseInt(array[2]),
                    Integer.parseInt(array[3]),
                    Integer.parseInt(array[4]),
                    Integer.parseInt(array[5]),
                    Integer.parseInt(array[6]),
                    Integer.parseInt(array[7]),
                    Integer.parseInt(array[8]),
                    Integer.parseInt(array[9]),
                    Integer.parseInt(array[10]),
                    Integer.parseInt(array[1])
            );
            simptomsDAO.addObject(o);
        }
    }

    public static ArrayList<String> getTitle(String fileName) throws IOException {
        ArrayList<String> titleString = new ArrayList<String>();
        FileReader input = new FileReader(fileName + ".txt");
        BufferedReader bufRead = new BufferedReader(input);
        String line = null;

        while ((line = bufRead.readLine()) != null) {
            String[] array = line.split(",");
            for (int i = 0; i < array.length; i++) {
                titleString.add(array[i]);
            }
        }
        return titleString;
    }
}
