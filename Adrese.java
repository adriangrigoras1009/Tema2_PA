import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class Adrese {
	static class Persoana {
		public String nume;
		public int id_real;
		public Persoana() {
			this.nume = null;
			
		}
	}
	static class Persoanareala {
		public String nume;
		public SortedSet<String> emails;
		public int contor;
		public Persoanareala(String nume) {
			this.nume = new String(nume);
			this.emails = new TreeSet<String>();
		}
	}
	static class Resolve {
		public static final String INPUT_FILE = "adrese.in";
		public static final String OUTPUT_FILE = "adrese.out";

		int id_contor;
		int n;
		Persoana[] persoane;
		Map<String, Integer> map;
		public void solve() {
			readInput();
			getResult();
		}
		private void readInput() {
			try {
				BufferedReader sc = new BufferedReader(new FileReader(INPUT_FILE));
				String nr = sc.readLine();
				id_contor = 0;
				n = Integer.parseInt(nr);
				persoane = new Persoana[n];
				map = new TreeMap<String, Integer>();
				for (int i = 0; i < n; i++) {
					String nume_si_nr = sc.readLine();
					String nume = "cva";
					int nr_adrese = 0;
					int j = 0;
					for (String c : nume_si_nr.split(" ")) {
						if (j == 0) {
							nume = new String(c);
						} else {
							nr_adrese = Integer.parseInt(c);
						}
						j++;
					}
					// persoana este adaugata in vector
					// initial id_real e acelasi cu al lui
					persoane[id_contor] = new Persoana();
					persoane[id_contor].nume = new String(nume);
					persoane[id_contor].id_real = id_contor;
					
					for (int z = 0; z < nr_adrese; z++) {
						String adresa = sc.readLine();	
						if (!map.containsKey(adresa)) {
							//daca nu exista adresa se adauga pur si simplu
							map.put(adresa, id_contor);
						} else {
							//daca exista verific care nume e mai mic lexicografic		
							int id_real = persoane[id_contor].id_real;
							int id_vechi = map.get(adresa);
							int id_real_vechi = persoane[id_vechi].id_real;
							if (persoane[id_real_vechi].nume.compareTo(persoane[id_real].nume)
								< 0) {
								//toate care au id-ul real de la user-ul curent, le schimbam
								for (int x1 = 0; x1 <= i; x1++) {
									if (persoane[x1].id_real == id_real) {
										persoane[x1].id_real = persoane[id_vechi].id_real;
									}
								}
							} else {
								int id_aux = persoane[id_vechi].id_real;
								//toate care au id-ul real de la user-ul gasit, le schimbam
								for (int x1 = 0; x1 <= i; x1++) {
									if (persoane[x1].id_real == id_aux) {
										persoane[x1].id_real = id_real;
									}
								}
							}
						}
					}  
					id_contor++;
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		public void getResult() {
			PrintWriter pw;
			try {
				pw = new PrintWriter(new File(OUTPUT_FILE));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			Persoanareala[] persoane_reale = new Persoanareala[n];
			Set<Map.Entry<String, Integer>> set = map.entrySet();
			Iterator<Map.Entry<String, Integer>> it = set.iterator();
			//formam noul vector de persoane_reale in functie de id_real de data asta
			while (it.hasNext()) {
				Map.Entry<String, Integer> adresa = it.next();
				int id_real = persoane[adresa.getValue()].id_real;
				if (persoane_reale[id_real] != null) {
					persoane_reale[id_real].emails.add(adresa.getKey());
					persoane_reale[id_real].contor++;
				} else {
					persoane_reale[id_real] = new Persoanareala(persoane[id_real].nume);
					persoane_reale[id_real].contor = 1;
					persoane_reale[id_real].emails.add(adresa.getKey());
				}
			}

			//ultima structura care se ordoneaza in functie de contor
			SortedSet<Persoanareala> lista_finala;
			lista_finala = new TreeSet<Persoanareala>(new Comparator<Persoanareala>() {
				public int compare(Persoanareala a1, Persoanareala a2) {
					if (a1.contor < a2.contor) {
						return -1;
					} else if (a2.contor < a1.contor) {
						return 1;
					} else {
						if (a1.nume.compareTo(a2.nume) <= 0) {
							return -1;
						}
						return 1;
					}
				}
			});
			//adaugam fiecare membru din vector nenul in lista_finala
			for (int i = 0; i < n; i++) {
				if (persoane_reale[i] != null) {
					lista_finala.add(persoane_reale[i]);
				}
			}
			//afisam lista finala
			pw.printf("%d\n", lista_finala.size());
			Iterator<Persoanareala> it2 = lista_finala.iterator();
			while (it2.hasNext()) {
				Persoanareala nou = it2.next();
				pw.printf("%s %d\n", nou.nume, nou.contor);
				Iterator<String> pt_email = nou.emails.iterator();
				while (pt_email.hasNext()) {
					pw.printf("%s\n", pt_email.next());
				}
			}
			pw.close();
		}
	}
	public static void main(String[] args) {
		new Resolve().solve();
	}
}