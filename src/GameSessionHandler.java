import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;

public class GameSessionHandler {
	private final int THRESHOLD_ROOM = 10;
	private HashMap<String, Integer> userRoomTable = new HashMap<String, Integer>();
	private GameSession[] rooms = new GameSession[10];
	
	
	public GameSessionHandler() {
		for(int i = 0 ; i < 10 ; i ++) {
			rooms[i] = new GameSession(new GameSessionCallback() {			
				@Override
				public void requestCollectGameThread(int roomid, int status) {
					// TODO Auto-generated method stub
					List<String> users = rooms[roomid].getUsers();
					for(int i = 0; i < users.size(); i ++) {
						userRoomTable.remove(users.get(i));
					}
				}
			});
		}
	}
	
	boolean fowardUserRequest(UserGameEventInfo msg) {
		int room = -1;
		if(userRoomTable.containsKey(msg.cme.getSender())) {
			room = userRoomTable.get(msg.cme.getSender());
		}
		
		switch(msg.opcode) {
		case 2:
			if(room != -1) {
				rooms[room].selectUser(msg.cme.getSender(), msg.args);
				return true;
			}
			return false;
		case 3:
			if(room != -1) {
				rooms[room].fowardChatEvent(msg.cme.getSender(), msg.args);
				return true;
			}
			return false;
		case 6:
			if(room != -1) {
				rooms[room].voteProsCons(msg.cme.getSender(), true);
				return true;
			}
			return false;
		case 7:
			if(room != -1) {
				rooms[room].voteProsCons(msg.cme.getSender(), false);
				return true;
			}
			return false;
		case 16:			
		case 17:
			if(room == -1) {
				for(int i = 0; i < 10; i ++) {
					if(rooms[i].canAddUser()) {
						userRoomTable.put(msg.cme.getSender(), i);
						return rooms[i].tryAddUser(msg.cme.getSender());
					}
				}
			}
			return false;
		}
		return true;
	}
	
	
}
