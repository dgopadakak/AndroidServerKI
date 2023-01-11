import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import universities.UniversityOperator;
import universities.Faculty;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MultiServer
{
    private ServerSocket serverSocket;
    private static UniversityOperator uo = new UniversityOperator();
    private static String aoJSON;
    private final static String filePath = "info.txt";

    public void start(int port) throws IOException
    {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        try {
            aoJSON = readFile(filePath, StandardCharsets.UTF_8);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        uo = gson.fromJson(aoJSON, UniversityOperator.class);


//        uo.addFaculty("КубГУ", new Faculty("ФКТиПМ", "Прикладная математика, " +
//                "Прикладная информатика, Фундаментальная информатика", 1, "fpm@kubsu.ru",
//                "21.08.1989", "400, 300, 300", 0,
//                "Факультет компьютеных технологий и прикладной математики"));
//        uo.addFaculty("КубГУ", new Faculty("РГФ", "Лингвистика, Перевод и переводоведение" +
//                ", Филология", 2, "rgf@kubsu.ru",
//                "01.01.1938", "200, 200, 300", 1,
//                "Факультет романо-германской филологии"));
//
//        uo.addFaculty("КубГТУ", new Faculty("ИНГЭ", "Эксплуатация и обслуживание объектов " +
//                "добычи нефти, Бурение нефтяных и газовых скважин", 1, "inge@kgtu.kuban.ru",
//                "12.04.1994", "300, 200", 1,
//                "Институт нефти, газа и энергетики"));
//        uo.addFaculty("КубГТУ", new Faculty("ИСТИ", "Городское строительство и хозяйство, " +
//                "Автомобильные дороги", 2, "isti@kgtu.kuban.ru", "15.05.2017",
//                "250, 250", 0, "Институт строительства и транспортной инфраструктуры"));
//
//        aoJSON = gson.toJson(uo);
//        writeFile(filePath, aoJSON);

        serverSocket = new ServerSocket(port);
        while (true)
        {
            new EchoClientHandler(serverSocket.accept()).start();
        }
    }

    public void stop() throws IOException
    {
        serverSocket.close();
    }

    private static class EchoClientHandler extends Thread
    {
        private final Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;

        public EchoClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run()
        {
            try
            {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            try
            {
                in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            String inputLine = null;
            while (true)
            {
                try
                {
                    if ((inputLine = in.readLine()) == null) break;
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                if (".".equals(inputLine))
                {
                    out.println("bye");
                    break;
                }
                if ("REFRESH".equals(inputLine))
                {
                    out.println(aoJSON);
                }
                if (inputLine != null)
                {
                    if ('d' == inputLine.charAt(0))     // d0,1
                    {
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        Gson gson = gsonBuilder.create();
                        String[] ids = inputLine.substring(1).split(",");
                        int groupID = Integer.parseInt(ids[0]);
                        int examID = Integer.parseInt(ids[1]);
                        uo.delFaculty(groupID, examID);
                        aoJSON = gson.toJson(uo);
                        writeFile(filePath, aoJSON);
                        out.println(aoJSON);
                    }
                    if ('e' == inputLine.charAt(0))     // e0,3##json
                    {
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        Gson gson = gsonBuilder.create();
                        String[] parts = inputLine.substring(1).split("##");
                        String[] ids = parts[0].split(",");
                        int groupID = Integer.parseInt(ids[0]);
                        int examID = Integer.parseInt(ids[1]);
                        Faculty tempFaculty = gson.fromJson(parts[1], Faculty.class);
                        uo.editFaculty(groupID, examID, tempFaculty);
                        aoJSON = gson.toJson(uo);
                        writeFile(filePath, aoJSON);
                        out.println(aoJSON);
                    }
                    if ('u' == inputLine.charAt(0))     // ujson
                    {
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        Gson gson = gsonBuilder.create();
                        UniversityOperator tempGo = gson.fromJson(inputLine.substring(1), UniversityOperator.class);
                        uo.setUniversities(tempGo.getUniversities());
                        aoJSON = gson.toJson(uo);
                        writeFile(filePath, aoJSON);
                    }
                    if ('a' == inputLine.charAt(0))
                    {
                        GsonBuilder gsonBuilder = new GsonBuilder();        // agroupName##json
                        Gson gson = gsonBuilder.create();
                        String[] parts = inputLine.substring(1).split("##");
                        Faculty tempFaculty = gson.fromJson(parts[1], Faculty.class);
                        uo.addFaculty(parts[0], tempFaculty);
                        aoJSON = gson.toJson(uo);
                        writeFile(filePath, aoJSON);
                        out.println(aoJSON);
                    }
                }
            }

            try
            {
                in.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            out.close();
            try
            {
                clientSocket.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public static String readFile(String path, Charset encoding) throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    public static void writeFile(String path, String text)
    {
        try(FileWriter writer = new FileWriter(path, false))
        {
            writer.write(text);
            writer.flush();
        }
        catch(IOException ex)
        {
            System.out.println(ex.getMessage());
        }
    }
}
