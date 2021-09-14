import domain.Grid;
import domain.Point;
import opponent.Opponent;

public class Main {

    public static void main(String[] args) {
        Opponent op = new Opponent() {
            @Override
            public Point move(Grid grid) {
                return null;
            }
        };
        System.out.println(op);
        // Load CLI app
    }
}
