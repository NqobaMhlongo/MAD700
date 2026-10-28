package com.example.smartpantrymanager.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/** Fills an EMPTY pantry table with test ingredients. Finds the table/columns by itself. */
public final class PantrySeeder {

    private static final String TAG = "PantrySeeder";

    // name, quantity, unit, expiry offset in days (null = no expiry)
    private static final Object[][] ITEMS = {
            {"Eggs", 6.0, "pieces", 0},
            {"Tomatoes", 4.0, "pieces", null},
            {"Oil", 100.0, "ml", null},
            {"Pasta", 500.0, "g", null},
            {"Potatoes", 1.0, "kg", null},
            {"Salt", 100.0, "g", null},
            {"Bread", 8.0, "slices", -1},
            {"Cheese", 200.0, "g", 20},
            {"Rice", 500.0, "g", null},
            {"Carrots", 3.0, "pieces", 7},
            {"Carrots", 300.0, "g", null},
            {"Garlic", 6.0, "cloves", null},
            {"Milk", 1.0, "l", 2},
            {"Butter", 250.0, "g", 30},
            {"Flour", 500.0, "g", null},
            {"Onions", 3.0, "pieces", null},
            {"Bananas", 4.0, "pieces", 3},
            {"Water", 1000.0, "ml", null},
            {"Oats", 500.0, "g", null},
    };

    private PantrySeeder() { }

    public static void seed(SQLiteDatabase db) {
        try {
            String table = findTable(db);
            if (table == null) {
                Log.w(TAG, "No pantry/inventory table found yet - nothing seeded");
                return;
            }
            if (DatabaseUtils.queryNumEntries(db, table) > 0) {
                Log.i(TAG, table + " already has rows - nothing seeded");
                return;
            }
            List<String[]> cols = describe(db, table);
            int inserted = 0;
            for (Object[] item : ITEMS) {
                ContentValues v = new ContentValues();
                for (String[] col : cols) {
                    String name = col[0];
                    String type = col[1].toUpperCase(Locale.ROOT);
                    boolean notNull = "1".equals(col[2]);
                    boolean hasDefault = col[3] != null;
                    boolean pk = !"0".equals(col[4]);
                    String n = name.toLowerCase(Locale.ROOT);
                    if (pk) {
                        continue;
                    } else if (n.contains("unit")) {
                        v.put(name, (String) item[2]);
                    } else if (n.contains("name")) {
                        v.put(name, (String) item[0]);
                    } else if (n.contains("quant") || n.contains("amount") || n.contains("qty")) {
                        double q = (Double) item[1];
                        if (type.contains("INT")) v.put(name, Math.round(q));
                        else if (type.contains("CHAR") || type.contains("TEXT")) v.put(name, String.valueOf(q));
                        else v.put(name, q);
                    } else if (n.contains("expir") || n.contains("best_before")) {
                        Integer off = (Integer) item[3];
                        if (off == null && !notNull) continue;
                        Calendar c = Calendar.getInstance();
                        c.add(Calendar.DAY_OF_MONTH, off == null ? 30 : off);
                        if (type.contains("INT")) v.put(name, c.getTimeInMillis());
                        else v.put(name, new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(c.getTime()));
                    } else if (notNull && !hasDefault) {
                        if (type.contains("INT") || type.contains("REAL") || type.contains("NUM")) v.put(name, 0);
                        else v.put(name, "");
                    }
                }
                if (db.insert(table, null, v) != -1) inserted++;
            }
            Log.i(TAG, "Seeded " + inserted + " rows into " + table);
        } catch (Exception e) {
            Log.e(TAG, "Pantry seeding failed (app continues normally)", e);
        }
    }

    private static String findTable(SQLiteDatabase db) {
        String best = null;
        int bestRank = 99;
        try (Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null)) {
            while (c.moveToNext()) {
                String name = c.getString(0);
                String l = name.toLowerCase(Locale.ROOT);
                if (l.startsWith("sqlite_") || l.startsWith("android_") || l.contains("recipe")) continue;
                int rank = l.contains("pantry") ? 0 : l.contains("inventory") ? 1
                        : l.contains("ingredient") ? 2 : 99;
                if (rank < bestRank) { best = name; bestRank = rank; }
            }
        }
        return best;
    }

    /** @return list of {name, type, notnull, default, pk} from PRAGMA table_info */
    private static List<String[]> describe(SQLiteDatabase db, String table) {
        List<String[]> cols = new ArrayList<>();
        try (Cursor c = db.rawQuery("PRAGMA table_info(" + table + ")", null)) {
            while (c.moveToNext()) {
                cols.add(new String[]{c.getString(1), c.getString(2) == null ? "" : c.getString(2),
                        c.getString(3), c.getString(4), c.getString(5)});
            }
        }
        return cols;
    }
}