import java.util.*;


//           MoneyKos — Manajemen Keuangan Anak Kos         
//    Aplikasi pencatatan pemasukan, pengeluaran, tabungan   
//   dan anggaran bulanan menggunakan konsep OOP Java       



// ─────────────────────────────────────────────────────────────
// ABSTRACT CLASS — Transaksi
// Kelas induk (parent) untuk semua jenis transaksi.
// Tidak bisa langsung dibuat objeknya (abstract),
// harus diwariskan dulu ke subclass.
// ─────────────────────────────────────────────────────────────
abstract class Transaksi {

    // Atribut dasar setiap transaksi.
    // 'protected' = bisa diakses oleh class ini sendiri
    // dan semua subclass-nya (Pemasukan, Pengeluaran).
    protected int    id;          // nomor unik transaksi
    protected String tanggal;     // tanggal transaksi
    protected double jumlah;      // nominal transaksi
    protected String keterangan;  // deskripsi transaksi

    // Counter statis untuk memberi ID unik otomatis ke setiap transaksi.
    // 'static' berarti satu variabel ini DIBAGI oleh semua objek Transaksi,
    // jadi ID tidak pernah duplikat selama program berjalan.
    private static int idCounter = 1;

    // Constructor: dijalankan otomatis saat objek Transaksi (atau subclass-nya) dibuat.
    // Mengisi semua atribut dasar dan menaikkan idCounter secara otomatis.
    public Transaksi(String tanggal, double jumlah, String keterangan) {
        this.id         = idCounter++; // ambil nilai counter sekarang lalu naikkan 1
        this.tanggal    = tanggal;
        this.jumlah     = jumlah;
        this.keterangan = keterangan;
    }

    // Method ABSTRACT: tidak punya isi di sini.
    // Setiap subclass WAJIB mengimplementasikan method ini.
    // Inilah inti dari konsep ABSTRACTION di OOP.
    public abstract String getTipe();

    // Method untuk mencetak detail satu transaksi ke konsol.
    // Menggunakan printf untuk format kolom yang rapi.
    // Format: [#01 | MASUK ] 01 Mar | Kiriman bulanan Mama   | Rp 2,000,000
    public void tampilkanInfo() {
        System.out.printf("    [#%02d | %-6s] %s | %-30s | Rp %,.0f%n",
                id,           // %02d = angka 2 digit (01, 02, dst.)
                getTipe(),    // %-6s = string 6 karakter rata kiri
                tanggal,
                keterangan,   // %-30s = string 30 karakter rata kiri
                jumlah);      // %,.0f = angka tanpa desimal dengan pemisah ribuan
    }

    // Getter — mengambil nilai atribut dari luar class.
    // Atribut 'protected' tidak perlu getter untuk subclass,
    // tapi tetap dibutuhkan untuk akses dari class lain.
    public double getJumlah()    { return jumlah; }
    public String getTanggal()   { return tanggal; }
    public String getKeterangan(){ return keterangan; }
    public int    getId()        { return id; }
}


// ─────────────────────────────────────────────────────────────
// SUBCLASS — Pemasukan
// Mewarisi (extends) Transaksi. Merepresentasikan uang masuk.
// Menambahkan atribut 'tipe' untuk jenis pemasukan
// (contoh: "Kiriman Ortu", "Beasiswa", "Freelance", dll.)
// ─────────────────────────────────────────────────────────────
class Pemasukan extends Transaksi {

    // Atribut tambahan khusus Pemasukan (tidak ada di parent class)
    private String tipe; // jenis sumber pemasukan

    // Constructor Pemasukan: menerima parameter dasar + tipe.
    // Memanggil super(...) untuk meneruskan data ke constructor parent (Transaksi).
    public Pemasukan(String tanggal, double jumlah, String keterangan, String tipe) {
        super(tanggal, jumlah, keterangan); // wajib dipanggil pertama kali
        this.tipe = tipe;
    }

    // Implementasi method abstract getTipe() dari parent class.
    // @Override menandai bahwa method ini menggantikan method di parent.
    // POLYMORPHISM: subclass yang berbeda mengembalikan nilai yang berbeda.
    @Override
    public String getTipe() { return "MASUK"; }

    // Getter khusus untuk atribut tipe yang hanya dimiliki Pemasukan
    public String getTipePemasukan() { return tipe; }
}


// ─────────────────────────────────────────────────────────────
// SUBCLASS — Pengeluaran
// Mewarisi (extends) Transaksi. Merepresentasikan uang keluar.
// Menambahkan atribut 'kategori' untuk pengelompokan pengeluaran
// (contoh: "MAKANAN", "HIBURAN", "PENDIDIKAN", dll.)
// ─────────────────────────────────────────────────────────────
class Pengeluaran extends Transaksi {

    // Atribut tambahan khusus Pengeluaran
    private String kategori; // kategori jenis pengeluaran

    // Constructor Pengeluaran: mirip Pemasukan, tapi parameter ke-4 adalah kategori
    public Pengeluaran(String tanggal, double jumlah, String keterangan, String kategori) {
        super(tanggal, jumlah, keterangan); // teruskan data dasar ke parent
        this.kategori = kategori;
    }

    // Override getTipe() — mengembalikan "KELUAR".
    // Bersama dengan Pemasukan yang mengembalikan "MASUK",
    // ini adalah contoh nyata POLYMORPHISM: satu method, dua perilaku berbeda.
    @Override
    public String getTipe() { return "KELUAR"; }

    // Getter untuk kategori
    public String getKategori() { return kategori; }
}


// ─────────────────────────────────────────────────────────────
// CLASS — TargetTabungan
// Merepresentasikan satu target/tujuan menabung.
// Menyimpan nama target, nominal yang diinginkan, dan
// total yang sudah terkumpul.
// ─────────────────────────────────────────────────────────────
class TargetTabungan {

    private String namaTarget;    // nama tujuan tabungan (ex: "iPad Pro M2")
    private double targetNominal; // nominal yang ingin dicapai
    private double terkumpul;     // total yang sudah ditabung

    // Constructor: terkumpul dimulai dari 0 saat target baru dibuat
    public TargetTabungan(String namaTarget, double targetNominal) {
        this.namaTarget    = namaTarget;
        this.targetNominal = targetNominal;
        this.terkumpul     = 0; // belum ada yang ditabung
    }

    // Menambahkan sejumlah uang ke tabungan ini.
    // Dipanggil setiap kali user menabung ke target ini.
    public void tambahTabungan(double jumlah) {
        this.terkumpul += jumlah; // += adalah singkatan dari: terkumpul = terkumpul + jumlah
    }

    // Menampilkan progres target tabungan dengan progress bar visual.
    public void tampilkanInfo() {
        double persen = (terkumpul / targetNominal) * 100; // hitung persentase pencapaian
        double sisa   = targetNominal - terkumpul;          // hitung sisa yang dibutuhkan

        String bar = buatProgressBar(persen); // buat visualisasi progress bar

        // Ternary operator: kalau sudah tercapai → "TERCAPAI!", kalau belum → tampilkan sisa
        String status = terkumpul >= targetNominal
                ? "TERCAPAI!"
                : "Sisa: Rp " + String.format("%,.0f", sisa);

        // Format: iPad Pro M2         | [========------------] 16.7% | Sisa: Rp 11,800,000
        System.out.printf("    %-22s | %s %.1f%% | %s%n",
                namaTarget, bar, persen, status);
    }

    // Method privat untuk membuat string progress bar visual.
    // Contoh output: [========------------] untuk 40%
    private String buatProgressBar(double persen) {
        int total  = 20; // total panjang bar (20 karakter)
        // Hitung berapa karakter '=' yang harus diisi.
        // Math.min() memastikan tidak melebihi 20 meski persen > 100
        int terisi = (int) Math.min(persen / 100 * total, total);

        StringBuilder bar = new StringBuilder("["); // mulai dengan '['
        for (int i = 0; i < total; i++) {
            bar.append(i < terisi ? "=" : "-"); // '=' = sudah, '-' = belum
        }
        bar.append("]"); // tutup dengan ']'
        return bar.toString();
    }

    // Getters untuk mengambil data dari luar class
    public String getNamaTarget()    { return namaTarget; }
    public double getTerkumpul()     { return terkumpul; }
    public double getTargetNominal() { return targetNominal; }

    // Mengecek apakah target sudah tercapai (terkumpul >= target)
    public boolean sudahTercapai()   { return terkumpul >= targetNominal; }
}


// ─────────────────────────────────────────────────────────────
// CLASS — AnggaranBulanan
// Menyimpan batas anggaran dan realisasi pengeluaran per kategori
// dalam satu bulan. Menggunakan Map (struktur key-value) agar
// fleksibel untuk kategori apapun.
// ─────────────────────────────────────────────────────────────
class AnggaranBulanan {

    // Map<String, Double>: key = nama kategori, value = nominal (Rupiah)
    private Map<String, Double> batasAnggaran; // batas maksimal per kategori
    private Map<String, Double> realisasi;     // pengeluaran aktual per kategori

    // Constructor: inisialisasi kedua Map sebagai LinkedHashMap.
    // LinkedHashMap dipilih karena MENJAGA URUTAN data sesuai urutan dimasukkan
    // (berbeda dengan HashMap yang urutannya acak).
    public AnggaranBulanan() {
        this.batasAnggaran = new LinkedHashMap<>();
        this.realisasi     = new LinkedHashMap<>();
    }

    // Mendaftarkan kategori baru dengan batas anggarannya.
    // Sekaligus inisialisasi realisasi ke 0 untuk kategori tersebut.
    public void setBatas(String kat, double limit) {
        batasAnggaran.put(kat, limit);  // daftar kategori + batas
        realisasi.put(kat, 0.0);        // mulai dengan realisasi 0
    }

    // Menambahkan jumlah pengeluaran ke realisasi kategori tertentu.
    // getOrDefault(kat, 0.0) = ambil nilai untuk 'kat', kalau tidak ada pakai 0.0
    // Ini aman untuk kategori yang belum pernah dimasukkan ke realisasi.
    public void tambahRealisasi(String kat, double jml) {
        realisasi.put(kat, realisasi.getOrDefault(kat, 0.0) + jml);
    }

    // Mengecek apakah realisasi kategori sudah melewati batasnya.
    // Mengembalikan false jika kategori tidak terdaftar (biar aman).
    public boolean isMelebihiBatas(String kat) {
        if (!batasAnggaran.containsKey(kat)) return false; // kategori tidak dikenal → abaikan
        return realisasi.getOrDefault(kat, 0.0) > batasAnggaran.get(kat);
    }

    // Mencetak laporan perbandingan anggaran vs realisasi dalam format tabel.
    public void tampilkanLaporan() {
        System.out.println("    " + "-".repeat(68)); // garis pemisah atas
        System.out.printf( "    %-18s | %-14s | %-14s | %s%n",
                "Kategori", "Anggaran", "Terpakai", "Status"); // header tabel
        System.out.println("    " + "-".repeat(68));

        // Loop setiap kategori yang sudah didaftarkan
        for (String kat : batasAnggaran.keySet()) {
            double batas  = batasAnggaran.get(kat);
            double real   = realisasi.getOrDefault(kat, 0.0);
            // Cek status: melebihi batas jika limit > 0 DAN realisasi melebihinya
            // (limit 0 = tidak dibatasi, seperti kategori TABUNGAN)
            String status = (batas > 0 && real > batas) ? "!! MELEBIHI" : "OK";
            System.out.printf("    %-18s | Rp %,10.0f | Rp %,10.0f | %s%n",
                    kat, batas, real, status);
        }
        System.out.println("    " + "-".repeat(68)); // garis pemisah bawah
    }

    // Getter untuk Map — dibutuhkan AnakKos saat menampilkan alert
    public Map<String, Double> getBatasAnggaran() { return batasAnggaran; }
    public Map<String, Double> getRealisasi()     { return realisasi; }
}


// ─────────────────────────────────────────────────────────────
// CLASS — DompetDigital
// Merepresentasikan satu dompet/rekening (Tunai, BCA, GoPay, dll.)
// Menyimpan nama dompet dan saldo saat ini.
// ─────────────────────────────────────────────────────────────
class DompetDigital {

    private String namaDompet; // nama dompet/rekening
    private double saldo;      // saldo saat ini di dompet ini

    // Constructor: buat dompet baru dengan nama dan saldo awal
    public DompetDigital(String namaDompet, double saldoAwal) {
        this.namaDompet = namaDompet;
        this.saldo      = saldoAwal;
    }

    // Method untuk menambah dan mengurangi saldo dompet ini secara individual.
    // Catatan: dalam implementasi AnakKos saat ini, method ini belum dipanggil.
    // Yang diupdate adalah totalSaldo di AnakKos, bukan saldo per dompet.
    public void tambahSaldo(double jumlah)  { this.saldo += jumlah; }
    public void kurangiSaldo(double jumlah) { this.saldo -= jumlah; }

    // Getter untuk mengambil nama dan saldo dompet dari luar class
    public String getNamaDompet() { return namaDompet; }
    public double getSaldo()      { return saldo; }
}


// ─────────────────────────────────────────────────────────────
// CLASS UTAMA — AnakKos
// Class inti yang mengelola semua data keuangan seorang user.
// Menyimpan semua daftar transaksi, target, dompet, dan anggaran.
// Semua operasi keuangan dilakukan melalui method class ini.
// ─────────────────────────────────────────────────────────────
class AnakKos {

    private String nama;        // nama pemilik akun
    private double totalSaldo;  // total saldo dari semua dompet (setelah transaksi)

    // List untuk menyimpan koleksi data yang bisa bertambah dinamis.
    // Masing-masing List hanya menyimpan satu jenis objek.
    private List<Pemasukan>      daftarPemasukan;   // riwayat semua pemasukan
    private List<Pengeluaran>    daftarPengeluaran;  // riwayat semua pengeluaran
    private List<TargetTabungan> daftarTarget;       // semua target tabungan aktif
    private List<DompetDigital>  daftarDompet;       // semua dompet yang terdaftar

    private AnggaranBulanan      anggaranAktif; // anggaran bulan yang sedang berjalan

    // Map untuk menyimpan histori anggaran per bulan.
    // Key = nama bulan (ex: "Maret 2026"), Value = objek AnggaranBulanan-nya.
    private Map<String, AnggaranBulanan> historiAnggaran;

    // Constructor: inisialisasi semua koleksi data sebagai kosong,
    // dan totalSaldo mulai dari 0 sebelum dompet ditambahkan.
    public AnakKos(String nama) {
        this.nama              = nama;
        this.totalSaldo        = 0;
        this.daftarPemasukan   = new ArrayList<>(); // List kosong siap diisi
        this.daftarPengeluaran = new ArrayList<>();
        this.daftarTarget      = new ArrayList<>();
        this.daftarDompet      = new ArrayList<>();
        this.historiAnggaran   = new LinkedHashMap<>(); // Map kosong, urutan terjaga
    }

    // ── MANAJEMEN DOMPET ─────────────────────────────────────
    // Menambahkan dompet baru ke daftar dan menjumlahkan saldo
    // awalnya ke total saldo user.
    public void tambahDompet(DompetDigital dompet) {
        daftarDompet.add(dompet);           // masuk ke list
        totalSaldo += dompet.getSaldo();    // saldo awal dompet ditambah ke total
        System.out.printf("  [OK] Dompet \"%s\" ditambahkan | Saldo: Rp %,.0f%n",
                dompet.getNamaDompet(), dompet.getSaldo());
    }

    // ── MANAJEMEN ANGGARAN ───────────────────────────────────
    // Membuat anggaran baru untuk bulan tertentu dan menyimpannya
    // ke histori agar bisa diakses kembali nanti.
    public void mulaiAnggaranBulan(String bulan) {
        anggaranAktif = new AnggaranBulanan();        // buat anggaran baru
        historiAnggaran.put(bulan, anggaranAktif);    // simpan ke histori dengan key nama bulan
        System.out.println("  [OK] Anggaran bulan " + bulan + " berhasil dibuat.");
    }

    // Menetapkan batas anggaran untuk satu kategori pada bulan aktif.
    // null check dilakukan dulu: jika anggaranAktif belum dibuat, tidak melakukan apa-apa.
    public void setAnggaranKategori(String kategori, double limit) {
        if (anggaranAktif != null) anggaranAktif.setBatas(kategori, limit);
    }

    // ── MANAJEMEN TARGET TABUNGAN ────────────────────────────
    // Membuat target tabungan baru dan menambahkannya ke daftar.
    public void tambahTarget(String namaTarget, double nominal) {
        daftarTarget.add(new TargetTabungan(namaTarget, nominal)); // buat & langsung masukkan
        System.out.printf("  [TARGET] \"%s\" ditambahkan | Nominal: Rp %,.0f%n",
                namaTarget, nominal);
    }

    // ── TAMBAH PEMASUKAN ─────────────────────────────────────
    // Mencatat uang masuk: buat objek Pemasukan, simpan ke list,
    // dan tambahkan ke total saldo.
    public void tambahPemasukan(String tanggal, double jumlah, String keterangan, String tipe) {
        Pemasukan p = new Pemasukan(tanggal, jumlah, keterangan, tipe); // buat objek baru
        daftarPemasukan.add(p);  // simpan ke riwayat
        totalSaldo += jumlah;    // saldo bertambah
        System.out.printf("  (+) %s | %-30s | +Rp %,.0f  =>  Saldo: Rp %,.0f%n",
                tanggal, keterangan, jumlah, totalSaldo);
    }

    // ── TAMBAH PENGELUARAN ───────────────────────────────────
    // Mencatat uang keluar, dengan validasi saldo dan pemantauan anggaran.
    public void tambahPengeluaran(String tanggal, double jumlah, String keterangan, String kategori) {

        // VALIDASI: cek apakah saldo mencukupi sebelum memproses
        if (jumlah > totalSaldo) {
            // Jika tidak cukup, tolak dan hentikan method dengan 'return'
            System.out.printf("  [DITOLAK] \"%s\" — butuh Rp %,.0f, saldo hanya Rp %,.0f%n",
                    keterangan, jumlah, totalSaldo);
            return; // keluar dari method, baris di bawah tidak dijalankan
        }

        // Jika saldo cukup: catat pengeluaran dan kurangi saldo
        Pengeluaran p = new Pengeluaran(tanggal, jumlah, keterangan, kategori);
        daftarPengeluaran.add(p);
        totalSaldo -= jumlah; // saldo berkurang

        // UPDATE ANGGARAN: jika anggaran bulan aktif ada, update realisasinya
        if (anggaranAktif != null) {
            anggaranAktif.tambahRealisasi(kategori, jumlah); // tambah ke realisasi kategori

            // Cek apakah sudah melebihi batas anggaran kategori ini
            if (anggaranAktif.isMelebihiBatas(kategori)) {
                double real  = anggaranAktif.getRealisasi().get(kategori);
                double batas = anggaranAktif.getBatasAnggaran().get(kategori);
                // Tampilkan peringatan jika melebihi batas
                System.out.printf("  [ALERT]  Anggaran \"%s\" melebihi batas! (Rp %,.0f / Rp %,.0f)%n",
                        kategori, real, batas);
            }
        }

        // Cetak konfirmasi pengeluaran
        System.out.printf("  [-] %s | %-30s | -Rp %,.0f  =>  Saldo: Rp %,.0f%n",
                tanggal, keterangan, jumlah, totalSaldo);
    }

    // ── SIMPAN KE TARGET TABUNGAN ────────────────────────────
    // Memindahkan sejumlah uang dari saldo ke target tabungan tertentu.
    // Mencari target berdasarkan nama, lalu mengurangi saldo.
    public void simpanKeTarget(String namaTarget, double jumlah, String tanggal) {

        // Loop mencari target yang namanya cocok
        for (TargetTabungan t : daftarTarget) {

            // equalsIgnoreCase = bandingkan string tanpa peduli huruf besar/kecil
            if (t.getNamaTarget().equalsIgnoreCase(namaTarget)) {

                // VALIDASI saldo sebelum menabung
                if (jumlah > totalSaldo) {
                    System.out.printf("  [DITOLAK] Nabung ke \"%s\" gagal — saldo tidak cukup%n", namaTarget);
                    return; // hentikan method
                }

                // Tambah uang ke objek target dan kurangi saldo
                t.tambahTabungan(jumlah);
                totalSaldo -= jumlah;

                // Tabungan juga dicatat sebagai realisasi kategori "TABUNGAN" di anggaran
                if (anggaranAktif != null) {
                    anggaranAktif.tambahRealisasi("TABUNGAN", jumlah);
                }

                // Cetak konfirmasi beserta progres tabungan saat ini
                System.out.printf("  [NABUNG] %s | -> %-22s | Rp %,.0f | Progres: %.1f%% | Saldo: Rp %,.0f%n",
                        tanggal, namaTarget, jumlah,
                        t.getTerkumpul() / t.getTargetNominal() * 100, // hitung persen progres
                        totalSaldo);

                // Cek apakah target sudah tercapai setelah tabungan ini
                if (t.sudahTercapai()) {
                    System.out.println(" YEYYYY TARGET \"" + namaTarget.toUpperCase() + "\" TERCAPAI! ");
                }

                return; // target ditemukan & diproses, hentikan loop
            }
        }

        // Jika loop selesai tanpa menemukan target → tampilkan pesan error
        System.out.println("  [!] Target \"" + namaTarget + "\" tidak ditemukan.");
    }

    // ── LAPORAN: BREAKDOWN PENGELUARAN PER KATEGORI ──────────
    // Menampilkan tabel pengeluaran per kategori beserta persentase porsinya
    // dari total keseluruhan pengeluaran bulan tertentu.
    public void tampilkanBreakdownKategori(String bulan) {
        // Ambil data anggaran bulan yang diminta dari histori
        AnggaranBulanan ab = historiAnggaran.get(bulan);
        if (ab == null) { System.out.println("    (tidak ada data)"); return; } // guard clause

        Map<String, Double> real = ab.getRealisasi(); // ambil map realisasi per kategori

        // Stream API: cara modern Java memproses koleksi.
        // .values() → ambil semua nilai (nominal)
        // .stream() → ubah jadi stream untuk diproses
        // .mapToDouble(Double::doubleValue) → konversi ke DoubleStream
        // .sum() → jumlahkan semuanya
        double grandTotal = real.values().stream().mapToDouble(Double::doubleValue).sum();

        System.out.println("    " + "-".repeat(52));
        System.out.printf( "    %-18s | %-14s | %s%n", "Kategori", "Total", "Porsi");
        System.out.println("    " + "-".repeat(52));

        // Map.Entry memungkinkan iterasi sekaligus mengakses key (nama) dan value (nominal)
        for (Map.Entry<String, Double> e : real.entrySet()) {
            if (e.getValue() > 0) { // hanya tampilkan kategori yang ada pengeluarannya
                // Hitung persentase kategori ini dari total (hindari divide-by-zero dengan cek grandTotal > 0)
                double pct = grandTotal > 0 ? (e.getValue() / grandTotal) * 100 : 0;
                System.out.printf("    %-18s | Rp %,10.0f | %.1f%%%n",
                        e.getKey(), e.getValue(), pct);
            }
        }
        System.out.println("    " + "-".repeat(52));
        System.out.printf( "    %-18s | Rp %,10.0f |%n", "TOTAL PENGELUARAN", grandTotal);
        System.out.println("    " + "-".repeat(52));
    }

    // ── LAPORAN: EVALUASI ANGGARAN ───────────────────────────
    // Menampilkan laporan perbandingan batas vs realisasi anggaran
    // untuk bulan tertentu (delegate ke method di AnggaranBulanan).
    public void tampilkanLaporanAnggaran(String bulan) {
        AnggaranBulanan ab = historiAnggaran.get(bulan);
        if (ab == null) { System.out.println("    (tidak ada data anggaran)"); return; }
        ab.tampilkanLaporan(); // panggil method laporan di objek AnggaranBulanan
    }

    // ── RINGKASAN KEUANGAN ───────────────────────────────────
    // Menampilkan rangkuman total pemasukan, pengeluaran, tabungan,
    // saldo akhir, dan status kesehatan keuangan.
    public void tampilkanRingkasan() {

        // Stream API untuk menjumlahkan semua nominal dari tiap list
        double totalMasuk  = daftarPemasukan.stream().mapToDouble(Transaksi::getJumlah).sum();
        double totalKeluar = daftarPengeluaran.stream().mapToDouble(Transaksi::getJumlah).sum();
        double totalNabung = daftarTarget.stream().mapToDouble(TargetTabungan::getTerkumpul).sum();

        // Rasio = sisa saldo dibanding total pemasukan
        // Ternary untuk hindari divide-by-zero: jika totalMasuk = 0, rasio = 0
        double rasio  = totalMasuk > 0 ? totalSaldo / totalMasuk : 0;

        // Tentukan status keuangan berdasarkan rasio:
        //   >= 30% sisa → AMAN
        //   10%-29% sisa → PERHATIAN
        //   < 10% sisa   → KRITIS
        // Ternary bersarang: bisa dibaca seperti if-else if-else
        String status = (rasio >= 0.30) ? "AMAN" : (rasio >= 0.10) ? "PERHATIAN" : "KRITIS";

        System.out.println();
        System.out.println("  ══════════════════════════════════════════════════");
        System.out.printf( "        RINGKASAN KEUANGAN %-23s%n", nama.toUpperCase());
        System.out.println("  ══════════════════════════════════════════════════");
        System.out.printf( "     Total Pemasukan    :  Rp %,20.0f    %n", totalMasuk);
        System.out.printf( "     Total Pengeluaran  :  Rp %,20.0f    %n", totalKeluar);
        System.out.printf( "     Total Ditabung     :  Rp %,20.0f    %n", totalNabung);
        System.out.printf( "     Saldo Akhir        :  Rp %,20.0f    %n", totalSaldo);
        System.out.println("  ══════════════════════════════════════════════════");
        System.out.printf( "     Status Keuangan    :  %-26s  %n", status);
    }

    // ── RIWAYAT TRANSAKSI ────────────────────────────────────
    // Menampilkan semua transaksi (pemasukan + pengeluaran)
    // diurutkan berdasarkan ID (kronologis).
    public void tampilkanRiwayatTransaksi() {

        // Buat list gabungan dari kedua daftar transaksi
        List<Transaksi> semua = new ArrayList<>();
        semua.addAll(daftarPemasukan);    // tambahkan semua Pemasukan
        semua.addAll(daftarPengeluaran);  // tambahkan semua Pengeluaran

        // Urutkan berdasarkan ID transaksi (paling kecil = paling lama)
        // Comparator.comparingInt = sorting berdasarkan nilai integer dari method getId()
        // Ini adalah contoh POLYMORPHISM: Pemasukan dan Pengeluaran diperlakukan
        // sebagai Transaksi karena keduanya mewarisi class Transaksi.
        semua.sort(Comparator.comparingInt(Transaksi::getId));

        System.out.println("    " + "-".repeat(70));
        System.out.printf( "    %-8s | %-10s | %-30s | %s%n",
                "Tipe/ID", "Tanggal", "Keterangan", "Jumlah");
        System.out.println("    " + "-".repeat(70));

        // Loop dan panggil tampilkanInfo() pada setiap transaksi.
        // getTipe() dan tampilkanInfo() bekerja secara polymorphic —
        // tiap objek tahu cara menampilkan dirinya sendiri.
        for (Transaksi t : semua) t.tampilkanInfo();

        System.out.println("    " + "-".repeat(70));
    }

    // ── TAMPILKAN TARGET TABUNGAN ────────────────────────────
    // Menampilkan progress bar semua target tabungan.
    public void tampilkanTargetTabungan() {
        if (daftarTarget.isEmpty()) { // cek dulu apakah ada target
            System.out.println("    (tidak ada target tabungan)");
            return;
        }
        // Loop dan tampilkan info progres masing-masing target
        for (TargetTabungan t : daftarTarget) t.tampilkanInfo();
    }

    // Getter untuk total saldo (dibutuhkan jika class lain perlu cek saldo)
    public double getTotalSaldo() { return totalSaldo; }
}


// ─────────────────────────────────────────────────────────────
// MAIN CLASS — App
// Titik masuk (entry point) program. Berisi method main()
// yang menjadi titik awal eksekusi Java.
// Mensimulasikan skenario keuangan Nayla selama bulan Maret 2026.
// ─────────────────────────────────────────────────────────────
public class App {

    // Helper: mencetak satu baris berisi karakter 'c' sebanyak 'n' kali
    // Contoh: garis('=', 5) → mencetak "====="
    static void garis(char c, int n) { System.out.println(String.valueOf(c).repeat(n)); }

    // Helper: mencetak header section dengan judul dan garis pemisah di bawahnya
    static void header(String judul) {
        System.out.println();
        System.out.println("  " + judul);
        garis('-', 62); // garis dengan karakter '-' sepanjang 62
    }

    // ── METHOD MAIN ──────────────────────────────────────────
    // Entry point: Java selalu mulai eksekusi dari sini.
    // String[] args = parameter dari command line (tidak dipakai di sini)
    public static void main(String[] args) {

        // ── HEADER APLIKASI ──────────────────────────────────
        garis('=', 62);
        System.out.println("     MoneyKos| Manajemen Keuangan Anak Kos");
        garis('=', 62);


        // ── [1] PROFIL PENGGUNA ───────────────────────────────
        // Membuat objek AnakKos baru untuk user Nayla Aisha.
        // Semua operasi keuangan dilakukan melalui objek 'user' ini.
        header("[1] PROFIL PENGGUNA");
        AnakKos user = new AnakKos("Nayla Aisha"); // buat objek user baru
        System.out.println("  Nama    : Nayla Aisha");
        System.out.println("  Asal    : Surabaya");
        System.out.println("  Kampus  : Institut Teknologi Sepuluh Nopember (ITS)");
        System.out.println("  Jurusan : Teknologi Informasi");


        // ── [2] SETUP DOMPET ──────────────────────────────────
        // Menambahkan 3 dompet dengan saldo awal.
        // Objek DompetDigital dibuat langsung di dalam argumen method (anonymous object).
        // Total saldo awal: 50.000 + 2.000.000 + 75.000 = Rp 2.125.000
        header("[2] SETUP DOMPET");
        user.tambahDompet(new DompetDigital("Tunai",      50_000));      // uang tunai fisik
        user.tambahDompet(new DompetDigital("BCA Mobile", 2_000_000));   // rekening bank
        user.tambahDompet(new DompetDigital("GoPay",      75_000));      // dompet digital


        // ── [3] TARGET TABUNGAN ───────────────────────────────
        // Mendaftarkan 2 target tabungan dengan nominal tujuan masing-masing.
        header("[3] TARGET TABUNGAN");
        user.tambahTarget("iPad Pro M2",      12_000_000); // target jangka panjang
        user.tambahTarget("blindbox hirono",     300_000); // target jangka pendek


        // ── [4] ANGGARAN BULAN MARET 2026 ────────────────────
        // Membuat anggaran bulan Maret dan menetapkan batas per kategori.
        // TABUNGAN diberi batas 0 = tidak ada batas anggaran untuk tabungan.
        header("[4] ANGGARAN BULAN MARET 2026");
        user.mulaiAnggaranBulan("Maret 2026");                    // inisialisasi anggaran
        user.setAnggaranKategori("MAKANAN",        500_000);      // maks belanja makanan
        user.setAnggaranKategori("KEBUTUHAN_KOST", 350_000);      // maks kebutuhan kos
        user.setAnggaranKategori("HIBURAN",        100_000);      // maks hiburan
        user.setAnggaranKategori("PENDIDIKAN",     200_000);      // maks keperluan kuliah
        user.setAnggaranKategori("TABUNGAN",             0);      // tabungan tidak dibatasi


        // ── [5] PEMASUKAN MARET 2026 ─────────────────────────
        // Satu pemasukan: kiriman bulanan dari mama.
        // Saldo setelah ini: 2.125.000 + 2.000.000 = Rp 4.125.000
        header("[5] PEMASUKAN MARET 2026");
        user.tambahPemasukan("01 Mar", 2_000_000, "Kiriman bulanan Mama", "Kiriman Ortu");


        // ── [6] PENGELUARAN MARET 2026 ───────────────────────
        // 8 transaksi pengeluaran berbagai kategori.
        // HIBURAN melebihi batas: 120.000 + 110.000 = 230.000 > batas 100.000
        // KEBUTUHAN_KOST melebihi batas: 500.000 + 310.000 = 810.000 > batas 350.000
        header("[6] PENGELUARAN MARET 2026");
        user.tambahPengeluaran("01 Mar", 500_000, "Bayar kos",             "KEBUTUHAN_KOST");
        user.tambahPengeluaran("02 Mar",  20_000, "Sarapan nasi kuning",   "MAKANAN");
        user.tambahPengeluaran("04 Mar",  45_000, "Print laporan tugas IT","PENDIDIKAN");
        user.tambahPengeluaran("08 Mar", 120_000, "Nonton bioskop",        "HIBURAN");       // akan memicu ALERT saat Game Online
        user.tambahPengeluaran("12 Mar",  90_000, "Beli buku algoritma",   "PENDIDIKAN");
        user.tambahPengeluaran("15 Mar", 150_000, "Makan seafood",         "MAKANAN");
        user.tambahPengeluaran("18 Mar", 110_000, "Game online",           "HIBURAN");       // ALERT: melebihi batas HIBURAN 100k
        user.tambahPengeluaran("20 Mar", 310_000, "Beli Sprei baru",       "KEBUTUHAN_KOST");// ALERT: melebihi batas KEBUTUHAN_KOST


        // ── [7] MENABUNG MARET 2026 ──────────────────────────
        // Menabung ke dua target:
        //   - iPad Pro M2: 200.000 dari 12.000.000 (progres 1.7%)
        //   - blindbox hirono: 300.000 dari 300.000 (TERCAPAI! 100%)
        header("[7] MENABUNG MARET 2026");
        user.simpanKeTarget("iPad Pro M2",      200_000, "25 Mar");
        user.simpanKeTarget("blindbox hirono",  300_000, "29 Mar"); // akan memicu pesan TERCAPAI


        // ── [8] LAPORAN AKHIR MARET 2026 ─────────────────────
        // Menampilkan 3 laporan: breakdown kategori, evaluasi anggaran, progress target.
        header("[8] LAPORAN AKHIR MARET 2026");

        System.out.println("  > Breakdown Pengeluaran per Kategori:");
        user.tampilkanBreakdownKategori("Maret 2026"); // tabel porsi per kategori

        System.out.println("  > Evaluasi Anggaran:");
        user.tampilkanLaporanAnggaran("Maret 2026"); // tabel batas vs terpakai

        System.out.println("  > Progress Target Tabungan:");
        user.tampilkanTargetTabungan(); // progress bar semua target


        // ── [9] RINGKASAN KEUANGAN AKHIR ─────────────────────
        // Ringkasan total dan status kesehatan keuangan Nayla bulan Maret.
        header("[9] RINGKASAN KEUANGAN AKHIR");
        user.tampilkanRingkasan(); // cetak ringkasan + status AMAN/PERHATIAN/KRITIS

        System.out.println();
        garis('=', 62); 
    }
}