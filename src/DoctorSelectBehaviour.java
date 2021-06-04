
public class DoctorSelectBehaviour implements UserSelectedBehaviour{

	@Override
	public void whenUserSelected(int uid, int who, GameUser[] users, GameCMConnector cm) {
		// TODO Auto-generated method stub
		if(users[who].job == 3 && !users[who].isVoted) {
			users[uid].isProtected = true;
			users[who].isVoted = true;
		}
	}
	
}
