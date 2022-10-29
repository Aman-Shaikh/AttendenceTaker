package com.example.attendencemanager_miniproject;
public class StudentModel {


    private String studentId,subjectName;

    public StudentModel(String studentId, String subjectName, int imgId) {
        this.studentId = studentId;
        this.subjectName = subjectName;
        this.imgId = imgId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }


    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    private int imgId;
}
