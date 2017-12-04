public class SharedData {

    public static int randomPort (int port_count_to_get){

        int min_port=1024;
        int max_port=65565;

        int PortRange = (max_port - min_port) + 1;
        return (int)(Math.random() * PortRange) + min_port;
    }

}
