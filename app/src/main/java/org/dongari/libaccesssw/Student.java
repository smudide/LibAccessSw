package org.dongari.libaccesssw;

/**
 * Created by kmudide on 11/16/2015.
 */
public class Student {
    public String StudentFirstName;
    public String StudentSurname;
    public String StudentClass;

    public Student() {
        StudentFirstName = new String("");
        StudentSurname = new String("");
        StudentClass = new String("");
    }

    public Student(String studentFirstName, String studentSurname, String studentClass) {
        StudentFirstName = studentFirstName;
        StudentSurname = studentSurname;
        StudentClass = studentClass;
    }

    public String toString() {
        String str = new String("");
        if (StudentSurname.length() != 0) {
            str += StudentSurname;
        }
        if (StudentFirstName.length() != 0) {
            if (StudentSurname.length() != 0) {
                str += ", ";
            }
            str += StudentFirstName;
        }
        if ((StudentClass.length() != 0) && (!StudentClass.equals(new String("0")))){
            if ((StudentSurname.length() != 0) || (StudentFirstName.length() != 0)){
                str += " - ";
            }
            str += StudentClass;
        }
        str += "\n";

        return str;
    }

    public void splitStringIntoStudent(String studentStr) {
        StudentSurname = studentStr.split(", ")[0];
        StudentFirstName = studentStr.split(", ")[1].split(" - ")[0];
        StudentClass = studentStr.split(" - ")[1];
    }
}
