import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import companies.AirlineOperator;
import companies.Plane;

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
    private static AirlineOperator ao = new AirlineOperator();
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
        ao = gson.fromJson(aoJSON, AirlineOperator.class);


//        ao.addPlane("s7", new Plane("Airbus A380", "Green", 1, "Airbus factory",
//                "11.10.2015", 525, 0, "Базируется в Москве"));
//        ao.addPlane("s7", new Plane("Boeing 767", "White", 2, "Boeing factory",
//                "25.06.2004", 224, 0, "Базируется в Санкт-Петербурге"));
//
//        ao.addPlane("Аэрофлот", new Plane("Cessna 750", "Red", 1, "Citation factory",
//                "12.07.2001", 8, 0, "Базируется в Краснодаре"));
//        ao.addPlane("Аэрофлот", new Plane("Airbus A380", "White", 2, "Airbus factory",
//                "18.09.2015", 525, 0, "Базируется в Перми"));
//
//        aoJSON = gson.toJson(ao);
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
                        ao.delPlane(groupID, examID);
                        aoJSON = gson.toJson(ao);
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
                        Plane tempPlane = gson.fromJson(parts[1], Plane.class);
                        ao.editPlane(groupID, examID, tempPlane);
                        aoJSON = gson.toJson(ao);
                        writeFile(filePath, aoJSON);
                        out.println(aoJSON);
                    }
                    if ('u' == inputLine.charAt(0))     // ujson
                    {
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        Gson gson = gsonBuilder.create();
                        AirlineOperator tempGo = gson.fromJson(inputLine.substring(1), AirlineOperator.class);
                        ao.setAirlines(tempGo.getAirlines());
                        aoJSON = gson.toJson(ao);
                        writeFile(filePath, aoJSON);
                    }
                    if ('a' == inputLine.charAt(0))
                    {
                        GsonBuilder gsonBuilder = new GsonBuilder();        // agroupName##json
                        Gson gson = gsonBuilder.create();
                        String[] parts = inputLine.substring(1).split("##");
                        Plane tempPlane = gson.fromJson(parts[1], Plane.class);
                        ao.addPlane(parts[0], tempPlane);
                        aoJSON = gson.toJson(ao);
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
