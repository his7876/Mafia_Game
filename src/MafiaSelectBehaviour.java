
public class MafiaSelectBehaviour implements UserSelectedBehaviour{

	@Override
	public void whenUserSelected(int uid, int who, GameUser[] users, GameCMConnector cm) {
		// TODO Auto-generated method stub
		if(users[who].job == 1 && !users[who].isVoted) {
			users[uid].voteCount ++;
			users[who].isVoted = true;
		}		
	}

}
