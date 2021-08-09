import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

public class Lego {
	static class Resolve {
		public static final String INPUT_FILE = "lego.in";
		public static final String OUTPUT_FILE = "lego.out";

		int K;
		int N;
		int T;
		int max;
		SortedSet<Integer> ceva;
		int[] SolutionF;
		PrintWriter pw;

		public void solve() {
			readInput();
			getResult();
		}

		private void readInput() {
			try {
				BufferedReader sc = new BufferedReader(new FileReader(INPUT_FILE));
				String k_n_t = sc.readLine();
				int j = 0;
				//citesc cei trei parametri
				for (String c : k_n_t.split(" ")) {
					if (j == 0) {
						K = Integer.parseInt(c);
					} else if (j == 1) {
						N = Integer.parseInt(c);
					} else if (j == 2) {
						T = Integer.parseInt(c);
					}
					j++;
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
		void dfs(int i, int[] z, int nivel, int suma) {
			//cat timp nivelul nu e maxim, adaug la suma, dar adaug si suma
			//pot fi folosite doar 2 piese sau doar 1 piesa, etc
			while (nivel < T - 1 && i < N) {
				ceva.add(suma);
				suma += z[i];
				nivel++;
				dfs(i, z, nivel, suma);
				//cand ies din recursivitate cresc indexul solutiei
				//pentru a incerca alte combinatii
				nivel--;
				suma -= z[i];
				i++;
			}
			if (i < N) {
				ceva.add(suma);
			}
		}
		void try_func(int[] solution) {
			ceva = new TreeSet<Integer>();
			int max1 = 0;
			int[] good_sol = new int[N];
			//construiesc solutia si cu 1
			good_sol[0] = 1;
			for (int i = 1; i < N; i++) {
				good_sol[i] = solution[i - 1];
			}
			// intru in dfs pentru fiecare element
			for (int i = 0; i < N; i++) {
				if (i == 0) {
					dfs(i, good_sol, 0, 1);
				} else {
					dfs(i, good_sol, 0, solution[i - 1]);
				}
			}
			Iterator<Integer> it = ceva.iterator();
			// verific sumele din set, si vad cate sunt consecutive
			int z = it.next();
			int contor = 1;
			while (it.hasNext()) {
				int x = it.next();
				if (x == z + 1) {
					contor++;
				} else {
					if (contor > max1) {
						max1 = contor;
					}
					contor = 1;
				}
				z = x;
			}
			if (contor > max1) {
				max1 = contor;
			}
			//daca sunt mai multe decat erau initial, se schimba solutia
			if (max1 > max) {
				max = max1;
				SolutionF = new int[N];
				for (int a = 0; a < N; a++) {
					SolutionF[a] = good_sol[a];
				}
			}
		} 
		public void back(int step, int stop, int[] domain, int[] solution) {
			if (step == stop) { //daca am gasit N piese, calculez
				try_func(solution);
				return;
			}
			int i = step > 0 ? solution[step - 1] + 1 : 2;
			for (; i < K - 1 + 2; i++) {
				solution[step] = i;
				back(step + 1, stop, domain, solution);
			}
		}
		public int getResult() {
			//creez domeniul si solutia
			int[] domain = new int[K - 1];
			int[] solution = new int[N - 1];
			int j = 0;
			for (int i = 2; i <= K; i++) {
				domain[j] = i;
				j++;
			}
			//aplic backtrackingul
			back(0, N - 1, domain, solution);
			pw.printf("%d\n", max);
			//afisez solutia finala
			for (int i = 0; i < N; i++) {
				if (i < N - 1) {
					pw.printf("%d ", SolutionF[i]);
				} else {
					pw.printf("%d", SolutionF[i]);
				}
			}
			pw.close();
			return 0;
		}
	}
	public static void main(String[] args) {
		new Resolve().solve();
	}
}