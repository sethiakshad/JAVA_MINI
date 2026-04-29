import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

interface UserActions {
    void reset();
}
interface QuizInfo {
    int getTotalQuestions();
}
abstract class User {
    protected String id;
    protected String name;
    protected String password;

    public User(String id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }
    public String getId() { return id; }
    public String getName() { return name; }
    public String getPassword() { return password; }

    public abstract String getRole();
}
abstract class SystemUser extends User {
    public SystemUser(String id, String name, String password) {
        super(id, name, password);
    }
}
class Student extends SystemUser implements UserActions {
    private double lastScore = -1;
    private double lastPercentage = -1;
    private boolean attempted = false;

    public Student(String mis, String name, String password) {
        super(mis, name, password);
    }

    public String getMis() { return getId(); }
    
    @Override
    public String getRole() { return "Student"; }

    public double getLastScore() { return lastScore; }
    public double getLastPercentage() { return lastPercentage; }
    public boolean isAttempted() { return attempted; }

    public void setResults(double score, double percentage) {
        this.lastScore = score;
        this.lastPercentage = percentage;
        this.attempted = true;
    }
    @Override
    public void reset() {
        this.lastScore = -1;
        this.lastPercentage = -1;
        this.attempted = false;
    }
    @Override
    protected void finalize() throws Throwable {
        try {
            this.id = null;
            this.name = null;
            this.password = null;
        } finally {
            super.finalize();
        }
    }
}
class Admin extends SystemUser {
    public Admin(String username, String password) {
        super(username, "Admin", password);
    }
    @Override
    public String getRole() {
        return "Admin";
    }
}

class Question {
    private String text;
    private String[] options;
    private int correctOption;

    public Question(String text, String[] options, int correctOption) {
        this.text = text;
        this.options = options;
        this.correctOption = correctOption;
    }
    public String getText() { return text; }
    public String[] getOptions() { return options; }
    public int getCorrectOption() { return correctOption; }
}
abstract class QuizBase implements QuizInfo {
    protected String title;
    protected int positivePoints;
    protected int negativePoints;
    protected int timeLimit; 
    protected boolean shuffle;

    public QuizBase(String title, int positivePoints, int negativePoints, int timeLimit, boolean shuffle) {
        this.title = title;
        this.positivePoints = positivePoints;
        this.negativePoints = negativePoints;
        this.timeLimit = timeLimit;
        this.shuffle = shuffle;
    }

    public String getTitle() { return title; }
    public int getPositivePoints() { return positivePoints; }
    public int getNegativePoints() { return negativePoints; }
    public int getTimeLimit() { return timeLimit; }

    public abstract void createQuiz();
    public abstract void displayQuiz();
}
class Quiz extends QuizBase {
    private List<Question> questions;

    public Quiz(String title, int positivePoints, int negativePoints, int timeLimit, boolean shuffle) {
        super(title, positivePoints, negativePoints, timeLimit, shuffle);
        this.questions = new ArrayList<>();
    }
    @Override
    public void createQuiz() {
    }

    @Override
    public void displayQuiz() {
        System.out.println("Quiz Title: " + title);
    }
    public void addQuestion(Question q) {
        questions.add(q);
    }
    @Override
    public int getTotalQuestions() {
        return questions.size();
    }
    public List<Question> getQuestions() {
        if (shuffle) {
            List<Question> shuffled = new ArrayList<>(questions);
            Collections.shuffle(shuffled);
            return shuffled;
        }
        return new ArrayList<>(questions);
    }
}