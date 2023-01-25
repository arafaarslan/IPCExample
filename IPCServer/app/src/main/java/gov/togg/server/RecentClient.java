package gov.togg.server;

public class RecentClient {

    private static RecentClient recentClient = null;
    private Client c = null;

    private RecentClient() {

    }

    public static RecentClient getRecentClient(){
        if(recentClient == null){
            recentClient = new RecentClient();
        }
        return recentClient;
    }

    public void setClient(Client client) {
        this.c = client;
    }

    public Client getClient() {
        return c;
    }
}
