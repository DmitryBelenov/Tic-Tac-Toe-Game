package game;

import java.util.*;

/**
 * Игра "Крестики - Нолики"
 *
 * @author Dmitry Belenov
 */

public class TicTacToeGame {
    private final String field = "  %s |  %s |  %s\n" +
            "     |     |      \n" +
            "- - - - - - - - -\n" +
            "  %s |  %s |  %s\n" +
            "     |     |\n" +
            "- - - - - - - - -\n" +
            "  %s |  %s |  %s\n" +
            "     |     |\t ";

    private final String X = "X ";
    private final String O = "O ";

    /**
     * Набор победных сетов
     *
     * */
    private final int[][] winSets = new int[][] {{0, 3, 6}, {1, 4, 7}, {2, 5, 8}, {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {0, 4, 8}, {2, 4, 6}};

    private boolean start = true;
    private boolean exit = false;

    private int steps_counter = 0;
    private int whos_step = 0;
    private String[] values = new String[] {"  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  "};

    private static Map<Integer, Integer> keyMap = new HashMap<>();

    static {
        keyMap.put(7, 0);
        keyMap.put(8, 1);
        keyMap.put(9, 2);
        keyMap.put(4, 3);
        keyMap.put(5, 4);
        keyMap.put(6, 5);
        keyMap.put(1, 6);
        keyMap.put(2, 7);
        keyMap.put(3, 8);
    }

    /**
     * Игровой челендж,
     * 10 игр, по итогу которых определяется победитель
     *
     * */
    public void challenge(){
        int counter = 0;

        while (counter < 10){
            counter++;
            System.out.println("ИГРА "+counter+":");
            TicTacToeGame tt = new TicTacToeGame();
            tt.start();
        }

        if (Counters.player_wins_counter == Counters.pc_wins_counter){
            System.out.println("ИТОГ (побед):\nигрок - "+Counters.player_wins_counter+"\nмашина - "+Counters.pc_wins_counter+"\nНИЧЬЯ!");
        } else {
            System.out.println("ИТОГ (побед):\nигрок - "+Counters.player_wins_counter
                    +"\nмашина - "+Counters.pc_wins_counter+"\nПОБЕДА "+(Counters.player_wins_counter > Counters.pc_wins_counter ? "ИГРОКА!" : "МАШИНЫ!\n(тренеруйся больше)"));
        }
        sleep(6000);
    }

    /**
     * Основной игровой метод
     *
     * определяет победителя, запускает следующий ход
     *
     * */
    private void start () {
        while (!exit) {
            if (start) {
                first_step();
            } else {
                boolean no_wins_check = true;

                for (int[] set : winSets) {
                    int win_counter_o = 0;
                    int win_counter_x = 0;

                    for (int i : set) {
                        if (values[i].equals(X)) win_counter_x++;
                        if (values[i].equals(O)) win_counter_o++;
                    }

                    if (win_counter_o == 3) {
                        System.out.println("Победа машины над разумом! Шагов: " + steps_counter);
                        exit = true;
                        no_wins_check = false;
                        Counters.pc_wins_counter++;
                        break;
                    }
                    if (win_counter_x == 3) {
                        System.out.println("Победа разума над машиной! Шагов: " + steps_counter);
                        no_wins_check = false;
                        Counters.player_wins_counter++;
                        exit = true;
                        break;
                    }
                }

                if (no_wins_check) {
                    boolean nobody_wins = Arrays.stream(values).noneMatch(s -> s.equals("  "));
                    if (nobody_wins) {
                        System.out.println("Ничья! Шагов: " + steps_counter);
                        exit = true;
                    }
                }

                if (!exit)
                    game_process();
            }
        }

        System.out.println("Игра окончена\n");
        sleep(4000);
    }

    /**
     * Переключатель логики игры компьютера
     * и игрока
     *
     * */
    private void game_process () {
        if(whos_step == 0) {
            System.out.println("Ход компьютера:\n");

            if (Arrays.asList(values).contains(O)){

                List<Integer> last = new ArrayList<>();
                for (int i = 0; i < values.length; i++) {
                    String s = values[i];
                    if (s.equals("  ")) {
                        last.add(i);
                    }
                }

                if (last.size() == 1){
                    values[last.get(0)] = O;
                    System.out.println(String.format(field, values) + "\n");
                } else {
                    boolean set_value_1 = set_value(O);

                    boolean set_value_2 = false;
                    if (!set_value_1) {
                        set_value_2 = set_value(X);
                    }

                    if (!set_value_2) {
                        for (int[] set : winSets) {
                            int win_counter_o = 0;
                            List<Integer> empty = new ArrayList<>();
                            for (int i : set) {
                                if (values[i].equals(O)) win_counter_o++;
                                if (values[i].equals("  ")) empty.add(i);
                            }

                            if (win_counter_o == 1 && empty.size() == 2) {
                                Random r = new Random();
                                int index = r.nextInt(2);

                                values[empty.get(index)] = O;
                                System.out.println(String.format(field, values) + "\n");
                                break;
                            }
                        }
                    }
                }
            } else {
                for (int[] set : winSets) {
                    int empty_counter = 0;
                    for (int i : set) {
                        if (values[i].equals("  ")) empty_counter++;
                    }

                    if (empty_counter == 3){
                        Random r = new Random();
                        int x = r.nextInt(3);

                        values[set[x]] = O;
                        System.out.println(String.format(field, values)+"\n");
                        break;
                    }
                }
            }

            steps_counter++;
            whos_step = 1;
        } else {
            System.out.println("Ваш ход:");
            Scanner scan;
            scan = new Scanner(System.in);
            int entry;

            try {
                entry = scan.nextInt();
            } catch (Exception e) {
                System.out.println("Допустимы только цифры");
                return;
            }

            if (!keyMap.keySet().contains(entry)){
                System.out.println("Допустимы только цифры 1-7 ");
                return;
            }

            int index = keyMap.get(entry);
            String cell = values[index];
            if (!cell.equals("  ")){
                System.out.println("Ячейка занята, там '"+cell.trim()+"'");
                return;
            }

            values[index] = X;
            System.out.println(String.format(field, values)+"\n");

            steps_counter++;
            whos_step = 0;
        }
    }

    /**
     * Устанавливает значение от имени компьютера
     * в логически правильную ячейку.
     * Зависит от текущего положения 'Х' и 'O' на поле
     *
     * */
    private boolean set_value(String value){
        for (int[] set : winSets) {
            int counter = 0;
            Integer empty = null;
            for (int i : set) {
                if (values[i].equals(value)) counter++;
                if (values[i].equals("  ")) empty = i;
            }

            if (counter == 2 && empty != null) {
                values[empty] = O;
                System.out.println(String.format(field, values) + "\n");
                return true;
            }
        }
        return false;
    }

    /**
     * Обработчик первого хода.
     * Случайно выбирает кто ходит и если это
     * компьютер, то заполняет случайную ячейку значением 'O'
     *
     * */
    private void first_step () {
        Scanner scan;
        Random r = new Random();
        int first = r.nextInt(2);

        if (first == 1) {
            System.out.println("Ваш ход\n" + String.format(field, values));
            scan = new Scanner(System.in);

            int entry;

            try {
                entry = scan.nextInt();
            } catch (Exception e) {
                System.out.println("Допустимы только цифры");
                return;
            }

            if (!keyMap.keySet().contains(entry)){
                System.out.println("Допустимы только цифры 1-7");
                return;
            }

            values[keyMap.get(entry)] = X;
            System.out.println(String.format(field, values)+"\n");
        } else {
            r = new Random();
            int step = r.nextInt((9 - 1) + 1) + 1;

            values[keyMap.get(step)] = O;
            System.out.println(String.format(field, values)+"\n");

            whos_step = 1;
        }

        start = false;
        steps_counter++;
    }

    /**
     * Сон для искуственного замедления игры
     * */
    private void sleep(long millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            System.out.println("Can't sleep.."+e);
        }
    }
}
