package DragonAnalyzer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class PrintUsers {
    public void writeCsv(ArrayList<String> names, ArrayList<String> links, String fileName) throws IOException {
        if (!Files.exists(Paths.get("c:\\DragonAnalyzer\\out"))){
            new File("c:\\DragonAnalyzer\\out").mkdir();
        }
        PrintStream out = new PrintStream(new FileOutputStream("c:\\DragonAnalyzer\\out\\"+fileName+".csv"));
        File file = new File("c:\\DragonAnalyzer\\out\\"+fileName+".csv");
        FileWriter fw = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(fw);

        bw.write("Domain,Total");
        bw.newLine();
        for(int i=0;i<names.size();i++)
        {
            bw.write(names.get(i)+","+links.get(i));
            bw.newLine();
        }
        bw.close();
        fw.close();
    }
}
