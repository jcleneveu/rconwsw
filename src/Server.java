import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.*;

public class Server {

	protected String address = "127.0.0.1";
	protected int port = 44400;
	protected String password = "password";
	private DatagramSocket socket = null;
	private int maxBufferSize = 1000;

	public Server() {

	}

	public Server(String address, int port, String password, int maxBufferSize) {
		this.address = address;
		this.port = port;
		this.password = password;
		this.maxBufferSize = maxBufferSize;
	}

	private void openSocket() {
		if (socket == null) {
			try {
				this.socket = new DatagramSocket();
			} catch (SocketException e) {
				e.printStackTrace();
			}
		}
	}

	public String sendRcon(String command) throws SocketException, IOException {

		this.openSocket();

		byte[] header = { (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff };
		byte[] body = ("rcon " + password + " " + command).getBytes();
		byte[] outputBuffer = new byte[header.length + body.length];
		System.arraycopy(header, 0, outputBuffer, 0, header.length);
		System.arraycopy(body, 0, outputBuffer, header.length, body.length);

		DatagramPacket output = new DatagramPacket(outputBuffer, outputBuffer.length,
				new InetSocketAddress(address, port));

		socket.send(output);
		
		byte[] inputBuffer = new byte[maxBufferSize];
		DatagramPacket input = new DatagramPacket(inputBuffer, inputBuffer.length,
				new InetSocketAddress(address, port));
		socket.receive(input);

		return new String(input.getData(), 0, input.getLength())
				.substring(10);
	}
	
	public Boolean map(String mapName) throws SocketException, IOException {
		String problem = "Couldn't find map";
		return ! sendRcon("map \"" + mapName + "\"").contains(problem);
	}
	
	public ArrayList<String> maplist() throws SocketException, IOException {
		String[] lines = sendRcon("maplist").split("\n");
		ArrayList<String> maplist = new ArrayList<String>(Arrays.asList(lines));
		maplist.remove(0);
		return maplist;
	}
	
	public Boolean gametype(String gtName) throws SocketException, IOException {
		sendRcon("g_gametype \"" + gtName + "\"");
		return true;
	}
	
	public String gametype() throws SocketException, IOException {
		String[] lines = sendRcon("g_gametype").split("\n");
		String result = lines[1].substring(17);
		String[] split = result.split("\"");
		return split[0];
	}
	
	public String status() throws SocketException, IOException {
		return sendRcon("status");
	}
	
	public Boolean operatorPassword(String opPassword) throws SocketException, IOException {
		sendRcon("g_operator_password \"" + opPassword + "\"");
		return true;
	}
	
	public String operatorPassword() throws SocketException, IOException {
		String[] lines = sendRcon("g_operator_password").split("\n");
		String result = lines[1].substring(26);
		String[] split = result.split("\"");
		return split[0];
	}
	
	public Boolean kick(int playerId) throws SocketException, IOException {
		sendRcon("kick " + playerId);
		return true;
	}
	
	public Boolean restart() throws SocketException, IOException {
		sendRcon("restart");
		return true;
	}
	
	public Boolean numbots(int nbBots) throws SocketException, IOException {
		sendRcon("g_numbots " + nbBots);
		return true;
	}
	
	public int numbots() throws SocketException, IOException {
		String[] lines = sendRcon("g_numbots").split("\n");
		String result = lines[1].substring(16);
		String[] split = result.split("\"");
		return Integer.parseInt(split[0]);
	}
	
	public Boolean hostname(String hostname) throws SocketException, IOException {
		sendRcon("sv_hostname \"" + hostname + "\"");
		return true;
	}
	
	public String hostname() throws SocketException, IOException {
		String[] lines = sendRcon("sv_hostname").split("\n");
		String result = lines[1].substring(18);
		String[] split = result.split("\"");
		return split[0];
	}
	
	public Boolean gravity(int gravityCoef) throws SocketException, IOException {
		sendRcon("g_gravity " + gravityCoef);
		return true;
	}
	
	public int gravity() throws SocketException, IOException {
		String[] lines = sendRcon("g_gravity").split("\n");
		String result = lines[1].substring(16);
		String[] split = result.split("\"");
		return Integer.parseInt(split[0]);
	}
}
