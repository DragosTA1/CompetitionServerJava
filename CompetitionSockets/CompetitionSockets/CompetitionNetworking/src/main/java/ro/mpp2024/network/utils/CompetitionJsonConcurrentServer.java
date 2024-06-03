package ro.mpp2024.network.utils;

import ro.mpp2024.network.jsonprotocol.CompetitionClientJsonWorker;
import ro.mpp2024.services.IServices;

import java.net.Socket;

public class CompetitionJsonConcurrentServer extends AbsConcurrentServer {
    private IServices server;
    public CompetitionJsonConcurrentServer(int port, IServices server) {
        super(port);
        this.server = server;
        System.out.println("Competition- CompetitionJsonConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        CompetitionClientJsonWorker worker = new CompetitionClientJsonWorker(server, client);

        Thread tw = new Thread(worker);
        return tw;
    }
}
