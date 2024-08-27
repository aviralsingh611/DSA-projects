import heap_package.Node;
import heap_package.Heap;

import java.math.MathContext;
import java.util.ArrayList;

import javax.management.openmbean.KeyAlreadyExistsException;

public class Poker{

	private int city_size;            // City Population
	public int[] money;		         // Denotes the money of each citizen. Citizen ids are 0,1,...,city_size-1. 

	/* 
	   1. Can use helper methods but they have to be kept private. 
	   2. Allowed to use only PriorityQueue data structure globally but can use ArrayList inside methods. 
	   3. Can create at max 4 priority queues.
	*/

	private Heap lossExhaust;
	private Heap profitExhaust;
	private Heap maxProfit_players;
	private Heap maxLoss_players;

	public void initMoney(){
		// Do not change this function.
		for(int i = 0;i<city_size;i++){
			money[i] = 100000;							// Initially all citizens have $100000. 
		}
	}

	public Poker(int city_size, int[] players, int[] max_loss, int[] max_profit){

		/* 
		   1. city_size is population of the city.
		   1. players denotes id of the citizens who have come to the Poker arena to play Poker.
		   2. max_loss[i] denotes the maximum loss player "players[i]"" can bear.
		   3. max_profit[i] denotes the maximum profit player "players[i]"" will want to get.
		   4. Initialize the heap data structure(if required). 
		   n = players.length 
		   Expected Time Complexity : O(n).
		*/

		this.city_size = city_size;
		this.money = new int[this.city_size];
		this.initMoney();

		// To be filled in by the student
		System.out.println("building");
		try {
			int m = players.length;
			// for (int i=0; i<m; i++) {
			// 	System.out.print(players[i]+",");
			// }
			int[] max_lossDifference = new int[m];
			int[] max_profitDifference = new int[m]; 
			int[] all_citizens = new int[city_size];
			int[] citizenProfitOrLoss = new int[city_size];
			for (int i = 0; i < city_size; i++) {
				all_citizens[i] = i;
				citizenProfitOrLoss[i] = 0; //money[i]-100000 or 100000-money[i]
			}

			for (int i = 0; i < m; i++) {
				max_lossDifference[i] = -max_loss[i];
				max_profitDifference[i] = -max_profit[i];
				//scores[i] = min(, i)
			}
			lossExhaust = new Heap(city_size, players, max_lossDifference);
			profitExhaust = new Heap(city_size, players, max_profitDifference);
			maxLoss_players = new Heap(city_size, all_citizens, citizenProfitOrLoss);
			maxProfit_players = new Heap(city_size, all_citizens, citizenProfitOrLoss);

		} catch (Exception duplicateKey) {
			//System.out.println("buildheap exception");
		}
		//System.out.println("built");
	}

	public ArrayList<Integer> Play(int[] players, int[] bids, int winnerIdx){

		/* 
		   1. players.length == bids.length
		   2. bids[i] denotes the bid made by player "players[i]" in this game.
		   3. Update the money of the players who has played in this game in array "money".
		   4. Returns players who will leave the poker arena after this game. (In case no
		      player leaves, return an empty ArrayList).
                   5. winnerIdx is index of player who has won the game. So, player "players[winnnerIdx]" has won the game.
		   m = players.length
		   Expected Time Complexity : O(mlog(n))
		*/
 
		int winner = players[winnerIdx];					// Winner of the game.

		ArrayList<Integer> playersToBeRemoved = new ArrayList<Integer>();     // Players who will be removed after this game. 

		// To be filled in by the student
		//System.out.println("play");
		try {
			// for (int i = 0; i < players.length; i++) {
			// 	System.out.print(players[i]+",");
			// }
			// System.out.println(winner+"winner");
			// for (int i = 0; i < bids.length; i++) {
			// 	System.out.print(bids[i]+",");
			// }
			int m = players.length;
			for (int i = 0; i < m; i++) {
				if (i != winner) {
					money[players[i]] -= bids[i];
					money[winner] += bids[i];
					lossExhaust.update(players[i], +bids[i]);
					lossExhaust.update(winner, -bids[i]);
					profitExhaust.update(winner, +bids[i]);
					profitExhaust.update(players[i], -bids[i]);
					maxLoss_players.update(players[i], +bids[i]);
					maxLoss_players.update(winner, -bids[i]);
					maxProfit_players.update(winner, +bids[i]);
					maxProfit_players.update(players[i], -bids[i]);
					if (lossExhaust.getMaxValue() >= 0) {
						playersToBeRemoved.add(players[i]);
						lossExhaust.deleteMax();
					}
				}
			}

			if (profitExhaust.getMaxValue() >= 0) {
				playersToBeRemoved.add(profitExhaust.deleteMax().get(0));
				lossExhaust.update(winner, 2*money[winner]);
				lossExhaust.deleteMax();
			}
			for (int i = 0; i < playersToBeRemoved.size()-1; i++) {
				profitExhaust.update(playersToBeRemoved.get(i), 2*money[playersToBeRemoved.get(i)]);
				profitExhaust.deleteMax();
			}
			if (playersToBeRemoved.get(playersToBeRemoved.size()-1) != winner) {
				profitExhaust.update(playersToBeRemoved.get(playersToBeRemoved.size()-1), 2*money[playersToBeRemoved.get(playersToBeRemoved.size()-1)]);
				profitExhaust.deleteMax();
			}
		} catch (Exception manyExceptions) {}

		return playersToBeRemoved;
	}

	public void Enter(int player, int max_loss, int max_profit){

		/*
			1. Player with id "player" enter the poker arena.
			2. max_loss is maximum loss the player can bear.
			3. max_profit is maximum profit player want to get. 
			Expected Time Complexity : O(logn)
		*/

		// To be filled in by the student
		try {
			lossExhaust.update(player, -max_loss + 100000 - money[player]);
			profitExhaust.update(player, -max_profit - 100000 + money[player]);
		} catch (Exception nullKey) {
			try {
				lossExhaust.insert(player, -max_loss + 100000 - money[player]);
				profitExhaust.insert(player, -max_profit - 100000 + money[player]);
			} catch (Exception duplicateKey) {}
		}
		//System.out.println("entered"+player);
	}

	public ArrayList<Integer> nextPlayersToGetOut(){

		/* 
		   Returns the id of citizens who are likely to get out of poker arena in the next game. 
		   Expected Time Complexity : O(1). 
		*/

		ArrayList<Integer> players = new ArrayList<Integer>();    // Players who are likely to get out in next game.

		// To be filled in by the student
		try {
			//System.out.println("nextplayerstogetout");
			if (lossExhaust.getMaxValue() > profitExhaust.getMaxValue()) {
				return lossExhaust.getMax();
			}
			else if (lossExhaust.getMaxValue() < profitExhaust.getMaxValue()) {
				return profitExhaust.getMax();
			}
			else {
				ArrayList<Integer> temp = new ArrayList<Integer>();
				temp.addAll(profitExhaust.getMax());
				players.addAll(lossExhaust.getMax());
				for (int i = 0; i < temp.size(); i++) {
					if(!players.contains(temp.get(i))) {
						players.add(temp.get(i));
					}
				}
			}
		} catch (Exception manyExceptions) {
			//System.out.println("yoyoyoyo");
		}
		
		//System.out.println("nextplayend");
		return players;
	}

	public ArrayList<Integer> playersInArena(){

		/* 
		   Returns id of citizens who are currently in the poker arena. 
		   Expected Time Complexity : O(n).
		*/

		ArrayList<Integer> currentPlayers = new ArrayList<Integer>();    // citizens in the arena.

		// To be filled in by the student
		System.out.println("players in arena");
		try {
			currentPlayers.addAll(lossExhaust.getKeys());
		} catch (Exception nullRoot) {
			System.out.println("why arena");
		}

		System.out.println("player in arena end");
		return currentPlayers;
	}

	public ArrayList<Integer> maximumProfitablePlayers(){

		/* 
		   Returns id of citizens who has got most profit. 
			
		   Expected Time Complexity : O(1).
		*/

		ArrayList<Integer> citizens = new ArrayList<Integer>();    // citizens with maximum profit.

		// To be filled in by the student
		System.out.println("maxprofitable_players");
		try {
			citizens.addAll(maxProfit_players.getMax());
		} catch (Exception nullRoot) {}

		System.out.println("maxproft end");
		return citizens;
	}

	public ArrayList<Integer> maximumLossPlayers(){

		/* 
		   Returns id of citizens who has suffered maximum loss. 
			
		   Expected Time Complexity : O(1).
		*/

		ArrayList<Integer> citizens = new ArrayList<Integer>();     // citizens with maximum loss.

		// To be filled in by the student
		System.out.println("maxloss_players");
		try {
			citizens.addAll(maxLoss_players.getMax());
		} catch (Exception nullRoot) {}

		System.out.println("maxloss end");
		return citizens;
	}
}