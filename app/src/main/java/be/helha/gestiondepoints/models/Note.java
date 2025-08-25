package be.helha.gestiondepoints.models;

public class Note {
    private long id;
    private long studentId;  // Identifiant de l'étudiant
    private long evaluationId;  // Identifiant de l'évaluation
    private double score;  // Note de l'étudiant (arrondie à 0,5 près)

    public Note() {
    }

    public Note(int id, long studentId, long evaluationId, double score) {
        this.id = id;
        this.studentId = studentId;
        this.evaluationId = evaluationId;
        this.score = score;
    }

    // Getters et setters
    public long getId() { return id; }
    public long getStudentId() { return studentId; }
    public long getEvaluationId() { return evaluationId; }
    public double getScore() { return score; }

    public void setId(long id) { this.id = id; }
    public void setStudentId(long studentId) { this.studentId = studentId; }
    public void setEvaluationId(long evaluationId) { this.evaluationId = evaluationId; }
    public void setScore(double score) { this.score = score; }
}


