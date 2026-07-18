package quiz;

import model.Question;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Stores and provides a randomized pool of questions for the quiz.
 */
public class QuestionBank {
    private final List<Question> questions;

    public QuestionBank() {
        questions = new ArrayList<>();
        loadQuestions();
    }

    private void loadQuestions() {
        questions.add(new Question("Which of the following is not a Java feature?", new String[]{"Dynamic", "Architecture Neutral", "Use of pointers", "Object-oriented"}, 2));
        questions.add(new Question("What is the return type of the hashCode() method in the Object class?", new String[]{"Object", "int", "long", "void"}, 1));
        questions.add(new Question("Which keyword is used for accessing the features of a package?", new String[]{"package", "import", "extends", "export"}, 1));
        questions.add(new Question("In java, jar stands for?", new String[]{"Java Archive Runner", "Java Application Resource", "Java Application Runner", "Java Archive"}, 3));
        questions.add(new Question("Which of these cannot be used for a variable name in Java?", new String[]{"identifier", "keyword", "identifier & keyword", "none of the mentioned"}, 1));
        questions.add(new Question("What is the extension of java code files?", new String[]{".js", ".txt", ".class", ".java"}, 3));
        questions.add(new Question("Which environment variable is used to set the java path?", new String[]{"MAVEN_PATH", "JavaPATH", "JAVA", "JAVA_HOME"}, 3));
        questions.add(new Question("Which of the following is not an OOPS concept in Java?", new String[]{"Polymorphism", "Inheritance", "Compilation", "Encapsulation"}, 2));
        questions.add(new Question("What is it called if an object has its own lifecycle and there is no owner?", new String[]{"Aggregation", "Composition", "Encapsulation", "Association"}, 3));
        questions.add(new Question("What is the size of float and double in java?", new String[]{"32 and 64", "32 and 32", "64 and 64", "64 and 32"}, 0));
        questions.add(new Question("When is the object created with new keyword?", new String[]{"At run time", "At compile time", "Depends on the code", "None"}, 0));
        questions.add(new Question("Identify the incorrect Java feature.", new String[]{"Object-oriented", "Use of pointers", "Dynamic", "Architectural neutral"}, 1));
        questions.add(new Question("Which of these packages contains the exception Stack Overflow in Java?", new String[]{"java.io", "java.system", "java.lang", "java.util"}, 2));
        questions.add(new Question("Which statement is true about Java?", new String[]{"Java is a sequence-dependent programming language", "Java is a code dependent programming language", "Java is a platform-dependent programming language", "Java is a platform-independent programming language"}, 3));
        questions.add(new Question("Which component is used to compile, debug and execute the java programs?", new String[]{"JRE", "JIT", "JDK", "JVM"}, 2));
        questions.add(new Question("Which of these keywords is used to define interfaces in Java?", new String[]{"intf", "Intf", "interface", "Interface"}, 2));
        questions.add(new Question("Which of the following is a superclass of every class in Java?", new String[]{"ArrayList", "Abstract class", "Object class", "String"}, 2));
        questions.add(new Question("Which of the following is not an access modifier?", new String[]{"Protected", "Void", "Public", "Private"}, 1));
        questions.add(new Question("What is the extension of compiled java classes?", new String[]{".txt", ".js", ".class", ".java"}, 2));
        questions.add(new Question("Which exception is thrown when java is out of memory?", new String[]{"MemoryError", "OutOfMemoryError", "MemoryOutOfBoundsException", "MemoryFullException"}, 1));
    }

    public List<Question> getRandomQuestions(int count) {
        List<Question> shuffled = new ArrayList<>(questions);
        Collections.shuffle(shuffled);
        return shuffled.subList(0, Math.min(count, shuffled.size()));
    }
}