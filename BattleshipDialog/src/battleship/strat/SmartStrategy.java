package battleship.strat;

import java.util.List;
import java.util.LinkedList;
import java.util.Stack;

public class SmartStrategy {
	Stack<Integer> moves;
	List<Integer> history;
	int size;
	public SmartStrategy(int size){
		this.moves = new Stack<Integer>();
		this.history = new LinkedList<Integer>();
		this.size= size;
	}
	void shootRandom(){
		int random = (int)(Math.random()*this.size)+1;
		if(!history.contains(random))
			this.moves.push(random);
		else
			shootRandom();
	}
	int checkShot(){
		if(moves.isEmpty())
			shootRandom();
		return moves.peek();
	}
	int doShot(){
		int shot = moves.pop();
		this.history.add(shot);
		return shot;
	}
	void notifyHit(int place){
		moves.empty();
		//push some moves
		int start = place -1 - this.size;
		for(int i = start; i <= start + 2*this.size; i+=this.size){
			for(int j = i; j <= i+2; j++){
				if(isValid(j))
					moves.push(j);
			}
		}
	}
	boolean isValid(int check){
		boolean valid = true;
		if(check >=100 || check <0)
			valid = false;
		return valid && !history.contains(check);
	}
}
