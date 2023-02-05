import javax.swing.*;
import java.util.Scanner;

public class MainWindow extends JFrame {
    public MainWindow(){//задаю здесь пармаетры
        setTitle("Змейка");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);// когда нажимаем на крестик чтобы закрывалось
        setSize(352,372);
        setLocation(400,400);
        add(new GameField());
        setVisible(true); //чтобы видно было его

    }
    public static void main(String[] args){
        System.out.println("Выберите режим игры: 1 - со стенками, 2 - без них");
        Scanner ss = new Scanner(System.in);
        int variant = ss.nextInt();
        GameField.setLevel(variant);
        MainWindow mw = new MainWindow();
    }
}
