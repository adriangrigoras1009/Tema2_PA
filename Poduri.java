import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;

public class Poduri {
	static class Pozitie {
		int x;
		int y;
		public Pozitie(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
	static class Resolve {
		public static final String INPUT_FILE = "poduri.in";
		public static final String OUTPUT_FILE = "poduri.out";

		int N;
		int M;
		int X;
		int Y;
		char[][] mat;
		int nr_poduri_max;

		public void solve() {
			readInput();
			writeOutput(getResult());
		}
		private void readInput() {
			try {
				BufferedReader sc = new BufferedReader(new FileReader(INPUT_FILE));
				String n_si_m = sc.readLine();
				String[] da = new String[2];
				int i = 0;
				for (String c : n_si_m.split(" ")) {
					da[i] = c;
					i++;
				}
				N = Integer.parseInt(da[0]); //citire linii
				M = Integer.parseInt(da[1]); //citire coloane
				mat = new char[N][M];
				i = 0;
				String x_si_y = sc.readLine();
				for (String c : x_si_y.split(" ")) {
					da[i] = c;
					i++;
				}
				//pozitia initiala
				X = Integer.parseInt(da[0]);
				Y = Integer.parseInt(da[1]);
				int i1 = 0;
				//matricea
				while ((n_si_m = sc.readLine()) != null) {
					for (int j = 0; j < M; j++) {
						mat[i1][j] = n_si_m.charAt(j);
					}
					i1++;
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		private void writeOutput(int result) {
			try {
				PrintWriter pw = new PrintWriter(new File(OUTPUT_FILE));
				pw.printf("%d",result);
				pw.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		} 
		public void bfs(int[][] directii, int X1, int Y1, int nr_poduri_aux) {
			int[][] visited = new int[N][M];
			Queue<Pozitie> q = new LinkedList<Pozitie>();
			Pozitie nou = new Pozitie(X1,Y1);
			//adaug in coada pozitia initiala
			q.add(nou);
			nr_poduri_aux = 1;
			int contor = 0;
			int contor2 = 1;
			while (q.size() != 0) {
				while (contor2 > 0) {
					//scoatem din coada primul element
					Pozitie s = q.poll();
					visited[s.x][s.y] = 1;
					//incercam fiecare directie in care ar putea sa o ia
					for (int i = 0; i < 4; i++) {
						int new_line = s.x + directii[i][0];
						int new_col = s.y + directii[i][1];
						//verificam cazul in care iese din matrice si se termina
						if ((new_line >= N || new_line < 0) 
							&& (mat[s.x][s.y] == 'V' || mat[s.x][s.y] == 'D')) {
							nr_poduri_max = nr_poduri_aux;
							return;
						} else if ((new_col >= M || new_col < 0) 
							&& (mat[s.x][s.y] == 'O' || mat[s.x][s.y] == 'D')) {
							nr_poduri_max = nr_poduri_aux;
							return;
						}
						//verificam cazurile cand inca e in matrice si se poate muta
						if (new_line < N && new_line >= 0 && new_col < M 
							&& new_col >= 0 && visited[new_line][new_col] != 1) {
							if (mat[s.x][s.y] == 'D' 
								&& mat[new_line][new_col] != '.') {
								Pozitie caz1 = new Pozitie(new_line, new_col);
								visited[new_line][new_col] = 1;
								q.add(caz1);
								contor++;
							} else if (mat[s.x][s.y] == 'O' 
								&& mat[new_line][new_col] != '.' && (i == 0 || i == 1)) {
								Pozitie caz1 = new Pozitie(new_line, new_col);
								visited[new_line][new_col] = 1;
								q.add(caz1);
								contor++;
							} else if (mat[s.x][s.y] == 'V' 
								&& mat[new_line][new_col] != '.' && (i == 2 || i == 3)) {
								Pozitie caz1 = new Pozitie(new_line, new_col);
								visited[new_line][new_col] = 1;
								q.add(caz1);
								contor++;
							}
						}
					}
					contor2--;
				}
				//cresc nivelul
				contor2 = contor;
				contor = 0;
				nr_poduri_aux++;
			}
			nr_poduri_max = nr_poduri_aux;

		}
		public int getResult() {
			int[][] directii = {
				{0, 1},
				{0, -1},
				{1, 0},
				{-1, 0}
			};
			nr_poduri_max = -1;
			bfs(directii, X - 1, Y - 1, 0);
			return nr_poduri_max;
		}
	}
	public static void main(String[] args) {
		new Resolve().solve();
	}
}