import java.util.ArrayList;
import java.util.Random;

// 셔플등의 기능을 지원하는 클래스
public class Randomizer {
	private ArrayList<Integer> nums;
	
	
	public Randomizer() {
		nums = new ArrayList<Integer>();
	}
	
	
	
	// 리스트 안의 요소들을 섞어주는 함수
	private void shuffle() {
		Random random = new Random();
		for(int i = 0; i < nums.size() * 2; i ++) {
			int target = (random.nextInt() % nums.size());
			int curr = nums.get(target);
			nums.remove(target);
			int insertTo = (random.nextInt() % (nums.size()));
			nums.add(insertTo, curr);
		}
	}
	
	
	public int[] getNums(){
		shuffle();
		int[] ret = new int[nums.size()];
		for(int i = 0; i < nums.size(); i ++) {
			ret[i] = nums.get(i);
		}
		return ret;
	}
	
	
	public int addNumber(int number) {
		nums.add(number);
		return nums.size();
	}
	
	public void init() {
		nums = new ArrayList<Integer>();
	}
}
