import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Stack;

public class Retea {
	static class Resolve {
		public static final String INPUT_FILE = "retea.in";
		public static final String OUTPUT_FILE = "retea.out";

		int n;
		int m;
		ArrayList<Integer>[] adj;
		PrintWriter pw;

		int[] parent;

		int[] found;

		int[] low_link;

		Stack<Integer> nodes_stack;

		boolean[] in_stack;

		int timestamp;

		public void solve() {
			readInput();
			getResult();
		}

		private void readInput() {
			try {
				BufferedReader sc = new BufferedReader(new FileReader(INPUT_FILE));
				String n_m = sc.readLine();
				int j = 0;
				for (String c : n_m.split(" ")) {
					if (j == 0) {
						n = Integer.parseInt(c);
					} else if (j == 1) {
						m = Integer.parseInt(c);
					}
					j++;
				}
				adj = new ArrayList[n + 1];
				for (int i = 1; i <= n; i++) {
					adj[i] = new ArrayList<>();
				}
				for (int i = 0; i < m; i++) {
					String muchie = sc.readLine();
					int j1 = 0;
					int x = -1;
					int y = -1;
					for (String q : muchie.split(" ")) {
						if (j1 == 0) {
							x = Integer.parseInt(q);
						} else if (j1 == 1) {
							y = Integer.parseInt(q);
						}
						j1++;   
					}
					adj[x].add(y);
					adj[y].add(x);

				}
				try {
					pw = new PrintWriter(new File(OUTPUT_FILE));
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		public void getResult() {
			
			parent = new int[n + 1];
			found = new int[n + 1];
			low_link = new int[n + 1];

			HashSet<Integer> all_cvs = new HashSet<>();
			timestamp = 0;
			//aflu punctele de articulatie
			for (int node = 1; node <= n; node++) {
				if (parent[node] == 0) {
					parent[node] = node;
					dfs2(node, all_cvs);
				}
			}
			//un vector de visited pentru a stii punctele de art
			int[] visited = new int[n + 1];
			Iterator<Integer> it = all_cvs.iterator();
			//parcurg toate punctele de art
			while (it.hasNext()) {
				Integer x1 = it.next();
				if (adj[x1].size() > 1) {				
					parent = new int[n + 1];
					found = new int[n + 1];
					low_link = new int[n + 1];
					in_stack = new boolean[n + 1];
					nodes_stack = new Stack<Integer>();
					//aflu componentele conexe intr-un graf fara punctul curent
					ArrayList<ArrayList<Integer>> all_sccs = new ArrayList<>();
					timestamp = 0;
					for (int node1 = 1; node1 <= n; node1++) {
						if (parent[node1] == 0 && node1 != x1) {
							parent[node1] = node1;
							dfs(node1, all_sccs, x1);
						}
					}
					int max = 2 * (n - 1);
					//calculez noul numar de conexiuni afectate, in functie de marimea comp
					for (ArrayList<Integer> x : all_sccs) {
						max = max + x.size() * ((n - 1) - x.size());
					}
					//salvez numarul de conexiuni, ca nefiind standard
					visited[x1] = max;
				}  
			}
			//afisez numarul de conexiuni afectate pentru fiecare punct eliminat
			for (int i = 1; i <= n; i++) {
				if (visited[i] != 0) {
					//daca e punct de articulatie
					pw.printf("%d\n",visited[i]);  
				} else {
					//daca nu e punct de articulatie
					int max;
					max = 2 * (n - 1);
					pw.printf("%d\n",max);
				}
			} 
			pw.close();
		}
		int stack_aux = 0;
		int[] stack_incercare = new int[n + 1];
		public void dfs(int node, ArrayList<ArrayList<Integer>> all_sccs, int i) {
			found[node] = ++timestamp;
			low_link[node] = found[node];
			nodes_stack.push(node);
			in_stack[node] = true;

			for (Integer neigh : adj[node]) {
				if (neigh != i) {
					if (parent[neigh] != 0) {
						if (in_stack[neigh]) {
							low_link[node] = Math.min(low_link[node], found[neigh]);
						}
						continue;
					}
					parent[neigh] = node;

					dfs(neigh, all_sccs, i);

					low_link[node] = Math.min(low_link[node], low_link[neigh]);
				}
			}
			if (low_link[node] == found[node]) {
				ArrayList<Integer> scc = new ArrayList<>();
				do {
					Integer x = nodes_stack.peek();
					nodes_stack.pop();
					in_stack[x] = false;
					scc.add(x);
				} while (scc.get(scc.size() - 1) != node);

				all_sccs.add(scc);
			}
		}

		public void dfs2(int node, HashSet<Integer>  all_cvs) {
			found[node] = ++timestamp;
			low_link[node] = found[node];

			int children = 0;
			for (Integer neigh : adj[node]) {
				if (parent[neigh] != 0) {
					if (neigh != parent[node]) {
						low_link[node] = Math.min(low_link[node], found[neigh]);
					}
					continue;
				}

				parent[neigh] = node;
				++children;

				dfs2(neigh, all_cvs);

				low_link[node] = Math.min(low_link[node], low_link[neigh]);

				if (parent[node] != node && low_link[neigh] >= found[node]) {
					all_cvs.add(node);
				}
			}
			if (parent[node] == node && children > 1) {
				all_cvs.add(node);
			}
		}
	}
	public static void main(String[] args) {
		new Resolve().solve();
	}
}