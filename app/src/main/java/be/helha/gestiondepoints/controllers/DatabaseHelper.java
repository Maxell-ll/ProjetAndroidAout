package be.helha.gestiondepoints.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import be.helha.gestiondepoints.models.Class;
import be.helha.gestiondepoints.models.Evaluation;
import be.helha.gestiondepoints.models.Note;
import be.helha.gestiondepoints.models.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "gestion_points.db";
    private static final int DATABASE_VERSION = 16;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE `Evaluation` (" +
                "`id` INTEGER PRIMARY KEY," +
                "`name` TEXT NOT NULL," +
                "`parent_id` BIGINT NOT NULL," +
                "`class_id` BIGINT NOT NULL," +
                "`points_max` BIGINT NOT NULL," +
                "FOREIGN KEY(`parent_id`) REFERENCES `Evaluation`(`id`)," +
                "FOREIGN KEY(`class_id`) REFERENCES `Class`(`id`)" +
                ");");

        db.execSQL("CREATE TABLE `Student` (" +
                "`id` INTEGER PRIMARY KEY AUTOINCREMENT," +
                "`class_id` BIGINT NOT NULL," +
                "`first_name` TEXT NOT NULL," +
                "`last_name` TEXT NOT NULL," +
                "FOREIGN KEY(`class_id`) REFERENCES `Class`(`id`)" +
                ");");

        db.execSQL("CREATE TABLE `Class` (" +
                "`id` INTEGER PRIMARY KEY AUTOINCREMENT," +
                "`name` TEXT NOT NULL" +
                ");");

        db.execSQL("CREATE TABLE `Note` (" +
                "`eval_id` BIGINT NOT NULL," +
                "`student_id` BIGINT NOT NULL," +
                "`note` DOUBLE NOT NULL," +
                "PRIMARY KEY(`eval_id`, `student_id`)," +
                "FOREIGN KEY(`eval_id`) REFERENCES `Evaluation`(`id`)," +
                "FOREIGN KEY(`student_id`) REFERENCES `Student`(`id`)" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS `Note`;");
        db.execSQL("DROP TABLE IF EXISTS `Evaluation`;");
        db.execSQL("DROP TABLE IF EXISTS `Student`;");
        db.execSQL("DROP TABLE IF EXISTS `Class`;");
        onCreate(db);
    }

    public void insertClass(Class c) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", c.getName());
        values.putNull("id");
        db.insert("Class", null, values);
    }

    public List<Class> getAllClasses() {
        List<Class> classList = new ArrayList<>();
        String selectQuery = "SELECT * FROM Class";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                //permet de récupérer les données des classes et de les afficher dans la liste
                Class c = new Class();
                c.setId(cursor.getLong(cursor.getColumnIndexOrThrow("id")));
                c.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                classList.add(c);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return classList;
    }

    public Class getClassesById(long classId) {
        String selectQuery = "SELECT * FROM Class WHERE id = " + classId;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        Class c = new Class();
        if (cursor.moveToFirst()) {
            c.setId(cursor.getLong(cursor.getColumnIndexOrThrow("id")));
            c.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
        }
        cursor.close();
        return c;
    }

    public void insertStudent(Student s) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("class_id", s.getClassId());
        values.put("first_name", s.getFirstName());
        values.put("last_name", s.getLastName());
        db.insert("Student", null, values);
    }

    public List<Student> getStudentsByClassId(long classId) {
        List<Student> studentList = new ArrayList<>();
        String selectQuery = "SELECT * FROM Student WHERE class_id = " + classId;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Student s = new Student();
                s.setId(cursor.getLong(cursor.getColumnIndexOrThrow("id")));
                s.setClassId(cursor.getLong(cursor.getColumnIndexOrThrow("class_id")));
                s.setFirstName(cursor.getString(cursor.getColumnIndexOrThrow("first_name")));
                s.setLastName(cursor.getString(cursor.getColumnIndexOrThrow("last_name")));
                studentList.add(s);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return studentList;
    }

    public List<Evaluation> getEvaluationsByClassId(long classId) {
        List<Evaluation> evaluationList = new ArrayList<>();
        String selectQuery = "SELECT * FROM Evaluation WHERE class_id = " + classId;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Evaluation e = new Evaluation();
                e.setId(cursor.getLong(cursor.getColumnIndexOrThrow("id")));
                e.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                e.setParentEvaluationId(cursor.getLong(cursor.getColumnIndexOrThrow("parent_id")));
                e.setClassId(cursor.getLong(cursor.getColumnIndexOrThrow("class_id")));
                e.setMaxPoints(cursor.getDouble(cursor.getColumnIndexOrThrow("points_max")));
                evaluationList.add(e);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return evaluationList;
    }

    public void insertCourse(Evaluation e) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", generateId());
        values.put("name", e.getName());
        values.put("parent_id", e.getParentEvaluationId());
        values.put("class_id", e.getClassId());
        values.put("points_max", e.getMaxPoints());
        db.insert("Evaluation", null, values);
    }

    public Evaluation getEvaluationById(long evaluationId) {
        String selectQuery = "SELECT * FROM Evaluation WHERE id = " + evaluationId;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        Evaluation e = new Evaluation();
        if (cursor.moveToFirst()) {
            e.setId(cursor.getLong(cursor.getColumnIndexOrThrow("id")));
            e.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
            e.setParentEvaluationId(cursor.getLong(cursor.getColumnIndexOrThrow("parent_id")));
            e.setClassId(cursor.getLong(cursor.getColumnIndexOrThrow("class_id")));
            e.setMaxPoints(cursor.getDouble(cursor.getColumnIndexOrThrow("points_max")));
        }
        cursor.close();
        return e;
    }

    public void upsertNoteForEvaluation(Note n) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("eval_id", n.getEvaluationId());
        values.put("student_id", n.getStudentId());
        values.put("note", n.getScore());
        db.insertWithOnConflict("Note", null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public void deleteNoteForEvaluation(long evaluationId, long studentId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Note", "eval_id = ? AND student_id = ?", new String[]{String.valueOf(evaluationId), String.valueOf(studentId)});
    }

    public List<Note> getNotesByEvaluationId(long evaluationId) {
        List<Note> noteList = new ArrayList<>();
        String selectQuery = "SELECT * FROM Note WHERE eval_id = " + evaluationId;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Note n = new Note();
                n.setEvaluationId(cursor.getLong(cursor.getColumnIndexOrThrow("eval_id")));
                n.setStudentId(cursor.getLong(cursor.getColumnIndexOrThrow("student_id")));
                n.setScore(cursor.getDouble(cursor.getColumnIndexOrThrow("note")));
                noteList.add(n);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return noteList;
    }

    public boolean isParentEvaluation(long evaluationId) {
        String selectQuery = "SELECT * FROM Evaluation WHERE parent_id = " + evaluationId;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        boolean isParent = false;
        if (cursor.moveToFirst()) {
            isParent = true;
        }
        cursor.close();
        return isParent;
    }

    public double calculateAverageForStudent(long evaluationId, long studentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        double totalScore = 0.0;
        double totalWeight = 0.0;

        List<Evaluation> evaluations = getAllSubEvaluations(evaluationId);

        for (Evaluation eval : evaluations) {
            String selectQuery = "SELECT note FROM Note WHERE eval_id = ? AND student_id = ?";
            Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(eval.getId()), String.valueOf(studentId)});

            if (cursor.moveToFirst()) {
                double note = cursor.getDouble(cursor.getColumnIndexOrThrow("note"));
                double maxPoints = eval.getMaxPoints();

                totalScore += note;
                totalWeight += maxPoints;
            }
            cursor.close();
        }

        return totalWeight > 0 ? totalScore / totalWeight : 0.0;
    }

    private List<Evaluation> getAllSubEvaluations(long parentId) {
        List<Evaluation> evaluations = new ArrayList<>();
        String selectQuery = "SELECT * FROM Evaluation WHERE parent_id = ?";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(parentId)});

        while (cursor.moveToNext()) {
            Evaluation eval = new Evaluation();
            eval.setId(cursor.getLong(cursor.getColumnIndexOrThrow("id")));
            eval.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
            eval.setParentEvaluationId(cursor.getLong(cursor.getColumnIndexOrThrow("parent_id")));
            eval.setClassId(cursor.getLong(cursor.getColumnIndexOrThrow("class_id")));
            eval.setMaxPoints(cursor.getDouble(cursor.getColumnIndexOrThrow("points_max")));
            evaluations.add(eval);
        }
        cursor.close();
        return evaluations;
    }

    private long generateId() {
        return UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
    }
}
