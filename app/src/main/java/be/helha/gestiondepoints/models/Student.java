package be.helha.gestiondepoints.models;

public class Student {
    private long id;
    private long classId;
    private String firstName;
    private String lastName;

    public Student(long id, long classId, String firstName, String lastName) {
        this.id = id;
        this.classId = classId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Student() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getClassId() {
        return classId;
    }

    public void setClassId(long classId) {
        this.classId = classId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
