import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

interface QuizActions {
    void createQuiz(Quiz quiz);
    void deleteQuiz();
    Quiz getCurrentQuiz();
}
interface LeaderboardActions {
    List<Student> getLeaderboard();
    void exportLeaderboardToCSV(String filename) throws IOException;
}
class QuizManager implements QuizActions, LeaderboardActions {
    private static final QuizManager instance = new QuizManager();

    private Quiz currentQuiz = null;
    private Map<String, Student> students = new HashMap<>();

    private QuizManager() {
    }
    public static QuizManager getInstance() {
        return instance;
    }
    public boolean registerStudent(String mis, String name, String password) {
        if (students.containsKey(mis)) return false;
        students.put(mis, new Student(mis, name, password));
        return true;
    }
    public Student loginStudent(String mis, String password) {
        Student s = students.get(mis);
        if (s != null && s.getPassword().equals(password)) {
            return s;
        }
        return null;
    }
    @Override
    public void createQuiz(Quiz quiz) {
        currentQuiz = quiz;
        for (Student s : students.values()) {
            s.reset();
        }
    }
    @Override
    public void deleteQuiz() {
        currentQuiz = null;
        for (Student s : students.values()) {
            s.reset();
        }
    }
    @Override
    public Quiz getCurrentQuiz() {
        return currentQuiz;
    }
    @Override
    public List<Student> getLeaderboard() {
        List<Student> leaderboard = new ArrayList<>();
        for (Student s : students.values()) {
            if (s.isAttempted()) {
                leaderboard.add(s);
            }
        }
        leaderboard.sort((s1, s2) -> Double.compare(s2.getLastScore(), s1.getLastScore()));
        return leaderboard;
    }
    @Override
    public void exportLeaderboardToCSV(String filename) throws IOException {
        List<Student> leaderboard = getLeaderboard();
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("Rank,MIS,Name,Score\n");
            int rank = 1;
            for (Student s : leaderboard) {
                writer.write(rank++ + "," + s.getMis() + "," + s.getName() + "," + 
                             String.format("%.2f", s.getLastScore()) + "\n");
            }
        }
    }
}