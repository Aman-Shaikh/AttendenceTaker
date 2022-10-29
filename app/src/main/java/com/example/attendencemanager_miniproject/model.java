package com.example.attendencemanager_miniproject;

public class model
{
  String name,email;
  String Subject,TeacherName,ClassYear,ClassSem;



    public model() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public String getTeacherName() {
        return TeacherName;
    }

    public void setTeacherName(String teacherName) {
        TeacherName = teacherName;
    }

    public String getClassYear() {
        return ClassYear;
    }

    public void setClassYear(String classYear) {
        ClassYear = classYear;
    }

    public String getClassSem() {
        return ClassSem;
    }

    public void setClassSem(String classSem) {
        ClassSem = classSem;
    }

    public model(String name, String email, String subject, String teacherName, String classYear, String classSem) {
        this.name = name;
        this.email = email;
        Subject = subject;
        TeacherName = teacherName;
        ClassYear = classYear;
        ClassSem = classSem;
    }


}
