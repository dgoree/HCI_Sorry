package gameItems;
import java.util.ArrayList;
import java.util.Collections;


public class Deck extends ArrayList<Card> {
	
	//1 (x5); S,2,3,4,5,7,8,10,11,12 (x4) = 45 cards
	public Deck(boolean populate) {
		
		//populate the deck
		if(populate) {
			//sorry cards (numerical value of 0)
			for(int i=0;i<4;i++) {
				add(new Card(0,"Sorry!"));
			}
			//ones
			for(int i=0;i<5;i++) {
				add(new Card(1,"Move from start or move forward 1 space."));
			}
			//cards with normal messages
			int[] ranks = {3,5,8,12};
			for(int i=0;i<ranks.length;i++) {
				for(int j=0;j<4;j++) {
					add(new Card(ranks[i],"Move forward "+String.valueOf(ranks[i])+" spaces."));
				}
			}
			//other cards
			for(int i=0;i<4;i++) {
				add(new Card(2,"Move from Start or move forward 2 spaces."));
			}
			for(int i=0;i<4;i++) {
				add(new Card(4,"Move backwards 4 spaces."));
			}
			for(int i=0;i<4;i++) {
				add(new Card(7,"Move one or two pawns a total of 7 spaces."));
			}
			for(int i=0;i<4;i++) {
				add(new Card(10,"Move forward 10 spaces or backwards 1 space."));
			}
			for(int i=0;i<4;i++) {
				add(new Card(11,"Move forward 11 spaces or switch places with an opponent's pawn."));
			}
			
			shuffle();
		}
	}
	
	//shuffle the deck via swap algorithm
	public void shuffle() {
		int random;
	    Card temp;
	    for(int i=0;i<45;i++) {
	        random = (int) (Math.random() * (44-i) + i);
	        temp = get(i);
	        set(i,get(random));
	        set(random,temp);
	    }
	}
}
