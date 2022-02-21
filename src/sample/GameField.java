package sample;

public class GameField {

    private final static int ROW_FIELD = 9;
    private final static int COL_FIELD = 9;
    private final static int NUM_BOMBS = 10;

    private int[][] bombs;
    private char[][] Field;

    public static int getRowField() {
        return ROW_FIELD;
    }

    public static int getColField() {
        return COL_FIELD;
    }

    public static int getNumBombs() {
        return NUM_BOMBS;
    }

    public GameField(int init_i, int init_j){

        bombs = new int[NUM_BOMBS][2];
        Field = new char[ROW_FIELD][COL_FIELD];

        int NumBombs = 0;

        int temp_j;
        int temp_i;

        for(int i = 0; i < bombs.length; i++) {

            temp_j = COL_FIELD;//first init
            temp_i = ROW_FIELD;//first init

            boolean isNotNewBomb = true; // repeated

            while(isNotNewBomb) {

                while (temp_j > COL_FIELD - 1) {

                    temp_j = (int) (Math.random() * 10);

                    while (temp_i > ROW_FIELD - 1 || (checkInitCells(init_i, init_j, temp_i, temp_j, Field.length)))
                        temp_i = (int) (Math.random() * 10);

                }

                isNotNewBomb = false;
                for(int it = 0; it < NumBombs; it++){

                    if(bombs[it][0] == temp_i && bombs[it][1] == temp_j){
                        isNotNewBomb = true;
                        temp_j = 9;
                        break;
                    }

                }

                if (!isNotNewBomb){

                    NumBombs++;

                    bombs[i][1] = temp_j;
                    bombs[i][0] = temp_i;

                    Field[temp_i][temp_j] = 'X';
                }

            }

        }

        // Definition cells of Field
        defineCellsOfField();

        //Checking data
        /*System.out.println("Field:");
        for(int i = 0; i < Field.length; i++){
            for(int j = 0; j < Field[0].length; j++){
                System.out.print(Field[j][i] + "  ");
            }
            System.out.println();
        }*/

    }

    public char[][] getField() {
        return Field;
    }

    private static boolean checkInitCells(int init_i, int init_j, int temp_i, int temp_j, int lengthField){

        boolean isMatched = false;

        if( init_i - 1 >= 0 ){

            try {
                if (temp_i == init_i - 1 && temp_j == init_j - 1)
                    return true;
            }catch (Exception ex){}

            try {
                if (temp_i == init_i - 1 && temp_j == init_j)
                    return true;
            }catch (Exception ex){}

            try {
                if (temp_i == init_i - 1 && temp_j == init_j + 1)
                    return true;
            }catch (Exception ex){}

        }


        try {
            if (temp_i == init_i && temp_j == init_j - 1)
                return true;
        }catch (Exception ex){}

        try {
            if (temp_i == init_i && temp_j == init_j)
                return true;
        }catch (Exception ex){}

        try {
            if (temp_i == init_i && temp_j == init_j + 1)
                return true;
        }catch (Exception ex){}


        if(init_i + 1 <= lengthField - 1){

            try {
                if (temp_i == init_i + 1 && temp_j == init_j - 1)
                    return true;
            }catch (Exception ex){}

            try {
                if (temp_i == init_i + 1 && temp_j == init_j)
                    return true;
            }catch (Exception ex){}

            try {
                if (temp_i == init_i + 1 && temp_j == init_j + 1)
                    return true;
            }catch (Exception ex){}

        }

        return isMatched;
    }

    private void defineCellsOfField(){

        int countBombs;
        for(int i = 0; i < Field.length; i++){
            for(int j = 0; j < Field[0].length; j++){

                if(Field[i][j] != 'X'){

                    countBombs = 0;

                    if( i - 1 >= 0 ){

                        try {
                            if (Field[i - 1][j - 1] == 'X')
                                countBombs++;
                        }catch (Exception ex){}

                        try {
                            if (Field[i - 1][j] == 'X')
                                countBombs++;
                        }catch (Exception ex){}

                        try {
                            if (Field[i - 1][j + 1] == 'X')
                                countBombs++;
                        }catch (Exception ex){}

                    }

                    try {
                        if (Field[i][j - 1] == 'X')
                            countBombs++;
                    }catch (Exception ex){}

                    try {
                        if (Field[i][j + 1] == 'X')
                            countBombs++;
                    }catch (Exception ex){}

                    if(i + 1 <= Field.length - 1){

                        try {
                            if (Field[i + 1][j - 1] == 'X')
                                countBombs++;
                        }catch (Exception ex){}

                        try {
                            if (Field[i + 1][j] == 'X')
                                countBombs++;
                        }catch (Exception ex){}

                        try {
                            if (Field[i + 1][j + 1] == 'X')
                                countBombs++;
                        }catch (Exception ex){}

                    }

                    try {
                        Field[i][j] = Integer.toString(countBombs).charAt(0);
                    }catch (Exception ex){}

                }

            }
        }

    }

}
