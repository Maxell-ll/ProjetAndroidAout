package be.helha.gestiondepoints.models;

public class Evaluation {
    private long id;
    private String name;
    private double maxPoints;
    private long classId;
    private long parentEvaluationId;

    public Evaluation(){
    }

    public Evaluation(long id, String name, double maxPoints, long classId) {
        this.id = id;
        this.name = name;
        this.maxPoints = maxPoints;
        this.classId = classId;
        this.parentEvaluationId = id;
    }

    public Evaluation(long id, String name, double maxPoints, long classId, long parentEvaluationId) {
        this.id = id;
        this.name = name;
        this.maxPoints = maxPoints;
        this.classId = classId;
        this.parentEvaluationId = parentEvaluationId;
    }

    // Getters et setters
    public long getId() { return id; }
    public String getName() { return name; }
    public double getMaxPoints() { return maxPoints; }
    public long getClassId() { return classId; }
    public long getParentEvaluationId() { return parentEvaluationId; }

    public void setId(long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setMaxPoints(double maxPoints) { this.maxPoints = maxPoints; }
    public void setClassId(long classId) { this.classId = classId; }
    public void setParentEvaluationId(long parentEvaluationId) { this.parentEvaluationId = parentEvaluationId; }
}
