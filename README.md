# MoneyKos — Aplikasi Manajemen Keuangan Anak Kos

**Nama:** Nayla Aisha Hanifa

**NRP:** 5027251075

---
## Deskripsi Kasus

### Latar Belakang

Setelah saya menjalani sebagai anak rantau, menjadi mahasiswa perantauan bukanlah suatu hal mudah. Selain harus berjuang dengan tugas kuliah, ujian, dan kehidupan serta lingkungan yang serba baru, ada satu tantangan yang sering kali bikin pusing dan menambah pikiran yaitu tentang **mengelola uang bulanan**.



### Masalah yang Menumpuk

- Tidak Ada Catatan Real-Time: Saya tidak sadar bahwa akumulasi pengeluaran kecil seperti jajan cilok dan kopi bisa membengkak.
- Ambisi Finansial yang Terhambat: Saya ingin membeli iPad Pro M2 untuk menunjang desain UI/UX dan mengoleksi blind box Hirono, namun tabungannya tidak pernah terkumpul karena selalu terpakai untuk "keadaan darurat" yang sebenarnya bisa dihindari.
- Overbudgeting: Saya sering melampaui anggaran hiburan tanpa peringatan, sehingga uang jatah pendidikan atau kost sering terpakai.


### Konsep MoneyKos

Saya ingin merancang sebuah program berbasis OOP dengan nama **MoneyKos**. Sebuah sistem simulasi yang menjawab semua masalah saya diatas, isi dari moneykos:

1. **Dompet digital** 
Mencatat semua saldo yang ada. Baik tunai, dibank mobile ataupun digopay.


2. **Pencatatan pemasukan** 
Dari kiriman bulanan

3. **Pencatatan pengeluaran** 
Yang dikategorikan: makanan, kebutuhan kos, hiburan, dan pendidikan.

4. **Anggaran bulanan** 
Setiap kategori punya batas maksimal. Jika sudah melebihi batas, sistem langsung memberi peringatan saat itu juga.

5. **Target tabungan**  
Ada nama tujuan, nominal target, dan progress bar-nya.

6. **Validasi saldo** 
sistem menolak pengeluaran yang melebihi saldo. 

7. **Laporan bulanan** 
Breakdown pengeluaran per kategori dan evaluasi anggaran tersedia untuk setiap bulan.

### Komponen dalam Sistem

**`AnakKos`** adalah kelas inti yang mewakili saya(Nayla) sebagai pengguna. Menyimpan semua data keuangan (dompet, pemasukan, pengeluaran, tabungan, anggaran) dan menjadi pusat seluruh operasi.

**`Transaksi`** adalah abstract class sebagai kerangka umum untuk semua pencatatan uang. Dua subclass-nya (`Pemasukan` dan `Pengeluaran`) mewarisi atribut dasarnya namun memiliki atribut tambahan yang berbeda.

**`DompetDigital`** memberikan informasi sumber uang saya. Saldo dari semua dompet baik tunai ataupun nontunai menjadi saldo awal sistem.

**`AnggaranBulanan`** menyimpan batas pengeluaran per kategori menggunakan `Map`, sekaligus mencatat realisasinya. Setiap bulan memiliki instance anggarannya sendiri.

**`TargetTabungan`** menampilkan target yang ingin saya capai masing-masing dengan nominal target, jumlah yang sudah terkumpul, dan progress bar visual.

---

## Class Diagram

![gambar class diagramnya](assets/class_diagram.png)
[Kode Class Diagram (Mermaid)](./src/diagram.mmd)

---

## Kode Program Java

[Kode Program Java Lengkap](./src/App.java)

### Highlight Kode Penting

**1. `Transaksi` sebagai abstract class dengan method abstract `getTipe()`**

```java
abstract class Transaksi {
    protected int    id;
    protected String tanggal;
    protected double jumlah;
    protected String keterangan;

    public abstract String getTipe(); // wajib diimplementasikan subclass

    public void tampilkanInfo() {
        System.out.printf("    [#%02d | %-6s] %s | %-30s | Rp %,.0f%n",
                id, getTipe(), tanggal, keterangan, jumlah);
    }
}
```

Saya jadikan `Transaksi` sebagai kerangka dasar supaya `Pemasukan` dan `Pengeluaran` punya struktur yang sama. `getTipe()` saya buat abstract karena memang kedua subclass ini harus ngasih label tipenya sendiri, yang satu `"MASUK"`, yang satu `"KELUAR"`. Sementara `tampilkanInfo()` cukup saya tulis sekali di sini, otomatis kepakai di keduanya.

---

**2. `Pemasukan` dan `Pengeluaran` sebagai subclass konkret**

```java
class Pemasukan extends Transaksi {
    private String tipe; // contoh: "Kiriman Ortu"

    @Override
    public String getTipe() { return "MASUK"; }
}

class Pengeluaran extends Transaksi {
    private String kategori; // contoh: "MAKANAN", "HIBURAN"

    @Override
    public String getTipe() { return "KELUAR"; }
    public String getKategori() { return kategori; }
}
```

Masing-masing hanya perlu menambahkan satu atribut unik dan mengimplementasikan `getTipe()`. Atribut `id`, `tanggal`, `jumlah`, `keterangan`, serta `tampilkanInfo()` sudah diwarisi dari `Transaksi` tanpa perlu ditulis ulang.

---

**3. `DompetDigital` untuk mengelola saldo dari berbagai sumber**

```java
class DompetDigital {
    private String namaDompet;
    private double saldo;

    public DompetDigital(String namaDompet, double saldoAwal) {
        this.namaDompet = namaDompet;
        this.saldo      = saldoAwal;
    }
}
```

```java
// Di AnakKos — saldo awal dihitung dari semua dompet yang didaftarkan
public void tambahDompet(DompetDigital dompet) {
    daftarDompet.add(dompet);
    totalSaldo += dompet.getSaldo();
}
```

Kenyataannya uang saya itu nggak cuma di satu tempat, ada yang tunai, di BCA Mobile, sama di GoPay. Jadi saya bikin class `DompetDigital` supaya tiap sumber bisa didaftarkan satu-satu, dan saldo awalnya otomatis keitung dari semua dompet sekaligus.


---

**4. `AnggaranBulanan` menggunakan `Map` — satu instance per bulan**

```java
public void mulaiAnggaranBulan(String bulan) {
    anggaranAktif = new AnggaranBulanan();
    historiAnggaran.put(bulan, anggaranAktif); // disimpan dengan kunci nama bulan
}
```

```java
public boolean isMelebihiBatas(String kat) {
    if (!batasAnggaran.containsKey(kat)) return false;
    return realisasi.getOrDefault(kat, 0.0) > batasAnggaran.get(kat);
}
```

Ide awalnya simpel, saya pengen bisa lihat laporan Maret dan April secara terpisah tanpa datanya kecamp. Makanya tiap `AnggaranBulanan` saya simpan di Map dengan kunci nama bulannya. Tinggal panggil `"Maret 2026"` dan laporannya langsung keluar.

---

**5. Alert anggaran real-time saat pengeluaran melebihi batas**

```java
anggaranAktif.tambahRealisasi(kategori, jumlah);
if (anggaranAktif.isMelebihiBatas(kategori)) {
    double real  = anggaranAktif.getRealisasi().get(kategori);
    double batas = anggaranAktif.getBatasAnggaran().get(kategori);
    System.out.printf("  [ALERT]  Anggaran \"%s\" melebihi batas! (Rp %,.0f / Rp %,.0f)%n",
            kategori, real, batas);
}
```

Saya nggak mau alertnya baru muncul pas buka laporan akhir bulan, seperti sudah terlambat kalau munculnya diakhirnya saja. Jadi setiap kali ada pengeluaran, langsung dicek saat itu juga. Kalau udah lewat batas, langsung keluar peringatan `[ALERT]` di terminal.

---

**6. Validasi saldo sebelum mencatat pengeluaran atau menabung**

```java
public void tambahPengeluaran(...) {
    if (jumlah > totalSaldo) {
        System.out.printf("  [DITOLAK] \"%s\" — butuh Rp %,.0f, saldo hanya Rp %,.0f%n",
                keterangan, jumlah, totalSaldo);
        return;
    }
    // lanjut proses...
}
```

Ini biar saldo nggak bisa minus. Kalau pengeluaran yang diinput lebih besar dari saldo yang ada, transaksinya langsung ditolak dan sistem nampilin info butuh berapa, punya berapa. Mirip cara kerja kartu debit kalau limitnya nggak cukup. Jadi biar menyadarkan saya juga.


---

**7. Polymorphism saat menampilkan riwayat transaksi**

```java
List<Transaksi> semua = new ArrayList<>();
semua.addAll(daftarPemasukan);   // Pemasukan dimasukkan sebagai Transaksi
semua.addAll(daftarPengeluaran); // Pengeluaran dimasukkan sebagai Transaksi
semua.sort(Comparator.comparingInt(Transaksi::getId));

for (Transaksi t : semua) t.tampilkanInfo(); // polymorphism
```

Biar riwayat transaksinya keliatan urut dan rapi, saya gabung `Pemasukan` sama `Pengeluaran` ke satu list bertipe `Transaksi`, terus saya sort berdasarkan ID. Waktu `tampilkanInfo()` dipanggil, Java sendiri yang nentuin versi mana yang jalan sesuai tipe objek aslinya.

---

**8. Progress bar ASCII di `TargetTabungan`**

```java
private String buatProgressBar(double persen) {
    int terisi = (int) Math.min(persen / 100 * 20, 20);
    StringBuilder bar = new StringBuilder("[");
    for (int i = 0; i < 20; i++) bar.append(i < terisi ? "=" : "-");
    bar.append("]");
    return bar.toString();
}
```

Biar keliatan seberapa deket targetnya tanpa harus ngitung manual, saya tambahin progress bar ASCII `[========------------]`. Bagian `=` nunjukin yang udah terkumpul, sisanya `-`. Simpel tapi langsung keliatan progressnya.

---

## Screenshot Output
![alt text](assets/output.png)

---

## Prinsip OOP yang Diterapkan

### 1. Abstraction
`Transaksi` dibuat sebagai abstract class yang mendefinisikan struktur umum sebuah pencatatan uang. Method `getTipe()` bersifat abstract karena setiap subclass memiliki tipe yang berbeda. Kode yang memproses daftar transaksi tidak perlu mengetahui apakah objeknya `Pemasukan` atau `Pengeluaran`, cukup panggil `tampilkanInfo()`.

### 2. Inheritance
`Pemasukan` dan `Pengeluaran` mewarisi `Transaksi`. Keduanya otomatis mendapatkan atribut `id`, `tanggal`, `jumlah`, `keterangan`, serta method `tampilkanInfo()` tanpa menulis ulang. Masing-masing hanya menambahkan atribut yang unik: `Pemasukan` menambah `tipe`, `Pengeluaran` menambah `kategori`.

### 3. Encapsulation
Semua atribut di seluruh class menggunakan access modifier `private` atau `protected`. Saldo di `AnakKos` hanya bisa berubah melalui method resmi `tambahDompet()`, `tambahPemasukan()`, `tambahPengeluaran()`, atau `simpanKeTarget()`, tidak bisa diubah langsung dari luar class.

### 4. Polymorphism
`Pemasukan` dan `Pengeluaran` digabungkan ke satu `List<Transaksi>` saat menampilkan riwayat. Method `tampilkanInfo()` dipanggil secara seragam pada semua elemen, namun Java otomatis menjalankan versi yang tepat berdasarkan tipe objek aslinya. Begitu pula `getTipe()` yang menghasilkan `"MASUK"` atau `"KELUAR"` secara berbeda meski dipanggil dari referensi bertipe `Transaksi`.

---

## Keunikan Program

### 1. Alert anggaran muncul langsung, bukan nunggu laporan
Kebanyakan program pencatat keuangan baru kasih tau kalau anggaran kebablasan pas laporan bulanan dibuka. Di MoneyKos, pengecekan dilakukan tiap kali ada transaksi masuk, jadi [ALERT] langsung keluar saat itu juga sebelum pengeluaran makin numpuk.

### 2. Saldo awal dari banyak dompet sekaligus
Uang saya nggak cuma ada di satu tempat. Lewat DompetDigital, saya bisa daftarin Tunai, BCA Mobile, dan GoPay sekaligus. Saldo totalnya langsung keitung dari semua sumber tanpa harus input manual satu angka.

### 3. Target tabungan yang bisa dilacak satu per satu
Saya bisa nabung iPad Pro M2 sama blindbox Hirono secara bersamaan. Setiap target punya catatannya sendiri lengkap dengan progress bar dan notifikasi kalau udah tercapai.

### 4. Laporan bisa dipanggil per bulan kapan aja
Karena tiap AnggaranBulanan disimpan di Map dengan kunci nama bulan, laporan Maret nggak akan ketimpa data April. Bisa dipanggil ulang kapanpun tanpa kehilangan data bulan sebelumnya.

### 5. Progress Bar Visual untuk Tabungan Bertujuan
Setiap target tabungan ditampilkan dengan progress bar ASCII `[========---------]` yang membuat progres dapat diliat secara visual

---

