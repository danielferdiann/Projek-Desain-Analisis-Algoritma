# Sudoku Solver: Recursive Backtracking with MRV Optimization

Proyek ini adalah implementasi penyelesaian puzzle Sudoku 9x9 menggunakan bahasa pemrograman **Java**. Fokus utama dari proyek ini adalah membandingkan efisiensi antara algoritma **Recursive Backtracking** standar dengan optimasi heuristik **Minimum Remaining Values (MRV)** dalam kerangka *Constraint Satisfaction Problem* (CSP).

## 🚀 Fitur Utama
- **Backtracking Algorithm:** Implementasi Depth First Search (DFS) untuk mencari solusi secara sistematis.
- **MRV Heuristic:** Optimasi untuk memilih sel dengan jumlah kemungkinan angka paling sedikit guna mempercepat pencarian solusi.
- **Analisis Performa:** Membandingkan waktu eksekusi (nanodetik) dan jumlah langkah rekursi antara metode standar dan MRV.
- **Java Swing GUI:** Antarmuka pengguna grafis untuk memudahkan input dan visualisasi penyelesaian Sudoku.

## 📊 Hasil Penelitian
Berdasarkan pengujian pada berbagai tingkat kesulitan, metode **MRV** terbukti jauh lebih efisien dibandingkan backtracking standar:
- **Reduksi Langkah Rekursi:** Hingga **99,92%** lebih sedikit pada puzzle sulit.
- **Kecepatan Eksekusi:** Peningkatan performa hingga **98,88%**.

## 🛠️ Teknologi & Tools
- **Bahasa Pemrograman:** Java (JDK 17 atau lebih baru)
- **Build Tool:** Maven
- **IDE:** IntelliJ IDEA / VS Code

## 📁 Struktur Folder
```text
SudokuSolver/
├── src/                # Kode sumber (Main logic & GUI)
├── .mvn/               # Maven wrapper
├── pom.xml             # Konfigurasi dependensi Maven
├── .gitignore          # File untuk mengabaikan folder target/ dan .idea/
└── README.md           # Dokumentasi proyek
