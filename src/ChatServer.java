import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

public class ChatServer {
    private static final int PORT=9002;
    private static HashSet<String> names= new HashSet<String>();
    private static HashSet<PrintWriter> writers=new HashSet<PrintWriter>();
    public static void main(String args[]) throws Exception{
        //starting chat server
        System.out.println("The Chat server is running ...");
        ServerSocket listner =new ServerSocket(PORT);
        try{
            while(true){
                Socket socket=listner.accept();
                Thread handlerThread= new Thread(new Handler(socket));
                handlerThread.start();
            }
        }finally {
            listner.close();
        }


    }
    private static class Handler implements Runnable{
        private String name;
        private Socket socket;

        private BufferedReader in;
        private PrintWriter out;
        public Handler(Socket socket){
            this.socket=socket;
        }
        public void run(){
            try{
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                out= new PrintWriter(socket.getOutputStream(),true);
                while(true){
                    out.println("Submit Name:");
                    name= in.readLine();
                    if(name==null){
                        return;
                    }
                    if(!names.contains(names)){
                        names.add(name);
                        break;
                    }
                }
                out.println("Name Accepted!");
                writers.add(out);
                while(true){
                    String input =in.readLine();
                    if(input==null){
                        return;
                    }
                    for(PrintWriter writer:writers){
                        writer.println("Message"+name+":"+input);
                    }
                }

            }catch(IOException e){
              System.out.println(e);
            }finally {
                if(names != null){
                    names.remove(name);
                }
                if(out !=null){
                    writers.remove(out);
                }try{
                    socket.close();
                }catch(IOException e){

                }
            }


        }
    }


}
