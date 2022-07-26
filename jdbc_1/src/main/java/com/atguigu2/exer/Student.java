package com.atguigu2.exer;

/**
 * @Description
 * @Author lipeng
 * @create 2022/7/18
 */
public class Student {

    private int flowID;
    private int type;
    private String idCard;
    private  String examCard;
    private String studentName;
    private  String location;
    private int grade;

    public Student() {
    }

    public Student(int flowID, int type, String idCard, String examCard, String studentName, String location, int grade) {
        this.flowID = flowID;
        this.type = type;
        this.idCard = idCard;
        this.examCard = examCard;
        this.studentName = studentName;
        this.location = location;
        this.grade = grade;
    }

    public int getFlowID() {
        return flowID;
    }

    public void setFlowID(int flowID) {
        this.flowID = flowID;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getExamCard() {
        return examCard;
    }

    public void setExamCard(String examCard) {
        this.examCard = examCard;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        System.out.println("======查询结果=========");
//        return "Student{" +
//                "flowID=" + flowID +
//                ", type=" + type +
//                ", idCard='" + idCard + '\'' +
//                ", examCard='" + examCard + '\'' +
//                ", studentName='" + studentName + '\'' +
//                ", location='" + location + '\'' +
//                ", grade=" + grade +
//                '}';
        return info();
    }

    private String info() {
        return "流水号： "+flowID+"\n"+
                "四级/六级："+type+"\n"+
                "身份证号："+idCard+"\n"+
                "准考证号："+examCard+"\n"+
                "学生姓名："+studentName+"\n"+
                "区域："+location+"\n"+
                "成绩："+grade;
    }
}
