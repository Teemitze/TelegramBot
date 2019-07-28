package food;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class Menu {
    public void addFood(String food) {
        try {
            FileWriter writer = new FileWriter("resources/food.txt", true);
            writer.write(food + '\n');
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeFood(int index) {
        try {
            ArrayList<String> menu = getAllFood();
            FileWriter writer = new FileWriter("resources/food.txt", false);
            menu.remove(index - 1);

            for (String s: menu) {
                writer.write(s + '\n');
            }

            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public ArrayList<String> getAllFood() {
        ArrayList<String> menu = new ArrayList<>();

        try {
            FileInputStream fstream = new FileInputStream("resources/food.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            String strLine;
            while ((strLine = br.readLine()) != null) {
                menu.add(strLine);
                System.out.println(strLine);
            }
        }catch (IOException e) {
            System.out.println("Ошибка");
        }

        return menu;

    }

    public String getOneFood() {
        int sizeListFood = 0;
        ArrayList<String> foods = new ArrayList<>();

        try {
            FileInputStream fstream = new FileInputStream("resources/food.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            String strLine;
            while ((strLine = br.readLine()) != null) {
                foods.add(strLine);
            }
                sizeListFood = foods.size();
        } catch (IOException e) {
            System.out.println("Ошибка");
        }

        Random random = new Random();

            String result = foods.get(random.nextInt(sizeListFood));
            System.out.println(result);

        return result;
    }

    public static void main(String[] args) {
        new Menu().removeFood(23);
    }
}
