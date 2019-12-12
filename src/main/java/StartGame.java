import game.TicTacToeGame;

/**
 * main метод для внешнего запуска из консоли Windows
 *
 * @author Dmitry Belenov
 */

public class StartGame {
    public static void main (String[] args) {
        TicTacToeGame game = new TicTacToeGame();
        game.challenge();
    }
}
