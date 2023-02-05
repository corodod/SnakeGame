import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

/**
 * Created by infuntis on 15/01/17.
 */
public class GameField extends JPanel implements ActionListener{

    private final int SIZE = 320; // окошечко активное
    private final int DOT_SIZE = 16; // то сколько занимает один пиксель
    private final int ALL_DOTS = 400; // сколько ячеек помещается
    private Image dot;
    private Image apple;
    private int appleX;//значение икс яблока
    private int appleY;//
    private int[] x = new int[ALL_DOTS];// значения х координат нашей змеи, олдотс потому что столько у нас ячеек
    private int[] y = new int[ALL_DOTS];// значения у координат нашей змеи
    private int dots;// размер змейки в данный момент(количество звеньев)
    private Timer timer;//
    private boolean left = false;//
    private boolean right = true;//
    private boolean up = false;//
    private boolean down = false;//
    private boolean inGame = true;// состояние до столкновения стенки

    private static int level = 0;


    public GameField(){
        setBackground(Color.DARK_GRAY);// наше поле серое теперь
        loadImages();// добавляем картинки яблока и змейки
        initGame();// начинаем игру
        addKeyListener(new FieldKeyListener());// здесь управление
        setFocusable(true);// чтобы клавиши управляли именно нашей змейкой, подавали команды внутрь поль

    }

    public void initGame(){// метод который инициализирует начало игры
        dots = 3;// длина змеи изначально 3
        for (int i = 0; i < dots; i++) {
            x[i] = 48 - i*DOT_SIZE;// 16*3 = 48 откуда у нас змейка начинается
            y[i] = 48; //положится плашмя вдоль оси икс
        }
        timer = new Timer(150,this);// тут скорость 250 милисекунд, зис говорит что именно гэймфилд будет
        // отвечать за обработку каждого вызова таймера
        timer.start();
        createApple();
    }

    public void createApple(){// функция для создания яблока
        appleX = new Random().nextInt(20)*DOT_SIZE;// рандомим координаты нового яблока
        appleY = new Random().nextInt(20)*DOT_SIZE;
        boolean areInSnakeX = false;
        boolean areInSnakeY = false;
        for(int i = 0;i<x.length;i++){
            if(x[i] == appleX && y[i] == appleY){
                appleX = new Random().nextInt(20)*DOT_SIZE;// рандомим координаты нового яблока
                appleY = new Random().nextInt(20)*DOT_SIZE;
            }
        }
    }

    public void loadImages(){
        ImageIcon iia = new ImageIcon("ImageAppple.jpg");
        apple = iia.getImage();
        ImageIcon iid = new ImageIcon ("IMAGESNAKE.jpg");
        dot = iid.getImage();
    }

    @Override// оверайд это преопределить
    protected void paintComponent(Graphics g) {// протектед это значит что доступен только классам одного и тогоже пакета или подклассам
        super.paintComponent (g);//
        if(inGame){
            g. drawImage(apple, appleX, appleY, this);//здесь мы рисуем картинку эпл с координатами соответсвующими
            for (int i = 0; i < dots; i++) {// сколько точек столько и перерисовываем
                g. drawImage(dot, x [i],y [i], this);// здесь мы рисуем нашу змею
            }
        } else{
            String str = "Game Over";
            g.setColor(Color.white);
            g.drawString (str, 125, SIZE /2);// здесь мы выводим что конец
        }
    }

    public void move(){
        for (int i = dots; i > 0; i--) {
            x[i] = x[i-1];//здесь мы передвигаем голову а за ней все отсальные
            y[i] = y[i-1];// вторая точка на позицию третьей и тд это все точки которые не голова, а голову мы переносим туда куда у нас направление
        }
        if(left){
            x[0] -= DOT_SIZE;
        }
        if(right){
            x[0] += DOT_SIZE;
        } if(up){
            y[0] -= DOT_SIZE;
        } if(down){
            y[0] += DOT_SIZE;
        }
    }

    public void checkApple(){
        if(x[0] == appleX && y[0] == appleY){
            dots++;
            createApple();//сьели и создали новое яблоко
        }
    }

    public void checkCollisions(int level){// метод для проверки сталкивания с собой и стенками
        for (int i = dots; i >0 ; i--) {
            if(i>4 && x[0] == x[i] && y[0] == y[i]){// если ячейки совпадают то сталкивание
                inGame = false;
            }
        }

        if(x[0]>SIZE){//здесь задано чтобы не выходило за размеры поля
            if(level == 1){
                inGame = false;
            }else{
                x[0] = 0;
            }

        }
        if(x[0]<0){
            if(level == 1){
                inGame = false;
            }else{
                x[0] = SIZE;
            }
        }
        if(y[0]>SIZE){
            if(level == 1){
                inGame = false;
            }else{
                y[0] = 0;
            }
        }
        if(y[0]<0){
            if(level == 1){
                inGame = false;
            }else{
                y[0] = SIZE;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(inGame){
            checkApple();
            checkCollisions(level);
            move();
        }
        repaint();
    }

    class FieldKeyListener  extends KeyAdapter{// здесь мы подключаем управление
        @Override
        public void keyPressed (KeyEvent e) {
            super.keyPressed(e);
            int key = e.getKeyCode();// здесь какая клавиша нажата
            if(key == KeyEvent.VK_LEFT && !right){ // если нажато влево и мы не двигаемся вправо, тогда следующее
                left = true;
                up = false;
                down = false;
            }
            if(key == KeyEvent.VK_RIGHT && !left){
                right = true;
                up = false;
                down = false;
            }

            if(key == KeyEvent.VK_UP && !down){
                right = false;
                up = true;
                left = false;
            }
            if(key == KeyEvent.VK_DOWN && !up){
                right = false;
                down = true;
                left = false;
            }
        }
    }

    public static void setLevel(int level) {
        GameField.level = level;
    }
}