import java.io.*;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class SnakesLadder extends AbstractSnakeLadders {
	
	int N, M;
	int snakes[];
	int ladders[];
	int rev_snakes[];
	int rev_ladders[];
	int optimalMoves[];
	int rev_optimalMoves[];
	ArrayList<Integer> OnlyLadders;
	int x, y;
	
	public SnakesLadder(String name)throws Exception{
		File file = new File(name);
		BufferedReader br = new BufferedReader(new FileReader(file));
		N = Integer.parseInt(br.readLine());
        
        M = Integer.parseInt(br.readLine());

	    snakes = new int[N];
		ladders = new int[N];
		rev_ladders = new int[N];
		rev_snakes = new int[N];
	    for (int i = 0; i < N; i++){
			snakes[i] = -1;
			ladders[i] = -1;
			rev_ladders[i] = -1;
			rev_snakes[i] = -1;
		}

		OnlyLadders = new ArrayList<Integer>();
		for(int i=0;i<M;i++){
            String e = br.readLine();
            StringTokenizer st = new StringTokenizer(e);
            int source = Integer.parseInt(st.nextToken());
            int destination = Integer.parseInt(st.nextToken());

			if(source<destination){
				ladders[source] = destination;
				rev_ladders[destination] = source;				
			}
			else{
				snakes[source] = destination;
				rev_snakes[destination] = source;
			}
        }

		for(int i = 0; i < N; i++) {
			if (ladders[i] != -1) {
				OnlyLadders.add(i);
			}
		}
		// ArrayList<Integer>[] adjacencyList = new ArrayList[N]; //an array of arraylists
		// for (int i = 0; i < N; i++) {
		// 	adjacencyList[i] = new ArrayList<Integer>();
		// }
		optimalMoves = new int[N+1];
		// for (int i = 1; i <= 6; i++) {
		// 	optimalMoves[i] = 1;
		// }
		// for (int i = 1; i <= N; i++) {
		// 	if (ladders[i] != -1) {
		// 		UpdateOptMov(ladders[i], optimalMoves[i]);
		// 	}
		// 	if (snakes[i] != -1) {
		// 		UpdateOptMov(snakes[i], optimalMoves[i]);
		// 	}
		// 	for (int j = 1; j <= 6 && i+j <= N; j++) {
		// 		UpdateOptMov(i+j, optimalMoves[i]+1);
		// 	}
		// }

		

		Queue<Integer> BFS = new LinkedList<Integer>();
		for (int i = 1; i <= 6; i++) {
			BFS.add(i);
			optimalMoves[i] = 1;
		}

		// while (!BFS.isEmpty()) {			
		// 	int temp = BFS.poll();
		// 	System.out.print("temp"+temp);
		// 	if (temp != N) {
		// 		if (ladders[temp] != -1) {
		// 			if (updateOptMov(ladders[temp], optimalMoves[temp])) {
		// 				BFS.add(ladders[temp]);
		// 			}
		// 		}
		// 		if (snakes[temp] != -1) {
		// 			if (updateOptMov(snakes[temp], optimalMoves[temp])) {
		// 				BFS.add(snakes[temp]);
		// 			}
		// 		}
		// 		for (int j = 1; (j <= 6 && temp+j <= N); j++) {
		// 			if (updateOptMov(temp+j, optimalMoves[temp]+1)) {
		// 				BFS.add(temp+j);
		// 			}
		// 		}
		// 	}					
		// }
		// System.out.println("constructed");	
		
		while (!BFS.isEmpty()) {
			int temp = BFS.poll();
			if (temp != N) { //snakes or ladders cannot exist at N
				if (ladders[temp] != -1) {
					if (optimalMoves[ladders[temp]] == 0 || (optimalMoves[temp] < optimalMoves[ladders[temp]])) {
						optimalMoves[ladders[temp]] = optimalMoves[temp];
						BFS.add(ladders[temp]);
					}
					// optimalMoves[temp] = 0;
				}
				else if (snakes[temp] != -1) {
					if (optimalMoves[snakes[temp]] == 0 || (optimalMoves[temp] < optimalMoves[snakes[temp]])) {
						optimalMoves[snakes[temp]] = optimalMoves[temp];
						BFS.add(snakes[temp]);
					}
					// optimalMoves[temp] = 0;
				}
				else {
					for (int i = 1; (i <= 6 && temp+i <= N); i++) {
						if (optimalMoves[temp+i] == 0 || ((optimalMoves[temp]+1) < optimalMoves[temp+i])) {
							optimalMoves[temp+i] = optimalMoves[temp] + 1;
							BFS.add(temp+i);
						}
					}
				}
			}
		}

		rev_optimalMoves = new int[N+1];
		Queue<Integer> rev_BFS = new LinkedList<Integer>();
		//System.out.println("constructing");
		for (int i = N-1; i >= N-6; i--) {
			rev_BFS.add(i);
			rev_optimalMoves[i] = 1;
		}
		//System.out.println("queue initialized");
		//System.out.println(M+"value of M");
		for (int i = 0; i < N; i++) {
			if (ladders[i] != -1) {
				//System.out.println(i+","+ladders[i]+"ladder");
			}
			else if (snakes[i] != -1) {
				//System.out.println(i+","+snakes[i]+"snake");
			}
		}
		//System.out.println("end of snakes and ladders");

		while (!rev_BFS.isEmpty()) {
			int temp = rev_BFS.poll();
			//System.out.println("deqing"+temp);
			if (temp != N) {
				if (rev_ladders[temp] != -1) {
					if (rev_optimalMoves[rev_ladders[temp]] == 0 || (rev_optimalMoves[temp] < rev_optimalMoves[rev_ladders[temp]])) {
						rev_optimalMoves[rev_ladders[temp]] = rev_optimalMoves[temp];
						//System.out.println("if ladder at"+rev_ladders[temp]+"then moves->"+rev_optimalMoves[temp]);
						rev_BFS.add(rev_ladders[temp]);
					}
				}
				else if (rev_snakes[temp] != -1) {
					if (rev_optimalMoves[rev_snakes[temp]] == 0 || (rev_optimalMoves[temp] < rev_optimalMoves[rev_snakes[temp]])) {
						rev_optimalMoves[rev_snakes[temp]] = rev_optimalMoves[temp];
						//System.out.println("if ladder at"+rev_snakes[temp]+"then moves->"+rev_optimalMoves[rev_snakes[temp]]);
						rev_BFS.add(rev_snakes[temp]);
					}
				}	
				else {
					for (int i = 1; (i <= 6 && temp-i >= 1); i++) {
						if (rev_optimalMoves[temp-i] == 0 || ((rev_optimalMoves[temp]+1) < rev_optimalMoves[temp-i])) {
							rev_optimalMoves[temp-i] = rev_optimalMoves[temp] + 1;
							rev_BFS.add(temp-i);
							//System.out.println("enqueuing"+(temp-i)+"then moves->"+rev_optimalMoves[temp-i]);
						}
					}
				}			
			}
		}

		int optimusPrime = optimalMoves[N];

		if (optimalMoves[N] == 0) {
			optimalMoves[N] = -1;
			optimusPrime = N;
		}

		// for (int i = 0; i < optimalMoves.length; i++) {
		// 	System.out.println(i+","+optimalMoves[i]);
		// }
		//System.out.println(Arrays.toString(optimalMoves));
		//System.out.println(Arrays.toString(rev_optimalMoves));

		int x1 = 0 /*y2*/ /*Switch = 0*/;
		x = y = -1;
		for (int i = 0; i < OnlyLadders.size(); i++) {
			x1 = OnlyLadders.get(i);
			//System.out.println("x1="+x1);
			int temp_x = finalDestination(x1);
			// if (Switch == 0) {
			// 	Switch = 1;
			// }
			for (int j = i+1; j < OnlyLadders.size(); j++) {
				//System.out.println("pot_y1="+OnlyLadders.get(j));
				if (OnlyLadders.get(j) >= temp_x) {
					break;
				}
				if (optimalMoves[x1] + rev_optimalMoves[OnlyLadders.get(j)] < optimusPrime) {
					optimusPrime = optimalMoves[x1] + rev_optimalMoves[OnlyLadders.get(j)];
					y = OnlyLadders.get(j);
					// y2 = finalDestination(y);
					x = finalDestination(x1);
				}
			}
			// else if (Switch == 1) {}
		}
	}

	private int finalDestination (int i) {
		if (ladders[i] != -1) {
			return finalDestination(ladders[i]);
		}
		else if (snakes[i] != -1) {
			return finalDestination(snakes[i]);
		}
		else {
			return i;
		}
	}

	// private boolean updateOptMov(int index, int value) {
	// 	System.out.print("update");
	// 	if ((optimalMoves[index] == 0) || (optimalMoves[index] > value)) {
	// 		optimalMoves[index] = value;
	// 		return true;
	// 	}
	// 	System.out.print("updated");
	// 	return false;
	// }
    
	public int OptimalMoves()
	{
		/* Complete this function and return the minimum number of moves required to win the game. */
		return optimalMoves[N];
	}

	public int Query(int x, int y)
	{
		/* Complete this function and 
			return +1 if adding a snake/ladder from x to y improves the optimal solution, 
			else return -1. */
		//System.out.println("query "+x+","+y);
		if (optimalMoves[x] + rev_optimalMoves[y] < optimalMoves[N]) {
			return 1;
		}
		return -1;
	}

	public int[] FindBestNewSnake()
	{
		
		int result[] = {x, y};
		/* Complete this function and 
			return (x, y) i.e the position of snake if adding it increases the optimal solution by largest value,
			if no such snake exists, return (-1, -1) */
		return result;
	}

	// public static void main(String[] args) throws Exception {
	// 	SnakesLadder obj = new SnakesLadder("C:\\Users\\Vayun\\Downloads\\A5_starter_code\\input4.txt");
	// 	System.out.println(obj.OptimalMoves());
	// 	System.out.println(obj.Query(54, 50));
	// 	System.out.println(obj.Query(54, 95));
	// 	System.out.println(obj.Query(54, 10));
	// 	System.out.println(obj.FindBestNewSnake()[0]+","+obj.FindBestNewSnake()[1]);
	// }
}